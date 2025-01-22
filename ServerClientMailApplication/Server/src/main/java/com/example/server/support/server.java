package com.example.server.support;

import javafx.scene.control.TextArea;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * Create a new server socket, allowing multiple connection to the server using a pool of thread
 * @author: Sandri Mattia, Sandri Gabriele
 *
 */
public class server extends Thread {

    private ServerSocket serverSocket;
    ExecutorService executor;
    private final int port;
    private final TextArea log;
    private boolean running = false;
    private ReadWriteLock rwl;
    private Lock rl,wl;

    /**
     * constructor of the class server.
     * when it's constructed it's also create a new ReadWriteLock
     * to synchronize all the thread when they're reading or writing
     * on the Memory
     *
     * @param log a Text area where every log can write information
     *            and error about the connection and communication on the socket
     * @param concurrentUser number of thread in the pool
     * @param port number of the port to open;
     *
     */
    public server(int port, int concurrentUser, TextArea log) {
        this.port = port;
        executor = Executors.newFixedThreadPool(concurrentUser);
        this.log = log;
        rwl = new ReentrantReadWriteLock();
        rl = rwl.readLock();
        wl = rwl.writeLock();
    }


    /**
    * it opens a new server socket using the port assigned in the constructor, then using a while loop
    * it can take all the new connection to the server. when a new connection income the created client socket
    * is passed as argument in the constructor method of the class Communication,that is responsible to listen
    * the request of client, execute the command and send a response.
    */
    @Override
    public void run(){
        try {
            running = true;
            //Create a server socket
            serverSocket = new ServerSocket(port);

            // Main cycle that open the client socket for all the new connection
            while (running) {
                try {
                    //wait for a new client
                    Socket clientSocket = serverSocket.accept();

                    // create a new runnable to execute the communication with clients
                    Communication communication = new Communication(clientSocket,log,rl,wl);

                    //send the task to the pool
                    executor.execute(communication);

                } catch (IOException e) {
                    if (running) {
                        System.err.println("Errore nell'accettazione della connessione: " + e.getMessage());
                    } else {
                        System.out.println("Server chiuso durante l'attesa di connessioni.");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("an error occurred while opening the server socket... " + e.getMessage());
        }
    }


    /**
     * It stops the server, all the new connection are
     * refused by stopping the main loop of Run method
     * and all the task already started have 10 seconds
     * time to terminate.
     */
    public void close(){
        if(running){
            System.out.println("Server on closing...");
            running = false; // set the flag to terminate the cycle
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close(); // Chiudi il ServerSocket
                    System.out.println("Server closed...");

                    executor.shutdown(); // Inizia la procedura di shutdown
                    if (!executor.awaitTermination(10, TimeUnit.SECONDS)) { // Attende massimo 2 secondi
                        System.err.println("Some of the active task didn't terminate the execution. Forced closure attempt...");
                        executor.shutdownNow(); // Forza l'interruzione dei task
                    }
                    log.appendText("Server closed...");

                } catch (IOException e) {
                    System.err.println(" " + e.getMessage());
                } catch (InterruptedException e) {
                    System.err.println("Attesa interrotta: " + e.getMessage());
                    Thread.currentThread().interrupt(); // Ripristina lo stato di interruzione del thread

                }
            }
        }else{
            System.out.println("Il Server è già chiuso...");
        }

    }

}
