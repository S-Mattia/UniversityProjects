package com.example.client.support;

import Model.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * it allows the user to send a mail to the server.
 * in case of error while connecting to the server, the
 * runnable resend himself to the pool of thread received
 * in the constructor
 */
public class sendEmail implements Runnable {

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private email email;

    private ScheduledExecutorService s;

    private Boolean firstTime = true;

    /**
     * constructor of the class sendEmail.
     *
     * @param email the email that the client want to send
     * @param s the ScheduledExecutorService that manage
     *          all the request that the user make to the
     *          server
     */
    public sendEmail(email email, ScheduledExecutorService s) {
        this.email = email;
        this.s = s;
    }

    /**
     * it creates a new instance of clientCommunication to open
     * an input/output Object stream then try to send the mail
     * to the server by calling the function send(). in case of
     * connection error it reschedules himself into the scheduled
     * executor service
     */
    @Override
    public void run() {
        clientCommunication client = new clientCommunication();

        try {
            client.connectServer();
            in = client.getIn();
            out = client.getOut();
            send();
            client.disconnectServer();

        }catch (IOException e) {
            if(firstTime){
                firstTime = false;
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Sending Email Status");
                    alert.setHeaderText("Response from the application:");
                    alert.setContentText("You're not connected to the server, the mail will be sent as soon as possible.");
                    alert.showAndWait();
                });
            }
            System.out.println("retrying to send the mail");
            s.schedule(this,5, TimeUnit.SECONDS);

        }catch (RuntimeException | ClassNotFoundException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed request");
                alert.setHeaderText("Response from the server:");
                alert.setContentText("Something went wrong");

                // Mostra l'Alert
                alert.showAndWait();
            });

       }
    }


    /***
     * Support method that send the email to the server and
     * wait for a response from the server, if it's received
     * an arraylist then some of the receiver didn't exist.
     */
    public void send() throws ClassNotFoundException, IOException {
        //sending email to server
        System.out.println("Sending email");
        out.writeObject(this.email);
        Object obj = in.readObject();

        //show the receivers not found
        if(obj instanceof ArrayList<?>) {

            Platform.runLater(() -> {
                ArrayList<user> failedUser = ((ArrayList<user>) obj);
                String failed = "Not sent to:";
                for(user u : failedUser) {
                    failed= failed + u + ", ";
                }
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed request");
                alert.setHeaderText("Response from the server: ");
                alert.setContentText(failed);
                alert.showAndWait();
            });

        //email sent successfully
        } else if (obj instanceof String) {
            Platform.runLater(() -> {
                if(firstTime){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Request state:");
                    alert.setHeaderText("Response from the server:");
                    alert.setContentText(obj.toString());
                    alert.showAndWait();
                }

            });
        }else{
            throw new ClassNotFoundException();
        }
    }
}
