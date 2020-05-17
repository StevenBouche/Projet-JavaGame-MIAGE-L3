package share.face;

import share.ressource.TypeRessource;

import java.util.EnumMap;

/**
 * The type Face hybrid.
 */
public class FaceHybrid extends Face {

    /**
     * The List share.ressource.
     */
    public EnumMap<TypeRessource, Integer> listRessource;
    private boolean choice;
    /**
     * The Name.
     */
    public String name;

    /**
     * Instantiates a new Face hybrid.
     *
     * @param choice the share.choice
     * @param map    the map
     * @param name   the name
     */
    protected FaceHybrid(boolean choice, EnumMap<TypeRessource, Integer> map, String name) {
        super(TypeFace.HYBRIDE);
        this.choice = choice;
        this.listRessource = map;
        this.name = name;
    }

    public FaceHybrid clone(){
        FaceHybrid fh = new FaceHybrid();
        fh.listRessource = this.getListRessource();
        fh.choice = this.choice;
        fh.name = this.name;
        return fh;
    }

    /**
     * Instantiates a new Face hybrid.
     */
//For serialization
    public FaceHybrid(){ super(TypeFace.HYBRIDE);}

    /**
     * Gets list share.ressource.
     *
     * @return the list share.ressource
     */
    public EnumMap<TypeRessource, Integer> getListRessource() {
        return listRessource.clone();
    }

    /**
     * Is share.choice boolean.
     *
     * @return the boolean
     */
    public boolean isChoice() {
        return choice;
    }

    public String toString(){
        return this.name;
    }


    /**
     * Multiply value.
     *
     * @param i the
     */
    public void multiplyValue(int i) {
        for(TypeRessource type : this.listRessource.keySet()){
            this.listRessource.put(type,this.listRessource.get(type)*i);
        }
    }
}
