package server.network;

public class FactoryNetworkManager {

    public static INetworkManager createNetworkManager(ReceiverNetwork receiver){
        return new NetworkManager(receiver);
    }
}
