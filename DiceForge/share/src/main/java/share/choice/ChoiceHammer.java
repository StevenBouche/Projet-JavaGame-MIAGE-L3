package share.choice;

import share.cards.model.Hammer;


/**
 * The type Choice hammer.
 */
public class ChoiceHammer extends Choice {

    private Hammer hammersPlayer;
    private int valueGold;
    private int valueToHammer;

    /**
     * Instantiates a new Choice hammer.
     */
    public ChoiceHammer(){

    }

    /**
     * Gets hammers player.
     *
     * @return the hammers player
     */
    public Hammer getHammersPlayer() {
        return hammersPlayer;
    }

    /**
     * Sets hammers player.
     *
     * @param hammersPlayer the hammers player
     */
    public void setHammersPlayer(Hammer hammersPlayer) {
        this.hammersPlayer = hammersPlayer;
    }

    /**
     * Gets value gold.
     *
     * @return the value gold
     */
    public int getValueGold() {
        return valueGold;
    }

    /**
     * Sets value gold.
     *
     * @param valueGold the value gold
     */
    public void setValueGold(int valueGold) {
        this.valueGold = valueGold;
    }

    /**
     * Gets value to hammer.
     *
     * @return the value to hammer
     */
    public int getValueToHammer() {
        return valueToHammer;
    }

    /**
     * Sets value to hammer.
     *
     * @param valueToHammer the value to hammer
     */
    public void setValueToHammer(int valueToHammer) {
        this.valueToHammer = valueToHammer;
    }
}
