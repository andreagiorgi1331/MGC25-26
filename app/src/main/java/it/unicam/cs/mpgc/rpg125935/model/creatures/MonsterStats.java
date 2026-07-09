package it.unicam.cs.mpgc.rpg125935.model.creatures;

/**
 * Rappresenta le statistiche di base immutabili di un mostro.
 * Implementato come classe standard compatibile al 100% con Gson.
 */
public class MonsterStats {
    private final int maxPv;
    private final int attack;
    private final int defense;
    private final int magic;
    private final int speed;

    public MonsterStats(int maxPv, int attack, int defense, int magic, int speed) {
        this.maxPv = maxPv;
        this.attack = attack;
        this.defense = defense;
        this.magic = magic;
        this.speed = speed;
    }

    public int maxPv() {
        return maxPv;
    }

    public int attack() {
        return attack;
    }

    public int defense() {
        return defense;
    }

    public int magic() {
        return magic;
    }

    public int speed() {
        return speed;
    }
}