package server.command.card;

import server.command.CommandGameManager;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.ressource.TypeRessource;

import java.util.UUID;

public abstract class CommandCard extends CommandGameManager {

    /**
     * Instantiates a new Command.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param name    the name of this command
     */
    public CommandCard(IGameManager context, CommandManager manager, String name) {
        super(context, manager, name);
    }

    @Override
    protected void addRessourcePlayer(UUID idPlayer, TypeRessource type, int value){
        super.addRessourcePlayer(idPlayer, type,  value);
    }


}
