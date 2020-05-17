package server.game.loop;

import server.command.CommandManager;
import server.command.inter.ICommandManagerTurn;
import server.game.GameManager;
import server.game.IHandlerGame;
import server.statistics.StatisticsInGame;
import share.game.Game;
import share.game.GameState;
import share.ressource.TypeRessource;
import share.utils.Printer;

import java.util.*;

/**
 * The type Game loop.
 */
public class GameLoop implements  IGameLoop {

    private final IHandlerGame gameManager;
    private final ICommandManagerTurn commandManager;
    private StatisticsInGame stats;
    public GameStepLoopEnum currentStep;
    boolean firstExec;

    /**
     * Instantiates a new Game loop.
     *
     * @param gameManager    handler of game
     * @param commandManager the command manager
     */
    public GameLoop(IHandlerGame gameManager, ICommandManagerTurn commandManager, StatisticsInGame stats){
        this.commandManager = commandManager;
        this.gameManager = gameManager;
        this.stats = stats;
        this.firstExec = true;
        this.setAndPrintStepGame(GameStepLoopEnum.NOT_START);
    }

    /**
     * Execute loop of game.
     *
     * @param game Game object
     */
    @Override
    public void execute(Game game) {
        // Si la game est dans l'etat stop on arrete l'execution
        if(game.state != GameState.START || Thread.currentThread().isInterrupted()) this.gameManager.stopGame(); //todo revoir sortie game
        else {
            if(firstExec) this.addGoldPlayerStartTurn(game); // distribue les golds de debut de partie
            // si le nombre de tour jouer est superieur au nombre de joueur
            if(game.cptNbPlay > game.getPlayers().size()) this.endTurn(game);
            else this.executeTurn(game);
            // si le nombre de manche est inferieur au nombre de manche max
            if (game.nbManche <= game.nbMancheMax) this.execute(game);
        }

    }

    /**
     * Call all command we need trigger in one turn of game.
     * Use command manager turn to trigger action of game.
     *
     * @param game Game object
     */
    private void executeTurn(Game game){

        // todo plus besoin de step
        Printer.getInstance().logNewTurnGame(game.nbManche,game.cptNbPlay);
        this.setAndPrintStepGame(GameStepLoopEnum.ROLL_DICE);
        this.commandManager.rollDiceMajorAllPlayerCommand();
        this.setAndPrintStepGame(GameStepLoopEnum.SET_PLAYER_ACTIF);
        this.commandManager.triggerCommandSetPlayerActif();
        this.setAndPrintStepGame(GameStepLoopEnum.CALL_RENFORT);
        this.commandManager.triggerCommandCallRenfort();
        this.setAndPrintStepGame(GameStepLoopEnum.FORGE_OR_EXPLOIT_OR_NOTHING);
        this.commandManager.triggerCommandActionTurn();
        this.setAndPrintStepGame(GameStepLoopEnum.ONE_MORE_TURN);
        this.commandManager.triggerCommandOneMoreTurn();
        Printer.getInstance().printInventoryPlayers(game);
        Printer.getInstance().logEndTurnGame(game.nbManche,game.cptNbPlay);
        this.setAndPrintStepGame(GameStepLoopEnum.END_TURN);
        game.cptNbPlay++; // INC LE NOMBRE DE PLAY
        stats.incNbTurn();
    }

    /**
     * Use to change data when turn id finished.
     *
     * @param game Game object
     */
    private void endTurn(Game game){
        stats.incNbManche();
        game.nbManche++;
        game.cptNbPlay = 1;
    }

    /**
     * Trigger action add gold start game.
     *
     * @param game Game object
     */
    private void addGoldPlayerStartTurn(Game game){
        this.setAndPrintStepGame(GameStepLoopEnum.ADD_GOLD_PLAYER_START_TURN);
        this.commandManager.triggerCommandAddGoldPlayerStartGame();
        this.firstExec = false;
    }

    private void setAndPrintStepGame(GameStepLoopEnum step){
        this.currentStep = step;
        Printer.getInstance().logGameLoop("TURN STEP : "+step.toString());
    }

}
