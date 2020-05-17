package server.command.inter;

import share.choice.Choice;

import java.util.UUID;

public interface ICommandManager {

    /**
     * Trigger action to add glory card to inventory player on end game.
     */
    void triggerCommandGloryCardEndGame();

    /**
     * To notify last command reply of player
     * @param idPlayer id session player
     * @param choice choice object
     * @param <T> generic type of choice
     */
    <T extends Choice> void notifyChoicePlayerLastCommand(UUID idPlayer, T choice);


}
