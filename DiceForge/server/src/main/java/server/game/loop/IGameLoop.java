package server.game.loop;

import share.game.Game;

public interface IGameLoop {
    /**
     * execute loop of game
     *
     * @param g game object
     */
    void execute(Game g);
}
