package com.phonebook.model;

import com.phonebook.controller.SettingsController;
import javafx.scene.control.RadioMenuItem;

public class SettingsSelector extends SettingsController {
    private static SettingsSelector instance = null;
    private RadioMenuItem style;
    private RadioMenuItem sorter;

    private SettingsSelector(RadioMenuItem style, RadioMenuItem sorter) {
        this.style = style;
        this.sorter = sorter;
    }

    public static SettingsSelector getInstance(RadioMenuItem style, RadioMenuItem sorter) {
        if(instance == null) {
            instance = new SettingsSelector(style, sorter);
        }
        return instance;
    }

    public static SettingsSelector getInstance() {
        return instance;
    }

    public RadioMenuItem getStyle() {
        return this.style;
    }

    public RadioMenuItem getSorter() {
        return this.sorter;
    }

    public void setStyle(RadioMenuItem style) {
        this.style = style;
    }

    public void setSorter(RadioMenuItem sorter) {
        this.sorter = sorter;
    }
}
