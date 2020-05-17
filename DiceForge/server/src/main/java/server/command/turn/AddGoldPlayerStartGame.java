package server.command.turn;

import server.command.CommandGameManager;
import server.command.CommandManager;
import server.game.IGameManager;
import share.game.Game;
import share.ressource.TypeRessource;
import share.utils.Printer;

import java.util.Collections;
import java.util.UUID;

public class AddGoldPlayerStartGame extends CommandGameManager {
    /**
     * Instantiates a new Command.
     *
     * @param context the context
     * @param manager the manager
     *
     */
    public AddGoldPlayerStartGame(IGameManager context, CommandManager manager) {
        super(context, manager, "ADD_GOLD_PLAYER_START_GAME");
    }

    /**
     * Execute add gold start game. Shuffle list player, introduce RNG.
     */
    @Override
    public void onExecute() {
        Game g = getContext().getGame();
        Collections.shuffle(g.getIdPlayers()); // melange les joueurs a chaque debut de game pour ne pas toujours avantager le meme
        int nb = g.getIdPlayers().size();
        int i = 1;
        for(UUID id : g.getIdPlayers()) {
            Printer.getInstance().logGame("Player "+i+" : Add Gold "+nb+" to "+id);
            this.getManager().addRessourcePlayerCommand(id, TypeRessource.GOLD,nb);
            this.getContext().getStats().incNbRessourceGoldWinStartGame(id,TypeRessource.GOLD,nb);
            nb--;
            i++;
        }
    }
}
