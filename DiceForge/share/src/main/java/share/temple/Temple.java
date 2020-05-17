package share.temple;

import share.cards.Card;
import share.exeption.CaseOfIslandTempleOutOfBound;
import share.exeption.InstanceCardOutOfBoundException;

import java.util.EnumMap;
import java.util.UUID;


/**
 * The type Temple.
 */
public class Temple {

    private EnumMap<IslandEnum,Island> listIsland;

    /**
     * Instantiates a new Temple.
     *
     * @throws InstanceCardOutOfBoundException the instance card out of bound exception
     */
    public Temple()  throws InstanceCardOutOfBoundException {
        FactoryTemple t = new FactoryTemple();
        this.listIsland = t.createIslandWithEnum(Extension.STANDARD);
    }

    /**
     * Get list island array list.
     *
     * @return the array list
     */
    public EnumMap<IslandEnum,Island> getListIsland(){
        return this.listIsland;
    }

    /**
     * Gets card on island.
     *
     * @param islandNumber the island number
     * @param caseIsland   the case island
     * @return the card on island
     * @throws CaseOfIslandTempleOutOfBound the case of island temple out of bound
     */
    public Card getCardOnIsland(int islandNumber,int caseIsland) throws CaseOfIslandTempleOutOfBound{
        if(this.listIsland.containsKey(islandNumber)) return this.listIsland.get(islandNumber).getCard(caseIsland);
        else throw new CaseOfIslandTempleOutOfBound("Island not exist with:"+ islandNumber);
    }

    /**
     * Get island from card island.
     *
     * @param c the c
     * @return the island
     */
    public Island getIslandFromCard(Card c){
        return this.listIsland.get(c.islandId);
    }

    public Island getIsland(IslandEnum is){
        return this.listIsland.get(is);
    }

    /**
     * Add player on island uuid.
     *
     * @param idPlayer the id player
     * @param islandId the island id
     * @return the uuid
     */
    public UUID addPlayerOnIsland(UUID idPlayer, IslandEnum islandId) {
        UUID id = null;
        if(this.listIsland.get(islandId).player != null) id = this.listIsland.get(islandId).player;
        this.listIsland.get(islandId).player = idPlayer;
        return id;
    }
}
