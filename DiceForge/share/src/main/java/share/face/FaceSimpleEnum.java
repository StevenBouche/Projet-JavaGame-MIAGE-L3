package share.face;

import share.config.ConfigFace;
import share.ressource.TypeRessource;

/**
 * The enum Face simple enum.
 */
public enum FaceSimpleEnum {

    /**
     * Gold one share.face simple enum.
     */
    GOLD_ONE(TypeRessource.GOLD,1, ConfigFace.FACE_GOLD_ONE_NB),
    /**
     * Gold three share.face simple enum.
     */
    GOLD_THREE(TypeRessource.GOLD,3, ConfigFace.FACE_GOLD_THREE_NB),
    /**
     * Gold four share.face simple enum.
     */
    GOLD_FOUR(TypeRessource.GOLD,4, ConfigFace.FACE_GOLD_FOUR_NB),
    /**
     * Gold six share.face simple enum.
     */
    GOLD_SIX(TypeRessource.GOLD,6, ConfigFace.FACE_GOLD_SIX_NB),
    /**
     * Lunar one share.face simple enum.
     */
    LUNAR_ONE(TypeRessource.LUNAR,1, ConfigFace.FACE_LUNAR_ONE_NB),
    /**
     * Lunar two share.face simple enum.
     */
    LUNAR_TWO(TypeRessource.LUNAR,2, ConfigFace.FACE_LUNAR_TWO_NB),
    /**
     * Solar one share.face simple enum.
     */
    SOLAR_ONE(TypeRessource.SOLAR,1, ConfigFace.FACE_SOLAR_ONE_NB),
    /**
     * Solar two share.face simple enum.
     */
    SOLAR_TWO(TypeRessource.SOLAR,2, ConfigFace.FACE_SOLAR_TWO_NB),
    /**
     * Glory two share.face simple enum.
     */
    GLORY_TWO(TypeRessource.GLORY,2, ConfigFace.FACE_GLORY_TWO_NB),
    /**
     * Glory three share.face simple enum.
     */
    GLORY_THREE(TypeRessource.GLORY,3, ConfigFace.FACE_GLORY_TWO_NB),
    /**
     * Glory four share.face simple enum.
     */
    GLORY_FOUR(TypeRessource.GLORY,4, ConfigFace.FACE_GLORY_TWO_NB);

    /**
     * The Type.
     */
    public TypeRessource type;
    /**
     * The Value.
     */
    public int value;
    /**
     * The Nb instance max.
     */
    public int nbInstanceMax;

    FaceSimpleEnum(TypeRessource t, int value, int nbInstanceMax){
        this.type = t;
        this.value = value;
        this.nbInstanceMax = nbInstanceMax;
    }






}
