package share.choice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import share.cards.model.Hammer;

class ChoiceHammerTest {

    static Hammer playerHammer;
    static ChoiceHammer myHammer;


    @BeforeEach
    void initTest(){
        myHammer = new ChoiceHammer();
        playerHammer = new Hammer();
    }

    @Test
    void getHammersPlayer() {
        myHammer.setHammersPlayer(playerHammer);
        Assertions.assertEquals(playerHammer,myHammer.getHammersPlayer());
    }

    @Test
    void setHammersPlayer() {
        myHammer.setHammersPlayer(playerHammer);
        Assertions.assertEquals(playerHammer,myHammer.getHammersPlayer());
    }

    @Test
    void getValueGold() {
        myHammer.setValueGold(5);
        Assertions.assertEquals(5,myHammer.getValueGold());
    }

    @Test
    void setValueGold() {
        myHammer.setValueGold(5);
        Assertions.assertEquals(5,myHammer.getValueGold());
    }

    @Test
    void getValueToHammer() {
        myHammer.setValueToHammer(8);
        Assertions.assertEquals(8,myHammer.getValueToHammer());
    }

    @Test
    void setValueToHammer() {
        myHammer.setValueToHammer(8);
        Assertions.assertEquals(8,myHammer.getValueToHammer());
    }
}