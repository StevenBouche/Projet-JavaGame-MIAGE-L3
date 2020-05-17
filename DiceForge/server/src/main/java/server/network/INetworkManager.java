package server.network;

import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import share.choice.Choice;
import share.eventclientserver.Events;

import java.util.UUID;

public interface INetworkManager extends ConnectListener, DisconnectListener {

    /**
     * To stop network
     */
    void stopNetwork();

    /**
     * to start network
     */
    void startNetwork();

    /**
     * To send data and event at one client
     * @param id id session socket
     * @param ev event object
     * @param o object to serialize
     */
    void sendEventToClient(UUID id, Events ev, Object o);

    void handleChoiceClient(UUID id, Choice choice);
    void setPort(int port);
}
