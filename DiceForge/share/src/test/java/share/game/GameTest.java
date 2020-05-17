package share.game;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import share.cards.Card;
import share.cards.effects.TypeEffectBasique;
import share.exeption.CaseOfIslandTempleOutOfBound;
import share.exeption.CaseOfPoolForgeOutOfBound;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.FaceSpecialEnum;
import share.face.FactoryFace;
import share.player.Player;
import share.ressource.TypeRessource;
import share.temple.Island;
import share.temple.IslandEnum;

import java.util.*;

class GameTest {

    static Game game;
    static Player player;
    static Player player2;
    static Player player3;
    static UUID id;
    static UUID id2;
    static UUID id3;
    static Map<UUID,Player> listPlayers;
    static ArrayList<UUID> listId;

    @BeforeEach
    void initTest() throws InstanceFaceOutOfBoundException, CaseOfIslandTempleOutOfBound {
        FactoryFace.resetInstance();
        listId = new ArrayList<>();
        listPlayers = new HashMap<>();
        game = new Game();
        id = UUID.randomUUID();
        id2 = UUID.randomUUID();
        id3 = UUID.randomUUID();
        player = new Player(id,"");
        player2 = new Player(id2,"");
        player3 = new Player(id3,"");
        game.state = GameState.WAITING_PLAYER;
        game.nbManche = 7;
        game.cptNbPlay = 2;
        game.currentPlayer = player;
        game.addPlayer(player,id);
        listPlayers.put(id,player);
        listId.add(id);
        listPlayers.put(id2,player2);
        listId.add(id2);
        FactoryFace.resetInstance();
    }

    @Test
    void reset() {
        game.reset();
        Assertions.assertEquals(GameState.INIT,game.state);
        Assertions.assertEquals(1,game.nbManche);
        Assertions.assertEquals(1,game.cptNbPlay);
        Assertions.assertEquals(null,game.currentPlayer);
    }

    @Test
    void getNbPlayer() {
        Assertions.assertEquals(1,game.getNbPlayer());
    }

    @Test
    void getPlayer() {
        Assertions.assertEquals(player,game.getPlayer(id));
    }

    @Test
    void getPlayers() {
        game.addPlayer(player2,id2);
        Assertions.assertEquals(listPlayers,game.getPlayers());
    }

    @Test
    void getIdPlayers() {
        game.addPlayer(player2,id2);
        Assertions.assertEquals(listId,game.getIdPlayers());
    }

    @Test
    void addPlayer() {
        game.addPlayer(player2,id2);
        Assertions.assertEquals(2,game.getNbPlayer());
        Assertions.assertEquals(listId,game.getIdPlayers());
        Assertions.assertEquals(listPlayers,game.getPlayers());
    }

    @Test
    void removePlayer() {
        game.addPlayer(player2,id2);
        game.removePlayer(id);
        listId.remove(0);
        listPlayers.remove(id,player);
        Assertions.assertEquals(1,game.getNbPlayer());
        Assertions.assertEquals(listId,game.getIdPlayers());
        Assertions.assertEquals(listPlayers,game.getPlayers());
    }

    @Test
    void stop() {
        game.stop();
        Assertions.assertEquals(GameState.STOP,game.state);
    }

    @Test
    void getForge() {                                    //Lors de la création il est cense avoir 7 pools dans la Forge...
        Assertions.assertEquals(7,game.getForge().getListPools().size());
    }

    @Test
    void getCardFromIsland() {
        Assertions.assertEquals(game.getIslandsOfTemple().get(IslandEnum.ISLAND5).listcard.get(2),game.getCardFromIsland(IslandEnum.ISLAND5,2));
    }

    @Test
    void removeRessourcesPlayer() {
        Map<TypeRessource,Integer> cost;
        cost = new HashMap<>();
        cost.put(TypeRessource.GOLD,3);
        cost.put(TypeRessource.LUNAR,4);
        cost.put(TypeRessource.SOLAR,1);
        game.addRessourcePlayer(id,TypeRessource.GOLD,3);
        game.addRessourcePlayer(id,TypeRessource.LUNAR,4);
        game.addRessourcePlayer(id,TypeRessource.SOLAR,1);
        game.removeRessourcesPlayer(cost,id);
        Assertions.assertEquals(0,game.getPlayer(id).getInventory().getRessources().get(TypeRessource.LUNAR).value);
        Assertions.assertEquals(0,game.getPlayer(id).getInventory().getRessources().get(TypeRessource.GOLD).value);
        Assertions.assertEquals(0,game.getPlayer(id).getInventory().getRessources().get(TypeRessource.SOLAR).value);
    }

    @Test
    void removeRessourcePlayer() {
        game.addRessourcePlayer(id,TypeRessource.LUNAR,5);
        game.removeRessourcePlayer(id,TypeRessource.LUNAR,3);
        Assertions.assertEquals(2,game.getPlayer(id).getInventory().getRessources().get(TypeRessource.LUNAR).value);
    }

