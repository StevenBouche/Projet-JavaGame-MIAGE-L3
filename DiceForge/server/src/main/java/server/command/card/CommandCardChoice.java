package server.command.card;

import server.command.CommandManager;
import server.command.inter.ICommandChoice;
import server.game.IGameManager;
import share.choice.Choice;
import share.ressource.TypeRessource;

import java.util.UUID;

public abstract class CommandCardChoice<T extends Choice> extends CommandCard implements ICommandChoice<T> {

    public T choice;

    /**
     * Instantiates a new Command.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param name    the name of this command
     */
    public CommandCardChoice(IGameManager context, CommandManager manager, String name) {
        super(context, manager, name);
    }

    @Override
    protected void addRessourcePlayer(UUID idPlayer, TypeRessource type, int value){
        super.addRessourcePlayer(idPlayer, type,  value);
    }

    @Override
    protected void removeRessourcePlayer(UUID idPlayer, TypeRessource type, int value){
        super.removeRessourcePlayer(idPlayer, type,  value);
    }

    @Override
    public void resetChoice() {
        this.choice = null;
    }

    @Override
    public void waitingChoice() {
        while(this.choice == null) this.waitingDecision();
    }

}
