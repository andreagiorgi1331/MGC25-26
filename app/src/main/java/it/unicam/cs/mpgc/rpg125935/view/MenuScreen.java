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

        // Non sovrascriviamo lo stile dei RetroButton: 
        // il loro stile IDLE/HOVER è gestito internamente.

        // Label di notifica/errore per il menù (es. se manca il file di salvataggio)
        Label infoLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #8B0000;");
        infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red; -fx-font-weight: bold;");

        // --- 2. Eventi dei Bottoni ---
        
        // Nuova Partita: rimanda alla schermata di scelta dello slot
        newGameBtn.setOnAction(e -> {
            System.out.println("Transizione alla selezione slot per nuova partita...");
            app.switchScreen(new SaveSlotScreen(app, SaveSlotScreen.Mode.NEW_GAME));
        });

        // Carica Partita: rimanda alla schermata di scelta dello slot per il caricamento
        loadGameBtn.setOnAction(e -> {
            System.out.println("Transizione alla selezione slot per caricare una partita...");
            app.switchScreen(new SaveSlotScreen(app, SaveSlotScreen.Mode.LOAD_GAME));
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