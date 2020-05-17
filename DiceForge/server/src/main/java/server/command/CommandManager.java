package server.command;

import server.command.card.Hammer;
import server.command.card.HandleActionCard;
import server.command.face.*;
import server.command.inter.ICommandChoice;
import server.command.inter.ICommandManager;
import server.command.inter.ICommandManagerTurn;
import server.command.turn.AddGoldPlayerStartGame;
import server.command.turn.HandleActionTurn;
import server.command.turn.HandleOneMoreActionTurn;
import server.command.turn.TriggerExploitTurnPlayer;
import server.command.dice.ForgeFaceOnDice;
import server.command.player.RemovePlayerIsland;
import server.command.player.SetPlayerActif;
import server.command.player.SetPlayerOnIsland;
import server.command.ressource.AddGloryCardEndGame;
import server.command.ressource.CmdRessource;
import server.command.ressource.RessourcePlayerCommand;
import server.command.rolldice.*;

import server.game.IGameManager;
import share.cards.Card;
import share.cards.Cards;
import share.choice.*;
import share.face.Face;
import share.face.FaceHybrid;
import share.face.FaceSimple;
import share.face.FaceSpecial;
import share.ressource.TypeRessource;
import share.temple.IslandEnum;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * The type Command manager.
 */
public class CommandManager implements ICommandManager, ICommandManagerTurn {

    private final IGameManager gameManager;
    private final ConcurrentLinkedDeque<Command> queue;


    /**
     * Instantiates a new Command manager.
     *
     * @param gameManager the game manager
     */
    public CommandManager(IGameManager gameManager){
        this.gameManager = gameManager;
        this.queue = new ConcurrentLinkedDeque<>();
    }

