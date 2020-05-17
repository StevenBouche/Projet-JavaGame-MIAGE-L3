package server.command.face;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.command.face.HandleActionFaceHybridNotChoice;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.eventclientserver.Events;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.*;
import share.ressource.TypeRessource;

import java.util.*;

import static org.mockito.Matchers.any;

class HandleActionFaceHybridNotChoiceTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;
    UUID idPlayer2;
    HandleActionFaceHybridNotChoice cmd;
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
    void onExecute() {
        FaceHybrid fs = null;
        try {
            fs = factory.getFaceHybrid(FaceHybridEnum.GOLD2_LUNAR1);
            FaceHybrid fs2 = factory.getFaceHybrid(FaceHybridEnum.GLORY1_SOLAR1);
            Map<UUID, List<FaceHybrid>> event = new HashMap<>();
            List<FaceHybrid> l = new ArrayList<>();
            List<FaceHybrid> l2 = new ArrayList<>();
            l.add(fs);
            l2.add(fs2);
            event.put(idPlayer,l);
            event.put(idPlayer2,l2);
            cmd = new HandleActionFaceHybridNotChoice(manager,managerCmd,event);
            Assertions.assertEquals(0,cmd.mode);
            cmd.trigger();

            for(TypeRessource type : fs.listRessource.keySet()){
                Mockito.verify(managerCmd,
                        Mockito.times(1))
                        .addRessourcePlayerCommand(idPlayer,type,fs.getListRessource().get(type));
            }

            for(TypeRessource type : fs2.listRessource.keySet()){
                Mockito.verify(managerCmd,
                        Mockito.times(1))
                        .addRessourcePlayerCommand(idPlayer2,type,fs2.getListRessource().get(type));
            }


        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }


    }

    @Test
    void OneFace(){
        FaceHybrid fs3 = null;
        try {
            fs3 = factory.getFaceHybrid(FaceHybridEnum.GOLD1_GLORY1_SOLAR1_LUNAR1);
            cmd = new HandleActionFaceHybridNotChoice(manager,managerCmd,idPlayer,fs3);
            Assertions.assertEquals(1,cmd.mode);
            cmd.trigger();

            for(TypeRessource type : fs3.listRessource.keySet()){
                Mockito.verify(managerCmd,
                        Mockito.times(1))
                        .addRessourcePlayerCommand(idPlayer,type,fs3.getListRessource().get(type));
            }
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    void onExecuteInverse() {
        FaceHybrid fs = null;
        try {
            fs = factory.getFaceHybrid(FaceHybridEnum.GOLD2_LUNAR1);
            FaceHybrid fs2 = factory.getFaceHybrid(FaceHybridEnum.GLORY1_SOLAR1);
            Map<UUID, List<FaceHybrid>> event = new HashMap<>();
            List<FaceHybrid> l = new ArrayList<>();
            List<FaceHybrid> l2 = new ArrayList<>();
            l.add(fs);
            l2.add(fs2);
            event.put(idPlayer,l);
            event.put(idPlayer2,l2);
            cmd = new HandleActionFaceHybridNotChoice(manager,managerCmd,event,true);
            Assertions.assertEquals(0,cmd.mode);
            cmd.trigger();

            for(TypeRessource type : fs.listRessource.keySet()){
                Mockito.verify(managerCmd,
                        Mockito.times(1))
                        .removeRessourcePlayerCommand(idPlayer,type,fs.getListRessource().get(type));
            }

            for(TypeRessource type : fs2.listRessource.keySet()){
                Mockito.verify(managerCmd,
                        Mockito.times(1))
                        .removeRessourcePlayerCommand(idPlayer2,type,fs2.getListRessource().get(type));
            }


        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }


    }

    @Test
    void OneFaceInverse(){
        FaceHybrid fs3 = null;
        try {
            fs3 = factory.getFaceHybrid(FaceHybridEnum.GOLD1_GLORY1_SOLAR1_LUNAR1);
            cmd = new HandleActionFaceHybridNotChoice(manager,managerCmd,idPlayer,fs3,true);
            Assertions.assertEquals(1,cmd.mode);
            cmd.trigger();

            for(TypeRessource type : fs3.listRessource.keySet()){
                Mockito.verify(managerCmd,
                        Mockito.times(1))
                        .removeRessourcePlayerCommand(idPlayer,type,fs3.getListRessource().get(type));
            }
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
        }

    }
}