package it.unicam.cs.mpgc.rpg125935.model.creatures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class BasicMonsterTest {

    @Test
    void shouldInitializeWithCorrectTypeAndMaxPv() {
        MonsterStats stats = new MonsterStats(100, 15, 10, 20, 25);
        // Passiamo MonsterType.FIRE al costruttore
        BasicMonster monster = new BasicMonster("Draghetto", MonsterType.FIRE, stats);

        assertEquals(MonsterType.FIRE, monster.getType());
        assertEquals(100, monster.getCurrentPv());
        assertFalse(monster.isFainted());
    }

    @Test
    void shouldTakeDamageCorrectly() {
        MonsterStats stats = new MonsterStats(100, 15, 10, 20, 25);
        BasicMonster monster = new BasicMonster("Draghetto", MonsterType.FIRE, stats);

        monster.takeDamage(30);
        assertEquals(70, monster.getCurrentPv());
    }

    @Test
    void shouldNotDropBelowZeroPv() {
        MonsterStats stats = new MonsterStats(50, 15, 10, 20, 25);
        BasicMonster monster = new BasicMonster("Slime", MonsterType.WATER, stats);

        monster.takeDamage(100);

        assertEquals(0, monster.getCurrentPv());
        assertTrue(monster.isFainted());
    }

    @Test
    void shouldHealCorrectlyButNotExceedMax() {
        MonsterStats stats = new MonsterStats(100, 15, 10, 20, 25);
        BasicMonster monster = new BasicMonster("Golem", MonsterType.NORMAL, stats);

        monster.takeDamage(60);
        monster.heal(20);

        assertEquals(60, monster.getCurrentPv());

        monster.heal(100);
        assertEquals(100, monster.getCurrentPv());
    }

    @Test
    void shouldThrowExceptionOnNegativeValues() {
        MonsterStats stats = new MonsterStats(100, 15, 10, 20, 25);
        BasicMonster monster = new BasicMonster("Spettro", MonsterType.GRASS, stats);

        assertThrows(IllegalArgumentException.class, () -> monster.takeDamage(-10));
        assertThrows(IllegalArgumentException.class, () -> monster.heal(-20));
    }
}