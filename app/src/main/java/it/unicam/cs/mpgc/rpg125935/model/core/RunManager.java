package it.unicam.cs.mpgc.rpg125935.model.core;

import it.unicam.cs.mpgc.rpg125935.model.battle.BattleManager;
import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterFactory;
import it.unicam.cs.mpgc.rpg125935.model.player.Player;

/**
 * Gestisce il ciclo (loop) di un'esplorazione sequenziale stile "roguelike".
 * Tiene traccia dello stage corrente e orchestra i passaggi tra Hub e Combattimento.
 */
public class RunManager {

    private final Player player;
    private int currentStage;
    private BattleManager currentBattle;
    private int slotIndex; // 1, 2, o 3

    public RunManager(Player player, int slotIndex) {
        this.player = player;
        this.currentStage = 0;
        this.slotIndex = slotIndex;
    }

    public RunManager(Player player, int currentStage, int slotIndex) {
        this.player = player;
        this.currentStage = currentStage;
        this.slotIndex = slotIndex;
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public BattleManager getCurrentBattle() {
        return currentBattle;
    }

    /**
     * @return true se il giocatore è in mezzo a un combattimento.
     */
    public boolean isInBattle() {
        return currentBattle != null && !currentBattle.isBattleOver();
    }

    /**
     * Inizia il prossimo scontro generando un nemico di livello adeguato.
     */
    public void startNextEncounter() {
        if (player.getParty().isAllFainted()) {
            throw new IllegalStateException("Game Over! Tutti i tuoi mostri sono esausti.");
        }
        if (isInBattle()) {
            throw new IllegalStateException("Devi prima finire il combattimento attuale!");
        }

        currentStage++;
        Monster enemy = MonsterFactory.generateRandomEnemy(currentStage);
        
        // Inizializza la battaglia usando il mostro attivo del giocatore
        currentBattle = new BattleManager(player.getParty().getActiveMonster(), enemy);
    }

    /**
     * Conclude lo scontro attuale. Se il giocatore ha vinto, ottiene una ricompensa.
     */
    public void resolveEncounter() {
        if (currentBattle != null) {
            if (currentBattle.getWinner() == player.getParty().getActiveMonster()) {
                Monster myMonster = player.getParty().getActiveMonster();
                Monster enemy = currentBattle.getEnemyMonster();

                // 1. Calcolo ed erogazione XP (usando i tuoi metodi!)
                int xpGained = enemy.getLevel() * 50; 
                myMonster.addExperience(xpGained);

                System.out.println("Scontro Vinto! " + myMonster.getName() + " guadagna " + xpGained + " XP!");

                // 2. Progressione e Drop
                currentStage++;
                player.addItem("Pozione Salute", 1);
            }
            
            // 3. Resettiamo sempre la battaglia per l'Hub
            this.currentBattle = null;
        }
    }
}