package it.unicam.cs.mpgc.rpg125935.model.moves;

import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterType;

/**
 * Effetto di danno fisico che calcola i danni in base alle statistiche 
 * e ai moltiplicatori di tipo.
 */
public class DamageEffect implements MoveEffect {

    private final int basePower;
    private final MonsterType moveType;

    public DamageEffect(int basePower, MonsterType moveType) {
        this.basePower = basePower;
        this.moveType = moveType;
    }

    @Override
    public void apply(Monster attacker, Monster defender) {
        // Calcolo del moltiplicatore di tipo (es. Fuoco contro Erba)
        double typeMultiplier = moveType.getEffectivenessMultiplier(defender.getType());
        
        // Formula del danno semplificata: (Potenza Base * (Attacco / Difesa)) * Moltiplicatore
        double damageCalc = basePower * ((double) attacker.getBaseStats().attack() / defender.getBaseStats().defense()) * typeMultiplier;
        
        // Assicuriamo che la mossa faccia almeno 1 di danno, convertendo in intero
        int finalDamage = Math.max(1, (int) Math.round(damageCalc));
        
        defender.takeDamage(finalDamage);
    }
}
