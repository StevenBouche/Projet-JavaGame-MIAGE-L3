package server.command;

import server.game.GameManager;
import server.game.IGameManager;
import share.ressource.TypeRessource;

import java.util.UUID;

public abstract class CommandGameManager extends Command<IGameManager> {


    /**
     * Instantiates a new Command.
     *
     * @param context the context
     * @param manager the manager
     * @param name    the name
     */
    public CommandGameManager(IGameManager context, CommandManager manager, String name) {
        super(context, manager, name);
    }


    protected void addRessourcePlayer(UUID idPlayer, TypeRessource type, int value){
        getManager().addRessourcePlayerCommand(idPlayer, type,  value);
    }

    protected void removeRessourcePlayer(UUID idPlayer, TypeRessource type, int value){
        getManager().removeRessourcePlayerCommand(idPlayer, type,  value);
    }

}
