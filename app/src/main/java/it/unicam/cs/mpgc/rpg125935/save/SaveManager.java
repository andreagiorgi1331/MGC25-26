package it.unicam.cs.mpgc.rpg125935.save;

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
 */
public class SaveManager {

    private final Gson gson;

    public SaveManager() {
        // Configurazione di Gson.
        // Visto che "Monster" è un'interfaccia, dobbiamo dire a Gson di
        // deserializzarla sempre come un oggetto concreto "BasicMonster".
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Monster.class, (JsonDeserializer<Monster>) (json, typeOfT, context) -> 
                        context.deserialize(json, BasicMonster.class))
                .setPrettyPrinting() // Formatta il JSON in modo leggibile (con gli "a capo")
                .create();
    }

    /**
     * Salva lo stato del giocatore in un file JSON.
     * @param player Il giocatore da salvare.
     * @param filePath Il percorso del file di destinazione (es. "savegame.json").
     */
    public void saveGame(Player player, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(player, writer);
        }
    }

    /**
     * Carica lo stato del giocatore da un file JSON.
     * @param filePath Il percorso del file di salvataggio.
     * @return L'oggetto Player ricostruito.
     */
    public Player loadGame(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, Player.class);
        }
    }
}