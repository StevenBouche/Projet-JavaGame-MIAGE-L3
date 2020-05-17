package share.temple;

import share.cards.Card;
import share.ressource.TypeRessource;

import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

/**
 * The type Island.
 */
public class Island {
    /**
     * The Listcard.
     */
    public List<Card> listcard;
    public EnumMap<TypeRessource, Integer> listCosts;

    /**
     * The Player.
     */
    public UUID player;

    /**
     * The Id.
     */
    public IslandEnum id;

    /**
     * Instantiates a new Island.
     */
    public Island(){

    }

    /**
     * Instantiates a new Island.
     *
     * @param island the island
     */
    public Island(IslandEnum island, EnumMap<TypeRessource, Integer> list) {
        this.id = island;
        this.listCosts = list;
    }

    /**
     * Gets card.
     *
     * @param index the index
     * @return the card
     */
    public Card getCard(int index) {
       return this.listcard.remove(index);
    }

    /**
     * Get index int.
     *
     * @return the int
     */
    public int IdIslandEnum(){
        return this.id.getId();
    }

    /**
     * Get player player.
     *
     * @return the player
     */
    public UUID getPlayer(){
        return this.player;
    }

    public String toString(){
        return this.listcard.toString();
    }
}
