package server.command.player;

import server.command.CommandGameManager;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;

import java.util.UUID;

/**
 * The type Set player actif.
 */
public class SetPlayerActif extends CommandGameManager {

    /**
     * Instantiates a new Set player actif.
     *
     * @param context the context
     * @param manager the manager
     */
    public SetPlayerActif(IGameManager context, CommandManager manager) {
        super(context, manager,"SET_PLAYER_ACTIF_COMMAND");
    }

    /**
     * Get next Player and set up in game as current player
     */
    @Override
    public void onExecute() {
        UUID currentIdPlayer = this.getContext().getGame().getIdPlayers().remove(0);
        this.getContext().getGame().currentPlayer = this.getContext().getGame().getPlayer(currentIdPlayer);
        this.print(" CURRENT PLAYER TURN : "+currentIdPlayer+"\n"+this.getContext().getGame().currentPlayer.toString());
        this.getContext().getGame().getIdPlayers().add(currentIdPlayer);
    }
}
