package server.command.player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.face.FactoryFace;

import java.util.UUID;

import static org.mockito.Matchers.any;

class SetPlayerActifTest {

    static CommandManager managerCmd;
    static GameManager manager;
    static UUID idPlayer;
    static UUID idPlayer2;
    static SetPlayerActif cmd;

    @BeforeEach
    void initTest(){
        FactoryFace.resetInstance();
        idPlayer = UUID.randomUUID();
        idPlayer2 = UUID.randomUUID();
        INotifyEvent callback = Mockito.mock(INotifyEvent.class);
        manager = new GameManager(callback);
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "",0);
        manager.notifyConnectionPlayer(idPlayer2, "",0);
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
    }

    @Test
    void onExecute() {
        Assertions.assertNull(manager.getGame().currentPlayer);
        cmd = new SetPlayerActif(manager,managerCmd);
        cmd.trigger();
        Assertions.assertEquals(idPlayer,manager.getGame().getIdOfCurrentPlayer());
        Assertions.assertEquals(idPlayer,manager.getGame().currentPlayer.getId());
        cmd.trigger();
        Assertions.assertEquals(idPlayer2,manager.getGame().getIdOfCurrentPlayer());
        Assertions.assertEquals(idPlayer2,manager.getGame().currentPlayer.getId());
    }
}