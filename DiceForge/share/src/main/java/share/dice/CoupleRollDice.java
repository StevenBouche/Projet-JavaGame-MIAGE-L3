package share.dice;

import share.face.Face;

/**
 * The type Couple roll dice.
 */
public class CoupleRollDice{
    /**
     * The Face one.
     */
    public Face faceOne;
    /**
     * The Face two.
     */
    public Face faceTwo;
    public String toString(){
        return  this.faceOne+"/"+this.faceTwo;
    }
}
