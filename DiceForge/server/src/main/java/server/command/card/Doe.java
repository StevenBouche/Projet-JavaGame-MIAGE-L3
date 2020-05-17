package server.command.card;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;

import java.util.UUID;

/**
 * The type Doe.
 */
public class Doe extends CommandCard {
    private final UUID idPlayer;

    /**
     * Instantiates a new Doe.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Doe(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager, "DOE_COMMAND");
        this.idPlayer = idPlayer;
    }

    /**
     * Execute action of DoeCard : execute a roll minor for a player
     */
    @Override
    public void onExecute() {
        this.print(" EXECUTE FOR PLAYER : "+idPlayer);
        this.getManager().rollDiceMinorCommand(idPlayer);
    }
}
