package server.command.card;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.cards.Card;
import share.cards.Cards;
import share.cards.effects.TypeEffectBasique;
import share.eventclientserver.Events;
import share.face.FactoryFace;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;

class HandleActionCardTest {

    private UUID idPlayer;
    private GameManager manager;
    private CommandManager managerCmd;
    private HandleActionCard cmd;


    @BeforeEach
    void init(){
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
        this.createCmdHandleAction();
    }

    @Test
    void onExecuteImmediateCard() {
        int i = 0;
        for(Cards c : Cards.values()){
            if(c.typeEffect == TypeEffectBasique.IMMEDIATE){
                i++;
                cmd.c.typeEffect = c.typeEffect;
                cmd.c.name = c.name;
                cmd.trigger();
            }
        }
        Mockito.verify(managerCmd,times(i)).triggerCommandActionCard(any(UUID.class),any(Card.class));

    }

    private void createCmdHandleAction() {
        cmd = new HandleActionCard(manager,managerCmd,idPlayer,new Card());
        cmd = Mockito.spy(cmd);
    //    Mockito.doNothing().when(cmd).waitingDecision();
        Mockito.doNothing().when(cmd).notifyDecision();
    }
}