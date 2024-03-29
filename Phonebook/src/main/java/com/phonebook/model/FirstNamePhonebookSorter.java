package com.phonebook.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;

public class FirstNamePhonebookSorter implements IPhonebookSorter {
    @Override
    public void sort(ObservableList<Contact> phonebook) {
        Comparator<Contact> comparator = Comparator.comparing(Contact::getFirstName);
        FXCollections.sort(phonebook, comparator);
    }
}
