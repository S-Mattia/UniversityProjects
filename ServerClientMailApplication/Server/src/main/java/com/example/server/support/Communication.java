package com.example.server.support;

import Model.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;


class Communication implements Runnable{

    @FXML
    private TextArea log;
    private final Socket communicationSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Lock rl;
    private final Lock wl;

    //constructor for the runnable of the server
    public Communication(Socket communicationSocket, TextArea log, Lock rl, Lock wl) {
        this.communicationSocket = communicationSocket;
        this.log = log;
        this.rl = rl;
        this.wl = wl;
    }

    //Main runnable for the server
    @Override
    public void run(){
        //path of the directory where are stored the file data of every user
        String memoryPath = "";

        //trying to create new input and output stream on the socket
        try {
            OutputStream outStream = communicationSocket.getOutputStream();
            out = new ObjectOutputStream(outStream);
            InputStream inStream = communicationSocket.getInputStream();
            in = new ObjectInputStream(inStream);

        } catch (IOException e) {
            System.err.println("Error opening input/output Stream: " + e.getMessage());
        }

        //try to communicate with the client
        try {
            Object message = in.readObject();

            //login section of code
            if (message instanceof user) {
                user user = (user) message;
                String path = memoryPath + user.getEmail() + ".txt";

                try {
                    //open the file at the current path
                    File file = new File(path);

                    // checking if the user exist
                    wl.lock();
                    try {
                        if (!file.exists() || file.length() == 0) {
                            log.appendText("An unsigned user tried to login...\n");
                            out.writeObject("Non-existent user");
                            return;
                        }
                    }finally {
                        wl.unlock();
                    }

                    //read the inbox of the current user
                    inBox inbox;


                    //getting the user's state for resolve user request
                    switch (user.getState()) {
                        case 0: // Login: sending all the inbox to the client
                            wl.lock();// lock for reading
                            try (ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(file))) {
                                inbox = (inBox) fileInput.readObject();
                                inbox.newEmail();
                                log.appendText("User "+inbox.getUser()+" logged in...\n");

                                //lock the file to update the status of the emails
                                try (ObjectOutputStream fileOutput = new ObjectOutputStream(new FileOutputStream(file))) {
                                    fileOutput.writeObject(inbox);
                                }
                            }finally{
                                wl.unlock();
                            }
                            out.writeObject(inbox);
                            break;

                        case 1: // Refresh: invia solo le nuove email

                            wl.lock();// lock for reading
                            try (ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(file))) {
                                inbox = (inBox) fileInput.readObject();
                                ArrayList<email> newEmails = inbox.newEmail();

                                //already up to date
                                if (newEmails.isEmpty()) {
                                    out.writeObject("Already up to date");

                                    //sending the new emails
                                } else {
                                    out.writeObject(newEmails);


                                    //lock the file to update the status of the emails
                                    try (ObjectOutputStream fileOutput = new ObjectOutputStream(new FileOutputStream(file))) {
                                        fileOutput.writeObject(inbox);
                                    }
                                    log.appendText("User "+inbox.getUser()+" received new emails...\n");
                                }
                            }finally{
                                wl.unlock();
                            }

                            break;

                        default:
                            out.writeObject("Error: not valid state of user.");
                            break;
                    }

                } catch (FileNotFoundException e) {
                    System.err.println("File not found: " + e.getMessage());
                    out.writeObject("Errore: file not found.");
                } catch (IOException e) {
                    System.err.println("Error of IO: " + e.getMessage());
                    out.writeObject("Error on file access.");
                } catch (ClassNotFoundException e) {
                    System.err.println("Error of deserialization: " + e.getMessage());
                    out.writeObject("Error class not found.");
                }


            }else if(message instanceof email)
            {
                email email = (email)message;

                try {

                    switch (email.getState()) {

                        case 0: // send email

                            //array of receivers and failed receivers
                            ArrayList<user> users = email.getReceivers();
                            ArrayList<user> failedUsers = new ArrayList<user>();
                            ArrayList<user> successUsers = new ArrayList<user>();

                            for (user user : users) {
                                String path = memoryPath + user.getEmail() + ".txt";
                                File file = new File(path);

                                //check if the file exist
                                if (!file.exists() || file.length() == 0) {
                                    if(!failedUsers.contains(user)) {
                                        failedUsers.add(user);
                                    }
                                    // add the current recevier to the list of failed
                                    continue;
                                }
                                if(!successUsers.contains(user)){
                                    successUsers.add(user);
                                }
                            }

                            email.setReceivers(successUsers);


                                //try sending the email to every receiver
                            try {
                                for (user user : successUsers) {
                                    inBox inBox;
                                    String path = memoryPath + user.getEmail() + ".txt";
                                    File file = new File(path);

                                    //send the email
                                    wl.lock();
                                    try{
                                        //reaching the file of the receiver and read it
                                        try (ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(path))) {
                                            inBox = (inBox) fileInput.readObject();
                                        } catch (IOException | ClassNotFoundException e) {
                                            System.err.println("an error occurred reading the file of the receiver " + user.getEmail() + ": " + e.getMessage());
                                            failedUsers.add(user); // Add the receiver to the list of failed
                                            continue; // Skip the failed receiver
                                        }

                                        //set the owner of the email and add it to his inbox
                                        email.setOwner(user);
                                        inBox.addEmail(email);

                                        //save the changes of the inbox
                                        try (ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(path))) {
                                            fileOut.writeObject(inBox);
                                        } catch (IOException e) {
                                            System.err.println("an error occurred writing on the file of user " + user.getEmail() + ": " + e.getMessage());
                                            failedUsers.add(user); // Add the receiver to the list of failed
                                            continue; // Skip the failed receiver
                                        }
                                        log.appendText("User "+email.getSender()+" sent an email to " + user+"...\n");
                                    }finally{
                                        //unlock the wrinting lock
                                        wl.unlock();
                                    }
                                }

                                // Check if there are some failed user
                                if (!failedUsers.isEmpty()) {
                                    // send the list of failed receivers
                                    out.writeObject(failedUsers);
                                } else {
                                    //the mail has been sent to everyone
                                    out.writeObject("Information: The email has been sent to everyone");
                                }

                            } catch (Exception e) {
                                System.err.println("an error occured sending the email: " + e.getMessage());
                                out.writeObject("Error: the email hasn't been sent");
                            }
                            break;

                        case 2: //delete email
                            try {
                                String path = memoryPath + email.getOwner().getEmail() + ".txt";
                                inBox inBox;

                                // open and read the inbox of the user
                                wl.lock();
                                try{

                                    try (ObjectInputStream fileInput = new ObjectInputStream(new FileInputStream(path))) {
                                        inBox = (inBox) fileInput.readObject();
                                    }

                                    // Delete email from the inbox
                                    boolean removed = inBox.removeEmail(email);

                                    if(removed){
                                        // save the changes of the inbox
                                        out.writeObject("Information: The email has been deleted..."); // Conferma la cancellazione

                                    }else{

                                        out.writeObject("Error: the email doesn't exist");
                                    }

                                    try (ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(path))) {
                                        fileOut.writeObject(inBox);
                                    }
                                    log.appendText("User "+email.getOwner()+" deleted an email...\n");
                                }finally {
                                    wl.unlock();
                                }
                            } catch (FileNotFoundException e) {
                                out.writeObject("the email hasn't been deleted"); // Utente inesistente
                                System.err.println("file not found " + e.getMessage());
                            } catch (IOException e) {
                                out.writeObject("an error occurred deleting the email: " + e.getMessage());
                                System.err.println("IOexeption error: " + e.getMessage());
                            } catch (ClassNotFoundException e) {
                                out.writeObject("Error: Class not found: " + e.getMessage());
                                System.err.println("Error: Deserialization error " + e.getMessage());
                            }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else
            {
                System.err.println("Error during the communication...");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                communicationSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing communication socket");
            }
        }

    }
}