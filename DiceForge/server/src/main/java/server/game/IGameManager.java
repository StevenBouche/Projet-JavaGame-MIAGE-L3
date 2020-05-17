package server.game;

import server.statistics.StatisticsInGame;
import share.choice.Choice;
import share.eventclientserver.Events;
import share.game.Game;
import share.player.Player;
import share.utils.Printer;

import java.util.UUID;

import static share.game.GameState.START;
import static share.game.GameState.WAITING_PLAYER;

public interface IGameManager extends IHandlerGame {

    /**
     * To get game object
     *
     * @return game object
     */
    Game getGame();

    /**
     * To send data player
     */
    void sendEventToClient(UUID id, Events ev, Object o);

    /**
     * get stats of game
     *
     * @return statistics in game object
     */
    StatisticsInGame getStats();

    /**
     * to notify game than player have reply
     *
     * @param id id session player
     * @param choice choice object
     */
    void notifyChoicePlayer(UUID id, Choice choice);;

    /**
     * to add player in game
     *
     * @param id id session player
     * @param str version name
     * @param idP id of client
     */
    void notifyConnectionPlayer(UUID id, String str,int idP);

    /**
     * to remove player of game
     *
     * @param id id session player
     */
    void notifyDeconnectionPlayer(UUID id);



}
