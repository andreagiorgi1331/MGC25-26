package it.unicam.cs.mpgc.rpg125935.model.items;

import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;

/**
 * Rappresenta un oggetto consumabile nel gioco.
 */
public class Item {

    private final String name;
    private final String description;
    private final ItemEffect effect;

    public Item(String name, String description, ItemEffect effect) {
        this.name = name;
        this.description = description;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Usa l'oggetto su un mostro.
     */
    public void useOn(Monster target) {
        effect.apply(target);
    }
}