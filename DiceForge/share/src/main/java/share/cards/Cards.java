package share.cards;

import share.cards.effects.Effect;
import share.cards.effects.TypeEffectBasique;
import share.ressource.TypeRessource;
import share.temple.Extension;
import share.temple.IslandEnum;
import share.utils.CoupleValeur;

import java.util.EnumMap;
import java.util.Map;


/**
 * The enum Cards.
 */
public enum Cards {
    /**
     * The Hammer.
     */
    HAMMER(IslandEnum.ISLAND1,0, Effect.initHammer, TypeEffectBasique.IMMEDIATE, "The Blacksmith's Hammer", Extension.STANDARD,
            new CoupleValeur(1,TypeRessource.LUNAR)),
    /**
     * The Chest.
     */
    CHEST(IslandEnum.ISLAND1,2, Effect.increaseResLimit, TypeEffectBasique.IMMEDIATE, "Chest", Extension.STANDARD,
            new  CoupleValeur(1,TypeRessource.LUNAR)),
    /**
     * The Doe.
     */
    DOE(IslandEnum.ISLAND2,2, Effect.throw1dice, TypeEffectBasique.REINFORCEMENT, "The Silver Hind", Extension.STANDARD,
            new  CoupleValeur(2,TypeRessource.LUNAR)),
    /**
     * The Satyres.
     */
    SATYRES(IslandEnum.ISLAND2,6, Effect.steal2, TypeEffectBasique.IMMEDIATE, "Satyrs", Extension.STANDARD, //todo
            new  CoupleValeur(3,TypeRessource.LUNAR)),
    /**
     * The Passeur.
     */
    PASSEUR(IslandEnum.ISLAND3,12, Effect.noaction, TypeEffectBasique.NONE,"Ferryman", Extension.STANDARD,//todo ?
            new  CoupleValeur(4,TypeRessource.LUNAR)),
    /**
     * The Invisible.
     */
    INVISIBLE(IslandEnum.ISLAND3,4,Effect.side3x, TypeEffectBasique.IMMEDIATE,"Helmet of Invisibility", Extension.STANDARD,//todo
            new  CoupleValeur(5,TypeRessource.LUNAR)),
    /**
     * The Claw.
     */
    CLAW(IslandEnum.ISLAND7,8,Effect.fullThrow, TypeEffectBasique.IMMEDIATE,"Cancer", Extension.STANDARD,//todo
            new  CoupleValeur(6,TypeRessource.LUNAR)),
    /**
     * The Ancient.
     */
    ANCIENT(IslandEnum.ISLAND4,0,Effect.goldToVP, TypeEffectBasique.REINFORCEMENT,"The Elder", Extension.STANDARD,
            new  CoupleValeur(1,TypeRessource.SOLAR)),
    /**
     * The Grass.
     */
    GRASS(IslandEnum.ISLAND4,2,Effect.G3M, TypeEffectBasique.IMMEDIATE,"Wild Spirits", Extension.STANDARD,
            new  CoupleValeur(1,TypeRessource.SOLAR)),
    /**
     * The Owl.
     */
    OWL(IslandEnum.ISLAND5,4,Effect.choose1GMF, TypeEffectBasique.REINFORCEMENT,"The Guardian's Owl", Extension.STANDARD,
            new  CoupleValeur(2,TypeRessource.SOLAR)),
    /**
     * The Minotaure.
     */
    MINOTAURE(IslandEnum.ISLAND5,8,Effect.loosThrow, TypeEffectBasique.IMMEDIATE,"Minotaur", Extension.STANDARD,
            new  CoupleValeur(3,TypeRessource.SOLAR)),
    /**
     * The Medusa.
     */
    MEDUSA(IslandEnum.ISLAND6,14,Effect.noaction, TypeEffectBasique.NONE,"Gorgon", Extension.STANDARD,
            new  CoupleValeur(4,TypeRessource.SOLAR)),
    /**
     * The Mirror.
     */
    MIRROR(IslandEnum.ISLAND6,10,Effect.sideMirror, TypeEffectBasique.IMMEDIATE,"Mirror of the Abyss", Extension.STANDARD,
            new  CoupleValeur(5,TypeRessource.SOLAR)),
    /**
     * The Enigma.
     */
    ENIGMA(IslandEnum.ISLAND7,10,Effect.Throws, TypeEffectBasique.IMMEDIATE,"SPHINX", Extension.STANDARD,
            new CoupleValeur(6,TypeRessource.SOLAR)),
    /**
     * The Hydra.
     */
    HYDRA(IslandEnum.ISLAND7,26,Effect.noaction, TypeEffectBasique.NONE,"Hydra", Extension.STANDARD, //todo ?
            new  CoupleValeur(5,TypeRessource.SOLAR),
            new  CoupleValeur(5,TypeRessource.LUNAR));

