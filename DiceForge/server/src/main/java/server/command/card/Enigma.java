package server.command.card;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;

import java.util.UUID;

/**
 * The type Enigma.
 */
public class Enigma extends CommandCard {

    private final UUID idPlayer;

    /**
     * Instantiates a new Enigma.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Enigma(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"ENIGMA_COMMAND");
        this.idPlayer = idPlayer;
    }

    /**
     * Execute action of EnigmaCard : execute 4 roll minor for a player
     */
    @Override
    public void onExecute()
    {   //todo with the same dice
        this.print("EXECUTE 4 ROLL DICE MINOR FOR PLAYER "+idPlayer);
        for(int i=0; i<4; i++) this.getManager().rollDiceMinorCommand(idPlayer);
    }
}
