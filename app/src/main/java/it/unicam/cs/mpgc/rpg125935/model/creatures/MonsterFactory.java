package it.unicam.cs.mpgc.rpg125935.model.creatures;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg125935.model.moves.MoveFactory;

/**
 * Factory class per la creazione di mostri preimpostati e casuali da un file JSON.
 * Mantiene in cache i dati dei mostri per ottimizzare le prestazioni.
 */
public class MonsterFactory {

    private static List<MonsterTemplate> cachedTemplates = null;

    private static class MonsterTemplate {
        String name;
        String type;
        MonsterStats baseStats;
        List<String> moves;
    }

    private static void loadMonstersIfNecessary() {
        if (cachedTemplates != null) {
            return;
        }
        try (InputStream is = MonsterFactory.class.getResourceAsStream("/data/monsters.json")) {
            if (is == null) {
                System.err.println("Errore: monsters.json non trovato.");
                cachedTemplates = new ArrayList<>();
                return;
            }
            Gson gson = new Gson();
            cachedTemplates = gson.fromJson(new InputStreamReader(is), new TypeToken<List<MonsterTemplate>>(){}.getType());
        } catch (Exception e) {
            System.err.println("Errore nel caricamento di monsters.json: " + e.getMessage());
            cachedTemplates = new ArrayList<>();
        }
    }

    private static Monster createMonsterFromTemplate(MonsterTemplate template) {
        MonsterType mType = MonsterType.valueOf(template.type.toUpperCase());
        BasicMonster monster = new BasicMonster(template.name, mType, template.baseStats);
        
        // Popola il set di mosse dal file JSON usando MoveFactory
        if (template.moves != null) {
            for (String moveName : template.moves) {
                monster.addMove(MoveFactory.createMove(moveName));
            }
        }
        return monster;
    }

    public static Monster createFireStarter() {
        loadMonstersIfNecessary();
        for (MonsterTemplate t : cachedTemplates) {
            if (t.name.equalsIgnoreCase("Ignis")) {
                return createMonsterFromTemplate(t);
            }
        }
        // Fallback
        BasicMonster ignis = new BasicMonster("Ignis", MonsterType.FIRE, new MonsterStats(100, 20, 10, 15, 25));
        ignis.addMove(MoveFactory.createMove("Braciere"));
        ignis.addMove(MoveFactory.createMove("Azione"));
        return ignis;
    }

    public static Monster createWaterStarter() {
        loadMonstersIfNecessary();
        for (MonsterTemplate t : cachedTemplates) {
            if (t.name.equalsIgnoreCase("Aqua")) {
                return createMonsterFromTemplate(t);
            }
        }
        // Fallback
        BasicMonster aqua = new BasicMonster("Aqua", MonsterType.WATER, new MonsterStats(120, 15, 20, 10, 15));
        aqua.addMove(MoveFactory.createMove("Pistolacqua"));
        aqua.addMove(MoveFactory.createMove("Scontro"));
        return aqua;
    }

    public static Monster createGrassStarter() {
        loadMonstersIfNecessary();
        for (MonsterTemplate t : cachedTemplates) {
            if (t.name.equalsIgnoreCase("Flora")) {
                return createMonsterFromTemplate(t);
            }
        }
        // Fallback
        BasicMonster flora = new BasicMonster("Flora", MonsterType.GRASS, new MonsterStats(110, 15, 15, 25, 20));
        flora.addMove(MoveFactory.createMove("Fogliame"));
        flora.addMove(MoveFactory.createMove("Graffio"));
        return flora;
    }

    /**
     * Genera un nemico casuale dal file JSON le cui statistiche e livello scalano in base allo stage attuale.
     * @param stage Il livello di profondità della run.
     */
    public static Monster generateRandomEnemy(int stage) {
        loadMonstersIfNecessary();
        if (cachedTemplates.isEmpty()) {
            // Fallback estremo se il JSON è vuoto
            BasicMonster fallback = new BasicMonster("Mostro Livello " + stage, MonsterType.NORMAL, 
                new MonsterStats(40 + stage * 15, 10 + stage * 3, 10 + stage * 2, 10 + stage * 2, 10 + stage));
            fallback.addMove(MoveFactory.createMove("Azione"));
            return fallback;
        }

        // Seleziona un template a caso dal JSON
        int randomIndex = (int)(Math.random() * cachedTemplates.size());
        MonsterTemplate template = cachedTemplates.get(randomIndex);

        // Scala le statistiche in base allo stage (livello = stage)
        double multiplier = 1.0 + (stage - 1) * 0.12; // 12% di crescita a livello
        int scaledHp = (int) Math.round(template.baseStats.maxPv() * multiplier);
        int scaledAtk = (int) Math.round(template.baseStats.attack() * multiplier);
        int scaledDef = (int) Math.round(template.baseStats.defense() * multiplier);
        int scaledMagic = (int) Math.round(template.baseStats.magic() * multiplier);
        int scaledSpd = (int) Math.round(template.baseStats.speed() * multiplier);

        MonsterStats scaledStats = new MonsterStats(scaledHp, scaledAtk, scaledDef, scaledMagic, scaledSpd);

        BasicMonster enemy = new BasicMonster(template.name, MonsterType.valueOf(template.type.toUpperCase()), scaledStats);
        
        // Assegna un livello pari allo stage
        for (int i = 1; i < stage; i++) {
            // Aumenta semplicemente il livello a livello dello stage senza ricalcolare (le stats le abbiamo già scalate)
            enemy.addExperience(i * 100); 
        }

        // Popola mosse
        if (template.moves != null) {
            for (String moveName : template.moves) {
                enemy.addMove(MoveFactory.createMove(moveName));
            }
        } else {
            enemy.addMove(MoveFactory.createMove("Azione"));
        }

        return enemy;
    }
}