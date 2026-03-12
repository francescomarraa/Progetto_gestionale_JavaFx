package org.example.progettouid.controller;
import javafx.scene.chart.*;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import org.example.progettouid.model.Prodotto;
import org.example.progettouid.dao.StatisticheProdottiDAO;
import org.example.progettouid.controller.HomeController;
import org.example.progettouid.dao.ProdottiDAO;




public class StatisticheProdottiController {


    @FXML private PieChart pieChartValore;
    @FXML
    private Pane pnlStatistiche;
    @FXML private Label lblTotProdotti;
    @FXML private Label lblValoreTotale;
    @FXML private Label lblPrezzoMedio;

    @FXML private BarChart<String, Number> barPrezzo;

    @FXML private DatePicker dataInizio;
    @FXML private DatePicker dataFine;

    private final ProdottiDAO prodottoDAO = new ProdottiDAO();

    public void initialize(){
        if (lblTotProdotti == null) {
            System.err.println("lblTotProdotti NON iniettato!");
        } else {
            aggiornaLabel();
        }
        if(barPrezzo == null){
            System.err.println("barPrezzo NON iniettato!");
        }else {
            CreaGraficoComparazione();
        }

        if (pieChartValore == null) {
            System.err.println("pieChartValore NON iniettato!");
        } else {
            CreaGraficoTorta();
        }
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    @FXML
    void annulla(ActionEvent event) {
        closeStage(event);
    }

    public void  aggiornaLabel(){
        lblTotProdotti.setText(String.valueOf(prodottoDAO.countProdotti()));
        double media = prodottoDAO.mediaProdotti();
        lblPrezzoMedio.setText(String.format("%.2f€", media));
        System.out.println(prodottoDAO.mediaProdotti());
        double somma = prodottoDAO.sommaProdotti();
        lblValoreTotale.setText(String.format("%.2f€", somma));
    }

    private void CreaGraficoComparazione() {
        barPrezzo.setTitle("Statistiche Prodotti");
        barPrezzo.getXAxis().setLabel("Categoria");
        barPrezzo.getYAxis().setLabel("Pezzi in Magazzino");
        
        barPrezzo.getXAxis().setTickLabelRotation(90);
        barPrezzo.setAnimated(false);
        
        barPrezzo.getData().clear();

        XYChart.Series<String, Number> serieQuantita = new XYChart.Series<>();
        serieQuantita.setName("Inventario");

        Task<Map<String, Integer>> task = new Task<>() {
            @Override
            protected Map<String, Integer> call() throws Exception {
                StatisticheProdottiDAO statisticheProdottiDAO = new StatisticheProdottiDAO();
                return statisticheProdottiDAO.getTuttiProdotti();
            }
        };

        task.setOnSucceeded(e -> {
            Map<String, Integer> datiDalDB = task.getValue();
            for (Map.Entry<String, Integer> entry : datiDalDB.entrySet()) {
                serieQuantita.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
            barPrezzo.getData().add(serieQuantita);
        });

        task.setOnFailed(workerStateEvent -> {
            Throwable eccezione = task.getException();
            showPopupReg(Alert.AlertType.ERROR, "Errore", "Errore caricamento dati: " + eccezione.getMessage());
            eccezione.printStackTrace();
        });

        new Thread(task).start();
    }



    private void CreaGraficoTorta(){
    pieChartValore.getData().clear();
    pieChartValore.setLabelsVisible(true);

    Task<Map<String, Integer>> task = new Task<Map<String, Integer>>() {
        @Override
        protected Map<String, Integer> call() throws Exception {
            StatisticheProdottiDAO statisticheProdottiDAO = new StatisticheProdottiDAO();
            return statisticheProdottiDAO.getValoreEconomici();

        }
    };
    task.setOnSucceeded(e->{
        Map<String, Integer> datiDalDB = task.getValue();
        for (Map.Entry<String, Integer> entry : datiDalDB.entrySet()) {
            PieChart.Data fetta = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChartValore.getData().add(fetta);
        }

    });

    task.setOnFailed(workerStateEvent -> {
        Throwable eccezione = task.getException();
        showPopupReg(Alert.AlertType.ERROR, "Errore", "Errore: " + eccezione.getMessage());
        System.out.println("Errore: " + eccezione.getMessage());
        eccezione.printStackTrace();
    });

    new Thread(task).start();


}



private void showPopupReg(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}
}
