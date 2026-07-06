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
import javafx.scene.layout.VBox;

public class MenuScreen implements Screen {

    private final Scene scene;
    private final App app; // Il riferimento al "Regista" per poter cambiare schermata

    public MenuScreen(App app) {
        this.app = app;

        // 1. Creiamo i componenti grafici (Nodi)
        Label titleLabel = new Label("MonsterRogue");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        Button newGameBtn = new Button("Nuova Partita");
        Button loadGameBtn = new Button("Carica Partita");

        // Stile base per i bottoni (più avanti potremo creare una classe Custom come RetroButton)
        String buttonStyle = "-fx-font-size: 16px; -fx-padding: 10 20; -fx-cursor: hand;";
        newGameBtn.setStyle(buttonStyle);
        loadGameBtn.setStyle(buttonStyle);

        // 2. Comportamento dei bottoni (Eventi)
        newGameBtn.setOnAction(e -> {
            System.out.println("Hai cliccato Nuova Partita!");
            // Qui in futuro diremo: app.switchScreen(new HubScreen(...));
        });

        newGameBtn.setOnAction(e -> {
            System.out.println("Creazione Nuova Partita...");
            
            // 1. Inizializziamo il Model (per ora fissiamo il nome e lo starter di Fuoco)
            GameSession session = new GameSession();
            Monster starter = MonsterFactory.createFireStarter();
            session.startNewGame("Eroe", starter);
            
            RunManager runManager = new RunManager(session.getPlayer());
            
            // 2. Passiamo alla schermata Hub passando i dati
            app.switchScreen(new HubScreen(app, runManager, session.getPlayer()));
        });

        // 3. Layout (Come disponiamo gli elementi)
        VBox layout = new VBox(20); // VBox impila gli elementi verticalmente con 20px di spazio
        layout.setAlignment(Pos.CENTER); // Centriamo tutto al centro della finestra
        layout.getChildren().addAll(titleLabel, newGameBtn, loadGameBtn);

        // 4. Creazione della Scena finale
        this.scene = new Scene(layout, App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}