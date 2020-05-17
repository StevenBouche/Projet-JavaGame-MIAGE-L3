package server.game.loop;

/**
 * The enum Game step loop enum.
 */
public enum GameStepLoopEnum {

    /**
     * Not start game step loop enum.
     */
    NOT_START(0),
    /**
     * Add gold player start turn game step loop enum.
     */
    ADD_GOLD_PLAYER_START_TURN(1),
    /**
     * Roll dice game step loop enum.
     */
    ROLL_DICE(2),
    /**
     * Trigger dice game step loop enum.
     */
    TRIGGER_DICE(4),
    /**
     * Set player actif game step loop enum.
     */
    SET_PLAYER_ACTIF(4),
    /**
     * Call renfort game step loop enum.
     */
    CALL_RENFORT(5),
    /**
     * Forge or exploit or nothing game step loop enum.
     */
    FORGE_OR_EXPLOIT_OR_NOTHING(6),
    /**
     * One more turn game step loop enum.
     */
    ONE_MORE_TURN(7),
    /**
     * End turn game step loop enum.
     */
    END_TURN(8);

    /**
     * The Id.
     */
    public final int id;

    GameStepLoopEnum(int next){
        this.id = next;
    }

}
