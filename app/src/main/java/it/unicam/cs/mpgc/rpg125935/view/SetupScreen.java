package it.unicam.cs.mpgc.rpg125935.view;

import it.unicam.cs.mpgc.rpg125935.App;
import it.unicam.cs.mpgc.rpg125935.model.core.GameSession;
import it.unicam.cs.mpgc.rpg125935.model.core.RunManager;
import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterFactory;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    public SetupScreen(App app) {
        this.app = app;

        // --- 1. Componenti Grafici ---
        Label titleLabel = new Label("Crea il tuo Personaggio");
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Label nameLabel = new Label("Inserisci il tuo nome da Allenatore:");
        nameLabel.setStyle("-fx-font-size: 14px;");

        // Campo di testo per l'input del nome
        TextField nameField = new TextField();
        nameField.setPromptText("Nome dell'eroe...");
        nameField.setMaxWidth(220);
        nameField.setStyle("-fx-font-size: 14px; -fx-padding: 5;");

        // Label d'errore (nascosta o vuota finché l'input è valido)
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 13px; -fx-font-weight: bold;");

        Label starterLabel = new Label("Scegli il tuo Mostro Iniziale:");
        starterLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Bottoni per la scelta degli starter (Fuoco, Acqua, Erba)
        Button fireBtn = new Button("Ignis (Fuoco)");
        Button waterBtn = new Button("Aqua (Acqua)");
        Button grassBtn = new Button("Herba (Erba)");

        String starterBtnStyle = "-fx-font-size: 14px; -fx-padding: 12 20; -fx-cursor: hand; -fx-font-weight: bold;";
        fireBtn.setStyle(starterBtnStyle + " -fx-base: #ff6666;");
        waterBtn.setStyle(starterBtnStyle + " -fx-base: #66b2ff;");
        grassBtn.setStyle(starterBtnStyle + " -fx-base: #99ff99;");

        // --- 2. Gestione degli Eventi (Controller Logic) ---
        // Al click su uno starter, proviamo ad avviare la partita passando il rispettivo mostro della Factory
        fireBtn.setOnAction(e -> handleStartGame(nameField.getText(), MonsterFactory.createFireStarter(), errorLabel));
        waterBtn.setOnAction(e -> handleStartGame(nameField.getText(), MonsterFactory.createWaterStarter(), errorLabel));
        grassBtn.setOnAction(e -> handleStartGame(nameField.getText(), MonsterFactory.createGrassStarter(), errorLabel));

        // Impaginiamo i bottoni degli starter orizzontalmente
        HBox startersBox = new HBox(20, fireBtn, waterBtn, grassBtn);
        startersBox.setAlignment(Pos.CENTER);

        // Bottone opzionale per tornare indietro se ci si ripensa
        Button backBtn = new Button("Annulla");
        backBtn.setStyle("-fx-font-size: 12px; -fx-padding: 5 10; -fx-cursor: hand;");
        backBtn.setOnAction(e -> app.switchScreen(new MenuScreen(app)));

        // --- 3. Layout di Insieme ---
        VBox layout = new VBox(25);
        layout.setAlignment(Pos.CENTER);
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
        System.out.println("Salvataggio configurazione: Allenatore " + finalName + " con starter " + selectedStarter.getName());

        // 1. Inizializzazione del dominio (Model) con le scelte dell'utente
        GameSession session = new GameSession();
        session.startNewGame(finalName, selectedStarter);
        
        RunManager runManager = new RunManager(session.getPlayer());

        // 2. Cambio scena verso l'Hub di gioco
        app.switchScreen(new HubScreen(app, runManager, session.getPlayer()));
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}