    /**
     * The Cost.
     */
    public Map<TypeRessource,Integer> cost;
    /**
     * The Glory.
     */
    public int glory;
    /**
     * The Effect.
     */
    public Effect effect;
    /**
     * The Type effect.
     */
    public TypeEffectBasique typeEffect;
    /**
     * The Name.
     */
    public String name;
    /**
     * The Extension.
     */
    public Extension extension;
    /**
     * The Island.
     */
    public IslandEnum island;

    Cards(IslandEnum island, int glory, Effect effect, TypeEffectBasique typeEffect, String name, Extension ext , CoupleValeur... listCouple){
        this.island = island;
        this.effect = effect;
        this.extension = ext;
        this.glory = glory;
        this.typeEffect = typeEffect;
        this.name = name;
        this.cost = new EnumMap<TypeRessource, Integer>(TypeRessource.class);
        for(CoupleValeur c : listCouple){
            this.cost.put(c.type,c.value);
        }
    }

    /**
     * Get value of enum by name cards.
     *
     * @param name the name
     * @return the cards
     */
    public static Cards getValueOfEnumByName(String name){
        for(Cards c : Cards.values()){
            if(c.name.equals(name)) return c;
        }
        return null;
    }
}

//  BEAR(2,Effect.noaction, TypeEffectBasique.NONE,"The Great Bear", Extension.EXTENSION,new coupleValeur(2,TypeRessource.LUNAR)),
//  CERBERUS(6,Effect.tokenCerberus, TypeEffectBasique.IMMEDIATE,"Cerberus", Extension.EXTENSION,new coupleValeur(4,TypeRessource.LUNAR)),
//  SENTINEL(6,Effect.fullThrow2Transform, TypeEffectBasique.IMMEDIATE,"sentinel", Extension.EXTENSION,new coupleValeur(6,TypeRessource.LUNAR)),
//   SHIP(4,Effect.sideShip, TypeEffectBasique.IMMEDIATE,"Celestial Ship", Extension.EXTENSION,new coupleValeur(2,TypeRessource.SOLAR)),
//  SHIELD(6,Effect.shieldForge, TypeEffectBasique.IMMEDIATE,"The Guardian's Shield", Extension.EXTENSION,new coupleValeur(3,TypeRessource.SOLAR)),
//   TRITON(8,Effect.tokenTriton, TypeEffectBasique.IMMEDIATE,"Triton", Extension.EXTENSION,new coupleValeur(4,TypeRessource.SOLAR)),
//   CYCLOPS(8,Effect.ThrowsTransform, TypeEffectBasique.IMMEDIATE,"Cyclops", Extension.EXTENSION,new coupleValeur(6,TypeRessource.SOLAR)),
// TYPHON(16,Effect.scoreForgedSides, TypeEffectBasique.IMMEDIATE,"typhon", Extension.EXTENSION,new coupleValeur(5,TypeRessource.SOLAR),new coupleValeur(5,TypeRessource.LUNAR));
  /*  BOAR(4, Effect.boarForge, TypeEffectBasique.IMMEDIATE, "Tenacious Boar", Extension.STANDARD, //todo
            new coupleValeur(3,TypeRessource.LUNAR)),*/
