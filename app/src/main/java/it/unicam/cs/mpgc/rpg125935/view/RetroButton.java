package it.unicam.cs.mpgc.rpg125935.view;

import javafx.scene.Cursor;
import javafx.scene.control.Button;

public class RetroButton extends Button {

    // Stile Normale: Sfondo quasi nero, testo grigio cenere, bordo rosso scuro
    private final String IDLE_STYLE = "-fx-background-color: #1a1a1a; " +
                                      "-fx-text-fill: #b3b3b3; " +
                                      "-fx-font-size: 16px; " +
                                      "-fx-font-family: 'Courier New', monospace; " + // Font in stile macchina da scrivere/goth
                                      "-fx-font-weight: bold; " +
                                      "-fx-padding: 12 25; " +
                                      "-fx-border-color: #5c0000; " + // Rosso scuro
                                      "-fx-border-width: 2px;";
                                      
    // Stile Hover (Mouse sopra): Sfondo rossastro, testo bianco, bordo rosso vivo
    private final String HOVER_STYLE = "-fx-background-color: #330000; " +
                                       "-fx-text-fill: #ffffff; " + 
                                       "-fx-font-size: 16px; " +
                                       "-fx-font-family: 'Courier New', monospace; " +
                                       "-fx-font-weight: bold; " +
                                       "-fx-padding: 12 25; " +
                                       "-fx-border-color: #ff1a1a; " + // Rosso vivo
                                       "-fx-border-width: 2px;";

    public RetroButton(String text) {
        super(text);
        
        setStyle(IDLE_STYLE);
        setCursor(Cursor.HAND);

        setOnMouseEntered(e -> setStyle(HOVER_STYLE));
        setOnMouseExited(e -> setStyle(IDLE_STYLE));
        
        setOnMousePressed(e -> setTranslateY(2));
        setOnMouseReleased(e -> setTranslateY(0));
    }
}