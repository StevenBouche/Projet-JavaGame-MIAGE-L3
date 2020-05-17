package server.command;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.CardToAction;
import server.command.CommandManager;
import server.command.card.Doe;
import server.game.GameManager;
import share.cards.Cards;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

class CardToActionTest {

    GameManager gameManager;
    CommandManager commandManager;

    @BeforeEach
    void init(){
        commandManager = Mockito.mock(CommandManager.class);
        gameManager = Mockito.mock(GameManager.class);
    }

    @Test
    void getActionCard() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Doe d = (Doe) CardToAction.getActionCard(Cards.DOE,gameManager,commandManager, UUID.randomUUID());
        Assertions.assertEquals("DOE_COMMAND",d.getName());
        Assertions.assertEquals(CardToAction.DOE_ACTION.cmd.getName(),d.getClass().getName());
    }
}