package share.choice;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The type Choice 3 gold for 4 glory.
 */
@JsonTypeName("Choice3GoldFor4Glory")
public class Choice3GoldFor4Glory extends Choice {

    /**
     * The Value gold.
     */
    public int valueGold;
    /**
     * The Choice.
     */
    public boolean choice;

    /**
     * Instantiates a new Choice 3 gold for 4 glory.
     *
     * @param value the value
     */
    public Choice3GoldFor4Glory(int value){
        this.valueGold = value;
    }

    /**
     * Instantiates a new Choice 3 gold for 4 glory.
     */
    public Choice3GoldFor4Glory(){

    }
}
