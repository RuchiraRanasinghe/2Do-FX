package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import database.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterFormController implements Initializable {

    @FXML
    private JFXButton btnSignIn;

    @FXML
    private JFXButton btnSignUp;

    @FXML
    private JFXCheckBox comboBoxFemale;

    @FXML
    private JFXCheckBox comboBoxMale;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXTextField txtUsername;


    public void btnSignUpOnAction(ActionEvent actionEvent) throws SQLException {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String gender;
        if (comboBoxMale.isSelected()){
            gender="male";
        }else {
            gender="female";
        }
        User user = new User(0,username,password,gender);
        signUpUser(user);
    }

    private void signUpUser(User user) throws SQLException {
        PreparedStatement preparedStatement = DBHandler.getInstance().getConnection().prepareStatement("INSERT INTO users(username,password,gender) VALUES(?,?,?)");
        preparedStatement.setString(1,user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getGender());
        preparedStatement.executeUpdate();
        System.out.println("Successfully Registered");
    }

    public void btnSignInOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login_form.fxml"))));
        stage.show();
        Stage currentStage = (Stage) txtUsername.getScene().getWindow();
        if (currentStage != null) {
            currentStage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
