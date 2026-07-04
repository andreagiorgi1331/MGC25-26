package it.unicam.cs.mpgc.rpg125935.model.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterFactory;

class GameSessionTest {

    @Test
    void shouldInitializeNewGameCorrectly() {
        GameSession session = new GameSession();
        
        assertFalse(session.isGameActive());

        // Il giocatore sceglie lo starter di tipo Fuoco
        Monster fireStarter = MonsterFactory.createFireStarter();
        session.startNewGame("Eroe", fireStarter);

        assertTrue(session.isGameActive());
        assertNotNull(session.getPlayer());
        assertEquals("Eroe", session.getPlayer().getName());
        
        // Verifica il party
        assertEquals(1, session.getPlayer().getParty().getMonsters().size());
        assertEquals("Ignis", session.getPlayer().getParty().getActiveMonster().getName());

        // Verifica l'inventario iniziale
        assertEquals(3, session.getPlayer().getInventory().get("Pozione Salute"));
    }
}