package server.game;

import share.eventclientserver.Events;

import java.util.UUID;

/**
 * The interface Notify event.
 */
public interface INotifyEvent {

    /**
     * To notify when a player is added in game
     */
    void notifyPlayerAddGame();

    /**
     * to send data at one client
     *
     * @param id id socket client
     * @param ev event object
     * @param o object to serialize
     */
    void sendEventToClient(UUID id, Events ev, Object o);

}