    @Test
    void addRessourcePlayer() {
        game.addRessourcePlayer(id,TypeRessource.LUNAR,5);
        Assertions.assertEquals(5,game.getPlayer(id).getInventory().getRessources().get(TypeRessource.LUNAR).value);
    }

    @Test
    void addCardPlayer() {
        game.addCardPlayer(game.temple.getIsland(IslandEnum.ISLAND1).listcard.get(0),id);
        Assertions.assertEquals(game.temple.getIsland(IslandEnum.ISLAND1).listcard.get(0),game.getPlayer(id).getInventory().getCardsByTypeEffect(game.temple.getIsland(IslandEnum.ISLAND1).listcard.get(0).typeEffect).get(0));
    }

    @Test
    void removeFaceOfDicePlayer() throws CaseOfPoolForgeOutOfBound {
        game.addFaceOfDicePlayer(id,1,3,game.getForge().getFaceOnPool(3,3));
        game.removeFaceOfDicePlayer(id,1,4);
        Assertions.assertEquals(null,game.getPlayer(id).getInventory().getDice(1).getFace(4));
    }

    @Test
    void addFaceOfDicePlayer() throws CaseOfPoolForgeOutOfBound {
        game.addFaceOfDicePlayer(id,1,3,game.getForge().getFaceOnPool(3,3));
        Assertions.assertEquals(game.getForge().getFaceOnPool(3,3),game.getPlayer(id).getInventory().getDice(1).getFace(3));
    }

    @Test
    void addHistoryFacePlayer() throws CaseOfPoolForgeOutOfBound {
        game.addHistoryFacePlayer(id,game.getForge().getFaceOnPool(3,2));
        Assertions.assertEquals(game.getForge().getFaceOnPool(3,2),game.getPlayer(id).getInventory().getHistoryFace().get(0));
    }

    @Test
    void getPlayerSortByTypeRessource() {
        game.addPlayer(player2,id2);
        game.addPlayer(player3,id3);
        game.getPlayer(id).getInventory().addRessource(TypeRessource.GLORY,1);
        game.getPlayer(id2).getInventory().addRessource(TypeRessource.GLORY,2);
        game.getPlayer(id3).getInventory().addRessource(TypeRessource.GLORY,3);
        List<Player> listPlayer;
        listPlayer = game.getPlayerSortByTypeRessource(TypeRessource.GLORY);
        Assertions.assertEquals(player3,listPlayer.get(0));
        Assertions.assertEquals(player2,listPlayer.get(1));
        Assertions.assertEquals(player,listPlayer.get(2));
    }

    @Test
    void getCardWithEffectOfPlayer() {
        Assertions.assertEquals(game.getPlayer(id).getInventory().getListCards(TypeEffectBasique.IMMEDIATE),game.getCardWithEffectOfPlayer(id,TypeEffectBasique.IMMEDIATE));
        Assertions.assertEquals(game.getPlayer(id).getInventory().getListCards(TypeEffectBasique.REINFORCEMENT),game.getCardWithEffectOfPlayer(id,TypeEffectBasique.REINFORCEMENT));
        Assertions.assertEquals(game.getPlayer(id).getInventory().getListCards(TypeEffectBasique.AUTOMATIC),game.getCardWithEffectOfPlayer(id,TypeEffectBasique.AUTOMATIC));
        Assertions.assertEquals(game.getPlayer(id).getInventory().getListCards(TypeEffectBasique.NONE),game.getCardWithEffectOfPlayer(id,TypeEffectBasique.NONE));
    }

    @Test
    void getFaceFromPool() throws CaseOfPoolForgeOutOfBound {
        Assertions.assertEquals(game.getForge().getFaceOnPool(2,2),game.getFaceFromPool(2,2));
    }

    @Test
    void addHammerPlayer() {
        game.addHammerPlayer(id);
        Assertions.assertEquals(false,game.getPlayer(id).getInventory().getHammer().isEmpty());
    }

    @Test
    void getIdOfCurrentPlayer() {
        Assertions.assertEquals(id,game.getIdOfCurrentPlayer());
    }

    @Test
    void getDiceOfCurrentPlayer() {
        Assertions.assertEquals(game.getPlayer(id).getInventory().getDice(0),game.getDiceOfCurrentPlayer(0));
    }

    @Test
    void getRessourcesOfCurrentPlayer() {
        Assertions.assertEquals(game.getPlayer(id).getInventory().getRessources(),game.getRessourcesOfCurrentPlayer());
    }

    @Test
    void getValueGoldPlayer() {
        game.addRessourcePlayer(id,TypeRessource.GOLD,5);
        Assertions.assertEquals(5,game.getValueGoldPlayer(id));
    }

