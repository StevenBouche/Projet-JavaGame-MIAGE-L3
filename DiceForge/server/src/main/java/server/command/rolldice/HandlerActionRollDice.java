package server.command.rolldice;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.face.*;

import java.util.*;

/**
 * The type Handler action roll dice.
 */
public class HandlerActionRollDice extends CommandRollDice {

    final Map<UUID, List<FaceHybrid>> eventFaceHybridChoice;
    final Map<UUID, List<FaceSimple>> eventFaceSimple;
    final Map<UUID, List<FaceHybrid>> eventFaceHybrid;
    final Map<UUID, List<FaceSpecial>> eventFaceSpecial;
    final List<UUID> listPlayerHaveTrigger;
    private boolean inverse;

    /**
     * Get inverse command boolean.
     *
     * @return the boolean
     */
    public boolean getInverse(){
        return this.inverse;
    }

    /**
     * Instantiates a new Handler action roll dice.
     *
     * @param gameManager the game manager
     * @param manager     the manager
     */
    public HandlerActionRollDice(IGameManager gameManager, CommandManager manager){
        super(gameManager,manager,"HANDLE_ACTION_ROLL");
        this.listPlayerHaveTrigger = new ArrayList<>();
        this.eventFaceSpecial = new HashMap<>();
        this.eventFaceHybrid = new HashMap<>();
        this.eventFaceSimple = new HashMap<>();
        this.eventFaceHybridChoice = new HashMap<>();
        this.inverse = false;
    }

    /**
     * Instantiates a new Handler action roll dice.
     *
     * @param gameManager the game manager
     * @param manager     the manager
     * @param inverse     the inverse
     */
    public HandlerActionRollDice(IGameManager gameManager, CommandManager manager, boolean inverse){
        super(gameManager,manager,"HANDLE_ACTION_ROLL");
        this.listPlayerHaveTrigger = new ArrayList<>();
        this.eventFaceSpecial = new HashMap<>();
        this.eventFaceHybrid = new HashMap<>();
        this.eventFaceSimple = new HashMap<>();
        this.eventFaceHybridChoice = new HashMap<>();
        this.inverse = inverse;
    }

    /**
     * Trigger all face in all queue list.
     */
    @Override
    public void onExecute() {
        triggerFinishOnExecuteRollDice();
    }

    /**
     * Execute roll dice minor.
     *
     * @param face     the face
     * @param idPlayer the id player
     */
    public void executeRollDiceMinor(Face face, UUID idPlayer){
        this.print( "Player "+idPlayer+" has roll :\t"+face.toString());
        this.handleAction(idPlayer,face);
    }

    /**
     * Execute roll dice major.
     *
     * @param faceOne  the face one
     * @param faceTwo  the face two
     * @param idPlayer the id player
     */
    public void executeRollDiceMajor(Face faceOne, Face faceTwo, UUID idPlayer) {
        this.print( "Player "+idPlayer+" has roll :\t"+faceOne.toString()+"\t"+faceTwo.toString());
        this.handleAction(idPlayer,faceOne);
        this.handleAction(idPlayer,faceTwo);
    }

    /**
     * Add face in one list in function of different types faces.
     * @param id id session player
     * @param f face object
     */
    private void handleAction(UUID id, Face f){
        if(!this.listPlayerHaveTrigger.contains(id)) this.listPlayerHaveTrigger.add(id);
        if(f instanceof FaceSimple) this.actionSimple(id, (FaceSimple) f);
        else if(f instanceof FaceHybrid) this.actionHybrid(id, (FaceHybrid) f);
        else if(f instanceof FaceSpecial) this.actionSpecial(id, (FaceSpecial) f);
    }

    /**
     * Add in queue special
     * @param id id session player
     * @param f face special object
     */
    private void actionSpecial(UUID id, FaceSpecial f) {
        this.addEvent(id,eventFaceSpecial,f);
    }

    /**
     * Add in queue simple
     *
     * @param idPlayer is session player
     * @param fs face simple object
     */
    private void actionSimple(UUID idPlayer, FaceSimple fs){
        this.addEvent(idPlayer,eventFaceSimple,fs);
    }

    /**
     * Add in queue hybrid
     *
     * @param idPlayer is session player
     * @param fh face hybrid object
     */
    private void actionHybrid(UUID idPlayer, FaceHybrid fh){
        if(!fh.isChoice()) this.addEvent(idPlayer,eventFaceHybrid,fh);
        else this.addEvent(idPlayer,eventFaceHybridChoice,fh);
    }

    /**
     * Add a Face in map of player and her all face.
     *
     * @param idPlayer id session player
     * @param map map to add element
     * @param face face object
     * @param <T> generic refer to class child of Face
     */
    private <T extends Face> void addEvent(UUID idPlayer, Map<UUID, List<T>> map, T face){
        map.computeIfAbsent(idPlayer, k -> new ArrayList<>()); // si la list n'existe pa on la creer
        if(canAddEvent(idPlayer)) map.get(idPlayer).add(face); // on l'ajoute a la liste de traitement
    }

