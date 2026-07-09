package it.unicam.cs.mpgc.rpg125935.view;

import java.io.File;
import it.unicam.cs.mpgc.rpg125935.App;
import it.unicam.cs.mpgc.rpg125935.model.core.RunManager;
import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.player.Player;
import it.unicam.cs.mpgc.rpg125935.save.GameSaveState;
import it.unicam.cs.mpgc.rpg125935.save.SaveManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Schermata per la selezione del caricamento o della creazione di una partita
 * attraverso 3 slot di salvataggio distinti.
 */
public class SaveSlotScreen implements Screen {

    public enum Mode {
        NEW_GAME,
        LOAD_GAME
    }

    private final Scene scene;
    private final App app;
    private final Mode mode;
    private final SaveManager saveManager;

    // Tracker per la conferma di sovrascrittura di uno slot
    private int confirmSlotIndex = -1;

    public SaveSlotScreen(App app, Mode mode) {
        this.app = app;
        this.mode = mode;
        this.saveManager = new SaveManager();

        // 1. Componenti Grafici
        String titleText = mode == Mode.NEW_GAME ? "Seleziona uno Slot per la Nuova Partita" : "Carica una Partita Salvata";
        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-family: 'Courier New', monospace; -fx-font-weight: bold; -fx-text-fill: #8B0000;");

        Label infoLabel = new Label();
        infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #ffa500; -fx-font-weight: bold; -fx-font-family: 'Courier New', monospace;");

        // Creazione dei 3 bottoni degli slot
        RetroButton slot1Btn = createSlotButton(1, infoLabel);
        RetroButton slot2Btn = createSlotButton(2, infoLabel);
        RetroButton slot3Btn = createSlotButton(3, infoLabel);

        // Pulsante Indietro
        RetroButton backBtn = new RetroButton("Indietro");
        backBtn.setOnAction(e -> app.switchScreen(new MenuScreen(app)));

        // 2. Layout
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #050505;");
        layout.getChildren().addAll(titleLabel, slot1Btn, slot2Btn, slot3Btn, infoLabel, backBtn);

        this.scene = new Scene(layout, App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
    }

    /**
     * Crea un RetroButton configurato per uno specifico slot di salvataggio.
     */
    private RetroButton createSlotButton(int slotIndex, Label infoLabel) {
        GameSaveState saveState = saveManager.getSaveState(slotIndex);
        String buttonText;

        if (saveState == null) {
            buttonText = "Slot " + slotIndex + " - [Vuoto]";
        } else {
            Player player = saveState.getPlayer();
            int stage = saveState.getCurrentStage();
            Monster activeMonster = player.getParty().getActiveMonster();
            String monsterInfo = activeMonster != null ? activeMonster.getName() + " (Lv." + activeMonster.getLevel() + ")" : "Nessuno";
            
            buttonText = String.format("Slot %d - %s | Stage %d | %s", 
                    slotIndex, player.getName(), stage, monsterInfo);
        }

        RetroButton btn = new RetroButton(buttonText);
        
        btn.setOnAction(e -> {
            if (mode == Mode.LOAD_GAME) {
                // Modalità CARICAMENTO
                if (saveState == null) {
                    infoLabel.setText("Impossibile caricare: lo slot " + slotIndex + " è vuoto!");
                } else {
                    System.out.println("Caricamento slot " + slotIndex + " in corso...");
                    Player loadedPlayer = saveState.getPlayer();
                    
                    // Ricostruiamo il RunManager ripristinando lo stage salvato
                    RunManager runManager = new RunManager(loadedPlayer, saveState.getCurrentStage(), slotIndex);
                    app.switchScreen(new HubScreen(app, runManager, loadedPlayer));
                }
            } else {
                // Modalità NUOVA PARTITA
                if (saveState != null && confirmSlotIndex != slotIndex) {
                    // Lo slot è occupato e richiede una conferma per essere sovrascritto
                    confirmSlotIndex = slotIndex;
                    infoLabel.setText("ATTENZIONE: Lo Slot " + slotIndex + " è occupato. Clicca di nuovo per sovrascriverlo!");
                } else {
                    // Slot vuoto o conferma ricevuta: iniziamo la configurazione
                    System.out.println("Nuova partita selezionata per lo slot " + slotIndex);
                    app.switchScreen(new SetupScreen(app, slotIndex));
                }
            }
        });

        return btn;
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}
