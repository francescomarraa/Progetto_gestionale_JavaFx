package org.example.progettouid.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StatisticheController {
    public void apriStatisticheProdotti(ActionEvent e) {
        caricaFinestra("/org/example/progettouid/statisticheProdotti.fxml", "Statistiche Prodotti");
    }

    public void apriStatisticheClienti(ActionEvent e) {
        caricaFinestra("/org/example/progettouid/statisticheClienti.fxml", "Statistiche Clienti");
    }

    public void apriStatisticheFornitori(ActionEvent e) {
        caricaFinestra("/org/example/progettouid/statisticheFornitori.fxml", "Statistiche Fornitori");
    }

    private void caricaFinestra(String fxmlPath, String titolo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(titolo);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
