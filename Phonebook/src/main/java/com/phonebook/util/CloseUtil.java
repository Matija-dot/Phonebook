package com.phonebook.util;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CloseUtil {
    private static final String IMAGE_PATH = "file:src/icons/phonebook-close.png";

    public static void setAlertIcon(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(IMAGE_PATH));
    }
}
