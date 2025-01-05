package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import database.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Task;
import model.User;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ToDoFormController implements Initializable {
    @FXML
    public DatePicker startDatePicker;
    public JFXListView doneListView;
    @FXML
    private JFXButton btnAddToDo;

    @FXML
    private JFXButton btnLogout;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblUsername1;

    @FXML
    private Label lblUsername2;

    @FXML
    private Label lblUsername3;

    @FXML
    private JFXListView todoListView;

    @FXML
    private TextArea txtDescription;

    @FXML
    private TextField txtTask;
    private static User user;

    @FXML
    void btnAddToDoOnAction(ActionEvent event) {
        // Create a VBox to act as a card
        VBox vBox = new VBox();
        vBox.getStyleClass().add("vbox"); // Add the CSS class for styling

        // Add task label
        Label taskName = new Label("Task: " + txtTask.getText());
        taskName.getStyleClass().add("cart-task-label"); // Add CSS class for task label

        // Add description label
        Label description = new Label("Description: " + txtDescription.getText());
        description.getStyleClass().add("cart-description-label"); // Add CSS class for description label

        // Add date label
        Label date = new Label("Date: " + startDatePicker.getValue());
        date.getStyleClass().add("cart-date-label"); // Add CSS class for date label

        Task newTask = new Task(user.getUserid(),txtTask.getText(), Date.valueOf(startDatePicker.getValue()),txtDescription.getText());
        boolean isAddedToDb = addToTaskTable(newTask);
        if (isAddedToDb){
            System.out.println("task Added to db");
        }

        // Add a checkbox for completion status
        CheckBox checkBox = new CheckBox("Completed");
        checkBox.getStyleClass().add("cart-checkbox"); // Add CSS class for checkbox
        checkBox.setOnMouseClicked(mouseEvent -> {
            if (checkBox.isSelected()){
                doneListView.getItems().add(vBox);
                todoListView.getItems().remove(vBox);
            }
        });

        // Add all elements to the VBox
        vBox.getChildren().addAll(taskName, description, date, checkBox);

        // Add the VBox to the ListView
        todoListView.getItems().add(vBox);

        // Clear the input fields after adding
        txtTask.clear();
        txtDescription.clear();
        startDatePicker.setValue(null);
    }


    @FXML
    void btnLogoutOnAction(ActionEvent event) {

    }

    public static void setUser(User thisUser) {
        user = thisUser;
    }

    public boolean addToTaskTable(Task task){
        String query = "INSERT INTO tasks (userid,task,datecreated, discription) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = DBHandler.getInstance().getConnection().prepareStatement(query)) {
            statement.setInt(1,task.getUserid());
            statement.setString(2, task.getTask());
            statement.setDate(3, (Date) task.getDatecreated());
            statement.setString(4, task.getDescription());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error adding task", e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblUsername.setText(user.getUsername());
    }
}

