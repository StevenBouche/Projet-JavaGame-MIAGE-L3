package server.command.card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.choice.ChoiceHammer;
import share.eventclientserver.Events;
import share.face.FactoryFace;
import share.ressource.TypeRessource;

import java.util.UUID;

import static org.mockito.Matchers.any;

class HammerTest {

    static CommandManager managerCmd;
    static GameManager manager;
    static UUID idPlayer;
    static Hammer cmd;
    static ChoiceHammer ch;

    @BeforeEach
     void initTest(){
        FactoryFace.resetInstance();
        FactoryFace.getInstance();
        idPlayer = UUID.randomUUID();
        INotifyEvent callback = Mockito.mock(INotifyEvent.class);
        manager = new GameManager(callback);
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "str",0);
        Mockito.doNothing().when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
    }

    @Test
    void onExecuteAddHammer(){
        testExecuteAddHammer();
    }

    @Test
    void onExecuteTreatmentHammer(){
        testExecuteTreatment();
    }

    @Test
    void executeHammerPlayerDontWant(){
        createCmdHammer(0,idPlayer);
        cmd.trigger();
        Assertions.assertTrue(manager.getGame().getPlayer(idPlayer).getInventory().haveHammer());
        Assertions.assertEquals(1,manager.getGame().getPlayer(idPlayer).getInventory().getSizeHammers());
        Mockito.verify(managerCmd).onEndExecute(cmd);
        int current = manager.getGame().getPlayer(idPlayer).getInventory().getCurrentIdHammer();
        int valueMax = manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getMaxGold();
        createCmdHammer(1,idPlayer);
        setUpChoiceCommand(5,0);
        cmd.trigger();
        Assertions.assertEquals(5,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD));
        Assertions.assertEquals(0,manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getNbStep());
        Assertions.assertEquals(0,manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getCurrentGold());
    }

    @Test
    void executeHammerPlayerwantMoreThanHammer(){
        createCmdHammer(0,idPlayer);
        cmd.trigger();
        Assertions.assertTrue(manager.getGame().getPlayer(idPlayer).getInventory().haveHammer());
        Assertions.assertEquals(1,manager.getGame().getPlayer(idPlayer).getInventory().getSizeHammers());
        Mockito.verify(managerCmd).onEndExecute(cmd);
        int current = manager.getGame().getPlayer(idPlayer).getInventory().getCurrentIdHammer();
        int valueMax = manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getMaxGold();
        createCmdHammer(1,idPlayer);
        setUpChoiceCommand(valueMax+2,valueMax+2);
        cmd.trigger();
        Assertions.assertEquals(2,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD));
        Assertions.assertEquals(1,manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getNbStep());
        Assertions.assertEquals(0,manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getCurrentGold());
    }

    private static void createCmdHammer(int mode, UUID idPlayer) {
        cmd = new Hammer(manager,managerCmd,mode,idPlayer);
        cmd = Mockito.spy(cmd);
    //    Mockito.doNothing().when(cmd).waitingDecision();
        Mockito.doNothing().when(cmd).notifyDecision();
    }

    private void testExecuteAddHammer() {
        Assertions.assertFalse(manager.getGame().getPlayer(idPlayer).getInventory().haveHammer());
        createCmdHammer(0,idPlayer);
        cmd.trigger();
        Assertions.assertTrue(manager.getGame().getPlayer(idPlayer).getInventory().haveHammer());
        Assertions.assertEquals(1,manager.getGame().getPlayer(idPlayer).getInventory().getSizeHammers());
        Mockito.verify(managerCmd).onEndExecute(cmd);
        createCmdHammer(0,idPlayer);
        cmd.trigger();
        Assertions.assertEquals(2,manager.getGame().getPlayer(idPlayer).getInventory().getSizeHammers());
        Mockito.verify(managerCmd).onEndExecute(cmd);
    }

    private void testExecuteTreatment() {
        withOneHammer();
    }

    private void withOneHammer(){
        int current = manager.getGame().getPlayer(idPlayer).getInventory().getCurrentIdHammer();
        createCmdHammer(0,idPlayer);
        cmd.trigger();
        int valueMax = manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getMaxGold();
        createCmdHammer(1,idPlayer);
        setUpChoiceCommand(5,3);
        cmd.trigger();
        Assertions.assertEquals(2,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD));
        Assertions.assertEquals(0,manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getNbStep());
        Assertions.assertEquals(3,manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getCurrentGold());
        Assertions.assertFalse(manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).isFinish());
        int nbRestHammer = valueMax - manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getCurrentGold();
        createCmdHammer(1,idPlayer);
        setUpChoiceCommand(nbRestHammer+1,nbRestHammer+1);
        cmd.trigger();
        Assertions.assertEquals(3,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD));
        Assertions.assertEquals(10,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GLORY));
        Assertions.assertEquals(1,manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getNbStep());
        Assertions.assertEquals(0,manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getCurrentGold());
        Assertions.assertFalse(manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).isFinish());
        createCmdHammer(1,idPlayer);
        setUpChoiceCommand(valueMax,valueMax);
        cmd.trigger();
        Assertions.assertEquals(3,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD));
        Assertions.assertEquals(25,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GLORY));
        Assertions.assertEquals(2,manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getNbStep());
        Assertions.assertEquals(15,manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).getCurrentGold());
        Assertions.assertTrue(manager.getGame().getPlayer(idPlayer).getInventory().getHammer(current).isFinish());
    }

    private void setUpChoiceCommand(int valueGoldWin, int valueChoice){
        cmd.setValueChoiceHammer(valueGoldWin); //  gold player choice
        ch = createChoiceHammer(valueGoldWin);
        ch.setValueToHammer(valueChoice); // player have choice
        cmd.notifyChoice(idPlayer,ch);
    }

    private ChoiceHammer createChoiceHammer(int valueChoice) {
        ChoiceHammer ch = new ChoiceHammer();
        ch.setValueGold(valueChoice);
        return  ch;
    }
}