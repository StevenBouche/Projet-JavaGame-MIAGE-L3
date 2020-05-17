package share.cards.model;

import share.config.ConfigGame;

/**
 * The type Hammer.
 */
public class Hammer {

    private int nbStep;
    private int maxGold;
    private int currentGold;
    private boolean finish;

    /**
     * Instantiates a new Hammer.
     */
    public Hammer(){
        this.finish = false;
        this.nbStep = 0;
        this.maxGold = ConfigGame.MAX_GOLD_HAMMER;
        this.currentGold = 0;
    }

    /**
     * Gets nb step.
     *
     * @return the nb step
     */
    public int getNbStep() {
        return nbStep;
    }

    /**
     * Sets nb step.
     *
     * @param nbStep the nb step
     */
    public void setNbStep(int nbStep) {
        this.nbStep = nbStep;
    }

    /**
     * Gets max gold.
     *
     * @return the max gold
     */
    public int getMaxGold() {
        return maxGold;
    }

    /**
     * Gets current gold.
     *
     * @return the current gold
     */
    public int getCurrentGold() {
        return currentGold;
    }

    /**
     * Sets current gold.
     *
     * @param currentGold the current gold
     */
    public void setCurrentGold(int currentGold) {
        this.currentGold = currentGold;
    }

    /**
     * Add gold int.
     *
     * @param value the value
     * @return the int
     */
    public int addGold(int value){
        int reste = 0;
        if(this.nbStep < 2){ //si pas fini
            if((currentGold + value) >= maxGold) { // si les la futur inc superieur ou egale a la limite du hammer
                reste =  (currentGold + value) - maxGold; //calcule de la différence
                nbStep++; // on inc letape en cour
                this.currentGold += (value-reste); //on ajoute la différence
                if(this.getNbStep() != 2) this.currentGold = 0;
                else this.finish = true;
            }else this.currentGold += value;
        }
        return reste;
    }

    public String toString(){
        return "NbStep : "+nbStep+" , Gold : "+currentGold+"/"+maxGold;
    }

    /**
     * Is finish boolean.
     *
     * @return the boolean
     */
    public boolean isFinish() {
        return finish;
    }
}
