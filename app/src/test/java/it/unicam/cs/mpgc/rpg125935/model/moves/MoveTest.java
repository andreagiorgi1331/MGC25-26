package it.unicam.cs.mpgc.rpg125935.model.moves;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unicam.cs.mpgc.rpg125935.model.creatures.BasicMonster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterStats;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterType;

class MoveTest {

    @Test
    void testSuperEffectiveDamageMove() {
        // Attaccante: Statistiche base
        MonsterStats attackerStats = new MonsterStats(100, 20, 10, 10, 15);
        BasicMonster fireDragon = new BasicMonster("Drago Rosso", MonsterType.FIRE, attackerStats);

        // Difensore: Debole al fuoco
        MonsterStats defenderStats = new MonsterStats(100, 10, 10, 10, 10);
        BasicMonster grassPlant = new BasicMonster("Bulbo", MonsterType.GRASS, defenderStats);

        // Creazione di una mossa di Fuoco con 40 di potenza base
        MoveEffect fireballEffect = new DamageEffect(40, MonsterType.FIRE);
        Move fireball = new Move("Palla di Fuoco", fireballEffect);

        // Calcolo teorico: 40 * (20/10) * 2.0 (Superefficace) = 160 danni
        fireball.execute(fireDragon, grassPlant);

        // Il mostro d'Erba dovrebbe essere esausto (PV = 0)
        assertTrue(grassPlant.isFainted());
    }

    @Test
    void testNotVeryEffectiveDamageMove() {
        MonsterStats attackerStats = new MonsterStats(100, 20, 10, 10, 15);
        BasicMonster fireDragon = new BasicMonster("Drago Rosso", MonsterType.FIRE, attackerStats);

        // Difensore: Resistente al fuoco
        MonsterStats defenderStats = new MonsterStats(100, 10, 20, 10, 10);
        BasicMonster waterTurtle = new BasicMonster("Tarta", MonsterType.WATER, defenderStats);

        MoveEffect fireballEffect = new DamageEffect(40, MonsterType.FIRE);
        Move fireball = new Move("Palla di Fuoco", fireballEffect);

        // Calcolo teorico: 40 * (20/20) * 0.5 (Poco Efficace) = 20 danni
        fireball.execute(fireDragon, waterTurtle);

        // La Tartaruga aveva 100 PV, ha subito 20 danni, quindi deve averne 80
        assertEquals(80, waterTurtle.getCurrentPv());
    }
}