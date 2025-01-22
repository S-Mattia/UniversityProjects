package com.example.client.support;

import Model.email;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class deleteEmail implements Runnable {

    private ScheduledExecutorService s;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private email email;
    private final ObservableList<email> emailItems;
    //first time flag
    private boolean firstTime = true;

    //constructor for the runnable class
    public deleteEmail(email email, ScheduledExecutorService s, ObservableList<Model.email> emailItems) {
        this.s = s;
        this.email = email;
        this.emailItems = emailItems;
    }

    /*Function that try to delete an email if exist*/
    @Override
    public void run() {
        //create a new client communication class
        clientCommunication client = new clientCommunication();

        try{
            client.connectServer();
            in = client.getIn();
            out = client.getOut();
            String response = removeemail();
            client.disconnectServer();

            Platform.runLater(() -> {
                //checkin if the server removed the email
                if(response.equals("Information: The email has been deleted...")){
                    synchronized (this.emailItems) {
                        this.emailItems.remove(email);
                    }
                }
            });

        }catch (IOException e ) {
            if(firstTime){
                firstTime = false;
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Sending Email Status");
                    alert.setHeaderText("Error from the application:");
                    alert.setContentText("You lost the connection to the server, on logout the progress will be losted!");
                    alert.showAndWait();
                });
            }
            System.out.println("Retrying to delete the email");
            s.schedule(this,5, TimeUnit.SECONDS);

        }catch (Exception e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed request");
                alert.setHeaderText("Response from the server:");
                alert.setContentText("Something went wrong");
                alert.showAndWait();
            });
        }
    }

    public String removeemail(){
        try{
            //set the state of the email to 2 to ask the server to remove it
            this.email.setState(2);
            out.writeObject(this.email);
            Object obj = in.readObject();

            //checking if is deleted
            if (obj instanceof String) {
                return (String) obj;
            }else{
                throw new ClassNotFoundException();
            }
        }catch (ClassNotFoundException | IOException ex) {
            return "Something went wrong in the deleting process";
        }
    }
}
