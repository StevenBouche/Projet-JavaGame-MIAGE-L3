package share.forge;

import share.exeption.CaseOfPoolForgeOutOfBound;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.Face;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Pool.
 */
public class Pool {

    private int cost;
    /**
     * The Listface.
     */
    public List<Face> listface;

    /**
     * Instantiates a new Pool.
     *
     * @param cost the cost
     * @throws InstanceFaceOutOfBoundException the instance share.face out of bound exception
     */
    public Pool(int cost) throws InstanceFaceOutOfBoundException {
        this.listface = new ArrayList<>();
        this.cost = cost;
        this.init();
    }

    /**
     * Instantiates a new Pool.
     */
    public Pool(){}

    private void init() throws InstanceFaceOutOfBoundException {

        switch (this.getCost()) {
            case 2: listface = PoolEnum.POOL2.getListFace(); break;
            case 3: listface = PoolEnum.POOL3.getListFace(); break;
            case 4: listface = PoolEnum.POOL4.getListFace(); break;
            case 5: listface = PoolEnum.POOL5.getListFace(); break;
            case 6: listface = PoolEnum.POOL6.getListFace(); break;
            case 8: listface = PoolEnum.POOL8.getListFace(); break;
            case 12: listface = PoolEnum.POOL12.getListFace(); break;
        }

    }

    /**
     * Gets cost.
     *
     * @return the cost
     */
    public int getCost() {
        return cost;
    }


    /**
     * Gets share.face pool.
     *
     * @param caseNb the case nb
     * @return the share.face pool
     * @throws CaseOfPoolForgeOutOfBound the case of pool share.forge out of bound
     */
    public Face getFaceWithRemove(int caseNb) throws CaseOfPoolForgeOutOfBound { //TODO revoir
        if(this.listface.get(caseNb) == null) throw new CaseOfPoolForgeOutOfBound("Face in this case is null");
        return this.listface.remove(caseNb);
    }

    public Face getFace(int caseNb) throws CaseOfPoolForgeOutOfBound { //TODO revoir
        if(this.listface.get(caseNb) == null) throw new CaseOfPoolForgeOutOfBound("Face in this case is null");
        return this.listface.get(caseNb);
    }

    public String toString(){
        return this.listface.toString();
    }

}

