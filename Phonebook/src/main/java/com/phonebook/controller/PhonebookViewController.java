package com.phonebook.controller;

import com.phonebook.model.Contact;
import com.phonebook.model.Validation;
import com.phonebook.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class PhonebookViewController extends SceneController implements Initializable {

    @FXML
    private ListView<Contact> listView;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField birthDateField;

    @FXML
    private TextField streetAddressField;

    @FXML
    private TextField townField;

    @FXML
    private Button editButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button applyButton;

    private final ObservableList<Contact> phonebook;
    private Contact selectedContact;

    public PhonebookViewController() {
        this.phonebook = FXCollections.observableArrayList();

        String query = "SELECT * FROM contact";
        try {
            Statement statement =
                    Objects.requireNonNull(DatabaseConnector.getInstance().getConnection()).createStatement();
            ResultSet queryResponse = statement.executeQuery(query);

            while(queryResponse.next()) {
                this.phonebook.add(new Contact(
                        queryResponse.getInt("contactID"),
                        queryResponse.getString("phoneNumber"),
                        queryResponse.getString("firstName"),
                        queryResponse.getString("lastName"),
                        queryResponse.getString("streetAddress"),
                        queryResponse.getString("town"),
                        convertDateToLocalDate(queryResponse.getDate("birthDate"))));
            }
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    private LocalDate convertDateToLocalDate(Date dateToConvert) {
        if(dateToConvert == null) {
            return null;
        } else {
            return Instant.ofEpochMilli(dateToConvert.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstNameField.setEditable(false);
        lastNameField.setEditable(false);
        phoneNumberField.setEditable(false);
        streetAddressField.setEditable(false);
        townField.setEditable(false);
        editButton.setVisible(false);
        removeButton.setVisible(false);
        applyButton.setVisible(false);

        PhonebookSorter.getInstance().getPhonebookSorter().sort(this.phonebook);
        listView.setItems(this.phonebook);
        listView.setCellFactory(contactListView -> new PhonebookViewCell());

        listView.getSelectionModel().selectedItemProperty().addListener((observableValue, contact, t1) -> {
                editButton.setVisible(true);
                removeButton.setVisible(true);

                selectedContact = listView.getSelectionModel().getSelectedItem();
                showDetails(selectedContact);
        });
    }

    private void showDetails(Contact contact)
    {
        firstNameField.setText(contact.getFirstName());
        lastNameField.setText(contact.getLastName());
        phoneNumberField.setText(String.valueOf(contact.getPhoneNumber()));

        LocalDate birthDate = contact.getBirthDate();
        String formattedBirthDate;
        if(birthDate == null) {
            formattedBirthDate = "Date of birth unknown";
        } else {
            formattedBirthDate = birthDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
        }
        birthDateField.setText(formattedBirthDate);

        if(selectedContact.getStreetAddress().equals(""))
        {
            streetAddressField.setText("Street address unknown");
        } else {
            streetAddressField.setText(contact.getStreetAddress());
        }

        if(selectedContact.getTown().equals(""))
        {
            townField.setText("Town unknown");
        } else {
            townField.setText(contact.getTown());
        }
    }

    public void editContact() {
        firstNameField.setEditable(true);
        lastNameField.setEditable(true);
        phoneNumberField.setEditable(true);
        streetAddressField.setEditable(true);
        townField.setEditable(true);
        editButton.setVisible(false);
        applyButton.setVisible(true);
    }

    public void applyChanges(ActionEvent event) {
        selectedContact = listView.getSelectionModel().getSelectedItem();
        FormValidator formValidator = new FormValidator();
        Validation validation = formValidator.Validate(phoneNumberField.getText(), firstNameField.getText(),
                lastNameField.getText(), streetAddressField.getText(), townField.getText(), selectedContact.getBirthDate());

        if(validation.isSuccessful()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("APPLY");
            alert.setHeaderText("You are about apply changes to the selected contact.");
            alert.setContentText("Confirm changes?");
            PhonebookUtil.setAlertIcon(alert);

            if (alert.showAndWait().get() == ButtonType.OK) {
                String update =
                        "UPDATE contact " +
                                "SET phoneNumber = ?, firstName = ?, lastName = ?, streetAddress = ?, town = ? " +
                                "WHERE contactID = ?";
                try {
                    PreparedStatement preparedStatement =
                            Objects.requireNonNull(DatabaseConnector.getInstance().getConnection()).prepareStatement(update);

                    preparedStatement.setString(1, phoneNumberField.getText());
                    preparedStatement.setString(2, firstNameField.getText());
                    preparedStatement.setString(3, lastNameField.getText());
                    preparedStatement.setString(4, streetAddressField.getText());
                    preparedStatement.setString(5, townField.getText());
                    preparedStatement.setInt(6, selectedContact.getContactID());

                    preparedStatement.executeUpdate();
                    preparedStatement.close();

                    updateContact(selectedContact);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        } else {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            int toastMsgTime = 3500;
            int fadeInTime = 500;
            int fadeOutTime= 500;
            Toast.makeText(stage, validation.getReportMessage(), toastMsgTime, fadeInTime, fadeOutTime);
        }

        firstNameField.setEditable(false);
        lastNameField.setEditable(false);
        phoneNumberField.setEditable(false);
        streetAddressField.setEditable(false);
        townField.setEditable(false);
        editButton.setVisible(true);
        applyButton.setVisible(false);
        showDetails(selectedContact);
    }

    private void updateContact(Contact contact)
    {
        contact.setPhoneNumber(phoneNumberField.getText());
        contact.setFirstName(firstNameField.getText());
        contact.setLastName(lastNameField.getText());
        contact.setStreetAddress(streetAddressField.getText());
        contact.setTown(townField.getText());
    }

    public void removeContact() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("REMOVE");
        alert.setHeaderText("You are about to remove the selected contact.");
        alert.setContentText("Confirm removal?");
        PhonebookUtil.setAlertIcon(alert);

        if(alert.showAndWait().get() == ButtonType.OK) {
            String delete = "DELETE FROM contact WHERE contactID = ?";

            try {
                Contact selectedForRemoval = listView.getSelectionModel().getSelectedItem();

                PreparedStatement preparedStatement =
                        Objects.requireNonNull(DatabaseConnector.getInstance().getConnection()).prepareStatement(delete);
                preparedStatement.setInt(1, selectedForRemoval.getContactID());
                preparedStatement.executeUpdate();
                preparedStatement.close();

                phonebook.remove(selectedForRemoval);
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}