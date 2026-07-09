package it.unicam.cs.mpgc.rpg125935.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.unicam.cs.mpgc.rpg125935.App;
import it.unicam.cs.mpgc.rpg125935.model.core.RunManager;
import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterFactory;
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

    // Elementi grafici
    private final Label stageLabel;
    private final Label instructionLabel;
    private final VBox partyButtonsContainer;
    private final Button healBtn;
    private final Button recruitBtn;

    // Stato di selezione per l'uso degli oggetti
    private boolean isHealingMode = false;

    public HubScreen(App app, RunManager runManager, Player player) {
        this.app = app;
        this.runManager = runManager;
        this.player = player;

        // 1. Creazione Componenti Visivi
        Label titleLabel = new Label("Accampamento");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #c0c0c0; -fx-font-family: 'Courier New', monospace;");

        this.stageLabel = new Label();
        this.stageLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #8B0000; -fx-font-family: 'Courier New', monospace;");

        this.instructionLabel = new Label("Gestisci la tua squadra o seleziona un'azione:");
        this.instructionLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #888888; -fx-font-family: 'Courier New', monospace;");

        // Contenitore per la lista cliccabile dei mostri
        this.partyButtonsContainer = new VBox(10);
        this.partyButtonsContainer.setAlignment(Pos.CENTER);

        // Bottoni comandi
        this.healBtn = new RetroButton("Usa Pozione Salute");
        this.recruitBtn = new RetroButton("Recluta un Mostro");
        RetroButton nextStageBtn = new RetroButton("Addentrati nell'Oscurità");
        RetroButton saveBtn = new RetroButton("Riposa e Salva");
        RetroButton exitBtn = new RetroButton("Esci al Menu Principale");

        // Associazione eventi
        this.healBtn.setOnAction(e -> toggleHealingMode());
        this.recruitBtn.setOnAction(e -> handleRecruit());
        saveBtn.setOnAction(e -> handleSave());
        exitBtn.setOnAction(e -> app.switchScreen(new MenuScreen(app)));

        nextStageBtn.setOnAction(e -> {
            try {
                if (isHealingMode) {
                    toggleHealingMode(); // Annulla modalità cura prima di iniziare
                }
                runManager.startNextEncounter();
                app.switchScreen(new BattleScreen(app, runManager, player));
            } catch (IllegalStateException ex) {
                instructionLabel.setText(ex.getMessage());
                instructionLabel.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");
            }
        });

        // Popolamento iniziale UI
        updateUI();

        // 3. Layout complessivo
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #0d0d0d;");
        layout.getChildren().addAll(
            titleLabel, 
            this.stageLabel, 
            this.instructionLabel, 
            this.partyButtonsContainer, 
            this.healBtn, 
            this.recruitBtn, 
            nextStageBtn, 
            saveBtn, 
            exitBtn
        );

        this.scene = new Scene(layout, App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
    }

    /**
     * Aggiorna i testi e ricrea la lista cliccabile dei mostri nel party.
     */
    private void updateUI() {
        stageLabel.setText("Stage Corrente: " + runManager.getCurrentStage());
        
        // Configura il testo di stato in base alla modalità di cura attiva
        if (isHealingMode) {
            instructionLabel.setText("SELEZIONA IL MOSTRO DA CURARE (Usa Pozione Salute)");
            instructionLabel.setStyle("-fx-text-fill: #ffa500; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace; -fx-font-weight: bold;");
            healBtn.setText("Annulla Cura");
        } else {
            instructionLabel.setText("Seleziona un mostro per impostarlo come ATTIVO (in prima posizione):");
            instructionLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");
            int potions = player.getInventory().getOrDefault("Pozione Salute", 0);
            healBtn.setText("Usa Pozione Salute (Disponibili: " + potions + ")");
            healBtn.setDisable(potions <= 0);
        }

        // Ricostruisce la lista di bottoni dei mostri
        partyButtonsContainer.getChildren().clear();
        List<Monster> monsters = player.getParty().getMonsters();
        
        for (int i = 0; i < monsters.size(); i++) {
            final int index = i;
            Monster m = monsters.get(i);
            
            String status = i == 0 ? "[ATTIVO] " : "";
            String monsterText = String.format("%s%s (Lv.%d) - HP: %d/%d (Tipo: %s)",
                    status, m.getName(), m.getLevel(), m.getCurrentPv(), m.getBaseStats().maxPv(), m.getType());
            
            RetroButton monsterBtn = new RetroButton(monsterText);
            
            // Colora in modo differenziato il mostro attivo
            if (i == 0) {
                monsterBtn.setStyle(monsterBtn.getStyle() + " -fx-border-color: #2de2e6;");
            }
            
            monsterBtn.setOnAction(e -> handleMonsterSelection(index));
            partyButtonsContainer.getChildren().add(monsterBtn);
        }

        // Configura bottone reclutamento
        int partySize = player.getParty().getMonsters().size();
        recruitBtn.setText("Recluta Nuovo Mostro (Squadra: " + partySize + "/3)");
        recruitBtn.setDisable(partySize >= 3);
    }

    /**
     * Alterna la modalità di utilizzo delle pozioni.
     */
    private void toggleHealingMode() {
        isHealingMode = !isHealingMode;
        updateUI();
    }

    /**
     * Gestisce il click su un mostro della lista.
     * - Se in modalità cura: applica la pozione e scala l'inventario.
     * - Altrimenti: scambia il mostro selezionato in prima posizione (mostro attivo).
     */
    private void handleMonsterSelection(int index) {
        List<Monster> monsters = player.getParty().getMonsters();
        Monster selected = monsters.get(index);

        if (isHealingMode) {
            int potions = player.getInventory().getOrDefault("Pozione Salute", 0);
            if (potions > 0) {
                if (selected.getCurrentPv() >= selected.getBaseStats().maxPv()) {
                    instructionLabel.setText(selected.getName() + " ha già la vita al massimo!");
                    instructionLabel.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 12px; -fx-font-family: 'Courier New', monospace;");
                    return;
                }
                Item potion = ItemFactory.createHealthPotion();
                potion.useOn(selected);
                player.useItem("Pozione Salute");
                System.out.println("Curato " + selected.getName() + " di 50 PV!");
            }
            isHealingMode = false; // Esci dalla modalità di cura
        } else {
            // Se non siamo in modalità cura, cliccando impostiamo il mostro come attivo (scambio all'indice 0)
            if (index != 0) {
                player.getParty().swapMonsters(0, index);
                System.out.println("Impostato " + selected.getName() + " come mostro attivo!");
            }
        }
        updateUI();
    }

    /**
     * Gestisce il reclutamento di un nuovo mostro.
     */
    private void handleRecruit() {
        if (player.getParty().getMonsters().size() < 3) {
            Monster recruited = MonsterFactory.generateRandomEnemy(runManager.getCurrentStage());
            player.getParty().addMonster(recruited);
            System.out.println("Reclutato nuovo mostro: " + recruited.getName());
            updateUI();
        }
    }

    /**
     * Gestisce il salvataggio chiamando il SaveManager.
     */
    private void handleSave() {
        SaveManager saveManager = new SaveManager();
        try {
            player.getParty().healAll();
            int slot = runManager.getSlotIndex();
            saveManager.saveGame(player, runManager.getCurrentStage(), slot);
            System.out.println("Partita salvata nello slot " + slot + " con successo!");
            app.switchScreen(new HubScreen(app, runManager, player));
        } catch (IOException ex) {
            System.out.println("Errore durante il salvataggio: " + ex.getMessage());
        }
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}