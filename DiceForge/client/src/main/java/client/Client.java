package client;

import client.game.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import okhttp3.OkHttpClient;
import share.choice.*;
import share.eventclientserver.Events;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.socket.client.IO;
import io.socket.client.Socket;
import share.ressource.TypeRessource;
import share.utils.Printer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type client.Client.
 */
public class Client implements Runnable {

    private Socket socket;
    private boolean isConnected = false;
    private ObjectMapper mapper = new ObjectMapper();
    private GameManager gameManagerRandom;
    private int port;
    private boolean running;
    private Lock lock;
    private Condition conditionFinClient;
    private Lock lockLauncher;
    private Condition conditionClientStarting;
    BotVersions version;
    int id;

    /**
     * Instantiates a new Client.
     *
     * @param port           the port network
     * @param version        the version bot
     * @param id             the id give by launcher
     * @param lock           the lock launcher
     * @param conditionStart the condition start
     */
    public Client(int port, BotVersions version, int id, Lock lock, Condition conditionStart){
        this.id = id;
        this.version = version;
        this.lockLauncher = lock;
        this.conditionClientStarting = conditionStart;
        this.lock = new ReentrantLock();
        this.conditionFinClient = this.lock.newCondition();
        this.port = port;
        this.running = true;
        this.gameManagerRandom = FactoryGameManager.createGameManager(this.version);
        this.socket = initSocket(this.port);
        if(this.socket != null) this.initEvents(this.socket);
    }

