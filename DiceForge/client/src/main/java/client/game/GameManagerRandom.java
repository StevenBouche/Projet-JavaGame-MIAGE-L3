package client.game;

import share.cards.Card;
import share.cards.model.Hammer;
import share.choice.*;
import share.dice.Dice;
import share.exeption.CaseOfPoolForgeOutOfBound;
import share.face.Face;
import share.ressource.TypeRessource;
import share.temple.IslandEnum;
import share.utils.Printer;

import java.util.*;

/**
 * The type Game manager.
 */
public class GameManagerRandom extends GameManager implements IActionGameServer {

    private boolean haveTryForge;
    private boolean haveTryExploit;
    private boolean wantBuyOtherFace;

    /**
     * Instantiates a new Game manager random.
     */
    public GameManagerRandom(){
        super();
    }

    /**
     * Select a random ressource from an hybrid face
     *
     * @param fh the hybrid face
     * @return the random ressource
     */
    @Override
    public TypeRessource choiceRessourceFaceHybrid(EnumMap<TypeRessource, Integer> fh) {
        TypeRessource randomRessource = null;
        int rand = this.handleRandom.getRandomBetweenMinMax(0, fh.size() - 1);
        int i = 0;
        for (TypeRessource e : fh.keySet()){
            if (i++ == rand){
                randomRessource = e;
                break;
            }
        }
        return randomRessource;
    }

    /**
     * Call the choice face in the forge or the choice of temple
     */
    @Override
    public void choiceForgeOrExploiOrNothing(ChoiceNothingForgeExploit ch){        //Choice est un int (0 : Nothing, 1 : Forge, 2 : Exploit).
        this.haveTryForge = false;
        this.haveTryExploit = false;
        this.wantBuyOtherFace = false;
        int v = this.handleRandom.getRandomBetweenMinMax(0,1);
        if(v == 0) this.choiceFaceInForge(ch);
        else this.choiceTemple(ch);
    }

    /**
     * Method recursive with redirection to method choiceFaceForge if player have not try forge face before
     * End point : list of island id empty
     * Method get random element and remove it if can't buy item
     *
     * @param ch ChoiceNothingForgeExploit object
     */
    private void choiceTemple(ChoiceNothingForgeExploit ch) {
        // si le temple n'a pas d'ile alors redirige ou ne fais rien
        if(ch.listIslandTemple.isEmpty()){
            if(this.haveTryForge){// si on vient d'essayer de forge alors on fais rien sinon on essaye de forger une face vu que l'on peux pas acheter de carte
                ch.choice = EnumTypeChoice.NOTHING;
                Printer.getInstance().logClient("Le joueur a choisi de faire car plus d'ile : " + Printer.BLUE + EnumTypeChoice.NOTHING, id);
            } else {
                this.haveTryExploit = true;
                this.choiceFaceInForge(ch);
            }
            return;
        }
        // tire index d une ile entre 1 et 7
        int randIsland = this.handleRandom.getRandomBetweenMinMax(1,7);
        IslandEnum island = null;
        for(IslandEnum i : ch.listIslandTemple.keySet()){
            if(i.getId() == randIsland) island = i;
        }
        //si l'ile n'existe pas on retry
        if(island == null ) this.choiceTemple(ch);
        else if(ch.listIslandTemple.get(island) == null) this.choiceTemple(ch);
        else if(ch.listIslandTemple.get(island).listcard.isEmpty()){ // SI L ILE EXIST MAIS QUE CA LIST EST VIDE ON SUPPRIME L ILE DE LA LIST ET ON RETENTE
            ch.listIslandTemple.remove(island);
            this.choiceTemple(ch);
        } else { //sinon cherche une case random
            int randIslandCase = this.handleRandom.getRandomBetweenMinMax(0,ch.listIslandTemple.get(island).listcard.size()-1);
            Card c = ch.listIslandTemple.get(island).listcard.get(randIslandCase);
            if(this.cantBuyCard(c,ch)) { // le joueur ne peux pas acheter la carte
                ch.listIslandTemple.remove(island);
                this.choiceTemple(ch);
            } else {
                ch.choice = EnumTypeChoice.EXPLOIT;
                ch.choiceIsland = island;
                ch.choiceIndexIslandCase = randIslandCase;
                Printer.getInstance().logClient("Le joueur veut acheter la carte : " + Printer.BLUE + c.name, id);
            }
        }
    }

