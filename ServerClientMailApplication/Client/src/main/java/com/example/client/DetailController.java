package com.example.client;

import com.example.client.support.deleteEmail;
import com.example.client.support.sendEmail;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.*;
import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DetailController {

    @FXML
    public TextArea text;
    @FXML
    private Label emailSubjectLabel;
    @FXML
    private Label emailSenderLabel;
    @FXML
    private TextArea emailBodyArea;
    @FXML
    private TextField objectField;
    @FXML
    private Label errorMail;
    @FXML
    private TextField mail;
    @FXML
    private Tab Tab;
    @FXML
    private TabPane tabPane; // Associa il TabPane

    private ScheduledExecutorService s;

    private ObservableList emailItems;
    private email email;
    private boolean validate = false;


    // Metodo per chiudere la finestra dei dettagli
    @FXML
    private void onCloseButton() {
        Stage stage = (Stage)this.emailSubjectLabel.getScene().getWindow();
        stage.close();
    }

    // Imposta i dettagli della email selezionata
    @FXML
    public void setEmailDetails(email email) {
        tabPane.getTabs().remove(Tab);
        this.emailSubjectLabel.setText(email.getObject());
        this.emailSenderLabel.setText("From: " + email.getSender());
        this.emailBodyArea.setText(email.getContent());
        this.email = email;
    }

    @FXML
    protected void checkInput() {
        if (!(mail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}+(,+[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})*$"))) {
            errorMail.setText("Invalid E-mail");
            validate = false;
        } else {
            errorMail.setText("");
            validate = true;
        }
    }


    @FXML
    private void onReplyButtonClicked() {
        this.mail.setText(email.getSender().toString());
        this.objectField.setText(email.getObject());
        this.text.clear();
        errorMail.setText("");
        Tab.setText("Reply");
        mail.setDisable(true);
        objectField.setDisable(true);
        text.setDisable(false);
        openTab();
        checkInput();
    }

    @FXML
    private void onReplyAllButtonClicked() {
        mail.clear();
        ArrayList<user> receivers = email.getReceivers();
        for (user receiver : receivers) {
            if(!receiver.equals(email.getOwner()) && !receiver.equals(email.getSender())){
                this.mail.appendText(receiver.toString()+",");
            }
        }
        this.mail.appendText(email.getSender().toString());

        this.objectField.setText(email.getObject());
        this.text.clear();
        errorMail.setText("");
        Tab.setText("Reply All");
        mail.setDisable(true);
        objectField.setDisable(true);
        text.setDisable(false);
        openTab();
        checkInput();
    }

    @FXML
    private void onForwardButtonClicked() {
        this.mail.clear();
        this.objectField.setText(email.getObject());
        this.text.setText(email.getContent());
        Tab.setText("Forward");
        errorMail.setText("Empty Field");
        mail.setDisable(false);
        objectField.setDisable(true);
        text.setDisable(true);
        openTab();
    }

    @FXML
    private void openTab() {
        // Aggiungi il tab "Reply" al TabPane se non è già presente
        if (!tabPane.getTabs().contains(Tab)) {
            tabPane.getTabs().add(Tab);
        }
        // Seleziona il tab "Reply"
        tabPane.getSelectionModel().select(Tab);

    }

    @FXML
    private void closeTab() {
        // Aggiungi il tab "Reply" al TabPane se non è già presente
        if (tabPane.getTabs().contains(Tab)) {
            tabPane.getTabs().remove(Tab);
        }

    }

    public void onDeleteButtonClick() {
        deleteEmail deleteEmail = new deleteEmail(this.email,this.s,this.emailItems);
        this.s.schedule(deleteEmail,0,TimeUnit.SECONDS);
        onCloseButton();
    }


    public void onSendButtonClick() {

        //if the email is in accordance with the regex sen the email
        if(validate){
            //bring all the receivers
            String[] receivers_string = mail.getText().split(",");
            ArrayList<user> receivers = new ArrayList<>();
            for (String receiver : receivers_string) {
                receivers.add(new user(receiver));
                System.out.println(receiver);
            }

            email emailToSend = new email(email.getOwner(), receivers, objectField.getText(), text.getText() );

            //creating a new runnable of sendemail and set a scheduled execution
            sendEmail sendEmail = new sendEmail(emailToSend, this.s);
            s.schedule(sendEmail, 0, TimeUnit.SECONDS);

            //clear the form and close the writing tab
            mail.clear();
            objectField.clear();
            text.clear();
            closeTab();
        }
    }

    //setter for passing data
    public void setEmailItems(ObservableList emailItems) {
        this.emailItems = emailItems;
    }

    public void setS(ScheduledExecutorService s) {
        this.s = s;
    }
}
