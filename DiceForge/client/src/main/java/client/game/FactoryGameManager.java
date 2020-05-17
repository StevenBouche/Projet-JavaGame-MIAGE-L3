package client.game;

public class FactoryGameManager {

    public static GameManager createGameManager(BotVersions version){
        if(version == BotVersions.RANDOM) return new GameManagerRandom();
        else if(version == BotVersions.VERSION_SIMPLE) return new GameManagerVersionSimple();
        else if(version == BotVersions.VERSION_STATISTIQUE) return new GameManagerVersionStat();
        else return null;
    }
}
