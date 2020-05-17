package share.inventory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import share.cards.Card;
import share.cards.Cards;
import share.cards.FactoryCard;
import share.cards.effects.TypeEffectBasique;
import share.cards.model.Hammer;
import share.dice.Dice;
import share.dice.FactoryDice;
import share.face.*;
import share.ressource.TypeRessource;
import share.utils.HandleRandom;

import java.util.*;

import static org.mockito.Matchers.any;

class InventoryTest {

    static Inventory i;
    static EnumMap <TypeRessource, InventoryElement> e;
    static InventoryElement ie;
    static TypeRessource gold;
    static TypeRessource solar;
    static TypeRessource lunar;
    static Face f1;
    static Face f2;
    static Map<Integer, FaceSimpleEnum> f;
    static Dice d;
    static ArrayList hammers;
    static Hammer h;
    static ArrayList<Dice> listDice;
   static  Map<TypeEffectBasique,List<Card>> listsCards;
   static TypeEffectBasique type;
    static   List<Card> listFacto;



    @BeforeEach
    void iniTest(){
        FactoryFace.resetInstance();
        i= new Inventory();
        gold = TypeRessource.GOLD;
        lunar = TypeRessource.LUNAR;
        solar = TypeRessource.SOLAR;
        f1 = new FaceSimple(TypeRessource.GOLD,3);
        f2 = new FaceSimple(TypeRessource.GOLD,3);
        f = new HashMap<>();
        hammers = new ArrayList<>();
        h = new Hammer();
        listDice = FactoryDice.createDicesPlayer();
        listsCards = new EnumMap<TypeEffectBasique,List<Card>>(TypeEffectBasique.class);
        type = TypeEffectBasique.NONE;
        listFacto= FactoryCard.createXInstanceOfCard(Cards.CHEST,1);
        FactoryFace.resetInstance();
    }

    @Test
    void getRessources() {
        i.addRessource(gold,3);

        Assertions.assertEquals(3,i.getRessources().get(gold).value);

}


    @Test
    void getValueRessource() {
        i.addRessource(gold,15);
        Assertions.assertEquals(i.getRessources().get(gold).valueMax,i.getValueRessource(TypeRessource.GOLD));
        i.removeRessource(gold,20);
        Assertions.assertEquals(0,i.getValueRessource(TypeRessource.GOLD));

    }

    @Test
    void getValueMaxRessource() {
    i.addRessource(gold,15);
        Assertions.assertEquals(12,i.getRessources().get(gold).valueMax);
        i.extendRessource(gold,3);
        Assertions.assertEquals(15,i.getRessources().get(gold).valueMax);


    }

    @Test
    void addRessource() {
        i.addRessource(solar,15);
        Assertions.assertEquals(i.getRessources().get(solar).valueMax,i.getRessources().get(solar).value);


    }

    @Test
    void removeRessource() {
        i.addRessource(solar,15);
        i.removeRessource(solar, 2);
        Assertions.assertEquals(i.getRessources().get(solar).valueMax - 2,i.getRessources().get(solar).value);

    }

    @Test
    void extendRessource() {
        i.extendRessource(solar,3);
        i.addRessource(solar,13);
        Assertions.assertEquals(i.getRessources().get(solar).valueMax ,i.getRessources().get(solar).value);


    }

    @Test
    void reduceRessource() {
        i.reduceRessource(lunar,3);
        i.addRessource(lunar,6);
        Assertions.assertEquals(i.getRessources().get(lunar).value,i.getRessources().get(lunar).valueMax);


    }

    @Test
    void rollDiceMajorList() {

        //Assertions.assertFalse();


    }

    @Test
    void rollDiceMajorMap() {
        i.getDice(0).handleRandom = Mockito.spy(i.getDice(0).handleRandom);
        i.getDice(1).handleRandom = Mockito.spy(i.getDice(1).handleRandom);
        Mockito.doReturn(0).when(i.getDice(0).handleRandom).getRandomBetweenMinMax(any(Integer.class),any(Integer.class));
        Mockito.doReturn(0).when(i.getDice(1).handleRandom).getRandomBetweenMinMax(any(Integer.class),any(Integer.class));
        Map<Integer,Face> result = i.rollDiceMajorMap();
        Assertions.assertEquals(i.getDice(0).getFace(0),result.get(0));
        Assertions.assertEquals(i.getDice(1).getFace(0),result.get(1));
    }

    @Test
    void getLastRoll() {
    }

    @Test
    void rollDiceMinor() {
    }

    @Test
    void getDice() {

        Assertions.assertEquals(listDice.get(0).toString(),i.getDice(0).toString());

    }

    @Test
    void addHistoryFace() {
        i.addHistoryFace(f1);
        Assertions.assertEquals(f1,i.getHistoryFace().get(0));
}

    @Test
    void getHammer() {
        Assertions.assertEquals(hammers,i.getHammer());
        hammers.add(h);
        i.addHammer(h);
        Assertions.assertEquals(hammers.get(0),i.getHammer(0));

    }

    @Test
    void addHammer() {
        hammers.add(h);
        i.addHammer(h);
        Assertions.assertEquals(hammers.get(0),i.getHammer(0));
    }

    @Test
    void testGetHammer() {
        Assertions.assertEquals(hammers,i.getHammer());
        hammers.add(h);
        i.addHammer(h);
        Assertions.assertEquals(hammers.get(0),i.getHammer(0));

    }

    @Test
    void getListsCards() {
        Assertions.assertEquals(listsCards.toString(),i.getListsCards().toString());

    }

    @Test
    void getListCards() {
        //Assertions.assertEquals(listsCards.get(type).toString(),i.getListCards(type).toString()); //todo
    }

    @Test
    void setListsCards() {
        i.setListsCards(listsCards);
        Assertions.assertEquals(listsCards,i.getListsCards());

    }

    @Test
    void addCard() {
        Card c = listFacto.get(0); //CHEST
        i.addCard(TypeEffectBasique.IMMEDIATE, c);

        listsCards.put(TypeEffectBasique.IMMEDIATE,listFacto);
        Assertions.assertEquals(listsCards,i.getListsCards());
    }

    @Test
    void removeHammer() {
        i.addHammer(h);


        Assertions.assertEquals(1,i.getSizeHammers());
        i.removeHammer(0);
        Assertions.assertEquals(0,i.getSizeHammers());

    }

    @Test
    void getCurrentHammer() {
        i.addHammer(h);
        Assertions.assertEquals(0,i.getCurrentHammer().getNbStep());
    }

    @Test
    void haveHammer() {
        Assertions.assertFalse(i.haveHammer());
    }

    @Test
    void getSizeHammers() {

        hammers.add(h);
        i.addHammer(h);
        Assertions.assertEquals(hammers.size(),i.getSizeHammers());
    }

    @Test
    void setCurrentHammer() {
        i.addHammer(h);
        i.setCurrentHammer(0);

        Assertions.assertEquals(0,i.getCurrentHammer().getCurrentGold());

    }

    @Test
    void getCardsByTypeEffect() {

        Assertions.assertEquals(i.getListsCards().get(TypeEffectBasique.NONE),i.getCardsByTypeEffect(TypeEffectBasique.NONE));
    }


}