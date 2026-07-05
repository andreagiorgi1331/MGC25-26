package it.unicam.cs.mpgc.rpg125935.save;

import it.unicam.cs.mpgc.rpg125935.model.creatures.BasicMonster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterStats;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterType;
import it.unicam.cs.mpgc.rpg125935.model.player.Player;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SaveManagerTest {

    @Test
    void testSaveAndLoadPlayer() throws IOException {
        // 1. Setup dei dati da salvare
        Player originalPlayer = new Player("Ash");
        originalPlayer.addItem("Pozione Salute", 5);

        MonsterStats stats = new MonsterStats(100, 20, 15, 10, 30);
        BasicMonster pikachu = new BasicMonster("Pikachu", MonsterType.NORMAL, stats);
        pikachu.addExperience(150); // Lo facciamo salire di livello per variare le statistiche base
        pikachu.takeDamage(20);     // Cambiamo i PV attuali
        
        originalPlayer.getParty().addMonster(pikachu);

        // 2. Creiamo il SaveManager e salviamo in un file temporaneo
        SaveManager saveManager = new SaveManager();
        String tempFilePath = "test_savegame.json";
        
        saveManager.saveGame(originalPlayer, tempFilePath);

        // 3. Carichiamo i dati in una nuova istanza
        Player loadedPlayer = saveManager.loadGame(tempFilePath);

        // 4. Verifiche per accertarci che la ricostruzione sia perfetta
        assertNotNull(loadedPlayer);
        assertEquals("Ash", loadedPlayer.getName());
        assertEquals(5, loadedPlayer.getInventory().get("Pozione Salute"));

        // Verifiche sul party e sul mostro
        assertEquals(1, loadedPlayer.getParty().getMonsters().size());
        
        var loadedMonster = loadedPlayer.getParty().getActiveMonster();
        assertEquals("Pikachu", loadedMonster.getName());
        assertEquals(MonsterType.NORMAL, loadedMonster.getType());
        
        // Verifichiamo che i PV attuali e l'esperienza siano stati salvati e ricaricati correttamente
        assertEquals(pikachu.getCurrentPv(), loadedMonster.getCurrentPv());
        assertEquals(pikachu.getExperience(), loadedMonster.getExperience());
        assertEquals(pikachu.getLevel(), loadedMonster.getLevel());

        // Pulizia finale: cancelliamo il file di test
        new File(tempFilePath).delete();
    }
}