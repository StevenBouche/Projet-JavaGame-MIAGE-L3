package share.cards;

import share.cards.effects.Effect;
import share.cards.effects.TypeEffectBasique;
import share.ressource.TypeRessource;
import share.temple.IslandEnum;

import java.util.Map;

/**
 * The type Card.
 */
public class Card {

    /**
     * The Cost.
     */
    public Map<TypeRessource,Integer> cost;
    /**
     * The Glory.
     */
    public int glory;
    /**
     * The Type effect.
     */
    public TypeEffectBasique typeEffect;
    /**
     * The Effect.
     */
    public Effect effect;
    /**
     * The Name.
     */
    public String name;
    /**
     * The Island id.
     */
    public IslandEnum islandId;

    public Cards cardsId;
    /**
     * Instantiates a new Card.
     *
     * @param island the island
     * @param cost   the cost
     * @param glory  the glory
     * @param type   the type
     * @param effect the effect
     * @param name   the name
     */
    public Card(Cards c, IslandEnum island, Map<TypeRessource,Integer> cost, int glory, TypeEffectBasique type, Effect effect, String name){
        this.islandId = island;
        this.name = name;
        this.cost = cost;
        this.glory = glory;
        this.typeEffect = type;
        this.effect = effect;
        this.cardsId = c;
    }

    /**
     * Instantiates a new Card.
     */
    public Card(){

    }

    /**
     * Get cost map.
     *
     * @return the map
     */
    public Map<TypeRessource,Integer> getCost(){
        return cost;
    }

    /**
     * Get glory int.
     *
     * @return the int
     */
    public int getGlory(){
        return glory;
    }


    public String toString(){
        return this.name;
    }
}
