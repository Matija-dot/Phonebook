package com.phonebook.model;

import com.phonebook.model.Contact;

import javafx.collections.ObservableList;

public interface IPhonebookSorter {
    void sort(ObservableList<Contact> phonebook);
}
