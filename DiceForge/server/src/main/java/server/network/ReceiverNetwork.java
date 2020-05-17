package server.network;

import com.corundumstudio.socketio.SocketIOClient;
import share.choice.Choice;

import java.util.UUID;

public interface ReceiverNetwork {

    /**
     * To notify we have receive an event
     *
     * @param sessionID id session socket emitter
     * @param choice choice object of emitter
     */
    void handleChoiceClient(UUID sessionID, Choice choice);

    /**
     * To notify when client connect at the server
     *
     * @param idSession id session socket client
     * @param version version name of client
     * @param idPlayer id give at client by launcher
     */
    void connectClient(UUID idSession, String version, int idPlayer);

    /**
     * To notify one disconnect of client
     *
     * @param idSession id session socket
     */
    void disconnectClient(UUID idSession);

}
