# Progetto_SO
## Partecipanti:
- Sandri Gabriele 1041535 gabriele.sandri509@edu.unito.it
- Sandri Mattia 1041531 mattia.sandri@edu.unito.it

## Consegna:
 Si intende simulare una reazione a catena. A tal fine sono presenti i seguenti processi:
1. un processo master che gestisce la simulazione e mantiene delle statistiche;
2. processi atom che si scindono in altri processi atomo, generando energia;
3. un processo activator che manda un comando di scissione ai processi atomo;
4. un processo inhibitor (disattivabile) che assorbe parte dell'energia e rende scorie alcuni processi atomo;
5. un processo feeder che aggiunge nuovi atom ogni STEP_FEEDER nanosecondi.

## Struttura del progetto:
- /src - Codice sorgente del progetto
- /include - File header del progetto
- /bin - File generati durante la compilazione
- /logs - File log contenete tutte le operazioni compiute durante la simulazione
- /config - File di configurazione
- /library File di librerie esterne utilizzate nell'progetto
- MakeFile

## Implementazione:
### Comunicazione tra processi atomo-attivatore e atomo-inibitore:
I processi atomo comunicano all'attivatore o all'inibitore il proprio pid attraverso l'utilizzo della coda di messaggi.
L'attivatore e l'inibitore, una volta recuperato il pid di un atomo dalla coda di messaggi mandano un segnale all'atomo, rispettivamente SIGUSR1(attivazione) e SIGUSR2(inibizione).

### Scissione:
In caso di segnale di attivazione l'atomo si scinde liberando energia, mentre in caso di segnale di inibizione, gli scenari sono 2:
1. si scinde cedendo parte dell'energia all'inibitore;
2. diventa scoria.

### Statistiche condivise:
Il master stampa a schermo ogni secondo, le statistiche della simulazione, i valori sono costantemente aggiornati dai processi per ogni operazione compiuta. la struttura della memoria condivisa è composta dalle seguenti variabili:
1. pid del master;
2. stato dell'inibitore;
3. numero di atomi inseriti relativo e assoluto;
4. numero di attivazioni relativo e assoluto;
5. numero di scissioni relativo e assoluto;
6. energia prodotta relativa e assoluta;
7. energia consumata relativa e assoluta;
8. numero di scorie relativo e assoluto;
9. numero di atomi attualmente in esecuzione;

### Sincronizzazione:
Per sincronizzare i processi ed evitare risultati non consistenti, utilizziamo un set di smafori(solo per variabili relative all'ultimo secondo):
1. START_SEM per dare il via alla simulazione;
2. ATOMS_SEM per modificare il numero di atomi inseriti;
3. SPLITTING_SEM per modificare il numero di scissioni;
4. WASTE_SEM per modificare il numero di scorie;
5. ACTIVATION_SEM per modificare il numero di attivazioni; 
6. ABSORBING_SEB per modificare l'energia assorbita dall'inibitore;
7. ENERGY_SEM per modificare l'energia prodotta;
8. MSGQ_SEM per leggere dalla coda di messaggi;
9. LOG_SEM per scrivere sul filelog.

## Comandi:
| Combinazione | Descrizione |
|:--|--:|
| CTRL + c | Fine della simulazione ordinata dallo user |
| CTRL + z | Attivazione/disattivazione del processo inibitore |

## Configurazione:
i parametri di configurazione vengono letti a tempo di esecuzione attravero un file config.ini, la struttura dei file del file di configurazione è la seguente: 
| Nome Campo                   | Descrizione                                                   |
|------------------------------|---------------------------------------------------------------|
| n_atoms_init                 | Numero di atomi creati dal master ad inizio simulazione       |
| energy_demand                | Energia consumata dal master ogni secondo                     |
| energy_explode_threshold     | Soglia massima energia accomulabile                           |
| simulation_duration          | Durata della simulazione in secondi                           |
| n_atom_max                   | Numero atomico massimo generabile                             |
| n_atom_min                   | Numero atomico minimo affinché un atomo diventi scoria        |
| feeder_step                  | Velocità del processo feeder                                  |
| n_new_atoms                  | Numero di atomi inseriti dal feeder_step ogni step nanosecondi|
| activator_step               | Velocità del processo attivatore                              |
| inhibitor_step               | Velocità del processo inibitore                               |
| in_mode                      | Stato del processo inibitore all'inizio della simulazione     |
| inhibitor_percent_absorbing  | Proporzione di energia assorbita dall'inibitore               |
| inhibitor_probability_waste  | Probabilità che l'inibitore renda scoria un atomo             |


## Librerie: 
all'interno del progetto sono state utilizzate le seguenti librerie:
1. stdio.h
2. stdlib.h 
3. sys/ipc.h, sys/shm.h, sys/sem.h, sys/msg.h per creare e effetuare operazioni su strutture ipc
4. signal.h per modificare o effetuare operazioni su handler dei vari segnali
5. time.h fornisce funzioni per ottenere e gestire il tempo e le date. Utilizzato per registrare l'orario in cui vengono effettuate le operazioni (es. per scrivere timestamp nei log).
6. errno.h per avere accesso alle costanti associate agli errori
7. unistd.h per avere accesso a varie chiamate di sistema
7. stdarg.h per utilizzare liste di argomenti in funzioni
8. ini.h libreria di terze parti per poter recuperare le informazioni di configurazione dal file config.ini (reperibile al link: https://github.com/benhoyt/inih.git )

## Note: 
le funzioni per la gestione della maschera di segnali, come : block_signals(), unblock_signals(), reset_signal(), sono state create partendo da un progetto di esempio presentato nel anno accademico 2022/2023.