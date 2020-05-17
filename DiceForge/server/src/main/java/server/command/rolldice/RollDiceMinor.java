package server.command.rolldice;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.face.Face;
import share.utils.Printer;

import java.util.UUID;

/**
 * The type Roll dice minor.
 */
public class RollDiceMinor extends CommandRollDice {

    private final UUID idplayer;
    /**
     * The Handler action roll dice.
     */
    HandlerActionRollDice handlerActionRollDice;

    /**
     * Instantiates a new Roll dice minor.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer the id player
     */
    public RollDiceMinor(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"ROLL_DICE_MINOR");
        this.idplayer = idPlayer;
        this.handlerActionRollDice = new HandlerActionRollDice(getContext(),getManager());
    }

    /**
     * Execute action Roll Dice Minor. Roll one RNG dice of player and trigger action face.
     */
    @Override
    public void onExecute() {
        Printer.getInstance().printCommand(this.getName()," ROLL DICE MINOR FOR PLAYER : "+idplayer,this.getStack());
        Face f = getContext().getGame().getPlayer(idplayer).getInventory().rollDiceMinor();
        this.getContext().getStats().incNbRollDiceMinorPlayer(idplayer);
        this.handlerActionRollDice.executeRollDiceMinor(f,idplayer);
        this.getManager().triggerCommandHandleActionRollDice(this.handlerActionRollDice);
    }
}
