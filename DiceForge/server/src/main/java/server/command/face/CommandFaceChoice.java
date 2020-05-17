package server.command.face;

import server.command.CommandManager;
import server.command.inter.ICommandChoice;
import server.game.IGameManager;
import share.choice.Choice;
import share.ressource.TypeRessource;

import java.util.UUID;

/**
 * The type Command face choice.
 *
 * @param <T> the type parameter
 */
public abstract class CommandFaceChoice<T extends Choice> extends CommandFace implements ICommandChoice<T> {

    /**
     * The Choice.
     */
    public T choice;

    /**
     * Instantiates a new Command.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer the id player
     * @param inverse  the inverse
     * @param name     the name
     */
    public CommandFaceChoice(IGameManager context, CommandManager manager, UUID idPlayer, boolean inverse, String name) {
        super(context, manager, idPlayer, inverse, name);
    }

    /**
     * Instantiates a new Command face choice.
     *
     * @param context the context
     * @param manager the manager
     * @param inverse the inverse
     * @param name    the name
     */
    public CommandFaceChoice(IGameManager context, CommandManager manager, boolean inverse, String name) {
        super(context, manager, inverse, name);
    }

    /**
     * Override to inject data in stat.
     * @param idPlayer id session player
     * @param type type resource
     * @param value value resource
     */
    @Override
    protected void addRessourcePlayer(UUID idPlayer, TypeRessource type, int value){
        super.addRessourcePlayer(idPlayer, type,  value);
    }

    /**
     * Reset current choice.
     */
    @Override
    public void resetChoice() {
        this.choice = null;
    }

    /**
     * Waiting choice player.
     */
    @Override
    public void waitingChoice() {
        while(this.choice == null) this.waitingDecision();
    }

}
