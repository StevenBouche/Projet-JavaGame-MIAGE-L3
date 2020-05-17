package server.command.dice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.eventclientserver.Events;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.Face;
import share.face.FaceHybridEnum;
import share.face.FactoryFace;

import java.util.UUID;

import static org.mockito.Matchers.any;

class ForgeFaceOnDiceTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;

    @Test
    void onExecute() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        FactoryFace.getInstance();
        idPlayer = UUID.randomUUID();
        manager = new GameManager(Mockito.mock(INotifyEvent.class));
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "str",0);
        Mockito.doNothing().when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
        FactoryFace.resetInstance();
        Face f = FactoryFace.getInstance().getFaceHybrid(FaceHybridEnum.GOLD1_GLORY1_SOLAR1_LUNAR1);
        ForgeFaceOnDice cmd = new ForgeFaceOnDice(manager,managerCmd,idPlayer,f,0,0);
        Face fs = manager.getGame().getPlayer(idPlayer).getInventory().getDice(0).mapFaces.get(0);
        cmd.trigger();
        Assertions.assertEquals(f.toString(),manager.getGame().getPlayer(idPlayer).getInventory().getDice(0).mapFaces.get(0).toString());
        Assertions.assertEquals(fs.toString(),manager.getGame().getPlayer(idPlayer).getInventory().getHistoryFace().get(0).toString());
    }
}