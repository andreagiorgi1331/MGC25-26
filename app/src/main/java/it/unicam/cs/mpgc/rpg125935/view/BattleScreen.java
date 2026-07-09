package it.unicam.cs.mpgc.rpg125935.view;

import it.unicam.cs.mpgc.rpg125935.App;
import it.unicam.cs.mpgc.rpg125935.model.battle.BattleManager;
import it.unicam.cs.mpgc.rpg125935.model.core.RunManager;
import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterType;
import it.unicam.cs.mpgc.rpg125935.model.moves.DamageEffect;
import it.unicam.cs.mpgc.rpg125935.model.moves.Move;
import it.unicam.cs.mpgc.rpg125935.model.player.Player;
import it.unicam.cs.mpgc.rpg125935.view.controller.BattleController;
import it.unicam.cs.mpgc.rpg125935.view.handler.BattleViewHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.List;

/**
 * Schermata di combattimento — implementa sia {@link Screen} (per la navigazione)
 * che {@link BattleViewHandler} (Presenter, per gestire le azioni dell'utente).
 *
 * Questa classe fa da ponte tra il Controller FXML (pura presentazione)
 * e il Model (logica di business). Non contiene codice di layout/styling
 * (tutto in FXML + CSS), e non contiene logica di combattimento
 * (tutto nel BattleManager).
 */
public class BattleScreen implements Screen, BattleViewHandler {

    private final Scene scene;
    private final App app;
    private final RunManager runManager;
    private final Player player;
    private final BattleManager battleManager;
    private final BattleController controller;

    // Mosse disponibili per il turno corrente
    private final List<Move> availableMoves;

    public BattleScreen(App app, RunManager runManager, Player player) {
        this.app = app;
        this.runManager = runManager;
        this.player = player;
        this.battleManager = runManager.getCurrentBattle();

        // --- 1. Caricamento FXML ---
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/battle_screen.fxml")
            );
            Parent root = loader.load();
            this.controller = loader.getController();

            // --- 2. Connessione Presenter ---
            controller.setHandler(this);

            // --- 3. Creazione Scena con CSS ---
            this.scene = new Scene(root, App.WINDOW_WIDTH, App.WINDOW_HEIGHT);
            scene.getStylesheets().add(
                getClass().getResource("/css/battle.css").toExternalForm()
            );

        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare battle_screen.fxml", e);
        }

        // --- 4. Preparazione Mosse ---
        Monster playerMonster = player.getParty().getActiveMonster();
        this.availableMoves = playerMonster.getMoves();

        // --- 5. Popolazione Dati Iniziali dal Model ---
        initializeBattleUI();
    }

    /**
     * Popola la UI con i dati iniziali della battaglia dal Model.
     */
    private void initializeBattleUI() {
        Monster enemy = battleManager.getEnemyMonster();
        Monster playerMon = battleManager.getPlayerMonster();

        // Stage
        controller.setStage(runManager.getCurrentStage());

        // Dati nemico
        controller.setEnemyData(
            enemy.getName(), enemy.getLevel(), enemy.getType(),
            enemy.getCurrentPv(), enemy.getBaseStats().maxPv()
        );

        // Dati giocatore (con EXP)
        int expToNext = playerMon.getLevel() * 100; // Soglia dal Model
        controller.setPlayerData(
            playerMon.getName(), playerMon.getLevel(), playerMon.getType(),
            playerMon.getCurrentPv(), playerMon.getBaseStats().maxPv(),
            playerMon.getExperience(), expToNext
        );

        // Mosse
        controller.setMoves(availableMoves);

        // Messaggio iniziale
        controller.showBattleLog("Un " + enemy.getName() + " selvatico appare!");
    }

    /**
     * Aggiorna tutte le barre e i dati nella UI con lo stato corrente del Model.
     */
    private void refreshUI() {
        Monster enemy = battleManager.getEnemyMonster();
        Monster playerMon = battleManager.getPlayerMonster();

        controller.updateEnemyHp(enemy.getCurrentPv(), enemy.getBaseStats().maxPv());
        controller.updatePlayerHp(playerMon.getCurrentPv(), playerMon.getBaseStats().maxPv());

        int expToNext = playerMon.getLevel() * 100;
        controller.updatePlayerExp(playerMon.getExperience(), expToNext);
    }

    // ══════════════════════════════════════════════
    //  IMPLEMENTAZIONE BattleViewHandler (Presenter)
    // ══════════════════════════════════════════════

    @Override
    public void onMoveSelected(int moveIndex) {
        if (moveIndex < 0 || moveIndex >= availableMoves.size()) return;

        Move playerMove = availableMoves.get(moveIndex);

        // Delega al Model per calcolare il turno
        Move enemyMove = battleManager.playTurn(playerMove);

        // Aggiorna la UI con i risultati
        refreshUI();

        // Mostra il log del turno
        String logMessage = "Hai usato " + playerMove.getName() + "!\n"
                          + "Il nemico risponde con " + enemyMove.getName() + "!";

        // Controlla fine battaglia
        if (battleManager.isBattleOver()) {
            Monster winner = battleManager.getWinner();
            boolean playerWon = (winner == battleManager.getPlayerMonster());

            if (playerWon) {
                controller.showEndBattle(true,
                    winner.getName() + " ha sconfitto il nemico!\n" + logMessage);
            } else {
                // Il mostro del giocatore è andato K.O.: viene rimosso dal party (permadeath)
                Monster faintedMonster = battleManager.getPlayerMonster();
                player.getParty().removeMonster(faintedMonster);
                
                if (player.getParty().getMonsters().isEmpty()) {
                    controller.showEndBattle(false,
                        faintedMonster.getName() + " è esausto! Non hai altri mostri disponibili. Game Over.\n" + logMessage);
                } else {
                    Monster nextMonster = player.getParty().getActiveMonster();
                    controller.showEndBattle(false,
                        faintedMonster.getName() + " è esausto ed è stato rimosso dal party!\n" +
                        "Puoi continuare l'esplorazione con " + nextMonster.getName() + ".\n" + logMessage);
                }
            }
        } else {
            // Battaglia continua: mostra log → poi menu azioni
            controller.showBattleLog(logMessage);
        }
    }

    @Override
    public void onBagSelected() {
        // TODO: Implementare schermata inventario in una fase successiva
        controller.showBattleLog("Lo zaino è vuoto... per ora.");
    }

    @Override
    public void onPartySelected() {
        // TODO: Implementare schermata party in una fase successiva
        controller.showBattleLog("Non puoi cambiare mostro... per ora.");
    }

    @Override
    public void onFleeSelected() {
        // Fuga: torna all'Hub senza ricompense
        app.switchScreen(new HubScreen(app, runManager, player));
    }

    @Override
    public void onEndBattleContinue() {
        // Risolvi l'incontro (XP, ricompense, etc.)
        runManager.resolveEncounter();
        
        if (player.getParty().getMonsters().isEmpty()) {
            // Game Over definitivo: torna al menu principale
            app.switchScreen(new MenuScreen(app));
        } else {
            // Torna all'Hub
            app.switchScreen(new HubScreen(app, runManager, player));
        }
    }

    // ══════════════════════════════════════════════
    //  IMPLEMENTAZIONE Screen
    // ══════════════════════════════════════════════

    @Override
    public Scene getScene() {
        return this.scene;
    }
}