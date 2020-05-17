package share.face;

import share.config.ConfigGame;
import share.exeption.InstanceFaceOutOfBoundException;
import share.ressource.TypeRessource;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * The type Factory share.face.
 */
public class FactoryFace {

    /**
     * Instantiates a new Factory face.
     */
    private static final ThreadLocal<FactoryFace> _threadLocal =
            new ThreadLocal<FactoryFace>() {
                @Override
                protected FactoryFace initialValue() {
                    return new FactoryFace();
                }
            };

    public static synchronized FactoryFace getInstance() {
        return _threadLocal.get();
    }

    public static synchronized void resetInstance(){
        _threadLocal.remove();
    }

    private FactoryFace(){
        this.initFaceSimpleOfGame();
        this.initFaceHybridOfGame();
        this.initFaceSpecialOfGame();
    }

    private EnumMap<FaceSimpleEnum, List<FaceSimple>> listFaceSimple;
    private EnumMap<FaceHybridEnum, List<FaceHybrid>> listFaceHybrid;
    private EnumMap<FaceSpecialEnum, List<FaceSpecial>> listFaceSpecial;

    private void initFaceSimpleOfGame(){
        this.listFaceSimple = new EnumMap<>(FaceSimpleEnum.class);
        for(FaceSimpleEnum f : FaceSimpleEnum.values()){
            this.listFaceSimple.put(f,this.createXInstanceOfFaceSimple(f.type,f.value,f.nbInstanceMax));
        }
    }

    private void initFaceHybridOfGame(){
        this.listFaceHybrid = new EnumMap<>(FaceHybridEnum.class);
        for(FaceHybridEnum f : FaceHybridEnum.values()){
            this.listFaceHybrid.put(f, this.createXInstanceOfFaceHybrid(f.choice,f.listRessource,f.nbInstanceMax,f.toString()));
        }
    }

    private void initFaceSpecialOfGame(){
        this.listFaceSpecial = new EnumMap<>(FaceSpecialEnum.class);
        for(FaceSpecialEnum f : FaceSpecialEnum.values()){
            this.listFaceSpecial.put(f, this.createXInstanceOfFaceSpecial(ConfigGame.NB_PLAYER_MAX,f.toString(),f));
        }
    }

    /**
     * Gets share.face simple.
     *
     * @param face the share.face
     * @return the share.face simple
     * @throws InstanceFaceOutOfBoundException the instance share.face out of bound exception
     */
    public FaceSimple getFaceSimple(FaceSimpleEnum face) throws InstanceFaceOutOfBoundException {
        List<FaceSimple> l = this.listFaceSimple.get(face);
        if(l.isEmpty()) {
            throw new InstanceFaceOutOfBoundException("Face Simple instance max depasser");
        }
        else return l.remove(0);
    }

    /**
     * Gets share.face hybrid.
     *
     * @param face the share.face
     * @return the share.face hybrid
     * @throws InstanceFaceOutOfBoundException the instance share.face out of bound exception
     */
    public FaceHybrid getFaceHybrid(FaceHybridEnum face) throws InstanceFaceOutOfBoundException {
        List<FaceHybrid> l = this.listFaceHybrid.get(face);
        if(l.isEmpty()) {
            throw new InstanceFaceOutOfBoundException("Face Hybrid instance max depasser");
        }
        else return l.remove(0);
    }

    /**
     * Gets face special.
     *
     * @param face the face
     * @return the face special
     * @throws InstanceFaceOutOfBoundException the instance face out of bound exception
     */
    public FaceSpecial getFaceSpecial(FaceSpecialEnum face) throws InstanceFaceOutOfBoundException {
        List<FaceSpecial> l = this.listFaceSpecial.get(face);
        if(l.isEmpty()) throw new InstanceFaceOutOfBoundException("Face Hybrid instance max depasser");
        else return l.remove(0);
    }

    /**
     * Gets faces special.
     *
     * @return the faces special
     * @throws InstanceFaceOutOfBoundException the instance face out of bound exception
     */
    public EnumMap<FaceSpecialEnum, List<FaceSpecial>> getFacesSpecial() throws InstanceFaceOutOfBoundException {
        return this.listFaceSpecial;
    }

    private ArrayList<FaceSimple> createXInstanceOfFaceSimple(TypeRessource type, int value, int nbInstance){
        ArrayList<FaceSimple> list = new ArrayList<>();
        for(int i = 0; i < nbInstance; i++){
            list.add(new FaceSimple(type,value));
        }
        return list;
    }

    private ArrayList<FaceHybrid> createXInstanceOfFaceHybrid(boolean choice, EnumMap<TypeRessource, Integer> map, int nbInstance, String name){
        ArrayList<FaceHybrid> list = new ArrayList<>();
        for(int i = 0; i < nbInstance; i++){
            list.add(new FaceHybrid(choice,map,name));
        }
        return list;
    }

    private ArrayList<FaceSpecial> createXInstanceOfFaceSpecial(int nbInstance, String name, FaceSpecialEnum face){
        ArrayList<FaceSpecial> list = new ArrayList<>();
        for(int i = 0; i < nbInstance; i++){
            list.add(new FaceSpecial(name,face));
        }
        return list;
    }




}
