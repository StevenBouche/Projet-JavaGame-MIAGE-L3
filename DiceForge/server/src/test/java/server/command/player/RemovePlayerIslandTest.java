package server.command.player;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.command.CommandManager;
import server.game.GameManager;
import share.temple.IslandEnum;

import java.util.UUID;

class RemovePlayerIslandTest {

    GameManager game;
    CommandManager command;

    @Test
    void onExecute() {
        game = Mockito.mock(GameManager.class);
        command = Mockito.mock(CommandManager.class);
        UUID id = UUID.randomUUID();
        RemovePlayerIsland r = new RemovePlayerIsland(game,command,id, IslandEnum.ISLAND1);
        r.trigger();
        Mockito.verify(command,Mockito.times(1)).rollDiceMajorCommand(id);
    }
}