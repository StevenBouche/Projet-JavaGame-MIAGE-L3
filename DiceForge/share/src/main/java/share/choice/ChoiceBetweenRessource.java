package share.choice;


import share.ressource.TypeRessource;

import java.util.EnumMap;

/**
 * The type Choice hybrid share.face.
 */
public class ChoiceBetweenRessource extends Choice {

    private EnumMap<TypeRessource, Integer> listRessource;
    private TypeRessource typeRessource;

    /**
     * Instantiates a new Choice hybrid share.face.
     *
     * @param listRessource the list ressource
     */
    public ChoiceBetweenRessource(EnumMap<TypeRessource, Integer> listRessource) {
        this.listRessource = listRessource;
    }

    /**
     * Instantiates a new Choice hybrid share.face.
     *
     * @param listRessource the list ressource
     * @param typeRessource the type share.ressource
     */
    public ChoiceBetweenRessource(EnumMap<TypeRessource, Integer> listRessource, TypeRessource typeRessource) {
        this.listRessource = listRessource;
        this.typeRessource = typeRessource;
    }

    /**
     * Instantiates a new Choice hybrid share.face.
     */
    public ChoiceBetweenRessource() {}


    /**
     * Gets type share.ressource.
     *
     * @return the type share.ressource
     */
    public TypeRessource getTypeRessource() {
        return typeRessource;
    }

    /**
     * Sets type share.ressource.
     *
     * @param typeRessource the type share.ressource
     */
    public void setTypeRessource(TypeRessource typeRessource) {
        this.typeRessource = typeRessource;
    }

    /**
     * Gets list ressource.
     *
     * @return the list ressource
     */
    public EnumMap<TypeRessource, Integer> getListRessource() {
        return listRessource;
    }
}
