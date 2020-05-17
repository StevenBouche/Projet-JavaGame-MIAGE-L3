package client.game;

import share.choice.ChoiceNothingForgeExploit;
import share.ressource.TypeRessource;

import java.util.EnumMap;

/**
 * The interface Action game server.
 */
public interface IActionGameServer {
    /**
     * Choice share.ressource share.face hybrid type share.ressource.
     *
     * @param fh the hybrid share.face
     * @return the TypeRessource of an Hybrid Face (RNG)
     */
    TypeRessource choiceRessourceFaceHybrid(EnumMap<TypeRessource, Integer> fh);

    /**
     * Choice share.forge or exploi or nothing.
     *
     * @param ch the ch
     */
    void choiceForgeOrExploiOrNothing(ChoiceNothingForgeExploit ch);

    /**
     * Choice share.face in share.forge.
     *
     * @param ch the ch
     */
    void choiceFaceInForge(ChoiceNothingForgeExploit ch);
}
