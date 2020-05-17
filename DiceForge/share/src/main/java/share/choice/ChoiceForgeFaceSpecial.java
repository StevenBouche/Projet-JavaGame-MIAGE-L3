package share.choice;

import share.face.FaceSpecial;

/**
 * The type Choice forge face special.
 */
public class ChoiceForgeFaceSpecial extends Choice {

    /**
     * The Face special.
     */
    public FaceSpecial faceSpecial;

    /**
     * The Index dice.
     */
    public int indexDice;
    /**
     * The Index dice face.
     */
    public int indexDiceFace;

    /**
     * Instantiates a new Choice forge face special.
     *
     * @param face the face
     */
    public ChoiceForgeFaceSpecial(FaceSpecial face){
        this.faceSpecial = face;
    }

    /**
     * Instantiates a new Choice forge face special.
     */
    public ChoiceForgeFaceSpecial(){

    }

}
