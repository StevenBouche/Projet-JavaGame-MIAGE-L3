package share.game;

import share.cards.Card;
import share.cards.effects.TypeEffectBasique;
import share.cards.model.Hammer;
import share.dice.Dice;
import share.exeption.CaseOfPoolForgeOutOfBound;
import share.exeption.InstanceCardOutOfBoundException;
import share.exeption.InstanceFaceOutOfBoundException;
import share.face.Face;
import share.face.FaceSpecial;
import share.face.FaceSpecialEnum;
import share.face.FactoryFace;
import share.forge.Forge;
import share.core.IHandlerForge;
import share.core.IHandlerPlayer;
import share.core.IHandlerTemple;
import share.forge.Pool;
import share.inventory.InventoryElement;
import share.player.Player;
import share.ressource.TypeRessource;
import share.temple.Island;
import share.temple.IslandEnum;
import share.temple.Temple;
import share.utils.Printer;

import java.util.*;

import static share.game.GameState.*;

/**
 * The type Game.
 */
public class Game implements IHandlerTemple, IHandlerForge, IHandlerPlayer {

    private HashMap<UUID, Player> listPlayer;
    private ArrayList<UUID> indexPlayer;
    private Forge forge;

    /**
     * The State.
     */
    public GameState state;
    /**
     * The Player nb.
     */
    public int playerNb;
    /**
     * The Nb manche.
     */
    public int nbManche;
    /**
     * The Nb manche max.
     */
    public int nbMancheMax;
    /**
     * The Cpt nb play.
     */
    public int cptNbPlay;

    /**
     * The Current share.player.
     */
    public Player currentPlayer;

    /**
     * The Temple.
     */
    public Temple temple;

    /**
     * Instantiates a new Game.
     */
    public Game (){

        this.listPlayer = new HashMap<>();
        this.indexPlayer = new ArrayList<>();
        this.currentPlayer = null;
        this.playerNb = 4;
        this.nbMancheMax = 9;

        this.init();

    }

