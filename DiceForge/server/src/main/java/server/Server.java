package server;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.*;
import server.game.FactoryGameManager;
import server.game.IGameManager;
import server.network.FactoryNetworkManager;
import server.network.INetworkManager;
import server.network.NetworkManager;
import server.network.ReceiverNetwork;
import server.statistics.Statistics;
import server.statistics.StatisticsInGame;
import server.game.loop.GameStepLoopEnum;
import share.choice.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import share.eventclientserver.Events;
import server.game.GameManager;
import share.game.GameState;
import server.game.INotifyEvent;
import share.player.Player;
import share.utils.HandleRandom;
import share.utils.Printer;

import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type Server.
 */
public class Server implements Runnable, ReceiverNetwork, INotifyEvent, IServerDiceForge {

    int nbGame;
    private int nbGameTot;
    IGameManager gameManager;
    private INetworkManager networkManager;
    private ConfigurationServer config;
    boolean running;
    private StateServer state;
    private boolean runGame;
    private final Lock lockGameManager = new ReentrantLock();
    private final Lock lockServer = new ReentrantLock();
    private final Condition conditionPlayersHavejoin = lockServer.newCondition();

    /**
     * To create singleton in multi threading environment
     */
    private static final ThreadLocal<Server> _threadLocal =
            new ThreadLocal<Server>() {
                @Override
                protected Server initialValue() {
                    return new Server();
                }
            };

    /**
     * To get instance of server, thread local singleton
     *
     * @return server of game
     */
    public synchronized static Server getInstance() {
        return _threadLocal.get();
    }

    /**
     * Reset singleton
     */
    public synchronized static void resetInstance(){
        _threadLocal.remove();
    }

    /**
     * Private constructor, called by method initialValue of _threadLocal
     */
    private Server(){
        this.initServer();
    }

    /**
     * Run task server
     * Start th server, wait client connection and start nb games
     * Disconnect all client when finish execution and stop network server
     */
    @Override
    public void run()  {
        if(config==null) return;
        this.startServer(); // start server
        this.initStatistics(); // init statistic
        while(this.state == StateServer.STARTED && !Thread.currentThread().isInterrupted()){ // while server started
            this.handleConnectionPlayers(); // process connexion players
            this.processGame(); // execute game
        }
        this.running = false;
        networkManager.stopNetwork();
    }

    /**
     * Notify gameManager than receive choice player
     *
     * @param sessionID id session of socket connection
     * @param choice choice object send by the client
     */
    @Override
    public void handleChoiceClient(UUID sessionID, Choice choice){
        this.gameManager.notifyChoicePlayer(sessionID,choice);
    }

    /**
     * Notify gameManager than one player want play
     *
     * @param idSession id session of socket connection client
     * @param version version string of the client
     * @param idPlayer id give by launcher, not random
     */
    @Override
    public void connectClient(UUID idSession, String version, int idPlayer) {
        Printer.getInstance().logServer("Connection client "+idSession,this.config.portNetwork);
        this.gameManager.notifyConnectionPlayer(idSession, version, idPlayer);
    }

    /**
     *  Notify game Manager than one player have disconnect
     *
     * @param idSession id session of the player disconnect
     */
    @Override
    public void disconnectClient(UUID idSession) {
        Printer.getInstance().logServer("Disconnect client "+idSession,this.config.portNetwork);
        this.gameManager.notifyDeconnectionPlayer(idSession);
        this.runGame = false;
    }

    /**
     * Send object at client
     *
     * @param id id session of socket connection player
     * @param ev event name
     * @param o object to serialize
     */
    @Override
    public void sendEventToClient(UUID id, Events ev, Object o) {
        networkManager.sendEventToClient(id,ev,o);
    }

    /**
     * Initialize server
     */
    private void initServer(){
        this.config = new ConfigurationServer(0,0,null,null,null); // config null attention
        this.setState(StateServer.INIT);
        this.running = false;
        this.gameManager = FactoryGameManager.createGameManager(this);
        this.networkManager = FactoryNetworkManager.createNetworkManager(this);
        this.setState(StateServer.INITIALIZED);
        //Statistics.getInstance().setEnabled(true);
    }

