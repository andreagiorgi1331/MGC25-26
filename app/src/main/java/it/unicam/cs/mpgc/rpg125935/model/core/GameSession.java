package it.unicam.cs.mpgc.rpg125935.model.core;

import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.player.Player;

/**
 * Gestisce lo stato globale di una singola partita (run).
 * Fa da punto di accesso (Facade) per il Controller/GUI.
 */
public class GameSession {

    private Player player;
    private boolean isGameActive;

    /**
     * Inizializza una nuova partita creando il giocatore, assegnandogli
     * il mostro iniziale e un kit di base nell'inventario.
     * * @param playerName Il nome scelto dal giocatore.
     * @param starter Il mostro iniziale scelto.
     */
    public void startNewGame(String playerName, Monster starter) {
        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto");
        }
        if (starter == null) {
            throw new IllegalArgumentException("Devi scegliere uno starter");
        }

        this.player = new Player(playerName);
        this.player.getParty().addMonster(starter);
        
        // Diamo al giocatore un piccolo aiuto iniziale!
        this.player.addItem("Pozione Salute", 3);
        
        this.isGameActive = true;
    }

    public Player getPlayer() {
        if (!isGameActive) {
            throw new IllegalStateException("Nessuna partita attiva!");
        }
        return player;
    }

    public boolean isGameActive() {
        return isGameActive;
    }
}