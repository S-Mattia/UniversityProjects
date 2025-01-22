package com.example.server;

import com.example.server.support.server;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;




public class ServerController {
    @FXML
    public Button cleanButton;
    public Button startButton;
    public TextArea log;
    public Button stopButton;

    private com.example.server.support.server server;

    @FXML
    public void onCleanButton() { log.setText("");
    }

    @FXML
    public void onStartButton() {
        if(server == null || !server.isAlive()){
            server = new server(6789,5,log);
            server.start();
            startButton.disableProperty().setValue(true);
            stopButton.disableProperty().setValue(false);
            System.out.println("server started");
            log.appendText("Server started\n");
        }

    }

    @FXML
    public void onStopButton() {

        if(server != null && server.isAlive()){
            server.close();
            try {
                server.join();
            } catch (InterruptedException e) {
                System.err.println("an error occured while waiting for serverThread to stop...");
            }
            System.out.println("server stopped\n");
            stopButton.disableProperty().setValue(true);
            startButton.disableProperty().setValue(false);

        }

    }
}
