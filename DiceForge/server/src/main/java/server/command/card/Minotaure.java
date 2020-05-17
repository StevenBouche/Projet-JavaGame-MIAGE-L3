package server.command.card;

import server.command.CommandManager;
import server.command.rolldice.HandlerActionRollDice;
import server.game.GameManager;
import server.game.IGameManager;
import share.face.Face;
import share.player.Player;

import java.util.Map;
import java.util.UUID;

/**
 * The type Minotaure.
 */
public class Minotaure extends CommandCard {

    UUID idPlayer;
    HandlerActionRollDice handlerActionRollDice;
    /**
     * Instantiates a new Minotaure.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Minotaure(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"MINOTAURE_COMMAND");
        this.idPlayer = idPlayer;
        this.handlerActionRollDice = new HandlerActionRollDice(getContext(),getManager(),true);
    }

    /**
     *
     * Execute action of MinotaureCard : All other players roll dice major and loose instead of win resource
     *
     */
    @Override
    public void onExecute() {
        for(Player p : this.getContext().getGame().getPlayers().values()){
            if(p.getId() != idPlayer){
                Map<Integer, Face> res = this.getContext().getGame().getPlayer(p.getId()).getInventory().rollDiceMajorMap();
                this.print("PLAYER "+p.getId()+" HAVE ROLL FOR MINOTAURE "+res.get(0)+" "+res.get(1));
                this.handlerActionRollDice.executeRollDiceMajor(res.get(0),res.get(1),p.getId());
            }
        }
        this.getManager().triggerCommandHandleActionRollDice(this.handlerActionRollDice);
    }

}
