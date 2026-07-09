package it.unicam.cs.mpgc.rpg125935.model.moves;

import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;
import it.unicam.cs.mpgc.rpg125935.model.creatures.MonsterType;

/**
 * Rappresenta una mossa utilizzabile in combattimento.
 * Contiene il nome della mossa, il suo tipo elementale e delega il suo comportamento a un MoveEffect.
 */
public class Move {

    private final String name;
    private final MonsterType type;
    private final MoveEffect effect;

    public Move(String name, MonsterType type, MoveEffect effect) {
        this.name = name;
        this.type = type;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public MonsterType getType() {
        return type;
    }

    /**
     * Esegue la mossa, attivando il suo effetto.
     */
    public void execute(Monster attacker, Monster defender) {
        effect.apply(attacker, defender);
    }
}
