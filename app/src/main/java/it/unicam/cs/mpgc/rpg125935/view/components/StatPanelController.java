package it.unicam.cs.mpgc.rpg125935.view.components;

import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller del componente riutilizzabile StatPanel (databox di un mostro).
 * Gestisce l'aggiornamento dinamico di:
 * - Nome e livello
 * - Barra HP con cambio colore automatico (verde → giallo → rosso)
 * - Barra EXP (opzionale, solo per il giocatore)
 * - HP numerico (opzionale, solo per il giocatore)
 *
 * Istanziato automaticamente da JavaFX tramite fx:include del file stat_panel.fxml.
 */
public class StatPanelController {

    @FXML private VBox root;
    @FXML private Label nameLabel;
    @FXML private Label levelLabel;
    @FXML private ProgressBar hpBar;
    @FXML private Label hpNumericLabel;
    @FXML private ProgressBar expBar;
    @FXML private HBox expBarContainer;

    // Soglie per il cambio colore della barra HP
    private static final double HP_THRESHOLD_MEDIUM = 0.50;
    private static final double HP_THRESHOLD_LOW = 0.20;

    // Classi CSS per gli stati della barra HP
    private static final String HP_CLASS_HIGH = "hp-bar-high";
    private static final String HP_CLASS_MEDIUM = "hp-bar-medium";
    private static final String HP_CLASS_LOW = "hp-bar-low";

    /**
     * Imposta i dati identificativi del mostro.
     * @param name nome del mostro
     * @param level livello corrente
     * @param type tipo del mostro (usato per eventuale styling aggiuntivo)
     */
    public void setMonsterData(String name, int level, MonsterType type) {
        nameLabel.setText(name);
        levelLabel.setText("Lv." + level);
    }

    /**
     * Aggiorna la barra HP e cambia colore dinamicamente in base alla percentuale.
     * - > 50%: verde (#48d048)
     * - 20-50%: giallo (#f8d030)
     * - ≤ 20%: rosso (#f85050)
     *
     * @param currentHp punti vita correnti
     * @param maxHp punti vita massimi
     */
    public void updateHp(int currentHp, int maxHp) {
        double percentage = maxHp > 0 ? (double) currentHp / maxHp : 0;
        percentage = Math.max(0, Math.min(1, percentage)); // Clamp [0, 1]

        hpBar.setProgress(percentage);

        // Aggiorna la classe CSS per il colore
        hpBar.getStyleClass().removeAll(HP_CLASS_HIGH, HP_CLASS_MEDIUM, HP_CLASS_LOW);
        if (percentage > HP_THRESHOLD_MEDIUM) {
            hpBar.getStyleClass().add(HP_CLASS_HIGH);
        } else if (percentage > HP_THRESHOLD_LOW) {
            hpBar.getStyleClass().add(HP_CLASS_MEDIUM);
        } else {
            hpBar.getStyleClass().add(HP_CLASS_LOW);
        }

        // Aggiorna l'HP numerico se visibile
        if (hpNumericLabel.isVisible()) {
            hpNumericLabel.setText(currentHp + " / " + maxHp);
        }
    }

    /**
     * Aggiorna la barra dell'esperienza.
     * @param currentExp esperienza corrente nel livello
     * @param expToNext esperienza necessaria per il prossimo livello
     */
    public void updateExp(int currentExp, int expToNext) {
        if (expBar != null && expToNext > 0) {
            double percentage = (double) currentExp / expToNext;
            expBar.setProgress(Math.max(0, Math.min(1, percentage)));
        }
    }

    /**
     * Mostra o nasconde la barra EXP (visibile solo per il giocatore).
     */
    public void setExpBarVisible(boolean visible) {
        if (expBarContainer != null) {
            expBarContainer.setVisible(visible);
            expBarContainer.setManaged(visible);
        }
    }

    /**
     * Mostra o nasconde l'HP numerico (visibile solo per il giocatore).
     */
    public void setNumericHpVisible(boolean visible) {
        hpNumericLabel.setVisible(visible);
        hpNumericLabel.setManaged(visible);
    }

    /**
     * Aggiunge una classe CSS aggiuntiva al pannello root (es. "stat-panel-enemy" o "stat-panel-player").
     */
    public void addStyleClass(String styleClass) {
        if (!root.getStyleClass().contains(styleClass)) {
            root.getStyleClass().add(styleClass);
        }
    }
}
