package server.command.card;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;

import java.util.UUID;

/**
 * The type Medusa.
 */
public class Medusa extends CommandCardNoExecute {

    /**
     * Instantiates a new Medusa.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Medusa(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"MEDUSA_COMMAND");
    }

}
