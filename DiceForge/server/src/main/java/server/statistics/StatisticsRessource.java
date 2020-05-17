package server.statistics;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import share.ressource.TypeRessource;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class StatisticsRessource {

    public TypeRessource type;

    public int haveWin =0;
    public int haveExtendMax=0;
    public int haveLoose=0;
    public int haveLooseWhenBuyCardOrFace=0;
    public int haveExtendMin=0;

    public StatisticsRessource(TypeRessource type){
        this.type = type;
    }

    public StatisticsRessource(){

    }

    public void merge(StatisticsRessource statisticsRessource) {
        haveWin += statisticsRessource.haveWin;
        haveLoose += statisticsRessource.haveLoose;
        haveExtendMax += statisticsRessource.haveExtendMax;
        haveExtendMin += statisticsRessource.haveExtendMin;
        haveLooseWhenBuyCardOrFace += statisticsRessource.haveLooseWhenBuyCardOrFace;
    }

}
