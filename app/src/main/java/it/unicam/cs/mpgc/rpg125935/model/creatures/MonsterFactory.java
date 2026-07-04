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
}