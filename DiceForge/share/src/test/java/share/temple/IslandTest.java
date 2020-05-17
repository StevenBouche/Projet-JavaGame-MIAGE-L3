package share.temple;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IslandTest {

   static FactoryIsland fci;

   static List<Island> listIle;

    @BeforeEach
    void initTest()  {
        fci = new FactoryIsland();
        listIle = new ArrayList<>();
        for (IslandEnum e : IslandEnum.values()){
            listIle.add(FactoryIsland.createIsland(e,Extension.STANDARD));
        }
    }

    @Test
    void getCard()  {
        Island i = listIle.get(0);
        try {
            i.getCard(i.listcard.size());
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index "+i.listcard.size()+" out of bounds for length "+i.listcard.size(), e.getMessage());
        }

            Assertions.assertEquals(i.listcard.get(0).toString(),i.getCard(1).toString());
            Assertions.assertEquals(7,i.listcard.size());


    }

    @Test
    void getId() {
        for(Island e : listIle){
            Assertions.assertEquals(e.id.getId(),e.IdIslandEnum());
        }


    }

    @Test
    void getPlayer() {
        Island i = listIle.get(0);
        Assertions.assertEquals(null,i.getPlayer());

        i.player = UUID.randomUUID();
        Assertions.assertEquals(i.player,i.getPlayer());
    }
}