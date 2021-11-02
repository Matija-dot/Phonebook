package com.phonebook.controller;

import com.phonebook.util.*;

import javafx.event.ActionEvent;

import java.io.IOException;

public class SettingsController extends SceneController {

    public void selectBlackYellow() {
        SceneController.getInstance().changeStyle("black&yellow.css");
    }

    public void selectLilac() {
        SceneController.getInstance().changeStyle("lilac.css");
    }

    public void selectMintgreen() {
        SceneController.getInstance().changeStyle("mintgreen.css");
    }

    public void selectMoonlight() {
        SceneController.getInstance().changeStyle("moonlight.css");
    }

    public void selectOcean() {
        SceneController.getInstance().changeStyle("ocean.css");
    }

    public void selectFirstNameSort() {
        IPhonebookSorter phonebookSorter = new FirstNamePhonebookSorter();
        PhonebookSorter.setPhonebookSorter(phonebookSorter);
    }

    public void selectLastNameSort() {
        IPhonebookSorter phonebookSorter = new LastNamePhonebookSorter();
        PhonebookSorter.setPhonebookSorter(phonebookSorter);
    }

    public void selectPhoneNumberSort() {
        IPhonebookSorter phonebookSorter = new PhoneNumberPhonebookSorter();
        PhonebookSorter.setPhonebookSorter(phonebookSorter);
    }

    public void apply(ActionEvent event) throws IOException {
        switchToSettings(event);
    }
}
