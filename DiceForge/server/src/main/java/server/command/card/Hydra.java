package server.command.card;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;

import java.util.UUID;

/**
 * The type Hydra.
 */
public class Hydra extends CommandCardNoExecute {

    /**
     * Instantiates a new Hydra.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Hydra(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"HYDRA_COMMAND");
    }


}
