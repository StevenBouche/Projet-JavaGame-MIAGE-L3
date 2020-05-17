package share.temple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import share.exeption.InstanceCardOutOfBoundException;
import share.ressource.TypeRessource;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.*;

class FactoryIslandTest {

    static FactoryIsland f;
    static Island i;




    @BeforeEach
    void initTest()  {
        f = new FactoryIsland();
        i = f.createIsland(IslandEnum.ISLAND1,Extension.STANDARD);


    }

    @Test
    void createIsland() { //TODO A REFAIRE
        Assertions.assertEquals(i.toString(),f.createIsland(IslandEnum.ISLAND1,Extension.STANDARD).toString());
    }
}