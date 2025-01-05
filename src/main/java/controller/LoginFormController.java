package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import database.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void btnSignInOnAction(ActionEvent actionEvent) throws IOException {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Incomplete username or password..!").show();
        }else {
            loginUser(username,password);
        }
    }

    private User authenticateUser(String username, String password) {
        try {
            ResultSet rst = DBHandler.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'");
            if (rst.next()){
                return new User(
                        rst.getInt("userid"),
                        rst.getString("username"),
                        rst.getString("password"),
                        rst.getString("gender")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void loginUser(String username, String password) throws IOException {
        User user = authenticateUser(username, password);
        if (user!=null){
            System.out.println("login Successfully");
            ToDoFormController.setUser(user);
            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/todo_form.fxml"))));
            stage.show();
            Stage currentStage = (Stage) txtUsername.getScene().getWindow();
            if (currentStage != null) {
                currentStage.close();
            }


        }else {
            new Alert(Alert.AlertType.ERROR,"User not found..!\nSign Up please.").show();
        }

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
