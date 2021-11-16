package com.phonebook.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import com.phonebook.util.DatabaseConnector;
import com.phonebook.model.FormValidator;
import com.phonebook.util.PhonebookUtil;
import com.phonebook.util.UniqueIDGenerator;
import com.phonebook.model.Validation;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class InputFormController extends SceneController {

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField streetAddressField;

    @FXML
    private TextField townField;

    @FXML
    private DatePicker birthDatePicker;

    public void saveContact(ActionEvent event) throws IOException {
        FormValidator formValidator = new FormValidator();
        Validation validation = formValidator.Validate(phoneNumberField.getText(), firstNameField.getText(),
                lastNameField.getText(), streetAddressField.getText(), townField.getText(), birthDatePicker.getValue());

        if(validation.isSuccessful()) {
            String query = "SELECT * FROM phonebook.contact";
            ObservableList<Integer> existingIDs = FXCollections.observableArrayList();
            try {
                Statement statement =
                        Objects.requireNonNull(DatabaseConnector.getInstance().getConnection()).createStatement();
                ResultSet queryResponse = statement.executeQuery(query);

                while(queryResponse.next()) {
                    existingIDs.add(queryResponse.getInt("contactID"));
                }
            } catch(Exception exception) {
                exception.printStackTrace();
            }

            String insert =
                    "INSERT INTO phonebook.contact (phoneNumber, firstName, lastName, streetAddress, town, birthDate, contactID) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement preparedStatement = DatabaseConnector.getInstance().getConnection().prepareStatement(insert);

                preparedStatement.setString(1, phoneNumberField.getText());
                preparedStatement.setString(2, firstNameField.getText());
                preparedStatement.setString(3, lastNameField.getText());
                preparedStatement.setString(4, streetAddressField.getText());
                preparedStatement.setString(5, townField.getText());
                if(birthDatePicker.getValue() == null) {
                    preparedStatement.setDate(6, null);
                } else {
                    preparedStatement.setDate(6, java.sql.Date.valueOf(birthDatePicker.getValue()));
                }
                preparedStatement.setInt(7, UniqueIDGenerator.getInstance().generateNew(existingIDs));

                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch(Exception exception) {
                exception.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("SAVE");
            alert.setHeaderText(validation.getReportMessage() + " Contact added to phonebook!");
            alert.setContentText("Would you like to enter an additional contact?");
            PhonebookUtil.setAlertIcon(alert);

            if(alert.showAndWait().orElse(null) == ButtonType.OK) {
                switchToInputForm(event);
            } else {
                switchToPhonebookView(event);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("SAVE");
            alert.setHeaderText(validation.getReportMessage());
            alert.setContentText("Would you like to fix the input?");
            PhonebookUtil.setAlertIcon(alert);

            if(alert.showAndWait().orElse(null) != ButtonType.OK) {
                switchToPhonebookView(event);
            }
        }
    }
}