    /**
     * Can add event in queue in function of limitation by player.
     * Limitation is 2 events.
     *
     * @param idPlayer id session player
     * @return true if nb events < 2
     */
    private boolean canAddEvent(UUID idPlayer){
        if(getNbRollPlayer(idPlayer) < 2) return true;
        else try {
            throw new Exception("try add queue but player have already 2 event");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Trigger all queue of face for all players.
     */
    private void triggerFinishOnExecuteRollDice() {
        this.triggerComboSpecial();
        this.triggerOtherEvents();
        this.testAllEventsHasTrigger();
    }

    /**
     * Trigger all face special or combo special with one other face
     */
    private void triggerComboSpecial() {
        for(UUID id : this.listPlayerHaveTrigger){
            if(this.eventFaceSpecial.get(id) != null) {
                int sizeSpecial = this.eventFaceSpecial.get(id).size();
                if(sizeSpecial == 1) playerHaveOneFaceSpecial(id);
                else if(sizeSpecial == 2) playerHaveTwoFaceSpecial(id);
            }
        }
    }

    /**
     * Trigger queue of all type face with different handler
     */
    private void triggerOtherEvents() {
        if(inverse){
            if(!this.eventFaceSimple.isEmpty()) getManager().triggerCommandHandleFaceSimple(this.eventFaceSimple, inverse);
            if(!this.eventFaceHybrid.isEmpty()) getManager().triggerCommandHandleHybrid(this.eventFaceHybrid, inverse);
            if(!this.eventFaceHybridChoice.isEmpty()) getManager().triggerCommandHandleChoiceHybrid(this.eventFaceHybridChoice, inverse);
        } else {
            if(!this.eventFaceSimple.isEmpty()) getManager().triggerCommandHandleFaceSimple(this.eventFaceSimple);
            if(!this.eventFaceHybrid.isEmpty()) getManager().triggerCommandHandleHybrid(this.eventFaceHybrid);
            if(!this.eventFaceHybridChoice.isEmpty()) getManager().triggerCommandHandleChoiceHybrid(this.eventFaceHybridChoice);
        }
    }

    /**
     * Verification if one event is always here. Problem if not equals at 0 because rest one event in queue.
     */
    private void testAllEventsHasTrigger() {
        for(UUID id : this.listPlayerHaveTrigger){
            if(getNbRollPlayer(id) != 0){
                try {
                    throw new Exception("Event not finish "+id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Handle two face special
     * @param idPlayer id session player
     */
    private void playerHaveTwoFaceSpecial(UUID idPlayer) {
        FaceSpecial fs1 = this.eventFaceSpecial.get(idPlayer).remove(1);
        FaceSpecial fs2 = this.eventFaceSpecial.get(idPlayer).remove(0);
        this.triggerCommandActionFaceSpecialWithOther(idPlayer,fs1,fs2);
    }

    /**
     * Handle one face special with other type
     * @param idPlayer id session player
     */
    private void playerHaveOneFaceSpecial(UUID idPlayer) {
        FaceSpecial fs1 = this.eventFaceSpecial.get(idPlayer).remove(0);
        if(getNbRollPlayer(idPlayer) == 0){
            if(fs1.faceEnum == FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER)
                getManager().triggerCommandActionFaceSpecialPower(idPlayer, null, inverse);
        } else if(getNbRollPlayer(idPlayer) == 1){
            if(getSizeHybrid(idPlayer) == 1)
                this.triggerCommandActionFaceSpecialWithOther(idPlayer,fs1,this.eventFaceHybrid.get(idPlayer).remove(0));
            else if(getSizeSimple(idPlayer) == 1)
                this.triggerCommandActionFaceSpecialWithOther(idPlayer,fs1,this.eventFaceSimple.get(idPlayer).remove(0));
            else if(getSizeHybridChoice(idPlayer) == 1)
                this.triggerCommandActionFaceSpecialWithOther(idPlayer,fs1,this.eventFaceHybridChoice.get(idPlayer).remove(0));
        } else try {
            throw new Exception("OTHER EVENT WTF");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Trigger action special with other
     *
     * @param idPlayer id session player
     * @param face face special
     * @param face2 other face
     */
    private void triggerCommandActionFaceSpecialWithOther(UUID idPlayer, FaceSpecial face, Face face2){
        if(inverse) getManager().triggerCommandActionFaceSpecialWithOther(idPlayer,face,face2, inverse);
        else getManager().triggerCommandActionFaceSpecialWithOther(idPlayer,face,face2);
    }

    /**
     * Get nb roll player int.
     *
     * @param id the id
     * @return the int
     */
    int getNbRollPlayer(UUID id){
        int sizeSpecial = getSizeSpecial(id);
        int sizeHybride = getSizeHybrid(id);
        int sizeSimple = getSizeSimple(id);
        int sizeHybrideChoice = getSizeHybridChoice(id);
         return sizeSpecial + sizeHybride +sizeSimple + sizeHybrideChoice;
    }

    private int getSizeSpecial(UUID id){
        if(this.eventFaceSpecial.get(id) != null) return this.eventFaceSpecial.get(id).size();
        return 0;
    }

    private int getSizeSimple(UUID id){
        if(this.eventFaceSimple.get(id) != null) return this.eventFaceSimple.get(id).size();
        return 0;
    }

    private int getSizeHybrid(UUID id){
        if(this.eventFaceHybrid.get(id) != null) return this.eventFaceHybrid.get(id).size();
        return 0;
    }

    private int getSizeHybridChoice(UUID id){
        if(this.eventFaceHybridChoice.get(id) != null) return this.eventFaceHybridChoice.get(id).size();
        return 0;
    }


}