    @Test
    void extendRessourcesPlayer() {                
        game.extendRessourcesPlayer(id,4);
        Assertions.assertEquals(16,game.getPlayer(id).getInventory().getValueMaxRessource(TypeRessource.GOLD));
        Assertions.assertEquals(10,game.getPlayer(id).getInventory().getValueMaxRessource(TypeRessource.SOLAR));
        Assertions.assertEquals(10,game.getPlayer(id).getInventory().getValueMaxRessource(TypeRessource.LUNAR));
    }

    @Test
    void getCurrentIdHammerPlayer() {
        Assertions.assertEquals(game.getPlayer(id).getInventory().getCurrentIdHammer(),game.getCurrentIdHammerPlayer(id));
    }

    @Test
    void getCurrentHammerPlayer() {
        Assertions.assertEquals(game.getPlayer(id).getInventory().getCurrentHammer(),game.getCurrentHammerPlayer(id));
    }

    @Test
    void ifPlayerHaveHammer() {
        Assertions.assertEquals(false,game.ifPlayerHaveHammer(id));
        game.addHammerPlayer(id);
        Assertions.assertEquals(true,game.ifPlayerHaveHammer(id));
    }

    @Test
    void setCurrentIdHammerPlayer() {
        game.setCurrentIdHammerPlayer(id,0);
        Assertions.assertEquals(0,game.getPlayer(id).getInventory().getCurrentIdHammer());
    }

    @Test
    void getFaceSpecial() {
        Assertions.assertEquals(game.getForge().getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3).name,game.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3).name);
        Assertions.assertEquals(game.getForge().getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3).faceEnum,game.getFaceSpecial(FaceSpecialEnum.FACE_MULTIPLE_3).faceEnum);
}



    @Test
    void getFaceOnDicePlayer() {
        Assertions.assertEquals(game.getPlayer(id).getInventory().getDice(0).mapFaces.get(0),game.getFaceOnDicePlayer(id,0,0));
        Assertions.assertEquals(game.getPlayer(id).getInventory().getDice(0).mapFaces.get(2),game.getFaceOnDicePlayer(id,0,2));
        Assertions.assertEquals(game.getPlayer(id).getInventory().getDice(0).mapFaces.get(3),game.getFaceOnDicePlayer(id,0,3));
        Assertions.assertEquals(game.getPlayer(id).getInventory().getDice(0).mapFaces.get(5),game.getFaceOnDicePlayer(id,0,5));
    }

    @Test
    void getCostOfPoolForge() {
        Assertions.assertEquals(game.getForge().getListPools().get(3).getCost(),game.getCostOfPoolForge(3));
        Assertions.assertEquals(game.getForge().getListPools().get(5).getCost(),game.getCostOfPoolForge(5));
        Assertions.assertEquals(game.getForge().getListPools().get(8).getCost(),game.getCostOfPoolForge(8));
    }



    @Test
    void getValueRessourcePlayer() {
        game.addRessourcePlayer(id,TypeRessource.GLORY,100);
        Assertions.assertEquals(100,game.getPlayer(id).getInventory().getRessources().get(TypeRessource.GLORY).value);
    }

    @Test
    void getLastRollsPlayer() {
        Assertions.assertEquals(game.getPlayer(id).getInventory().getLastRoll(),game.getLastRollsPlayer(id));
    }

    @Test
    void ifPlayerisAlreadyOnIslandCard() {
        Card card;
        card = game.getCardFromIsland(IslandEnum.ISLAND3,0);
        Assertions.assertEquals(true,game.ifPlayerisAlreadyOnIslandCard(id,card));
    }



    @Test
    void addPlayerOnIslandOfTemple() {
        game.addPlayerOnIslandOfTemple(id,IslandEnum.ISLAND1);
        Assertions.assertEquals(id,game.getIslandsOfTemple().get(IslandEnum.ISLAND1).player);
    }

    @Test
    void getIslandsOfTemple() {
        Assertions.assertEquals(7,game.getIslandsOfTemple().keySet().size());
        Assertions.assertEquals(true,game.getIslandsOfTemple().keySet().contains(IslandEnum.ISLAND1));
        Assertions.assertEquals(true,game.getIslandsOfTemple().keySet().contains(IslandEnum.ISLAND2));
        Assertions.assertEquals(true,game.getIslandsOfTemple().keySet().contains(IslandEnum.ISLAND3));
        Assertions.assertEquals(true,game.getIslandsOfTemple().keySet().contains(IslandEnum.ISLAND4));
        Assertions.assertEquals(true,game.getIslandsOfTemple().keySet().contains(IslandEnum.ISLAND5));
        Assertions.assertEquals(true,game.getIslandsOfTemple().keySet().contains(IslandEnum.ISLAND6));
        Assertions.assertEquals(true,game.getIslandsOfTemple().keySet().contains(IslandEnum.ISLAND7));
    }

}