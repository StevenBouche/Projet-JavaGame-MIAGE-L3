package server.command.card;

import server.command.CommandManager;
import server.command.rolldice.HandlerActionRollDice;
import server.game.IGameManager;
import server.statistics.Statistics;
import server.game.GameManager;
import share.choice.ChoiceSatyre;
import share.eventclientserver.Events;
import share.face.Face;
import share.player.Player;

import java.util.Map;
import java.util.UUID;

/**
 * The type Satyres.
 */
public class Satyres extends CommandCardChoice<ChoiceSatyre> {

    private final UUID idPlayer;
    /**
     * The Handler action roll dice.
     */
    HandlerActionRollDice handlerActionRollDice;

    /**
     * Instantiates a new Satyres.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Satyres(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"SATYRE_COMMAND");
        this.idPlayer = idPlayer;
        this.handlerActionRollDice = new HandlerActionRollDice(getContext(),getManager());
    }

    /**
     *
     * Execute action of SatyreCard : all other players roll dice major and player choice 2 faces to execute her power
     *
     */
    @Override
    public void onExecute() {
        ChoiceSatyre ch = setUpChoice();
        this.print("PLAYER "+idPlayer+" WANT 2 FACE ? "+ch.rollPlayers.toString());
        this.getContext().sendEventToClient(idPlayer, Events.CHOICE_SATYRE,ch);
        this.waitingChoice();
        this.print("PLAYER "+idPlayer+" WANT "+this.choice.faceChoiceOne+" "+this.choice.faceChoiceTwo);
        Statistics.getInstance().addSatyreChoice(idPlayer, this.choice.faceChoiceOne,this.choice.faceChoiceTwo);
        this.handlerActionRollDice.executeRollDiceMajor(this.choice.faceChoiceOne,this.choice.faceChoiceTwo,idPlayer);
        //this.handlerActionRollDice.triggerFinishOnExecuteRollDice();
        this.getManager().triggerCommandHandleActionRollDice(this.handlerActionRollDice);
    }

    /**
     * Set up Choice satyre
     *
     * @return ChoiceSatyre object
     */
    private ChoiceSatyre setUpChoice() {
        ChoiceSatyre ch = new ChoiceSatyre();
        for(Player p : this.getContext().getGame().getPlayers().values()){
            if(p.getId() != idPlayer){
                Map<Integer, Face> res = this.getContext().getGame().getPlayer(p.getId()).getInventory().rollDiceMajorMap();
                ch.addRollsPlayers(p.getId(),res.get(0),res.get(1));
                this.print("PLAYER "+p.getId()+" HAVE ROLL FOR SATYRE "+res.get(0)+" "+res.get(1));
            }
        }
        return ch;
    }

    /**
     * To notify when we receive respond of player
     *
     * @param idPlayer ID session player
     * @param ch choice object with respond of player
     *
     */
    @Override
    public void notifyChoice(UUID idPlayer, ChoiceSatyre ch){
        this.choice =  ch;
        this.getContext().getStats().incNbTimeReflexionPlayers(this.idPlayer, this,System.currentTimeMillis()-this.getStartTime());
        this.notifyDecision();
    }
}
