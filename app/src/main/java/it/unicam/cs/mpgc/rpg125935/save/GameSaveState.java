package it.unicam.cs.mpgc.rpg125935.save;

import it.unicam.cs.mpgc.rpg125935.model.player.Player;

/**
 * Rappresenta lo stato completo di un salvataggio di gioco.
 * Include i dati del giocatore, lo stage corrente della run e l'indice dello slot di salvataggio.
 */
public class GameSaveState {

    private final Player player;
    private final int currentStage;
    private final int slotIndex;

    public GameSaveState(Player player, int currentStage, int slotIndex) {
        this.player = player;
        this.currentStage = currentStage;
        this.slotIndex = slotIndex;
    }

    public Player getPlayer() {
        return this.player;
    }

    public int getCurrentStage() {
        return this.currentStage;
    }

    public int getSlotIndex() {
        return this.slotIndex;
    }
}
