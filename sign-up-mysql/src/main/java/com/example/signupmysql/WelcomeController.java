package com.example.signupmysql;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {
    @FXML
    private Label label_welcome;
    @FXML
    private Label label_fav_channel;
    @FXML
    private Button button_logout;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setUserInformation(String username, String favChannel) {
        label_welcome.setText("Welcome " + username + "!");
        label_fav_channel.setText("Your favorite YouTube Channel is " + favChannel + "!");
    }

    @FXML
    private void btnHandler(ActionEvent event) {
        DBUtils.changeScene(event, "login.fxml", "Log in!", null, null);
    }
}
