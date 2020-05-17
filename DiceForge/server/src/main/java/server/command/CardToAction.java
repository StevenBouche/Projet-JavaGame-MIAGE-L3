package server.command;

import server.command.card.*;
import server.game.IGameManager;
import share.cards.Cards;


import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

/**
 * The enum Card to action.
 */
public enum CardToAction{
    /**
     * Hammer action card to action.
     */
    HAMMER_ACTION(Cards.HAMMER, Hammer.class),
    /**
     * Chest action card to action.
     */
    CHEST_ACTION(Cards.CHEST, Chest.class),
    /**
     * Doe action card to action.
     */
    DOE_ACTION(Cards.DOE, Doe.class),
    /**
     * Ancient action card to action.
     */
    ANCIENT_ACTION(Cards.ANCIENT, Ancient.class),
    /**
     * Grass action card to action.
     */
    GRASS_ACTION(Cards.GRASS, Grass.class),
    /**
     * Owl action card to action.
     */
    OWL_ACTION(Cards.OWL, Owl.class),
    /**
     * Satyres action card to action.
     */
    SATYRES_ACTION(Cards.SATYRES,Satyres.class),
    /**
     * Passeur action card to action.
     */
    PASSEUR_ACTION(Cards.PASSEUR,Passeur.class),
    /**
     * Invisible action card to action.
     */
    INVISIBLE_ACTION(Cards.INVISIBLE,Invisible.class),
    /**
     * Claw action card to action.
     */
    CLAW_ACTION(Cards.CLAW,Claw.class),
    /**
     * Minotaure action card to action.
     */
    MINOTAURE_ACTION(Cards.MINOTAURE,Minotaure.class),
    /**
     * Medusa action card to action.
     */
    MEDUSA_ACTION(Cards.MEDUSA,Medusa.class),
    /**
     * Mirror action card to action.
     */
    MIRROR_ACTION(Cards.MIRROR,Mirror.class),
    /**
     * Enigma action card to action.
     */
    ENIGMA_ACTION(Cards.ENIGMA,Enigma.class),
    /**
     * Hydra action card to action.
     */
    HYDRA_ACTION(Cards.HYDRA,Hydra.class);

    /**
     * The Card.
     */
    public final Cards card;
    /**
     * The Cmd.
     */
    public final Class<? extends Command<IGameManager>> cmd;

    CardToAction(Cards c, Class<? extends Command<IGameManager>> cmd){
        this.card = c;
        this.cmd = cmd;
    }

    private static Class<? extends Command<IGameManager>> getClassOfCard(Cards c){
        for(CardToAction cta : CardToAction.values()){
            if(cta.card == c) return cta.cmd;
        }
        return null;
    }

    /**
     * Gets action card.
     *
     * @param c        the c
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer the id player
     * @return the action card
     */
    public static Command<IGameManager> getActionCard(Cards c, IGameManager context, CommandManager manager,UUID idPlayer) {
        try {
            if(getClassOfCard(c) == null) return null;
            return getClassOfCard(c).getConstructor(IGameManager.class,CommandManager.class,UUID.class).newInstance(context, manager, idPlayer);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
