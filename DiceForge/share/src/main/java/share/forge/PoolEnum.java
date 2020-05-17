package share.forge;

import share.face.Face;
import share.face.FaceHybridEnum;
import share.face.FaceSimpleEnum;
import share.face.FactoryFace;
import share.exeption.InstanceFaceOutOfBoundException;

import java.util.*;

/**
 * The type Couple share.face type.
 */
class CoupleFaceType{
    /**
     * The Number of share.face.
     */
    int nbFace;

    /**
     * Instantiates a new Couple share.face type.
     *
     * @param nb the nb
     */
    public CoupleFaceType(int nb) {
        this.nbFace = nb;
    }
}

/**
 * The type Couple share.face simple type.
 */
class CoupleFaceSimpleType extends CoupleFaceType{
    /**
     * The Face.
     */
    FaceSimpleEnum face;

    /**
     * Instantiates a new Couple share.face simple type.
     *
     * @param f  the f
     * @param nb the nb
     */
    public CoupleFaceSimpleType(FaceSimpleEnum f, int nb){
        super(nb);
        this.face = f;
    }
}

/**
 * The type Couple share.face hybrid type.
 */
class CoupleFaceHybridType extends CoupleFaceType{
    /**
     * The Face.
     */
    FaceHybridEnum face;

    /**
     * Instantiates a new Couple share.face hybrid type.
     *
     * @param f  the f
     * @param nb the nb
     */
    public CoupleFaceHybridType(FaceHybridEnum f, int nb){
        super(nb);
        this.face = f;
    }
}

/**
 * The enum Pool enum.
 */
public enum PoolEnum {

    /**
     * The Pool 2.
     */
    POOL2(2,
            new CoupleFaceSimpleType(FaceSimpleEnum.GOLD_THREE,4),
            new CoupleFaceSimpleType(FaceSimpleEnum.LUNAR_ONE,4)
    ),
    /**
     * The Pool 3.
     */
    POOL3(3,
            new CoupleFaceSimpleType(FaceSimpleEnum.SOLAR_ONE,4),
            new CoupleFaceSimpleType(FaceSimpleEnum.GOLD_FOUR,4)
    ),
    /**
     * The Pool 4.
     */
    POOL4(4,
            new CoupleFaceSimpleType(FaceSimpleEnum.GOLD_SIX,1),
            new CoupleFaceHybridType(FaceHybridEnum.GOLD2_LUNAR1,1),
            new CoupleFaceHybridType(FaceHybridEnum.GLORY1_SOLAR1,1),
            new CoupleFaceHybridType(FaceHybridEnum.GOLD1_OR_SOLAR1_OR_LUNAR1,1)
    ),
    /**
     * The Pool 5.
     */
    POOL5(5,
            new CoupleFaceHybridType(FaceHybridEnum.GOLD3_OR_GLORY2,4)
    ),
    /**
     * The Pool 6.
     */
    POOL6(6,
            new CoupleFaceSimpleType(FaceSimpleEnum.LUNAR_TWO,4)
    ),
    /**
     * The Pool 8.
     */
    POOL8(8,
            new CoupleFaceSimpleType(FaceSimpleEnum.GLORY_THREE,4),
            new CoupleFaceSimpleType(FaceSimpleEnum.SOLAR_TWO,4)),
    /**
     * The Pool 12.
     */
    POOL12(12,
            new CoupleFaceSimpleType(FaceSimpleEnum.GLORY_FOUR,1),
            new CoupleFaceHybridType(FaceHybridEnum.GOLD1_GLORY1_SOLAR1_LUNAR1,1),
            new CoupleFaceHybridType(FaceHybridEnum.GOLD2_OR_SOLAR2_OR_LUNAR2,1),
            new CoupleFaceHybridType(FaceHybridEnum.GLORY2_LUNAR2,1)
    );

    /**
     * The Cost.
     */
    public int cost;
    /**
     * The List couple.
     */
    public List<CoupleFaceType> listCouple;

    PoolEnum(int value, CoupleFaceType... listCouple){
        this.cost = value;
        this.listCouple = Arrays.asList(listCouple);
    }

    /**
     * Gets list share.face.
     *
     * @return the list share.face
     * @throws InstanceFaceOutOfBoundException the instance share.face out of bound exception
     */
    public List<Face> getListFace() throws InstanceFaceOutOfBoundException {
        List<Face> list = new ArrayList<>();
        FactoryFace f = FactoryFace.getInstance();
        for(CoupleFaceType cp : this.listCouple){
            for(int i = 0; i < cp.nbFace; i++){
                if(cp instanceof CoupleFaceSimpleType) list.add(f.getFaceSimple(((CoupleFaceSimpleType) cp).face));
                else if(cp instanceof CoupleFaceHybridType) list.add(f.getFaceHybrid(((CoupleFaceHybridType) cp).face));
            }
        }
        return list;
    }

    /**
     * Get costs pool list.
     *
     * @return the list
     */
    public static synchronized List<Integer> getCostsPool(){
        List<Integer> list = new ArrayList<>();
        for(PoolEnum p : PoolEnum.values()){ list.add(p.cost);}
        Collections.sort(list);
        return list;
    }

    public static boolean getHavePoolOnCost(int cost){
        for(PoolEnum p : PoolEnum.values()) if(p.cost == cost) return true;
        return false;
    }

}
