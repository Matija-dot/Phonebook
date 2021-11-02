package com.phonebook.util;

import com.phonebook.controller.SceneController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static DatabaseConnector instance = null;
    private static Connection connection;

    private DatabaseConnector() {

    }

    public static DatabaseConnector getInstance() {
        if(instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    public void connectDatabase() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/phonebook",
                    "root", "m0408bi");

            if (this.connection != null) {
                System.out.println("Connected to the database.");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException exception) {
            System.err.format("SQL State: %s\n%s", exception.getSQLState(), exception.getMessage());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }
}
