package it.unicam.cs.mpgc.rpg125935.view.controller;

import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterType;
import it.unicam.cs.mpgc.rpg125935.model.moves.Move;
import it.unicam.cs.mpgc.rpg125935.view.components.StatPanelController;
import it.unicam.cs.mpgc.rpg125935.view.handler.BattleViewHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * Controller FXML per la schermata di combattimento.
 * Si occupa ESCLUSIVAMENTE della logica di presentazione:
 * - Aggiornare gli elementi grafici (barre HP, log, sprite)
 * - Catturare gli eventi utente (click dei bottoni)
 * - Delegare ogni azione al {@link BattleViewHandler} (Presenter)
 *
 * NON contiene logica di business: non conosce BattleManager, RunManager, ecc.
 */
public class BattleController {

    // ══════════════════════════════════════════════
    //  CAMPI FXML — Arena
    // ══════════════════════════════════════════════

    @FXML private AnchorPane arenaPane;
    @FXML private Label stageLabel;

    // Stat Panels (inclusi via fx:include, naming convention JavaFX:
    // fx:id="enemyStatPanel" → Node + Controller con suffisso "Controller")
    @FXML private Node enemyStatPanel;
    @FXML private StatPanelController enemyStatPanelController;
    @FXML private Node playerStatPanel;
    @FXML private StatPanelController playerStatPanelController;

    // Sprite placeholders
    @FXML private Rectangle enemySpritePlaceholder;
    @FXML private Label enemySpriteLabel;
    @FXML private Rectangle playerSpritePlaceholder;
    @FXML private Label playerSpriteLabel;

    // ══════════════════════════════════════════════
    //  CAMPI FXML — Command Area (Layer System)
    // ══════════════════════════════════════════════

    @FXML private StackPane commandArea;
    @FXML private VBox messagePane;
    @FXML private Label battleLog;
    @FXML private GridPane actionMenuPane;
    @FXML private VBox moveSelectionPane;
    @FXML private GridPane moveGrid;
    @FXML private VBox endBattlePane;
    @FXML private Label endBattleTitle;
    @FXML private Label endBattleMessage;

    // ══════════════════════════════════════════════
    //  STATO INTERNO
    // ══════════════════════════════════════════════

    /** Handler/Presenter per delegare le azioni utente al Model */
    private BattleViewHandler handler;

    /** Mosse attualmente disponibili (per mappare indice → mossa) */
    private List<Move> currentMoves;

    // ══════════════════════════════════════════════
    //  INIZIALIZZAZIONE
    // ══════════════════════════════════════════════

    @FXML
    private void initialize() {
        // Configurazione iniziale dei stat panels
        enemyStatPanelController.addStyleClass("stat-panel-enemy");
        enemyStatPanelController.setExpBarVisible(false);
        enemyStatPanelController.setNumericHpVisible(false);

        playerStatPanelController.addStyleClass("stat-panel-player");
        playerStatPanelController.setExpBarVisible(true);
        playerStatPanelController.setNumericHpVisible(true);

        // Stato iniziale: mostra il messaggio di battaglia
        showLayer(messagePane);
    }

    /**
     * Inietta il Presenter/Handler che gestirà le azioni dell'utente.
     * Deve essere chiamato subito dopo il caricamento FXML.
     */
    public void setHandler(BattleViewHandler handler) {
        this.handler = handler;
    }

    // ══════════════════════════════════════════════
    //  METODI PUBBLICI — Aggiornamento dati dal Model
    // ══════════════════════════════════════════════

    /**
     * Imposta il numero dello stage corrente nel titolo.
     */
    public void setStage(int stage) {
        stageLabel.setText("Stage " + stage);
    }

    /**
     * Configura i dati iniziali del nemico.
     */
    public void setEnemyData(String name, int level, MonsterType type, int hp, int maxHp) {
        enemyStatPanelController.setMonsterData(name, level, type);
        enemyStatPanelController.updateHp(hp, maxHp);
        updateSpritePlaceholder(enemySpritePlaceholder, enemySpriteLabel, name, type);
    }

