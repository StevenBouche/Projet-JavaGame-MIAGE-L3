package share.forge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.FactoryFace;

import java.util.Map;

class PoolTest {

    static FactoryForge fc;
    static Map<Integer, Pool> lis;
    static int[] cout = {2, 3, 4, 5, 6, 8, 12};


    @BeforeEach
    void initTest() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        fc = new FactoryForge();
        lis = fc.createPool();

    }

    @Test
    void getCost() {
        for (int prix : cout) {
            Assertions.assertEquals(prix, lis.get(prix).getCost());
        }
    }

    @Test
    void getFace() { //TODO

    }


}