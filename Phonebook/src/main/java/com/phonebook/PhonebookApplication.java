package com.phonebook;

import com.phonebook.controller.SceneController;
import com.phonebook.util.DatabaseConnector;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class PhonebookApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException{
        DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
        databaseConnector.connectDatabase();
        SceneController sceneController = SceneController.getInstance();
        sceneController.openPhonebook();
    }

    public static void main(String[] args) {
        launch(args);
    }
}