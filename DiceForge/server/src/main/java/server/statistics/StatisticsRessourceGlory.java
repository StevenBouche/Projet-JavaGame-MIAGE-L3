package server.statistics;

import share.ressource.TypeRessource;

public class StatisticsRessourceGlory extends StatisticsRessource {

    public int haveWinWithHammer=0;

    public StatisticsRessourceGlory(){
        super(TypeRessource.GLORY);
    }

    @Override
    public void merge(StatisticsRessource stat){
        super.merge(stat);
        if(stat instanceof StatisticsRessourceGlory){
            this.haveWinWithHammer+=((StatisticsRessourceGlory) stat).haveWinWithHammer;
        }
    }
}
