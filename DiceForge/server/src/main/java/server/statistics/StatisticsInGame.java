package server.statistics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import server.command.inter.ICommandChoice;
import share.cards.Cards;
import share.face.Face;
import share.player.Player;
import share.ressource.TypeRessource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StatisticsInGame {


    public int nbManche;
    public int nbTurn;
    public int nbGame;

    int id;
    public int getId(){
        return this.id;
    }

    public void incNbManche(){
        nbManche++;
    }

    public void incNbTurn(){
        nbTurn++;
    }

    public void incNbGame(){
        nbGame++;
    }

    private Map<UUID, StatisticsPlayer> statsPlayers;
    public Map<Integer, StatisticsPlayer> statsPlayersIdLauncher;

    private <T> void incValueMapObject(Map<T, Integer> map, T oKey, UUID idPlayer){
        map.putIfAbsent(oKey,0);
        map.put(oKey,map.get(oKey)+1);
    }

    private <T> void incValueMapObject(int value, Map<T, Integer> map, T oKey, UUID idPlayer){
        map.putIfAbsent(oKey,0);
        map.put(oKey,map.get(oKey)+value);
    }

    public void addPlayer(UUID id, String str, int idP) {
        StatisticsPlayer s = new StatisticsPlayer(idP,id,str);
        statsPlayers.put(id, s);
        statsPlayersIdLauncher.put(idP, s);
    }

    public void incNbTimeReflexionPlayers(UUID idPlayer, ICommandChoice c, long time){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.nbTimeReflexionPlayers+=time;
        s.nbTimeReflexionCommandPlayers.putIfAbsent(c.getClass(), (long) 0);
        s.nbTimeReflexionCommandPlayers.put(c.getClass(),s.nbTimeReflexionCommandPlayers.get(c.getClass())+time);
        s.nbTimeCommandPlayers.putIfAbsent(c.getClass(), 0);
        s.nbTimeCommandPlayers.put(c.getClass(),s.nbTimeCommandPlayers.get(c.getClass())+1);
    }

    public void incNbRollDiceMinorPlayer(UUID idPlayer){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.nbRollDiceMinorPlayers+=1;
    }

    public void incNbRollDiceMajorPlayer(UUID idPlayer){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.nbRollDiceMajorPlayers+=1;
    }



    public <T extends Face> void incNbBuyFacePlayers(UUID idPlayer, T f){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        incValueMapObject(s.nbBuyFacePlayers,f.getClass(),idPlayer);
    }

    public void incNbBuyCardsPlayer(UUID idPlayer, Cards c){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        incValueMapObject(s.nbBuyCardsPlayer,c,idPlayer);
    }

    public void incNbForgeFace(UUID idPlayer){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.nbForgeFacePlayers+=1;
    }

    public void incNbActionForge(UUID idPlayer){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.nbActionForge+=1;
    }

    public void incNbActionExploit(UUID idPlayer){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.nbActionExploit+=1;
    }

    public void incNbActionMoreTurn(UUID idPlayer){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.nbActionMoreTurn+=1;
    }

    public void incNbActionCardRenfort(UUID idPlayer){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.nbActionCardRenfort+=1;
    }

    public void incNbActionCardImmediate(UUID idPlayer){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.nbActionCardImmediate+=1;
    }

    public void incNbActionCardHammer(UUID idPlayer){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.nbActionCardHammer+=1;
    }

    public void addRankOfOneGame(List<Player> listResultGame) {
        int i = 1;
        for(Player p : listResultGame){
            StatisticsPlayer s = statsPlayers.get(p.getId());
            incValueMapObject(s.rankingCountPlayer,i,p.getId());
            i++;
        }
    }

    private  List<String> result;
    /**
     * Result list.
     *
     * @return the list
     */
    public List<String> getResult(){ return result; }


    public StatisticsInGame(){
        //players = new HashMap<>();
        statsPlayers = new HashMap<>();
        statsPlayersIdLauncher = new HashMap<>();
        result = new ArrayList<>();
    }

    public String toJSONString(){
        ObjectMapper o = new ObjectMapper();
        try {
            return o.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String toJSONStringForm(){
        String str = "";
        List<String> l = new ArrayList<>(this.result);
        this.result.clear();
        ObjectMapper o = new ObjectMapper();
        try {
            str =  o.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.result = l;
        return str;
    }

    private void getStringOfMapInteger(Map<UUID, Integer> map,StringBuilder builder){
        for(UUID id : map.keySet()){
            builder.append("\t").append(id).append(" : ").append(map.get(id)).append("\n");
        }
    }

    private <T> void getStringOfMapOfMapInteger(Map<UUID, Map<T, Integer>> map, StringBuilder builder){
        for(UUID id : map.keySet()){
            builder.append("\t").append(id).append(" : \n");
            for(T elem : map.get(id).keySet()){
                builder.append("\t\t").append(elem.toString()).append(" : ").append(map.get(id).get(elem)).append("\n");
            }
        }
    }

    public void addResultGame(String str) {
        this.result.add(str);
    }

    public String writeDataJsonFile() throws IOException {
        ObjectMapper o = new ObjectMapper();
        List<StatisticsInGame> list = new ArrayList<>();
        String currentDirecory = Paths.get("").toAbsolutePath().toString();
        File directory = new File(currentDirecory+"\\Stats");
        File fileStat = new File(currentDirecory+"\\Stats\\statistique.json");
        if(!directory.exists()) directory.mkdir();

        if(!fileStat.exists()) {
            directory.createNewFile();
            PrintWriter writer = new PrintWriter(fileStat);
            writer.print(o.writeValueAsString(list));
            writer.flush();
        }

        CollectionType typeReference = TypeFactory.defaultInstance().constructCollectionType(List.class, StatisticsInGame.class);
        String data = new String(Files.readAllBytes(Paths.get(fileStat.getAbsolutePath())));
        try {
            list = o.readValue(data,typeReference);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list.isEmpty()) this.id = 1;
        else this.id = list.get(list.size()-1).id+1;
        list.add(this);
        fileStat.delete();
        fileStat.createNewFile();
        PrintWriter writer = new PrintWriter(fileStat);
        writer.print(o.writeValueAsString(list));
        writer.flush();
        return fileStat.getAbsolutePath();
    }

    public void mergeStat(StatisticsInGame stat) {
        nbGame+=stat.nbGame;
        nbManche+=stat.nbManche;
        nbTurn+=stat.nbTurn;
        this.result.addAll(stat.result);
        for (int nbPlayer : stat.statsPlayersIdLauncher.keySet()) {
            for (int id : this.statsPlayersIdLauncher.keySet()) {
                if (stat.statsPlayersIdLauncher.get(nbPlayer).id == this.statsPlayersIdLauncher.get(id).id) {
                    this.statsPlayersIdLauncher.get(id).merge(stat.statsPlayersIdLauncher.get(nbPlayer));
                }
            }
        }
    }

    public void incNbRessourceWinPlayers(UUID idPlayer,TypeRessource type,int value){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.statsRessource.get(type).haveWin+=value;
     //   incValueMapObject(value,s.nbRessourceWinPlayers,type,idPlayer);
    }

    public void incNbRessourceLoosePlayers(UUID idPlayer,TypeRessource type,int value){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.statsRessource.get(type).haveLoose+=value;
       // incValueMapObject(value,s.nbRessourceLoosePlayers,type,idPlayer);
    }

    public void incNbRessourceExtendMaxPlayers(UUID idPlayer,TypeRessource type,int value){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.statsRessource.get(type).haveExtendMax+=value;
     //   incValueMapObject(value,s.nbRessourceExtendMaxPlayers,type,idPlayer);
    }

    public void incNbRessourceNotLoosePlayers(UUID idPlayer,TypeRessource type,int value){
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.statsRessource.get(type).haveExtendMin+=value;
    //    incValueMapObject(value,s.nbRessourceNotLoosePlayers,type,idPlayer);
    }

    public void incNbGoldAddHammerPlayer(UUID idPlayer,  int value) {
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        ((StatisticsRessourceGold) s.statsRessource.get(TypeRessource.GOLD)).haveStockInHammer+=value;
    }

    public void incNbbRessourceWinGloryHammerPlayer(UUID idPlayer,  int value) {
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        ((StatisticsRessourceGlory) s.statsRessource.get(TypeRessource.GLORY)).haveWinWithHammer+=value;
    }

    public void incNbRessourceLooseWhenBuyPlayer(UUID idPlayer, TypeRessource type, int value) {
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        s.statsRessource.get(type).haveLooseWhenBuyCardOrFace+=value;
    }

    public void incNbRessourceGoldWinStartGame(UUID idPlayer, TypeRessource type, int value) {
        StatisticsPlayer s = statsPlayers.get(idPlayer);
        if(type == TypeRessource.GOLD) ((StatisticsRessourceGold) s.statsRessource.get(type)).haveWinStartGame+=value;
    }
}
