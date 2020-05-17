package server.command.turn;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.command.turn.HandleActionTurn;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.cards.Card;
import share.choice.ChoiceFaceOnDice;
import share.choice.ChoiceNothingForgeExploit;
import share.choice.EnumTypeChoice;
import share.config.ConfigPlayer;
import share.dice.Dice;
import share.eventclientserver.Events;
import share.exeption.CaseOfPoolForgeOutOfBound;
import share.face.Face;
import share.face.FactoryFace;
import share.forge.Pool;
import share.forge.PoolEnum;
import share.ressource.TypeRessource;
import share.temple.IslandEnum;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

class HandleActionTurnTest {

    CommandManager managerCmd;
    GameManager manager;
    UUID idPlayer;
    ChoiceNothingForgeExploit ch;
    HandleActionTurn cmd;

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
        setMaxRessourcePlayer();
        manager.getGame().currentPlayer = manager.getGame().getPlayer(idPlayer);
    }

    private void setMaxRessourcePlayer(){
        manager.getGame().getPlayer(idPlayer).getInventory().addRessource(TypeRessource.GOLD, ConfigPlayer.GOLD_PLAYER_MAX_INIT);
        manager.getGame().getPlayer(idPlayer).getInventory().addRessource(TypeRessource.LUNAR, ConfigPlayer.LUNAR_PLAYER_MAX_INIT);
        manager.getGame().getPlayer(idPlayer).getInventory().addRessource(TypeRessource.SOLAR, ConfigPlayer.SOLARY_PLAYER_MAX_INIT);
    }

    @Test
    void testActionForge() throws CaseOfPoolForgeOutOfBound {
        ArgumentCaptor<Face> argument = ArgumentCaptor.forClass(Face.class);
        setDataChoiceNothingForgeExploit();
        setChoicePlayerForge();
        cmd = new HandleActionTurn(manager,managerCmd);
        cmd.notifyChoice(idPlayer,ch);
        cmd.trigger();
        for(ChoiceFaceOnDice choice : ch.listChoiceForge){
            Face f = manager.getGame().getFaceFromPool(choice.choicePool,choice.choiceIndexPool);
            Mockito.verify(managerCmd,Mockito.times(1)).triggerCommandForgeFaceOnDice(eq(idPlayer),argument.capture(),eq(choice.choiceIndexDice),eq(choice.choiceIndexFaceDice));
            Assertions.assertEquals(f.toString(),argument.getValue().toString());
        }
        Mockito.verify(managerCmd,Mockito.times(ch.listChoiceForge.size())).removeRessourcePlayerCommand(eq(idPlayer),eq(TypeRessource.GOLD),eq(2));
    }

    @Test
    void testActionExploit() {
        ArgumentCaptor<Card> argument = ArgumentCaptor.forClass(Card.class);
        setDataChoiceNothingForgeExploit();
        setChoicePlayerExploit();
        cmd = new HandleActionTurn(manager,managerCmd);
        cmd.notifyChoice(idPlayer,ch);
        cmd.trigger();
        Card c = manager.getGame().temple.getIsland(ch.choiceIsland).listcard.get(ch.choiceIndexIslandCase);
        for(TypeRessource type : c.getCost().keySet()) {
            Mockito.verify(managerCmd).removeRessourcePlayerCommand(eq(idPlayer),eq(type),eq(c.getCost().get(type)));
        }
        Assertions.assertEquals(c.toString(),manager.getGame().getPlayer(idPlayer).getInventory().getListCards(c.typeEffect).get(0).toString());
        Mockito.verify(managerCmd).handleActionCardWhenBuy(eq(idPlayer),argument.capture());
        Assertions.assertEquals(c.toString(),argument.getValue().toString());
    }

    private void setDataChoiceNothingForgeExploit(){
        Map<Integer, Pool> pools =  manager.getGame().getPoolsForge(); // recup les pools de la force
        UUID idcurrent = manager.getGame().getPlayer(idPlayer).getId(); // recup l'id du player current
        Dice d1 = manager.getGame().getPlayer(idPlayer).getInventory().getDice(0); // recup dice 1
        Dice d2 = manager.getGame().getPlayer(idPlayer).getInventory().getDice(1); // recup dice 2
        ch = new ChoiceNothingForgeExploit(d1,d2,pools
                ,manager.getGame().currentPlayer.getInventory().getRessources()
                ,manager.getGame().getIslandsOfTemple()
        );
    }

    private void setChoicePlayerForge(){
        ChoiceFaceOnDice choice = new ChoiceFaceOnDice();
        ChoiceFaceOnDice choice2 = new ChoiceFaceOnDice();
        choice.choicePool = PoolEnum.getCostsPool().get(0);
        choice.choiceIndexDice = 0;
        choice.choiceIndexFaceDice = 0;
        choice.choiceIndexPool = 0;
        choice2.choicePool = PoolEnum.getCostsPool().get(0);
        choice2.choiceIndexDice = 1;
        choice2.choiceIndexFaceDice = 0;
        choice2.choiceIndexPool = 0;
        ch.listChoiceForge.add(choice);
        ch.listChoiceForge.add(choice2);
        ch.choice = EnumTypeChoice.FORGE;
    }

    private void setChoicePlayerExploit(){
        ch.choiceIsland = IslandEnum.ISLAND1;
        ch.choiceIndexIslandCase = 0;
        ch.choice = EnumTypeChoice.EXPLOIT;
    }

    private void setChoicePlayerNothing(){
        ch.choice = EnumTypeChoice.NOTHING;
    }


}