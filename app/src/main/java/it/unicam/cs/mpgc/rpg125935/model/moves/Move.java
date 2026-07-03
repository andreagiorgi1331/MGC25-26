package it.unicam.cs.mpgc.rpg125935.model.moves;

import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;

/**
 * Rappresenta una mossa utilizzabile in combattimento.
 * Contiene il nome della mossa e delega il suo comportamento a un MoveEffect.
 */
public class Move {

    private final String name;
    private final MoveEffect effect;

    public Move(String name, MoveEffect effect) {
        this.name = name;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    /**
     * Esegue la mossa, attivando il suo effetto.
     */
    public void execute(Monster attacker, Monster defender) {
        effect.apply(attacker, defender);
    }
}
