package server.network;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.namespace.Namespace;
import com.fasterxml.jackson.databind.ObjectMapper;
import share.choice.*;
import share.eventclientserver.Events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NetworkManager implements INetworkManager {

    private SocketIOServer server;
    private int port;
    private ReceiverNetwork receiverNetwork;
    private final Lock lock = new ReentrantLock();
    private final ObjectMapper mapper;
    private List<DataListenerDiceForge> listListener;
    /**
     * Create object NetworkManager
     *
     * @param receiverNetwork received network to notify him to all events
     */
    public NetworkManager(ReceiverNetwork receiverNetwork){
        this.listListener = new ArrayList<>();
        this.receiverNetwork = receiverNetwork;
        this.mapper = new ObjectMapper();
    }

    /**
     * stop network server, disconnect all client ans stop
     */
    @Override
    public void stopNetwork() {
        for(SocketIOClient s : server.getAllClients()) s.disconnect(); // disconnect all player server change state
        server.stop(); // stop server and finish current thread
    }

    /**
     * start network server, configure the socket server and block while network is not started
     */
    public void startNetwork() {
        Configuration config = this.getConfiguration();
        this.server = new SocketIOServer(config);
        this.initEvents();
        Future<Void> attente = this.server.startAsync(); // Start le server mais async attention
        while(!attente.isDone()) this.sleep(); // tant que la task du start server n'est pas fini attend
    }

    /**
     * To send data at one client
     *
     * @param id id session socket client
     * @param ev event name
     * @param o object to serialize
     */
    @Override
    public void sendEventToClient(UUID id, Events ev, Object o) {
        this.getClientFromId(id).sendEvent(ev.getEventID(), o);
    }

    /**
     * To setup port of network server
     * @param port
     */
    @Override
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Get socket of one client
     *
     * @param id id session socket
     * @return socket client object
     */
    private SocketIOClient getClientFromId(UUID id) {
        return this.server.getClient(id);
    }

    /**
     * set up configuration network object
     *
     * @return configuration network to inject in to socket server
     */
    private Configuration getConfiguration(){
        //    config.setTransports(Transport.WEBSOCKET);
        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(this.port);
        config.setUpgradeTimeout(10000); // https://github.com/socketio/socket.io-client-java/issues/491 Note2: The default values of these parameters in okHttp is 10000ms or 10 seconds.
        return config;
    }

    /**
     * Handle connection client. Trigger when client try connection
     * @param socketIOClient socket client object
     */
    @Override
    public void onConnect(SocketIOClient socketIOClient) {
        this.lock.lock();
        String str = "NO_NAME";
        int id = 0;
        if(socketIOClient.getHandshakeData() != null){
            str = socketIOClient.getHandshakeData().getSingleUrlParam("version");
            id = Integer.parseInt(socketIOClient.getHandshakeData().getSingleUrlParam("id"));
        }
        receiverNetwork.connectClient(socketIOClient.getSessionId(), str,id);
        this.lock.unlock();
    }

    /**
     * Handle and trigger when client disconnect
     * @param socketIOClient socket client object
     */
    @Override
    public void onDisconnect(SocketIOClient socketIOClient) {
        this.lock.lock();
        receiverNetwork.disconnectClient(socketIOClient.getSessionId());
        this.lock.unlock();
    }

    /**
     * When client send object to server
     *
     * @param sessionID id socket client
     * @param choice choice object
     */
    @Override
    public void handleChoiceClient(UUID sessionID, Choice choice){
        this.lock.lock();
        receiverNetwork.handleChoiceClient(sessionID,choice);
        this.lock.unlock();
    }

    /**
     * Initialize all events of network server
     */
    private void initEvents() {
        this.server.addConnectListener(this);
        this.server.addDisconnectListener(this);
        listListener.add(new DataListenerDiceForge<>(Events.CHOICE_BETWEEN_RESSOURCES, ChoiceBetweenRessource.class, this));
        listListener.add(new DataListenerDiceForge<>(Events.HANDLE_CHOICE_FORGE, ChoiceNothingForgeExploit.class, this));
        listListener.add(new DataListenerDiceForge<>(Events.CHOICE_ONE_MORE_ACTION, ChoiceOneMoreTurn.class, this));
        listListener.add(new DataListenerDiceForge<>(Events.CHOICE_HAMMER, ChoiceHammer.class, this));
        listListener.add(new DataListenerDiceForge<>(Events.CHOICE_3GOLD_FOR_4GLORY, Choice3GoldFor4Glory.class, this));
        listListener.add(new DataListenerDiceForge<>(Events.CHOICE_SATYRE, ChoiceSatyre.class, this));
        listListener.add(new DataListenerDiceForge<>(Events.CHOICE_FORGE_SPECIAL, ChoiceForgeFaceSpecial.class, this));
        listListener.add(new DataListenerDiceForge<>(Events.CHOICE_POWER_OTHER_PLAYER, ChoicePowerOnDiceOtherPlayer.class, this));
        for(DataListenerDiceForge dl : listListener){
            this.server.addEventListener(dl.getEvent().getEventID(),String.class, dl);
        }
    }

    private void sleep(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}