    private void init() {
        this.state = INIT;
        Printer.getInstance().logGame("GAME STATE : "+state);
        this.nbManche = 1;
        this.cptNbPlay = 1;
        try {
            this.forge = new Forge();
        } catch (InstanceFaceOutOfBoundException e) {
            e.printStackTrace();
            this.state = STOP;
        }
        try {
            this.temple = new Temple();
        } catch (InstanceCardOutOfBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reset.
     */
    public void reset() {
        FactoryFace.resetInstance();
        this.init();
        this.currentPlayer = null;
        HashMap<UUID, Player> list = new HashMap<>();
        for(UUID id : this.indexPlayer) list.put(id,new Player(id,this.listPlayer.get(id).version)) ;
        this.listPlayer.clear();
        this.listPlayer = list;
    }

    /**
     * Get nb share.player int.
     *
     * @return the int
     */
    public int getNbPlayer (){
        return this.listPlayer.size();
    }

    /**
     * Get share.player share.player.
     *
     * @param id the id
     * @return the share.player
     */
    public Player getPlayer(UUID id){
        return this.listPlayer.get(id);
    }

    /**
     * Get players map.
     *
     * @return the map
     */
    public Map<UUID, Player> getPlayers(){
        return this.listPlayer;
    }

    /**
     * Get id players list.
     *
     * @return the list
     */
    public List<UUID> getIdPlayers(){
        return this.indexPlayer;
    }

    /**
     * Add share.player.
     *
     *
     * @param player the share.player
     * @param id     the id
     */
    public void addPlayer(Player player, UUID id){
        this.listPlayer.put(id,player);
        this.indexPlayer.add(id);
    }

    /**
     * Remove share.player.
     *
     * @param id the id
     */
    public void removePlayer(UUID id){
        this.listPlayer.remove(id);
        this.indexPlayer.remove(id);
    }

    /**
     * Stop.
     */
    public void stop(){
        this.state = STOP;
    }

    /**
     * Get share.forge share.forge.
     *
     * @return the share.forge
     */
    public Forge getForge(){
        return this.forge;
    }

    /**
     * Gets card from island.
     *
     * @param island    the island
     * @param nbPosCard the nb pos card
     * @return the card from island
     */
    public Card pullCardFromIsland(IslandEnum island, int nbPosCard) {
        return this.temple.getListIsland().get(island).listcard.remove(nbPosCard);
    }

    public Card getCardFromIsland(IslandEnum island, int nbPosCard) {
        return this.temple.getListIsland().get(island).listcard.get(nbPosCard);
    }

    @Override
    public void removeRessourcesPlayer(Map<TypeRessource, Integer> costs, UUID idPlayer) {
        for(TypeRessource type : costs.keySet()){
            this.getPlayer(idPlayer).getInventory().removeRessource(type,costs.get(type));
        }
    }

    @Override
    public void removeRessourcePlayer(UUID idplayer, TypeRessource cost, int nb) {
        this.getPlayer(idplayer).getInventory().removeRessource(cost,nb);
    }

    @Override
    public void addRessourcePlayer(UUID idplayer, TypeRessource ressource, int nb) {
        this.getPlayer(idplayer).getInventory().addRessource(ressource,nb);
    }

    @Override
    public void addCardPlayer(Card c, UUID idPlayer) {
        this.getPlayer(idPlayer).getInventory().addCard(c.typeEffect,c);
    }

    @Override
    public Face removeFaceOfDicePlayer(UUID id, int nbDice, int nbPosFace) {
        return this.getPlayer(id).getInventory().getDice(nbDice).removeFace(nbPosFace);
    }

    @Override
    public Face addFaceOfDicePlayer(UUID idPlayer, int nbDice, int nbPosFace, Face f) {
        Face f2 = this.removeFaceOfDicePlayer(idPlayer, nbDice, nbPosFace);
        this.getPlayer(idPlayer).getInventory().getDice(nbDice).addFace(nbPosFace,f);
        return f2;
    }

    @Override
    public void addHistoryFacePlayer(UUID idPlayer, Face f) {
        this.getPlayer(idPlayer).getInventory().addHistoryFace(f); // ajoute en historique la share.face enlever dans l'inventaire
    }

    @Override
    public List<Player> getPlayerSortByTypeRessource(TypeRessource tp) {
        List<Player> list = new ArrayList<>(this.getPlayers().values());
        Collections.sort(list,new SortByTypeRessource(tp));
        return list;
    }

    @Override
    public List<Card> getCardWithEffectOfPlayer(UUID idPlayer, TypeEffectBasique typeEffectBasique) {
        return this.getPlayer(idPlayer).getInventory().getListCards(typeEffectBasique);
    }

    @Override
    public Face getFaceFromPool(int nbPool, int nbPosFace) throws CaseOfPoolForgeOutOfBound {
        return this.getForge().getFaceOnPool(nbPool,nbPosFace);
    }

    public Face getFaceWithRemoveFromPool(int nbPool, int nbPosFace) throws CaseOfPoolForgeOutOfBound {
        return this.getForge().getFaceWithRemoveOnPool(nbPool,nbPosFace);
    }

    /**
     * Add hammer player.
     *
     * @param currentPlayerId the current player id
     */
    public void addHammerPlayer(UUID currentPlayerId) {
        Hammer h = new Hammer();
        this.getPlayer(currentPlayerId).getInventory().addHammer(h);
    }


    public UUID getIdOfCurrentPlayer() {
        return currentPlayer.getId();
    }

    public Dice getDiceOfCurrentPlayer(int i) {
        return this.currentPlayer.getInventory().getDice(i);
    }

    public Map<TypeRessource, InventoryElement> getRessourcesOfCurrentPlayer() {
        return this.currentPlayer.getInventory().getRessources();
    }

    public int getValueGoldPlayer(UUID idPlayer) {
        return this.getPlayer(idPlayer).getInventory().getValueRessource(TypeRessource.GOLD);
    }

    public void extendRessourcesPlayer(UUID idPlayer, int value) {
        this.getPlayer(idPlayer).getInventory().extendRessource(TypeRessource.GOLD,value);
        this.getPlayer(idPlayer).getInventory().extendRessource(TypeRessource.SOLAR,value);
        this.getPlayer(idPlayer).getInventory().extendRessource(TypeRessource.LUNAR,value);
    }

    public int getCurrentIdHammerPlayer(UUID idPlayer) {
        return this.getPlayer(idPlayer).getInventory().getCurrentIdHammer();
    }

    public Hammer getCurrentHammerPlayer(UUID idPlayer) {
        return this.getPlayer(idPlayer).getInventory().getCurrentHammer();
    }

    public boolean ifPlayerHaveHammer(UUID idPlayer) {
        return this.getPlayer(idPlayer).getInventory().haveHammer();
    }

    public void setCurrentIdHammerPlayer(UUID idPlayer, int i) {
        this.getPlayer(idPlayer).getInventory().setCurrentHammer(i);
    }

    public FaceSpecial getFaceSpecial(FaceSpecialEnum face) {
        return this.getForge().getFaceSpecial(face);
    }

    public Face getFaceOnDicePlayer(UUID idPlayer,int indexDice, int indexDiceFace) {
        return this.getPlayer(idPlayer).getInventory().getDice(indexDice).getFace(indexDiceFace);
    }

    public int getCostOfPoolForge(int choicePool) {
        if(this.getForge().getListPools().get(choicePool) == null) return 0;
        return this.getForge().getListPools().get(choicePool).getCost();
    }

    public int getValueRessourcePlayer(UUID id, TypeRessource type) {
        return this.getPlayer(id).getInventory().getValueRessource(type);
    }

    public Map<Integer, Face> getLastRollsPlayer(UUID idPlayer) {
        return this.getPlayer(idPlayer).getInventory().getLastRoll();
    }

    public boolean ifPlayerisAlreadyOnIslandCard(UUID idPlayer, Card cardWasBuy) {
        return this.temple.getIslandFromCard(cardWasBuy).player != idPlayer;
    }

    public UUID addPlayerOnIslandOfTemple(UUID idPlayer, IslandEnum islandId) {
        return this.temple.addPlayerOnIsland(idPlayer,islandId);
    }

    public EnumMap<IslandEnum, Island> getIslandsOfTemple() {
        return this.temple.getListIsland();
    }

    public Map<Integer, Pool> getPoolsForge() {
        Map<Integer, Pool> l;
        l = this.getForge().getListPools();
        return l;
    }

}

/**
 * The type Sort by type ressource.
 */
class SortByTypeRessource implements Comparator<Player> {

    private final TypeRessource typeRessource;

    /**
     * Instantiates a new Sort by type ressource.
     *
     * @param tp the tp
     */
    public SortByTypeRessource(TypeRessource tp){
        this.typeRessource = tp;
    }

    @Override
    public int compare(Player player, Player t1) {
        return t1.getInventory().getValueRessource(this.typeRessource) - player.getInventory().getValueRessource(this.typeRessource);
    }
}
