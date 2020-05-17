package share.core;

import share.ressource.TypeRessource;

import java.util.UUID;

/**
 * The interface Handler share.face.
 */
public interface IHandlerFace {

    /**
     * Add share.ressource.
     *
     * @param idPlayer      the id share.player
     * @param typeRessource the type share.ressource
     * @param value         the value
     */
    void addRessource(UUID idPlayer, TypeRessource typeRessource, int value);

}
