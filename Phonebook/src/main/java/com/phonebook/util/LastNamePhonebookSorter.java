package com.phonebook.util;

import com.phonebook.model.Contact;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;

public class LastNamePhonebookSorter implements IPhonebookSorter {
    @Override
    public void sort(ObservableList<Contact> phonebook) {
        Comparator<Contact> comparator = Comparator.comparing(Contact::getLastName);
        FXCollections.sort(phonebook, comparator);
    }
}
