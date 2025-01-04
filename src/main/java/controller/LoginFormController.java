package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginFormController implements Initializable {

    @FXML
    private JFXButton btnSignIn;

    @FXML
    private JFXButton btnSignUp;

    @FXML
    private Label lblForgotPassword;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXTextField txtUsername;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void btnSignInOnAction(ActionEvent actionEvent) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (!username.equals("") && !password.equals("")){
            loginUser(username, password);
        }else {
            new Alert(Alert.AlertType.ERROR,"Incomplete username or password..!");
        }
    }

    private void loginUser(String username, String password) {
        System.out.println("login Successfully");
    }

    public void btnSignUpOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/register_form.fxml"))));
        stage.show();
        Stage currentStage = (Stage) txtUsername.getScene().getWindow();
        if (currentStage != null) {
            currentStage.close();
        }
    }
}
