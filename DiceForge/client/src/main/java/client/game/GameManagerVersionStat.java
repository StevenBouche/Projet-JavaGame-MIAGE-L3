package client.game;

import share.cards.Card;
import share.cards.Cards;
import share.cards.model.Hammer;
import share.choice.*;
import share.dice.Dice;
import share.exeption.CaseOfPoolForgeOutOfBound;
import share.face.Face;
import share.face.FaceHybrid;
import share.face.TypeFace;
import share.forge.PoolEnum;
import share.ressource.TypeRessource;
import share.temple.Island;
import share.temple.IslandEnum;
import share.utils.Printer;

import java.lang.reflect.Array;
import java.util.*;

public class GameManagerVersionStat extends GameManager {

    private boolean haveTryForge;
    private boolean haveTryExploit;
    private boolean wantBuyOtherFace;
    private ArrayList<String> facesOp = new ArrayList<>();
    private ArrayList<String> powerChoiceOp = new ArrayList<>();

    public GameManagerVersionStat() {
        String temp[] = {
            "1 of GOLD/1 of LUNAR",
            "GLORY1_SOLAR1/2 of LUNAR",
            "6 of GOLD/GLORY1_SOLAR1",
            "GOLD2_OR_SOLAR2_OR_LUNAR2/3 of GOLD",
            "2 of GLORY/GLORY2_LUNAR2",
            "GOLD3_OR_GLORY2/GLORY1_SOLAR1",
            "GOLD3_OR_GLORY2/GOLD2_OR_SOLAR2_OR_LUNAR2",
            "GOLD3_OR_GLORY2/4 of GOLD",
            "2 of GLORY/GOLD1_GLORY1_SOLAR1_LUNAR1",
            "FaceSpecial-FACE_MULTIPLE_3/GOLD2_LUNAR1",
            "FaceSpecial-FACE_MULTIPLE_3/GLORY1_SOLAR1",
        };
        facesOp.addAll(Arrays.asList(temp));

        temp = new String[] {
            "4 of GLORY",
            "4 of GOLD",
            "FaceSpecial-FACE_MULTIPLE_3",
            "GOLD2_OR_SOLAR2_OR_LUNAR2",
            "1 of GOLD ",
            "1 of LUNAR ",
            "1 of SOLAR",
            "GOLD3_OR_GLORY2",
            "3 of GLORY",
        };

        powerChoiceOp.addAll(Arrays.asList(temp));

    }

    @Override
    public TypeRessource choiceRessourceFaceHybrid(EnumMap<TypeRessource, Integer> fh) {

        TypeRessource ressource = null;
        int tempAmmount = 0;
        for (TypeRessource e : fh.keySet()){
            if(e == TypeRessource.GLORY ) {
                if(tempAmmount < fh.get(e)) {
                    tempAmmount = fh.get(e);
                    ressource = e;
                }
            }
        }

        if(ressource == null) {
            for (TypeRessource e : fh.keySet()){
                if(e == TypeRessource.SOLAR ) {
                    if(tempAmmount < fh.get(e)) {
                        tempAmmount = fh.get(e);
                        ressource = e;
                    }
                }
            }
        }

        if(ressource == null) {
            for (TypeRessource e : fh.keySet()){
                if(e == TypeRessource.GOLD ) {
                    if(tempAmmount < fh.get(e)) {
                        tempAmmount = fh.get(e);
                        ressource = e;
                    }
                }
            }
        }

        return ressource;
    }

    @Override
    public void choiceForgeOrExploiOrNothing(ChoiceNothingForgeExploit ch) {
        this.haveTryForge = false;
        this.haveTryExploit = false;
        this.wantBuyOtherFace = false;

        int v = this.handleRandom.getRandomBetweenMinMax(0,1);

        if(v == 0) this.choiceFaceInForge(ch);
        else this.choiceTemple(ch);
    }

