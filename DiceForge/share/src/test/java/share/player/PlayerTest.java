package share.player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import share.inventory.Inventory;
import share.ressource.TypeRessource;

import java.util.UUID;

class PlayerTest {
    static Player player;
    static UUID id;
    static Inventory inventory;

    @BeforeEach
    void initTest() {
        id = UUID.randomUUID();
        player = new Player(id,"");
        inventory = new Inventory();
        inventory.addRessource(TypeRessource.GLORY,5);
        inventory.addRessource(TypeRessource.GOLD,2);
    }

    @Test
    void getId() {
        Assertions.assertEquals(id,player.getId());
    }


    @Test
    void getInventory(){
        player.getInventory().addRessource(TypeRessource.GOLD,2);
        player.getInventory().addRessource(TypeRessource.GLORY,5);
        Assertions.assertEquals(inventory.getValueRessource(TypeRessource.GOLD),player.getInventory().getValueRessource(TypeRessource.GOLD));
        Assertions.assertEquals(inventory.getValueRessource(TypeRessource.GLORY),player.getInventory().getValueRessource(TypeRessource.GLORY));
    }
}