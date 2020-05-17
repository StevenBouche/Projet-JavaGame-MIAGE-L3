package server.command.card;

import server.command.CommandManager;
import server.game.IGameManager;
import server.statistics.Statistics;
import server.game.GameManager;
import share.choice.Choice3GoldFor4Glory;
import share.eventclientserver.Events;
import share.ressource.TypeRessource;

import java.util.UUID;

public class Ancient extends CommandCardChoice<Choice3GoldFor4Glory> {

    private final UUID idPlayer;


    /**
     * Instantiates a new Ancient.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Ancient(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager, "ANCIENT_COMMAND");
        this.idPlayer = idPlayer;
    }

    /**
     * Execute action of AncienCard : loose 3 gold to win 4 glory
     */
    @Override
    public void onExecute() {
        int valueGold = this.getContext().getGame().getValueGoldPlayer(idPlayer);
        if(valueGold >= 3){
            this.print("PLAYER "+idPlayer+" WANT BUY 4 GLORY WITH 3 GOLD ?");
            Choice3GoldFor4Glory choice = setUpChoice(valueGold);
            this.getContext().sendEventToClient(idPlayer, Events.CHOICE_3GOLD_FOR_4GLORY,choice);
            this.waitingChoice();
            Statistics.getInstance().add3GoldFor4GloryChoice(idPlayer, this.choice.choice);
            if(this.choice.choice) this.executeAction();
            else this.print("PLAYER "+idPlayer+" DONT ACCEPT");
        }
        else this.print("PLAYER "+idPlayer+" HAVE LESS THAN 3 GOLD - DO NOT EXECUTE");
    }

    private void executeAction() {
        this.print("PLAYER "+idPlayer+" ACCEPT");
        this.removeRessourcePlayer(idPlayer,TypeRessource.GOLD,3);
      //  this.getManager().removeRessourcePlayerCommand(idPlayer,TypeRessource.GOLD,3);
        this.addRessourcePlayer(idPlayer,TypeRessource.GLORY,4);
    }

    private void handleChoice(UUID idPlayer, Choice3GoldFor4Glory choice){
        this.choice = choice;
        this.getContext().getStats().incNbTimeReflexionPlayers(this.idPlayer, this,System.currentTimeMillis()-this.getStartTime());
        this.notifyDecision();
    }

    private Choice3GoldFor4Glory setUpChoice(int valueGoldPlayer){
        Choice3GoldFor4Glory choice = new Choice3GoldFor4Glory();
        choice.valueGold = valueGoldPlayer;
        choice.idPlayer = this.idPlayer;
        return choice;
    }

    @Override
    public void notifyChoice(UUID idPlayer, Choice3GoldFor4Glory choice) {
         this.handleChoice(idPlayer, choice);
    }
}
