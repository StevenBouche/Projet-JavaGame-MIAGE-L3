
package share.face;

import share.ressource.TypeRessource;

/**
 * The type Face simple.
 */
public class FaceSimple extends Face {

    private TypeRessource typeRessource;
    private int value;

    /**
     * Instantiates a new Face simple.
     *
     * @param typeRessource the type share.ressource
     * @param value         the value
     */
    public FaceSimple(TypeRessource typeRessource, int value){
        super(TypeFace.SIMPLE);
        this.typeRessource = typeRessource;
        this.value = value;
    }

    /**
     * Instantiates a new Face simple.
     */
//For serialization
    public FaceSimple(){  super(TypeFace.SIMPLE);}


    /**
     * Gets type share.ressource.
     *
     * @return the type share.ressource
     */
    public TypeRessource getTypeRessource() {
        return typeRessource;
    }

    /**
     * Get value int.
     *
     * @return the int
     */
    public int getValue(){
        return value;
    }


    public String toString(){
        return this.value+" of "+this.typeRessource;
    }

 //   public void multiplyValue(int v){
  //      this.value = this.value * v;
  //  }

}

