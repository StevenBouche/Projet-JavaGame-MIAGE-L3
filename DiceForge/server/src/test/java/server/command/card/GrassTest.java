package server.command.card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.CardToAction;
import server.command.Command;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import server.game.INotifyEvent;
import share.cards.Cards;
import share.choice.Choice3GoldFor4Glory;
import share.eventclientserver.Events;
import share.face.FactoryFace;
import share.ressource.TypeRessource;

import java.util.UUID;

import static org.mockito.Matchers.any;

class GrassTest {


    static CommandManager managerCmd;
    static GameManager manager;
    static UUID idPlayer;
    static Grass cmd;
    static Choice3GoldFor4Glory ch;

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
        this.createCmdAncient();
    }

    private void createCmdAncient() {
        cmd = new Grass(manager,managerCmd,idPlayer);
        cmd = Mockito.spy(cmd);
    }

    @Test
    void testIsLinkWithCard(){
        Command<IGameManager> command = CardToAction.getActionCard(Cards.GRASS,manager,managerCmd,idPlayer);
        Assertions.assertNotNull(command);
        Assertions.assertEquals(cmd.getName(),command.getName());
    }

    @Test
    void onExecute() {
        cmd.trigger();
        Assertions.assertEquals(3,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD));
        Assertions.assertEquals(3,manager.getGame().getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.LUNAR));
    }
}