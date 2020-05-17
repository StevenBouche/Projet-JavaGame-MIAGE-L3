package server.game;

import server.command.CommandManager;
import server.command.FactoryCommandManager;
import server.command.inter.ICommandManager;
import server.command.inter.ICommandManagerTurn;
import server.statistics.StatisticsInGame;
import server.game.loop.GameLoop;
import server.game.loop.GameStepLoopEnum;
import share.choice.*;
import share.eventclientserver.Events;
import share.game.Game;
import share.game.GameState;
import share.player.Player;
import share.ressource.TypeRessource;
import share.utils.Printer;

import java.util.*;

import static share.game.GameState.*;


/**
 * The type Game manager.
 */
public class GameManager implements IGameManager {

    private Game game;
    private final int playerMax;
    private GameLoop gameLoop;
    private final INotifyEvent notifyEventManager;
    public StatisticsInGame stats;
    /**
     * The Command manager.
     */
    public final ICommandManager commandManager;

    /**
     * Instantiates a new Game manager.
     *
     * @param notifyEventManager the notify event manager
     */
    public GameManager(INotifyEvent notifyEventManager){
        Printer.getInstance().logGame("Init GameManager");
        this.stats = new StatisticsInGame();
        this.commandManager = FactoryCommandManager.createCommandManager(this);
        this.notifyEventManager = notifyEventManager;
        this.playerMax = 4;
        this.gameInit();
    }

    /**
     * Game initialization.
     */
    private void gameInit(){
        this.game = new Game();
        this.gameLoop = new GameLoop(this, (ICommandManagerTurn) commandManager,this.stats);
        this.gameWaitingPlayer();
    }

    /**
     * game waiting player.
     */
    private void gameWaitingPlayer(){
        this.setAndPrintStateGame(WAITING_PLAYER);
    }

    /**
     * To reset game object.
     */
    @Override
    public void resetGame() {
        this.game.reset();
        this.gameLoop = new GameLoop(this, (ICommandManagerTurn) commandManager,this.stats);
    }

    /**
     * To start game and execute loop.
     */
    @Override
    public void startGame() {
        try {
            this.setAndPrintStateGame(START);
            this.gameLoop.execute(this.game);
            stats.incNbGame();
            this.gameFinish(); // notify handler than game have finished
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * No notify when game have finished.
     */
    @Override
    public void gameFinish() {
        this.commandManager.triggerCommandGloryCardEndGame(); // add all
        this.setAndPrintStateGame(FINISH);
        Printer.getInstance().printInventoryPlayers(this.game);
        this.stats.addRankOfOneGame(this.getListResultGame());
        Printer.getInstance().printRanking(this.getListResultGame());
    }

    /**
     * Set state game at STOP.
     */
    @Override
    public void stopGame() {
        this.setAndPrintStateGame(STOP);
    }

    private void setAndPrintStateGame(GameState state){
        this.game.state = state;
        Printer.getInstance().logGameState(state);
    }

    /**
     * Get max player game.
     *
     * @return nb max player in game
     */
    @Override
    public int getNbPlayerMax() {
        return this.playerMax;
    }

    /**
     * Get game game.
     *
     * @return the game object
     */
    @Override
    public Game getGame(){
        return this.game;
    }

    /**
     * Get current number player.
     *
     * @return nb current player
     */
    @Override
    public int getNbPlayer(){
        return this.game.getPlayers().size();
    }

    @Override
    public GameState getGameState() {
        if(this.game == null) return null;
        return this.game.state;
    }

    /**
     * Get list result game list.
     *
     * @return the list
     */
    @Override
    public List<Player> getListResultGame(){
        return this.game.getPlayerSortByTypeRessource(TypeRessource.GLORY);
    }

    /**
     * Notify choice player.
     *
     * @param id     the id
     * @param choice the choice
     */
    @Override
    public void notifyChoicePlayer(UUID id, Choice choice){
        this.commandManager.notifyChoicePlayerLastCommand(id,choice);
    }

    /**
     * to notify player connection
     *
     * @param id id session player
     * @param str version name
     * @param idP id of client
     */
    @Override
    public void notifyConnectionPlayer(UUID id, String str,int idP){
        if(this.game.state == WAITING_PLAYER) {
            this.stats.addPlayer(id,str,idP);
            Player p = new Player(id,str,idP);
            this.game.addPlayer(p, id);
            Printer.getInstance().logGame("Player "+id+" have join the game");
        }
        this.notifyEventManager.notifyPlayerAddGame();
    }

    /**
     * to notify player disconnect
     * @param id id session player
     */
    @Override
    public void notifyDeconnectionPlayer(UUID id){
        Printer.getInstance().logGame("Player "+id+" disconnected");
        this.game.removePlayer(id);
        if(this.game.getPlayer(id) != null && this.getGameState() == START) this.stopGame();
    }

    /**
     * Send event to client.
     *
     * @param id the id
     * @param ev the ev
     * @param o  the o
     */
    @Override
    public void sendEventToClient(UUID id, Events ev, Object o){
        this.notifyEventManager.sendEventToClient(id,ev,o);
    }

    /**
     * Get stats object.
     *
     * @return StatisticsInGame object
     */
    @Override
    public StatisticsInGame getStats() {
        return this.stats;
    }


}


