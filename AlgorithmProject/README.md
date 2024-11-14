
## Componenti del gruppo:
- Sandri Gabriele
- Sandri Mattia

## Relazione Tecnica e Tempistica su Merge-Sort e Quick-Sort e Edit\_Distance\_Dyn:

1. Si analizza, Merge-Sort e Quick-Sort, implementati in C e utilizzati come librerie all’interno di sort-Record.c che ordina 20 milioni di record. Ogni record è composto da tre campi: una stringa, un intero e un float. 
1. Analisi di Edit\_distance\_dyn: propone soluzioni alternative per parole non presenti in dizionario, in base alla loro edit\_distance;

Le performance degli algoritmi sono state misurate su due piattaforme: Mac e Windows. I tempi sono stati raccolti per diversi scenari di ordinamento. Etichettati come <field 1 a 3> <algo 1 a 2>;


Mac:

\- 1 1 (MergeSort su string): 28s

\- 1 2 (QuickSort su string): Non termina

\- 2 1 (MergeSort su int): 28s

\- 2 2 (QuickSort su int): 36s

\- 3 1 (MergeSort su float): 28s

\- 3 2 (QuickSort su float): 36s

Windows:

\- 1 1 (MergeSort su string): 64s

\- 1 2 (QuickSort su string): Non termina

\- 2 1 (MergeSort su int): 56s

\- 2 2 (QuickSort su int): 83s

\- 3 1 (MergeSort su float): 57s

\- 3 2 (QuickSort su float): 62s


Riducendo il numero di record a 1 milione (*dove QuickSort su string termina*):

\- 1 1: 3s  - 1 2: 15s  - 2 1: 3s  - 2 2: 3s  - 3 1: 2s  - 3 2: 3s


Mac:

\- (Edit\_distance\_dyn): 14s

Windows:

\- (Edit\_distance\_dyn): 32s
## Motivi per i Tempi Diversi:
1\. *Gestione della memoria e I/O:* MacOS e Windows gestiscono la memoria e le operazioni di I/O in maniera differente.

2\. *Ottimizzazioni del compilatore:* Il compilatore su MacOS (clang) e su Windows (MSYS2) influiscono in maggioranza, sì può notare infatti che il file compilato su mac pesa 30kbyte mentre su Windows supera i 130kByte.

## Problemi con Quick-Sort e Stringhe Ripetute:
Quick-Sort utilizza il primo elemento come pivot nella sua funzione di partition. Questo approccio non è ottimale in presenza di molti elementi ripetuti, poiché può portare a una partizione sbilanciata e quindi a una complessità quadratica. Questo è particolarmente evidente con le stringhe ripetute.

## Soluzione Proposta
Per migliorare le performance di Quick-Sort, si potrebbe adottare un metodo diverso per la scelta del pivot, come il "three way quicksort": questa tecnica migliora le probabilità di ottenere partizioni bilanciate, riducendo la probabilità di raggiungere la complessità peggiore.

## ` `Conclusione
Merge-Sort si dimostra performante su entrambe le piattaforme, mentre QuickSort soffre in presenza di molti elementi ripetuti, specialmente con stringhe. L'adozione del "three way partition" per la scelta del pivot potrebbe mitigare i problemi riscontrati con QuickSort. Le differenze di performance tra Mac e Windows sono principalmente dovute alle differenze nella gestione della memoria e ottimizzazioni del compilatore. L’utilizzo di una matrice in Edit\_distance\_Dyn, per registrare i valori già calcolati permette di diminuire la complessità asintotica giungendo alla fine del programma nonostante numeri elevati di cicli.
