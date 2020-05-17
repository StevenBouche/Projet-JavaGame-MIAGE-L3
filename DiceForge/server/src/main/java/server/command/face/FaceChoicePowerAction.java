package server.command.face;

import server.command.CommandManager;
import server.command.rolldice.HandlerActionRollDice;
import server.game.IGameManager;
import server.statistics.Statistics;
import server.game.GameManager;
import share.choice.ChoicePowerOnDiceOtherPlayer;
import share.eventclientserver.Events;
import share.face.Face;
import share.face.FaceSpecial;
import share.face.FaceSpecialEnum;
import share.player.Player;

import java.util.Map;
import java.util.UUID;

/**
 * The enum Mode face choice power action.
 */
enum ModeFaceChoicePowerAction {
    /**
     * Have x 3 mode face choice power action.
     */
    HAVE_X3(1),
    /**
     * Dont have x 3 mode face choice power action.
     */
    DONT_HAVE_X3(0);
    /**
     * The Id.
     */
    int id;
    ModeFaceChoicePowerAction(int v){
        this.id=v;
    }
}

/**
 * The type Face choice power action.
 */
public class FaceChoicePowerAction extends CommandFaceChoice<ChoicePowerOnDiceOtherPlayer> {

    private final FaceSpecial face;
    /**
     * The Handler action roll dice.
     */
    HandlerActionRollDice handlerActionRollDice;
    /**
     * The Mode.
     */
    int mode;

    /**
     * Instantiates a new Face choice power action.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer id player of source action
     * @param face     if face has been roll with other face special else null
     */
    public FaceChoicePowerAction(IGameManager context, CommandManager manager, UUID idPlayer, FaceSpecial face) {
        super(context, manager, idPlayer,false,"FACE_CHOICE_POWER");
        this.face = face;
        this.handlerActionRollDice = new HandlerActionRollDice(getContext(),getManager());
       setUpMode();
    }

    /**
     * Instantiates a new Face choice power action.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer id player of source action
     * @param face     if face has been roll with other face special else null
     * @param inverse  to switch between add or remove resource
     */
    public FaceChoicePowerAction(IGameManager context, CommandManager manager, UUID idPlayer, FaceSpecial face, boolean inverse) {
        super(context, manager, idPlayer,inverse,"FACE_CHOICE_POWER");
        this.face = face;
        this.handlerActionRollDice = new HandlerActionRollDice(getContext(),getManager());
        setUpMode();
    }

    /**
     *  For all other player roll her dice and request to player source which power he want trigger for him
     *  if other face special is a x3 trigger effect choice by player with x3 effect.
     *  else trigger effect of face choose by player
     */
    @Override
    public void onExecute() {
        ChoicePowerOnDiceOtherPlayer ch = new ChoicePowerOnDiceOtherPlayer();
        for(Player p : this.getContext().getGame().getPlayers().values()){
            if(p.getId() != idPlayer){
                Map<Integer, Face> res = this.getContext().getGame().getLastRollsPlayer(p.getId());
                ch.addRollsPlayers(p.getId(),res.get(0),res.get(1));
                this.print("PLAYER "+p.getId()+" HAVE "+res.get(0)+" "+res.get(1));
            }
        }
        this.getContext().sendEventToClient(idPlayer, Events.CHOICE_POWER_OTHER_PLAYER,ch);
        this.waitingChoice();
        this.print("PLAYER "+idPlayer+" WANT "+this.choice.faceChoice);
        Statistics.getInstance().addFaceChoicePowerChoice(idPlayer, this.choice.faceChoice);
        if(mode==ModeFaceChoicePowerAction.HAVE_X3.id) this.getManager().triggerCommandActionFaceSpecialWithOther(idPlayer,this.face,this.choice.faceChoice);
        else if(mode==ModeFaceChoicePowerAction.DONT_HAVE_X3.id) {
            this.handlerActionRollDice.executeRollDiceMinor(this.choice.faceChoice,idPlayer);
            this.getManager().triggerCommandHandleActionRollDice(this.handlerActionRollDice);
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
    public void notifyChoice(UUID idPlayer, ChoicePowerOnDiceOtherPlayer choice)  {
            this.choice = choice;
            this.getContext().getStats().incNbTimeReflexionPlayers(this.idPlayer, this,System.currentTimeMillis()-this.getStartTime());
            this.notifyDecision();
    }

    /**
     * Setup mode in function of type enum face special
     */
    private void setUpMode(){
        if(this.face == null) mode = ModeFaceChoicePowerAction.DONT_HAVE_X3.id;
        else if(this.face.faceEnum == FaceSpecialEnum.FACE_MULTIPLE_3) mode = ModeFaceChoicePowerAction.HAVE_X3.id;
    }


}
