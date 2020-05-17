package share.cards;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CardsTest {

    @Test
    void getValueOfEnumByName() {
        for(Cards c : Cards.values()){
            Cards c2 = Cards.getValueOfEnumByName(c.name);
            Assertions.assertEquals(c,c2);
        }
    }
}