package it.unicam.cs.mpgc.rpg125935.save;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import it.unicam.cs.mpgc.rpg125935.model.creatures.BasicMonster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.player.Player;

/**
 * Gestisce il salvataggio e il caricamento dei dati di gioco tramite Gson.
 * Supporta 3 slot di salvataggio distinti.
 */
public class SaveManager {

    private final Gson gson;

    public SaveManager() {
        // Configurazione di Gson.
        // Visto che "Monster" e "MoveEffect" sono interfacce, dobbiamo dire a Gson di
        // deserializzarle sempre come oggetti concreti "BasicMonster" e "DamageEffect".
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Monster.class, (JsonDeserializer<Monster>) (json, typeOfT, context) -> 
                        context.deserialize(json, BasicMonster.class))
                .registerTypeAdapter(it.unicam.cs.mpgc.rpg125935.model.moves.MoveEffect.class, (JsonDeserializer<it.unicam.cs.mpgc.rpg125935.model.moves.MoveEffect>) (json, typeOfT, context) -> 
                        context.deserialize(json, it.unicam.cs.mpgc.rpg125935.model.moves.DamageEffect.class))
                .setPrettyPrinting() // Formatta il JSON in modo leggibile
                .create();
    }

    /**
     * Restituisce il nome del file di salvataggio per un dato slot.
     */
    public String getSaveFileName(int slotIndex) {
        return "savegame_" + slotIndex + ".json";
    }

    /**
     * Verifica se un determinato slot di salvataggio esiste.
     */
    public boolean slotExists(int slotIndex) {
        return new File(getSaveFileName(slotIndex)).exists();
    }

    /**
     * Ottiene lo stato di salvataggio per uno slot specifico, se esiste.
     * @return GameSaveState se esiste, altrimenti null.
     */
    public GameSaveState getSaveState(int slotIndex) {
        if (!slotExists(slotIndex)) {
            return null;
        }
        try {
            return loadGame(slotIndex);
        } catch (IOException e) {
            System.err.println("Errore di caricamento slot " + slotIndex + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Salva lo stato del gioco in un file JSON relativo allo slot specificato.
     */
    public void saveGame(Player player, int currentStage, int slotIndex) throws IOException {
        GameSaveState state = new GameSaveState(player, currentStage, slotIndex);
        String filePath = getSaveFileName(slotIndex);
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(state, writer);
        }
    }

    /**
     * Carica lo stato del gioco da un file JSON relativo allo slot specificato.
     */
    public GameSaveState loadGame(int slotIndex) throws IOException {
        String filePath = getSaveFileName(slotIndex);
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, GameSaveState.class);
        }
    }
}