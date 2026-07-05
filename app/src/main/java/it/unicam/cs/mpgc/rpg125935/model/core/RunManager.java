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

    public RunManager(Player player) {
        this.player = player;
        this.currentStage = 0;
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
        if (currentBattle == null || !currentBattle.isBattleOver()) {
            throw new IllegalStateException("Il combattimento non è ancora finito o iniziato.");
        }

        if (currentBattle.getWinner() == currentBattle.getPlayerMonster()) {
            // Vittoria: Ricompensa in oggetti
            player.addItem("Pozione Salute", 1);
            
            // Vittoria: Assegnazione Punti Esperienza (scala in base allo stage)
            int xpReward = 50 + (currentStage * 25); 
            player.getParty().getActiveMonster().addExperience(xpReward);
            
            currentBattle = null; 
        } else {
            currentBattle = null;
        }
    }
}