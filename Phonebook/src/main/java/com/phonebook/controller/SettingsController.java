package com.phonebook.controller;

import com.phonebook.model.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends SceneController implements Initializable {

    @FXML
    private MenuButton styleMenuButton;

    @FXML
    private MenuButton sortMenuButton;

    @FXML
    protected ToggleGroup color;

    @FXML
    protected RadioMenuItem blackYellow;

    @FXML
    protected RadioMenuItem lilac;

    @FXML
    protected RadioMenuItem mintgreen;

    @FXML
    protected RadioMenuItem moonlight;

    @FXML
    protected RadioMenuItem ocean;

    @FXML
    protected ToggleGroup sort;

    @FXML
    protected RadioMenuItem firstNameSort;

    @FXML
    protected RadioMenuItem lastNameSort;

    @FXML
    protected RadioMenuItem phoneNumberSort;

    @FXML
    protected RadioMenuItem birthDateSort;

    public void selectBlackYellow() {
        SettingsSelector settingsSelector = SettingsSelector.getInstance();
        settingsSelector.setStyle(blackYellow);
        styleMenuButton.setText(settingsSelector.getStyle().getText());
        SceneController.getInstance().changeStyle("black&yellow.css");
    }

    public void selectLilac() {
        SettingsSelector settingsSelector = SettingsSelector.getInstance();
        settingsSelector.setStyle(lilac);
        styleMenuButton.setText(settingsSelector.getStyle().getText());
        SceneController.getInstance().changeStyle("lilac.css");
    }

    public void selectMintgreen() {
        SettingsSelector settingsSelector = SettingsSelector.getInstance();
        settingsSelector.setStyle(mintgreen);
        styleMenuButton.setText(settingsSelector.getStyle().getText());
        SceneController.getInstance().changeStyle("mintgreen.css");
    }

    public void selectMoonlight() {
        SettingsSelector settingsSelector = SettingsSelector.getInstance();
        settingsSelector.setStyle(moonlight);
        styleMenuButton.setText(settingsSelector.getStyle().getText());
        SceneController.getInstance().changeStyle("moonlight.css");
    }

    public void selectOcean() {
        SettingsSelector settingsSelector = SettingsSelector.getInstance();
        settingsSelector.setStyle(ocean);
        styleMenuButton.setText(settingsSelector.getStyle().getText());
        SceneController.getInstance().changeStyle("ocean.css");
    }

    public void selectFirstNameSort() {
        SettingsSelector settingsSelector = SettingsSelector.getInstance();
        settingsSelector.setSorter(firstNameSort);
        sortMenuButton.setText("Sorting by " + settingsSelector.getSorter().getText());
        IPhonebookSorter phonebookSorter = new FirstNamePhonebookSorter();
        PhonebookSorter.setPhonebookSorter(phonebookSorter);
    }

    public void selectLastNameSort() {
        SettingsSelector settingsSelector = SettingsSelector.getInstance();
        settingsSelector.setSorter(lastNameSort);
        sortMenuButton.setText("Sorting by " + settingsSelector.getSorter().getText());
        IPhonebookSorter phonebookSorter = new LastNamePhonebookSorter();
        PhonebookSorter.setPhonebookSorter(phonebookSorter);
    }

    public void selectPhoneNumberSort() {
        SettingsSelector settingsSelector = SettingsSelector.getInstance();
        settingsSelector.setSorter(phoneNumberSort);
        sortMenuButton.setText("Sorting by " + settingsSelector.getSorter().getText());
        IPhonebookSorter phonebookSorter = new PhoneNumberPhonebookSorter();
        PhonebookSorter.setPhonebookSorter(phonebookSorter);
    }

    public void selectBirthDateSort() {
        SettingsSelector settingsSelector = SettingsSelector.getInstance();
        settingsSelector.setSorter(birthDateSort);
        sortMenuButton.setText("Sorting by " + settingsSelector.getSorter().getText());
        IPhonebookSorter phonebookSorter = new BirthDatePhonebookSorter();
        PhonebookSorter.setPhonebookSorter(phonebookSorter);
    }

    public void preview(ActionEvent event) throws IOException {
        switchToSettings(event);
    }

    public void back(ActionEvent event) throws IOException {
        switchToPhonebookView(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SettingsSelector settingsSelector = SettingsSelector.getInstance(moonlight, lastNameSort);
        styleMenuButton.setText(settingsSelector.getStyle().getText());
        sortMenuButton.setText("Sorting by " + settingsSelector.getSorter().getText());
        color.selectToggle(settingsSelector.getStyle());
        sort.selectToggle(settingsSelector.getSorter());
    }
}
