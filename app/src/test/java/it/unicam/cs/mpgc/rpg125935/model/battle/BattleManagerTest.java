package it.unicam.cs.mpgc.rpg125935.model.battle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unicam.cs.mpgc.rpg125935.model.creatures.BasicMonster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterStats;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterType;
import it.unicam.cs.mpgc.rpg125935.model.moves.DamageEffect;
import it.unicam.cs.mpgc.rpg125935.model.moves.Move;

class BattleManagerTest {

    @Test
    void testFasterMonsterAttacksFirstAndCanPreventCounterAttack() {
        // Mostro veloce ma fragile
        MonsterStats fastStats = new MonsterStats(50, 100, 10, 10, 100); // Velocità 100
        BasicMonster fastMonster = new BasicMonster("Ninja", MonsterType.NORMAL, fastStats);

        // Mostro lento ma resistente
        MonsterStats slowStats = new MonsterStats(50, 10, 10, 10, 10); // Velocità 10
        BasicMonster slowMonster = new BasicMonster("Golem", MonsterType.NORMAL, slowStats);

        BattleManager battle = new BattleManager(fastMonster, slowMonster);

        // Creiamo una mossa abbastanza forte da sconfiggere l'avversario in un colpo (One-Shot)
        Move lethalMove = new Move("Colpo Letale", new DamageEffect(200, MonsterType.NORMAL));
        
        // Eseguiamo il turno (entrambi scelgono la mossa letale)
        battle.playTurn(lethalMove, lethalMove);

        // Verifiche
        assertTrue(battle.isBattleOver(), "La battaglia dovrebbe essere finita");
        assertTrue(slowMonster.isFainted(), "Il mostro lento dovrebbe essere andato K.O.");
        
        // Il punto cruciale: il mostro veloce NON deve aver subito danni,
        // perché ha ucciso il mostro lento prima che questo potesse attaccare!
        assertEquals(50, fastMonster.getCurrentPv(), "Il mostro veloce non deve subire il contrattacco se uccide l'avversario");
        assertEquals(fastMonster, battle.getWinner(), "Il mostro veloce dovrebbe essere il vincitore");
    }

    @Test
    void testExceptionThrownWhenPlayingTurnAfterBattleIsOver() {
        MonsterStats stats = new MonsterStats(10, 100, 10, 10, 50);
        BasicMonster player = new BasicMonster("Giocatore", MonsterType.FIRE, stats);
        BasicMonster enemy = new BasicMonster("Nemico", MonsterType.GRASS, stats);

        BattleManager battle = new BattleManager(player, enemy);
        Move attack = new Move("Attacco Base", new DamageEffect(50, MonsterType.NORMAL));

        // Turno 1: Il giocatore sconfigge il nemico
        battle.playTurn(attack, attack);
        assertTrue(battle.isBattleOver());

        // Turno 2: Provare a giocare un altro turno deve lanciare un'eccezione
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            battle.playTurn(attack, attack);
        });

        assertEquals("La battaglia è già terminata.", exception.getMessage());
    }

    @Test
    void testSingleParameterPlayTurnChoosesMoveAutomatically() {
        MonsterStats stats = new MonsterStats(100, 10, 10, 10, 50);
        BasicMonster player = new BasicMonster("Giocatore", MonsterType.FIRE, stats);
        BasicMonster enemy = new BasicMonster("Nemico", MonsterType.GRASS, stats);

        BattleManager battle = new BattleManager(player, enemy);
        Move playerMove = new Move("Attacco Base", new DamageEffect(30, MonsterType.NORMAL));

        // Eseguiamo il turno passando solo la mossa del giocatore
        Move enemyMove = battle.playTurn(playerMove);

        // La mossa del nemico deve essere stata determinata in automatico ed eseguita
        assertEquals("Azione", enemyMove.getName());

        // Poiché il tipo dell'attacco del nemico è basato sul suo tipo (GRASS), e il giocatore è FIRE,
        // verifichiamo che il danno subito dal giocatore consideri l'efficacia (GRASS contro FIRE è 0.5x non efficace).
        // Formula del danno: 30 * (10 / 10) * 0.5 = 15.
        // I PV del giocatore dovrebbero essere 100 - 15 = 85.
        assertEquals(85, player.getCurrentPv());
        assertEquals(70, enemy.getCurrentPv());
    }
}