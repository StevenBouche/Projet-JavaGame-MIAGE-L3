package client.game;

import share.cards.Card;

import share.choice.*;
import share.dice.Dice;

import share.exeption.CaseOfPoolForgeOutOfBound;
import share.forge.PoolEnum;


import share.ressource.TypeRessource;
import share.temple.Island;
import share.temple.IslandEnum;
import share.utils.Printer;

import java.util.*;

/**
 * The type Game manager version 2.
 */
class GameManagerVersionSimple extends GameManager {

    private boolean haveTryForge;
    private boolean haveTryExploit;
    private boolean wantBuyOtherFace;
    /**
     * Instantiates a new Game manager version 2.
     */
    public GameManagerVersionSimple(){
        super();
    }

    @Override
    public TypeRessource choiceRessourceFaceHybrid(EnumMap<TypeRessource, Integer> fh) {
        TypeRessource premierRes = null;

        Set<TypeRessource> ensRess = fh.keySet();
        premierRes = ensRess.iterator().next();
        return premierRes;
    }


    @Override
    public void choiceForgeOrExploiOrNothing(ChoiceNothingForgeExploit ch){
        this.haveTryForge = false;
        this.haveTryExploit = false;
        int v = this.handleRandom.getRandomBetweenMinMax(0,1);

        if(v == 0) this.choiceFaceInForge(ch);
        else this.choiceTemple(ch);

    }

    private void choiceTemple(ChoiceNothingForgeExploit ch) {
        ch.choice = EnumTypeChoice.EXPLOIT;

        //SI LE TEMPLE NA PAS D ILE ALORS NE FAIS RIEN DONC NOTHING  // 1 CAR ON NE TIRE PAS LES CARTES DE L ILE 7 POUR L INSTANT
        if(ch.listIslandTemple.size() <= 1){

            if(this.haveTryForge){ // si on vient d'essayer de forge alors on fais rien sinon on essaye de forger une face vu que l'on peux pas acheter de carte
                ch.choice = EnumTypeChoice.NOTHING;
                Printer.getInstance().logClient("Le joueur a choisi de faire car plus d'ile : " + Printer.BLUE + EnumTypeChoice.NOTHING, this.getUUID());
            } else {
                this.haveTryExploit = true;
                this.choiceFaceInForge(ch);
            }

            return;
        }
        List<Island> listIle = new ArrayList<>();
        for(IslandEnum i : ch.listIslandTemple.keySet()){
            listIle.add(ch.listIslandTemple.get(i));
        }

        getIslandSortByCost(listIle);

        Island island = listIle.get(0);


        //SI L ILE N EXISTE PAS ON RETENTE

        if(ch.listIslandTemple.get(island.id).listcard.isEmpty()){ // SI L ILE EXIST MAIS QUE CA LIST EST VIDE ON SUPPRIME L ILE DE LA LIST ET ON RETENTE
            ch.listIslandTemple.remove(island.id);
            this.choiceTemple(ch);
        } else { //SINON CHERCHE UNE CASE SUR L ILE RANDOM


                    //ch.choiceIndexIsland = randIsland; // todo int to IslandEnum
            ch.choiceIsland = island.id;
            ch.choiceIndexIslandCase = 0;
            Card c = ch.listIslandTemple.get(island.id).listcard.get(ch.choiceIndexIslandCase);

            if(this.cantBuyCard(c,ch)) { // le joueur ne peux pas acheter la carte
                ch.listIslandTemple.get(island.id).listcard.remove(0);
                this.choiceTemple(ch);
            } else Printer.getInstance().logClient("Le joueur veut acheter la carte : " + Printer.BLUE + c.name, this.getUUID());

        }

    }

    private boolean cantBuyCard(Card c, ChoiceNothingForgeExploit ch){
        boolean b = false;
        for(TypeRessource type : c.cost.keySet()) if(c.cost.get(type) > ch.ressourceOfPlayer.get(type).value) b= true;
        // regarde si je peux acheter la carte que j'ai tirer aléatoirement
        return b;
    }

    @Override
    public void choiceFaceInForge(ChoiceNothingForgeExploit ch) {

        ch.choice = EnumTypeChoice.FORGE;

 /*       if(ch.poolForge.isEmpty()) {
            this.switchForgeToTemple(ch);
            return;
        };*/

        // determine la limite max et min pour le random
        List<Integer> costsPool = PoolEnum.getCostsPool();
        int i = 0;
        int sizePool = costsPool.size();
        int min = -1;

        for (int k = 0; k < sizePool; k++){
            min = costsPool.get(k);
            if(ch.poolForge.get(min) != null) break;
        }

        if(min == -1){
            this.switchForgeToTemple(ch);
            return;
        }

        //Si plus de pool ou na pas assez d'argent alors ne fais rien
        if(ch.ressourceOfPlayer.get(TypeRessource.GOLD).value < min) {
            this.switchForgeToTemple(ch);
            return;
        }

        if(ch.poolForge.get(min) == null){
            //System.out.print("test");
        }
        // choisi un pool dans la share.forge
        if(ch.poolForge.get(min).listface != null && !(ch.poolForge.get(min).listface.isEmpty())) {
            ChoiceFaceOnDice choice = new ChoiceFaceOnDice();
            choice.choicePool = min;
            Printer.getInstance().logClient("Le joueur a choisi de faire : " + Printer.BLUE + EnumTypeChoice.FORGE,this.getUUID());
            choice.choiceIndexPool = 0;
            choice.choiceIndexDice = this.handleRandom.getRandomBetweenMinMax(0,1);
            choice.choiceIndexFaceDice = this.handleRandom.getRandomBetweenMinMax(0,5);
            Dice d;
            if(choice.choiceIndexDice == 0) d = ch.d1;
            else d = ch.d2;
            Printer.getInstance().logClient("Le joueur a choisi de remplacer : " +
                    Printer.BLUE + ch.poolForge.get(choice.choicePool).listface.get(choice.choiceIndexPool).toString() +
                    " - DICE" +choice.choiceIndexDice+"/"+choice.choiceIndexFaceDice+" : "+
                    d.getFace(choice.choiceIndexFaceDice).toString(), this.getUUID());
            this.wantBuyOtherFace = true;
            ch.ressourceOfPlayer.get(TypeRessource.GOLD).value-=min;
            ch.listChoiceForge.add(choice);
            try {
                ch.poolForge.get(choice.choicePool).getFaceWithRemove(choice.choiceIndexPool); //remove la face de la list
            } catch (CaseOfPoolForgeOutOfBound caseOfPoolForgeOutOfBound) {
                caseOfPoolForgeOutOfBound.printStackTrace();
            }
            this.choiceFaceInForge(ch);
        }
        else {
            ch.poolForge.remove(min);
            this.choiceFaceInForge(ch);
            return;
        }
                //si le pool est vide remove de la list et nouvelle tentative sinon choisi une share.face dans la pool et choisi la share.face de son share.dice a enlever
    }

