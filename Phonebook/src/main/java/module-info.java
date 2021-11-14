module com.example.phonebook {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.phonebook to javafx.fxml;
    exports com.phonebook;
    exports com.phonebook.model;
    opens com.phonebook.model to javafx.fxml;
    exports com.phonebook.controller;
    opens com.phonebook.controller to javafx.fxml;
}