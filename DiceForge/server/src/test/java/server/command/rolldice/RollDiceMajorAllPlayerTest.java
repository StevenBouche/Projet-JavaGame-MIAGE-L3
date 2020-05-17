package server.command.rolldice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.eventclientserver.Events;
import share.face.Face;
import share.face.FactoryFace;

import java.util.UUID;

import static org.mockito.Matchers.any;

class RollDiceMajorAllPlayerTest {
    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;
    UUID idPlayer2;
    HandlerActionRollDice cmd2;

    @BeforeEach
    void init(){
        FactoryFace.resetInstance();
        idPlayer = UUID.randomUUID();
        idPlayer2 = UUID.randomUUID();
        manager = new GameManager(Mockito.mock(INotifyEvent.class));
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "str",0);
        manager.notifyConnectionPlayer(idPlayer2, "str",0);
        Mockito.doNothing().when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
    }

    @Test
    void rollDiceMajorAllPlayer(){
        RollDiceMajorAllPlayer cmd = new RollDiceMajorAllPlayer(manager,managerCmd);
        cmd2 = new HandlerActionRollDice(manager,managerCmd);
        cmd2 = Mockito.spy(cmd2);
        cmd.handlerActionRollDice = cmd2;
        cmd.trigger();
        Mockito.verify(cmd2,Mockito.times(2)).executeRollDiceMajor(any(Face.class),any(Face.class),any(UUID.class));
        Mockito.verify(managerCmd,Mockito.times(1)).triggerCommandHandleActionRollDice(cmd2);
    }
}