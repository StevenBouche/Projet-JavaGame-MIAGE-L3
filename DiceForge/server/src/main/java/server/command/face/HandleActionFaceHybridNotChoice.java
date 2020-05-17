package server.command.face;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.face.FaceHybrid;
import share.ressource.TypeRessource;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The type Handle action face hybrid not choice.
 */
public class HandleActionFaceHybridNotChoice extends CommandFace {

    /**
     * The Event face hybrid.
     */
    Map<UUID, List<FaceHybrid>> eventFaceHybrid;
    /**
     * The Face.
     */
    FaceHybrid face;
    /**
     * The Mode.
     */
    final int mode;

    /**
     * Instantiates a new Handle action face hybrid not choice.
     *
     * @param context         the context
     * @param manager         the manager
     * @param eventFaceHybrid the event face hybrid
     */
    public HandleActionFaceHybridNotChoice(IGameManager context, CommandManager manager, Map<UUID, List<FaceHybrid>> eventFaceHybrid) {
        super(context, manager, false,"HANDLE_ACTION_FACE_HYBRID_NOT_CHOICE");
        this.eventFaceHybrid = eventFaceHybrid;
        mode = ModeHandleTypeFace.LIST.id;
    }

    /**
     * Instantiates a new Handle action face hybrid not choice.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer the id player
     * @param face     the face
     */
    public HandleActionFaceHybridNotChoice(IGameManager context, CommandManager manager, UUID idPlayer, FaceHybrid face) {
        super(context, manager, idPlayer,false, "HANDLE_ACTION_FACE_HYBRID_NOT_CHOICE");
        this.face = face;
        mode = ModeHandleTypeFace.ELEMENT.id;
    }

    /**
     * Instantiates a new Handle action face hybrid not choice.
     *
     * @param context         the context
     * @param manager         the manager
     * @param eventFaceHybrid the event face hybrid
     * @param inverse         the inverse
     */
    public HandleActionFaceHybridNotChoice(IGameManager context, CommandManager manager, Map<UUID, List<FaceHybrid>> eventFaceHybrid, boolean inverse) {
        super(context, manager, inverse,"HANDLE_ACTION_FACE_HYBRID_NOT_CHOICE");
        this.eventFaceHybrid = eventFaceHybrid;
        mode = ModeHandleTypeFace.LIST.id;
    }

    /**
     * Instantiates a new Handle action face hybrid not choice.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer the id player
     * @param face     the face
     * @param inverse  the inverse
     */
    public HandleActionFaceHybridNotChoice(IGameManager context, CommandManager manager, UUID idPlayer, FaceHybrid face, boolean inverse) {
        super(context, manager, idPlayer, inverse,"HANDLE_ACTION_FACE_HYBRID_NOT_CHOICE");
        this.face = face;
        mode = ModeHandleTypeFace.ELEMENT.id;
    }

    /**
     * Handle face hybrid action with no choice, and in function if list or element
     */
    @Override
    public void onExecute() {
        if(mode== ModeHandleTypeFace.LIST.id) handleFaceHybridAllPlayer();
        else if(mode== ModeHandleTypeFace.ELEMENT.id) handleFaceHybridOnePlayerOneFace();
    }

    /**
     * Do action of face hybrid not choice for one player
     */
    private void handleFaceHybridOnePlayerOneFace() {
        this.print("HANDLE "+face+" PLAYER "+idPlayer);
        for (TypeRessource e : face.getListRessource().keySet() ){ //pour toutes ressource on supprimer de l'inventaire du joueur
            this.addRessourcePlayer(idPlayer, e, face.getListRessource().get(e));
        }
    }

    /**
     * Do action of all face hybrid for all player
     */
    private void handleFaceHybridAllPlayer() {
        Map<UUID, List<FaceHybrid>> clone = this.cloneMapListFace(eventFaceHybrid);
        for(UUID id : clone.keySet()){
            for(FaceHybrid fh : clone.get(id)){
                this.print("HANDLE "+fh+" PLAYER "+id);
                for (TypeRessource e : fh.getListRessource().keySet() ){ //pour toutes ressource on supprimer de l'inventaire du joueur
                    this.addRessourcePlayer(id, e, fh.getListRessource().get(e));
                }
                eventFaceHybrid.get(id).remove(fh);
            }
            eventFaceHybrid.remove(id);
        }
    }

}
