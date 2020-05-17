package mainlauncher.launcher;

/**
 * The enum State launcher.
 */
public enum StateLauncher {

    /**
     * Not running state launcher.
     */
    NOT_RUNNING,
    /**
     * Starting server state launcher.
     */
    STARTING_SERVER,
    /**
     * Server started state launcher.
     */
    SERVER_STARTED,
    /**
     * Starting clients state launcher.
     */
    STARTING_CLIENTS,
    /**
     * Clients started state launcher.
     */
    CLIENTS_STARTED,
    /**
     * Ongoing game state launcher.
     */
    ONGOING_GAME,
    /**
     * Disconnection clients state launcher.
     */
    DISCONNECTION_CLIENTS,
    /**
     * Waiting shutdown server state launcher.
     */
    WAITING_SHUTDOWN_SERVER,
    /**
     * Shutdown state launcher.
     */
    SHUTDOWN

}
