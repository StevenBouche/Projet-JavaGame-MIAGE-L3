package server.command.rolldice;

import server.command.CommandGameManager;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;

/**
 * The type Command roll dice.
 */
public abstract class CommandRollDice extends CommandGameManager {
    /**
     * Instantiates a new Command.
     *
     * @param context the context
     * @param manager the manager
     * @param name    the name
     */
    public CommandRollDice(IGameManager context, CommandManager manager, String name) {
        super(context, manager, name);
    }
}
