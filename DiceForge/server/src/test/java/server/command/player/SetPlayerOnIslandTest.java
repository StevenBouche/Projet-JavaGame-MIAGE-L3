package server.command.player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.cards.Card;
import share.cards.Cards;
import share.cards.FactoryCard;
import share.eventclientserver.Events;
import share.face.FactoryFace;
import share.temple.Island;
import share.temple.IslandEnum;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;

class SetPlayerOnIslandTest {

    private UUID idPlayer;
    private UUID idPlayer2;

    private Card c;

    private GameManager manager;
    private CommandManager managerCmd;
    private SetPlayerOnIsland cmd;

    @BeforeEach
    void init(){
        FactoryFace.resetInstance();
        idPlayer = UUID.randomUUID();
        idPlayer2 = UUID.randomUUID();
        INotifyEvent callback = Mockito.mock(INotifyEvent.class);
        manager = new GameManager(callback);
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "str",0);
        Mockito.doNothing().when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
    }

    private void createCmd(UUID player) {
        c = FactoryCard.createXInstanceOfCard(Cards.ANCIENT,1).get(0);
        cmd = new SetPlayerOnIsland(manager,managerCmd,player,c);
        cmd = Mockito.spy(cmd);
    //    Mockito.doNothing().when(cmd).waitingDecision();
//        Mockito.doNothing().when(cmd).notifyDecision();
    }

    @Test
    void onExecute() {
       notPlayerOnIsland();
       playerOnIsland();
    }

    void notPlayerOnIsland(){
        this.createCmd(idPlayer);
        Island i = this.manager.getGame().temple.getIslandFromCard(c);
        Assertions.assertNull(i.player);
        cmd.trigger();
        i = this.manager.getGame().temple.getIslandFromCard(c);
        Assertions.assertEquals(idPlayer,i.player);
        Mockito.verify(this.managerCmd,times(0)).triggerCommandPlayerLeaveIsland(any(UUID.class),any(IslandEnum.class));
    }

    void playerOnIsland(){
        this.createCmd(idPlayer2);
        cmd.trigger();
        Island i = this.manager.getGame().temple.getIslandFromCard(c);
        Assertions.assertEquals(idPlayer2,i.player);
        Mockito.verify(this.managerCmd,times(1)).triggerCommandPlayerLeaveIsland(idPlayer,c.islandId);
    }

}