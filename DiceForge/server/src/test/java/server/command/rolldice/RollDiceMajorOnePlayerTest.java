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
import static org.mockito.Matchers.eq;

class RollDiceMajorOnePlayerTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;
    HandlerActionRollDice cmd2;

    @BeforeEach
    void init(){
        FactoryFace.resetInstance();
        idPlayer = UUID.randomUUID();
        manager = new GameManager(Mockito.mock(INotifyEvent.class));
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "str",0);
        Mockito.doNothing().when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
    }

    @Test
    void rollDiceMajorPlayer(){
        RollDiceMajorOnePlayer cmd = new RollDiceMajorOnePlayer(manager,managerCmd,idPlayer);
        cmd2 = new HandlerActionRollDice(manager,managerCmd);
        cmd2 = Mockito.spy(cmd2);
        cmd.handlerActionRollDice = cmd2;
        cmd.trigger();
        Mockito.verify(cmd2,Mockito.times(1)).executeRollDiceMajor(any(Face.class),any(Face.class),eq(idPlayer));
        Mockito.verify(managerCmd,Mockito.times(1)).triggerCommandHandleActionRollDice(cmd2);
    }
}