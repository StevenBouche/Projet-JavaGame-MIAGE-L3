package server.command.ressource;

import server.command.CommandGameManager;
import server.command.CommandManager;
import server.game.GameManager;
import server.game.IGameManager;
import share.ressource.TypeRessource;

import java.util.UUID;

/**
 * The type Ressource player command.
 */
public class RessourcePlayerCommand extends CommandGameManager {

    private final CmdRessource cmd;
    private final UUID idPlayer;
    private final TypeRessource type;
    private final int value;
    private int resourceB;
    private int resourceA;

    /**
     * Instantiates a new Ressource player command.
     *
     * @param context        the context
     * @param commandManager the command manager
     * @param cmd            the cmd
     * @param idPlayer       the id player
     * @param type           the type
     * @param value          the value
     */
    public RessourcePlayerCommand(IGameManager context, CommandManager commandManager, CmdRessource cmd, UUID idPlayer, TypeRessource type, int value) {
        super(context,commandManager,"RESSOURCE_PLAYER_COMMAND");
        this.cmd = cmd;
        this.idPlayer = idPlayer;
        this.type =type;
        this.value = value;
    }

    /**
     * Execute action in function of CmdRessource object
     * When add gold if player have hammer trigger command Hammer.
     * ADD_AFTER_HAMMER is for add gold after hammer to break infinite loop
     */
    @Override
    public void onExecute() {

        resourceB = this.getContext().getGame().getPlayer(idPlayer).getInventory().getValueRessource(type);

        if(this.cmd == CmdRessource.ADD) { // si le type de commande est un ajout
            if(type == TypeRessource.GOLD && this.getContext().getGame().getPlayer(idPlayer).getInventory().haveHammer()){ // un ajout de gold
                this.print("Player "+idPlayer+" have hammer, ressource : "+value+" "+type+" to "+idPlayer);
                this.getManager().triggerCommandEffectHammer(idPlayer,value); // on trigger leffet hammer
            }
            else {
                this.print("Add ressource : "+value+" "+type+" to "+idPlayer);
                getContext().getGame().addRessourcePlayer(idPlayer,type,value); //sinon on ajout la ressource
                this.calculeStatAdd();
            }
        }
        else if(this.cmd == CmdRessource.REMOVE) {
            this.print("Remove ressource : "+value+" "+type+" to "+idPlayer);
            getContext().getGame().removeRessourcePlayer(idPlayer,type,value); // remove ressource
            this.calculeStatRemove();
        }
        else if(this.cmd == CmdRessource.ADD_AFTER_HAMMER) {
            this.print("Add ressource after hammer : "+value+" "+type+" to "+idPlayer);
            getContext().getGame().addRessourcePlayer(idPlayer,type,value); //si c'est un ajout apres hammer alors exec la commande d'ajout de ressource
            this.calculeStatAdd();
        }
    }

    /**
     * Add stats remove resource
     */
    private void calculeStatRemove() {
        resourceA = this.getContext().getGame().getPlayer(idPlayer).getInventory().getValueRessource(type);
        int loose = resourceB - resourceA;
        int diff = (resourceA - resourceB) + value;
        this.getContext().getStats().incNbRessourceLoosePlayers(idPlayer,type,loose);
        this.getContext().getStats().incNbRessourceNotLoosePlayers(idPlayer,type,diff);
    }

    /**
     * Add stats add resource
     */
    private void calculeStatAdd() {
        resourceA = this.getContext().getGame().getPlayer(idPlayer).getInventory().getValueRessource(type);
        int win = resourceA - resourceB;
        int diff = (resourceB - resourceA) + value;
        this.getContext().getStats().incNbRessourceWinPlayers(idPlayer,type,win);
        this.getContext().getStats().incNbRessourceExtendMaxPlayers(idPlayer,type,diff);
    }
}
