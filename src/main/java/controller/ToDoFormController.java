package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import database.DBHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.DoneTask;
import model.Task;
import model.TodoList;
import model.User;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

        Task newTask = new Task(user.getUserid(),txtTask.getText(), startDatePicker.getValue().toString(),txtDescription.getText());
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
                boolean isAddedToDoneTasks = addToDoneTasks(taskName.getText(), Date.valueOf(date.getText()),description.getText());
                if (isAddedToDoneTasks){
                    if (deleteCompletedTask(taskName.getText())){
                        System.out.println("Successfully deleted from tasks");
                    }
                }
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
            statement.setString(3, task.getDatecreated());
            statement.setString(4, task.getDescription());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error adding task", e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblUsername.setText(user.getUsername());
        loadTodoList();
        loadDoneTaskList();
    }

    public void loadTodoList(){
        ArrayList<TodoList> todoListArrayList = getDataFromTasksTable();
        todoListArrayList.forEach(todoList -> {
            // Create a VBox to act as a card
            VBox vBox = new VBox();
            vBox.getStyleClass().add("vbox"); // Add the CSS class for styling

            // Add task label
            Label taskName = new Label("Task: " + todoList.getTask());
            taskName.getStyleClass().add("cart-task-label"); // Add CSS class for task label

            // Add description label
            Label description = new Label("Description: " + todoList.getDiscription());
            description.getStyleClass().add("cart-description-label"); // Add CSS class for description label

            // Add date label
            Label date = new Label("Date: " + todoList.getDatecreated().toString());
            date.getStyleClass().add("cart-date-label"); // Add CSS class for date label

            // Add a checkbox for completion status
            CheckBox checkBox = new CheckBox("Completed");
            checkBox.getStyleClass().add("cart-checkbox"); // Add CSS class for checkbox
            checkBox.setOnMouseClicked(mouseEvent -> {
                if (checkBox.isSelected()){
                    doneListView.getItems().add(vBox);
                    todoListView.getItems().remove(vBox);
                    boolean isAddedToDoneTasks = addToDoneTasks(todoList.getTask(), (Date) todoList.getDatecreated(), todoList.getDiscription());
                    if (isAddedToDoneTasks){
                        if (deleteCompletedTask(taskName.getText())){
                            System.out.println("Successfully deleted from tasks");
                        }
                    }
                }
            });

            // Add all elements to the VBox
            vBox.getChildren().addAll(taskName, description, date, checkBox);

            // Add the VBox to the ListView
            todoListView.getItems().add(vBox);
        });
    }

    public ArrayList<TodoList> getDataFromTasksTable() {
        ArrayList<TodoList> todoListArrayList = new ArrayList<>();
        try {
            ResultSet rst = DBHandler.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM tasks WHERE userid='" + user.getUserid() + "'");
            while (rst.next()) {
                todoListArrayList.add(new TodoList(rst.getInt(1), rst.getInt(2), rst.getString(3),rst.getDate(4), rst.getString(5)));
            }
            return todoListArrayList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteCompletedTask(String taskName) {
        System.out.println(taskName);
        try {
            if(DBHandler.getInstance().getConnection().createStatement().executeUpdate("DELETE FROM tasks WHERE task='"+taskName+"'")>0){
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addToDoneTasks(String taskName, Date date, String discription) {
        try {
            ResultSet rst = DBHandler.getInstance().getConnection().createStatement().executeQuery("SELECT userid FROM tasks WHERE task='" + taskName + "'");
            if (rst.next()) {
                PreparedStatement prepareStm = DBHandler.getInstance().getConnection().prepareStatement("INSERT INTO donetasks(userid,task,datecreated,discription) VALUES(?,?,?,?)");
                prepareStm.setString(1, rst.getString("userid"));
                prepareStm.setString(2, taskName);
                prepareStm.setDate(3, date);
                prepareStm.setString(4, discription);
                return prepareStm.executeUpdate() > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<DoneTask> getDataFromDoneTasks() {
        ArrayList<DoneTask>completedTaskArrayList = new ArrayList<>();
        try {
            ResultSet rst = DBHandler.getInstance().getConnection().createStatement().executeQuery("SELECT userid,task,datecreated,discription FROM donetasks WHERE userid='" + user.getUserid() + "'");
            while (rst.next()){
                completedTaskArrayList.add(new DoneTask(rst.getInt(1), rst.getString(2), rst.getString(3),rst.getString(4)));
            }
            return completedTaskArrayList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadDoneTaskList(){
        ArrayList<DoneTask> todoListArrayList = getDataFromDoneTasks();
        todoListArrayList.forEach(todoList -> {
            // Create a VBox to act as a card
            VBox vBox = new VBox();
            vBox.getStyleClass().add("vbox"); // Add the CSS class for styling

            // Add task label
            Label taskName = new Label("Task: " + todoList.getTask());
            taskName.getStyleClass().add("cart-task-label"); // Add CSS class for task label

            // Add description label
            Label description = new Label("Description: " + todoList.getDiscription());
            description.getStyleClass().add("cart-description-label"); // Add CSS class for description label

            // Add date label
            Label date = new Label("Date: " + todoList.getDatecreated().toString());
            date.getStyleClass().add("cart-date-label"); // Add CSS class for date label

            // Add a checkbox for completion status
            CheckBox checkBox = new CheckBox("Completed");
            checkBox.getStyleClass().add("cart-checkbox"); // Add CSS class for checkbox
            checkBox.setOnMouseClicked(mouseEvent -> {
                if (checkBox.isSelected()){
                    doneListView.getItems().add(vBox);
                    todoListView.getItems().remove(vBox);
                    boolean isAddedToDoneTasks = addToDoneTasks(todoList.getTask(), Date.valueOf(todoList.getDatecreated()), todoList.getDiscription());
                    if (isAddedToDoneTasks){
                        if (deleteCompletedTask(taskName.getText())){
                            System.out.println("Successfully deleted from tasks");
                        }
                    }
                }
            });

            // Add all elements to the VBox
            vBox.getChildren().addAll(taskName, description, date, checkBox);

            // Add the VBox to the ListView
            doneListView.getItems().add(vBox);
        });
    }
}

