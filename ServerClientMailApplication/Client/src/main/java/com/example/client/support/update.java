package com.example.client.support;

import Model.*;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javax.tools.Tool;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * it allows the user to request a recurring refresh
 * of the mail list
 */
public class update implements Runnable {

    @FXML
    private StringProperty status;
    private user user;
    private final ObservableList<email> ListItem;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    /**
     * constructor of the class update.
     *
     * @param user the email that the client want to send
     * @param ListItem the observable list to update mail
     * @param status label to write the status of the connection
     */
    public update(user user, ObservableList<email> ListItem, StringProperty status) {
        this.user = user;
        this.ListItem = ListItem;
        this.status= status;
    }

    /**
     * it creates a new instance of ClientCommunication to
     * open an input/output Object stream, then sends a request
     * to refresh the inbox to the server. If the response is an
     * arraylist, it contains the incoming emails, otherwise, if
     * it's a String then the inbox is already up-to-date.
     */
    @Override
    public void run() {

        clientCommunication client = new clientCommunication();
        try {
            client.connectServer();

            in = client.getIn();
            out = client.getOut();

            //send the refresh command and receive the list of new emails
            System.out.println("Refreshing");

            out.writeObject(user);

            Object obj = in.readObject();

            client.disconnectServer();

            //if there are incoming email
            if(obj instanceof ArrayList<?>) {
                ArrayList<email> list = (ArrayList<email>) obj;
                Platform.runLater(() -> {
                    // add the new emails to the observable list and sort it by the date
                    synchronized (this.ListItem) {
                        this.ListItem.addAll(list);
                        this.ListItem.sort(Comparator.comparing(email::getDate).reversed());
                        Toolkit toolkit = Toolkit.getDefaultToolkit();
                        toolkit.beep();
                    }

                });

            //if the inbox is already up to date
            }else if(obj instanceof String) {
                System.out.println((String)obj);
            } else {
                throw new ClassNotFoundException();
            }

            //correctly connected to the server update the status label to connected
            Platform.runLater(() -> {
                status.set("Connected");
            });

        //an error occurred while updating set the status label to not connected
        } catch (IOException ex) {

            Platform.runLater(() -> {
                status.set("Not Connected");
            });

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
