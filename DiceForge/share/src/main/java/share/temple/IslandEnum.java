package share.temple;

import share.ressource.TypeRessource;
import share.utils.CoupleValeur;

import java.util.EnumMap;

/**
 * The enum Island enum.
 */
public enum IslandEnum {

    /**
     * Island 1 island enum.
     */
    ISLAND1(1,
            new CoupleValeur(1,TypeRessource.LUNAR),
            new CoupleValeur(1,TypeRessource.LUNAR)
            ),
    /**
     * Island 2 island enum.
     */
    ISLAND2(2,
            new CoupleValeur(2,TypeRessource.LUNAR),
            new CoupleValeur(3,TypeRessource.LUNAR)
    ),
    /**
     * Island 3 island enum.
     */
    ISLAND3(3,
            new CoupleValeur(4,TypeRessource.LUNAR),
            new CoupleValeur(5,TypeRessource.LUNAR)),
    /**
     * Island 4 island enum.
     */
    ISLAND4(4,
            new CoupleValeur(1,TypeRessource.SOLAR),
            new CoupleValeur(1,TypeRessource.SOLAR)),
    /**
     * Island 5 island enum.
     */
    ISLAND5(5,
            new CoupleValeur(2,TypeRessource.SOLAR),
            new CoupleValeur(3,TypeRessource.SOLAR)),
    /**
     * Island 6 island enum.
     */
    ISLAND6(6,
            new CoupleValeur(4,TypeRessource.SOLAR),
            new CoupleValeur(5,TypeRessource.SOLAR)),
    /**
     * Island 7 island enum.
     */
    ISLAND7(7,
            new CoupleValeur(5,TypeRessource.SOLAR),
            new CoupleValeur(5,TypeRessource.LUNAR));

    /**
     * The List.
     */
    final EnumMap<TypeRessource, Integer> list;
    private int id;

    IslandEnum(int id,CoupleValeur... list){
        this.id = id;
        this.list = new EnumMap<TypeRessource, Integer>(TypeRessource.class);
            for(CoupleValeur c : list){
            this.list.put(c.type,c.value);
        }
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return this.id;
    }
}
