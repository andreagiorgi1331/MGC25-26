package it.unicam.cs.mpgc.rpg125935.model.moves;

import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;

/**
 * Interfaccia che definisce l'effetto (la strategia) di una mossa.
 */
public interface MoveEffect {
    
    /**
     * Applica l'effetto della mossa.
     * * @param attacker Il mostro che esegue l'attacco.
     * @param defender Il mostro che subisce l'attacco.
     */
    void apply(Monster attacker, Monster defender);
}