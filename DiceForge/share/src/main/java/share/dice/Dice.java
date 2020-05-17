package share.dice;

import share.face.Face;
import share.utils.HandleRandom;
import java.util.HashMap;

/**
 * The type Dice.
 */
public class Dice  {

    public HandleRandom handleRandom;
    /**
     * The Map faces.
     */
    public HashMap<Integer, Face> mapFaces;
    /**
     * The Index last roll.
     */
    public int indexLastRoll;

    /**
     * Instantiates a new Dice.
     */
    public Dice(){
        this.mapFaces = new HashMap<>();
        this.handleRandom = new HandleRandom();
    }


    /**
     * Add index and type of share.face in hashmap.
     *
     * @param index the index
     * @param face  the share.face
     */
    public void addFace(int index, Face face){
            this.mapFaces.put(index,face);
        }

    /**
     * Remove Face.
     *
     * @param index the index
     * @return share.face share.face
     */
    public Face removeFace(int index){
            return this.mapFaces.remove(index);
        }

    /**
     * Roll share.dice.
     *
     * @return the share.face.
     */
    public Face roll(){
        this.indexLastRoll = this.handleRandom.getRandomBetweenMinMax(0,5);
        return this.mapFaces.get(this.indexLastRoll);
    }

    /**
     * Get share.face share.face.
     *
     * @param index the index
     * @return the share.face
     */
    public Face getFace(int index){
        return this.mapFaces.get(index);
    }

    public String toString(){
        String str ="";
        for(Face f : this.mapFaces.values()){
            str += f.toString()+" | ";
        }
        return str;
    }
}
