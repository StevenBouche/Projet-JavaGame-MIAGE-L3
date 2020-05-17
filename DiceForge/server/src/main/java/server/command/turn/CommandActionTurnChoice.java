package server.command.turn;

import server.command.CommandManager;
import server.command.inter.ICommandChoice;
import server.game.IGameManager;
import share.choice.Choice;
import share.ressource.TypeRessource;

import java.util.UUID;

public abstract class CommandActionTurnChoice<T extends Choice> extends CommandActionTurn implements ICommandChoice<T> {

    public T choice;

    /**
     * Abstract constructor
     *
     * @param context game manager interface
     * @param manager command manager
     * @param name name of this command
     */
    public CommandActionTurnChoice(IGameManager context, CommandManager manager, String name) {
        super(context, manager, name);
    }

    /**
     * Reset Choice.
     */
    @Override
    public void resetChoice() {
        this.choice = null;
    }

    /**
     * Waiting choice. While choice object is equals null.
     */
    @Override
    public void waitingChoice() {
        while(this.choice == null) this.waitingDecision();
    }

    /**
     * Override super method to inject data in stats
     * @param idPlayer id session player
     * @param type type resource
     * @param value value resource
     */
    @Override
    public void removeRessourcePlayer(UUID idPlayer, TypeRessource type, int value){
        super.removeRessourcePlayer(idPlayer,type,value);
        this.getContext().getStats().incNbRessourceLooseWhenBuyPlayer(idPlayer, type, value);
    }

}
