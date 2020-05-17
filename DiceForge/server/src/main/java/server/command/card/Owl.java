package server.command.card;

import server.command.CommandManager;
import server.game.IGameManager;
import server.statistics.Statistics;
import server.game.GameManager;
import share.choice.ChoiceBetweenRessource;
import share.eventclientserver.Events;
import share.ressource.TypeRessource;

import java.util.EnumMap;
import java.util.UUID;

/**
 * The type Owl.
 */
public class Owl extends CommandCardChoice<ChoiceBetweenRessource> {

    private final UUID idPlayer;

    /**
     * Instantiates a new Owl.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Owl(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"OWL_COMMAND");
        this.idPlayer = idPlayer;
    }

    /**
     *
     * Execute action of OwlCard : player need to choice between three resources and add thr resource have choice
     *
     */
    @Override
    public void onExecute() {
        this.print("PLAYER "+idPlayer+" WANT 1 GOLD OR 1 LUNAR OR 1 SOLAR ? ");
        ChoiceBetweenRessource ch = setUpChoice();
        this.getContext().sendEventToClient(idPlayer,Events.CHOICE_BETWEEN_RESSOURCES,ch);
        while(this.choice == null)this.waitingDecision();
        int valueRessource = this.choice.getListRessource().get(this.choice.getTypeRessource()); // choice client ok
        Statistics.getInstance().addChoiceBetweenRessources(idPlayer, this.choice.getTypeRessource(), valueRessource);
        this.print("PLAYER "+idPlayer+" WANT "+valueRessource+" "+this.choice.getTypeRessource());
        this.addRessourcePlayer(idPlayer, this.choice.getTypeRessource(), valueRessource);
    }

    private ChoiceBetweenRessource setUpChoice(){
        EnumMap<TypeRessource, Integer> listRessource = new EnumMap<TypeRessource, Integer>(TypeRessource.class);
        listRessource.put(TypeRessource.GOLD,1);
        listRessource.put(TypeRessource.LUNAR,1);
        listRessource.put(TypeRessource.SOLAR,1);
        return new ChoiceBetweenRessource(listRessource);
    }

    /**
     * To notify when we receive respond of player
     *
     * @param idPlayer ID session player
     * @param choice choice object with respond of player
     *
     */
    @Override
    public void notifyChoice(UUID idPlayer, ChoiceBetweenRessource choice) {
        this.handleChoice(idPlayer, choice);
    }

    private void handleChoice(UUID idPlayer, ChoiceBetweenRessource choice) {
        this.choice = choice;
        this.getContext().getStats().incNbTimeReflexionPlayers(this.idPlayer, this,System.currentTimeMillis()-this.getStartTime());
        this.notifyDecision();
    }
}
