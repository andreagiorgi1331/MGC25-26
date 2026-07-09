package it.unicam.cs.mpgc.rpg125935;

import it.unicam.cs.mpgc.rpg125935.view.MenuScreen;
import it.unicam.cs.mpgc.rpg125935.view.Screen;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("MonsterRogue");
        this.primaryStage.setResizable(false);

        // Carica il font pixel-art prima di creare qualsiasi scena
        Font.loadFont(getClass().getResourceAsStream("/fonts/PressStart2P-Regular.ttf"), 12);

        // Inizializziamo il gioco partendo dal Menu Principale
        switchScreen(new MenuScreen(this));
        
        this.primaryStage.show();
    }

    /**
     * Metodo vitale: permette a qualsiasi schermata di dire all'App 
     * di passare a una nuova scena.
     * @param screen La nuova schermata da mostrare.
     */
    public void switchScreen(Screen screen) {
        primaryStage.setScene(screen.getScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}