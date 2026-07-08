package it.unicam.cs.mpgc.rpg125935.view;

import it.unicam.cs.mpgc.rpg125935.App;
import it.unicam.cs.mpgc.rpg125935.model.battle.BattleManager;
import it.unicam.cs.mpgc.rpg125935.model.core.RunManager;
import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterType;
import it.unicam.cs.mpgc.rpg125935.model.moves.DamageEffect;
import it.unicam.cs.mpgc.rpg125935.model.moves.Move;
import it.unicam.cs.mpgc.rpg125935.model.player.Player;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BattleScreen implements Screen {

    private final Scene scene;
    private final App app;
    private final RunManager runManager;
    private final Player player;
    private final BattleManager battleManager;

    private final Label enemyInfo;
    private final Label playerInfo;
    private final Label battleLog;
    private final HBox actionButtonsBox;
    private final Button returnToHubBtn;



    public BattleScreen(App app, RunManager runManager, Player player) {
        this.app = app;
        this.runManager = runManager;
        this.player = player;
        this.battleManager = runManager.getCurrentBattle();

        // --- 1. Elementi Grafici ---
        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #000000;"); // Nero assoluto


        Label title = new Label("Battaglia - Stage " + runManager.getCurrentStage());
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #5c0000;"); // Rosso scuro


        // Info Nemico (Rosso)
        enemyInfo = new Label();
        enemyInfo.setStyle("-fx-font-size: 18px; -fx-text-fill: darkred;");

        // Log della Battaglia (Cosa succede)
        battleLog = new Label("Un " + battleManager.getEnemyMonster().getName() + " selvatico appare!");
        battleLog.setStyle("-fx-font-size: 16px; -fx-font-style: italic; -fx-text-fill: #cccccc;"); // Grigio cenere

        // Info Giocatore (Blu)
        playerInfo = new Label();
        playerInfo.setStyle("-fx-font-size: 18px; -fx-text-fill: darkblue;");

        // --- 2. Creazione delle Mosse ---
        // (In futuro queste mosse le prenderemo direttamente dal Monster)
        Move lightAttack = new Move("Attacco Rapido", new DamageEffect(30, MonsterType.NORMAL));
        Move heavyAttack = new Move("Colpo Elementale", new DamageEffect(60, player.getParty().getActiveMonster().getType()));

        RetroButton btnLight = new RetroButton(lightAttack.getName() + " (Pot: 30)");
        RetroButton btnHeavy = new RetroButton(heavyAttack.getName() + " (Pot: 60)");

        btnLight.setOnAction(e -> executeTurn(lightAttack));
        btnHeavy.setOnAction(e -> executeTurn(heavyAttack));

        actionButtonsBox = new HBox(20, btnLight, btnHeavy);
        actionButtonsBox.setAlignment(Pos.CENTER);

        // Bottone per tornare all'Hub a fine battaglia (nascosto all'inizio)
        returnToHubBtn = new RetroButton("Ritorna all'Accampamento");
        returnToHubBtn.setVisible(false);
        returnToHubBtn.setOnAction(e -> {
            runManager.resolveEncounter(); // Prende le ricompense / XP
            app.switchScreen(new HubScreen(app, runManager, player)); // Torna all'Hub
        });

        // --- 3. Assembliamo la scena ---
        updateUI(); // Imposta i PV iniziali

        layout.getChildren().addAll(title, enemyInfo, battleLog, playerInfo, actionButtonsBox, returnToHubBtn);
        this.scene = new Scene(layout, App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
    }

    /**
     * Esegue il turno passando la mossa al BattleManager.
     */
    private void executeTurn(Move playerMove) {
        // Facciamo calcolare i danni e le velocità al Model!
        Move enemyMove = battleManager.playTurn(playerMove);
        
        battleLog.setText("Hai usato " + playerMove.getName() + "!\nIl nemico risponde con " + enemyMove.getName() + "!");
        
        updateUI();

        // Controllo Fine Battaglia
        if (battleManager.isBattleOver()) {
            Monster winner = battleManager.getWinner();
            if (winner == battleManager.getPlayerMonster()) {
                battleLog.setText("Hai vinto! " + winner.getName() + " ha sconfitto il nemico.");
            } else {
                battleLog.setText("Sei stato sconfitto... Game Over.");
            }
            
            // Nascondiamo gli attacchi e mostriamo il tasto per tornare all'hub
            actionButtonsBox.setVisible(false);
            returnToHubBtn.setVisible(true);
        }
    }

    /**
     * Aggiorna i testi dei Punti Vita.
     */
    private void updateUI() {
        Monster enemy = battleManager.getEnemyMonster();
        Monster me = battleManager.getPlayerMonster();

        enemyInfo.setText(String.format("NEMICO: %s | PV: %d/%d | Tipo: %s", 
                enemy.getName(), enemy.getCurrentPv(), enemy.getBaseStats().maxPv(), enemy.getType()));

        playerInfo.setText(String.format("TUO MOSTRO: %s | PV: %d/%d | Tipo: %s", 
                me.getName(), me.getCurrentPv(), me.getBaseStats().maxPv(), me.getType()));
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}