    /**
     * Configura i dati iniziali del mostro del giocatore.
     */
    public void setPlayerData(String name, int level, MonsterType type, 
                               int hp, int maxHp, int exp, int expToNext) {
        playerStatPanelController.setMonsterData(name, level, type);
        playerStatPanelController.updateHp(hp, maxHp);
        playerStatPanelController.updateExp(exp, expToNext);
        updateSpritePlaceholder(playerSpritePlaceholder, playerSpriteLabel, name, type);
    }

    /**
     * Aggiorna solo la barra HP del nemico (dopo un attacco).
     */
    public void updateEnemyHp(int currentHp, int maxHp) {
        enemyStatPanelController.updateHp(currentHp, maxHp);
    }

    /**
     * Aggiorna solo la barra HP del giocatore (dopo un attacco nemico).
     */
    public void updatePlayerHp(int currentHp, int maxHp) {
        playerStatPanelController.updateHp(currentHp, maxHp);
    }

    /**
     * Aggiorna la barra EXP del giocatore.
     */
    public void updatePlayerExp(int currentExp, int expToNext) {
        playerStatPanelController.updateExp(currentExp, expToNext);
    }

    /**
     * Aggiorna il livello visualizzato del giocatore (dopo un level-up).
     */
    public void updatePlayerLevel(int level) {
        playerStatPanelController.setMonsterData(null, level, null);
    }

    /**
     * Popola la griglia delle mosse (2×2) con le mosse disponibili.
     * Slot vuoti vengono disabilitati.
     *
     * @param moves lista delle mosse disponibili (max 4)
     */
    public void setMoves(List<Move> moves) {
        this.currentMoves = moves;
        moveGrid.getChildren().clear();

        for (int i = 0; i < 4; i++) {
            Button moveBtn;
            if (i < moves.size()) {
                Move move = moves.get(i);
                moveBtn = createMoveButton(move, i);
            } else {
                // Slot vuoto
                moveBtn = new Button("---");
                moveBtn.getStyleClass().add("move-button");
                moveBtn.setDisable(true);
            }
            // Posiziona nella griglia 2×2
            GridPane.setColumnIndex(moveBtn, i % 2);
            GridPane.setRowIndex(moveBtn, i / 2);
            GridPane.setHgrow(moveBtn, javafx.scene.layout.Priority.ALWAYS);
            GridPane.setVgrow(moveBtn, javafx.scene.layout.Priority.ALWAYS);
            GridPane.setFillWidth(moveBtn, true);
            GridPane.setFillHeight(moveBtn, true);
            moveBtn.setMaxWidth(Double.MAX_VALUE);
            moveBtn.setMaxHeight(Double.MAX_VALUE);
            moveGrid.getChildren().add(moveBtn);
        }
    }

    // ══════════════════════════════════════════════
    //  METODI PUBBLICI — Gestione dei Layer
    // ══════════════════════════════════════════════

    /**
     * Mostra il log di battaglia con un messaggio, poi dopo un click
     * passa automaticamente al menu azioni.
     */
    public void showBattleLog(String message) {
        battleLog.setText(message);
        showLayer(messagePane);

        // Click sul messaggio per passare al menu azioni
        messagePane.setOnMouseClicked(e -> {
            messagePane.setOnMouseClicked(null); // Reset handler
            showActionMenu();
        });
    }

    /**
     * Mostra il messaggio di log senza interazione (informativo).
     */
    public void showBattleLogNoAction(String message) {
        battleLog.setText(message);
        showLayer(messagePane);
    }

    /**
     * Mostra il menu azioni principale (Attacca, Zaino, Party, Fuggi).
     */
    public void showActionMenu() {
        showLayer(actionMenuPane);
    }

