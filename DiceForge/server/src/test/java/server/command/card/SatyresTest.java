package server.command.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.choice.ChoiceSatyre;
import share.eventclientserver.Events;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.FaceSpecial;
import share.face.FaceSpecialEnum;
import share.face.FactoryFace;

import java.util.UUID;

import static org.mockito.Matchers.any;

class SatyresTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;
    UUID idPlayer2;
    ChoiceSatyre ch;
    Satyres cmd;

    @BeforeEach
    void initTest(){
        FactoryFace.resetInstance();
        idPlayer = UUID.randomUUID();
        idPlayer = UUID.randomUUID();
        INotifyEvent callback = Mockito.mock(INotifyEvent.class);
        manager = new GameManager(callback);
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "str",0);
        manager.notifyConnectionPlayer(idPlayer2, "str",0);
        Mockito.doNothing().when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
    }

    @Test
    void onExecute() throws InstanceFaceOutOfBoundException {
        ArgumentCaptor<FaceSpecial> argument = ArgumentCaptor.forClass(FaceSpecial.class);
        FaceSpecial f = FactoryFace.getInstance().getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
        cmd = new Satyres(manager,managerCmd,idPlayer);
        ch = new ChoiceSatyre();
        ch.faceChoiceTwo = this.manager.getGame().getPlayer(idPlayer2).getInventory().getDice(0).mapFaces.get(0);
        ch.faceChoiceOne = this.manager.getGame().getPlayer(idPlayer2).getInventory().getDice(0).mapFaces.get(1);
        cmd.notifyChoice(idPlayer,ch);
        cmd.handlerActionRollDice = Mockito.spy(cmd.handlerActionRollDice);
        cmd.trigger();
        Mockito.verify(cmd.handlerActionRollDice).executeRollDiceMajor(ch.faceChoiceOne,ch.faceChoiceTwo,idPlayer);
        Mockito.verify(managerCmd).triggerCommandHandleActionRollDice(cmd.handlerActionRollDice);
    }
}