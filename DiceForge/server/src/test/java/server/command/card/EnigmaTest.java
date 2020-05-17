package server.command.card;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.eventclientserver.Events;
import share.face.FactoryFace;

import java.util.UUID;

import static org.mockito.Matchers.any;

class EnigmaTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;

    @Test
    void onExecute() {
        FactoryFace.resetInstance();
        idPlayer = UUID.randomUUID();
        manager = new GameManager(Mockito.mock(INotifyEvent.class));
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "str",0);
        Mockito.doNothing().when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
        Enigma cmd = new Enigma(manager,managerCmd,idPlayer);
        cmd.trigger();
        Mockito.verify(managerCmd,Mockito.times(4)).rollDiceMinorCommand(idPlayer);
    }
}