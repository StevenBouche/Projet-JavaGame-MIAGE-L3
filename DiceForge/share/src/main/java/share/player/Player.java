package share.player;

import share.inventory.FactoryInventory;
import share.inventory.Inventory;

import java.util.UUID;

/**
 * The type Player.
 */
public class Player {
    private Inventory inventory;
    private UUID id;
    public String version;
    public int nbPlayer;

    /**
     * Instantiates a new Player.
     *
     * @param id the id
     */
    public Player(UUID id, String version){
        this.version = version;
        this.id = id;
        this.inventory = FactoryInventory.createInventory();
    }

    public Player(UUID id, String version, int idP){
        this.nbPlayer = idP;
        this.version = version;
        this.id = id;
        this.inventory = FactoryInventory.createInventory();
    }

    /**
     * Get id uuid of the Player.
     *
     * @return the uuid
     */
    public UUID getId(){
        return this.id;
    }

    /**
     * Get share.inventory share.inventory.
     *
     * @return the share.inventory
     */
    public Inventory getInventory(){
        return this.inventory;
    }

    public String toString(){
        return "PLAYER "+this.version+"-"+id+" "+this.inventory.toString();
    }

}
