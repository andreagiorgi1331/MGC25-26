package it.unicam.cs.mpgc.rpg125935.view.handler;

/**
 * Interfaccia Presenter per il disaccoppiamento View ↔ Model nella schermata di combattimento.
 * Il Controller FXML chiama questi metodi in risposta alle azioni dell'utente.
 * L'implementazione concreta (tipicamente BattleScreen) delega al BattleManager.
 */
public interface BattleViewHandler {

    /**
     * L'utente ha selezionato una mossa dalla griglia.
     * @param moveIndex indice della mossa (0-3)
     */
    void onMoveSelected(int moveIndex);

    /**
     * L'utente ha premuto il bottone "Zaino".
     */
    void onBagSelected();

    /**
     * L'utente ha premuto il bottone "Party".
     */
    void onPartySelected();

    /**
     * L'utente ha premuto il bottone "Fuggi".
     */
    void onFleeSelected();

    /**
     * L'utente ha premuto "Continua" al termine della battaglia.
     */
    void onEndBattleContinue();
}
