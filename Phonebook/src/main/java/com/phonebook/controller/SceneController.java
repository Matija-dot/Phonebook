package com.phonebook.controller;

import com.phonebook.util.DatabaseConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.phonebook.util.PhonebookUtil;
import com.phonebook.util.CloseUtil;

import java.io.IOException;
import java.util.Objects;

public class SceneController {
    private static final String DEFAULT_STYLE = "moonlight.css";
    private static SceneController instance = null;
    private static String stylePath;

    @FXML
    private static String style;

    protected SceneController() {

    }

    public static SceneController getInstance() {
        if(instance == null) {
            instance = new SceneController();
            SceneController.stylePath = DEFAULT_STYLE;
        }
        return instance;
    }

    public void changeStyle(String stylePath) {
        SceneController.stylePath = stylePath;
    }

    public void openPhonebook() throws IOException {
        Stage initialStage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getClassLoader()
                .getResource("phonebook-view.fxml")));

        Scene scene = new Scene(root);
        SceneController.style = Objects.requireNonNull(this.getClass().getClassLoader()
                .getResource(SceneController.stylePath)).toExternalForm();
        scene.getStylesheets().add(style);

        initialStage.initStyle(StageStyle.UNIFIED);
        initialStage.setResizable(false);
        initialStage.setScene(scene);
        initialStage.show();
        initialStage.setTitle("PHONEBOOK");
        PhonebookUtil.setStageIcon(initialStage);
        initialStage.setOnCloseRequest(event -> {
                    event.consume();
                    closePhonebook(initialStage);
                });
    }

    public void closePhonebook(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CLOSE");
        alert.setHeaderText("You are about to close the phonebook.");
        alert.setContentText("Confirm close?");
        CloseUtil.setAlertIcon(alert);

        if(alert.showAndWait().orElse(null) == ButtonType.OK) {
            DatabaseConnector.getInstance().disconnectDatabase();
            stage.close();
        }
    }

    public void switchToScene(ActionEvent event, String sceneName) throws IOException {
        Parent builder = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().
                getResource(sceneName)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(builder);
        SceneController.style = Objects.requireNonNull(getClass().getClassLoader().
                getResource(SceneController.stylePath)).toExternalForm();
        scene.getStylesheets().add(style);

        stage.setScene(scene);
        stage.show();
    }

    public void switchToPhonebookView(ActionEvent event) throws IOException {
        switchToScene(event, "phonebook-view.fxml");
    }

    public void switchToInputForm(ActionEvent event) throws IOException {
        switchToScene(event, "input-form.fxml");
    }

    public void switchToSettings(ActionEvent event) throws IOException {
        switchToScene(event, "settings.fxml");
    }
}
