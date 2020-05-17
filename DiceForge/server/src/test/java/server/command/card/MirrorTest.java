package server.command.card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.choice.ChoiceForgeFaceSpecial;
import share.eventclientserver.Events;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.FaceSpecial;
import share.face.FaceSpecialEnum;
import share.face.FactoryFace;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

class MirrorTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;
    ChoiceForgeFaceSpecial ch;
    Mirror cmd;

    @BeforeEach
    void initTest(){
        FactoryFace.resetInstance();
        idPlayer = UUID.randomUUID();
        INotifyEvent callback = Mockito.mock(INotifyEvent.class);
        manager = new GameManager(callback);
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "str",0);
        Mockito.doNothing().when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
    }

    @Test
    void onExecute() throws InstanceFaceOutOfBoundException {
        ArgumentCaptor<FaceSpecial> argument = ArgumentCaptor.forClass(FaceSpecial.class);
        FaceSpecial f = FactoryFace.getInstance().getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
        cmd = new Mirror(manager,managerCmd,idPlayer);
        ch = new ChoiceForgeFaceSpecial();
        ch.indexDiceFace = 0;
        ch.indexDice = 0;
        cmd.notifyChoice(idPlayer,ch);
        cmd.trigger();
        Mockito.verify(managerCmd).triggerCommandForgeFaceOnDice(eq(idPlayer),argument.capture(),eq(0),eq(0));
        Assertions.assertEquals(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER,argument.getValue().faceEnum);
    }
}