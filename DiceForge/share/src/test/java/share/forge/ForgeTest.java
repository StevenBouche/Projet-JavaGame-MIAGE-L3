package share.forge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.FactoryFace;

import java.util.Map;

class ForgeTest {
    static Forge f;
    static FactoryForge fc ;
    static Map<Integer,Pool> lis;

    @BeforeEach
    void initTest() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        f = new Forge();
        lis = f.getListPools();
    }

    @Test
    void getListPools() {
        for(Map.Entry<Integer, Pool> p: lis.entrySet()){
           Pool value =  p.getValue();
           Integer k =  p.getKey();
            Map<Integer,Pool> pm = f.getListPools();
            Assertions.assertEquals(value.toString(),pm.get(k).toString());
        }
    }

}