    /**
     * Notify choice player at last command. Last command wait notify choice to continue execution.
     *
     * @param idPlayer the id player
     * @param choice   the choice object
     */
    public <T extends Choice> void notifyChoicePlayerLastCommand(UUID idPlayer, T choice){
        if(this.queue.getLast() instanceof ICommandChoice) ((ICommandChoice<T>) this.queue.getLast()).notifyChoice(idPlayer,choice);
        else {
            try {
                throw new Exception("BAD CHOICE CLIENT TO ACTION !");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Trigger command.
     * Add last queue command.
     *
     * @param cmd the cmd
     */
    public void triggerCommand(Command cmd){
        this.queue.addLast(cmd);
        if(this.queue.size() > 20){
            try {
                throw new Exception("STACK 20 ACTION AND MORE");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cmd.trigger();
    }

    /**
     * On end execute. Trigger when command finish its execution.
     * Remove command of queue.
     *
     * @param <T>      the type parameter
     * @param tCommand the t command
     */
    public <T> void onEndExecute(Command<T> tCommand) {
        this.queue.removeLast();
    }

    /**
     * Get nb stack commands int. To get nb command trigger before.
     *
     * @return the int
     */
    public int getNbStackCommands(){
        return this.queue.size();
    }

    /**
     * Add ressource player command.
     *
     * @param idPlayer the id player
     * @param type     the type
     * @param value    the value
     */
    public void addRessourcePlayerCommand(UUID idPlayer, TypeRessource type, int value){
        RessourcePlayerCommand cmd = new RessourcePlayerCommand(gameManager,this, CmdRessource.ADD,idPlayer,type,value);
        this.triggerCommand(cmd);
    }

    /**
     * Remove ressource player command.
     *
     * @param idPlayer the id player
     * @param type     the type
     * @param value    the value
     */
    public void removeRessourcePlayerCommand(UUID idPlayer, TypeRessource type, int value){
        RessourcePlayerCommand cmd = new RessourcePlayerCommand(gameManager,this,CmdRessource.REMOVE,idPlayer,type,value);
        this.triggerCommand(cmd);
    }

    /**
     * Add ressource player after hammer command.
     *
     * @param idPlayer the id player
     * @param type     the type
     * @param value    the value
     */
    public void addRessourcePlayerAfterHammerCommand(UUID idPlayer, TypeRessource type, int value){
        RessourcePlayerCommand cmd = new RessourcePlayerCommand(gameManager,this, CmdRessource.ADD_AFTER_HAMMER,idPlayer,type,value);
        this.triggerCommand(cmd);
    }

    /**
     * Roll dice major command.
     *
     * @param idPlayer the id player
     */
    public void rollDiceMajorCommand(UUID idPlayer){
        RollDiceMajorOnePlayer cmd = new RollDiceMajorOnePlayer(gameManager,this,idPlayer);
        this.triggerCommand(cmd);
    }

    /**
     * Roll dice major all player command.
     */
    public void rollDiceMajorAllPlayerCommand(){
        RollDiceMajorAllPlayer cmd = new RollDiceMajorAllPlayer(gameManager,this);
        this.triggerCommand(cmd);
    }

    /**
     * Roll dice minor command.
     *
     * @param idPlayer the id player
     */
    public void rollDiceMinorCommand(UUID idPlayer){
        RollDiceMinor cmd = new RollDiceMinor(gameManager,this,idPlayer);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command action turn.
     */
    public void triggerCommandActionTurn() {
        HandleActionTurn cmd = new HandleActionTurn(gameManager,this);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command one more turn.
     */
    public void triggerCommandOneMoreTurn() {
        HandleOneMoreActionTurn cmd = new HandleOneMoreActionTurn(gameManager,this);
        this.triggerCommand(cmd);
    }

    @Override
    public void triggerCommandAddGoldPlayerStartGame() {
        AddGoldPlayerStartGame cmd = new AddGoldPlayerStartGame(gameManager,this);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command set player actif.
     */
    public void triggerCommandSetPlayerActif() {
        SetPlayerActif cmd = new SetPlayerActif(gameManager,this);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command effect hammer.
     *
     * @param idPlayer the id player
     * @param value    the value
     */
    public void triggerCommandEffectHammer(UUID idPlayer, int value) {
        Hammer cmd = new Hammer(gameManager,this,1,idPlayer);
        cmd.setValueChoiceHammer(value);
        this.triggerCommand(cmd);
    }

    /**
     * Handle action card when buy.
     *
     * @param idPlayer the id player
     * @param c        the c
     */
    public void handleActionCardWhenBuy(UUID idPlayer, Card c) {
        HandleActionCard cmd = new  HandleActionCard(gameManager,this,idPlayer,c);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command handle choice hybrid.
     *
     * @param eventFaceHybrid the event face hybrid
     */
    public void triggerCommandHandleChoiceHybrid(Map<UUID, List<FaceHybrid>> eventFaceHybrid) {
        HandleChoiceHybridCommand cmd = new HandleChoiceHybridCommand(gameManager,this,eventFaceHybrid);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command handle choice hybrid.
     *
     * @param eventFaceHybrid the event face hybrid
     * @param inverse         the inverse
     */
    public void triggerCommandHandleChoiceHybrid(Map<UUID, List<FaceHybrid>> eventFaceHybrid, boolean inverse) {
        HandleChoiceHybridCommand cmd = new HandleChoiceHybridCommand(gameManager,this,eventFaceHybrid,inverse);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command handle face simple.
     *
     * @param eventFaceSimple the event face simple
     * @param inverse         the inverse
     */
    public void triggerCommandHandleFaceSimple(Map<UUID, List<FaceSimple>> eventFaceSimple, boolean inverse) {
        HandleActionFaceSimple cmd = new HandleActionFaceSimple(gameManager,this,eventFaceSimple,inverse);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command handle face simple.
     *
     * @param eventFaceSimple the event face simple
     */
    public void triggerCommandHandleFaceSimple(Map<UUID, List<FaceSimple>> eventFaceSimple) {
        HandleActionFaceSimple cmd = new HandleActionFaceSimple(gameManager,this,eventFaceSimple);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command handle hybrid.
     *
     * @param eventFaceHybrid the event face hybrid
     */
    public void triggerCommandHandleHybrid(Map<UUID, List<FaceHybrid>> eventFaceHybrid) {
        HandleActionFaceHybridNotChoice cmd = new HandleActionFaceHybridNotChoice(gameManager,this,eventFaceHybrid);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command handle hybrid.
     *
     * @param eventFaceHybrid the event face hybrid
     * @param inverse         the inverse
     */
    public void triggerCommandHandleHybrid(Map<UUID, List<FaceHybrid>> eventFaceHybrid,boolean inverse) {
        HandleActionFaceHybridNotChoice cmd = new HandleActionFaceHybridNotChoice(gameManager,this,eventFaceHybrid,inverse);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command call renfort.
     */
    public void triggerCommandCallRenfort() {
        TriggerExploitTurnPlayer cmd = new TriggerExploitTurnPlayer(gameManager,this);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command glory card end game.
     */
    public void triggerCommandGloryCardEndGame() {
        AddGloryCardEndGame add = new AddGloryCardEndGame(this.gameManager,this);
        this.triggerCommand(add);
    }

    /**
     * Trigger command set player on island.
     *
     * @param idPlayer the id player
     * @param c        the c
     */
    public void triggerCommandSetPlayerOnIsland(UUID idPlayer, Card c) {
        SetPlayerOnIsland cmdP = new SetPlayerOnIsland(this.gameManager,this,idPlayer,c);
        this.triggerCommand(cmdP);
    }

    /**
     * Trigger command action card.
     *
     * @param idPlayer the id player
     * @param c        the c
     */
    public void triggerCommandActionCard(UUID idPlayer, Card c) {
        Cards cAction = Cards.getValueOfEnumByName(c.name);
        Command<IGameManager> cmd = CardToAction.getActionCard(cAction,gameManager,this,idPlayer);
        if(cmd != null) this.triggerCommand(cmd);
    }

    /**
     * Trigger command forge face on dice.
     *
     * @param idPlayer      the id player
     * @param f             the f
     * @param indexDice     the index dice
     * @param indexDiceFace the index dice face
     */
    public void triggerCommandForgeFaceOnDice(UUID idPlayer, Face f, int indexDice, int indexDiceFace) {
        ForgeFaceOnDice cmd = new ForgeFaceOnDice(gameManager,this,idPlayer, f, indexDice,indexDiceFace);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command action face special with other.
     *
     * @param idPlayer   the id player
     * @param face       the face
     * @param faceChoice the face choice
     */
    public void triggerCommandActionFaceSpecialWithOther(UUID idPlayer, FaceSpecial face, Face faceChoice) {
        HandleActionFaceSpecialWithOther cmd = new HandleActionFaceSpecialWithOther(gameManager,this,idPlayer,face,faceChoice);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command action face special with other.
     *
     * @param idPlayer   the id player
     * @param face       the face
     * @param faceChoice the face choice
     * @param inverse    the inverse
     */
    public void triggerCommandActionFaceSpecialWithOther(UUID idPlayer, FaceSpecial face, Face faceChoice,boolean inverse) {
        HandleActionFaceSpecialWithOther cmd = new HandleActionFaceSpecialWithOther(gameManager,this,idPlayer,face,faceChoice,inverse);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command player leave island.
     *
     * @param idlastPlayerOnIsland the idlast player on island
     * @param islandId             the island id
     */
    public void triggerCommandPlayerLeaveIsland(UUID idlastPlayerOnIsland, IslandEnum islandId) {
        RemovePlayerIsland cmd = new RemovePlayerIsland(gameManager,this,idlastPlayerOnIsland, islandId);
        this.triggerCommand(cmd);
    }


    /**
     * Trigger command handle face simple.
     *
     * @param idPlayer    the id player
     * @param faceSimple2 the face simple 2
     * @param inverse     the inverse
     */
    public void triggerCommandHandleFaceSimple(UUID idPlayer, FaceSimple faceSimple2, boolean inverse) {
        HandleActionFaceSimple cmd = new HandleActionFaceSimple(gameManager,this,idPlayer,faceSimple2,inverse);
        this.triggerCommand(cmd);
    }


    /**
     * Trigger command action face special power.
     *
     * @param idPlayer the id player
     * @param fs       the fs
     * @param inverse  the inverse
     */
    public void triggerCommandActionFaceSpecialPower(UUID idPlayer, FaceSpecial fs,boolean inverse) {
        FaceChoicePowerAction cmd = new FaceChoicePowerAction(gameManager,this,idPlayer,fs,inverse);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command handle not choice hybrid.
     *
     * @param idPlayer the id player
     * @param fs       the fs
     * @param inverse  the inverse
     */
    public void triggerCommandHandleNotChoiceHybrid(UUID idPlayer, FaceHybrid fs,boolean inverse) {
        HandleActionFaceHybridNotChoice cmd = new HandleActionFaceHybridNotChoice(gameManager,this,idPlayer,fs,inverse);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command handle hybrid.
     *
     * @param idPlayer the id player
     * @param fs       the fs
     * @param inverse  the inverse
     */
    public void triggerCommandHandleHybrid(UUID idPlayer, FaceHybrid fs, boolean inverse) {
        HandleChoiceHybridCommand cmd = new HandleChoiceHybridCommand(gameManager,this, idPlayer, fs, inverse);
        this.triggerCommand(cmd);
    }

    /**
     * Trigger command handle action roll dice.
     *
     * @param handlerActionRollDice the handler action roll dice
     */
    public void triggerCommandHandleActionRollDice(HandlerActionRollDice handlerActionRollDice) {
        this.triggerCommand(handlerActionRollDice);
    }

}


