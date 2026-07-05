package it.unicam.cs.mpgc.rpg125935.model.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterFactory;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterType;
import it.unicam.cs.mpgc.rpg125935.model.moves.DamageEffect;
import it.unicam.cs.mpgc.rpg125935.model.moves.Move;
import it.unicam.cs.mpgc.rpg125935.model.player.Player;

class RunManagerTest {

    @Test
    void testEndlessRunLoop() {
        // Setup iniziale del giocatore
        Player player = new Player("Ash");
        player.getParty().addMonster(MonsterFactory.createFireStarter());
        
        RunManager run = new RunManager(player);
        
        // L'esplorazione inizia a stage 0, nessuna pozione nell'inventario
        assertEquals(0, run.getCurrentStage());
        assertFalse(player.getInventory().containsKey("Pozione Salute"));

        // ----- STAGE 1 -----
        run.startNextEncounter();
        assertEquals(1, run.getCurrentStage());
        assertTrue(run.isInBattle());

        // Simuliamo un attacco potentissimo per finire istantaneamente la battaglia
        Move opAttack = new Move("Attacco Divino", new DamageEffect(999, MonsterType.NORMAL));
        run.getCurrentBattle().playTurn(opAttack, opAttack);

        // La battaglia finisce, il giocatore ha vinto
        assertFalse(run.isInBattle());
        run.resolveEncounter(); // Richiede la ricompensa e torna all'Hub

        // Verifichiamo la ricompensa
        assertEquals(1, player.getInventory().get("Pozione Salute"));
        assertNull(run.getCurrentBattle()); // Siamo tornati all'Hub

        // ----- STAGE 2 -----
        run.startNextEncounter();
        assertEquals(2, run.getCurrentStage());
        
        // Verifichiamo che il nemico del secondo stage sia più forte (più PV base)
        int hpStage2 = run.getCurrentBattle().getEnemyMonster().getBaseStats().maxPv();
        assertTrue(hpStage2 > 40); // Più degli hp base calcolati nel livello 1
    }
}