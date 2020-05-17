package share.utils;

import share.choice.ChoiceBetweenRessource;
import share.game.Game;
import share.game.GameState;
import share.player.Player;
import share.ressource.TypeRessource;

import java.util.List;
import java.util.UUID;

/**
 * The type Printer.
 */
public class Printer {

    private static Printer instance = null;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    /**
     * Get instance printer.
     *
     * @return the printer
     */

    public static Printer getInstance(){
        if(instance == null) instance = new Printer(true);
        return instance;
    }

    public  static Printer getInstance(boolean b){
        if(instance == null) instance = new Printer(b);
        return instance;
    }

    private boolean verbose;

    private Printer(boolean b){
        this.verbose = b;
    }

    public  void logServer(String str, int port) {
        if(verbose) System.out.println(Printer.CYAN + "[SERVER "+port+"] " + Printer.GREEN +str + ANSI_RESET);
    }

    public   void logGame(String str) {
        if(verbose) System.out.println(Printer.PURPLE+ "[GAME] " + Printer.GREEN +str + ANSI_RESET);
    }

    public   void logGameLoop(String str){
        if(verbose) System.out.println(Printer.BLUE+ "[GAME LOOP] " + Printer.GREEN +str + ANSI_RESET);
    }

    public void logClient(String str, String id){
        if(verbose) System.out.println(Printer.YELLOW+ "[CLIENT "+id+"] " + Printer.GREEN +str + ANSI_RESET);
    }

    public void logLauncher(String s){
        if(verbose) System.out.println(Printer.RED+ "[LAUNCHER] "+Printer.GREEN+s+ANSI_RESET);
    }

    public void logNewTurnGame(int nbManche, int nbTurn){
        if(verbose) System.out.println();
        this.logGame("ROUND :"+nbManche+" | TURN : "+nbTurn);
    }

    public   void logEndTurnGame(int nbManche, int nbTurn){
        this.logGame("END ROUND :"+nbManche+" | TURN : "+nbTurn);
        if(verbose) System.out.println();
    }

    public  void logGameState(GameState g){
        if(verbose) System.out.println();
        this.logGame("GAME STATE HAVE CHANGE TO : "+g.name());
        if(verbose) System.out.println();
    }

    public void printInventoryPlayers(Game game){
        this.logGame("PRINT ALL INVENTORY PLAYER : ");
        for (UUID id: game.getIdPlayers())  {
            this.logGame(game.getPlayer(id).toString());
        }
    }

    public void printRanking(List<Player> list){
        this.logGame("RANKING OF GAME : ");
        int count = 1;
        for(Player p : list){
            this.logGame(p.getId()+" rank : "+count+" with "+p.getInventory().getValueRessource(TypeRessource.GLORY)+" "+TypeRessource.GLORY);
            count++;
        }
    }

    public synchronized void printCommand(String name, String txt, int nbStack){
        String str = "";
        for(int i = 0; i< nbStack; i++) str+="\t";
        if(verbose) System.out.println(str+""+Printer.PURPLE+ "["+name+"] " + Printer.GREEN +txt + ANSI_RESET);
    }
}
