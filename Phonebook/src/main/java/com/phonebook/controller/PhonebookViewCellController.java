package com.phonebook.controller;

import com.phonebook.model.Contact;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

public class PhonebookViewCellController extends ListCell<Contact> {

    @FXML
    private Label nameLabel;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private AnchorPane anchorPane;

    private FXMLLoader loader;

    public PhonebookViewCellController() {
    }

    @Override
    protected void updateItem(Contact contact, boolean empty) {
        super.updateItem(contact, empty);

        if(empty || contact == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (loader == null) {
                loader = new FXMLLoader(Objects.requireNonNull(getClass().getClassLoader().
                        getResource("contact-list-view-cell.fxml")));
                loader.setController(this);

                try {
                    loader.load();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }

            nameLabel.setText(String.valueOf(contact.getFullName()));
            phoneNumberLabel.setText(String.valueOf(contact.getPhoneNumber()));

            setText(null);
            setGraphic(anchorPane);
        }
    }
}
