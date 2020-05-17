package share.inventory;

import share.cards.Card;
import share.cards.effects.TypeEffectBasique;
import share.cards.model.Hammer;
import share.dice.Dice;
import share.dice.FactoryDice;
import share.face.Face;
import share.ressource.TypeRessource;
import share.utils.HandleRandom;

import java.util.*;

/**
 * The type Inventory.
 */
public class Inventory implements IInventoryInfos {

    private EnumMap<TypeRessource, InventoryElement> mapRessources;

    /**
     * Get ressources enum map.
     *
     * @return the enum map
     */
    public EnumMap<TypeRessource, InventoryElement> getRessources(){
        return this.mapRessources;
    }

    private ArrayList<Dice> listDice;
    private ArrayList<Face> historyFace;

    private int currentHammer;
    private List<Hammer> hammers;
    private Map<TypeEffectBasique,List<Card>> listsCards;

    /**
     * Instantiates a new Inventory.
     */
    public Inventory(){
        this.listsCards = new EnumMap<TypeEffectBasique,List<Card>>(TypeEffectBasique.class);
        this.historyFace = new ArrayList<>();
        this.hammers = new ArrayList<>();
        this.currentHammer = 0;
        List<Card> cardUsed = new ArrayList<>();
        this.mapRessources = new EnumMap<>(TypeRessource.class);
        this.listDice = FactoryDice.createDicesPlayer();
        for(TypeRessource type : TypeRessource.getListType()) this.mapRessources.put(type, new InventoryElement(type.getValueMaxDefault()));
    }

    public Hammer getCurrentHammer(){
        if(this.hammers.isEmpty()) return null;
        return this.hammers.get(currentHammer);
    }

    @Override
    public int getValueRessource(TypeRessource type){
         return this.mapRessources.get(type).value;
    }

    @Override
    public int getValueMaxRessource(TypeRessource type){
        return this.mapRessources.get(type).valueMax;
    }

    /**
     * Add share.ressource.
     *
     * @param type  the type
     * @param value the value
     */
    public void addRessource(TypeRessource type, int value){
        int valueTot = this.mapRessources.get(type).value + value;
        int valueMax = this.mapRessources.get(type).valueMax;
        if(valueTot > valueMax) this.mapRessources.get(type).value = valueMax;
        else this.mapRessources.get(type).value += value;
    }

    /**
     * Remove share.ressource.
     *
     * @param type  the type
     * @param value the value
     */
    public void removeRessource(TypeRessource type, int value){
        int valueTot = this.mapRessources.get(type).value - value;
        if(valueTot < 0) this.mapRessources.get(type).value = 0;
        else this.mapRessources.get(type).value -= value;
    }

    /**
     * Extend share.ressource.
     *
     * @param type  the type
     * @param value the value
     */

    public void extendRessource(TypeRessource type, int value){
        this.mapRessources.get(type).valueMax += value;
    }

    /**
     * Reduce share.ressource.
     *
     * @param type  the type
     * @param value the value
     */
    public void reduceRessource(TypeRessource type, int value) {
        int valueTot = this.mapRessources.get(type).valueMax - value;
        if (valueTot < 0) {
            this.mapRessources.get(type).valueMax = 0;
        } else {
            this.mapRessources.get(type).valueMax -= value;
        }
        if (this.mapRessources.get(type).valueMax < this.mapRessources.get(type).value){
            this.mapRessources.get(type).value = this.mapRessources.get(type).valueMax;
        }
    }

    /**
     * Roll share.dice major.
     *
     * @return the array list
     */
    public ArrayList<Face> rollDiceMajorList(){
        Face f1 = this.listDice.get(0).roll();
        Face f2 = this.listDice.get(1).roll();
        ArrayList<Face> f = new ArrayList<>();
        f.add(f1);
        f.add(f2);
        return f;
    }

    /**
     * Roll dice major map map.
     *
     * @return the map
     */
    public Map<Integer , Face> rollDiceMajorMap(){
        Face f1 = this.listDice.get(0).roll();
        Face f2 = this.listDice.get(1).roll();
        Map<Integer, Face> f = new HashMap<>();
        f.put(0,f1);
        f.put(1,f2);
        return f;
    }

    /**
     * Roll dice minor face.
     *
     * @return the face
     */
    public Face rollDiceMinor(){
        HandleRandom h = new HandleRandom();
        int r = h.getRandomBetweenMinMax(0,1);
        return this.listDice.get(r).roll();
    }

