package client.game;


import share.choice.*;
import share.ressource.TypeRessource;
import share.utils.HandleRandom;

import java.util.EnumMap;

/**
 * The type Game manager.
 */
public abstract class GameManager {

    /**
     * The Id.
     */
    String id;
    /**
     * The Handle random.
     */
    HandleRandom handleRandom;

    /**
     * Set uuid.
     *
     * @param id the id
     */
    public void setUUID(String id){ this.id = id;}

    /**
     * Get uuid string.
     *
     * @return the string
     */
    public String getUUID(){return this.id;}

    /**
     * Instantiates a new Game manager.
     */
    public GameManager(){
        this.handleRandom = new HandleRandom();
    }

    /**
     * Choice ressource face hybrid type ressource.
     *
     * @param fh the fh
     * @return the type ressource
     */
    public abstract TypeRessource choiceRessourceFaceHybrid(EnumMap<TypeRessource, Integer> fh);

    /**
     * Choice forge or exploi or nothing.
     *
     * @param ch the ch
     */
    public abstract void choiceForgeOrExploiOrNothing(ChoiceNothingForgeExploit ch);

    /**
     * Choice face in forge.
     *
     * @param ch the ch
     */
    public abstract void choiceFaceInForge(ChoiceNothingForgeExploit ch) ;

    /**
     * Choice one more time.
     *
     * @param ch the ch
     */
    public abstract void choiceOneMoreTime(ChoiceOneMoreTurn ch);

    /**
     * Choice hammer.
     *
     * @param ch the ch
     */
    public abstract void choiceHammer(ChoiceHammer ch) ;

    /**
     * Choice ancient.
     *
     * @param ch the ch
     */
    public abstract void choiceAncient(Choice3GoldFor4Glory ch);

    /**
     * Choice satyre.
     *
     * @param ch         the ch
     * @param choiceStep the choice step
     */
    public abstract void choiceSatyre(ChoiceSatyre ch, int choiceStep);

    /**
     * Choice forge special.
     *
     * @param ch the ch
     */
    public abstract void choiceForgeSpecial(ChoiceForgeFaceSpecial ch);

    /**
     * Choice power other player.
     *
     * @param ch the ch
     */
    public abstract void choicePowerOtherPlayer(ChoicePowerOnDiceOtherPlayer ch);

}

