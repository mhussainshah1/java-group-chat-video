package com.example.signupmysql;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    @FXML
    private Button button_login;
    @FXML
    private Button button_sign_up;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void btnHandler(ActionEvent event) {
        Button source = (Button) event.getSource();
        String id = source.getId();

        if (id.equals("button_login")) {
            DBUtils.logInUser(event, tf_username.getText(), tf_password.getText());
        } else if (id.equals("button_sign_up")) {
            DBUtils.changeScene(event, "sign-up.fxml","Sign Up!", null, null);
        }
    }

    @FXML
    private void txtHandler(ActionEvent event) {
        button_login.fire();
    }
}