package it.unicam.cs.mpgc.rpg125935.model.creatures;

/**
 * Implementazione base di una creatura nel gioco.
 * Gestisce lo stato attuale dei Punti Vita (PV) e le operazioni di danno/cura.
 */
public class BasicMonster implements Monster {

    private final String name;
    private final MonsterType type;
    private final MonsterStats baseStats;
    private int currentPv;

    /**
     * Costruisce un nuovo BasicMonster.
     * * @param name      Il nome del mostro.
     * @param type      Il tipo elementale del mostro.
     * @param baseStats Le statistiche di base.
     */
    public BasicMonster(String name, MonsterType type, MonsterStats baseStats) {
        this.name = name;
        this.type = type;
        this.baseStats = baseStats;
        this.currentPv = baseStats.maxPv();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public MonsterType getType() {
        return this.type;
    }

    @Override
    public MonsterStats getBaseStats() {
        return this.baseStats;
    }

    @Override
    public int getCurrentPv() {
        return this.currentPv;
    }

    @Override
    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Il danno non può essere negativo");
        }
        this.currentPv = Math.max(0, this.currentPv - damage);
    }

    @Override
    public void heal(int healAmount) {
        if (healAmount < 0) {
            throw new IllegalArgumentException("La quantità di cura non può essere negativa");
        }
        this.currentPv = Math.min(this.baseStats.maxPv(), this.currentPv + healAmount);
    }

    @Override
    public boolean isFainted() {
        return this.currentPv <= 0;
    }
}