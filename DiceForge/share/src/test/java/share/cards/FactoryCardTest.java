package share.cards;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class FactoryCardTest {

   static List<String> listCar ;
   static  FactoryCard facto;


    @BeforeEach
    void initTest(){
         listCar = new ArrayList<>();
         facto = new FactoryCard();
         listCar.add(Cards.CHEST.name);
    }


    @Test
   public void createXInstanceOfCard(){
        Assertions.assertEquals(FactoryCard.createXInstanceOfCard(Cards.CHEST, 1).get(0).name,listCar.get(0));

        Assertions.assertEquals(FactoryCard.createXInstanceOfCard(Cards.CHEST,1).size(),listCar.size());

    }
}