package org.example.progettouid.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;

import org.example.progettouid.dao.StatisticheClientiDAO;

import java.io.IOException;
import java.util.Map;

public class StatisticheClientiController {

    @FXML
    private Label lblTotClienti;

    @FXML
    private DatePicker dataInizio;

    @FXML
    private DatePicker dataFine;

    @FXML
    private BarChart<String, Number> barClienti;

    @FXML
    private PieChart pieClienti;

    private final StatisticheClientiDAO dao = new StatisticheClientiDAO();

    @FXML
    public void initialize() {
        aggiornaLabel();
        creaGraficoBar();
        creaGraficoPie();
    }

    @FXML
    void annulla(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void aggiornaLabel() {
        lblTotClienti.setText(String.valueOf(dao.countClienti()));
    }

    private void creaGraficoBar() {
        barClienti.getData().clear();
        barClienti.setTitle("Clienti");
        
        barClienti.getXAxis().setTickLabelRotation(90);
        barClienti.setAnimated(false);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Clienti");

        Task<Map<String, Integer>> task = new Task<>() {
            @Override
            protected Map<String, Integer> call() {
                return dao.getClientiPerNome();
            }
        };

        task.setOnSucceeded(e -> {
            Map<String, Integer> dati = task.getValue();
            for (Map.Entry<String, Integer> entry : dati.entrySet()) {
                serie.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            barClienti.getData().add(serie);
        });

        task.setOnFailed(e -> {
            Throwable exc = task.getException();
            showPopup(Alert.AlertType.ERROR, "Errore", "Errore caricamento dati: " + exc.getMessage());
        });

        new Thread(task).start();
    }

    private void creaGraficoPie() {
        pieClienti.getData().clear();
        pieClienti.setLabelsVisible(true);

        Task<Map<String, Integer>> task = new Task<>() {
            @Override
            protected Map<String, Integer> call() {
                return dao.getClientiPerCitta();
            }
        };

        task.setOnSucceeded(e -> {
            Map<String, Integer> dati = task.getValue();
            dati.forEach((citta, valore) -> pieClienti.getData().add(new PieChart.Data(citta, valore)));
        });

        task.setOnFailed(e -> {
            Throwable exc = task.getException();
            showPopup(Alert.AlertType.ERROR, "Errore", "Errore caricamento dati: " + exc.getMessage());
        });

        new Thread(task).start();
    }

    private void showPopup(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}