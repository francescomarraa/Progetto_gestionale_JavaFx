package org.example.progettouid.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.example.progettouid.dao.FornitoreDAO;
import org.example.progettouid.dao.StatisticheFornitoriDAO;
import org.example.progettouid.dao.StatisticheProdottiDAO;
import org.example.progettouid.model.Fornitore;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;

public class StatisticheFornitoriController {
    @FXML
    private Label lblTotFornitori;
    @FXML
    private DatePicker dataInizio;
    @FXML
    private DatePicker dataFine;
    @FXML
    private PieChart pieChart;
    @FXML
    private BarChart<String, Number> barPrezzo;
    private final FornitoreDAO fornitoreDAO = new FornitoreDAO();

    @FXML
    public void initialize() {
        aggiornaLabel();
        CreaGraficoComparazioneFornitori();
        CreaGraficoTortaFornitori();
    }

    private void CreaGraficoComparazioneFornitori(){
        barPrezzo.setTitle("Statistiche Fornitori");
        barPrezzo.getXAxis().setLabel("Fornitore");
        
        barPrezzo.getXAxis().setTickLabelRotation(90);
        
        barPrezzo.getYAxis().setLabel("Media Pezzi in Magazzino");
        barPrezzo.setAnimated(false);

        barPrezzo.getData().clear();
        XYChart.Series<String, Number> serieQuantita = new XYChart.Series<>();
        serieQuantita.setName("Inventario");

        Task<Map<String, Double>> task = new Task<>() {
            @Override
            protected Map<String, Double> call() throws Exception {
                StatisticheFornitoriDAO dao = new StatisticheFornitoriDAO();
                return dao.getTuttiFornitori();
            }
        };

        task.setOnSucceeded(e -> {
            Map<String, Double> datiDalDB = task.getValue();
            Platform.runLater(() -> {
                for (Map.Entry<String, Double> entry : datiDalDB.entrySet()) {
                    serieQuantita.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                }
                barPrezzo.getData().add(serieQuantita);
            });
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            ex.printStackTrace();
        });

        new Thread(task).start();
    }

    private void CreaGraficoTortaFornitori(){
        pieChart.getData().clear();
        pieChart.setLabelsVisible(true);
        
        Task<Map<String, Integer>> task = new Task<>() {
            @Override
            protected Map<String, Integer> call() throws Exception {
                StatisticheFornitoriDAO dao = new StatisticheFornitoriDAO();
                return dao.getDistribuzioneProdotti();
            }
        };

        task.setOnSucceeded(e->{
            Map<String, Integer> datiDalDB = task.getValue();
            Platform.runLater(() -> {
                for (Map.Entry<String, Integer> entry : datiDalDB.entrySet()) {
                    PieChart.Data fetta = new PieChart.Data(entry.getKey(), entry.getValue());
                    pieChart.getData().add(fetta);
                }
            });
        });

        task.setOnFailed(workerStateEvent -> {
            Throwable eccezione = task.getException();
            eccezione.printStackTrace();
        });

        new Thread(task).start();
    }

    public void aggiornaLabel() {
        int totFornitori = fornitoreDAO.countFornitori(); 
        lblTotFornitori.setText(String.valueOf(totFornitori));
    }

    @FXML
    private void annulla(javafx.event.ActionEvent event) {
        javafx.scene.Node source = (javafx.scene.Node) event.getSource();
        javafx.stage.Stage stage = (javafx.stage.Stage) source.getScene().getWindow();
        stage.close();
    }
}