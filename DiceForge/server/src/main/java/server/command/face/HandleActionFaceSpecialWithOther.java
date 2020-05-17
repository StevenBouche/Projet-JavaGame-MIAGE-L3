package server.command.face;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.face.*;

import java.util.UUID;

/**
 * The enum Mode special with other.
 */
enum ModeSpecialWithOther {
    /**
     * Special special mode special with other.
     */
    SPECIAL_SPECIAL(0),
    /**
     * Special hybrid mode special with other.
     */
    SPECIAL_HYBRID(1),
    /**
     * Special simple mode special with other.
     */
    SPECIAL_SIMPLE(2);
    /**
     * The Id.
     */
    int id;
    ModeSpecialWithOther(int v){
        this.id=v;
    }
}

/**
 * The type Handle action face special with other.
 */
public class HandleActionFaceSpecialWithOther extends CommandFace {

    /**
     * The Face special 1.
     */
    final FaceSpecial faceSpecial1;
    /**
     * The Face special 2.
     */
    FaceSpecial faceSpecial2;
    /**
     * The Face hybrid 2.
     */
    FaceHybrid faceHybrid2;
    /**
     * The Face simple 2.
     */
    FaceSimple faceSimple2;
    /**
     * The Mode.
     */
    int mode;

    /**
     * Instantiates a new Handle action face special with other.
     *
     * @param context    the context
     * @param manager    the manager
     * @param idPlayer   the id player
     * @param face       the face
     * @param faceChoice the face choice
     */
    public HandleActionFaceSpecialWithOther(IGameManager context, CommandManager manager, UUID idPlayer, FaceSpecial face, Face faceChoice) {
        super(context, manager, idPlayer,false,"HANDLE_FACE_SPECIAL_ACTION");
        this.faceSpecial1 = face;
        if(faceChoice instanceof FaceSpecial){
            mode = ModeSpecialWithOther.SPECIAL_SPECIAL.id;
            this.faceSpecial2 = (FaceSpecial) faceChoice;
        } else if(faceChoice instanceof  FaceHybrid){
            mode = ModeSpecialWithOther.SPECIAL_HYBRID.id;
            this.faceHybrid2 = (FaceHybrid) faceChoice;
        } else if(faceChoice instanceof FaceSimple){
            mode = ModeSpecialWithOther.SPECIAL_SIMPLE.id;
            this.faceSimple2 = (FaceSimple) faceChoice;
        }
    }

    /**
     * Instantiates a new Handle action face special with other.
     *
     * @param context    the context
     * @param manager    the manager
     * @param idPlayer   the id player
     * @param face       the face
     * @param faceChoice the face choice
     * @param inverse    the inverse
     */
    public HandleActionFaceSpecialWithOther(IGameManager context, CommandManager manager, UUID idPlayer, FaceSpecial face, Face faceChoice, boolean inverse) {
        this(context,manager,idPlayer,face,faceChoice);
        this.inverse = inverse;
    }

    /**
     * Execute action of face special with other in function of type of other face
     */
    @Override
    public void onExecute() {
        if(mode==ModeSpecialWithOther.SPECIAL_SPECIAL.id) hanleSpecialAndSpecial();
        else if(mode==ModeSpecialWithOther.SPECIAL_HYBRID.id) handleSpecialAndHybrid();
        else if(mode==ModeSpecialWithOther.SPECIAL_SIMPLE.id) handleSpecialAndSimple();
    }

    /**
     * Handle action special with simple
     */
    private void handleSpecialAndSimple() {
        if(faceSpecial1.faceEnum == FaceSpecialEnum.FACE_MULTIPLE_3) this.handleFaceX3WithFaceSimple();
        else if(faceSpecial1.faceEnum == FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER) this.handleFaceOtherWithFaceSimple();
    }

    /**
     * Handle action special with hybrid
     */
    private void handleSpecialAndHybrid() {
        if(faceSpecial1.faceEnum == FaceSpecialEnum.FACE_MULTIPLE_3) this.handleFaceX3WithFaceHybrid();
        else if(faceSpecial1.faceEnum == FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER) this.handleFaceOtherWithFaceHybrid();
    }

    /**
     * Handle action special with special
     */
    private void hanleSpecialAndSpecial() {
        if(faceSpecial1.faceEnum == FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER &&
                faceSpecial2.faceEnum == FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER) {
            this.triggerHandlerFaceOther();
            this.triggerHandlerFaceOther();
        }
        else if(faceSpecial1.faceEnum == FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER &&
                faceSpecial2.faceEnum == FaceSpecialEnum.FACE_MULTIPLE_3) this.triggerHandlerFaceOther(faceSpecial2);
        else if(faceSpecial1.faceEnum == FaceSpecialEnum.FACE_MULTIPLE_3 &&
                faceSpecial2.faceEnum == FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER) this.triggerHandlerFaceOther(faceSpecial1);
    }

    /**
     * Handle face special Other with hybrid
     */
    private void handleFaceOtherWithFaceHybrid() {
        this.triggerHandlerFaceHybrid(this.faceHybrid2);
        this.triggerHandlerFaceOther();
    }

    /**
     * Handle face x3 with hybrid
     */
    private void handleFaceX3WithFaceHybrid() {
        FaceHybrid fh = this.faceHybrid2.clone();
        fh.multiplyValue(3);
        this.triggerHandlerFaceHybrid(fh);
    }

    /**
     * Handle face special other with simple
     */
    private void handleFaceOtherWithFaceSimple() {
        this.getManager().triggerCommandHandleFaceSimple(idPlayer,this.faceSimple2,this.inverse);
        this.triggerHandlerFaceOther();
    }

    /**
     * Handle face x3 with simple
     */
    private void handleFaceX3WithFaceSimple() {
        this.addRessourcePlayer(idPlayer, this.faceSimple2.getTypeRessource(), this.faceSimple2.getValue()*3);
    }

    /**
     * Trigger handler face Other
     * @param fs other face special
     */
    private void triggerHandlerFaceOther(FaceSpecial fs) {
        this.getManager().triggerCommandActionFaceSpecialPower(idPlayer,fs,this.inverse);
    }

    /**
     * Trigger handler face Other with no other face
     */
    private void triggerHandlerFaceOther() {
        this.getManager().triggerCommandActionFaceSpecialPower(idPlayer,null,this.inverse);
    }

    /**
     * Trigger handler face hybrid
     * @param fs face hybrid
     */
    private void triggerHandlerFaceHybrid(FaceHybrid fs) {
        if(!fs.isChoice()) this.getManager().triggerCommandHandleNotChoiceHybrid(idPlayer,fs,this.inverse);
         else this.getManager().triggerCommandHandleHybrid(idPlayer,fs,this.inverse);
    }
}
