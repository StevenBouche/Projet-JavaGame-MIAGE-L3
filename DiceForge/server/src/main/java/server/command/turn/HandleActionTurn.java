package server.command.turn;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.cards.Card;
import share.choice.ChoiceFaceOnDice;
import share.choice.ChoiceNothingForgeExploit;
import share.choice.EnumTypeChoice;
import share.dice.Dice;
import share.eventclientserver.Events;
import share.exeption.CaseOfPoolForgeOutOfBound;
import share.face.Face;
import share.forge.Pool;
import share.inventory.InventoryElement;
import share.ressource.TypeRessource;
import server.statistics.Statistics;
import share.utils.Printer;

import java.util.Map;
import java.util.UUID;

/**
 * The type Handle action turn.
 */
public class HandleActionTurn extends CommandActionTurnChoice<ChoiceNothingForgeExploit> {

    /**
     * Instantiates a new Handle action turn.
     *
     * @param context the context
     * @param manager the manager
     */
    public HandleActionTurn(IGameManager context, CommandManager manager) {
        super(context, manager,"HANDLE_ACTION_TURN_COMMAND");
    }

    /**
     * Execute action of turn. Action is if player want forge faces or if he want buy card.
     */
    @Override
    public void onExecute() {
        ChoiceNothingForgeExploit ch = this.setUpChoice();
        this.print("PLAYER "+idPlayer+" WANT FORGE / EXPLOIT / NOTHING ?");
        this.getContext().sendEventToClient(idPlayer, Events.HANDLE_CHOICE_FORGE, ch);
        this.waitingChoice();
        // redirect in function of choice client
        if (this.choice.choice == EnumTypeChoice.FORGE) this.execChoiceForge(idPlayer,this.choice);
        else if(this.choice.choice == EnumTypeChoice.EXPLOIT) this.execChoiceExploit(idPlayer, this.choice);
    }

    /**
     * Setup choice object.
     *
     * @return ChoiceNothingForgeExploit object
     */
    private ChoiceNothingForgeExploit setUpChoice() {
        Map<Integer, Pool> pools =  this.getContext().getGame().getPoolsForge(); // recup les pools de la force
        Dice d1 = this.getContext().getGame().getDiceOfCurrentPlayer(0);
        Dice d2 = this.getContext().getGame().getDiceOfCurrentPlayer(1);
        Map<TypeRessource, InventoryElement> listRessources = this.getContext().getGame().getRessourcesOfCurrentPlayer();
        return new ChoiceNothingForgeExploit(d1,d2,pools,listRessources,this.getContext().getGame().getIslandsOfTemple()); //build l'objet de choix
    }

    /**
     * Execute action player want buy card.
     * @param idPlayer id session current player
     * @param choice choice object
     */
    private void execChoiceExploit(UUID idPlayer, ChoiceNothingForgeExploit choice) {
        this.getContext().getStats().incNbActionExploit(idPlayer);
        Card c = this.getContext().getGame().getCardFromIsland(choice.choiceIsland,choice.choiceIndexIslandCase); // get card of choice client
        if(!cantBuyCard(c,idPlayer)){ //if player can buy card
            c = this.getContext().getGame().pullCardFromIsland(choice.choiceIsland,choice.choiceIndexIslandCase); // extract card of island
            this.print("PLAYER WANT BUY CARD : "+c);
            Statistics.getInstance().addExploitChoice(idPlayer, c);
            for(TypeRessource type : c.getCost().keySet()) this.removeRessourcePlayer(idPlayer,type,c.getCost().get(type)); // remove toutes les ressources d'achat de la carte
            this.print("ADD CARD : "+c+" TO INVENTORY PLAYER "+idPlayer);
            this.getContext().getGame().addCardPlayer(c,idPlayer); // add card inventory player
            this.getManager().handleActionCardWhenBuy(idPlayer,c); // trigger execute when card was buy
        } else try {
            throw new Exception("Player cant buy card "+c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute action player want buys faces.
     * @param id id session socket
     * @param ch choice object
     */
    private void execChoiceForge(UUID id, ChoiceNothingForgeExploit ch){
        this.getContext().getStats().incNbActionForge(id);
        for(ChoiceFaceOnDice choice : ch.listChoiceForge){ // for all choice face
            try {
                this.handleChoiceForge(id,choice); // handle buy face
            } catch (CaseOfPoolForgeOutOfBound caseOfPoolForgeOutOfBound) {
                caseOfPoolForgeOutOfBound.printStackTrace();
            }
        }
    }

    /**
     * Handle choice one face of player
     *
     * @param idPlayer id session player
     * @param choice choice object
     * @throws CaseOfPoolForgeOutOfBound when index out of bound pool elements
     */
    private void handleChoiceForge(UUID idPlayer, ChoiceFaceOnDice choice) throws CaseOfPoolForgeOutOfBound {
        // enleve la share.face de la pool choisi
        Face f = this.getContext().getGame().getFaceWithRemoveFromPool(choice.choicePool,choice.choiceIndexPool);
        if(!cantBuyFace(choice.choicePool,idPlayer)){ // if player can buy face
            Printer.getInstance().printCommand(this.getName(),"PLAYER BUY FACE : "+f,this.getStack());
            this.getManager().triggerCommandForgeFaceOnDice(idPlayer, f, choice.choiceIndexDice,choice.choiceIndexFaceDice);
            int cost = this.getContext().getGame().getCostOfPoolForge(choice.choicePool); // todo test 0
            this.removeRessourcePlayer(idPlayer, TypeRessource.GOLD,cost); // remove les ressources du joueur
        } else {
            try {
                throw new Exception("Player cant buy face "+f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /***
     * Determine if player can buy card
     *
     * @param c card object
     * @param p id session player
     * @return true if he cant buy, else false
     */
    private boolean cantBuyCard(Card c, UUID p){
        boolean b = false;
        for(TypeRessource type : c.cost.keySet()) if(c.cost.get(type) > getContext().getGame().getPlayer(p).getInventory().getRessources().get(type).value) b= true;
        return b;
    }

    /***
     * Determine if player can buy face
     *
     * @param choicePool index of pool
     * @param p id session player
     * @return true if he cant buy, else false
     */
    private boolean cantBuyFace(int choicePool, UUID p){
        return this.getContext().getGame().getPoolsForge().get(choicePool).getCost() > getContext().getGame().getPlayer(p).getInventory().getRessources().get(TypeRessource.GOLD).value;
    }

    /**
     * Notify when player have reply
     *
     * @param idPlayer id session player
     * @param choice choice object
     */
    @Override
    public void notifyChoice(UUID idPlayer, ChoiceNothingForgeExploit choice) {
       this.handleChoiceActionTurn(idPlayer, choice);
    }

    /**
     * Trigger when player has reply. Set choice object and signal condition have received data.
     * @param idPlayer id session player
     * @param choice choice object
     */
    private void handleChoiceActionTurn(UUID idPlayer, ChoiceNothingForgeExploit choice){
        this.print("PLAYER "+idPlayer+" WANT .....");
        this.choice = choice; // exec quand le client a fais un choix
        this.getContext().getStats().incNbTimeReflexionPlayers(idPlayer, this,System.currentTimeMillis()-this.getStartTime());
        this.notifyDecision(); // notifie pour la poursuite du traitement
    }

}
