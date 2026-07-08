package it.unicam.cs.mpgc.rpg125935.view;

import java.io.IOException;

import it.unicam.cs.mpgc.rpg125935.App;
import it.unicam.cs.mpgc.rpg125935.model.core.RunManager;
import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.items.Item;
import it.unicam.cs.mpgc.rpg125935.model.items.ItemFactory;
import it.unicam.cs.mpgc.rpg125935.model.player.Player;
import it.unicam.cs.mpgc.rpg125935.save.SaveManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HubScreen implements Screen {

    private final Scene scene;
    private final App app;
    private final RunManager runManager;
    private final Player player;

    // Elementi grafici che devono essere aggiornati dinamicamente
    private final Label stageLabel;
    private final Label monsterInfoLabel;
    private final Button healBtn;

    public HubScreen(App app, RunManager runManager, Player player) {
        this.app = app;
        this.runManager = runManager;
        this.player = player;

        // 1. Creazione Componenti Visivi
        Label titleLabel = new Label("Accampamento");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        this.stageLabel = new Label();
        this.monsterInfoLabel = new Label();
        this.stageLabel.setStyle("-fx-font-size: 18px;");
        this.monsterInfoLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: darkblue;");

        this.healBtn = new RetroButton("Usa Pozione Salute");
        RetroButton nextStageBtn = new RetroButton("Addentrati nell'Oscurità"); // Nome più tematico!
        RetroButton saveBtn = new RetroButton("Riposa e Salva");


        // Inizializza i testi con i dati reali del Model
        updateUI();

        // 2. Comportamento dei Bottoni (Controller Logic)
        this.healBtn.setOnAction(e -> handleHeal());
        
        saveBtn.setOnAction(e -> handleSave());

        nextStageBtn.setOnAction(e -> {
            try {
                // 1. Il Model prepara i dati e il nemico
                runManager.startNextEncounter();
                
                // 2. Il Controller (App) carica la schermata dell'arena
                app.switchScreen(new BattleScreen(app, runManager, player));
                
            } catch (IllegalStateException ex) {
                System.out.println("Errore: " + ex.getMessage());
            }
        });

        // 3. Impaginazione
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #0d0d0d;"); // Grigio scurissimo

        // Colora i testi di bianco/grigio chiaro
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #c0c0c0;");
        this.stageLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #8B0000;"); // Stage in rosso
        this.monsterInfoLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #a6a6a6;"); // Mostro in grigio
        
        layout.getChildren().addAll(titleLabel, this.stageLabel, this.monsterInfoLabel, this.healBtn, nextStageBtn, saveBtn);

        this.scene = new Scene(layout, App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
    }

    /**
     * Aggiorna i testi dell'interfaccia basandosi sullo stato attuale del Model.
     */
    private void updateUI() {
        stageLabel.setText("Stage Corrente: " + runManager.getCurrentStage());
        
        Monster activeMonster = player.getParty().getActiveMonster();
        if (activeMonster != null) {
            monsterInfoLabel.setText(String.format("Mostro Attivo: %s | Livello: %d | PV: %d/%d",
                    activeMonster.getName(),
                    activeMonster.getLevel(),
                    activeMonster.getCurrentPv(),
                    activeMonster.getBaseStats().maxPv()));
        } else {
            monsterInfoLabel.setText("Nessun mostro nel party!");
        }

        int potions = player.getInventory().getOrDefault("Pozione Salute", 0);
        healBtn.setText("Usa Pozione Salute (Disponibili: " + potions + ")");
        healBtn.setDisable(potions <= 0 || activeMonster == null || activeMonster.isFainted());
    }

    /**
     * Gestisce la logica di cura.
     */
    private void handleHeal() {
        Monster activeMonster = player.getParty().getActiveMonster();
        if (activeMonster != null && player.getInventory().getOrDefault("Pozione Salute", 0) > 0) {
            Item potion = ItemFactory.createHealthPotion();
            potion.useOn(activeMonster);
            player.useItem("Pozione Salute");
            updateUI(); // Aggiorniamo la grafica dopo aver modificato i dati
        }
    }

    /**
     * Gestisce il salvataggio chiamando il SaveManager.
     */
    private void handleSave() {
        SaveManager saveManager = new SaveManager();
        try {
            saveManager.saveGame(player, "savegame.json");
            System.out.println("Partita salvata con successo!");
        } catch (IOException ex) {
            System.out.println("Errore durante il salvataggio: " + ex.getMessage());
        }
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}