    /**
     * Check if the player CANT buy the card
     *
     * @param c the card
     * @param ch ChoiceNothingForgeExploit
     * @return if the player CANT buy the card
     */
    public boolean cantBuyCard(Card c, ChoiceNothingForgeExploit ch){
        boolean b = false;
        for(TypeRessource type : c.cost.keySet()) if(c.cost.get(type) > ch.ressourceOfPlayer.get(type).value) b= true;
        // regarde si je peux acheter la carte que j'ai tirer aléatoirement
        return b;
    }

    /**
     * Method recursive with redirection to method choiceTemple if player have not try forge face before
     * End point : list of pool temple is empty
     * Method get random element and remove it if can't buy item
     *
     * @param ch ChoiceNothingForgeExploit object
     */
    @Override
    public void choiceFaceInForge(ChoiceNothingForgeExploit ch)  {

        ch.choice = EnumTypeChoice.FORGE;
        //Si plus de pool ou na pas assez d'argent alors ne fais rien
        if(ch.poolForge.isEmpty()) { this.switchForgeToTemple(ch); return; }

        // determine la limite max et min pour le random
        Set<Integer> clone = ch.poolForge.keySet();
        List<Integer> costsPool = new ArrayList<Integer>(clone);
        Collections.sort(costsPool);
        int max = costsPool.get(costsPool.size()-1);
        int min = costsPool.get(0);

        int rand = this.handleRandom.getRandomBetweenMinMax(0,costsPool.size()-1);
        if(ch.poolForge.get(costsPool.get(rand)) == null || ch.poolForge.get(costsPool.get(rand)).listface.isEmpty()){
            ch.poolForge.remove(costsPool.get(rand));
            this.choiceFaceInForge(ch);
            return;
        }

        if(ch.poolForge.get(costsPool.get(rand)).getCost() > ch.ressourceOfPlayer.get(TypeRessource.GOLD).value){
            ch.poolForge.remove(costsPool.get(rand));
            this.choiceFaceInForge(ch);
            return;
        }
        else {
            Printer.getInstance().logClient("Le joueur a choisi de faire : " + Printer.BLUE + EnumTypeChoice.FORGE,id);
            ChoiceFaceOnDice choice = new ChoiceFaceOnDice();
            choice.choicePool = costsPool.get(rand);
            choice.choiceIndexPool = this.handleRandom.getRandomBetweenMinMax(0,ch.poolForge.get(choice.choicePool).listface.size()-1);
            choice.choiceIndexDice = this.handleRandom.getRandomBetweenMinMax(0,1);
            choice.choiceIndexFaceDice = this.handleRandom.getRandomBetweenMinMax(0,5);
            Dice d;
            if(choice.choiceIndexDice == 0) d = ch.d1;
            else d = ch.d2;
            Printer.getInstance().logClient("Le joueur a choisi de remplacer : " +
                    Printer.BLUE + ch.poolForge.get(choice.choicePool).listface.get(choice.choiceIndexPool).toString() +
                    " - DICE" +choice.choiceIndexDice+"/"+choice.choiceIndexFaceDice+" : "+
                    d.getFace(choice.choiceIndexFaceDice).toString(), id);
            this.wantBuyOtherFace = true;
            ch.listChoiceForge.add(choice);
            try {
                ch.ressourceOfPlayer.get(TypeRessource.GOLD).value-=ch.poolForge.get(choice.choicePool).getCost();
                Face f = ch.poolForge.get(choice.choicePool).getFaceWithRemove(choice.choiceIndexPool); //remove la face de la list
                this.choiceFaceInForge(ch);
            } catch (CaseOfPoolForgeOutOfBound caseOfPoolForgeOutOfBound) {
                caseOfPoolForgeOutOfBound.printStackTrace();
            }
        }
    }

    /**
     * redirection forge to temple
     * @param ch Choice object
     */
    private void switchForgeToTemple(ChoiceNothingForgeExploit ch){
        if(this.haveTryExploit){ // si on vient d'exploit alors on fais aucun choix sinon essaye d'acheter une carte
            ch.choice = EnumTypeChoice.NOTHING;
            Printer.getInstance().logClient("Le joueur a choisi de faire : " + Printer.BLUE + EnumTypeChoice.NOTHING, id);
        } else if(!this.wantBuyOtherFace) {
            this.haveTryForge = true;
            this.choiceTemple(ch);
        }
    }

    /**
     * Choice if player want one more action in turn
     * @param ch choice object
     */
    @Override
    public void choiceOneMoreTime(ChoiceOneMoreTurn ch) {
        int result = this.handleRandom.getRandomBetweenMinMax(0,1);
        if(ch.valueSolaryPlayer > 2 && result == 0) {
            ch.choice = true;
            Printer.getInstance().logClient("Le joueur a choisi de faire une autre action.", id);
        }
        else {
            Printer.getInstance().logClient("Le joueur  n'a pas choisi de faire une autre action.", id);
            ch.choice = false;
        }
    }

