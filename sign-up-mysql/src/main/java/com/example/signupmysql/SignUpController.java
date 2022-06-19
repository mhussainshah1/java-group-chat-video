package com.example.signupmysql;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    @FXML
    private Button button_log_in;
    @FXML
    private Button button_signup;
    @FXML
    private RadioButton rb_wittcode;
    @FXML
    private RadioButton rb_someoneelse;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    @FXML
    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
  /*
        ToggleGroup toggleGroup = new ToggleGroup();
        rb_wittcode.setToggleGroup(toggleGroup);
        rb_someoneelse.setToggleGroup(toggleGroup);
        rb_wittcode.setSelected(true);*/
    }

    public void btnHandler(ActionEvent event) {
        Button source = (Button) event.getSource();
        String id = source.getId();

        String toggleName = ((RadioButton) toggleGroup.getSelectedToggle()).getText();

        if (id.equals("button_signup")) {
            if (!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty()) {
                DBUtils.signUpUser(event, tf_username.getText(), tf_password.getText(), toggleName);
            } else {
                System.out.println("Please fill in all information");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please fill in all information to sign up!");
                alert.show();
            }
        } else if (id.equals("button_log_in")) {
            DBUtils.changeScene(event, "login.fxml", "Log in!", null, null);
        }
    }

    public void txtHandler(ActionEvent event) {
        button_signup.fire();
    }
}
