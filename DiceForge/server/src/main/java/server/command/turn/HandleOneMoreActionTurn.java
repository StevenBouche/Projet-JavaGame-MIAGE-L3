package server.command.turn;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.choice.ChoiceOneMoreTurn;
import share.eventclientserver.Events;
import share.ressource.TypeRessource;
import server.statistics.Statistics;

import java.util.UUID;

/**
 * The type Handle one more action turn.
 */
public class HandleOneMoreActionTurn extends CommandActionTurnChoice<ChoiceOneMoreTurn> {

    /**
     * Instantiates a new Handle one more action turn.
     *
     * @param context the context
     * @param manager the manager
     */
    public HandleOneMoreActionTurn(IGameManager context, CommandManager manager) {
        super(context, manager,"MORE_ACTION_COMMAND");
    }

    /**
     * Trigger to player choice if want give one more action turn.
     * Only if player have 2 solar resource.
     */
    @Override
    public void onExecute() {
        this.print("PLAYER "+idPlayer+" WANT MORE TURN  ?");
        ChoiceOneMoreTurn ch = setUpChoice();
        if(ch.valueSolaryPlayer >= 2){ // need 2 solar to dive one more turn
            this.getContext().sendEventToClient(idPlayer, Events.CHOICE_ONE_MORE_ACTION, ch);
            this.waitingChoice();
            if(this.choice.choice) { // if player have choose true
                this.print("PLAYER WANT ONE MORE TURN : "+idPlayer);
                Statistics.getInstance().addOneMoreTurnChoice(idPlayer);
                this.getContext().getStats().incNbActionMoreTurn(idPlayer);
                this.removeRessourcePlayer(idPlayer,TypeRessource.SOLAR,2);
                this.getManager().triggerCommandActionTurn(); // trigger command action turn
            }
        }else {
            this.print("PLAYER "+idPlayer+" CANNOT GIVE MORE TURN HAVE NOT RESSOURCE 2 SOLARY MINI");
        }
    }

    /**
     * Set up choice to send client.
     * @return ChoiceOneMoreTurn object
     */
    private ChoiceOneMoreTurn setUpChoice(){
        ChoiceOneMoreTurn ch = new ChoiceOneMoreTurn();
        ch.valueSolaryPlayer = this.getContext().getGame().getValueRessourcePlayer(idPlayer,TypeRessource.SOLAR);
        return ch;
    }

    /**
     * handle when player have reply.
     * @param b ChoiceOneMoreTurn object
     */
    private void handleOneMoreTurn(ChoiceOneMoreTurn b){
        this.choice = b;
        this.getContext().getStats().incNbTimeReflexionPlayers(this.getContext().getGame().currentPlayer.getId(),this, System.currentTimeMillis()-this.getStartTime());
        this.notifyDecision();
    }

    /**
     * Notify when is last command. SetUp reply player and signal condition to continue execute game.
     * @param idPlayer id socket player reply
     * @param choice choice object of player
     */
    @Override
    public void notifyChoice(UUID idPlayer, ChoiceOneMoreTurn choice) {
        this.handleOneMoreTurn(choice);
    }
}
