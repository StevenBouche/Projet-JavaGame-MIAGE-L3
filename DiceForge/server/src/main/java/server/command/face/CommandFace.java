package server.command.face;

import server.command.CommandGameManager;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.face.Face;
import share.ressource.TypeRessource;

import java.util.*;

/**
 * The type Command face.
 */
public abstract class CommandFace extends CommandGameManager {

    /**
     * The Inverse.
     */
    public boolean inverse;
    /**
     * The Id player.
     */
    protected UUID idPlayer;

    /**
     * Instantiates a new Command.
     *
     * @param context  the context
     * @param manager  the manager
     * @param idPlayer the id player
     * @param inverse  the inverse
     * @param name     the name
     */
    public CommandFace(IGameManager context, CommandManager manager, UUID idPlayer, boolean inverse, String name) {
        super(context, manager, name);
        this.inverse = inverse;
        this.idPlayer = idPlayer;
    }

    /**
     * Instantiates a new Command face.
     *
     * @param context the context
     * @param manager the manager
     * @param inverse the inverse
     * @param name    the name
     */
    public CommandFace(IGameManager context, CommandManager manager, boolean inverse, String name) {
        super(context, manager, name);
        this.inverse = inverse;
    }

    /**
     * Override to inject data on stats
     * @param idPlayer id session player
     * @param type  type resource
     * @param value value resource
     */
    @Override
    protected void addRessourcePlayer(UUID idPlayer, TypeRessource type, int value){
        if(inverse) super.removeRessourcePlayer(idPlayer,type,value);
        else super.addRessourcePlayer(idPlayer, type,  value);
    }

    /**
     * Clone map list face map.
     *
     * @param <T> the type parameter
     * @param map the map
     * @return the map
     */
    public <T extends Face> Map<UUID, List<T>> cloneMapListFace(Map<UUID, List<T>> map){
        Map<UUID, List<T>> clone = new HashMap<>();
        for(UUID id : map.keySet()){
            clone.put(id,new ArrayList<>(map.get(id)));
        }
        return clone;
    }

}