    /**
     * Wait request from the server
     */
    @Override
    public void run() {
        this.socket.connect(); //tentative de connexion
        Printer.getInstance().logClient("Attente de connexion "+this.port,"");
        while (this.running) { // running passe a false quand event disconnected on socket
            this.lock.lock();
            try {
                this.conditionFinClient.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
            this.lock.unlock();
        } //garde le thread actif tant que la socket est connecter au serveur
        Printer.getInstance().logClient("Socket disconnected, loose connection",this.gameManagerRandom.getUUID());
        Printer.getInstance().logClient("Shutdown",this.gameManagerRandom.getUUID());
        if(this.socket.connected()) this.socket.close();
    }

    /**
     * Stop client thread.
     */
    public void stopClient(){
        this.lock.lock();
        this.isConnected = false;
        this.running = false;
        this.conditionFinClient.signal();
        this.lock.unlock();
    }

    /**
     * Init socket client.
     *
     * @param port the port
     * @return the socket
     */
    synchronized Socket initSocket(int port){
        Socket s;
         /*https://github.com/socketio/socket.io-client-java/issues/491  RESOLVE PROBLEM DISCONNECTION FROM SERVER NETTY SOCKETIO*/
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        IO.setDefaultOkHttpCallFactory(okHttpClient);
        IO.setDefaultOkHttpWebSocketFactory(okHttpClient);

        IO.Options opts = new IO.Options();
       // opts.transports = new String[] { WebSocket.NAME };// to active websocket protocol
        opts.reconnection = false;
        opts.forceNew = true;
        opts.callFactory = okHttpClient;
        opts.webSocketFactory = okHttpClient;
        opts.query = "version="+this.version.toString()+"&id="+this.id;
        try {
            s = IO.socket("http://127.0.0.1:"+port+"/", opts);
            return s;
        } catch (URISyntaxException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }

    /**
     * Initialize all event of client
     *
     * @param socket socket client
     */
    private void initEvents (Socket socket) {
        socket.on(Socket.EVENT_CONNECT, objects -> onConnect());
        socket.on(Socket.EVENT_DISCONNECT, objects -> onDisconnect());
        socket.on(Events.CHOICE_BETWEEN_RESSOURCES.getEventID(), objects -> choiceFaceHybrid(Events.CHOICE_BETWEEN_RESSOURCES,objects[0].toString()));
        socket.on(Events.HANDLE_CHOICE_FORGE.getEventID(), objects -> handleChoiceForgeExploitAndDoAction(Events.HANDLE_CHOICE_FORGE,objects[0].toString()));
        socket.on(Events.CHOICE_ONE_MORE_ACTION.getEventID(), objects -> handleChoiceOneMoreTime(Events.CHOICE_ONE_MORE_ACTION,objects[0].toString()));
        socket.on(Events.CHOICE_HAMMER.getEventID(), objects -> handleChoiceHammer(Events.CHOICE_HAMMER,objects[0].toString()));
        socket.on(Events.CHOICE_3GOLD_FOR_4GLORY.getEventID(), objects -> handleChoiceCardAncient(Events.CHOICE_3GOLD_FOR_4GLORY,objects[0].toString()));
        socket.on(Events.CHOICE_SATYRE.getEventID(), objects -> handleChoiceCardSatyre(Events.CHOICE_SATYRE,objects[0].toString()));
        socket.on(Events.CHOICE_FORGE_SPECIAL.getEventID(), objects -> handleChoiceForgeSpecial(Events.CHOICE_FORGE_SPECIAL,objects[0].toString()));
        socket.on(Events.CHOICE_POWER_OTHER_PLAYER.getEventID(), objects -> handleChoicePowerOtherPlayer(Events.CHOICE_POWER_OTHER_PLAYER,objects[0].toString()));
    }

    /**
     * Event on connect
     */
    private void onConnect(){
        this.isConnected = true;
        this.gameManagerRandom.setUUID(this.version.toString()+"-"+this.socket.id());
        Printer.getInstance().logClient("connexion id : "+this.socket.id()+" "+this.isConnected,this.socket.id());
        this.lockLauncher.lock();
        this.conditionClientStarting.signal();
        this.lockLauncher.unlock();
    }

    /**
     * Event on disconnect
     */
    private void onDisconnect(){
        this.stopClient();
    }

    private void handleChoicePowerOtherPlayer(Events evt, String toString) {
        Printer.getInstance().logClient("Event venant du server : "+evt.getEventID(), socket.id());
        try {
            ChoicePowerOnDiceOtherPlayer ch = this.mapper.readValue(toString,ChoicePowerOnDiceOtherPlayer.class);
            this.gameManagerRandom.choicePowerOtherPlayer(ch);
            socket.emit(evt.getEventID(), mapper.writeValueAsString(ch));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleChoiceForgeSpecial(Events evt, String toString) {
        Printer.getInstance().logClient("Event venant du server : "+evt.getEventID(), socket.id());
        try {
            ChoiceForgeFaceSpecial ch = this.mapper.readValue(toString,ChoiceForgeFaceSpecial.class);
            this.gameManagerRandom.choiceForgeSpecial(ch);
            socket.emit(evt.getEventID(), mapper.writeValueAsString(ch));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleChoiceCardSatyre(Events evt, String toString) {
        Printer.getInstance().logClient("Event venant du server : "+evt.getEventID(), socket.id());
        try {
            ChoiceSatyre ch = this.mapper.readValue(toString,ChoiceSatyre.class);
            this.gameManagerRandom.choiceSatyre(ch,0);
            socket.emit(evt.getEventID(), mapper.writeValueAsString(ch));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleChoiceCardAncient(Events evt, String toString) {
        Printer.getInstance().logClient("Event venant du server : "+evt.getEventID(), socket.id());
        try {
            Choice3GoldFor4Glory ch = this.mapper.readValue(toString,Choice3GoldFor4Glory.class);
            this.gameManagerRandom.choiceAncient(ch);
            socket.emit(evt.getEventID(), mapper.writeValueAsString(ch));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère le choix du hammer
     *
     */
    private synchronized void handleChoiceHammer(Events evt,String toString) {
        Printer.getInstance().logClient("Event venant du server : "+evt.getEventID(), socket.id());
        try {
            ChoiceHammer ch = this.mapper.readValue(toString,ChoiceHammer.class);
            this.gameManagerRandom.choiceHammer(ch);
            socket.emit(evt.getEventID(), mapper.writeValueAsString(ch));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void handleChoiceOneMoreTime(Events evt,String toString) {
        Printer.getInstance().logClient("Event venant du server : "+evt.getEventID(), socket.id());
        try {
            ChoiceOneMoreTurn ch = this.mapper.readValue(toString,ChoiceOneMoreTurn.class);
            this.gameManagerRandom.choiceOneMoreTime(ch);
            socket.emit(evt.getEventID(), mapper.writeValueAsString(ch));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Choice ressource from hybrid face
     *
     * @param value the choice
     */
    private synchronized void choiceFaceHybrid(Events evt,String value){
        ChoiceBetweenRessource choice = null;
        Printer.getInstance().logClient("Event venant du server : "+evt.getEventID(), socket.id());
        try {
            choice = this.mapper.readValue(value, ChoiceBetweenRessource.class);
            TypeRessource ressource = this.gameManagerRandom.choiceRessourceFaceHybrid(choice.getListRessource());
            choice.setTypeRessource(ressource);
            Printer.getInstance().logClient("Le joueur a choisi la share.ressource: " + Printer.BLUE + ressource + " "+ choice.getListRessource().get(ressource),socket.id());
            socket.emit(evt.getEventID(), mapper.writeValueAsString(choice));
        } catch (IOException e) {
            e.printStackTrace();
            this.stopClient();
        }
    }

    private synchronized void  handleChoiceForgeExploitAndDoAction(Events evt,String value){
        Printer.getInstance().logClient("Event venant du server : "+evt.getEventID(), socket.id());
        try {
            ChoiceNothingForgeExploit ch = this.mapper.readValue(value, ChoiceNothingForgeExploit.class);
            Printer.getInstance().logClient("Event share.choice nothing share.forge or exploit",socket.id());
            this.gameManagerRandom.choiceForgeOrExploiOrNothing(ch);
            ch.d1 = null;
            ch.d2 = null;
            ch.poolForge = null;
            socket.emit(evt.getEventID(), mapper.writeValueAsString(ch));
        } catch (IOException e) {
            e.printStackTrace();
            this.stopClient();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}
