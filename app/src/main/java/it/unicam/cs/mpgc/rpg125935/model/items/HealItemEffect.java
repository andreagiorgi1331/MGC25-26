package it.unicam.cs.mpgc.rpg125935.model.items;

import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;

/**
 * Effetto che ripristina i Punti Vita (PV) di un mostro.
 */
public class HealItemEffect implements ItemEffect {

    private final int healAmount;

    public HealItemEffect(int healAmount) {
        this.healAmount = healAmount;
    }

    @Override
    public void apply(Monster target) {
        if (target.isFainted()) {
            throw new IllegalStateException("Non puoi curare un mostro esausto con questo oggetto!");
        }
        target.heal(healAmount);
    }
}