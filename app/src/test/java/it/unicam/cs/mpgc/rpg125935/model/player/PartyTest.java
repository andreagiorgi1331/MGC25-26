package it.unicam.cs.mpgc.rpg125935.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import it.unicam.cs.mpgc.rpg125935.model.creatures.BasicMonster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterStats;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterType;

class PartyTest {

    @Test
    void shouldNotExceedMaximumOfFourMonsters() {
        Party party = new Party();
        MonsterStats stats = new MonsterStats(50, 10, 10, 10, 10);

        // Aggiungiamo i primi 4 mostri con successo
        party.addMonster(new BasicMonster("Mostro 1", MonsterType.NORMAL, stats));
        party.addMonster(new BasicMonster("Mostro 2", MonsterType.NORMAL, stats));
        party.addMonster(new BasicMonster("Mostro 3", MonsterType.NORMAL, stats));
        party.addMonster(new BasicMonster("Mostro 4", MonsterType.NORMAL, stats));

        assertEquals(4, party.getMonsters().size());

        // Il quinto mostro deve lanciare un'eccezione IllegalStateException
        assertThrows(IllegalStateException.class, () -> {
            party.addMonster(new BasicMonster("Mostro 5", MonsterType.NORMAL, stats));
        });
    }

    @Test
    void testSwapAndActiveMonster() {
        Party party = new Party();
        MonsterStats stats = new MonsterStats(50, 10, 10, 10, 10);
        BasicMonster m1 = new BasicMonster("Primo", MonsterType.FIRE, stats);
        BasicMonster m2 = new BasicMonster("Secondo", MonsterType.WATER, stats);

        party.addMonster(m1);
        party.addMonster(m2);

        // All'inizio il mostro attivo è il primo
        assertEquals(m1, party.getActiveMonster());

        // Facciamo lo swap tra posizione 0 e 1
        party.swapMonsters(0, 1);

        // Ora il mostro attivo deve essere il secondo
        assertEquals(m2, party.getActiveMonster());
    }
}