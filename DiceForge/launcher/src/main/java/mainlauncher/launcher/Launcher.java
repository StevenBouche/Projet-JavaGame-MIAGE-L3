package mainlauncher.launcher;

import client.game.BotVersions;
import server.ConfigurationServer;
import server.StateServer;
import server.Server;
import client.Client;
import server.statistics.StatisticsInGame;
import share.config.ConfigGame;
import share.utils.Printer;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type Launcher.
 */
public class Launcher implements Callable<StatisticsInGame> {

    /**
     * port sur lequel le serveur ecoute
     */
    public int port;
    /** le server de jeux*/
    private Server defaultServer;
    /** Thread hebergeant le server de jeux*/
    private Thread threadServer;

    Map<BotVersions, Integer> modeClients;

    /** Temps du debut du lancement du launcher*/
    private long startTime;

    /**
     * Get start time long.
     *
     * @return the long
     */
    public long getStartTime(){
        return this.startTime;
    }

    /** Etat actuel du launcher evolue au cour du temps*/
    private StateLauncher state;

    private void setState(StateLauncher s){
        this.state = s;
        Printer.getInstance().logLauncher("STATE : "+this.state.toString()); //Création thread server et le run
    }

    /** le nombre de partie que le launcher doit executer*/
    private int nbGame;

    public int getNbGame(){
        return this.nbGame;
    }
    public int getNbGameExecute(){
        return this.defaultServer.getCurrentNbGameExecuted();
    }

    /** verrou permettant la syncronisation*/
    private final Lock lock = new ReentrantLock();
    private final Condition serverStart = lock.newCondition();
    private final Condition clientStart = lock.newCondition();
    private final Condition gameFinish = lock.newCondition();

    private HashMap<StateLauncher, Condition> listWaitState;

    /**
     * liste composer de tous les etats possible du launcher avec pour chacune une condition utilisable pour la syncronisation  @param port the port
     *
     * @param nbGame                 the nb game
     * @param lock                   the lock
     * @param conditionLauncherStart the condition launcher start
     */
    public Launcher(int port, int nbGame, Map<BotVersions, Integer> modeClient, Lock lock, Condition conditionLauncherStart){
        this.modeClients = modeClient;
        this.port = port; //Port sur lequel executer la game
        this.listWaitState = new HashMap<>(); // liste de condition pour chaque etats du launcher
        for(StateLauncher s : StateLauncher.values()) this.listWaitState.put(s,this.lock.newCondition());
        this.startTime = 0; //Init temps
        this.nbGame = nbGame;
        this.setState(StateLauncher.NOT_RUNNING); // not running car non executer simplement creer
    }

    /**
     * Instantiates a new Launcher.
     *
     * @param port   the port
     * @param nbGame the nb game
     */
    public Launcher(int port, int nbGame, Map<BotVersions, Integer> modeClient){
        this.modeClients = modeClient;
        this.port = port; //Port sur lequel executer la game
        this.listWaitState = new HashMap<>(); // liste de condition pour chaque etats du launcher
        for(StateLauncher s : StateLauncher.values()) this.listWaitState.put(s,this.lock.newCondition());
        this.startTime = 0; //Init temps
        this.nbGame = nbGame;
        this.setState(StateLauncher.NOT_RUNNING); // not running car non executer simplement creer
    }

    @Override
    public StatisticsInGame call() throws Exception {

        this.startTime = System.currentTimeMillis(); // init le debut execution du launcher
        this.setState(StateLauncher.STARTING_SERVER); // demarre le server
        this.runServer("127.0.0.1",this.port);  // instancie le server et l'execute dans un thread bloque tant que le server ne l'as pas notifier
        this.setState(StateLauncher.SERVER_STARTED);
        this.startClientAndWaitOnGoingGame(); // demarre les x clients de la partie et attend leur fin
        while(this.defaultServer.getState() == StateServer.STARTED){ // tant que le nombre de game courant est inferieur au max et que le serveur et toujour start
            this.lock.lock();
            this.gameFinish.await(); // attend le signal server
            this.lock.unlock();
        }
        this.setState(StateLauncher.WAITING_SHUTDOWN_SERVER); // futur attente de la fin du thread server
        this.threadServer.join(); // attend le thread server
        this.setState(StateLauncher.SHUTDOWN); // thread server arret etat passe a shutdown
        return this.defaultServer.result(); // return le resultat de la partie

    }

    /**
     * Run server.
     *
     * @param hostname the hostname
     * @param port     the port
     * @throws InterruptedException the interrupted exception
     */
    public void runServer(String hostname, int port) throws InterruptedException { //todo remove parameter
        this.defaultServer = Server.getInstance(); // singleton unique au thread en cours
        this.defaultServer.setConfiguration(new ConfigurationServer(this.port,this.nbGame,this.lock,this.serverStart,this.gameFinish));
        this.threadServer = new Thread(this.defaultServer);
        this.threadServer.setName("SERVER "+this.port);
        this.threadServer.setPriority(Thread.MAX_PRIORITY);
        this.threadServer.start(); // start le thread
        this.lock.lock();
        while(this.defaultServer.getState() != StateServer.STARTED) this.serverStart.await(); // temps que le server n est pas start on attend
        this.lock.unlock();
    }

    private void startClientAndWaitOnGoingGame() throws InterruptedException {

        this.setState(StateLauncher.STARTING_CLIENTS);
        List<Client> clients = new ArrayList<>();
        List<Thread> tClients = new ArrayList<>();

        int z = 0;
        for(BotVersions b : this.modeClients.keySet()) z+=this.modeClients.get(b);

        int k = 1;
        if(z == ConfigGame.NB_PLAYER_MAX) {
            for(BotVersions b : this.modeClients.keySet()) {
                for(int i = 0; i< this.modeClients.get(b); i++){
                    Client client = new Client(port, b, k, this.lock, this.clientStart);
                    Thread tclient = new Thread(client);
                    tclient.setName("CLIENT "+port);
                    tclient.setPriority(Thread.MAX_PRIORITY);
                    tclient.start();
                    this.lock.lock();
                    while (!client.isConnected()) this.clientStart.await(); //attend la connection du client avec le server
                    this.lock.unlock();
                    tClients.add(tclient);
                    k++;
                }
            }
        }
        this.setState(StateLauncher.CLIENTS_STARTED);
    }

}
