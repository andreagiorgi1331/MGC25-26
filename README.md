# MonsterRogue 🎮

Un videogioco RPG Roguelike a turni in 2D sviluppato in Java con **JavaFX**, ispirato all'interfaccia ed al gameplay di **PokéRogue**.

Il progetto è strutturato secondo i principi **SOLID** ed il design pattern **MVC/Presenter (Model-View-Presenter)** per garantire un disaccoppiamento ottimale tra la logica di business e la presentazione visiva.

---

## 🚀 Caratteristiche Principali

1. **Combattimento a Turni PokéRogue-Style**
   - Interfaccia con sprite del giocatore (in basso a sinistra) e nemico (in alto a destra).
   - Barre della vita (HP) dinamiche con cambio colore automatico in base alla percentuale (Verde, Giallo, Rosso).
   - Barra dell'esperienza (EXP) per il mostro attivo del giocatore.
   - Menu dei comandi 2×2: `Attacca`, `Zaino`, `Party`, `Fuggi`.
   - Selezione delle mosse con griglia 2×2 e indicatore del tipo mossa.
   - Selezione dinamica delle mosse del nemico prese dal proprio moveset reale.

2. **Database di Mosse e Mostri da JSON**
   - Più di 10 mosse caricate dinamicamente da [moves.json](file:///c:/Users/andre/Documents/GitHub/MDP25-26/app/src/main/resources/data/moves.json) (fuoco, acqua, erba, normale) con potenze e calcolo dell'efficacia elementale.
   - Mostri (starter e selvatici) caricati dinamicamente da [monsters.json](file:///c:/Users/andre/Documents/GitHub/MDP25-26/app/src/main/resources/data/monsters.json), completo di statistiche e moveset personalizzati.

3. **Morte dei Mostri (Permadeath)**
   - Quando i punti vita di un mostro in squadra scendono a 0, il mostro viene rimosso permanentemente dal party.
   - Se rimangono altri mostri in squadra, la run continua con il mostro successivo; altrimenti viene attivato il Game Over e si ritorna al menu principale.

4. **Gestione Squadra con Lista Cliccabile**
   - Reclutamento di nuovi mostri dall'Accampamento (Hub) fino a un massimo di **3 mostri**.
   - La squadra è rappresentata da una **lista di bottoni cliccabili**:
     - Cliccando normalmente su un mostro della lista, viene impostato come **Attivo** (in prima posizione per il prossimo combattimento).
     - Cliccando su "Usa Pozione Salute" e poi su un mostro, la cura viene applicata a quel mostro specifico (con consumo della pozione).

5. **Sistema di Salvataggio su 3 Slot**
   - Possibilità di salvare e caricare la partita in 3 slot indipendenti (`savegame_1.json`, `savegame_2.json`, `savegame_3.json`).
   - Salvataggio dello stage corrente, dei dati del giocatore, dell'inventario e di tutte le statistiche aggiornate di ogni mostro in squadra.
   - Prevenzione della sovrascrittura accidentale con richiesta di doppia conferma.

---

## 🛠️ Tecnologie Utilizzate

- **Linguaggio**: Java 26
- **Interfaccia Grafica**: JavaFX (FXML + CSS per il design, programmatica per i menu dinamici)
- **Serializzazione**: Gson 2.10.1 (salvataggio ed importazione JSON)
- **Gestore del Progetto**: Gradle 9.6+
- **Font**: "Press Start 2P" (Pixel-art Google Font caricato dinamicamente a runtime)

---

## 📐 Architettura del Progetto

Il progetto segue rigidamente l'architettura **Model-View-Presenter (MVP)** per garantire la massima estendibilità:

- **Model** (`model/`): Contiene la logica di gioco pura (mostri, mosse, statistiche, inventario, progressione roguelike). Completamente indipendente dalla UI.
- **View (FXML + CSS)** (`resources/`): Definisce l'aspetto visivo ed il layout. Gli stili di PokéRogue (come i pulsanti con bordo ciano ad effetto hover e i testi con drop-shadow) sono applicati tramite CSS.
- **Controller** (`view/controller/`): I gestori associati ai file FXML che si occupano unicamente di ricevere gli eventi della UI e di aggiornare gli elementi grafici.
- **Presenter** (`view/BattleScreen.java`, `view/handler/`): Il ponte che riceve le interazioni della View, le elabora tramite il Model e indica alla View come aggiornarsi con i nuovi dati.

---

## 🏁 Istruzioni per l'Avvio

Assicurati di avere una JDK (Java Development Kit) 26 o superiore installata sul tuo computer.

### Compilazione del progetto
```bash
./gradlew compileJava
```

### Avvio del Gioco
```bash
./gradlew run
```

### Pulizia del Progetto (Clean)
```bash
./gradlew clean
```

---

## 📁 Struttura della Directory principale

```
app/
├── src/
│   ├── main/
│   │   ├── java/it/unicam/cs/mpgc/rpg125935/
│   │   │   ├── App.java                   # Entry point dell'applicazione JavaFX
│   │   │   ├── model/                     # Logica di business (creatures, moves, player, core, battle)
│   │   │   ├── save/                      # Logica di persistenza (SaveManager, GameSaveState)
│   │   │   └── view/                      # Schermate di gioco (Screens, Controllers, Components)
│   │   │
│   │   └── resources/
│   │       ├── css/                       # Fogli di stile CSS (battle.css)
│   │       ├── fonts/                     # Font pixel-art per la UI retro
│   │       └── fxml/                      # File di layout JavaFX (battle_screen, stat_panel)
```
