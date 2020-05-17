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
 * The type Mirror.
 */
public class Mirror extends CommandCardChoice<ChoiceForgeFaceSpecial> {

    private final UUID idPlayer;

    /**
     * Instantiates a new Mirror.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Mirror(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"MIRROR_COMMAND");
        this.idPlayer = idPlayer;
    }

    /**
     *
     * Execute action of MirrorCard : Player win a special face, request where player want craft face on her dice
     *
     */
    @Override
    public void onExecute() {
        FaceSpecial f = this.getContext().getGame().getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
        this.print("PLAYER "+idPlayer+" NEED CRAFT "+f+" ON HER DICE");
        ChoiceForgeFaceSpecial ch = new ChoiceForgeFaceSpecial(f);
        this.getContext().sendEventToClient(idPlayer, Events.CHOICE_FORGE_SPECIAL,ch);
        this.waitingChoice();
        this.print("PLAYER "+idPlayer+" WANT CRAFT "+f+" ON DICE "+this.choice.indexDice+" FACE "+this.choice.indexDiceFace);
        Face oldFace = getContext().getGame().getFaceOnDicePlayer(idPlayer,this.choice.indexDice,this.choice.indexDiceFace);
        Statistics.getInstance().addChoiceForgeFaceSpecial(idPlayer, oldFace, f);
        this.getManager().triggerCommandForgeFaceOnDice(idPlayer, f, this.choice.indexDice,this.choice.indexDiceFace);
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
        this.choice = (ChoiceForgeFaceSpecial) ch;
        this.getContext().getStats().incNbTimeReflexionPlayers(this.idPlayer, this,System.currentTimeMillis()-this.getStartTime());
        this.notifyDecision();
    }
}
