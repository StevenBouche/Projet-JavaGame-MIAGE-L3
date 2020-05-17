package server.network;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import share.choice.Choice;
import share.eventclientserver.Events;

public class DataListenerDiceForge<K extends Choice> implements DataListener<String> {

    private final ObjectMapper mapper;
    private final Events event;
    private Class<K> type;
    private INetworkManager manager;

    public  DataListenerDiceForge(Events event, Class<K> type, INetworkManager manager) {
        this.mapper = new ObjectMapper();
        this.event = event;
        this.type = type;
        this.manager = manager;
    }

    @Override
    public void onData(SocketIOClient socketIOClient, String s, AckRequest ackRequest) throws Exception {
        K elem = this.mapper.readValue(s,type);
        this.manager.handleChoiceClient(socketIOClient.getSessionId(),elem);
    }

    public Events getEvent() {
        return event;
    }
}
