package server;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ConfigurationServer {

    public final int nbGame;
    public final int portNetwork;
    public final Lock lockFather;
    public final Condition serverStart;
    public final Condition gameFinish;

    /**
     * Configuration of server object, need to inject in server for run it
     *
     * @param port port of network server
     * @param nbGame nb game execution
     * @param father lock father to notify him
     * @param serverStart condition attach to father for notify when server have started
     * @param gameFinish condition attach to father for notify when one game have finished
     */
    public ConfigurationServer(int port, int nbGame, Lock father, Condition serverStart, Condition gameFinish){
        this.nbGame = nbGame;
        this.portNetwork = port;
        this.lockFather = father;
        this.serverStart = serverStart;
        this.gameFinish = gameFinish;
    }

}
