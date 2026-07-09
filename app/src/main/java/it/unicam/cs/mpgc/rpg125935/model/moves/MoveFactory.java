package it.unicam.cs.mpgc.rpg125935.model.moves;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterType;

/**
 * Factory class per il caricamento e la creazione di mosse da file JSON.
 * Mantiene in cache i dati caricati per ottimizzare le prestazioni.
 */
public class MoveFactory {
    
    private static List<MoveData> cachedMoveDataList = null;

    private static class MoveData {
        String name;
        String type;
        int basePower;
    }

    private static void loadMovesIfNecessary() {
        if (cachedMoveDataList != null) {
            return;
        }
        try (InputStream is = MoveFactory.class.getResourceAsStream("/data/moves.json")) {
            if (is == null) {
                System.err.println("Errore: moves.json non trovato.");
                cachedMoveDataList = new ArrayList<>();
                return;
            }
            Gson gson = new Gson();
            cachedMoveDataList = gson.fromJson(new InputStreamReader(is), new TypeToken<List<MoveData>>(){}.getType());
        } catch (Exception e) {
            System.err.println("Errore nel caricamento di moves.json: " + e.getMessage());
            cachedMoveDataList = new ArrayList<>();
        }
    }

    /**
     * Crea un'istanza di Move configurata con nome, tipo ed effetto prelevati dal file JSON.
     * Se la mossa non è presente, viene restituita una mossa di tipo NORMAL di fallback.
     */
    public static Move createMove(String name) {
        loadMovesIfNecessary();
        for (MoveData data : cachedMoveDataList) {
            if (data.name.equalsIgnoreCase(name)) {
                MonsterType moveType = MonsterType.valueOf(data.type.toUpperCase());
                return new Move(data.name, moveType, new DamageEffect(data.basePower, moveType));
            }
        }
        // Fallback in caso di mossa non trovata
        return new Move(name, MonsterType.NORMAL, new DamageEffect(35, MonsterType.NORMAL));
    }
}