    /**
     * Get last roll map.
     *
     * @return the map
     */
    public Map<Integer , Face> getLastRoll(){
        Face f1 = this.listDice.get(0).getFace(this.listDice.get(0).indexLastRoll);
        Face f2 = this.listDice.get(1).getFace(this.listDice.get(1).indexLastRoll);
        Map<Integer, Face> f = new HashMap<>();
        f.put(0,f1);
        f.put(1,f2);
        return f;
    }


    /**
     * Get share.dice share.dice.
     *
     * @param num the num
     * @return the share.dice
     */
    public Dice getDice(int num){
        return this.listDice.get(num);
    }

    /**
     * Add history share.face.
     *
     * @param f the f
     */

    public void addHistoryFace(Face f){
        this.historyFace.add(f);
    }

    /**
     * Gets hammer.
     *
     * @return the hammer
     */
    public List<Hammer> getHammer() {
        return this.hammers;
    }

    /**
     * Add hammer.
     *
     * @param hammer the hammer
     */
    public void addHammer(Hammer hammer) {
        this.hammers.add(hammer);
        if(this.getHammer(this.currentHammer) != null) this.currentHammer = 0;
        else if(this.getHammer(this.currentHammer).isFinish()) this.currentHammer++;
    }

    /**
     * Get hammer hammer.
     *
     * @param index the index
     * @return the hammer
     */
    public Hammer getHammer(int index){
        if(index > this.hammers.size()-1) return null;
        return this.hammers.get(index);
    }

    @Override
    public String toString(){
        String s = "\n\t\t";
        for(TypeRessource element : this.mapRessources.keySet()) s += element + " : " + this.mapRessources.get(element).toString() + "\t" ;
        s += "\n\t\t";
        for(Dice d : this.listDice){
            s+= "DICE : "+d.toString()+"\n\t\t";
        }
        s+="CARD : ";

        for(TypeEffectBasique type : this.listsCards.keySet()){
            s+= "\n\t\t\t"+type.toString()+" : ";
            for(int i = 0; i < this.listsCards.get(type).size();i++) s+= this.listsCards.get(type).get(i).name+" | ";
        }

        if(!this.hammers.isEmpty()) s+="\n\t\tHAMMERS : "+this.hammers.toString()+" | ";

        return s;
    }

    /**
     * Gets lists cards.
     *
     * @return the lists cards
     */
    public Map<TypeEffectBasique, List<Card>> getListsCards() {
        return listsCards;
    }

    /**
     * Get list cards list.
     *
     * @param type the type
     * @return the list
     */
    public List<Card> getListCards(TypeEffectBasique type){
        return this.listsCards.get(type);
    }

    /**
     * Sets lists cards.
     *
     * @param listsCards the lists cards
     */
    public void setListsCards(Map<TypeEffectBasique, List<Card>> listsCards) {
        this.listsCards = listsCards;
    }

    /**
     * Add card.
     *
     * @param type the type
     * @param c    the c
     */
    public void addCard(TypeEffectBasique type, Card c){
        this.listsCards.computeIfAbsent(type, k -> new ArrayList<>());
        this.listsCards.get(type).add(c);
    }

    /**
     * Remove hammer hammer.
     *
     * @param i the
     * @return the hammer
     */
    public Hammer removeHammer(int i) {
        return this.hammers.remove(i);
    }

    /**
     * Gets current hammer.
     *
     * @return the current id hammer
     */
    public int getCurrentIdHammer() {
        return currentHammer;
    }

    /**
     * Have hammer boolean.
     *
     * @return the boolean
     */
    public boolean haveHammer(){
        return this.hammers.size() >= currentHammer+1 ;
    }

    /**
     * Get size hammers int.
     *
     * @return the int
     */
    public int getSizeHammers(){
        return this.hammers.size();
    }

    /**
     * Sets current hammer.
     *
     * @param currentHammer the current hammer
     */
    public void setCurrentHammer(int currentHammer) {
        this.currentHammer = currentHammer;
    }

    /**
     * Get cards by type effect list.
     *
     * @param type the type
     * @return the list
     */
    public List<Card> getCardsByTypeEffect(TypeEffectBasique type){
        return this.listsCards.get(type);
    }

    /**
     * Get history face list.
     *
     * @return the list
     */
    public List<Face> getHistoryFace(){
        return this.historyFace;
    }
}