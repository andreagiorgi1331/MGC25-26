package it.unicam.cs.mpgc.rpg125935.model.creatures;

public class BasicMonster implements Monster {

    private String name;
    private MonsterType type;
    private MonsterStats baseStats; // NON più final!
    private int currentPv;
    
    // Nuovi attributi per il livello
    private int level;
    private int experience;

    public BasicMonster(String name, MonsterType type, MonsterStats baseStats) {
        this.name = name;
        this.type = type;
        this.baseStats = baseStats;
        this.currentPv = baseStats.maxPv();
        this.level = 1;
        this.experience = 0;
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

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getExperience() {
        return this.experience;
    }

    @Override
    public void addExperience(int xp) {
        if (xp < 0) throw new IllegalArgumentException("L'XP non può essere negativa");
        
        this.experience += xp;
        
        // Calcolo della soglia (Es. Livello 1 = 100 XP, Livello 2 = 200 XP, ecc.)
        int requiredXp = this.level * 100;
        
        // Usiamo un while nel caso in cui ottenga così tanta XP da fare più livelli insieme
        while (this.experience >= requiredXp) {
            this.experience -= requiredXp;
            levelUp();
            requiredXp = this.level * 100; // Aggiorna la soglia per il prossimo ciclo
        }
    }

    /**
     * Gestisce l'aumento delle statistiche.
     */
    private void levelUp() {
        this.level++;
        
        // Aumentiamo tutte le statistiche del 10% (moltiplicatore 1.1)
        int newMaxPv = (int) Math.round(baseStats.maxPv() * 1.1);
        int newAtk = (int) Math.round(baseStats.attack() * 1.1);
        int newDef = (int) Math.round(baseStats.defense() * 1.1);
        int newMagic = (int) Math.round(baseStats.magic() * 1.1);
        int newSpd = (int) Math.round(baseStats.speed() * 1.1);

        this.baseStats = new MonsterStats(newMaxPv, newAtk, newDef, newMagic, newSpd);
        
        // Bonus stile PokéRogue: al passaggio di livello, il mostro si cura un po' o del tutto!
        this.currentPv = this.baseStats.maxPv();
    }

}