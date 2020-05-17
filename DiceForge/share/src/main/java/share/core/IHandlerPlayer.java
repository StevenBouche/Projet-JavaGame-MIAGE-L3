package share.core;

import share.cards.Card;
import share.cards.effects.TypeEffectBasique;
import share.face.Face;
import share.player.Player;
import share.ressource.TypeRessource;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The interface Handler player.
 */
public interface IHandlerPlayer {

    /**
     * Remove ressources player.
     *
     * @param costs    the costs
     * @param idplayer the idplayer
     */
    void removeRessourcesPlayer(Map<TypeRessource,Integer> costs, UUID idplayer);

    /**
     * Remove ressource player.
     *
     * @param idplayer the idplayer
     * @param cost     the cost
     * @param nb       the nb
     */
    void removeRessourcePlayer(UUID idplayer, TypeRessource cost, int nb);

    /**
     * Add ressource player.
     *
     * @param idplayer  the idplayer
     * @param ressource the ressource
     * @param nb        the nb
     */
    void addRessourcePlayer(UUID idplayer, TypeRessource ressource, int nb);

    /**
     * Add card player.
     *
     * @param c        the c
     * @param idPlayer the id player
     */
    void addCardPlayer(Card c, UUID idPlayer);

    /**
     * Remove face of dice player face.
     *
     * @param id        the id
     * @param nbDice    the nb dice
     * @param nbPosFace the nb pos face
     * @return the face
     */
    Face removeFaceOfDicePlayer(UUID id, int nbDice, int nbPosFace);

    /**
     * Add face of dice player face.
     *
     * @param idPlayer  the id player
     * @param nbDice    the nb dice
     * @param nbPosFace the nb pos face
     * @param f         the f
     * @return the face
     */
    Face addFaceOfDicePlayer(UUID idPlayer, int nbDice, int nbPosFace, Face f);

    /**
     * Add history face player.
     *
     * @param idPlayer the id player
     * @param f        the f
     */
    void addHistoryFacePlayer(UUID idPlayer, Face f);

    /**
     * Gets player sort by type ressource.
     *
     * @param tp the tp
     * @return the player sort by type ressource
     */
    List<Player> getPlayerSortByTypeRessource(TypeRessource tp);

    /**
     * Gets card with effect of player.
     *
     * @param idPlayer          the id player
     * @param typeEffectBasique the type effect basique
     * @return the card with effect of player
     */
    List<Card> getCardWithEffectOfPlayer(UUID idPlayer, TypeEffectBasique typeEffectBasique);
}
