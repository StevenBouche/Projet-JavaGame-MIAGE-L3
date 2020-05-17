package server.command.card;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;

import java.util.UUID;

/**
 * The type Chest.
 */
public class Chest extends CommandCard {

    final UUID idPlayer;

    /**
     * Instantiates a new Chest.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Chest(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"CHEST_COMMAND");
        this.idPlayer = idPlayer;
    }

    /**
     * Execute action of ChestCard : extend all resource by 4
     */
    @Override
    public void onExecute() {
        this.print("EXTEND ALL RESSOURCE PLAYER "+idPlayer+" BY 4");
        this.getContext().getGame().extendRessourcesPlayer(idPlayer,4);
    }
}