    /**
     * Choice player if player want add gold in her hammer
     * @param ch choice object
     */
    @Override
    public void choiceHammer(ChoiceHammer ch) {
        // prendre en compte et changer en liste de hammer
        Hammer h = ch.getHammersPlayer();
        int resteHammer = h.getMaxGold() - h.getCurrentGold();
        int min = 0;
        int max = ch.getValueGold();
        if(resteHammer < max) max = resteHammer;
        if(resteHammer > min){
            int rand = this.handleRandom.getRandomBetweenMinMax(0,1);
            ch.setValueToHammer(rand);
        } else ch.setValueToHammer(0);

/*
        if(ch.getValueGold() == 1){ // si le choix est 1 alors choice entre 1 et 0 rien mettre
            int rand = this.handleRandom.getRandomBetweenMinMax(0,1);
            ch.setValueToHammer(rand);
        }
        else if(ch.getValueGold() >= 1 && ch.getValueGold() <= resteHammer){
            //si value superieur ou egale a 1 et inferieur ou egale
            int rand = this.handleRandom.getRandomBetweenMinMax(0,ch.getValueGold());
            ch.setValueToHammer(rand);
        }
        else if(resteHammer >= 1 && ch.getValueGold() >= resteHammer){
            int rand = this.handleRandom.getRandomBetweenMinMax(0,resteHammer);
            ch.setValueToHammer(rand);
        }
        else {
            ch.setValueToHammer(0);
        }*/
        Printer.getInstance().logClient("Le joueur veut ajouter "+ch.getValueToHammer()+" GOLD au hammer",id);
    }

    @Override
    public void choiceAncient(Choice3GoldFor4Glory ch) {
        int v = this.handleRandom.getRandomBetweenMinMax(0,1);
        if(v == 0) {
            Printer.getInstance().logClient("Le joueur n'accepte pas 3 gold pour 4 glory ",id);
            ch.choice = false;
        }
        else {
            Printer.getInstance().logClient("Le joueur accepte 3 gold pour 4 glory ",id);
            ch.choice = true;
        }

    }

    @Override
    public void choiceSatyre(ChoiceSatyre ch, int choiceStep) {

        int nbPlayer = ch.rollPlayers.size();
        int roll = this.handleRandom.getRandomBetweenMinMax(1,nbPlayer*2);
        int loop = 1;
        Face f = null;
        for(UUID id : ch.rollPlayers.keySet()){
            if(loop == roll) {
                f = ch.rollPlayers.get(id).faceOne;
                break;
            }
            loop++;
            if(loop == roll) {
                f = ch.rollPlayers.get(id).faceTwo;
                break;
            }
            loop++;
        }

        if(choiceStep == 0){
            ch.faceChoiceOne = f;
            choiceStep++;
            this.choiceSatyre(ch,choiceStep);
            return;
        } else if (choiceStep == 1 && !ch.faceChoiceOne.equals(f)){
            ch.faceChoiceTwo = f;
        } else {
            this.choiceSatyre(ch,choiceStep);
            return;
        }
        Printer.getInstance().logClient("Le joueur a choisi pour le satyre "+ch.faceChoiceOne+" "+ch.faceChoiceTwo,id);
    }

    @Override
    public void choiceForgeSpecial(ChoiceForgeFaceSpecial ch) {
        ch.indexDice = this.handleRandom.getRandomBetweenMinMax(0,1);
        ch.indexDiceFace = this.handleRandom.getRandomBetweenMinMax(0,5);
        Printer.getInstance().logClient("Le joueur veut remplacer le dice "+ch.indexDice+" face "+ch.indexDiceFace+" pour forger "+ch.faceSpecial,id);
    }

    @Override
    public void choicePowerOtherPlayer(ChoicePowerOnDiceOtherPlayer ch) {
        int nbPlayer = ch.rollPlayers.size();
        int roll = this.handleRandom.getRandomBetweenMinMax(1,nbPlayer*2);
        int loop = 1;
        UUID idUse = null;
        Face f = null;
        for(UUID id : ch.rollPlayers.keySet()){
            idUse=id;
            if(loop == roll) {
                f = ch.rollPlayers.get(id).faceOne;
                break;
            }
            loop++;
            if(loop == roll) {
                f = ch.rollPlayers.get(id).faceTwo;
                break;
            }
            loop++;
        }
        ch.faceChoice = f;
        Printer.getInstance().logClient("Le joueur veut utiliser la face "+ch.faceChoice+" du player "+idUse,id);
    }
}
