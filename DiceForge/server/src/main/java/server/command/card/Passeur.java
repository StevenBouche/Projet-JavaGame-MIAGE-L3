package server.command.card;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;

import java.util.UUID;

/**
 * The type Passeur.
 */
public class Passeur extends CommandCardNoExecute {

    /**
     * Instantiates a new Passeur.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Passeur(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"PASSEUR_COMMAND");
    }

}
