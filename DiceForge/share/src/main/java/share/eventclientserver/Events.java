package share.eventclientserver;

/**
 * The enum Events.
 */
public enum Events {

    /**
     * Choice between ressources events.
     */
    CHOICE_BETWEEN_RESSOURCES ("CHOICE_BETWEEN_RESSOURCES"),
    /**
     * Handle choice forge events.
     */
    HANDLE_CHOICE_FORGE("HANDLE_CHOICE_FORGE"),
    /**
     * Choice one more action events.
     */
    CHOICE_ONE_MORE_ACTION("ONE_MORE_ACTION"),
    /**
     * Choice hammer events.
     */
    CHOICE_HAMMER("HANDLE_CHOICE_HAMMER"),
    /**
     * Choice 3 gold for 4 glory events.
     */
    CHOICE_3GOLD_FOR_4GLORY("COICE_3GOLD_FOR_4GLORY"),
    /**
     * Choice satyre events.
     */
    CHOICE_SATYRE("CHOICE_SATYRE"),
    /**
     * Choice forge special events.
     */
    CHOICE_FORGE_SPECIAL("CHOICE_FORGE_SPECIAL"),
    /**
     * Choice power other player events.
     */
    CHOICE_POWER_OTHER_PLAYER("CHOICE_POWER_OTHER_PLAYER");
    private String id;

    /**
     * Gets server.event id.
     *
     * @return the server.event id
     */
    public String getEventID() {
        return id;
    }

    Events(String eventID) {
        this.id = eventID;
    }

}
