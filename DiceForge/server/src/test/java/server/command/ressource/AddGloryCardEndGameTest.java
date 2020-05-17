package server.command.ressource;

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
import share.face.FactoryFace;
import share.ressource.TypeRessource;

import java.util.List;
import java.util.UUID;

import static org.mockito.Matchers.any;

class AddGloryCardEndGameTest {

    static CommandManager managerCmd;
    static GameManager manager;
    static UUID idPlayer;
    static UUID idPlayer2;
    static AddGloryCardEndGame cmd;

    @BeforeEach
    void initTest(){
        FactoryFace.resetInstance();
        idPlayer = UUID.randomUUID();
        idPlayer2 = UUID.randomUUID();
        INotifyEvent callback = Mockito.mock(INotifyEvent.class);
        manager = new GameManager(callback);
        manager = Mockito.spy(manager);
        managerCmd = (CommandManager) Mockito.spy(manager.commandManager);
        manager.notifyConnectionPlayer(idPlayer, "",0);
        manager.notifyConnectionPlayer(idPlayer2, "",0);
        Mockito.doNothing().when(managerCmd).onEndExecute(any(Command.class));
        Mockito.doNothing().when(managerCmd).triggerCommand(any(Command.class));
    }

    @Test
    void onExecute() {
        List<Card> c1 = FactoryCard.createXInstanceOfCard(Cards.HYDRA,4);
        manager.getGame().getPlayer(idPlayer).getInventory().addCard(c1.get(0).typeEffect,c1.get(0));
        manager.getGame().getPlayer(idPlayer).getInventory().addCard(c1.get(1).typeEffect,c1.get(1));
        manager.getGame().getPlayer(idPlayer2).getInventory().addCard(c1.get(2).typeEffect,c1.get(2));
        manager.getGame().getPlayer(idPlayer2).getInventory().addCard(c1.get(3).typeEffect,c1.get(3));
        cmd = new AddGloryCardEndGame(manager,managerCmd);
        cmd.trigger();
        Mockito.verify(managerCmd,Mockito.times(2)).addRessourcePlayerCommand(idPlayer, TypeRessource.GLORY,26);
        Mockito.verify(managerCmd,Mockito.times(2)).addRessourcePlayerCommand(idPlayer2, TypeRessource.GLORY,26);
    }

}