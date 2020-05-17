package share.face;

/**
 * The type Face special.
 */
public class FaceSpecial extends Face{

    /**
     * The Name.
     */
    public String name;
    /**
     * The Face enum.
     */
    public FaceSpecialEnum faceEnum;

    /**
     * Instantiates a new Face special.
     *
     * @param name the name
     * @param face the face
     */
    public FaceSpecial(String name, FaceSpecialEnum face) {
        super(TypeFace.SPECIAL);
        this.name = name;
        this.faceEnum = face;
    }

    /**
     * Instantiates a new Face special.
     */
    public FaceSpecial(){
        super(TypeFace.SPECIAL);
    }

    public String toString(){
        return "FaceSpecial-"+faceEnum.toString();
    }

}
