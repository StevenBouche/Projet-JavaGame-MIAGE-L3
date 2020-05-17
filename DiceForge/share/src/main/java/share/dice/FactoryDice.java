package share.dice;

import share.exeption.InstanceFaceOutOfBoundException;
import share.face.FaceSimpleEnum;
import share.face.FactoryFace;
import java.util.ArrayList;

/**
 * The type FactoryDice.
 * Create 2 dices with their different faces.
 */
public class FactoryDice {

    /**
     * Create dices share.player array list.
     *
     * @return the array list
     */
    public static /*synchronized*/ ArrayList<Dice> createDicesPlayer(){

        ArrayList<Dice> list = new ArrayList<>();
        Dice d1 = new Dice();
        Dice d2 = new Dice();
        FactoryFace factory = FactoryFace.getInstance();

        try {
            d1.addFace(0, factory.getFaceSimple(FaceSimpleEnum.GOLD_ONE));
            d1.addFace(1, factory.getFaceSimple(FaceSimpleEnum.GOLD_ONE));
            d1.addFace(2, factory.getFaceSimple(FaceSimpleEnum.GOLD_ONE));
            d1.addFace(3, factory.getFaceSimple(FaceSimpleEnum.GOLD_ONE));
            d1.addFace(4, factory.getFaceSimple(FaceSimpleEnum.SOLAR_ONE));
            d1.addFace(5, factory.getFaceSimple(FaceSimpleEnum.GOLD_ONE));

            d2.addFace(0, factory.getFaceSimple(FaceSimpleEnum.LUNAR_ONE));
            d2.addFace(1, factory.getFaceSimple(FaceSimpleEnum.GOLD_ONE));
            d2.addFace(2, factory.getFaceSimple(FaceSimpleEnum.GOLD_ONE));
            d2.addFace(3, factory.getFaceSimple(FaceSimpleEnum.GOLD_ONE));
            d2.addFace(4, factory.getFaceSimple(FaceSimpleEnum.GOLD_ONE));
            d2.addFace(5, factory.getFaceSimple(FaceSimpleEnum.GLORY_TWO));
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }

        list.add(d1);
        list.add(d2);
        return list;
    }

}
