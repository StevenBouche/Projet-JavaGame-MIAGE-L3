package server.command;

import share.utils.Printer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type Command.
 *
 * @param <T> the type parameter
 */
public abstract class Command<T> {

    private final T context;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = this.lock.newCondition();
    private final CommandManager manager;
    private final String name;
    private final int stack;
    private long startTime;

    /**
     * Instantiates a new Command.
     *
     * @param context the context
     * @param manager the manager
     * @param name    the name
     */
    public Command(T context, CommandManager manager, String name){
        this.context = context;
        this.manager = manager;
        this.name = name;
        stack = manager.getNbStackCommands();
    }

    /**
     * To reset start time of current command.
     * Need when more than one choice to calc time.
     */
    public void resetStartTime(){
        this.startTime = System.currentTimeMillis();
    }
    /**
     * Get context t.
     *
     * @return is generic type
     */
    public T getContext(){
        return this.context;
    }

    /**
     * Waiting decision on condition object and lock.
     */
    public void waitingDecision(){
        this.lock.lock();
        try {
            this.condition.await(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.lock.unlock();
    }

    /**
     * Notify decision. Signal condition.
     */
    public void notifyDecision(){
        this.lock.lock();
        this.condition.signal();
        this.lock.unlock();
    }

    /**
     * When command is trigger.
     * Get start time command and execute command and trigger endExecute
     */
    public void trigger(){
        this.startTime = System.currentTimeMillis();
        this.onExecute();
        this.onEndExecute();
    }

    /**
     * On execute. Abstract need to redefine
     */
    public abstract void onExecute();

    /**
     * Trigger on end execute command on manager command
     */
    private void onEndExecute(){
        this.manager.onEndExecute(this);
    }

    /**
     * Gets manager command.
     * @return the manager command
     */
    public CommandManager getManager() {
        return manager;
    }

    /**
     * Gets name command.
     * @return the name of command
     */
    public String getName() {
        return name;
    }

    /**
     * Gets stack. Is nb command before this.
     * @return the stack
     */
    public int getStack() {
        return stack;
    }

    /**
     * To get start time of command.
     * @return start time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Used by command to print data
     * @param str data want print
     */
    protected void print(String str){
        Printer.getInstance().printCommand(this.getName(),str,this.getStack());
    }

}
