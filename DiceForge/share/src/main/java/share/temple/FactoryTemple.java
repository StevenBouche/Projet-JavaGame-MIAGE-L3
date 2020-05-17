package share.temple;

import share.exeption.InstanceCardOutOfBoundException;

import java.util.EnumMap;
import java.util.HashMap;

/**
 * The type Factory temple.
 */
public class FactoryTemple {


    /**
     * Create island hash map.
     *
     * @param ex the ex
     * @return the hash map
     * @throws InstanceCardOutOfBoundException the instance card out of bound exception
     */
    public HashMap<Integer,Island> createIsland(Extension ex) throws InstanceCardOutOfBoundException {

        HashMap<Integer,Island> island = new HashMap<>();
        for(IslandEnum i : IslandEnum.values()){
            island.put(i.getId(),FactoryIsland.createIsland(i,ex));
        }

        return island;

    }

    /**
     * Create island with enum enum map.
     *
     * @param ex the ex
     * @return the enum map
     * @throws InstanceCardOutOfBoundException the instance card out of bound exception
     */
    public EnumMap<IslandEnum,Island> createIslandWithEnum(Extension ex) throws InstanceCardOutOfBoundException {
        EnumMap<IslandEnum,Island> island = new EnumMap<IslandEnum, Island>(IslandEnum.class);
        for(IslandEnum i : IslandEnum.values()){
            island.put(i,FactoryIsland.createIsland(i,ex));
        }
        return island;
    }


}


