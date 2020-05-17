package server.command.card;

import server.command.CommandManager;
import server.game.IGameManager;
import server.statistics.Statistics;
import server.game.GameManager;
import share.choice.ChoiceHammer;
import share.eventclientserver.Events;
import share.ressource.TypeRessource;

import java.util.UUID;

/**
 * The type Hammer.
 */
public class Hammer extends CommandCardChoice<ChoiceHammer> {

    private final int cmdP;
    private final UUID idPlayer;
    private int valueChoiceHammer;

    /**
     * Instantiates a new Hammer.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Hammer(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"HAMMER_COMMAND");
        this.cmdP = 0;
        this.idPlayer = idPlayer;
    }

    /**
     * Instantiates a new Hammer.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param cmdP     mode parameter, 0 to add new hammer, 1 to execute action hammer
     * @param idPlayer id player of source action
     *
     */
    public Hammer(IGameManager context, CommandManager manager, int cmdP, UUID idPlayer) {
        super(context, manager,"HAMMER_COMMAND");
        this.cmdP = cmdP;
        this.idPlayer = idPlayer;
    }

    /**
     * Execute action of HammerCard : Hammer have two actions
     *    Add new hammer to an player
     *    question player to know if he want store gold on hammer when he win gold resource
     */
    @Override
    public void onExecute() {
        if(cmdP == 0) this.setNewHammerToPlayer();
        else if (cmdP == 1) this.effectHammer();
    }

    /**
     * Sets value choice hammer object to send it.
     *
     * @param valueChoiceHammer the gold value win by player
     */
    public void setValueChoiceHammer(int valueChoiceHammer) {
        this.valueChoiceHammer = valueChoiceHammer;
    }

    /**
     * To notify when we receive respond of player
     *
     * @param idPlayer ID session player
     * @param choice choice object with respond of player
     *
     */
    @Override
    public void notifyChoice(UUID idPlayer, ChoiceHammer choice) {
        this.handleChoiceHammer(idPlayer,choice);
    }

    private void setNewHammerToPlayer(){
        Statistics.getInstance().addHammerToPlayer(idPlayer);
        this.print("ADD NEW HAMMER TO "+idPlayer);
        this.getContext().getGame().addHammerPlayer(this.idPlayer);
    }

    private void effectHammer(){
        int currentIndexHammer = this.getContext().getGame().getCurrentIdHammerPlayer(idPlayer);
        share.cards.model.Hammer h = this.getContext().getGame().getPlayer(idPlayer).getInventory().getHammer(currentIndexHammer);
        if(h==null) {
            this.print("PLAYER "+idPlayer+" HAVE NO HAMMER ");
            this.getManager().addRessourcePlayerAfterHammerCommand(idPlayer,TypeRessource.GOLD,this.valueChoiceHammer);
        }
        else if(!h.isFinish()) { // si le hammer n'est pas fini alors demande au client
            this.getContext().getStats().incNbActionCardHammer(idPlayer);
            this.print(" PLAYER "+idPlayer+" WANT ADD GOLD TO HAMMER ? ");
            ChoiceHammer ch = new ChoiceHammer();
            ch.setHammersPlayer(this.getContext().getGame().getPlayer(this.idPlayer).getInventory().getHammer(currentIndexHammer));
            ch.setValueGold(this.valueChoiceHammer);
            this.getContext().sendEventToClient(this.idPlayer, Events.CHOICE_HAMMER,ch);
            this.waitingChoice();
            this.choice.setValueGold(this.valueChoiceHammer);
            Statistics.getInstance().addGoldToHammer(idPlayer, this.choice.getValueToHammer());
            handleChoice(idPlayer);
        }
    }

    private void handleChoiceHammer(UUID idPlayer, ChoiceHammer ch) {
        this.choice = ch;
        this.getContext().getStats().incNbTimeReflexionPlayers(this.idPlayer,this, System.currentTimeMillis()-this.getStartTime());
        this.notifyDecision();
    }

    private void handleChoice(UUID idPlayer) {
        this.getContext().getGame().getCurrentHammerPlayer(idPlayer);
        if(this.getContext().getGame().ifPlayerHaveHammer(idPlayer)){ // si le player a un hammer
            if(this.choice.getValueToHammer() == 0) playerDontWantUseHammer();
            else playerWantUseHammer();
        }
    }

    private void playerWantUseHammer() {
        share.cards.model.Hammer h = this.getContext().getGame().getCurrentHammerPlayer(idPlayer);
        int currentIndexHammer = this.getContext().getGame().getCurrentIdHammerPlayer(idPlayer);
        int stepHammer = h.getNbStep();
        int extra = h.addGold(this.choice.getValueToHammer());
        this.print("Add GOLD hammer : "+this.choice.getValueToHammer()+" il reste "+extra);
        this.getContext().getStats().incNbGoldAddHammerPlayer(idPlayer,this.choice.getValueToHammer());
        int diff = this.valueChoiceHammer - this.choice.getValueToHammer() + extra; // calcule la diff entre le choix hammer et le gain normal de gold
        if (diff > 0) {
         //   if(type == TypeRessource.GOLD) this.getContext().stats.incNbGoldExtendMaxHammer(idPlayer,value);
            this.print("GOLD MORE THAN MAX HAMMER ADD RESTE TO PLAYER");
            this.getManager().addRessourcePlayerAfterHammerCommand(idPlayer,TypeRessource.GOLD,diff); // ajoute la difference au joueur
        }
        int stepHammerAfter = this.getContext().getGame().getPlayer(idPlayer).getInventory().getHammer(0).getNbStep(); // step after exec
        //si le hammer a changer de step on exec l'ajout de glory

        if (stepHammerAfter == stepHammer) return;

        this.print("HAMMER MAX GOLD HIT STEP : "+stepHammerAfter);

        if(stepHammerAfter == 1) {
            this.print("Add GLORY finish hammer : 10 step 1 to "+idPlayer);
            this.getContext().getStats().incNbbRessourceWinGloryHammerPlayer(idPlayer,10);
            this.addRessourcePlayer(idPlayer,TypeRessource.GLORY,10);
        }
        else if(stepHammerAfter == 2) {
            this.print("Add GLORY finish hammer : 15 step 2 to "+idPlayer);
            this.getContext().getStats().incNbbRessourceWinGloryHammerPlayer(idPlayer,15);
            this.addRessourcePlayer(idPlayer,TypeRessource.GLORY,15);
            this.getContext().getGame().setCurrentIdHammerPlayer(idPlayer,currentIndexHammer+1);
        }

    }

    private void playerDontWantUseHammer() {
        this.print("PLAYER DONT WANT ADD GOLD HAMMER");
        this.getManager().addRessourcePlayerAfterHammerCommand(idPlayer,TypeRessource.GOLD,this.choice.getValueGold());
    }

}
