package server.command.face;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.command.face.FaceChoicePowerAction;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.choice.ChoicePowerOnDiceOtherPlayer;
import share.eventclientserver.Events;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.Face;
import share.face.FaceSpecial;
import share.face.FaceSpecialEnum;
import share.face.FactoryFace;
import share.player.Player;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Matchers.any;

class FaceChoicePowerActionTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;
    UUID idPlayer2;
    ChoicePowerOnDiceOtherPlayer ch;
    FaceChoicePowerAction cmd;

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
    void executeWithNoSpecial()  {
        cmd = new FaceChoicePowerAction(manager,managerCmd,idPlayer,null);
        ch = new ChoicePowerOnDiceOtherPlayer();
        Assertions.assertEquals(0,cmd.mode);
        cmd.handlerActionRollDice = Mockito.spy(cmd.handlerActionRollDice);
        for(Player p : manager.getGame().getPlayers().values()){
            if(p.getId() != idPlayer){
                manager.getGame().getPlayer(p.getId()).getInventory().rollDiceMajorList();
                Map<Integer, Face> res =  manager.getGame().getPlayer(p.getId()).getInventory().getLastRoll();
                ch.addRollsPlayers(p.getId(),res.get(0),res.get(1));
            }
        }
        int choice = manager.getGame().getPlayer(idPlayer).getInventory().getDice(0).indexLastRoll;
        ch.faceChoice = manager.getGame().getPlayer(idPlayer).getInventory().getDice(0).getFace(choice);
        cmd.notifyChoice(idPlayer,ch);
        cmd.trigger();
        Mockito.verify(cmd.handlerActionRollDice).executeRollDiceMinor(ch.faceChoice,idPlayer);
        Mockito.verify(managerCmd).triggerCommandHandleActionRollDice(cmd.handlerActionRollDice);
    }

    @Test
    void executeWithSpecial() throws  InstanceFaceOutOfBoundException {
        FaceSpecial fs = FactoryFace.getInstance().getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3);
        cmd = new FaceChoicePowerAction(manager,managerCmd,idPlayer,fs);
        ch = new ChoicePowerOnDiceOtherPlayer();
        Assertions.assertEquals(1,cmd.mode);
        cmd.handlerActionRollDice = Mockito.spy(cmd.handlerActionRollDice);
        for(Player p : manager.getGame().getPlayers().values()){
            if(p.getId() != idPlayer){
                manager.getGame().getPlayer(p.getId()).getInventory().rollDiceMajorList();
                Map<Integer, Face> res =  manager.getGame().getPlayer(p.getId()).getInventory().getLastRoll();
                ch.addRollsPlayers(p.getId(),res.get(0),res.get(1));
            }
        }
        int choice = manager.getGame().getPlayer(idPlayer).getInventory().getDice(0).indexLastRoll;
        ch.faceChoice = manager.getGame().getPlayer(idPlayer).getInventory().getDice(0).getFace(choice);
        cmd.notifyChoice(idPlayer,ch);
        cmd.trigger();
        Mockito.verify(managerCmd).triggerCommandActionFaceSpecialWithOther(idPlayer,fs,ch.faceChoice);
    }
}