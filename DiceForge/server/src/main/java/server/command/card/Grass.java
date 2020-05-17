package server.command.card;

import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.ressource.TypeRessource;

import java.util.UUID;

/**
 * The type Grass.
 */
public class Grass extends CommandCard {
    private final UUID idPlayer;

    /**
     * Instantiates a new Grass.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer id player of source action
     */
    public Grass(IGameManager context, CommandManager manager, UUID idPlayer) {
        super(context, manager,"GRASS_COMMAND");
        this.idPlayer = idPlayer;
    }

    /**
     * Execute action of GrassCard : add 3 moon and 3 gold to a player
     */
    @Override
    public void onExecute() { //+3moon 3gold
        this.print("ADD 3 MOON AND 3 GOLD TO PLAYER "+idPlayer);
        this.addRessourcePlayer(this.idPlayer, TypeRessource.LUNAR, 3);
        this.addRessourcePlayer(this.idPlayer, TypeRessource.GOLD, 3);
    }
}
