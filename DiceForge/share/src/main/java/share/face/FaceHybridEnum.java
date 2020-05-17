package share.face;

import share.ressource.TypeRessource;

import java.util.EnumMap;

/**
 * The type Couple share.ressource value.
 */
class CoupleRessourceValue{
    /**
     * The Value.
     */
    int value;
    /**
     * The Type.
     */
    TypeRessource type;

    /**
     * Instantiates a new Couple share.ressource value.
     *
     * @param t     the t
     * @param value the value
     */
    CoupleRessourceValue(TypeRessource t, int value){
        this.value = value;
        this.type = t;
    }
}

/**
 * The enum Face hybrid enum.
 */
public enum FaceHybridEnum {

    /**
     * The Gold 3 or glory 2.
     */
    GOLD3_OR_GLORY2(true, 4,
            new CoupleRessourceValue(TypeRessource.GOLD,3),
            new CoupleRessourceValue(TypeRessource.GLORY,2)
    ),
    /**
     * The Glory 1 solar 1.
     */
    GLORY1_SOLAR1(false,1,
            new CoupleRessourceValue(TypeRessource.GLORY,1),
            new CoupleRessourceValue(TypeRessource.SOLAR,1)
    ),
    /**
     * The Gold 1 or solar 1 or lunar 1.
     */
    GOLD1_OR_SOLAR1_OR_LUNAR1(true,1,
            new CoupleRessourceValue(TypeRessource.GOLD,1),
            new CoupleRessourceValue(TypeRessource.SOLAR,1),
            new CoupleRessourceValue(TypeRessource.LUNAR,1)
    ),
    /**
     * The Gold 2 lunar 1.
     */
    GOLD2_LUNAR1(false,1,
            new CoupleRessourceValue(TypeRessource.GOLD,2),
            new CoupleRessourceValue(TypeRessource.LUNAR,1)
    ),
    /**
     * The Gold 1 glory 1 solar 1 lunar 1.
     */
    GOLD1_GLORY1_SOLAR1_LUNAR1(false,1,
            new CoupleRessourceValue(TypeRessource.GOLD,1),
            new CoupleRessourceValue(TypeRessource.LUNAR,1),
            new CoupleRessourceValue(TypeRessource.SOLAR,1),
            new CoupleRessourceValue(TypeRessource.GLORY,1)
    ),
    /**
     * The Glory 2 lunar 2.
     */
    GLORY2_LUNAR2(false,1,
            new CoupleRessourceValue(TypeRessource.GLORY,2),
            new CoupleRessourceValue(TypeRessource.LUNAR,2)
    ),
    /**
     * The Gold 2 or solar 2 or lunar 2.
     */
    GOLD2_OR_SOLAR2_OR_LUNAR2(true,1,
            new CoupleRessourceValue(TypeRessource.GOLD,2),
            new CoupleRessourceValue(TypeRessource.SOLAR,2),
            new CoupleRessourceValue(TypeRessource.LUNAR,2)
    );


    /**
     * The Choice.
     */
    final boolean choice;
    /**
     * List share.ressource share.face hybrid enum.
     */
    final EnumMap<TypeRessource, Integer> listRessource;
    /**
     * The Nb instance max.
     */
    final int nbInstanceMax;

    FaceHybridEnum(boolean choice, int nbInstanceMax, CoupleRessourceValue... elements){
        this.choice = choice;
        this.nbInstanceMax = nbInstanceMax;
        this.listRessource =  new EnumMap<>(TypeRessource.class);
        for(CoupleRessourceValue c : elements){
            this.listRessource.put(c.type,c.value);
        }
    }


}
