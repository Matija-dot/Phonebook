package com.phonebook.util;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PhonebookUtil {
    private static final String IMAGE_PATH = "file:src/icons/phonebook-active.png";

    public static void setStageIcon(Stage stage) {
        stage.getIcons().add(new Image(IMAGE_PATH));
    }

    public static void setAlertIcon(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(IMAGE_PATH));
    }
}