package share.choice;

import share.dice.CoupleRollDice;
import share.face.Face;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The type Choice power on dice other player.
 */
public class ChoicePowerOnDiceOtherPlayer extends Choice {

    /**
     * The Roll players.
     */
    public Map<UUID, CoupleRollDice> rollPlayers;
    /**
     * The Face choice.
     */
    public Face faceChoice;

    /**
     * Instantiates a new Choice power on dice other player.
     */
    public ChoicePowerOnDiceOtherPlayer(){
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
