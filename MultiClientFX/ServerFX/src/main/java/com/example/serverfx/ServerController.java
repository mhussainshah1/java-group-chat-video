package com.example.serverfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerController {
    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private VBox vbox_messages;
    @FXML
    private Server server;

    @FXML
    private void initialize() throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        this.server = new Server(serverSocket, data -> {
            addLabel(data.toString(), vbox_messages);
        });

        vbox_messages.heightProperty().addListener((observable, oldValue, newValue) -> sp_main.setVvalue((Double) newValue));

        Platform.runLater(() -> {
            Stage stage = (Stage) button_send.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                server.closeServerSocket();
                Platform.exit();
                System.exit(0);
            });
        });

        Thread ConnectionThread = new Thread(() -> server.startServer());
        ConnectionThread.start();

        //server.receiveMessageFromClient();


    }

    public void addLabel(String messageFromClient, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235); " + "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);
        Platform.runLater(() -> vBox.getChildren().add(hBox));
    }

    @FXML
    private void btnHandler(ActionEvent actionEvent) {
        String messageToSend = tf_message.getText();
        if (!messageToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 5, 5, 10));

            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-color: rgb(239,242,255);" + " -fx-background-color: rgb(15,125,242);" + " -fx-background-radius: 20px;");
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, .945, .996));

            hBox.getChildren().add(textFlow);
            vbox_messages.getChildren().add(hBox);

//                server.sendMessageToClient(messageToSend);
            server.send("SERVER: " + messageToSend);
            tf_message.clear();
        }
    }

    @FXML
    private void txtHandler(ActionEvent actionEvent) {
        button_send.fire();
    }
}
