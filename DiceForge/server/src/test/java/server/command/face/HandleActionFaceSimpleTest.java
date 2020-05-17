package server.command.face;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.command.face.HandleActionFaceSimple;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.eventclientserver.Events;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.FaceSimple;
import share.face.FaceSimpleEnum;
import share.face.FactoryFace;

import java.util.*;

import static org.mockito.Matchers.any;

class HandleActionFaceSimpleTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;
    UUID idPlayer2;
    HandleActionFaceSimple cmd;
    FactoryFace factory;

    @BeforeEach
    void init(){
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
        idPlayer = UUID.randomUUID();
        idPlayer2 = UUID.randomUUID();
        manager = new GameManager(Mockito.mock(INotifyEvent.class));
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "str",0);
        manager.notifyConnectionPlayer(idPlayer2, "str",0);
        Mockito.doNothing().when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        FactoryFace.resetInstance();
        factory = FactoryFace.getInstance();
    }

    @Test
    void handleListFaceSimple(){
        FaceSimple fs = null;
        try {
            fs = factory.getFaceSimple(FaceSimpleEnum.GOLD_ONE);
            cmd = new HandleActionFaceSimple(manager,managerCmd,idPlayer,fs);
            Assertions.assertEquals(1,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,Mockito.times(1)).addRessourcePlayerCommand(idPlayer,fs.getTypeRessource(),fs.getValue());
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    void handleOneFace(){
        try {
            FaceSimple fs = factory.getFaceSimple(FaceSimpleEnum.GOLD_ONE);
            FaceSimple fs2 = factory.getFaceSimple(FaceSimpleEnum.GLORY_FOUR);
            Map<UUID, List<FaceSimple>> event = new HashMap<>();
            List<FaceSimple> l = new ArrayList<>();
            List<FaceSimple> l2 = new ArrayList<>();
            l.add(fs);
            l2.add(fs2);
            event.put(idPlayer,l);
            event.put(idPlayer2,l2);
            cmd = new HandleActionFaceSimple(manager,managerCmd,event);
            Assertions.assertEquals(0,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,Mockito.times(1)).addRessourcePlayerCommand(idPlayer,fs.getTypeRessource(),fs.getValue());
            Mockito.verify(managerCmd,Mockito.times(1)).addRessourcePlayerCommand(idPlayer2,fs2.getTypeRessource(),fs2.getValue());

        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void handleListFaceSimpleInverse(){
        FaceSimple fs = null;
        try {
            fs = factory.getFaceSimple(FaceSimpleEnum.GOLD_ONE);
            cmd = new HandleActionFaceSimple(manager,managerCmd,idPlayer,fs,true);
            Assertions.assertEquals(1,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,Mockito.times(1)).removeRessourcePlayerCommand(idPlayer,fs.getTypeRessource(),fs.getValue());
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    void handleOneFaceInverse(){
        try {
            FaceSimple fs = factory.getFaceSimple(FaceSimpleEnum.GOLD_ONE);
            FaceSimple fs2 = factory.getFaceSimple(FaceSimpleEnum.GLORY_FOUR);
            Map<UUID, List<FaceSimple>> event = new HashMap<>();
            List<FaceSimple> l = new ArrayList<>();
            List<FaceSimple> l2 = new ArrayList<>();
            l.add(fs);
            l2.add(fs2);
            event.put(idPlayer,l);
            event.put(idPlayer2,l2);
            cmd = new HandleActionFaceSimple(manager,managerCmd,event,true);
            Assertions.assertEquals(0,cmd.mode);
            cmd.trigger();
            Mockito.verify(managerCmd,Mockito.times(1)).removeRessourcePlayerCommand(idPlayer,fs.getTypeRessource(),fs.getValue());
            Mockito.verify(managerCmd,Mockito.times(1)).removeRessourcePlayerCommand(idPlayer2,fs2.getTypeRessource(),fs2.getValue());

        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }
    }
}