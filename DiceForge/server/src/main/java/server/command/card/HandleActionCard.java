package server.command.card;

import server.command.CommandGameManager;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.cards.Card;
import share.cards.effects.TypeEffectBasique;

import java.util.UUID;

/**
 * The type Handle action card.
 */
public class HandleActionCard extends CommandGameManager {

    private final UUID idPlayer;
    /**
     * The C.
     */
    public final Card c;

    /**
     * Instantiates a new Handle action card.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     * @param c        the card was buy by the player
     */
    public HandleActionCard(IGameManager context, CommandManager manager, UUID idPlayer, Card c) {
        super(context, manager, "HANDLE_ACTION_CARD");
        this.idPlayer = idPlayer;
        this.c = c;
    }

    /**
     *
     * Execute action when a card was buy, execute set player on island, and if immediate card trigger it
     *
     */
    @Override
    public void onExecute() {
        this.print("SET PLAYER "+idPlayer+" ON "+c.islandId+" HAVE BUY "+c);
        this.getContext().getStats().incNbBuyCardsPlayer(idPlayer,c.cardsId);
        this.getManager().triggerCommandSetPlayerOnIsland(idPlayer,c); //Set le Player sur l ile de la carte
        if(c.typeEffect != TypeEffectBasique.IMMEDIATE) return;
        this.getContext().getStats().incNbActionCardImmediate(idPlayer);
        this.print("CARD HAVE IMMEDIATE ACTION EXECUTE ACTION "+c+" FOR PLAYER "+idPlayer);
        this.getManager().triggerCommandActionCard(idPlayer,c);
    }
}
