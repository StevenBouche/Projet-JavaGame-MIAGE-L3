package server.command.face;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.command.face.HandleActionFaceSpecialWithOther;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.eventclientserver.Events;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.*;
import share.ressource.TypeRessource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

class HandleActionFaceSpecialWithOtherTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;
    HandleActionFaceSpecialWithOther cmd;
    FactoryFace factory;

    @BeforeEach
    void init(){
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        idPlayer = UUID.randomUUID();
        manager = new GameManager(Mockito.mock(INotifyEvent.class));
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "str",0);
        Mockito.doNothing().when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
    }

    @Test
    void testConstructorCast() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
        FaceSimple fs2 = factory.getFaceSimple(FaceSimpleEnum.GLORY_FOUR);
        FaceHybrid fs3 = factory.getFaceHybrid(FaceHybridEnum.GLORY2_LUNAR2);
        FaceSpecial fs4 = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
        cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,(Face)fs2);
        Assertions.assertEquals(2,cmd.mode);
        Assertions.assertNotNull(cmd.faceSimple2);
        cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,(Face)fs3);
        Assertions.assertEquals(1,cmd.mode);
        Assertions.assertNotNull(cmd.faceHybrid2);
        cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,(Face)fs4);
        Assertions.assertEquals(0,cmd.mode);
        Assertions.assertNotNull(cmd.faceSpecial2);
    }

    @Test
    void X3WithSimple(){
        try {
            FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
            FaceSimple fs2 = factory.getFaceSimple(FaceSimpleEnum.GLORY_FOUR);
            cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,fs2);
            Assertions.assertEquals(2,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,Mockito.times(1)).addRessourcePlayerCommand(idPlayer, fs2.getTypeRessource(), fs2.getValue()*3);
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void X3WithSimpleInverse(){
        try {
            FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
            FaceSimple fs2 = factory.getFaceSimple(FaceSimpleEnum.GLORY_FOUR);
            cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,fs2,true);
            Assertions.assertEquals(2,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,Mockito.times(1)).removeRessourcePlayerCommand(idPlayer, fs2.getTypeRessource(), fs2.getValue()*3);
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void X3WithHybrid(){
        try {
            ArgumentCaptor<FaceHybrid> argument = ArgumentCaptor.forClass(FaceHybrid.class);
            FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
            FaceHybrid fs2 = factory.getFaceHybrid(FaceHybridEnum.GLORY2_LUNAR2);
            cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,fs2);
            Assertions.assertEquals(1,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,Mockito.times(1)).triggerCommandHandleNotChoiceHybrid(eq(idPlayer),argument.capture(),eq(false));
            for(TypeRessource type : fs2.listRessource.keySet()){
                Assertions.assertEquals(fs2.listRessource.get(type)*3,argument.getValue().listRessource.get(type));
            }
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void X3WithHybridInverse(){
        try {
            ArgumentCaptor<FaceHybrid> argument = ArgumentCaptor.forClass(FaceHybrid.class);
            FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
            FaceHybrid fs2 = factory.getFaceHybrid(FaceHybridEnum.GLORY2_LUNAR2);
            cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,fs2,true);
            Assertions.assertEquals(1,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,Mockito.times(1)).triggerCommandHandleNotChoiceHybrid(eq(idPlayer),argument.capture(),eq(true));
            for(TypeRessource type : fs2.listRessource.keySet()){
                Assertions.assertEquals(fs2.listRessource.get(type)*3,argument.getValue().listRessource.get(type));
            }
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void X3WithHybridChoice(){
        try {
            FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
            FaceHybrid fs2 = factory.getFaceHybrid(FaceHybridEnum.GOLD2_OR_SOLAR2_OR_LUNAR2);
            ArgumentCaptor<FaceHybrid> argument = ArgumentCaptor.forClass(FaceHybrid.class);
            cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,fs2);
            Assertions.assertEquals(1,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,
                    Mockito.times(1)).triggerCommandHandleHybrid(eq(idPlayer),argument.capture(),eq(false));
            for(TypeRessource type : fs2.listRessource.keySet()){
                assertEquals(fs2.listRessource.get(type)*3, argument.getValue().listRessource.get(type));
            }
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void X3WithHybridChoiceInverse(){
        try {
            FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
            FaceHybrid fs2 = factory.getFaceHybrid(FaceHybridEnum.GOLD2_OR_SOLAR2_OR_LUNAR2);
            ArgumentCaptor<FaceHybrid> argument = ArgumentCaptor.forClass(FaceHybrid.class);
            cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,fs2,true);
            Assertions.assertEquals(1,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,
                    Mockito.times(1)).triggerCommandHandleHybrid(eq(idPlayer),argument.capture(),eq(true));
            for(TypeRessource type : fs2.listRessource.keySet()){
                assertEquals(fs2.listRessource.get(type)*3, argument.getValue().listRessource.get(type));
            }
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void X3WithOtherSpecial(){
        try {
            FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
            FaceSpecial fs2 = factory.getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
            cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,fs2);
            Assertions.assertEquals(0,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,
                    Mockito.times(1)).triggerCommandActionFaceSpecialPower(idPlayer,fs,false);
            Mockito.reset(managerCmd);
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void OtherWithSimple(){
        try {
            FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
            FaceSimple fs2 = factory.getFaceSimple(FaceSimpleEnum.GLORY_FOUR);
            cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,fs2);
            Assertions.assertEquals(2,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,
                    Mockito.times(1)).triggerCommandActionFaceSpecialPower(idPlayer,null,false);
            Mockito.verify(managerCmd,
                    Mockito.times(1)).triggerCommandHandleFaceSimple(idPlayer,fs2,false);

        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void OtherWithHybrid(){
        try {
            FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
            FaceHybrid fs2 = factory.getFaceHybrid(FaceHybridEnum.GLORY2_LUNAR2);
            cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,fs2);
            Assertions.assertEquals(1,cmd.mode);
            cmd.trigger();

            Mockito.verify(managerCmd,
                    Mockito.times(1)).triggerCommandActionFaceSpecialPower(eq(idPlayer),eq(null),eq(false));
            Mockito.verify(managerCmd,
                    Mockito.times(1)).triggerCommandHandleNotChoiceHybrid(eq(idPlayer),eq(fs2),eq(false));

        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void OtherWithHybridChoice(){
        try {
            FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
            FaceHybrid fs2 = factory.getFaceHybrid(FaceHybridEnum.GOLD3_OR_GLORY2);
            cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,fs2);
            Assertions.assertEquals(1,cmd.mode);
            cmd.trigger();

            Mockito.verify(managerCmd,
                    Mockito.times(1)).triggerCommandActionFaceSpecialPower(eq(idPlayer),eq(null),eq(false));
            Mockito.verify(managerCmd,
                    Mockito.times(1)).triggerCommandHandleHybrid(eq(idPlayer),eq(fs2),eq(false));

        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void OtherWithX3Special(){
        try {
            FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
            FaceSpecial fs2 = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
            cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,fs2);
            Assertions.assertEquals(0,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,
                    Mockito.times(1)).triggerCommandActionFaceSpecialPower(idPlayer,fs2,false);
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void OtherWithOther(){
        try {
            FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
            FaceSpecial fs2 = factory.getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
            cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,fs2);
            Assertions.assertEquals(0,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,
                    Mockito.times(2)).triggerCommandActionFaceSpecialPower(idPlayer,null,false);
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void X3WithX3(){
        try {
            FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
            FaceSpecial fs2 = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
            cmd = new HandleActionFaceSpecialWithOther(manager,managerCmd,idPlayer,fs,fs2);
            Assertions.assertEquals(0,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,
                    Mockito.times(0)).triggerCommand(any(Command.class));
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }



}