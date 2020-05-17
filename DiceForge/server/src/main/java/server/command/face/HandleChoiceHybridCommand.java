package server.command.face;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.choice.ChoiceBetweenRessource;
import share.eventclientserver.Events;
import share.face.FaceHybrid;
import server.statistics.Statistics;
import share.utils.Printer;

import java.util.*;

/**
 * The type Handle choice hybrid command.
 */
public class HandleChoiceHybridCommand extends CommandFaceChoice<ChoiceBetweenRessource> {

    /**
     * The Event face hybrid.
     */
    public Map<UUID, List<FaceHybrid>> eventFaceHybrid;
    /**
     * The Events numbers.
     */
    public int eventsNumbers = 0;

    /**
     * The Face hybrid.
     */
    FaceHybrid faceHybrid;
    /**
     * The Mode.
     */
    final int mode;

    /**
     * Instantiates a new Handle choice hybrid command.
     *
     * @param context the context
     * @param manager the manager
     * @param events  the events
     */
    public HandleChoiceHybridCommand(IGameManager context, CommandManager manager, Map<UUID, List<FaceHybrid>> events) {
        super(context,manager,false,"HANDLE_CHOICE_HYBRID_COMMAND");
        this.eventFaceHybrid = events;
        mode = ModeHandleTypeFace.LIST.id;
    }

    /**
     * Instantiates a new Handle choice hybrid command.
     *
     * @param context the context
     * @param manager the manager
     * @param events  the events
     * @param inverse the inverse
     */
    public HandleChoiceHybridCommand(IGameManager context, CommandManager manager, Map<UUID, List<FaceHybrid>> events, boolean inverse) {
        super(context,manager,inverse,"HANDLE_CHOICE_HYBRID_COMMAND");
        this.eventFaceHybrid = events;
        mode = ModeHandleTypeFace.LIST.id;
    }

    /**
     * Instantiates a new Handle choice hybrid command.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer the id player
     * @param face     the face
     */
    public HandleChoiceHybridCommand(IGameManager context, CommandManager manager, UUID idPlayer, FaceHybrid face) {
        super(context,manager,idPlayer,false,"HANDLE_CHOICE_HYBRID_COMMAND");
        this.faceHybrid = face;
        mode = ModeHandleTypeFace.ELEMENT.id;
    }

    /**
     * Instantiates a new Handle choice hybrid command.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer the id player
     * @param face     the face
     * @param inverse  the inverse
     */
    public HandleChoiceHybridCommand(IGameManager context, CommandManager manager, UUID idPlayer, FaceHybrid face, boolean inverse) {
        super(context,manager,idPlayer,inverse,"HANDLE_CHOICE_HYBRID_COMMAND");
        this.faceHybrid = face;
        mode = ModeHandleTypeFace.ELEMENT.id;
    }

    /**
     * Execute action Face Hybrid in function of mode command. Handle more than one choice if is a list, if element handle one choice.
     */
    @Override
    public void onExecute() {
        if(mode==ModeHandleTypeFace.LIST.id)  this.handleFaceHybridWithDifferentsEvents();
        else if(mode==ModeHandleTypeFace.ELEMENT.id) {
            Printer.getInstance().printCommand(this.getName(),"HANDLE EVENT ROLL DICE CHOICE HYBRID FOR "+idPlayer,this.getStack());
            this.handleFaceHybrid();
        }
    }

    /**
     * To notify when we receive respond of player
     *
     * @param idPlayer ID session player
     * @param choice choice object with respond of player
     *
     */
    @Override
    public void notifyChoice(UUID idPlayer, ChoiceBetweenRessource choice)  {
        this.notifyEventFullfiled(idPlayer, choice);
    }

    /**
     * Execute action when choice resource.
     */
    private void handleFaceHybrid() {
        execChoiceHybrid(idPlayer, faceHybrid);
    }

    /**
     * Handle more than one choice client and more than one player.
     */
    private void handleFaceHybridWithDifferentsEvents(){
        Map<UUID, List<FaceHybrid>> mapC = this.cloneMapListFace(this.eventFaceHybrid); //copie de la list  pour travailler dessus
        for(UUID id : mapC.keySet()) this.eventsNumbers += mapC.get(id).size(); // determine le nombre d'event total de face hybrid choice pour tous les joueurs
        this.print("HANDLE "+this.eventsNumbers+" EVENTS ROLL DICE CHOICE HYBRID");
        while(this.eventsNumbers != 0){ // tant que tous les events n'ont pas etait traiter
            for (UUID entry : mapC.keySet()) { // exec pour toutes les events
                if(!mapC.get(entry).isEmpty()) {
                    this.print("HANDLE EVENTS ROLL DICE CHOICE HYBRID FOR PLAYER "+entry);
                    for(FaceHybrid face : mapC.get(entry)) execChoiceHybrid(entry, face);
                }
                eventFaceHybrid.remove(entry);
            }
        }
    }

    /**
     * Request choice player
     *
     * @param idPlayer id session player
     * @param face face object to get her resources
     */
    private void execChoiceHybrid(UUID idPlayer, FaceHybrid face){
        this.resetStartTime();
        getContext().sendEventToClient(idPlayer,Events.CHOICE_BETWEEN_RESSOURCES,new ChoiceBetweenRessource(face.listRessource)); // send la demande de choix client
        this.print("PLAYER WANT "+face.listRessource.toString() +" ?");
        this.waitingChoice();
        int valueRessource = this.choice.getListRessource().get(this.choice.getTypeRessource()); // choice client ok
        Statistics.getInstance().addHybridChoice(idPlayer, this.choice.getTypeRessource(), valueRessource);
        this.print("PLAYER WANT "+valueRessource+" "+this.choice.getTypeRessource());
        this.addRessourcePlayer(idPlayer,this.choice.getTypeRessource(), valueRessource);
        this.resetChoice();
    }

    /**
     * Notify when player reply and signal to handle it.
     *
     * @param id id session player
     * @param choiceHybridFace choice object
     */
    private void notifyEventFullfiled(UUID id, ChoiceBetweenRessource choiceHybridFace) {
        this.eventsNumbers--;
        this.choice = choiceHybridFace;
        this.getContext().getStats().incNbTimeReflexionPlayers(id, this,System.currentTimeMillis()-this.getStartTime());
        this.notifyDecision(); // notifie pour la poursuite du traitement
    }

}
