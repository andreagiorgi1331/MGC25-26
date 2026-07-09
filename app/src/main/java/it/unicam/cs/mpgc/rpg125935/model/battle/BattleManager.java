package it.unicam.cs.mpgc.rpg125935.model.battle;

import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.moves.Move;
import it.unicam.cs.mpgc.rpg125935.model.moves.DamageEffect;

/**
 * Gestisce la logica di un singolo combattimento 1vs1 a turni.
 */
public class BattleManager {

    private final Monster playerMonster;
    private final Monster enemyMonster;

    public BattleManager(Monster playerMonster, Monster enemyMonster) {
        this.playerMonster = playerMonster;
        this.enemyMonster = enemyMonster;
    }

    /**
     * Esegue un singolo turno di combattimento, determinando la mossa del nemico in automatico.
     * @param playerMove La mossa scelta dal giocatore.
     * @return La mossa eseguita dal nemico.
     */
    public Move playTurn(Move playerMove) {
        if (isBattleOver()) {
            throw new IllegalStateException("La battaglia è già terminata.");
        }
        Move enemyMove;
        if (enemyMonster.getMoves() != null && !enemyMonster.getMoves().isEmpty()) {
            int randIndex = (int) (Math.random() * enemyMonster.getMoves().size());
            enemyMove = enemyMonster.getMoves().get(randIndex);
        } else {
            enemyMove = it.unicam.cs.mpgc.rpg125935.model.moves.MoveFactory.createMove("Azione");
        }
        playTurn(playerMove, enemyMove);
        return enemyMove;
    }

    /**
     * Esegue un singolo turno di combattimento.
     * @param playerMove La mossa scelta dal giocatore.
     * @param enemyMove La mossa scelta dal nemico (NPC).
     */
    public void playTurn(Move playerMove, Move enemyMove) {
        if (isBattleOver()) {
            throw new IllegalStateException("La battaglia è già terminata.");
        }

        // Determina chi attacca per primo in base alla velocità
        boolean playerGoesFirst = playerMonster.getBaseStats().speed() >= enemyMonster.getBaseStats().speed();

        if (playerGoesFirst) {
            executeAttacks(playerMonster, playerMove, enemyMonster, enemyMove);
        } else {
            executeAttacks(enemyMonster, enemyMove, playerMonster, playerMove);
        }
    }

    /**
     * Metodo di supporto privato per eseguire gli attacchi in ordine.
     */
    private void executeAttacks(Monster firstAttacker, Move firstMove, Monster secondAttacker, Move secondMove) {
        // Il primo attacca
        firstMove.execute(firstAttacker, secondAttacker);

        // Se il secondo non è andato K.O., contrattacca
        if (!secondAttacker.isFainted()) {
            secondMove.execute(secondAttacker, firstAttacker);
        }
    }

    /**
     * Verifica se uno dei due mostri è andato K.O.
     */
    public boolean isBattleOver() {
        return playerMonster.isFainted() || enemyMonster.isFainted();
    }

    /**
     * Restituisce il vincitore, o null se la battaglia è ancora in corso.
     */
    public Monster getWinner() {
        if (!isBattleOver()) return null;
        return playerMonster.isFainted() ? enemyMonster : playerMonster;
    }

    public Monster getPlayerMonster() {
        return playerMonster;
    }

    public Monster getEnemyMonster() {
        return enemyMonster;
    }
}