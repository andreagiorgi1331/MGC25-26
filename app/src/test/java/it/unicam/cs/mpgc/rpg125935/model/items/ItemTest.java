package it.unicam.cs.mpgc.rpg125935.model.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import it.unicam.cs.mpgc.rpg125935.model.creatures.BasicMonster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterStats;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterType;
import it.unicam.cs.mpgc.rpg125935.model.player.Player;

class ItemTest {

    @Test
    void testUseHealthPotionOnDamagedMonster() {
        // Prepariamo il mostro
        MonsterStats stats = new MonsterStats(100, 10, 10, 10, 10);
        BasicMonster monster = new BasicMonster("Pikachu", MonsterType.NORMAL, stats);
        monster.takeDamage(60); // PV scendono a 40
        
        // Creiamo la pozione e la usiamo
        Item potion = ItemFactory.createHealthPotion();
        potion.useOn(monster);
        
        // 40 + 50 (della pozione) = 90 PV
        assertEquals(90, monster.getCurrentPv());
    }

    @Test
    void testCannotUsePotionOnFaintedMonster() {
        MonsterStats stats = new MonsterStats(100, 10, 10, 10, 10);
        BasicMonster faintedMonster = new BasicMonster("Pikachu", MonsterType.NORMAL, stats);
        faintedMonster.takeDamage(150); // Va K.O.

        Item potion = ItemFactory.createHealthPotion();

        // Deve lanciare un'eccezione perché il mostro è K.O.
        assertThrows(IllegalStateException.class, () -> {
            potion.useOn(faintedMonster);
        });
    }

    @Test
    void testPlayerInventoryIntegration() {
        Player player = new Player("Eroe");
        player.addItem("Pozione Salute", 1);
        
        // Simula il click del giocatore nell'Hub per usare l'oggetto
        player.useItem("Pozione Salute"); 
        
        // Verifichiamo che la pozione sia stata rimossa dall'inventario
        assertFalse(player.getInventory().containsKey("Pozione Salute") && player.getInventory().get("Pozione Salute") > 0);
    }
}