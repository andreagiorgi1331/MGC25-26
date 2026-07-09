package it.unicam.cs.mpgc.rpg125935.view;

import it.unicam.cs.mpgc.rpg125935.App;
import it.unicam.cs.mpgc.rpg125935.model.core.GameSession;
import it.unicam.cs.mpgc.rpg125935.model.core.RunManager;
import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterFactory;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Schermata di configurazione iniziale in cui l'utente inserisce 
 * il proprio nome e sceglie il mostro iniziale (Starter).
 */
public class SetupScreen implements Screen {

    private final Scene scene;
    private final App app;
    private final int slotIndex;

    public SetupScreen(App app, int slotIndex) {
        this.app = app;
        this.slotIndex = slotIndex;

        // --- 1. Componenti Grafici (tema scuro coerente) ---
        Label titleLabel = new Label("Crea il tuo Personaggio");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #8B0000; " +
                            "-fx-font-family: 'Courier New', monospace;");

        Label nameLabel = new Label("Inserisci il tuo nome da Allenatore (Slot " + slotIndex + "):");
        nameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #c0c0c0; " +
                           "-fx-font-family: 'Courier New', monospace;");

        // Campo di testo per l'input del nome, stilizzato per il tema scuro
        TextField nameField = new TextField();
        nameField.setPromptText("Nome dell'eroe...");
        nameField.setMaxWidth(260);
        nameField.setStyle("-fx-font-size: 14px; -fx-padding: 8; " +
                           "-fx-background-color: #1a1a1a; -fx-text-fill: #e0e0e0; " +
                           "-fx-border-color: #5c0000; -fx-border-width: 2px; " +
                           "-fx-prompt-text-fill: #666666; " +
                           "-fx-font-family: 'Courier New', monospace;");

        // Label d'errore (nascosta o vuota finché l'input è valido)
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 13px; -fx-font-weight: bold; " +
                            "-fx-font-family: 'Courier New', monospace;");

        Label starterLabel = new Label("Scegli il tuo Mostro Iniziale:");
        starterLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #a6a6a6; " +
                              "-fx-font-family: 'Courier New', monospace;");

        // Bottoni per la scelta degli starter con RetroButton e colori tematici
        RetroButton fireBtn = new RetroButton("\uD83D\uDD25 Ignis (Fuoco)");
        RetroButton waterBtn = new RetroButton("\uD83D\uDCA7 Aqua (Acqua)");
        RetroButton grassBtn = new RetroButton("\uD83C\uDF3F Herba (Erba)");

        // --- 2. Gestione degli Eventi (Controller Logic) ---
        // Al click su uno starter, proviamo ad avviare la partita passando il rispettivo mostro della Factory
        fireBtn.setOnAction(e -> handleStartGame(nameField.getText(), MonsterFactory.createFireStarter(), errorLabel));
        waterBtn.setOnAction(e -> handleStartGame(nameField.getText(), MonsterFactory.createWaterStarter(), errorLabel));
        grassBtn.setOnAction(e -> handleStartGame(nameField.getText(), MonsterFactory.createGrassStarter(), errorLabel));

        // Impaginiamo i bottoni degli starter orizzontalmente
        HBox startersBox = new HBox(15, fireBtn, waterBtn, grassBtn);
        startersBox.setAlignment(Pos.CENTER);

        // Bottone per tornare indietro alla scelta dello slot
        RetroButton backBtn = new RetroButton("Annulla");
        backBtn.setOnAction(e -> app.switchScreen(new SaveSlotScreen(app, SaveSlotScreen.Mode.NEW_GAME)));

        // --- 3. Layout di Insieme ---
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #050505;"); // Sfondo nero come MenuScreen
        layout.getChildren().addAll(titleLabel, nameLabel, nameField, errorLabel, starterLabel, startersBox, backBtn);

        this.scene = new Scene(layout, App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
    }

    /**
     * Valida i dati inseriti e, se corretti, istanzia il Model della partita 
     * traghettando il giocatore verso la schermata Hub.
     */
    private void handleStartGame(String rawName, Monster selectedStarter, Label errorLabel) {
        if (rawName == null || rawName.trim().isEmpty()) {
            errorLabel.setText("Attenzione: Devi inserire un nome valido!");
            return;
        }

        String finalName = rawName.trim();
        System.out.println("Salvataggio configurazione: Allenatore " + finalName + " con starter " + selectedStarter.getName() + " nello slot " + slotIndex);

        // 1. Inizializzazione del dominio (Model) con le scelte dell'utente
        GameSession session = new GameSession();
        session.startNewGame(finalName, selectedStarter);
        
        RunManager runManager = new RunManager(session.getPlayer(), slotIndex);

        // 2. Cambio scena verso l'Hub di gioco
        app.switchScreen(new HubScreen(app, runManager, session.getPlayer()));
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}