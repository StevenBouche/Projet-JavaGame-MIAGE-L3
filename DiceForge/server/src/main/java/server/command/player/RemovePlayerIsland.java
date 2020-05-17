package server.command.player;

import server.command.CommandGameManager;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.temple.IslandEnum;

import java.util.UUID;

/**
 * The type Remove player island.
 */
public class RemovePlayerIsland extends CommandGameManager {

    /**
     * The Id player.
     */
    final UUID idPlayer;
    /**
     * The Island.
     */
    final IslandEnum island;

    /**
     * Instantiates a new Remove player island.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer the id player
     * @param island   the island
     */
    public RemovePlayerIsland(IGameManager context, CommandManager manager, UUID idPlayer, IslandEnum island) {
        super(context, manager, "REMOVE_PLAYER_ISLAND");
        this.idPlayer = idPlayer;
        this.island = island;
    }

    /**
     * Player has been removed from one island, trigger roll dice major to this player.
     */
    @Override
    public void onExecute() {
        this.print("REMOVE PLAYER "+idPlayer+"FROM "+island);
        this.getManager().rollDiceMajorCommand(idPlayer);
    }

}
