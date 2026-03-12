module org.example.progettouid {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
    requires java.net.http;

    requires spring.security.crypto;
    requires org.xerial.sqlitejdbc;
    requires com.google.gson;
    requires com.github.librepdf.openpdf;
    requires spring.security.core;
    requires javafx.base;

    opens org.example.progettouid to javafx.fxml;
    opens org.example.progettouid.controller to javafx.fxml;
    opens org.example.progettouid.model to javafx.fxml;

    exports org.example.progettouid;
    exports org.example.progettouid.controller;
    exports org.example.progettouid.model;
}
