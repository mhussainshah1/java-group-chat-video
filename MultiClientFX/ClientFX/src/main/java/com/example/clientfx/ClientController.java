package com.example.clientfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class ClientController {
    @FXML
    private ListView<Label> userView;
    @FXML
    private Button button_send;
    @FXML
    private TextField tf_message;
    @FXML
    private VBox vbox_messages;
    @FXML
    private ScrollPane sp_main;
    private Client client;
    private String userName;

    @FXML
    private void initialize() {
        TextInputDialog login = new TextInputDialog();
        login.setTitle("User Name");
        Optional<String> optionalString = login.showAndWait();
        this.userName = optionalString.get();

        userView.getItems().add(new Label(userName));
        try {
            client = new Client(new Socket("localhost", 1234), userName, data -> {
                addLabel(data.toString(), vbox_messages);
            });
            System.out.println("Connected to server");
        } catch (IOException e) {
            e.printStackTrace();
        }

//        client.receiveMessageFromServer();
        client.listenForMessage();
        client.sendMessage(userName);

        vbox_messages.heightProperty().addListener(
                (observable, oldValue, newValue) -> sp_main.setVvalue((Double) newValue)
        );

        Platform.runLater(() -> {
            Stage stage = (Stage) button_send.getScene().getWindow();
            stage.setTitle(userName);
            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
        });
    }

    public void addLabel(String msgFromServer, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(msgFromServer);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("""
                -fx-background-color: rgb(233,233,235);
                -fx-background-radius: 20px;
                """);
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);
        Platform.runLater(() -> vBox.getChildren().add(hBox));
    }

    @FXML
    private void txtHandler(ActionEvent actionEvent) {
        button_send.fire();
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
            textFlow.setStyle("""
                     -fx-color: rgb(239,242,255);
                     -fx-background-color: rgb(15,125,242);
                     -fx-background-radius: 20px;
                    """
            );
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0.934, .945, .996));

            hBox.getChildren().add(textFlow);
            vbox_messages.getChildren().add(hBox);

//                client.sendMessageToServer(messageToSend);
            client.sendMessage(userName + ": " + messageToSend);
            tf_message.clear();
        }
    }
}