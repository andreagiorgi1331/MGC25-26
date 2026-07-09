package it.unicam.cs.mpgc.rpg125935.model.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unicam.cs.mpgc.rpg125935.model.creatures.Monster;

/**
 * Gestisce la squadra dei mostri del giocatore, imponendo un limite massimo di 4.
 */
public class Party {
    
    private static final int MAX_SIZE = 3;
    private List<Monster> monsters = new ArrayList<>();

    /**
     * Aggiunge un mostro alla squadra se c'è spazio disponibile.
     * @param monster Il mostro da aggiungere.
     */
    public void addMonster(Monster monster) {
        if (monster == null) {
            throw new IllegalArgumentException("Il mostro non può essere null");
        }
        if (monsters.size() >= MAX_SIZE) {
            throw new IllegalStateException("Il party è pieno! Massimo " + MAX_SIZE + " mostri.");
        }
        monsters.add(monster);
    }

    /**
     * @return Una vista non modificabile della lista dei mostri nel party.
     */
    public List<Monster> getMonsters() {
        return Collections.unmodifiableList(monsters);
    }

    /**
     * Rimuove un mostro specifico dal party.
     */
    public void removeMonster(Monster monster) {
        monsters.remove(monster);
    }

    /**
     * Rimuove tutti i mostri esausti (PV <= 0) dal party.
     */
    public void removeFainted() {
        monsters.removeIf(Monster::isFainted);
    }

    /**
     * Come su Pokémon, il mostro attivo è il primo della lista.
     * @return Il mostro attualmente in prima posizione, o null se il party è vuoto.
     */
    public Monster getActiveMonster() {
        if (monsters.isEmpty()) {
            return null;
        }
        return monsters.get(0);
    }

    /**
     * Scambia la posizione di due mostri nel party (es. per cambiare il mostro attivo).
     */
    public void swapMonsters(int index1, int index2) {
        if (index1 < 0 || index1 >= monsters.size() || index2 < 0 || index2 >= monsters.size()) {
            throw new IndexOutOfBoundsException("Indici non validi per lo scambio nel party");
        }
        Collections.swap(monsters, index1, index2);
    }

    /**
     * Cura completamente tutti i mostri del party (funzionalità per l'Hub).
     */
    public void healAll() {
        for (Monster m : monsters) {
            m.heal(m.getBaseStats().maxPv());
        }
    }

    /**
     * Verifica se tutti i mostri del party sono andati K.O. (Game Over).
     */
    public boolean isAllFainted() {
        if (monsters.isEmpty()) return true;
        return monsters.stream().allMatch(Monster::isFainted);
    }
}