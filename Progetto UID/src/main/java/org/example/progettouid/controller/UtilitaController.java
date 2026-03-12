package org.example.progettouid.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UtilitaController {

    private void loadview(String fxmlPath, String titolo) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(titolo);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void apriFattura(){
        loadview("/org/example/progettouid/UtilitaFatture.fxml", "Emissione Fatture");
    }

    @FXML
    private void apriCalendario(){
        loadview("/org/example/progettouid/UtilitaCalendario.fxml", "Calendario");
    }
}

