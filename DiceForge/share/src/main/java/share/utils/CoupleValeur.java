package share.utils;

import share.ressource.TypeRessource;

public class CoupleValeur {
    /**
     * The Value.
     */
    public int value;
    /**
     * The Type.
     */
    public TypeRessource type;


    /**
     * Instantiates a new Couple valeur.
     *
     * @param value the value
     * @param t     the t
     */
    public CoupleValeur(int value,TypeRessource t){
        this.type = t;
        this.value = value;
    }
}

