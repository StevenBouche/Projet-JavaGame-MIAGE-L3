package server.command.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.choice.ChoiceBetweenRessource;
import share.eventclientserver.Events;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.FactoryFace;
import share.ressource.TypeRessource;

import java.util.EnumMap;
import java.util.UUID;

import static org.mockito.Matchers.any;


class OwlTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;
    ChoiceBetweenRessource ch;
    Owl cmd;

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
        cmd = new Owl(manager,managerCmd,idPlayer);
        EnumMap<TypeRessource, Integer> listRessource = new EnumMap<TypeRessource, Integer>(TypeRessource.class);
        listRessource.put(TypeRessource.GOLD,1);
        listRessource.put(TypeRessource.LUNAR,1);
        listRessource.put(TypeRessource.SOLAR,1);
        ch = new ChoiceBetweenRessource(listRessource);
        ch.setTypeRessource(TypeRessource.LUNAR);
        cmd.notifyChoice(idPlayer,ch);
        cmd.trigger();
        Mockito.verify(managerCmd).addRessourcePlayerCommand(idPlayer,ch.getTypeRessource(),ch.getListRessource().get(ch.getTypeRessource()));
    }

}