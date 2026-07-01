package it.unicam.cs.mpgc.rpg125935.model.creatures;

/*** Rappresenta le statistiche di base immutabili di un mostro.*/

public record MonsterStats(
    int maxPv, 
    int attack, 
    int defense, 
    int magic, 
    int speed
) {
    
}