package it.unicam.cs.mpgc.rpg125935.model.player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Rappresenta il giocatore, contenendo i suoi dati, la squadra e l'inventario.
 */
public class Player {

    private String name;
    private Party party;
    private Map<String, Integer> inventory; // Associa il nome dell'oggetto alla quantità

    public Player(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome del giocatore non può essere vuoto");
        }
        this.name = name;
        this.party = new Party();
        this.inventory = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Party getParty() {
        return party;
    }

    /**
     * Aggiunge un oggetto all'inventario.
     */
    public void addItem(String itemName, int quantity) {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome dell'oggetto non può essere vuoto");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantità deve essere maggiore di zero");
        }
        inventory.put(itemName, inventory.getOrDefault(itemName, 0) + quantity);
    }

    /**
     * Consuma un oggetto dall'inventario.
     */
    public void useItem(String itemName) {
        if (!inventory.containsKey(itemName) || inventory.get(itemName) <= 0) {
            throw new IllegalStateException("Non possiedi l'oggetto: " + itemName);
        }
        int currentQty = inventory.get(itemName);
        if (currentQty == 1) {
            inventory.remove(itemName);
        } else {
            inventory.put(itemName, currentQty - 1);
        }
    }

    /**
     * @return Una vista non modificabile dell'inventario.
     */
    public Map<String, Integer> getInventory() {
        return Collections.unmodifiableMap(inventory);
    }
}