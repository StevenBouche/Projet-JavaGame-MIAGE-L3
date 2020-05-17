package server.command.player;

import server.command.CommandGameManager;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.cards.Card;

import java.util.UUID;

/**
 * The type Set player on island.
 */
public class SetPlayerOnIsland extends CommandGameManager {

    /**
     * The Id player.
     */
    final UUID idPlayer;
    /**
     * The Card was buy.
     */
    final Card cardWasBuy;

    /**
     * Instantiates a new Set player on island.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer the id player
     * @param c        the c
     */
    public SetPlayerOnIsland(IGameManager context, CommandManager manager, UUID idPlayer, Card c) {
        super(context, manager, "SET_PLAYER_ON_ISLAND");
        this.idPlayer = idPlayer;
        this.cardWasBuy = c;
    }

    /**
     * Set a player on a island. If other player is on this island remove it and set new player.
     */
    @Override
    public void onExecute() {
        boolean b = this.getContext().getGame().ifPlayerisAlreadyOnIslandCard(idPlayer, cardWasBuy);
        if(b){
            this.print("SET PLAYER "+idPlayer+" ON "+ cardWasBuy.islandId);
            UUID idlastPlayerOnIsland = this.getContext().getGame().addPlayerOnIslandOfTemple(idPlayer,cardWasBuy.islandId);
            if(idlastPlayerOnIsland != null) this.getManager().triggerCommandPlayerLeaveIsland(idlastPlayerOnIsland, cardWasBuy.islandId);
        }
    }
}
