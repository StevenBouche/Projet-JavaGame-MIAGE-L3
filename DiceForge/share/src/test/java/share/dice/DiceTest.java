package share.dice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import share.face.Face;
import share.face.FaceSimple;
import share.ressource.TypeRessource;
import share.utils.HandleRandom;

class DiceTest {

    static Dice dice;
    static Face face;
    static Face face2;
    static HandleRandom r;


    @BeforeEach
    void initTest(){
        dice = new Dice();
        face = new FaceSimple(TypeRessource.GOLD,2);
        dice.addFace(1,face);
        face2 =  new FaceSimple(TypeRessource.GOLD,3);

       r = Mockito.mock(HandleRandom.class);
       Mockito.doReturn(1).when(r).getRandomBetweenMinMax(Mockito.any(Integer.class),Mockito.any(Integer.class));


    }

    @Test
    void addFace(){
        dice.addFace(2,face2);
        Assertions.assertEquals(face,dice.getFace(1));
        Assertions.assertEquals(face2,dice.mapFaces.get(2));
    }

    @Test
    void removeFace() {
        dice.removeFace(2);
        Assertions.assertEquals(null,dice.getFace(2));
    }

    @Test
    void roll() {
        Assertions.assertEquals(1, r.getRandomBetweenMinMax(1, 5));

    }
        @Test
        void getFace () {
            Assertions.assertEquals(face, dice.mapFaces.get(1));
        }

        @Test
        void testToString () {
            Assertions.assertEquals("2 of GOLD | ", dice.toString());
        }

    }