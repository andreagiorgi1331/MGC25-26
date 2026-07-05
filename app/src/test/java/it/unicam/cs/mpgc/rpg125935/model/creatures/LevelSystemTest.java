package it.unicam.cs.mpgc.rpg125935.model.creatures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class LevelSystemTest {

    @Test
    void testMonsterLevelsUpAndStatsIncrease() {
        MonsterStats base = new MonsterStats(100, 20, 20, 20, 20);
        BasicMonster monster = new BasicMonster("Charmander", MonsterType.FIRE, base);

        assertEquals(1, monster.getLevel());
        assertEquals(0, monster.getExperience());

        // Al livello 1 servono 100 XP per salire. Diamo 150 XP.
        monster.addExperience(150);

        // Deve essere salito al livello 2, e gli devono essere rimasti 50 XP
        assertEquals(2, monster.getLevel());
        assertEquals(50, monster.getExperience());

        // Le statistiche dovrebbero essere aumentate del 10%
        // Max PV: 100 * 1.1 = 110
        assertEquals(110, monster.getBaseStats().maxPv());
        // Attacco: 20 * 1.1 = 22
        assertEquals(22, monster.getBaseStats().attack());
        
        // Verifica che il mostro sia stato curato al level up
        assertEquals(110, monster.getCurrentPv());
    }

    @Test
    void testMultipleLevelUpsAtOnce() {
        MonsterStats base = new MonsterStats(100, 20, 20, 20, 20);
        BasicMonster monster = new BasicMonster("Bulbasaur", MonsterType.GRASS, base);

        // Livello 1 -> 2 (100 XP)
        // Livello 2 -> 3 (200 XP)
        // Totale per arrivare al 3 = 300 XP. Diamo 350 XP.
        monster.addExperience(350);

        assertEquals(3, monster.getLevel());
        assertEquals(50, monster.getExperience());
    }
}