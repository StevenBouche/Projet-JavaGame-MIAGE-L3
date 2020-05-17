package server.game;

import share.game.GameState;
import share.player.Player;

import java.util.List;

public interface IHandlerGame {

    /**
     * to notify when game has finish
     */
    void gameFinish();
    /**
     * to start game
     */
    void startGame();
    /**
     * to stop game
     */
    void stopGame();
    /**
     * to reset game
     */
    void resetGame();

    /**
     * to get player sort by glory
     *
     * @return list player sort by glory
     */
    List<Player> getListResultGame();

    /**
     * get nb current player
     *
     * @return nb current player in game
     */
    int getNbPlayer();
    /**
     * get max nb player
     *
     * @return nb max player in game
     */
    int getNbPlayerMax();
    /**
     * get state of game
     *
     * @return game state object
     */
    GameState getGameState();
}
