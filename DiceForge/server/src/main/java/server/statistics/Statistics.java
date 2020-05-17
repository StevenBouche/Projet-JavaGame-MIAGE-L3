package server.statistics;

import share.cards.Card;
import share.face.Face;
import share.ressource.TypeRessource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

/**
 * The type Statistics.
 */
public class Statistics {

    private static Statistics instance = null;
    private BufferedWriter writer;
    private boolean isInitialized = false;
    private boolean isEnabled = false;

    /**
     * The enum Choice id.
     */
    public enum CHOICE_ID {
        /**
         * Forge choice id.
         */
        FORGE,
        /**
         * Exploit choice id.
         */
        EXPLOIT,
        /**
         * One more turn choice id.
         */
        ONE_MORE_TURN,
        /**
         * Hybrid choice id.
         */
        HYBRID,
        /**
         * Place choice id.
         */
        PLACE,
        /**
         * Choice 3 gold for 4 glory choice id.
         */
        CHOICE_3GOLD_FOR_4GLORY,
        /**
         * Choice between ressources choice id.
         */
        CHOICE_BETWEEN_RESSOURCES,
        /**
         * Choice forge face special choice id.
         */
        CHOICE_FORGE_FACE_SPECIAL,
        /**
         * Add hammer choice id.
         */
        ADD_HAMMER,
        /**
         * Add gold to hammer choice id.
         */
        ADD_GOLD_TO_HAMMER,
        /**
         * Satyre choice choice id.
         */
        SATYRE_CHOICE,
        /**
         * Power choice choice id.
         */
        POWER_CHOICE,
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static Statistics getInstance() {
        if(instance == null) instance = new Statistics();
        return instance;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    private Statistics() {}

    /**
     * Close writer.
     */
    public void closeWriter() {
        if(writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
           //     e.printStackTrace();
            }
        }
    }

    /**
     * Init.
     *
     * @param fileName the file name
     */
    public void init(String fileName) {
        if(!isEnabled) return;

        if(writer != null) {
            try {
                writer.close();
                writer = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer = new BufferedWriter(new FileWriter("./statistics/" +fileName + ".txt"));
            isInitialized = true;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add forge choice.
     *
     * @param playerID the player id
     * @param oldFace  the old face
     * @param newFace  the new face
     */
/*
        Ajout de choix du joueur dans le fichier texte => replacement de oldFace par newFace
     */
    public void addForgeChoice(UUID playerID, Face oldFace, Face newFace) {
        try {
            this.appendToFile(playerID + "\t" + CHOICE_ID.FORGE + "\t" + oldFace + "\t" + newFace);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add exploit choice.
     *
     * @param playerID the player id
     * @param card     the card
     */
/*
        Ajout du choix du joueur (de la forge ) dans le fichier texte
     */
    public void addExploitChoice(UUID playerID, Card card) {
        try {
            this.appendToFile(playerID + "\t" + CHOICE_ID.EXPLOIT + "\t" + card.name);
        } catch (Exception e) {
       //     e.printStackTrace();
        }
    }

    /**
     * Add one more turn choice.
     *
     * @param playerID the player id
     */
/*
        Ajout du choix du joueur one more turn dans le fichier texte
     */
    public void addOneMoreTurnChoice(UUID playerID) {
        try {
            this.appendToFile(playerID + "\t" + CHOICE_ID.ONE_MORE_TURN);
        } catch (Exception e) {
         //   e.printStackTrace();
        }
    }

    /**
     * Add hybrid choice.
     *
     * @param playerID the player id
     * @param type     the type
     * @param value    the value
     */
/*
        Ajout du choix de la carte hybride dans le fichier texte
     */
    public void addHybridChoice(UUID playerID, TypeRessource type, int value) {
        try {
            this.appendToFile(playerID + "\t" + CHOICE_ID.HYBRID + "\t" + type + "\t" + value);
        } catch (Exception e) {
        //    e.printStackTrace();
        }
    }

    /**
     * Add 3 gold for 4 glory choice.
     *
     * @param playerID    the player id
     * @param hasAccepted the has accepted
     */
/*
        Ajout du choix 3 gold contre 4 glory
     */
    public void add3GoldFor4GloryChoice(UUID playerID, boolean hasAccepted) {
        try {
            this.appendToFile(playerID + "\t" + CHOICE_ID.CHOICE_3GOLD_FOR_4GLORY + "\t" + (hasAccepted ? "true" : "false") );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add choice between ressources.
     *
     * @param playerID the player id
     * @param type     the type
     * @param amount   the amount
     */
/*
        Ajout du choix d'une ressource parmis une liste de ressources
     */
    public void addChoiceBetweenRessources(UUID playerID, TypeRessource type, int amount) {
        try {
            this.appendToFile(playerID + "\t" + CHOICE_ID.CHOICE_BETWEEN_RESSOURCES + "\t" + type + "\t" + amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add choice forge face special.
     *
     * @param playerID the player id
     * @param oldFace  the old face
     * @param newFace  the new face
     */
/*
        Ajout du choix de la forge d'une face speciale
     */
    public void addChoiceForgeFaceSpecial(UUID playerID, Face oldFace, Face newFace) {
        try {
            this.appendToFile(playerID + "\t" + CHOICE_ID.CHOICE_FORGE_FACE_SPECIAL + "\t" + oldFace + "\t" + newFace);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add hammer to player.
     *
     * @param playerID the player id
     */
/*
        Ajout de l'achat du hammer
     */
    public void addHammerToPlayer(UUID playerID) {
        try {
            this.appendToFile(playerID + "\t" + CHOICE_ID.ADD_HAMMER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add gold to hammer.
     *
     * @param playerID the player id
     * @param ammount  the ammount
     */
/*
        Ajout du choix du nombre de gold que veux ajouter le player à son hammer
     */
    public void addGoldToHammer(UUID playerID, int ammount) {
        try {
            this.appendToFile(playerID + "\t" + CHOICE_ID.ADD_GOLD_TO_HAMMER + "\t" + ammount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add satyre choice.
     *
     * @param playerId the player id
     * @param faceOne  the face one
     * @param faceTwo  the face two
     */
/*
        Ajout du choix de satyre
     */
    public void addSatyreChoice(UUID playerId, Face faceOne, Face faceTwo) {
        try {
            this.appendToFile(playerId + "\t" + CHOICE_ID.SATYRE_CHOICE + "\t" + faceOne + "\t" + faceTwo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add face choice power choice.
     *
     * @param playerId the player id
     * @param face     the face
     */
/*
        Ajout du choix face power
     */
    public void addFaceChoicePowerChoice(UUID playerId, Face face) {
        try {
            this.appendToFile(playerId + "\t" + CHOICE_ID.POWER_CHOICE + "\t" + face);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add end game results.
     *
     * @param line the line
     */
    public void addEndGameResults(String line) {
        try {
            this.appendToFile(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendToFile(String msg) throws Exception {
        if(!isEnabled) return;
        if(!isInitialized) throw new Exception("Fichier de statistiques non initialisé.");

        try {
            writer.write(msg + "\n");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
