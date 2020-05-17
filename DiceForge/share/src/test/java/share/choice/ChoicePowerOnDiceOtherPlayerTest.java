package share.choice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import share.dice.CoupleRollDice;
import share.face.Face;
import share.face.FaceSimple;
import share.ressource.TypeRessource;

import java.util.Map;
import java.util.UUID;


class ChoicePowerOnDiceOtherPlayerTest {

    static ChoicePowerOnDiceOtherPlayer choice;
    static UUID id;
    static Face face1;
    static Face face2;
    static Map<UUID, CoupleRollDice> map;
    static CoupleRollDice cr;
    static Map<UUID, CoupleRollDice> f;


    @BeforeEach
    void initTest(){
        choice = new ChoicePowerOnDiceOtherPlayer();
        id = UUID.randomUUID();
        face1 = new FaceSimple(TypeRessource.GOLD,2);
        face2 = new FaceSimple(TypeRessource.LUNAR,5);
        choice.addRollsPlayers(id,face1,face2);
        f = choice.rollPlayers;
    }

    @Test
    void addRollsPlayers() {
        Assertions.assertNotNull(choice.rollPlayers.get(id));
        Assertions.assertEquals(choice.rollPlayers.get(id).faceOne,face1);
        Assertions.assertEquals(choice.rollPlayers.get(id).faceTwo,face2);
    }
}