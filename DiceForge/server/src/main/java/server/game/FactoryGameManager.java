package server.game;

public class FactoryGameManager {

    public static IGameManager createGameManager(INotifyEvent not){
        return new GameManager(not);
    }
}
