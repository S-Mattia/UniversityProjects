package com.example.client;


import com.example.client.support.clientCommunication;
import com.example.client.support.sendEmail;
import com.example.client.support.update;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientController {
    @FXML
    public TextField objectField;
    @FXML
    public Label errorObject;
    @FXML
    public Label inBoxTitle;
    @FXML
    public TextField reciverAdress;
    @FXML
    public TextArea emailContent;
    @FXML
    private Label errorMail;
    @FXML
    private Label statusConnection;
    @FXML
    private TextField emailLogin;
    @FXML
    private ListView<email> emailListView;

    //number of concurrent thread
    private int threadNumber = 5;
    //bloolean to check the email in login
    private boolean validate = false;
    //user of the client
    private user user;
    //scheduled executor
    private ScheduledExecutorService s;
    //obsevabol list for display the list of the emial of the user
    private ObservableList<email> emailItems;
    //StringProperty for the connection label
    private StringProperty status;

    //constructor for scheduled executor
    public void setScheduledExecutorService(ScheduledExecutorService s) {
        this.s = s;
    }

    //getter and setter for the user
    public void setUser(user user) {
        this.user = user;
    }
    public void getUser(user user) {
        this.user = user;
    }

    public ObservableList<email> getEmailItems() {
        return emailItems;
    }
    public void setEmailItems(ObservableList<email> emailItems) {
        this.emailItems = emailItems;
    }

    /*Function to enter in personal pages if the inserted email in login form is in accordance with the regex and exist the user*/
    @FXML
    protected void onLoginButtonClick(ActionEvent e) throws IOException {
        //if the email is in accordance with the regex try to enter
        if (validate) {
            //bring from the form the inserted email
            user user = new user(emailLogin.getText());
            user.setState(0); //setting to 0 the state of the user for login on server
            //printing on console to debug
            System.out.println(user);
            System.out.println(user.getState());

            try {
                //opening a new client class of ClientCommunication to comunicate with the server
                clientCommunication client = new clientCommunication();
                //sending the email user to enter, and recive the inbox if the user exist or a string "User not exist"
                Object obj = client.login(user);

                //if the recived obj is an ibox, open a new page with personale data
                if(obj instanceof inBox) {
                    inBox inBox = (inBox) obj;
                    this.user = inBox.getUser();
                    // Crea una lista di email di esempio
                    ArrayList<email> emailList=inBox.getEmailist();

                    //loading FXML file to display (client-view.fxml)
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("client-view.fxml"));
                    Parent root = fxmlLoader.load();

                    // get the controller of the new page
                    ClientController controller = fxmlLoader.getController();

                    //set some data for the new controller
                    controller.setUser(user);
                    controller.setEmails(emailList);

                    //create a new string propery to bind hte connection label
                    status = new SimpleStringProperty("Connected");
                    controller.statusConnection.textProperty().bind(status);

                    //setting a pool of 3 threads for the client application
                    s = Executors.newScheduledThreadPool(threadNumber);
                    controller.setScheduledExecutorService(s);//pass it to the new controller

                    //setting the user's state to 1 to ask the server the updated data for the user, and set the task update to the scheduled excutor every 3 s
                    this.user.setState(1);
                    s.scheduleAtFixedRate(new update(this.user,controller.getEmailItems(),status), 0, 3, TimeUnit.SECONDS);

                    // get the new stage of the window
                    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

                    // setting of the new window
                    stage.setScene(new Scene(root, 420, 400));
                    stage.setTitle("Welcome "+this.user.toString());
                    stage.setResizable(true);

                    // set the onClosing evet to shutdown the sheduled executor without loggin out
                    stage.setOnCloseRequest(event -> {

                        // showing an alert to confirm the closing
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Exit Confirmation");
                        alert.setHeaderText("Are you sure to exit?");
                        alert.setContentText("Every pending email will be lost!");

                        //wait for the response
                        alert.showAndWait().ifPresent(response -> {
                            //standard button for confirmation alert
                            switch (response.getButtonData()) {
                                case CANCEL_CLOSE:
                                    event.consume();//consume the event to close the window
                                    break;
                                case OK_DONE:
                                    // shutdown the scheduled executor
                                    if(s!=null && !s.isShutdown()) {
                                        s.shutdownNow();
                                    }
                                    break;
                            }
                        });
                    });
                    stage.show();
                    //the user not exist
                }else if(obj instanceof String) {
                    this.errorMail.setText((String) obj);
                }else{
                    throw new ClassNotFoundException();
                }
            } catch (IOException ex) {
                errorMail.setText("Server Offline");
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

        }
    }

    // Metodo per gestire il click su "Logout"
    @FXML
    protected void onLogoutButtonClick(ActionEvent e){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure to exit?");
        alert.setContentText("Every pending email will be lost!");

        //wait for the response
        alert.showAndWait().ifPresent(response -> {
            //standard button for confirmation alert
            switch (response.getButtonData()) {
                case CANCEL_CLOSE:
                    return;
                case OK_DONE:
                    // shutdown the scheduled executor
                    if (s != null && !s.isShutdown()) {
                        s.shutdownNow();
                    }

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
                    Scene scene = null;
                    try {
                        scene = new Scene(fxmlLoader.load(), 300, 250);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Stage stage = ((Stage) (((Node) e.getSource())).getScene().getWindow());
                    stage.setScene(scene);
                    stage.setTitle("Client Login");
                    stage.setResizable(false);
                    stage.setOnCloseRequest(event -> {});

                    stage.show();
                    break;
                    }
                });

    }

    // Metodo di validazione per la mail
    @FXML
    protected void checkInput() {
        if (!(emailLogin.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))) {
            errorMail.setText("Invalid E-mail");
            validate = false;
        } else {
            errorMail.setText("");
            validate = true;
        }
    }

    @FXML
    protected void checkReceiverAddress() {
        if (!(reciverAdress.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}+(,+[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})*$"))) {
            errorMail.setText("Invalid E-mail");
            validate = false;
        } else {
            errorMail.setText("");
            validate = true;
        }
    }

    // Metodo di validazione per l'oggetto
    @FXML
    protected void checkObject(){
        if (!(objectField.getText().isEmpty())) {
            errorObject.setText("");
        } else {
            errorObject.setText("Empty Field");
        }
    }

    // Metodo per aprire la finestra con i dettagli della mail
    private void openEmailDetailWindow(email email) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("email_detail_view.fxml"));
            Parent root = loader.load();
            DetailController controller = loader.getController();
            controller.setEmailDetails(email);
            controller.setEmailItems(emailItems);
            controller.setS(this.s);
            Stage stage = new Stage();
            stage.setTitle("Email Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo per settare le email nella ListView
    public void setEmails(List<email> emails) {
        emailItems = FXCollections.observableArrayList(emails);
        synchronized (this.emailItems) {
            emailItems.sort(Comparator.comparing(email::getDate).reversed());
        }
        this.emailListView.setItems(emailItems);


        // Aggiungi l'evento per aprire i dettagli della mail
        this.emailListView.setOnMouseClicked((event) -> {
            if (event.getClickCount() == 2) {
                email selectedEmail = this.emailListView.getSelectionModel().getSelectedItem();
                if (selectedEmail != null) {
                    this.openEmailDetailWindow(selectedEmail);
                }
            }
        });
    }

    public void onSendButtonClick() {

        if(validate) {
            String[] receivers_string = reciverAdress.getText().split(",");
            ArrayList<user> receivers = new ArrayList<>();
            for (String receiver : receivers_string) {
                receivers.add(new user(receiver));
                System.out.println(receiver);
            }
            System.out.println(this.user);

            email email = new email(this.user, receivers, objectField.getText(), emailContent.getText() );

            emailSender(email);

            reciverAdress.clear();
            objectField.clear();
            emailContent.clear();
        }

    }


    public void emailSender(email email){
        sendEmail sendEmail = new sendEmail(email, this.s);
        s.schedule(sendEmail, 0, TimeUnit.SECONDS);

    }




}