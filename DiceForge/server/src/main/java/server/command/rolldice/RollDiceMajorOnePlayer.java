package server.command.rolldice;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.face.Face;
import share.utils.Printer;

import java.util.List;
import java.util.UUID;

/**
 * The type Roll dice major one player.
 */
public class RollDiceMajorOnePlayer extends CommandRollDice {

    private final UUID idPlayer;
    /**
     * The Handler action roll dice.
     */
    HandlerActionRollDice handlerActionRollDice;

    /**
     * Instantiates a new Roll dice major one player.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer the id player
     */
    public RollDiceMajorOnePlayer(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager, "ROLL_DICE_MAJOR_ONE_PLAYER");
        this.idPlayer = idPlayer;
        this.handlerActionRollDice = new HandlerActionRollDice(getContext(),getManager());
    }

    /**
     * Execute action Roll Dice Major. Roll all dices of player and trigger actions faces.
     */
    @Override
    public void onExecute() {
        Printer.getInstance().printCommand(this.getName(),"EXECUTE ROLL DICE MAJOR FOR "+idPlayer,this.getStack());
        List<Face> faceRoll = getContext().getGame().getPlayer(idPlayer).getInventory().rollDiceMajorList(); //roll
        this.handlerActionRollDice.executeRollDiceMajor(faceRoll.get(0),faceRoll.get(1),idPlayer);
        this.getManager().triggerCommandHandleActionRollDice(this.handlerActionRollDice); // execute
        this.getContext().getStats().incNbRollDiceMajorPlayer(idPlayer);
    }
}
