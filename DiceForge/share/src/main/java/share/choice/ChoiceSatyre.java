package share.choice;

import share.dice.CoupleRollDice;
import share.face.Face;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The type Choice satyre.
 */
public class ChoiceSatyre extends Choice {

    /**
     * The Roll players.
     */
    public Map<UUID, CoupleRollDice> rollPlayers;
    /**
     * The Face choice one.
     */
    public Face faceChoiceOne;
    /**
     * The Face choice two.
     */
    public Face faceChoiceTwo;

    /**
     * Instantiates a new Choice satyre.
     */
    public ChoiceSatyre(){
        this.rollPlayers = new HashMap<>();
    }

    /**
     * Add rolls players.
     *
     * @param idPlayer the id player
     * @param one      the one
     * @param two      the two
     */
    public void addRollsPlayers(UUID idPlayer, Face one, Face two){
        CoupleRollDice cp = new CoupleRollDice();
        cp.faceOne = one;
        cp.faceTwo = two;
        this.rollPlayers.put(idPlayer,cp);
    }

}
