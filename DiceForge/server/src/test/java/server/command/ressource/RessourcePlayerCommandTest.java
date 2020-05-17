package server.command.ressource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.command.card.Hammer;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.config.ConfigPlayer;
import share.face.FactoryFace;
import share.ressource.TypeRessource;

import java.util.UUID;

import static org.mockito.Matchers.any;

class RessourcePlayerCommandTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;
    RessourcePlayerCommand cmd;

    @BeforeEach
    void initTest(){
        FactoryFace.resetInstance();
        idPlayer = UUID.randomUUID();
        INotifyEvent callback = Mockito.mock(INotifyEvent.class);
        manager = new GameManager(callback);
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "str",1);
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
    }

    private void createCmdRessource(CmdRessource cmdEnum, TypeRessource type, int value){
        cmd = new RessourcePlayerCommand(manager,managerCmd,cmdEnum,idPlayer,type, value);
    }

    @Test
    void onExecute() {
        int maxGold = ConfigPlayer.GOLD_PLAYER_MAX_INIT;
        int maxGlory = ConfigPlayer.GLORY_PLAYER_MAX_INIT;
        executeAddRessourceNotGold(1);
        addRessourceGoldWithNoHammer(1);
        addRessourceGoldWithHammer(1);
        addRessourceGoldAfterHammer(1);
        removeRessource(1);
        testLimitAddRessource();
        testLimitRemoveRessource();
    }

    private void executeAddRessourceNotGold(int i){
        int currentValue = manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GLORY);
        createCmdRessource(CmdRessource.ADD, TypeRessource.GLORY, i);
        cmd.trigger();
        Assertions.assertEquals(currentValue+i,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GLORY));
    }

    private void addRessourceGoldWithNoHammer(int i){
        int currentValue = manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD);
        createCmdRessource(CmdRessource.ADD, TypeRessource.GOLD, i);
        cmd.trigger();
        Assertions.assertEquals(currentValue+i,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD));
    }

    private void addRessourceGoldWithHammer(int i){
        new Hammer(manager,managerCmd,0,idPlayer).trigger();
        Assertions.assertTrue(manager.getGame().getPlayer(idPlayer).getInventory().haveHammer());
        createCmdRessource(CmdRessource.ADD, TypeRessource.GOLD, i);
        cmd.trigger();
        Mockito.verify(managerCmd).triggerCommandEffectHammer(any(UUID.class),any(Integer.class));
    }

    private void addRessourceGoldAfterHammer(int i){
        int currentValue = manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD);
        createCmdRessource(CmdRessource.ADD_AFTER_HAMMER, TypeRessource.GOLD, i);
        cmd.trigger();
        Assertions.assertEquals(currentValue+i,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD));
    }

    private void removeRessource(int i){
        int currentValue = manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD);
        createCmdRessource(CmdRessource.REMOVE, TypeRessource.GOLD, i);
        cmd.trigger();
        Assertions.assertEquals(currentValue-i,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD));
    }

    private void testLimitAddRessource() {
        int currentValue = manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD);
        int maxGold = ConfigPlayer.GOLD_PLAYER_MAX_INIT;
        int diffLim = maxGold - currentValue + 1;
        createCmdRessource(CmdRessource.ADD_AFTER_HAMMER, TypeRessource.GOLD, diffLim);
        cmd.trigger();
        Assertions.assertEquals(maxGold,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD));
    }

    private void testLimitRemoveRessource() {
        int maxGold = ConfigPlayer.GOLD_PLAYER_MAX_INIT;
        int diffLim = maxGold + 1;
        createCmdRessource(CmdRessource.REMOVE, TypeRessource.GOLD, diffLim);
        cmd.trigger();
        Assertions.assertEquals(0,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD));
    }

}