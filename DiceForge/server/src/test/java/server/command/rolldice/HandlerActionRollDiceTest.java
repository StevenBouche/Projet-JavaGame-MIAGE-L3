package server.command.rolldice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.command.face.FaceChoicePowerAction;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.eventclientserver.Events;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

class HandlerActionRollDiceTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;
    UUID idPlayer2;
    UUID idPlayer3;
    UUID idPlayer4;
    HandlerActionRollDice cmd;
    FactoryFace factory;

    @BeforeEach
    void init(){
        FactoryFace.resetInstance();
        idPlayer = UUID.randomUUID();
        idPlayer2 = UUID.randomUUID();
        idPlayer3 = UUID.randomUUID();
        idPlayer4 = UUID.randomUUID();
        factory = FactoryFace.getInstance();
        manager = new GameManager(Mockito.mock(INotifyEvent.class));
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "str",0);
        manager.notifyConnectionPlayer(idPlayer2, "str",0);
        manager.notifyConnectionPlayer(idPlayer3, "str",0);
        manager.notifyConnectionPlayer(idPlayer4, "str",0);
        Mockito.doNothing().when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
        cmd = new HandlerActionRollDice(manager,managerCmd);
    }

    void testExecuteMinor(HandlerActionRollDice cmd){
        assertTrue(cmd.listPlayerHaveTrigger.contains(idPlayer));
        assertEquals(1,cmd.getNbRollPlayer(idPlayer));
    }

    void testExecute(HandlerActionRollDice cmd, int nbEvent, UUID idPlayer){
        assertTrue(cmd.listPlayerHaveTrigger.contains(idPlayer));
        assertEquals(nbEvent,cmd.getNbRollPlayer(idPlayer));
    }

    void verifyNbExecuteHandleFace(int nbS, int nbH, int nbHC){
        Mockito.verify(managerCmd,Mockito.times(nbS)).triggerCommandHandleFaceSimple(cmd.eventFaceSimple);
        Mockito.verify(managerCmd,Mockito.times(nbHC)).triggerCommandHandleChoiceHybrid(cmd.eventFaceHybridChoice);
        Mockito.verify(managerCmd,Mockito.times(nbH)).triggerCommandHandleHybrid(cmd.eventFaceHybrid);
    }

    void testListSimple(UUID idPlayer, FaceSimple fs){
        assertTrue(cmd.eventFaceSimple.containsKey(idPlayer));
        assertTrue(cmd.eventFaceSimple.get(idPlayer).contains(fs));
    }

    void testListHybrid(UUID idPlayer, FaceHybrid fh){
        assertFalse(fh.isChoice());
        assertTrue(cmd.eventFaceHybrid.containsKey(idPlayer));
        assertTrue(cmd.eventFaceHybrid.get(idPlayer).contains(fh));
    }

    void testListHybridChoice(UUID idPlayer, FaceHybrid fh){
        assertTrue(fh.isChoice());
        assertTrue(cmd.eventFaceHybridChoice.containsKey(idPlayer));
        assertTrue(cmd.eventFaceHybridChoice.get(idPlayer).contains(fh));
    }

    void testListSpecial(UUID idPlayer, FaceSpecial fs){
        assertTrue(cmd.eventFaceSpecial.containsKey(idPlayer));
        assertTrue(cmd.eventFaceSpecial.get(idPlayer).contains(fs));
    }

    @Test
    void executeRollDiceMinorSimple() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        FaceSimple fs = factory.getFaceSimple(FaceSimpleEnum.GLORY_FOUR);
        cmd.executeRollDiceMinor(fs,idPlayer);
        testExecuteMinor(cmd);
        testListSimple(idPlayer,fs);
        cmd.trigger();
        verifyNbExecuteHandleFace(1,0,0);
    }

    @Test
    void executeRollDiceMinorSpecial() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
        cmd.executeRollDiceMinor(fs,idPlayer);
        testExecuteMinor(cmd);
        testListSpecial(idPlayer,fs);
        cmd.trigger();
        verifyNbExecuteHandleFace(0,0,0);
        Mockito.verify(managerCmd,Mockito.times(0)).triggerCommand(Mockito.any(FaceChoicePowerAction.class));
        Assertions.assertEquals(0,cmd.eventFaceSpecial.get(idPlayer).size());
    }

    @Test
    void executeRollDiceMinorSpecial2() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        FaceSpecial fs = factory.getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
        cmd.executeRollDiceMinor(fs,idPlayer);
        testExecuteMinor(cmd);
        testListSpecial(idPlayer,fs);
        cmd.trigger();
        verifyNbExecuteHandleFace(0,0,0);
        Mockito.verify(managerCmd,Mockito.times(1)).triggerCommandActionFaceSpecialPower(idPlayer,null,false);
        Assertions.assertEquals(0,cmd.eventFaceSpecial.get(idPlayer).size());
    }

    @Test
    void executeRollDiceMinorHybrid() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        FaceHybrid fs = factory.getFaceHybrid(FaceHybridEnum.GLORY2_LUNAR2);
        cmd.executeRollDiceMinor(fs,idPlayer);
        testExecuteMinor(cmd);
        testListHybrid(idPlayer,fs);
        cmd.trigger();
        verifyNbExecuteHandleFace(0,1,0);
    }

    @Test
    void executeRollDiceMinorHybridChoice() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        FaceHybrid fs = factory.getFaceHybrid(FaceHybridEnum.GOLD3_OR_GLORY2);
        cmd.executeRollDiceMinor(fs,idPlayer);
        testExecuteMinor(cmd);
        testListHybridChoice(idPlayer,fs);
        cmd.trigger();
        Mockito.verify(managerCmd).triggerCommandHandleChoiceHybrid(cmd.eventFaceHybridChoice);
        verifyNbExecuteHandleFace(0,0,1);
    }

    @Test
    void testQueueEvent() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        FaceHybrid fs = factory.getFaceHybrid(FaceHybridEnum.GOLD3_OR_GLORY2);
        FaceHybrid fs2 = factory.getFaceHybrid(FaceHybridEnum.GLORY2_LUNAR2);
        FaceSpecial fs3 = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
        cmd.executeRollDiceMinor(fs,idPlayer);
        cmd.executeRollDiceMinor(fs2,idPlayer);
        cmd.executeRollDiceMinor(fs3,idPlayer);
        testExecute(cmd,2,idPlayer);
    }

    @Test
    void executeRollDiceSpecialSimple() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        FaceSimple fs = factory.getFaceSimple(FaceSimpleEnum.GLORY_FOUR);
        FaceSpecial fs4 = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
        cmd.executeRollDiceMajor(fs,fs4,idPlayer);
        cmd.trigger();
        Mockito.verify(managerCmd).triggerCommandActionFaceSpecialWithOther(idPlayer,fs4,fs);
        Assertions.assertEquals(0,cmd.getNbRollPlayer(idPlayer));
    }

    @Test
    void executeRollDiceSpecialHybrid() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        FaceHybrid fs3 = factory.getFaceHybrid(FaceHybridEnum.GLORY2_LUNAR2);
        FaceSpecial fs4 = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
        cmd.executeRollDiceMajor(fs3,fs4,idPlayer);
        cmd.trigger();
        Mockito.verify(managerCmd).triggerCommandActionFaceSpecialWithOther(idPlayer,fs4,fs3);
        Assertions.assertEquals(0,cmd.getNbRollPlayer(idPlayer));
    }

    @Test
    void executeRollDiceSpecialHybridChoice() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        FaceHybrid fs2 = factory.getFaceHybrid(FaceHybridEnum.GOLD3_OR_GLORY2);
        FaceSpecial fs4 = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
        cmd.executeRollDiceMajor(fs2,fs4,idPlayer);
        cmd.trigger();
        Mockito.verify(managerCmd).triggerCommandActionFaceSpecialWithOther(idPlayer,fs4,fs2);
        Assertions.assertEquals(0,cmd.getNbRollPlayer(idPlayer));

    }

    @Test
    void executeRollDiceOtherx3() throws InstanceFaceOutOfBoundException {
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        FaceSpecial fs4 = factory.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
        FaceSpecial fs3 = factory.getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
        cmd.executeRollDiceMajor(fs3,fs4,idPlayer);
        cmd.trigger();
        Mockito.verify(managerCmd,Mockito.times(1)).triggerCommandActionFaceSpecialWithOther(idPlayer,fs4,fs3);
        Assertions.assertEquals(0,cmd.getNbRollPlayer(idPlayer));
    }

    @Test
    void executeRollDiceOtherOther() throws InstanceFaceOutOfBoundException {
        ArgumentCaptor<Face> argument = ArgumentCaptor.forClass(Face.class);
        ArgumentCaptor<FaceSpecial> argument2 = ArgumentCaptor.forClass(FaceSpecial.class);
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        FaceSpecial fs4 = factory.getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
        FaceSpecial fs3 = factory.getFaceSpecial(FaceSpecialEnum.FACE_CHOICE_OTHER_FACE_PLAYER);
        cmd.executeRollDiceMajor(fs3,fs4,idPlayer);
        cmd.trigger();
        Mockito.verify(managerCmd,Mockito.times(1)).triggerCommandActionFaceSpecialWithOther(eq(idPlayer),argument2.capture(),argument.capture());
        Assertions.assertEquals(fs4.toString(),argument.getValue().toString());
        Assertions.assertEquals(fs3.toString(),argument2.getValue().toString());
        Assertions.assertEquals(0,cmd.getNbRollPlayer(idPlayer));
    }

    @Test
    void executeRollDiceInverse() throws InstanceFaceOutOfBoundException {
        cmd = new HandlerActionRollDice(manager,managerCmd,true);
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        FaceSimple fs4 = factory.getFaceSimple(FaceSimpleEnum.GLORY_FOUR);
        FaceHybrid fs3 = factory.getFaceHybrid(FaceHybridEnum.GOLD3_OR_GLORY2);
        FaceHybrid fs2 = factory.getFaceHybrid(FaceHybridEnum.GLORY1_SOLAR1);
        FaceSimple fs1 = factory.getFaceSimple(FaceSimpleEnum.GLORY_TWO);
        cmd.executeRollDiceMajor(fs3,fs4,idPlayer);
        cmd.executeRollDiceMajor(fs2,fs1,idPlayer2);
        cmd.trigger();
        Mockito.verify(managerCmd,Mockito.times(1)).triggerCommandHandleChoiceHybrid(any(), eq(true));
        Mockito.verify(managerCmd,Mockito.times(1)).triggerCommandHandleHybrid(any(), eq(true));
        Mockito.verify(managerCmd,Mockito.times(1)).triggerCommandHandleFaceSimple(any(), eq(true));
    }


}