package share.dice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import share.face.FactoryFace;

import java.util.ArrayList;

class FactoryDiceTest {
    static ArrayList<Dice> list;
     static FactoryDice factory;


    @BeforeEach
    void initTest(){
        FactoryFace.resetInstance();
        factory = new FactoryDice();
        list =  FactoryDice.createDicesPlayer();
    }

    @Test
    void createDicesPlayer() {
        Assertions.assertEquals("1 of GOLD",list.get(0).getFace(0).toString());
       Assertions.assertEquals("1 of GOLD",list.get(0).getFace(1).toString());
        Assertions.assertEquals("1 of GOLD",list.get(0).getFace(2).toString());
        Assertions.assertEquals("1 of GOLD",list.get(0).getFace(3).toString());
       Assertions.assertEquals("1 of SOLAR",list.get(0).getFace(4).toString());
        Assertions.assertEquals("1 of GOLD",list.get(0).getFace(5).toString());



        Assertions.assertEquals("1 of LUNAR",list.get(1).getFace(0).toString());
        Assertions.assertEquals("1 of GOLD",list.get(1).getFace(1).toString());
        Assertions.assertEquals("1 of GOLD",list.get(1).getFace(2).toString());
        Assertions.assertEquals("1 of GOLD",list.get(1).getFace(3).toString());
        Assertions.assertEquals("1 of GOLD",list.get(1).getFace(4).toString());
        Assertions.assertEquals("2 of GLORY",list.get(1).getFace(5).toString());

    }
}