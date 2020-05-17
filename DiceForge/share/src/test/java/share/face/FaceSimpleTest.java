package share.face;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import share.ressource.TypeRessource;


class FaceSimpleTest {
    static FaceSimple facesimple;

    @BeforeEach
    void initTest() {
        facesimple = new FaceSimple(TypeRessource.GOLD, 5);
    }


    @Test
    void getTypeRessource() {
        Assertions.assertEquals(TypeRessource.GOLD,facesimple.getTypeRessource());
    }

    @Test
    void getValue() {
        Assertions.assertEquals(5,facesimple.getValue());
    }
}