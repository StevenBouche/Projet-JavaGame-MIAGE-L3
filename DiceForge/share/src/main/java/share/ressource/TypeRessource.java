package share.ressource;

import share.config.ConfigPlayer;

import java.util.Arrays;
import java.util.List;

/**
 * The enum Type share.ressource.
 */
public enum TypeRessource {

    /**
     * Solar type share.ressource.
     */
    SOLAR(ConfigPlayer.SOLARY_PLAYER_MAX_INIT),
    /**
     * Lunar type share.ressource.
     */
    LUNAR(ConfigPlayer.LUNAR_PLAYER_MAX_INIT),
    /**
     * Gold type share.ressource.
     */
    GOLD(ConfigPlayer.GOLD_PLAYER_MAX_INIT),
    /**
     * Glory type share.ressource.
     */
    GLORY(ConfigPlayer.GLORY_PLAYER_MAX_INIT);

    private int valueMaxDefault;

    TypeRessource(int value){
        this.valueMaxDefault = value;
    }

    /**
     * Get list type list.
     *
     * @return the list
     */
    public static synchronized List<TypeRessource> getListType(){
        return Arrays.asList(TypeRessource.values());
    }

    /**
     * Get value max default int.
     *
     * @return the int
     */
    public int getValueMaxDefault(){
        int max;
        max = this.valueMaxDefault;
        return max;
    }

}
