package share.inventory;

import share.ressource.TypeRessource;


/**
 * The interface Inventory infos.
 */
public interface IInventoryInfos{

    /**
     * Gets value share.ressource.
     *
     * @param type the type
     * @return the value share.ressource
     */
    int getValueRessource(TypeRessource type);

    /**
     * Gets value max share.ressource.
     *
     * @param type the type
     * @return the value max share.ressource
     */
    int getValueMaxRessource(TypeRessource type);

}
