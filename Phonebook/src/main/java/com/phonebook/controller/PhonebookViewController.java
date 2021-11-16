package com.phonebook.controller;

import com.phonebook.model.Contact;
import com.phonebook.model.FormValidator;
import com.phonebook.model.PhonebookSorter;
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
import java.util.Locale;
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
    private Button applyButton;

    @FXML
    private Button removeButton;

    @FXML
    private Label searchLabel;

    @FXML
    private Label resetLabel;

    @FXML
    private Label nameSearchLabel;

    @FXML
    private TextField nameSearchField;

    @FXML
    private Label phoneNumberSearchLabel;

    @FXML
    private TextField phoneNumberSearchField;

    private final ObservableList<Contact> phonebook;
    private final ObservableList<Contact> phonebookCopy;
    private Contact selectedContact;

    public PhonebookViewController() {
        phonebook = FXCollections.observableArrayList();

        String query = "SELECT * FROM phonebook.contact";
        try {
            Statement statement =
                    Objects.requireNonNull(DatabaseConnector.getInstance().getConnection()).createStatement();
            ResultSet queryResponse = statement.executeQuery(query);

            while(queryResponse.next()) {
                phonebook.add(new Contact(
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

        phonebookCopy = FXCollections.observableArrayList();
        phonebookCopy.addAll(phonebook);
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
        setEditAccess();

        PhonebookSorter.getInstance().getPhonebookSorter().sort(phonebook);
        listView.setItems(phonebook);
        listView.setCellFactory(contactListView -> new PhonebookViewCellController());

        listView.getSelectionModel().selectedItemProperty().addListener((observableValue, contact, t1) -> {
                editButton.setVisible(true);
                removeButton.setVisible(true);

                selectedContact = listView.getSelectionModel().getSelectedItem();
                showDetails(selectedContact);
        });
    }

    private void showDetails(Contact contact)
    {
        if(contact != null) {
            firstNameField.setText(contact.getFirstName());
            lastNameField.setText(contact.getLastName());
            phoneNumberField.setText(String.valueOf(contact.getPhoneNumber()));

            LocalDate birthDate = contact.getBirthDate();
            String formattedBirthDate;
            if (birthDate == null) {
                formattedBirthDate = "N/A";
            } else {
                formattedBirthDate = birthDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
            }
            birthDateField.setText(formattedBirthDate);

            if (selectedContact.getStreetAddress().equals("")) {
                streetAddressField.setText("N/A");
            } else {
                streetAddressField.setText(contact.getStreetAddress());
            }

            if (selectedContact.getTown().equals("")) {
                townField.setText("N/A");
            } else {
                townField.setText(contact.getTown());
            }
        }
    }

    private void setEditAccess() {
        firstNameField.setEditable(false);
        lastNameField.setEditable(false);
        phoneNumberField.setEditable(false);
        streetAddressField.setEditable(false);
        townField.setEditable(false);
        editButton.setVisible(false);
        applyButton.setVisible(false);
        removeButton.setVisible(false);
    }

    public void switchEditAccess() {
        firstNameField.setEditable(!firstNameField.isEditable());
        lastNameField.setEditable(!lastNameField.isEditable());
        phoneNumberField.setEditable(!phoneNumberField.isEditable());
        streetAddressField.setEditable(!streetAddressField.isEditable());
        townField.setEditable(!townField.isEditable());
        editButton.setVisible(!editButton.isVisible());
        applyButton.setVisible(!applyButton.isVisible());
    }

    public void applyChanges(ActionEvent event) {
        selectedContact = listView.getSelectionModel().getSelectedItem();
        if(selectedContact != null) {
            FormValidator formValidator = new FormValidator();
            Validation validation = formValidator.Validate(phoneNumberField.getText(), firstNameField.getText(),
                    lastNameField.getText(), streetAddressField.getText(), townField.getText(), selectedContact.getBirthDate());

            if (validation.isSuccessful()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("APPLY");
                alert.setHeaderText("You are about apply changes to the selected contact.");
                alert.setContentText("Confirm changes?");
                PhonebookUtil.setAlertIcon(alert);

                if (alert.showAndWait().orElse(null) == ButtonType.OK) {
                    String update = "UPDATE phonebook.contact " +
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
                        PhonebookSorter.getInstance().getPhonebookSorter().sort(this.phonebook);
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
            } else {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                int toastMsgTime = 3500;
                int fadeInTime = 500;
                int fadeOutTime = 500;
                Toast.makeText(stage, validation.getReportMessage(), toastMsgTime, fadeInTime, fadeOutTime);
            }
            showDetails(selectedContact);
        }
        switchEditAccess();
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

        if(alert.showAndWait().orElse(null) == ButtonType.OK) {
            String delete = "DELETE FROM phonebook.contact WHERE contactID = ?";

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

    public void searchPhonebook() {
        reset();
        switchSearchVisibility();

        ObservableList<Contact> foundContacts = FXCollections.observableArrayList();

        nameSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            hideEdit();
            if(!newValue.equals("")) {
                phoneNumberSearchField.setText("");
            }
            listView.getItems().clear();

            for(Contact contact : phonebookCopy)
            {
                boolean nameMatches = contact.getFullName().replace(",", "").trim().toLowerCase(Locale.ROOT).contains(newValue.replace(",","").trim().toLowerCase(Locale.ROOT));
                if(nameMatches) {
                    foundContacts.add(contact);
                }
            }

            if(foundContacts.isEmpty()) {
                foundContacts.addAll(phonebookCopy);
            }

            listView.setItems(foundContacts);
        });

        phoneNumberSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            hideEdit();
            if(!newValue.equals("")) {
                nameSearchField.setText("");
            }
            listView.getItems().clear();

            for(Contact contact : phonebookCopy)
            {
                boolean phoneNumberMatches = contact.getPhoneNumber().replace("+385","0").replaceAll("[^0-9]", "").contains(newValue.replace("+385","0").replaceAll("[^0-9]", ""));
                if(phoneNumberMatches) {
                    foundContacts.add(contact);
                }
            }

            if(foundContacts.isEmpty()) {
                foundContacts.addAll(phonebookCopy);
            }

            listView.setItems(foundContacts);
        });
    }

    private void switchSearchVisibility() {
        searchLabel.setVisible(!searchLabel.isVisible());
        resetLabel.setVisible(!resetLabel.isVisible());
        nameSearchLabel.setVisible(!nameSearchLabel.isVisible());
        nameSearchField.setVisible(!nameSearchField.isVisible());
        phoneNumberSearchLabel.setVisible(!phoneNumberSearchLabel.isVisible());
        phoneNumberSearchField.setVisible(!phoneNumberSearchField.isVisible());
    }

    private void reset() {
        listView.getItems().clear();
        phonebook.addAll(phonebookCopy);
        listView.setItems(phonebook);
        nameSearchField.setText("");
        phoneNumberSearchField.setText("");
    }

    private void hideEdit() {
        editButton.setVisible(false);
        applyButton.setVisible(false);
        removeButton.setVisible(false);
    }
}