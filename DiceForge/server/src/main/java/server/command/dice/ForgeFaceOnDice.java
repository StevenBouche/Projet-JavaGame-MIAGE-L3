package server.command.dice;

import server.command.CommandGameManager;
import server.command.CommandManager;
import server.game.IGameManager;
import server.statistics.Statistics;
import server.game.GameManager;
import share.face.*;

import java.util.UUID;

/**
 * The type Forge face on dice.
 */
public class ForgeFaceOnDice extends CommandGameManager {

    final UUID idPlayer;
    final Face f;
    final int indexDice;
    final int indexFace;

    /**
     * Instantiates a new Forge face on dice.
     *
     * @param context  gameManager to access data of game
     * @param manager  commandManager to trigger other command
     * @param idPlayer  the id player
     * @param f         the f
     * @param indexDice the index dice
     * @param indexFace the index face
     */
    public ForgeFaceOnDice(IGameManager context, CommandManager manager, UUID idPlayer, Face f, int indexDice, int indexFace) {
        super(context, manager, "FORGE_FACE_ON_DICE");
        this.idPlayer = idPlayer;
        this.f = f;
        this.indexDice = indexDice;
        this.indexFace = indexFace;
    }

    /**
     * Execute action forge face in slot of dice player. Store removed face in history player.
     */
    @Override
    public void onExecute() {
        this.getContext().getStats().incNbBuyFacePlayers(idPlayer,f);
        this.getContext().getStats().incNbForgeFace(idPlayer);
        Face f2 = this.getContext().getGame().addFaceOfDicePlayer(idPlayer, indexDice, indexFace, f);
        this.print("REMOVED FACE : " + f2 + ", ADDED: " + f +" ON DICE "+indexDice);
        Statistics.getInstance().addForgeChoice(idPlayer, f, f2);
        this.getContext().getGame().addHistoryFacePlayer(idPlayer, f2); //ajoute la face a l'historique de face du joueur
    }

}
