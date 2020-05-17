package server.statistics;

import server.command.inter.ICommandChoice;
import share.cards.Cards;
import share.face.Face;
import share.ressource.TypeRessource;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatisticsPlayer {

    public int id;
    public String version;
    public UUID idSession;

    public Map<Integer, Integer> rankingCountPlayer;
    public long nbTimeReflexionPlayers = 0;
    public Integer nbRollDiceMinorPlayers = 0;
    public Integer nbRollDiceMajorPlayers = 0;
    public Map<TypeRessource, StatisticsRessource> statsRessource;
    public Integer nbForgeFacePlayers = 0;
    public Integer nbActionForge = 0;
    public Integer nbActionExploit = 0;
    public Integer nbActionMoreTurn = 0;
    public Integer nbActionCardRenfort = 0;
    public Integer nbActionCardImmediate = 0;
    public Integer nbActionCardHammer = 0;
    public Map<Class<? extends Face>, Integer> nbBuyFacePlayers;
    public Map<Cards, Integer> nbBuyCardsPlayer;
    public Map<Class<? extends ICommandChoice>,Long> nbTimeReflexionCommandPlayers;
    public Map<Class<? extends ICommandChoice>,Integer> nbTimeCommandPlayers;

    public StatisticsPlayer(){

    }

    public StatisticsPlayer(int id, UUID idSession, String version){
        this.id = id;
        this.idSession = idSession;
        this.version = version;
        nbBuyFacePlayers= new HashMap<>();
        nbBuyCardsPlayer= new HashMap<>();
        nbTimeReflexionCommandPlayers = new HashMap<>();
        nbTimeCommandPlayers = new HashMap<>();
        statsRessource = new HashMap<>();
        for(TypeRessource type : TypeRessource.values()) {
            if(type == TypeRessource.GOLD) this.statsRessource.put(type,new StatisticsRessourceGold());
            else if(type==TypeRessource.GLORY) this.statsRessource.put(type,new StatisticsRessourceGlory());
            else statsRessource.put(type,new StatisticsRessource(type));
        }
        rankingCountPlayer = new HashMap<>();
    }

    public void merge(StatisticsPlayer statisticsPlayer) {

        if(this.id == statisticsPlayer.id && this.version.equals(statisticsPlayer.version)){

            nbTimeReflexionPlayers += statisticsPlayer.nbTimeReflexionPlayers;
            nbRollDiceMinorPlayers += statisticsPlayer.nbRollDiceMinorPlayers;
            nbRollDiceMajorPlayers += statisticsPlayer.nbRollDiceMajorPlayers;
            nbForgeFacePlayers += statisticsPlayer.nbForgeFacePlayers;
            nbActionForge += statisticsPlayer.nbActionForge;
            nbActionExploit += statisticsPlayer.nbActionExploit;
            nbActionMoreTurn += statisticsPlayer.nbActionMoreTurn;
            nbActionCardRenfort += statisticsPlayer.nbActionCardRenfort;
            nbActionCardImmediate += statisticsPlayer.nbActionCardImmediate;
            nbActionCardHammer += statisticsPlayer.nbActionCardHammer;
            mergeMap(rankingCountPlayer,statisticsPlayer.rankingCountPlayer);
            for(TypeRessource type : statisticsPlayer.statsRessource.keySet()) this.statsRessource.get(type).merge(statisticsPlayer.statsRessource.get(type));
            mergeMap(nbBuyFacePlayers,statisticsPlayer.nbBuyFacePlayers);
            mergeMap(nbBuyCardsPlayer,statisticsPlayer.nbBuyCardsPlayer);
            mergeMapLong(nbTimeReflexionCommandPlayers,statisticsPlayer.nbTimeReflexionCommandPlayers);
            mergeMap(nbTimeCommandPlayers,statisticsPlayer.nbTimeCommandPlayers);

        }

    }

    private <T> void mergeMap(Map<T, Integer> map, Map<T, Integer> mapO){
        for(T elem : mapO.keySet()){
            if(map.containsKey(elem)) map.put(elem,map.get(elem)+mapO.get(elem));
            else map.put(elem,mapO.get(elem));
        }
    }

    private <T> void mergeMapLong(Map<T, Long> map, Map<T, Long> mapO){
        for(T elem : mapO.keySet()){
            if(map.containsKey(elem)) map.put(elem,map.get(elem)+mapO.get(elem));
            else map.put(elem,mapO.get(elem));
        }
    }
}
