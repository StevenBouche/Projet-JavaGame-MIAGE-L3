package mainlauncher.printer;

import mainlauncher.launcher.Launcher;
import mainlauncher.main.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Printer console launcher.
 */
public class PrinterConsoleLauncher implements Runnable {

    private boolean running;

    /**
     * Set running.
     *
     * @param b the b
     */
    public void setRunning(boolean b){
        this.running = b;
    }
    private List<Launcher> context;

    /**
     * Set context launcher.
     *
     * @param list the list
     */
    public void setContextLauncher(List<Launcher> list){
        synchronized (this.context){
            this.context = list;
        }
    }

    /**
     * Instantiates a new Printer console launcher.
     */
    public PrinterConsoleLauncher(){
        this.running = true;
        this.context = new ArrayList<>();
    }

    @Override
    public void run() {
        while(this.running){

                if(!this.context.isEmpty()){

                    int nbGame = 0;
                    int nbGameTot = 0;
                    long nbTime = 0;
                    for(Launcher l : this.context){
                        nbTime += System.currentTimeMillis() - l.getStartTime();
                        nbGame += l.getNbGameExecute();
                        nbGameTot += l.getNbGame();
                //        System.out.println(l.toString());
                    }

                    nbTime = (nbTime / this.context.size())/1000;

                    float avancement = ((float) nbGame / nbGameTot);
                    avancement = (float) Math.round(avancement*10000) /100;
                    int nbEqual = (int) (avancement /10);
                    int resteGame = nbGameTot - nbGame;

                    long timeExecution = (System.currentTimeMillis() - Main.getStartTime()) /1000;
                    long gamePerTime = 0 ;
                    if(timeExecution != 0) {
                        gamePerTime = (long) nbGame/timeExecution;
                    }
                    int resteTime = 0;
                    if(gamePerTime != 0 ) resteTime = (int) (resteGame / gamePerTime);


                    String str = "[";
                    for(int i = 0; i < nbEqual; i++) str += "=";
                    str += ">";
                    for(int nb = nbEqual; nb < 10; nb++) str += ".";
                    str += "] "+avancement +"% "+nbGame+"/"+nbGameTot;
                    str += "\tTime execution : "+nbTime+" secondes, Game per second : "+gamePerTime+", reste : "+resteTime+" secondes";
                    System.out.println(str);
                }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
