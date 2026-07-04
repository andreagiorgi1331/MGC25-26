package it.unicam.cs.mpgc.rpg125935.model.items;

import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;

/**
 * Interfaccia che definisce l'effetto (la strategia) di un oggetto consumabile.
 */
public interface ItemEffect {
    
    /**
     * Applica l'effetto dell'oggetto su un mostro bersaglio.
     * @param target Il mostro su cui usare l'oggetto.
     */
    void apply(Monster target);
}