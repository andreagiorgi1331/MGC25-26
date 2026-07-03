package it.unicam.cs.mpgc.rpg125935.model.creatures;

/**
 * Rappresenta l'elemento/tipo di un mostro e gestisce
 * il calcolo dei moltiplicatori di danno (resistenze e debolezze).
 */
public enum MonsterType {
    NORMAL,
    FIRE,
    WATER,
    GRASS;

    /**
     * Calcola il moltiplicatore di danno di questo tipo contro un tipo difensore.
     * * @param defenderType Il tipo del mostro che riceve l'attacco.
     * @return 2.0 per danno superefficace, 0.5 per non molto efficace, 1.0 per danno normale.
     */
    public double getEffectivenessMultiplier(MonsterType defenderType) {
        if (this == FIRE) {
            if (defenderType == GRASS) return 2.0;
            if (defenderType == WATER) return 0.5;
            if (defenderType == FIRE) return 0.5;
        } else if (this == WATER) {
            if (defenderType == FIRE) return 2.0;
            if (defenderType == GRASS) return 0.5;
            if (defenderType == WATER) return 0.5;
        } else if (this == GRASS) {
            if (defenderType == WATER) return 2.0;
            if (defenderType == FIRE) return 0.5;
            if (defenderType == GRASS) return 0.5;
        }
        
        // Il tipo NORMAL o qualsiasi combinazione non specificata fa danno neutro
        return 1.0;
    }
}