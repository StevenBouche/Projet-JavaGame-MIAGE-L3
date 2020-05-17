package server.command.inter;

import share.choice.Choice;

import java.util.UUID;

public interface ICommandChoice<K extends Choice> {

    /**
     * Reset choice when more than choice.
     */
    void resetChoice();

    /**
     * Notify when player has reply her choice.
     * @param idPlayer id session player
     * @param choice object choice
     */
    void notifyChoice(UUID idPlayer, K choice);

    /**
     * To wait choice player.
     */
    void waitingChoice();

}