    /**
     * Process of connection player
     * Waiting while nb player is lower than nb player need to start game
     */
    private void handleConnectionPlayers() {
        while (this.getNbPlayerGame() < this.getNbPlayerMaxGame() && !Thread.currentThread().isInterrupted()){
            this.lockServer.lock();
            try {
                this.conditionPlayersHavejoin.await(); // signal when player join game
            } catch (InterruptedException e) {
                this.setState(StateServer.INTERRUPTED);
                Thread.currentThread().interrupt();
                break;
            }
            this.lockServer.unlock();
        }
    }

    /**
     * Process execution of game
     * Waiting while nb current game executions is lower than game number needed
     */
    private void processGame() {
        this.runGame = true;
        while(this.nbGame > 0 && !Thread.currentThread().isInterrupted() && this.runGame){
            this.gameManager.startGame();
            this.handleStatResultEndGame();
            this.nbGame--;
            this.handleStateGame(); // reset or finish game
            this.config.lockFather.lock();
            this.config.gameFinish.signal(); //signal father we have finish one game
            this.config.lockFather.unlock();
        }
    }

    /**
     * trigger after game execute, to know if need to reset game or we have finish execute all game
     */
    private void handleStateGame() {
        if(this.nbGame > 0 && this.runGame) {
            this.initStatistics();
            this.gameManager.resetGame();
        }
        else this.setState(StateServer.FINISHED);
    }

    /**
     * Add all stats or result after one game finish
     */
    private void handleStatResultEndGame() {
        int nbG = this.nbGameTot-this.nbGame+1;
        String str = "RESULT LAUNCHER "+this.config.portNetwork+" GAME "+nbG+"\n";
        int place = 1;
        for(Player p : this.gameManager.getListResultGame()){
            str += p.toString()+"\n";
            Statistics.getInstance().addEndGameResults(p.getId().toString() + "\t" + Statistics.CHOICE_ID.PLACE + "\t" + place++);
        }
        Statistics.getInstance().closeWriter();
        str += "\n\n";
        this.gameManager.getStats().addResultGame(str);
    }

    /**
     * Initialize network server
     * Waiting while network is not started
     * And signal condition when i have start
     */
    private void startServer(){
        this.setState(StateServer.STARTING);
        networkManager.startNetwork(); // Attention server start peut mettre du temps
        this.running = true;
        Printer.getInstance().logServer("Started",this.config.portNetwork);
        this.setState(StateServer.STARTED);
        this.signalHaveStarted();
    }

    private void initStatistics() {
        int nbG = this.nbGameTot-this.nbGame+1;
        String fileName = nbG + "_" + System.currentTimeMillis() + "_" + new HandleRandom().getRandomBetweenMinMax(0, 0x7ffffffe);
        Statistics.getInstance().init(fileName);
    }

    /**
     * To notify when a player is add in this game
     * if current players number equal max player number notify signal thread server that can start game
     */
    @Override
    public void notifyPlayerAddGame() {
        if(this.getNbPlayerGame() == this.getNbPlayerMaxGame()) {
            this.lockServer.lock();
            this.conditionPlayersHavejoin.signal();
            this.lockServer.unlock();
        }
    }

    private int getNbPlayerGame() {
        return this.gameManager.getNbPlayer();
    }

    private int getNbPlayerMaxGame() {
        return this.gameManager.getNbPlayerMax();
    }

    /**
     * @return state object of the server
     */
    public StateServer getState(){
        return this.state;
    }

    /**
     * @return statistic in game object object of the server
     */
    public StatisticsInGame result() {
        return this.gameManager.getStats();
    }

    private void setState(StateServer init) {
        this.state = init;
        Printer.getInstance().logServer(this.state.toString(),this.config.portNetwork);
    }

    private void signalHaveStarted() {
        this.config.lockFather.lock();
        this.config.serverStart.signal();
        this.config.lockFather.unlock();
    }

    /**
     * @return current nb game executed
     */
    public int getCurrentNbGameExecuted() {
        return this.nbGameTot-this.nbGame;
    }

    /**
     * Set configuration of the server for it can start
     * @param configurationServer configuration server object
     */
    public void setConfiguration(ConfigurationServer configurationServer) {
        this.config = configurationServer;
        this.nbGame = this.config.nbGame;
        this.nbGameTot = this.nbGame;
        this.networkManager.setPort(this.config.portNetwork);
    }
}
