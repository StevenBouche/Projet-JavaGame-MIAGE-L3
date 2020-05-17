package share.choice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.FaceHybrid;
import share.face.FaceHybridEnum;
import share.face.FactoryFace;
import share.ressource.TypeRessource;

class ChoiceHybridFaceTest {

    static ChoiceBetweenRessource choiceHybridFace;
    static TypeRessource typeRessource;
    static FaceHybrid faceHybrid;
    static TypeRessource typeRessource2;

    @BeforeEach
    void initTest() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        typeRessource = TypeRessource.LUNAR;
        faceHybrid = FactoryFace.getInstance().getFaceHybrid(FaceHybridEnum.GOLD1_GLORY1_SOLAR1_LUNAR1);
        choiceHybridFace = new ChoiceBetweenRessource(faceHybrid.listRessource,typeRessource);
        typeRessource2 = TypeRessource.GOLD;
    }

    @Test
    void getFaceHybrid() {
        Assertions.assertEquals(faceHybrid.getListRessource().toString(),choiceHybridFace.getListRessource().toString());
    }

    @Test
    void getTypeRessource() {
        Assertions.assertEquals(typeRessource,choiceHybridFace.getTypeRessource());
    }

    @Test
    void setTypeRessource() {
        choiceHybridFace.setTypeRessource(typeRessource2);
        Assertions.assertEquals(typeRessource2,choiceHybridFace.getTypeRessource());
    }
}