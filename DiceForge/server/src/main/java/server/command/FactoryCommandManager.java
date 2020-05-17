package server.command;

import server.command.inter.ICommandManager;
import server.game.IGameManager;

public class FactoryCommandManager {

    public static ICommandManager createCommandManager(IGameManager gameManager){
        return new CommandManager(gameManager);
    }

}
