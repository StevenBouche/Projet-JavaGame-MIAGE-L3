package server.command.ressource;

import server.command.card.CommandCard;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.cards.Card;
import share.cards.effects.TypeEffectBasique;
import share.game.Game;
import share.player.Player;
import share.ressource.TypeRessource;

/**
 * The type Add glory card end game.
 */
public class AddGloryCardEndGame extends CommandCard {

    /**
     * Instantiates a new Add glory card end game.
     *
     * @param context the context
     * @param manager the manager
     */
    public AddGloryCardEndGame(IGameManager context, CommandManager manager) {
        super(context, manager, "ADD_GLORY_CARD_END_GAME");
    }

    /**
     * Execute for all player add resource glory off cards player
     */
    @Override
    public void onExecute() {
        this.calculGloryPointWithCardPlayer(this.getContext().getGame());
    }

    /**
     * Add all resource glory
     *
     * @param game game object
     */
    private void calculGloryPointWithCardPlayer(Game game) {
        for(Player p : game.getPlayers().values()){
            for(TypeEffectBasique type : p.getInventory().getListsCards().keySet()){
                for(Card c : p.getInventory().getListsCards().get(type)){
                    this.print("ADD GLORY OF "+c+" TO PLAYER "+p.getId());
                    this.addRessourcePlayer(p.getId(), TypeRessource.GLORY,c.glory);
                }
            }
        }
    }


}
