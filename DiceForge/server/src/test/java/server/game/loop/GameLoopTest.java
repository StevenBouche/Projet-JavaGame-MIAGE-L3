package server.game.loop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.Command;
import server.command.CommandManager;
import server.command.player.SetPlayerActif;
import server.game.GameManager;
import server.game.INotifyEvent;
import share.face.FactoryFace;
import share.game.GameState;
import share.ressource.TypeRessource;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Matchers.any;

class GameLoopTest {

    GameManager gameManager;
    CommandManager commandManager;
    GameLoop gameLoop;

    UUID idPlayer;
    UUID idPlayer2;
    UUID idPlayer3;
    UUID idPlayer4;

    @BeforeEach
    void init(){
        FactoryFace.resetInstance();
        gameManager = new GameManager(Mockito.mock(INotifyEvent.class));
        gameManager = Mockito.spy(gameManager);
        commandManager = (CommandManager) Mockito.spy(gameManager.commandManager);
        gameLoop = new GameLoop(gameManager,commandManager,gameManager.stats);
        gameLoop = Mockito.spy(gameLoop);
        Mockito.doNothing().when(commandManager).triggerCommand(any(Command.class));
        Mockito.doNothing().when(commandManager).onEndExecute(any(Command.class));
    }

    void initPlayer(){
        idPlayer = UUID.randomUUID();
        idPlayer2 = UUID.randomUUID();
        idPlayer3 = UUID.randomUUID();
        idPlayer4 = UUID.randomUUID();
        gameManager.notifyConnectionPlayer(idPlayer, "str",0);
        gameManager.notifyConnectionPlayer(idPlayer2, "str",0);
        gameManager.notifyConnectionPlayer(idPlayer3, "str",0);
        gameManager.notifyConnectionPlayer(idPlayer4, "str",0);
        SetPlayerActif c = new SetPlayerActif(gameManager,commandManager);
        c.trigger();
    }

    @Test
    void executeGameStop() {
        Mockito.doNothing().when(gameManager).stopGame();
        gameManager.getGame().state = GameState.STOP;
        gameLoop.execute(gameManager.getGame());
        Mockito.verify(gameManager,Mockito.times(1)).stopGame();
    }

    @Test
    void executeFirstExec() {
        Mockito.doNothing().when(commandManager).addRessourcePlayerCommand(any(UUID.class), any(TypeRessource.class), any(Integer.class));
        Mockito.doNothing().when(gameManager).gameFinish();
        initPlayer();
        gameManager.getGame().nbManche = gameManager.getGame().nbMancheMax+1;
        Assertions.assertTrue(gameLoop.firstExec);
        gameManager.getGame().state = GameState.START;
        gameLoop.execute(gameManager.getGame());
        Assertions.assertFalse(gameLoop.firstExec);
//        Mockito.verify(gameManager,Mockito.times(1)).gameFinish();
        List<UUID> lp = gameManager.getGame().getIdPlayers();
        Mockito.verify(commandManager,Mockito.times(1)).triggerCommandAddGoldPlayerStartGame();
    }

    @Test
    void executeGame() {
        Mockito.doNothing().when(commandManager).addRessourcePlayerCommand(any(UUID.class), any(TypeRessource.class), any(Integer.class));
        Mockito.doNothing().when(gameManager).gameFinish();
        initPlayer();
        Assertions.assertEquals(1, gameManager.getGame().nbManche);
        Assertions.assertEquals(1, gameManager.getGame().cptNbPlay);
        gameManager.getGame().state = GameState.START;
        gameLoop.execute(gameManager.getGame());
   //     Mockito.verify(gameManager,Mockito.times(1)).gameFinish();
        Assertions.assertEquals(gameManager.getGame().nbMancheMax+1, gameManager.getGame().nbManche);
        Assertions.assertEquals(1, gameManager.getGame().cptNbPlay);
        int tot = (gameManager.getGame().nbMancheMax*gameManager.getNbPlayer())+gameManager.getGame().nbMancheMax;
        Mockito.verify(gameLoop,Mockito.times(tot)).execute(gameManager.getGame());
    }

    @Test
    void executeLoop() {
        Mockito.doNothing().when(commandManager).addRessourcePlayerCommand(any(UUID.class), any(TypeRessource.class), any(Integer.class));
        Mockito.doNothing().when(gameManager).gameFinish();
        initPlayer();
        gameManager.getGame().state = GameState.START;
        gameLoop.execute(gameManager.getGame());
        int tot = gameManager.getGame().nbMancheMax*gameManager.getNbPlayer();
        Mockito.verify(commandManager,Mockito.times(tot)).rollDiceMajorAllPlayerCommand();
        Mockito.verify(commandManager,Mockito.times(tot)).triggerCommandSetPlayerActif();
        Mockito.verify(commandManager,Mockito.times(tot)).triggerCommandCallRenfort();
        Mockito.verify(commandManager,Mockito.times(tot)).triggerCommandActionTurn();
        Mockito.verify(commandManager,Mockito.times(tot)).triggerCommandOneMoreTurn();
    }

}