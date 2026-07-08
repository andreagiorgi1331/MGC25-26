package it.unicam.cs.mpgc.rpg125935.view;

import java.io.File;
import java.io.IOException;

import it.unicam.cs.mpgc.rpg125935.App;
import it.unicam.cs.mpgc.rpg125935.model.core.RunManager; // Importiamo il gestore dei salvataggi
import it.unicam.cs.mpgc.rpg125935.model.player.Player;
import it.unicam.cs.mpgc.rpg125935.save.SaveManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuScreen implements Screen {

    private final Scene scene;
    private final App app;

    public MenuScreen(App app) {
        this.app = app;

        // --- 1. Componenti Grafici ---
        Label titleLabel = new Label("MonsterRogue");
        titleLabel.setStyle("-fx-font-size: 52px; -fx-font-weight: bold; -fx-text-fill: #8B0000;");

        // Usiamo la nostra nuova classe!
        RetroButton newGameBtn = new RetroButton("Nuova Partita");
        RetroButton loadGameBtn = new RetroButton("Carica Partita");

        String buttonStyle = "-fx-font-size: 16px; -fx-padding: 10 20; -fx-cursor: hand;";
        newGameBtn.setStyle(buttonStyle);
        loadGameBtn.setStyle(buttonStyle);

        // Label di notifica/errore per il menù (es. se manca il file di salvataggio)
        Label infoLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #8B0000;");
        infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red; -fx-font-weight: bold;");

        // --- 2. Eventi dei Bottoni ---
        
        // Nuova Partita: rimanda alla schermata di creazione inserimento nome
        newGameBtn.setOnAction(e -> {
            System.out.println("Transizione alla creazione del personaggio...");
            app.switchScreen(new SetupScreen(app));
        });

        // Carica Partita: Logica di ripristino della sessione dal file JSON
        loadGameBtn.setOnAction(e -> {
            File saveFile = new File("savegame.json");
            
            // Gestione dell'errore (Input/State Validation): se il file non esiste, avvisiamo l'utente
            if (!saveFile.exists()) {
                infoLabel.setText("Nessun salvataggio trovato! Avvia una Nuova Partita.");
                System.out.println("Errore: file savegame.json non trovato nella radice del progetto.");
                return;
            }

            System.out.println("Rilevato file di salvataggio. Avvio deserializzazione...");
            SaveManager saveManager = new SaveManager();
            
            try {
                // 1. Carichiamo lo stato del giocatore dal JSON
                Player loadedPlayer = saveManager.loadGame("savegame.json");
                
                // 2. Ricostruiamo il gestore della progressione (RunManager)
                RunManager runManager = new RunManager(loadedPlayer);
                
                System.out.println("Partita caricata con successo! Allenatore: " + loadedPlayer.getName());
                
                // 3. Traghettiamo l'utente direttamente all'accampamento (Hub)
                app.switchScreen(new HubScreen(app, runManager, loadedPlayer));
                
            } catch (IOException ex) {
                infoLabel.setText("Errore critico durante il caricamento del file!");
                System.err.println("Impossibile caricare la partita: " + ex.getMessage());
            }
        });

        // --- 3. Layout e Scena ---
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        // Sfondo nero puro per il menu
        layout.setStyle("-fx-background-color: #050505;");
        
        // Assicurati che il titolo sia rosso sangue
        titleLabel.setStyle("-fx-font-size: 52px; -fx-font-family: 'Courier New', monospace; -fx-font-weight: bold; -fx-text-fill: #8B0000;");
        
        layout.getChildren().addAll(titleLabel, newGameBtn, loadGameBtn, infoLabel);

        this.scene = new Scene(layout, App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}