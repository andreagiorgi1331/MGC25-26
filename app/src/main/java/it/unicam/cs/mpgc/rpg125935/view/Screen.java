package it.unicam.cs.mpgc.rpg125935.view;

import javafx.scene.Scene;

/**
 * Interfaccia base per tutte le schermate del gioco (Menu, Hub, Battaglia).
 */
public interface Screen {
    
    /**
     * @return La scena JavaFX pronta per essere mostrata nello Stage principale.
     */
    Scene getScene();
}