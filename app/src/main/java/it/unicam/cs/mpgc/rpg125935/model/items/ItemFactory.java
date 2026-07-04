package it.unicam.cs.mpgc.rpg125935.model.items;

/**
 * Factory class per la generazione degli oggetti del gioco.
 */
public class ItemFactory {

    public static Item createHealthPotion() {
        return new Item("Pozione Salute", "Ripristina 50 PV di un mostro", new HealItemEffect(50));
    }
    
    public static Item createMaxPotion() {
        // Una pozione che cura 9999 PV per rimettere in sesto chiunque
        return new Item("Pozione Max", "Ripristina completamente i PV", new HealItemEffect(9999));
    }
}