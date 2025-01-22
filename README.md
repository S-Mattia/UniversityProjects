# TeamWork Projects
**Acquired skills:** TeamWork, algorithms and Data Structures, algorithms complexity, programming language compiler, DFA, sorting algorithms, Prim's algorithm, Java, JavaFX, C, memoization, OOP, dynamic programming, Unit testing, web development (HTML, CSS, Bootstrap, JavaScript, PHP, PHPMailer), SQL, Design of DataBase.

## Team:
   - **[Gabriele Sandri](https://github.com/GabrieleSandri)**
   - **[Sandri Mattia](https://github.com/S-mattia)**

## 1. Algorithms and Data Structures
*(Project for a University exam)*
This project involves the implementation of various algorithms and data structures in Java and C. It includes sorting algorithms, data structures like priority queues and graphs, and dynamic programming techniques. `(/AlgorithmProject)`

### Deliverables:
   - Implementation of Marge Sort and Quick Sort algorithms, optimized for datasets up to 20 million records. `(EX1)`
   - Dynamic Programming Edit distance calculation with memoization. `(EX2)`
   - Implementation od data Structures Including priority queues and graph structures. `(EX3_4)`
   - Prim's algorithm using a priority queue and graph. `(EX3_4)`
   

## 2. Custom Programming Language
*(Project for a University exam)*
This project involves the creation of a basic programming language for the JVM, including a lexer and parser, as well as a simple DFA (Deterministic Finite Automaton) project. The lexer converts the source code into tokens, while the parser processes these tokens to generate Jasmin bytecode instructions. `(/CompilerProject)`

### Deliverables:
   - Some exercises on DFA, Parser, Lexer. `(SEZ_1 SEZ_3 SEZ_4 SEZ_FC)`
   - A lexer that tokenizes the input program. `(SEZ_2)`
   - A translator that translate the token producted by lexer into instructions jasmine. `(SEZ_5)`
   - support classes. `(SEZ_5)`

## 3. Database design for a food delivery application:
*(Project for a University exam)*
This project involved designing a database for a food delivery application, from the ER diagram to the final SQL code. The process included analyzing requirements, creating an ER diagram to model entities like customers, orders, and restaurants, and normalizing the schema to 3NF. The SQL script provides the PostgreSQL code to create the database structure, define relationships, and insert sample data. `(/DatabaseProject)`

### Deliverables: 

   - A `PDF` report detailing the design, ER diagram, and normalization steps.
   - An `SQL` script to create the PostgreSQL database.


## 4. Server & Client Email Application 
*(Project for a University exam)* 
This project involves the creation of a Client-Server mail application built with Java, JavaFX, and Maven, following the MVC pattern. The server handles incoming requests from multiple clients, processes them, and manages user email data stored in files. The client application, with a graphical user interface, allows users to send, receive and manage emails. The communication between the server and client is done via sockets.
(Files were used to store data instead of a database due to a specific requirement of the project.). The application is built and managed with Maven, and development was done using IntelliJ IDEA Ultimate as IDE. `(/ServerClientMailApplication)`

### Deliverables:  

- Server: A multithreaded coded server that listens on a given port, handles client connections, processes email requests, and uses a ReadWriteLock to synchronize access to email files. It handles operations like sending, receiving, and deleting emails. `(Server)`

- Client: A client application that communicates with the server to send and receive emails. It manages the user interface with JavaFX and allows the user to view their inbox, send new emails, and handle disconnections from the server. If the server goes offline, the client alerts the user and ensures that any pending actions are lost if not completed. `(Client)`

- Model: all the classes that allow the server to manage, read and view the data stored into the files. `(SharedModel)`


## 5. Nuclear Power Plant simulation project:
*(Project for a University exam)* 
This project simulates the chain reaction of a nuclear power plant, using multi-process coding in a Unix environment. The simulation involves inter-process communication (IPC) and synchronization mechanisms to manage and coordinate different roles including: master, atom, activator, inhibitor, and feeder processes. The master process starts the simulation and handles statistics, while other processes simulate energy production, inhibition, activation, split and waste generation. The project uses Unix concepts such as Process control, Semaphores, Synchronization, Shared memory, Message queues, and Signal handling. `(/NuclearPowerPlantSimulation)`

## Deliverables:
- `src` Source code of the project
- `include` Header Files of different modules
- `library` contain the library used in the project
- `config` contain the configuration file
  
## 6. Website for Accommodation Facility
A web development project for an accommodation facility, featuring front-end technologies (HTML, CSS, JavaScript) and back-end functionality in PHP.

[`https://www.domuslanghe.it`](https://www.domuslanghe.it)

