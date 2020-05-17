package server.command.turn;

import server.command.CommandGameManager;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.player.Player;

import java.util.UUID;

public abstract class CommandActionTurn extends CommandGameManager {

    protected UUID idPlayer;

    /**
     * Instantiates a new Command.
     *
     * @param context the context
     * @param manager the manager
     * @param name    the name
     */
    public CommandActionTurn(IGameManager context, CommandManager manager, String name) {
        super(context, manager, name);
        this.idPlayer = this.getContext().getGame().currentPlayer.getId();
    }

    /**
     * To get current player in actual turn.
     * @return id session of current player
     */
    public Player getCurrentPlayer(){
        return this.getContext().getGame().currentPlayer;
    }

}
