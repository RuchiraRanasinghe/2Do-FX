package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHandler {
    private static DBHandler instance;
    private Connection connection;

    private DBHandler() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/todo","root","1234");
    }

    public static DBHandler getInstance() throws SQLException {
        return instance == null?instance=new DBHandler():instance;
    }

    public Connection getConnection(){
        return connection;
    }
}
