package server.command.card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.command.rolldice.HandlerActionRollDice;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.face.Face;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

class MinotaureTest {


    UUID idPlayer;
    UUID idPlayer2;
    CommandManager managerCmd;
    GameManager manager;
    HandlerActionRollDice handler;
    Minotaure cmd;

    @Test
    void onExecute() {
        idPlayer = UUID.randomUUID();
        idPlayer2 = UUID.randomUUID();
        INotifyEvent callback = Mockito.mock(INotifyEvent.class);
        manager = new GameManager(callback);
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
        manager.notifyConnectionPlayer(idPlayer, "str",0);
        manager.notifyConnectionPlayer(idPlayer2, "str1",1);
        cmd = new Minotaure(manager,managerCmd,idPlayer);
        cmd.handlerActionRollDice = Mockito.spy(cmd.handlerActionRollDice);
        cmd.trigger();
        Assertions.assertTrue(cmd.handlerActionRollDice.getInverse());
        Mockito.verify(cmd.handlerActionRollDice,Mockito.times(1)).executeRollDiceMajor(any(Face.class),any(Face.class),eq(idPlayer2));
    }
}