package it.unicam.cs.mpgc.rpg125935.model.creatures;

/*** Interfaccia base che definisce il comportamento di una creatura nel gioco.*/
public interface Monster {
    /**
     * @return Il nome del mostro.
     */
    String getName();

    /**
     * @return Il tipo del mostro.
     */
    MonsterType getType();

    /**
     * @return Le statistiche di base del mostro.
     */
    MonsterStats getBaseStats();

    /**
     * @return I Punti Vita (Pv) attuali del mostro.
     */
    int getCurrentPv();

    /**
     * Applica un danno al mostro, riducendo i suoi Pv attuali.
     * @param damage L'ammontare di danno calcolato da infliggere.
     */
    void takeDamage(int damage);

    /**
     * Cura il mostro, ripristinando i Pv (senza superare i maxPv).
     * @param healAmount La quantità di salute da ripristinare.
     */
    void heal(int healAmount);

    /**
     * @return true se i Pv attuali sono scesi a 0 o meno.
     */
    boolean isFainted();

    /**
     * @return Il livello attuale del mostro.
     */
    int getLevel();

    /**
     * @return I punti esperienza (XP) attuali.
     */
    int getExperience();

    /**
     * Aggiunge punti esperienza al mostro. Se supera la soglia, sale di livello.
     * @param xp La quantità di esperienza da aggiungere.
     */
    void addExperience(int xp);
}
