package server.command.card;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;

import java.util.UUID;

/**
 * The type Claw.
 */
public class Claw extends CommandCard {
    private final UUID idPlayer;

    /**
     * Instantiates a new Claw.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Claw(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"CLAW_COMMAND");
        this.idPlayer = idPlayer;
    }

    /**
     * Execute action of CardCard : execute 2 roll major for a player
     */
    @Override
    public void onExecute() {
        for (int i = 0; i < 2; i++) {
            this.print(i+1+"/2 EXECUTE ROLL DICE MAJOR FOR PLAYER "+idPlayer);
            this.getManager().rollDiceMajorCommand(this.idPlayer);
        }
    }
}
