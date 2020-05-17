package share.cards;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class CardTest {

     static FactoryCard facto;
     static   List<Card> listFacto;


    @BeforeEach
    void initTest(){

        facto = new FactoryCard();
       listFacto= FactoryCard.createXInstanceOfCard(Cards.CHEST,1);
    }

    @Test
    public void getCost(){
        Assertions.assertEquals(Cards.CHEST.cost, listFacto.get(0).getCost());
    }

    @Test
    public void getGlory(){

        Assertions.assertEquals(2, listFacto.get(0).getGlory());
    }

}