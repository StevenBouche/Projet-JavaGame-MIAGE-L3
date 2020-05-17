package server.command.inter;

public interface ICommandManagerTurn {

    /**
     * trigger roll dice major for all player
     */
    void rollDiceMajorAllPlayerCommand();

    /**
     * trigger set current player turn in game.
     */
    void triggerCommandSetPlayerActif();

    /**
     * Trigger all card reinforcement of current player.
     */
    void triggerCommandCallRenfort();

    /**
     * Trigger action of turn.
     */
    void triggerCommandActionTurn();

    /**
     * Trigger if current player want other action turn.
     */
    void triggerCommandOneMoreTurn();

    /**
     * Trigger action to add gold at start of game.
     */
    void triggerCommandAddGoldPlayerStartGame();

}