    private void switchForgeToTemple(ChoiceNothingForgeExploit ch){
        if(this.haveTryExploit){ // si on vient d'exploit alors on fais aucun choix sinon essaye d'acheter une carte
            ch.choice = EnumTypeChoice.NOTHING;
            Printer.getInstance().logClient("Le joueur a choisi de faire : " + Printer.BLUE + EnumTypeChoice.NOTHING, this.getUUID());
        } else if(!this.wantBuyOtherFace) {
            this.haveTryForge = true;
            this.choiceTemple(ch);
        }
    }

    public void choiceOneMoreTime(ChoiceOneMoreTurn ch) {
        int result = this.handleRandom.getRandomBetweenMinMax(0,1);
        if(ch.valueSolaryPlayer > 2 && result == 0) {
            ch.choice = true;
            Printer.getInstance().logClient("Le joueur a choisi de faire une autre action.", this.getUUID());
        }
        else {
            Printer.getInstance().logClient("Le joueur  n'a pas choisi de faire une autre action.", this.getUUID());
            ch.choice = false;
        }

    }

    public void choiceHammer(ChoiceHammer ch) {
        // TOUJOURS FOCUS LE MARTEAU
            ch.setValueToHammer(ch.getValueGold());
            Printer.getInstance().logClient("Le joueur veut ajouter "+ch.getValueToHammer()+" GOLD au hammer",this.getUUID());
    }

    public void choiceAncient(Choice3GoldFor4Glory ch) {
        Printer.getInstance().logClient("Le joueur accepte 3 gold pour 4 glory ",this.getUUID());
        ch.choice = true;

    }

    public void choiceSatyre(ChoiceSatyre ch, int choiceStep) {

        int p1 = this.handleRandom.getRandomBetweenMinMax(0,ch.rollPlayers.size()-1);
        int p2 = this.handleRandom.getRandomBetweenMinMax(0,ch.rollPlayers.size()-1);
        UUID firstPlayer = (UUID) ch.rollPlayers.keySet().toArray()[p1];
        UUID deuxiemPlayer = (UUID) ch.rollPlayers.keySet().toArray()[p2];
        int rand = this.handleRandom.getRandomBetweenMinMax(0,1);
        if(rand == 0){
            ch.faceChoiceOne = ch.rollPlayers.get(firstPlayer).faceOne;
            ch.faceChoiceTwo = ch.rollPlayers.get(deuxiemPlayer).faceTwo;
        } else {
            ch.faceChoiceOne = ch.rollPlayers.get(deuxiemPlayer).faceTwo;
            ch.faceChoiceTwo = ch.rollPlayers.get(firstPlayer).faceOne;
        }
        Printer.getInstance().logClient("Le joueur a choisi pour le satyre "+ch.faceChoiceOne+" "+ch.faceChoiceTwo,this.getUUID());
    }

    public void choiceForgeSpecial(ChoiceForgeFaceSpecial ch) {
        ch.indexDice = this.handleRandom.getRandomBetweenMinMax(0,1);
        ch.indexDiceFace = this.handleRandom.getRandomBetweenMinMax(0,5);
        Printer.getInstance().logClient("Le joueur veut remplacer le dice "+ch.indexDice+" face "+ch.indexDiceFace+" pour forger "+ch.faceSpecial,this.getUUID());

    }

    public void choicePowerOtherPlayer(ChoicePowerOnDiceOtherPlayer ch) {
        int p1 = this.handleRandom.getRandomBetweenMinMax(0,ch.rollPlayers.size()-1);
        UUID idUse = (UUID) ch.rollPlayers.keySet().toArray()[p1];
        int rand = this.handleRandom.getRandomBetweenMinMax(0,1);
        if(rand == 0) ch.faceChoice = ch.rollPlayers.get(idUse).faceOne;
        else ch.faceChoice = ch.rollPlayers.get(idUse).faceTwo;
        Printer.getInstance().logClient("Le joueur veut utiliser la face "+ch.faceChoice+" du player "+idUse,this.getUUID());
    }


    public void getIslandSortByCost(List<Island> ilelist) {

        Collections.sort(ilelist,new SortByIslandCost());

    }
}


class SortByIslandCost implements Comparator<Island> {

    @Override
    public int compare(Island i1, Island i2) {
        int totI1 = 0;
        int totI2 = 0;
        for(TypeRessource type : i1.listCosts.keySet()) totI1+=i1.listCosts.get(type);
        for(TypeRessource type : i2.listCosts.keySet()) totI2+=i2.listCosts.get(type);
        return totI1 - totI2;
    }

}