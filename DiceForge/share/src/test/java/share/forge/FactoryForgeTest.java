package share.forge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.FactoryFace;

import java.util.Map;

class FactoryForgeTest {
    static Map<Integer,Pool> map;
    static FactoryForge facto;


    @BeforeEach
    void initTest() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        facto = new FactoryForge();
        map =  facto.createPool();
    }

    @Test
    void createPool() throws InstanceFaceOutOfBoundException {
       /* FactoryFace.resetInstance();
        Assertions.assertEquals(map.get(2).getCost(),facto.createPool().get(2).getCost());
        Assertions.assertEquals(map.get(3).getCost(),facto.createPool().get(3).getCost());
        Assertions.assertEquals(map.get(4).getCost(),facto.createPool().get(4).getCost());
        Assertions.assertEquals(map.get(5).getCost(),facto.createPool().get(5).getCost());
        Assertions.assertEquals(map.get(6).getCost(),facto.createPool().get(6).getCost());
        Assertions.assertEquals(map.get(8).getCost(),facto.createPool().get(8).getCost());
        Assertions.assertEquals(map.get(12).getCost(),facto.createPool().get(12).getCost());*/
    }
}
