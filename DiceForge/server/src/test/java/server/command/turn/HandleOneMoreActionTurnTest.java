package server.command.turn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.command.turn.HandleOneMoreActionTurn;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.choice.ChoiceOneMoreTurn;
import share.eventclientserver.Events;
import share.face.FactoryFace;
import share.ressource.TypeRessource;

import java.util.UUID;

import static org.mockito.Matchers.any;

class HandleOneMoreActionTurnTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;
    ChoiceOneMoreTurn ch;
    HandleOneMoreActionTurn cmd;

    @BeforeEach
    void initTest(){
        FactoryFace.resetInstance();
        idPlayer = UUID.randomUUID();
        INotifyEvent callback = Mockito.mock(INotifyEvent.class);
        manager = new GameManager(callback);
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "",0);
        Mockito.doNothing().when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
    }

    @Test
    void onExecute()  {
        manager.getGame().currentPlayer = manager.getGame().getPlayer(idPlayer);
        cmd = new HandleOneMoreActionTurn(manager,managerCmd);
        cmd.trigger();
        Mockito.verify(managerCmd,Mockito.times(0)).triggerCommand(any(Command.class));
    }

    @Test
    void onExecute2() {
        manager.getGame().currentPlayer = manager.getGame().getPlayer(idPlayer);
        manager.getGame().getPlayer(idPlayer).getInventory().addRessource(TypeRessource.SOLAR,2);
        cmd = new HandleOneMoreActionTurn(manager,managerCmd);
        ch = new ChoiceOneMoreTurn();
        ch.valueSolaryPlayer = 2;
        ch.choice = true;
        cmd.notifyChoice(idPlayer,ch);
        cmd.trigger();
        Mockito.verify(managerCmd,Mockito.times(1)).removeRessourcePlayerCommand(idPlayer,TypeRessource.SOLAR,2);
        Mockito.verify(managerCmd,Mockito.times(1)).triggerCommandActionTurn();

    }

    @Test
    void onExecute3()  {
        manager.getGame().currentPlayer = manager.getGame().getPlayer(idPlayer);
        manager.getGame().getPlayer(idPlayer).getInventory().addRessource(TypeRessource.SOLAR,2);
        cmd = new HandleOneMoreActionTurn(manager,managerCmd);
        ch = new ChoiceOneMoreTurn();
        ch.valueSolaryPlayer = 2;
        ch.choice = false;
        cmd.notifyChoice(idPlayer,ch);
        cmd.trigger();
        Mockito.verify(managerCmd,Mockito.times(0)).triggerCommand(any(Command.class));
    }
}