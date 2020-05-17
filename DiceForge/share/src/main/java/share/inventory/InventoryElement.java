package share.inventory;

/**
 * The type Inventory element.
 */
public class InventoryElement {
    /**
     * The Value.
     */
    public int value;
    /**
     * The Value max.
     */
    public int valueMax;

    /**
     * Instantiates a new Inventory element.
     *
     * @param valueMax the value max
     */
    public InventoryElement(int valueMax){
            this.value = 0;
            this.valueMax = valueMax;
    }

    public InventoryElement(){

    }

    @Override
    public String toString(){
            return this.value + "/" + this.valueMax;
        }

}