    /**
     * Mostra la griglia di selezione delle mosse.
     */
    public void showMoveSelection() {
        showLayer(moveSelectionPane);
    }

    /**
     * Mostra il pannello di fine battaglia.
     * @param playerWon true se il giocatore ha vinto
     * @param message messaggio descrittivo
     */
    public void showEndBattle(boolean playerWon, String message) {
        endBattleTitle.getStyleClass().removeAll("end-battle-title-win", "end-battle-title-lose");

        if (playerWon) {
            endBattleTitle.setText("Vittoria!");
            endBattleTitle.getStyleClass().add("end-battle-title-win");
        } else {
            endBattleTitle.setText("Sconfitta...");
            endBattleTitle.getStyleClass().add("end-battle-title-lose");
        }

        endBattleMessage.setText(message);
        showLayer(endBattlePane);
    }

    // ══════════════════════════════════════════════
    //  HANDLER BOTTONI — Delegano al Presenter
    // ══════════════════════════════════════════════

    @FXML
    private void onAttackClicked() {
        showMoveSelection();
    }

    @FXML
    private void onBagClicked() {
        if (handler != null) {
            handler.onBagSelected();
        }
    }

    @FXML
    private void onPartyClicked() {
        if (handler != null) {
            handler.onPartySelected();
        }
    }

    @FXML
    private void onFleeClicked() {
        if (handler != null) {
            handler.onFleeSelected();
        }
    }

    @FXML
    private void onMoveBackClicked() {
        showActionMenu();
    }

    @FXML
    private void onContinueClicked() {
        if (handler != null) {
            handler.onEndBattleContinue();
        }
    }

    // ══════════════════════════════════════════════
    //  METODI PRIVATI — Utility
    // ══════════════════════════════════════════════

    /**
     * Mostra un solo layer nel commandArea, nascondendo tutti gli altri.
     */
    private void showLayer(Node layerToShow) {
        messagePane.setVisible(false);
        actionMenuPane.setVisible(false);
        moveSelectionPane.setVisible(false);
        endBattlePane.setVisible(false);

        layerToShow.setVisible(true);
    }

    /**
     * Crea un bottone per una mossa con styling basato sul tipo.
     */
    private Button createMoveButton(Move move, int index) {
        Button btn = new Button(move.getName());
        btn.getStyleClass().add("move-button");
        btn.getStyleClass().add(getMoveTypeClass(move));

        btn.setOnAction(e -> {
            if (handler != null) {
                handler.onMoveSelected(index);
            }
        });

        return btn;
    }

    /**
     * Restituisce la classe CSS per il tipo della mossa.
     * La classe viene determinata dal nome della mossa come fallback,
     * dato che il Model attuale non espone il tipo direttamente dalla Move.
     */
    private String getMoveTypeClass(Move move) {
        // Heuristica basata sul nome della mossa
        // (in futuro, il Model potrebbe esporre il tipo direttamente)
        String name = move.getName().toLowerCase();
        if (name.contains("elementale")) {
            return "move-type-normal"; // Sarà determinato dal tipo del mostro
        }
        return "move-type-normal";
    }

    /**
     * Aggiorna il placeholder dello sprite con il colore e il nome del tipo.
     */
    private void updateSpritePlaceholder(Rectangle rect, Label label, String name, MonsterType type) {
        label.setText(name);

        // Rimuovi tutte le classi tipo precedenti
        rect.getStyleClass().removeAll(
            "sprite-placeholder-fire", "sprite-placeholder-water",
            "sprite-placeholder-grass", "sprite-placeholder-normal"
        );

        // Aggiungi la classe per il tipo corretto
        String typeClass = switch (type) {
            case FIRE -> "sprite-placeholder-fire";
            case WATER -> "sprite-placeholder-water";
            case GRASS -> "sprite-placeholder-grass";
            case NORMAL -> "sprite-placeholder-normal";
        };
        rect.getStyleClass().add(typeClass);
    }
}
