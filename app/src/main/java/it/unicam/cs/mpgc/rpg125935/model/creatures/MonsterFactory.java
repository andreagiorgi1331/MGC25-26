package it.unicam.cs.mpgc.rpg125935.model.creatures;

/**
 * Factory class per la creazione di mostri preimpostati.
 * Nasconde la complessità dell'inizializzazione delle statistiche.
 */
public class MonsterFactory {

    public static Monster createFireStarter() {
        // PV, Atk, Def, Magic, SPD
        MonsterStats stats = new MonsterStats(100, 20, 10, 15, 25);
        return new BasicMonster("Ignis", MonsterType.FIRE, stats);
    }

    public static Monster createWaterStarter() {
        MonsterStats stats = new MonsterStats(120, 15, 20, 10, 15);
        return new BasicMonster("Aqua", MonsterType.WATER, stats);
    }

    public static Monster createGrassStarter() {
        MonsterStats stats = new MonsterStats(110, 15, 15, 25, 20);
        return new BasicMonster("Flora", MonsterType.GRASS, stats);
    }

    /**
     * Genera un nemico casuale le cui statistiche scalano in base allo stage attuale.
     * @param stage Il livello di profondità della run.
     */
    public static Monster generateRandomEnemy(int stage) {
        // Le statistiche aumentano progressivamente con lo stage
        int hp = 40 + (stage * 15);
        int atk = 10 + (stage * 3);
        int def = 10 + (stage * 2);
        int magic = 10 + (stage * 2);
        int spd = 10 + stage;

        MonsterStats enemyStats = new MonsterStats(hp, atk, def, magic, spd);
        
        // Per semplicità, in questa fase diamo un tipo base, ma potresti randomizzarlo!
        MonsterType[] types = MonsterType.values();
        MonsterType randomType = types[(int)(Math.random() * types.length)];

        return new BasicMonster("Mostro Livello " + stage, randomType, enemyStats);
    }

}