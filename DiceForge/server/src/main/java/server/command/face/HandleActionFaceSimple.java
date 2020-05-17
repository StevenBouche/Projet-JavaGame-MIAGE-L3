package server.command.face;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.face.FaceSimple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The type Handle action face simple.
 */
public class HandleActionFaceSimple extends CommandFace {

    /**
     * The Event face simple.
     */
    Map<UUID, List<FaceSimple>> eventFaceSimple;
    /**
     * The Face simple.
     */
    FaceSimple faceSimple;
    /**
     * The Mode.
     */
    final int mode;


    /**
     * Instantiates a new Handle action face simple.
     *
     * @param context         the context
     * @param manager         the manager
     * @param eventFaceSimple the event face simple
     */
    public HandleActionFaceSimple(IGameManager context, CommandManager manager, Map<UUID, List<FaceSimple>> eventFaceSimple) {
        super(context, manager, false,"HANDLE_ACTION_FACE_SIMPLE");
        this.eventFaceSimple = eventFaceSimple;
        mode = ModeHandleTypeFace.LIST.id;
    }

    /**
     * Instantiates a new Handle action face simple.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer the id player
     * @param face     the face
     */
    public HandleActionFaceSimple(IGameManager context, CommandManager manager, UUID idPlayer, FaceSimple face) {
        super(context, manager, false,"HANDLE_ACTION_FACE_SIMPLE");
        this.idPlayer = idPlayer;
        this.faceSimple = face;
        mode = ModeHandleTypeFace.ELEMENT.id;
    }

    /**
     * Instantiates a new Handle action face simple.
     *
     * @param context         the context
     * @param manager         the manager
     * @param eventFaceSimple the event face simple
     * @param inverse         the inverse
     */
    public HandleActionFaceSimple(IGameManager context, CommandManager manager, Map<UUID, List<FaceSimple>> eventFaceSimple,boolean inverse) {
        super(context, manager, inverse,"HANDLE_ACTION_FACE_SIMPLE");
        this.eventFaceSimple = eventFaceSimple;
        mode = ModeHandleTypeFace.LIST.id;
    }

    /**
     * Instantiates a new Handle action face simple.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer the id player
     * @param face     the face
     * @param inverse  the inverse
     */
    public HandleActionFaceSimple(IGameManager context, CommandManager manager, UUID idPlayer, FaceSimple face,boolean inverse) {
        super(context, manager, inverse,"HANDLE_ACTION_FACE_SIMPLE");
        this.idPlayer = idPlayer;
        this.faceSimple = face;
        mode = ModeHandleTypeFace.ELEMENT.id;
    }

    /**
     * In function of Mode List or element, add all resource to a player
     */
    @Override
    public void onExecute() {
        if(mode==ModeHandleTypeFace.LIST.id){
            Map<UUID, List<FaceSimple>> clone = this.cloneMapListFace(eventFaceSimple);
            for(UUID id : clone.keySet()){
                for(FaceSimple fs : clone.get(id)){
                    this.print("HANDLE "+fs+" PLAYER "+id);
                    this.addRessourcePlayer(id, fs.getTypeRessource(), fs.getValue());
                    eventFaceSimple.get(id).remove(fs);
                }
                eventFaceSimple.remove(id);
            }
        } else if(mode==ModeHandleTypeFace.ELEMENT.id){
            this.print("HANDLE "+faceSimple+" PLAYER "+idPlayer);
            this.addRessourcePlayer(idPlayer, faceSimple.getTypeRessource(), faceSimple.getValue());
        }

    }

}
