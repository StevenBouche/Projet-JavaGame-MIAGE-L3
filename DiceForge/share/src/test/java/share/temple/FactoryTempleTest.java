package share.temple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import share.exeption.InstanceCardOutOfBoundException;

import java.util.HashMap;

class FactoryTempleTest {
    static HashMap<Integer,Island> map;
    static FactoryTemple facto;

    @BeforeEach
    void initTest() throws InstanceCardOutOfBoundException {
        facto = new FactoryTemple();
        map = facto.createIsland(Extension.STANDARD);
    }

    @Test
    void createIsland() throws InstanceCardOutOfBoundException {
        for (IslandEnum i : IslandEnum.values()) {
            Assertions.assertEquals(i, facto.createIsland(Extension.STANDARD).get(i.getId()).id);
        }
    }

    @Test
    void createIslandWithEnum() throws InstanceCardOutOfBoundException{
        for (IslandEnum i : IslandEnum.values()) {
            Assertions.assertEquals(i, facto.createIslandWithEnum(Extension.STANDARD).get(i).id);
        }
    }
}