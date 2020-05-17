package share.inventory;


/**
 * The type Factory share.inventory.
 */
public class FactoryInventory {

    /**
     * Create share.inventory share . share.inventory . share.inventory.
     *
     *
     * @return the share . share.inventory . share.inventory
     */
    public static synchronized   Inventory createInventory(){
        return new Inventory();
    }

}
