package server.command.rolldice;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.face.Face;

import java.util.*;

/**
 * The type Roll dice major all player.
 */
public class RollDiceMajorAllPlayer extends CommandRollDice {

    /**
     * The Id player.
     */
    public UUID idPlayer;
    /**
     * The Face one.
     */
    public Face faceOne;
    /**
     * The Face two.
     */
    public Face faceTwo;
    /**
     * The Handler action roll dice.
     */
    HandlerActionRollDice handlerActionRollDice;

    /**
     * Instantiates a new Roll dice major all player.
     *
     * @param context the context
     * @param manager the manager
     */
    public RollDiceMajorAllPlayer(IGameManager context, CommandManager manager){
        super(context, manager,"ROLL_DICE_ALL_PLAYER_COMMAND");
        this.handlerActionRollDice = new HandlerActionRollDice(getContext(),getManager());
    }

    /**
     * Execute action Roll Dice Major for all player in game.
     */
    @Override
    public void onExecute() {
        rollMajor();
    }

    /**
     * For all player trigger all dices and add face to queue, to execute at the end
     */
    private void rollMajor(){
        for(UUID id : getContext().getGame().getIdPlayers()) { // pour tous les joueurs
            List<Face> faceRoll =getContext().getGame().getPlayers().get(id).getInventory().rollDiceMajorList(); // on roll les des
            this.idPlayer = id;
            this.faceOne = faceRoll.get(0);
            this.faceTwo = faceRoll.get(1);
            this.handlerActionRollDice.executeRollDiceMajor(this.faceOne,this.faceTwo,idPlayer); // execute les actions des face via un handler
            this.getContext().getStats().incNbRollDiceMajorPlayer(idPlayer);
        }
        this.getManager().triggerCommandHandleActionRollDice(this.handlerActionRollDice); //execute les rolls
    }

}
