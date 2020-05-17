package share.temple;

import share.cards.Card;
import share.cards.Cards;
import share.cards.FactoryCard;
import share.config.ConfigGame;
import share.exeption.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Factory island.
 */
public class FactoryIsland {

    /**
     * Create island island.
     *
     * @param island the island
     * @param str    the str
     * @return the island
     */
    public static Island createIsland(IslandEnum island, Extension str){
        Island i = null;
        i = new Island(island,island.list);
        i.listcard = generatePoolCard(island,str);
        return i;
    }

    private static List<Card> generatePoolCard(IslandEnum island, Extension str){
        if(str.equals(Extension.EXTENSION)) return generatePoolCardExtension(island);
        else return generatePoolCardBasic(island);
    }


    private static List<Card> generatePoolCardExtension(IslandEnum id){
        try {
            throw new NotImplementedException("");
        } catch (NotImplementedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Card> generatePoolCardBasic(IslandEnum island){
        List<Card> listcards = new ArrayList<>();
        for(Cards c : Cards.values()){
            if(c.island == island){
                listcards.addAll(FactoryCard.createXInstanceOfCard(c, ConfigGame.NB_PLAYER_MAX));
            }
        }
        return listcards;
    }

}
