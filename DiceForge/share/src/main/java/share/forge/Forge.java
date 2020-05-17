package share.forge;

import share.exeption.CaseOfPoolForgeOutOfBound;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.Face;
import share.face.FaceSpecial;
import share.face.FaceSpecialEnum;
import share.face.FactoryFace;

import java.util.*;

/**
 * The type Forge.
 */
public class Forge {

    private Map<Integer,Pool> listPools;
    private EnumMap<FaceSpecialEnum, List<FaceSpecial>> faceSpecial;

    /**
     * Instantiates a new Forge.
     *
     * @throws InstanceFaceOutOfBoundException the instance share.face out of bound exception
     */
    public Forge() throws InstanceFaceOutOfBoundException {
        FactoryForge f = new FactoryForge();
        this.listPools =  f.createPool();
        this.faceSpecial = FactoryFace.getInstance().getFacesSpecial();
    }

    /**
     * Get list pools map.
     *
     * @return the map
     */
    public Map<Integer,Pool> getListPools(){
        return this.listPools;
    }

    /**
     * Gets share.face on pool.
     *
     * @param poolNumber the pool number
     * @param casePool   the case pool
     * @return the share.face on pool
     * @throws CaseOfPoolForgeOutOfBound the case of pool share.forge out of bound
     */
    public Face getFaceOnPool(int poolNumber, int casePool) throws CaseOfPoolForgeOutOfBound {
        if(this.listPools.containsKey(poolNumber)) return this.listPools.get(poolNumber).getFace(casePool);
        else throw new CaseOfPoolForgeOutOfBound("Pool not exist with : "+poolNumber);
    }

    public Face getFaceWithRemoveOnPool(int poolNumber, int casePool) throws CaseOfPoolForgeOutOfBound {
        if(this.listPools.containsKey(poolNumber)) return this.listPools.get(poolNumber).getFaceWithRemove(casePool);
        else throw new CaseOfPoolForgeOutOfBound("Pool not exist with : "+poolNumber);
    }

    /**
     * Get face special face special.
     *
     * @param face the face
     * @return the face special
     */
    public FaceSpecial getFaceSpecial(FaceSpecialEnum face){
        if(!this.faceSpecial.get(face).isEmpty()) return this.faceSpecial.get(face).remove(0);
        else try {
            throw new CaseOfPoolForgeOutOfBound("No facespecial "+face.name());
        } catch (CaseOfPoolForgeOutOfBound caseOfPoolForgeOutOfBound) {
            caseOfPoolForgeOutOfBound.printStackTrace();
        }
        return null;
    }

}

