package server.statistics;

import share.ressource.TypeRessource;

public class StatisticsRessourceGold extends StatisticsRessource {

    public int haveWinStartGame=0;
    public int haveStockInHammer=0;

    public StatisticsRessourceGold(){
        super(TypeRessource.GOLD);
    }

    @Override
    public void merge(StatisticsRessource stat){
        super.merge(stat);
        if(stat instanceof StatisticsRessourceGold){
            this.haveWinStartGame+=((StatisticsRessourceGold) stat).haveWinStartGame;
            this.haveStockInHammer+=((StatisticsRessourceGold) stat).haveStockInHammer;
        }
    }
}
