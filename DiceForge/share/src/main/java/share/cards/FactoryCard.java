package share.cards;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Factory card.
 */
public class FactoryCard {

    /**
     * Create x instance of card list.
     *
     * @param c  the c
     * @param nb the nb
     * @return the list
     */
    public static List<Card> createXInstanceOfCard(Cards c, int nb){
        List<Card> list = new ArrayList<>();
        for(int i = 0; i < nb; i++){
            list.add(new Card(c,c.island,c.cost,c.glory,c.typeEffect,c.effect,c.name));
        }
        return list;
    }

}


