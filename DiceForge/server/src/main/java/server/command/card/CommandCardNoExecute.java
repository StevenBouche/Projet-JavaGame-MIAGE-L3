package server.command.card;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;

class CardWithNoExecuteException extends Exception{
    public CardWithNoExecuteException(String str){
        super(str);
    }
}
public abstract class CommandCardNoExecute extends CommandCard {
    /**
     * Instantiates a new Command.
     *
     * @param context the context
     * @param manager the manager
     * @param name    the name
     */
    public CommandCardNoExecute(IGameManager context, CommandManager manager, String name) {
        super(context, manager, name);
    }

    @Override
    public void onExecute(){
        try {
            throw new CardWithNoExecuteException("Card have not execute, but has trigger");
        } catch (CardWithNoExecuteException e) {
            e.printStackTrace();
        }
    }
}
