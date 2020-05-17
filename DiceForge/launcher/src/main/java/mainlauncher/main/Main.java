package mainlauncher.main;

import client.game.BotVersions;
import server.statistics.StatisticsInGame;
import share.config.ConfigGame;
import share.config.ConfigLauncher;
import share.config.ConfigNetwork;
import mainlauncher.launcher.Launcher;
import share.config.ConfigPlayer;
import share.utils.Printer;
import mainlauncher.printer.PrinterConsoleLauncher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type Main.
 */
public class Main {

    /**
     * The Nb launcher.
     */
    static int nbLauncher = 0;
    /**
     * The Nb client.
     */
    static int nbClient = 0;
    /**
     * The Printer.
     */
    static PrinterConsoleLauncher printer;
    /**
     * The Thread printer.
     */
    static Thread threadPrinter;
    /**
     * The Executor.
     */
    static ThreadPoolExecutor executor;
    /**
     * The Launcher list.
     */
    static List<Launcher> launcherList;
    static List<Future<StatisticsInGame>> listResultLauncher;
    private static long startTime;
    private static boolean modeDebug;

    private static Map<BotVersions,Integer> botsVersion;
    /**
     * Get start time long.
     *
     * @return the long
     */
    public static long getStartTime(){
        return startTime;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {

        //time stats
        startTime = System.currentTimeMillis();
        modeDebug = false;
        launcherList = new ArrayList<>(); // listCallable
        listResultLauncher = new ArrayList<>();
        //execute program
        if(args.length == 0) executeSingleInstanceOfLauncher(); // Pas besoin de Thread
        else analyseCommandeInput(args); // command saisie

        exitProgram(0);
    }

    private static void exitProgram(int status){
        // time stats
        long endTime = System.currentTimeMillis();
        long timeExec = (endTime - startTime) / 1000;
        System.out.println("EXIT DICEFORGE PROGRAM, TIME : "+timeExec+" seconds.");
        System.exit(status);
    }

    private static void executeSingleInstanceOfLauncher() {
        initBotVersion();
        initPrinterMonoGame();
        executor = (ThreadPoolExecutor)  Executors.newFixedThreadPool(1); // manager thread
        Launcher launcher = new Launcher(ConfigNetwork.START_PORT_NETWORK,1,botsVersion); // creer un nouveau launcher
        launcherList.add(launcher);
        listResultLauncher.add(executor.submit(launcher));
        executor.shutdown(); // n'accepte plus d'autres taches
        waitProcessingLauncherAndStopThread();
    }

    private static void initBotVersion() {
        if(botsVersion == null) botsVersion = getDefaultModeBot();
    }

    private static void analyseCommandeInput(String[] args) {

        int nbInstance = -1;
        int current = 0;
        int botSimple = 0;
        int botRandom = 0;
        int botStat = 0;

        for(String str : args){
            if ("-p".equals(str)) {
                if (current + 1 <= args.length - 1)
                    nbInstance = Integer.parseInt(args[current + 1]); // get nb Thread game
            }
            else if("-bot".equals(str)){
                botRandom = Integer.parseInt(args[current + 1]);
                botSimple = Integer.parseInt(args[current + 2]);
                botStat = Integer.parseInt(args[current + 3]);
                botsVersion = getModeBot(botRandom,botSimple,botStat);
            }
            current++;
        }

        if(nbInstance == -1) executeSingleInstanceOfLauncher();
        else executeLauncherThread(nbInstance); // execute traitement thread
    }

    /**
     * Execute launcher thread.
     *
     * @param nbThread the nb thread
     */
    public static void executeLauncherThread(int nbThread) {
        initPrinterMultiGame();
        initAndStartLaunchers(nbThread);
        waitProcessingLauncherAndStopThread();
    }

    private static void initPrinterMultiGame() {
        // PRINTER DE LAUNCHER
        printer = new PrinterConsoleLauncher();
        threadPrinter = new Thread(printer);
        threadPrinter.start();
        Printer.getInstance(modeDebug); // init le logger avec verbose ou non
    }

    private static void initPrinterMonoGame() {
        Printer.getInstance(true); // Initialise le logger
    }

    private static void initAndStartLaunchers(int nbThread) {

        initBotVersion();

        int pool = 0;
        int poolWorker = 0;
        int rest = 0;
        if(nbThread < ConfigLauncher.NB_WORKER_LAUNCHER) { // plage de game trop petite pour le pool de thread le diminue
            pool = 1;
            poolWorker = nbThread;
        }
        else { // sinon partage le nombre de game entre le pool de launcher
            pool = nbThread /ConfigLauncher.NB_WORKER_LAUNCHER;
            rest = nbThread%ConfigLauncher.NB_WORKER_LAUNCHER; // reste de la division dans le cas ou pas divisible
            poolWorker = ConfigLauncher.NB_WORKER_LAUNCHER;
        }

        executor = (ThreadPoolExecutor)  Executors.newFixedThreadPool(poolWorker); // manager thread
        int port = ConfigNetwork.START_PORT_NETWORK; // port network start

        Lock lo = new ReentrantLock();
        Condition c = lo.newCondition();
        for(int i = 0; i < poolWorker; i++) {
            Launcher l;
            if(i == poolWorker-1) l = new Launcher(port+i,pool+rest,botsVersion,lo,c); // si dernier launcher alors prend en plus le reste de la division
            else l = new Launcher(port+i,pool,botsVersion,lo,c);
            launcherList.add(l);
            listResultLauncher.add(executor.submit(l));
            nbLauncher++;
        }
        nbClient = nbLauncher * ConfigGame.NB_PLAYER_MAX;
        executor.shutdown(); // refuse new Task
        printer.setContextLauncher(launcherList); //add le context au printer
    }

    private static void waitProcessingLauncherAndStopThread() {

        List<StatisticsInGame> stats = new ArrayList<>();
        try {
            executor.awaitTermination(1,TimeUnit.HOURS);
            if(printer != null) printer.setRunning(false);
            if(threadPrinter != null) threadPrinter.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        //recuperation des resultats a revoir : prendre en compte si le launcher a etait interrupt donc pas de result
        for(Future<StatisticsInGame> f : listResultLauncher){
            try {
                stats.add(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        StatisticsInGame statMain = stats.remove(0);
        if(stats.size() >= 1) for(StatisticsInGame st : stats) statMain.mergeStat(st);

        System.out.println(statMain.toJSONStringForm());

        try {
            String str = statMain.writeDataJsonFile();
    //        System.out.println("TOTAL EXECUTE GAME BY LAUNCHERS "+partie);
            System.out.println("STATISTIC ADD TO FILE : "+str);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Map<BotVersions,Integer> getDefaultModeBot(){
        Map<BotVersions,Integer> setBot = new HashMap<>();
        setBot.put(BotVersions.RANDOM,2);
        setBot.put(BotVersions.VERSION_SIMPLE,1);
        setBot.put(BotVersions.VERSION_STATISTIQUE, 1);
        return setBot;
    }

    private static Map<BotVersions,Integer> getModeBot(int random, int simple, int stat){
        int tot = random + simple + stat;
        if(tot == 4){
            Map<BotVersions,Integer> setBot = new HashMap<>();
            setBot.put(BotVersions.RANDOM,random);
            setBot.put(BotVersions.VERSION_SIMPLE,simple);
            setBot.put(BotVersions.VERSION_STATISTIQUE, stat);
            return setBot;
        }
        else try {
            throw new Exception("TO EXECUTE PLAYER NEED "+ ConfigGame.NB_PLAYER_MAX+" BUT YOU WANT " +tot+" PLAYERS");
        } catch (Exception e) {
            e.printStackTrace();
            exitProgram(-1);
        }
        return null;
    }
}
