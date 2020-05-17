package server.command.card;

import server.command.CommandManager;
import server.game.IGameManager;
import server.statistics.Statistics;
import server.game.GameManager;
import share.choice.ChoiceForgeFaceSpecial;
import share.eventclientserver.Events;
import share.face.Face;
import share.face.FaceSpecial;
import share.face.FaceSpecialEnum;

import java.util.UUID;

/**
 * The type Invisible.
 */
public class Invisible extends CommandCardChoice<ChoiceForgeFaceSpecial> {

    private final UUID idPlayer;

    /**
     * Instantiates a new Invisible.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Invisible(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"INVISIBLE_COMMAND");
        this.idPlayer = idPlayer;
    }

    /**
     *
     * Execute action of InvisibleCard : Player win a special face x3, request to player where he want craft face on her dice
     *
     */
    @Override
    public void onExecute() {
        FaceSpecial f = this.getContext().getGame().getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
        ChoiceForgeFaceSpecial ch = new ChoiceForgeFaceSpecial(f);
        this.getContext().sendEventToClient(idPlayer, Events.CHOICE_FORGE_SPECIAL,ch);
        this.waitingChoice();
        this.print("PLAYER "+idPlayer+" WANT CRAFT "+f+" ON DICE "+this.choice.indexDice+" FACE "+this.choice.indexDiceFace);
        Face oldFace = getContext().getGame().getFaceOnDicePlayer(idPlayer,this.choice.indexDice,this.choice.indexDiceFace);
        this.getManager().triggerCommandForgeFaceOnDice(idPlayer, f, this.choice.indexDice,this.choice.indexDiceFace);
        Statistics.getInstance().addChoiceForgeFaceSpecial(idPlayer, oldFace, f);
    }

    /**
     * To notify when we receive respond of player
     *
     * @param idPlayer ID session player
     * @param ch choice object with respond of player
     *
     */
    @Override
    public void notifyChoice(UUID idPlayer, ChoiceForgeFaceSpecial ch){
        this.choice = ch;
        this.getContext().getStats().incNbTimeReflexionPlayers(this.idPlayer, this,System.currentTimeMillis()-this.getStartTime());
        this.notifyDecision();
    }

}
