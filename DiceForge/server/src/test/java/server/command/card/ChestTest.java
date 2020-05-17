package server.command.card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.config.ConfigPlayer;
import share.eventclientserver.Events;
import share.face.FactoryFace;
import share.ressource.TypeRessource;

import java.util.UUID;

import static org.mockito.Matchers.any;

class ChestTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;

    @Test
    void onExecute() {
        FactoryFace.resetInstance();
        idPlayer = UUID.randomUUID();
        manager = new GameManager(Mockito.mock(INotifyEvent.class));
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "str",0);
        Mockito.doNothing().when(manager).sendEventToClient(any(UUID.class), any(Events.class), any(Object.class));
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
        Chest ch = new Chest(manager,managerCmd,idPlayer);
        ch.trigger();
        Assertions.assertEquals(ConfigPlayer.GOLD_PLAYER_MAX_INIT+4,manager.getGame().getPlayer(idPlayer).getInventory().getValueMaxRessource(TypeRessource.GOLD));
        Assertions.assertEquals(ConfigPlayer.SOLARY_PLAYER_MAX_INIT+4,manager.getGame().getPlayer(idPlayer).getInventory().getValueMaxRessource(TypeRessource.SOLAR));
        Assertions.assertEquals(ConfigPlayer.LUNAR_PLAYER_MAX_INIT+4,manager.getGame().getPlayer(idPlayer).getInventory().getValueMaxRessource(TypeRessource.LUNAR));
    }
}