    private boolean containsCard(String name, List<Card> listcard) {
        for(Card c : listcard) {
            if(c.name.equals(name)){
                return true;
            }
        }
        return false;
    }
    private boolean containsId(int id, EnumMap<IslandEnum, Island> map) {
        for(IslandEnum i : map.keySet()) {
            if(i.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private void choiceTemple(ChoiceNothingForgeExploit ch) {

        //SI LE TEMPLE NA PAS D ILE ALORS NE FAIS RIEN DONC NOTHING  // 1 CAR ON NE TIRE PAS LES CARTES DE L ILE 7 POUR L INSTANT
        if(ch.listIslandTemple.isEmpty()){
            if(this.haveTryForge){ // si on vient d'essayer de forge alors on fais rien sinon on essaye de forger une face vu que l'on peux pas acheter de carte
                ch.choice = EnumTypeChoice.NOTHING;
                Printer.getInstance().logClient("Le joueur a choisi de faire car plus d'ile : " + Printer.BLUE + EnumTypeChoice.NOTHING, id);
            } else {
                this.haveTryExploit = true;
                this.choiceFaceInForge(ch);
            }
            return;
        }

        int topIds[] = {1, 4, 2, 3, 7, 6};
        IslandEnum island = null;

        for(Integer i : topIds) {
            if(containsId(i, ch.listIslandTemple)){
                for(IslandEnum is : ch.listIslandTemple.keySet()) {
                    if(is.getId() == i) {
                        island = is;
                        break;
                    }
                }
            }
            if(island != null) break;
        }


        if(island == null) {
            for(IslandEnum i : ch.listIslandTemple.keySet()) {
                island = i;
            }
        }


        if(island == null ) this.choiceTemple(ch);
        else if(ch.listIslandTemple.get(island) == null) this.choiceTemple(ch);
        else if(ch.listIslandTemple.get(island).listcard.isEmpty()){ // SI L ILE EXIST MAIS QUE CA LIST EST VIDE ON SUPPRIME L ILE DE LA LIST ET ON RETENTE
            ch.listIslandTemple.remove(island);
            this.choiceTemple(ch);
        } else { //SINON CHERCHE UNE CASE SUR L ILE RANDOM
            Card card = null;

            String topCards[] = {
                    "The Blacksmith's Hammer",
                    "Chest",
                    "The Elder",
                    "Wild Spirits",
                    "The Silver Hind",
                    "Ferryman",
                    "Hydra",
                    "Mirror of the Abyss",
                    "Minotaur"};

            for(String s : topCards) {
                if (containsCard(s, ch.listIslandTemple.get(island).listcard)) {
                    for (Card c : ch.listIslandTemple.get(island).listcard) {
                        if (c.name.equals(s)) {
                            card = c;
                            break;
                        }
                    }
                }
                if(card != null) break;
            }

            int choicePos = 0;
            for(int i = 0; i < ch.listIslandTemple.get(island).listcard.size(); i++) {
               if(ch.listIslandTemple.get(island).listcard.get(i) != null) {
                   card = ch.listIslandTemple.get(island).listcard.get(i);
                   choicePos = i;
                   break;
               }
            }

            if(this.cantBuyCard(card,ch)) { // le joueur ne peux pas acheter la carte
                ch.listIslandTemple.remove(island);
                this.choiceTemple(ch);
            } else {
                ch.choice = EnumTypeChoice.EXPLOIT;
                ch.choiceIsland = island;
                ch.choiceIndexIslandCase = choicePos;
                Printer.getInstance().logClient("Le joueur veut acheter la carte : " + Printer.BLUE + card.name, id);
            }
        }
    }

    public boolean cantBuyCard(Card c, ChoiceNothingForgeExploit ch){
        boolean b = false;
        for(TypeRessource type : c.cost.keySet()) if(c.cost.get(type) > ch.ressourceOfPlayer.get(type).value) b= true;
        // regarde si je peux acheter la carte que j'ai tirer aléatoirement
        return b;
    }

    private void switchForgeToTemple(ChoiceNothingForgeExploit ch){
        if(this.haveTryExploit){ // si on vient d'exploit alors on fais aucun choix sinon essaye d'acheter une carte
            ch.choice = EnumTypeChoice.NOTHING;
            Printer.getInstance().logClient("Le joueur a choisi de faire : " + Printer.BLUE + EnumTypeChoice.NOTHING, id);
        } else if(!this.wantBuyOtherFace) {
            this.haveTryForge = true;
            this.choiceTemple(ch);
        }
    }

    @Override
    public void choiceFaceInForge(ChoiceNothingForgeExploit ch)  {

        ch.choice = EnumTypeChoice.FORGE;

        List<Integer> costsPool = PoolEnum.getCostsPool();
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

        if(ch.ressourceOfPlayer.get(TypeRessource.GOLD).value < min) {
            this.switchForgeToTemple(ch);
            return;
        }

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

    }

    @Override
    public void choiceOneMoreTime(ChoiceOneMoreTurn ch) {
        if(ch.valueSolaryPlayer > 2) {
            ch.choice = true;
            Printer.getInstance().logClient("Le joueur a choisi de faire une autre action.", id);
        }
        else {
            Printer.getInstance().logClient("Le joueur  n'a pas choisi de faire une autre action.", id);
            ch.choice = false;
        }
    }

    @Override
    public void choiceHammer(ChoiceHammer ch) {

        Hammer h = ch.getHammersPlayer();
        int resteHammer = h.getMaxGold() - h.getCurrentGold();

        if(ch.getValueGold() <= 3) {
            ch.setValueToHammer(0);
        }else {
            if (resteHammer >= ch.getValueGold()) {
                ch.setValueToHammer(ch.getValueGold());
            } else {
                int max = ch.getValueGold() - resteHammer;
                ch.setValueToHammer(max);
            }
        }
        Printer.getInstance().logClient("Le joueur veut ajouter "+ch.getValueToHammer()+" GOLD au hammer",id);
    }

    @Override
    public void choiceAncient(Choice3GoldFor4Glory ch) {
        int v = this.handleRandom.getRandomBetweenMinMax(0,100);
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
        Face f = null;

        if(facesOp.size() > 0) {
            for (UUID idPlayer : ch.rollPlayers.keySet()) {
                String toRemove = "";
                for (String couple : facesOp) {
                    if (ch.rollPlayers.get(idPlayer).toString().equals(couple)) {
                        toRemove = couple;
                        int rand = this.handleRandom.getRandomBetweenMinMax(0, 1);
                        if(rand == 0) {
                            f = ch.rollPlayers.get(idPlayer).faceOne;
                        }else {
                            f = ch.rollPlayers.get(idPlayer).faceTwo;
                        }
                        break;
                    }
                }
                facesOp.remove(toRemove);
            }
        }

        if(f == null) {
            int roll = this.handleRandom.getRandomBetweenMinMax(1,nbPlayer*2);
            int loop = 1;
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
        UUID idUse = null;
        Face f = null;

        String toRemove = "";
        for(UUID id : ch.rollPlayers.keySet()){
            for(String s : powerChoiceOp) {
                if(ch.rollPlayers.get(id).toString().equals(s)) {
                    toRemove = s;
                    int rand = this.handleRandom.getRandomBetweenMinMax(0, 1);
                    if(rand == 0) {
                        f = ch.rollPlayers.get(id).faceOne;
                    }else {
                        f = ch.rollPlayers.get(id).faceTwo;
                    }
                    idUse=id;
                    break;
                }
                powerChoiceOp.remove(toRemove);
            }
        }

        if(f == null) {
            int roll = this.handleRandom.getRandomBetweenMinMax(1,nbPlayer*2);
            int loop = 1;
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
        }

        ch.faceChoice = f;
        Printer.getInstance().logClient("Le joueur veut utiliser la face "+ch.faceChoice+" du player "+idUse,id);
    }

}

