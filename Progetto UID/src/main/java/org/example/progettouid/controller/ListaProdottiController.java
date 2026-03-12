package org.example.progettouid.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.progettouid.dao.ProdottiDAO;
import org.example.progettouid.model.Cliente;
import org.example.progettouid.model.Prodotto;
import javafx.concurrent.Task;

import java.io.FileOutputStream;
import java.io.IOException;
import org.openpdf.text.*;
import org.openpdf.text.pdf.PdfPCell;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import java.util.List;

public class ListaProdottiController {

    @FXML
    private TableView<Prodotto> tabellaProdotti;
    @FXML
    private TableColumn<Prodotto, String> colonnaCodice;
    @FXML
    private TableColumn<Prodotto, String> colonnaNome;
    @FXML
    private TableColumn<Prodotto, Float> colonnaPrezzo;
    @FXML
    private TableColumn<Prodotto, Integer> colonnaQuantita;
    @FXML
    private TableColumn<Prodotto, String> colonnaCategoria;
    @FXML
    private TableColumn<Prodotto, String> colonnaFornitore;
    @FXML
    private TableColumn<Prodotto, String> colonnaDataCarico;
    @FXML
    private TextField txtCerca;

    private ProdottiDAO prodottiDAO;
    private ObservableList<Prodotto> listaOsservabile;

    @FXML
    public void initialize(){
        prodottiDAO = new ProdottiDAO();
        listaOsservabile = FXCollections.observableArrayList();

        colonnaCodice.setCellValueFactory(new PropertyValueFactory<>("codiceProdotto"));
        colonnaNome.setCellValueFactory(new PropertyValueFactory<>("nomeProdotto"));
        colonnaPrezzo.setCellValueFactory(new PropertyValueFactory<>("prezzoUnitario"));
        colonnaQuantita.setCellValueFactory(new PropertyValueFactory<>("quantitaProdotto"));
        colonnaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colonnaFornitore.setCellValueFactory(new PropertyValueFactory<>("idFornitore"));
        colonnaDataCarico.setCellValueFactory(new PropertyValueFactory<>("dataCarico"));

        tabellaProdotti.setItems(listaOsservabile);
        caricaDati();
    }


    public void caricaDati(){
        Task<List<Prodotto>> task = new Task<>() {
            @Override
            protected List<Prodotto> call() throws Exception {
                return prodottiDAO.getProdotti();
            }
        };

        task.setOnSucceeded(e->{
            listaOsservabile.clear();
            listaOsservabile.addAll(task.getValue());
        });

        task.setOnFailed(e->{
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }


    @FXML
    public void scaricaPdfProdotti(){

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salva PDF prodotti");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
            );
            java.io.File file = fileChooser.showSaveDialog(tabellaProdotti.getScene().getWindow());
            if (file == null) {
                return;
            }

            Document document = new Document(PageSize.A4);

            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Font titolo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            document.add(new Paragraph("Elenco Prodotti", titolo));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);

            table.addCell(new PdfPCell(new Phrase("Codice Prodotto")));
            table.addCell(new PdfPCell(new Phrase("Nome Prodotto")));
            table.addCell(new PdfPCell(new Phrase("Prezzo Unitario")));
            table.addCell(new PdfPCell(new Phrase("Quantità Prodotto")));
            table.addCell(new PdfPCell(new Phrase("Categoria")));
            table.addCell(new PdfPCell(new Phrase("Fornitore")));
            table.addCell(new PdfPCell(new Phrase("Data Carico")));


            List<Prodotto> lista = tabellaProdotti.getItems();
            for (Prodotto p : lista) {
                table.addCell(p.getCodiceProdotto());
                table.addCell(p.getNomeProdotto());
                table.addCell(String.valueOf(p.getPrezzoUnitario()));
                table.addCell(String.valueOf(p.getQuantitaProdotto()));
                table.addCell(p.getCategoria());
                table.addCell(String.valueOf(p.getIdFornitore()));
                table.addCell(String.valueOf(p.getDataCarico()));
            }

            document.add(table);
            document.close();

            showPopupLog(Alert.AlertType.INFORMATION, "Successo", "PDF generato con successo!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void eliminaProdotto(ActionEvent event){
        Prodotto selezionato = tabellaProdotti.getSelectionModel().getSelectedItem();
        if (selezionato == null){
            System.out.println("seleziona un prodotto");
            return;
        }
        Task<Void> task = new Task<>(){
            @Override
            protected Void call() throws Exception {
                prodottiDAO.eliminaProdotto(selezionato);
                return null;
            }
        };

        task.setOnSucceeded(e->{
            tabellaProdotti.getItems().remove(selezionato);
            caricaDati();
        });

        task.setOnFailed(e->{
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    @FXML
    public void cercaProdotto() {
        String testo = txtCerca.getText().toLowerCase().trim();

        if (testo.isEmpty()) {
            tabellaProdotti.setItems(listaOsservabile);
            return;
        }

        ObservableList<Prodotto> filtrati = FXCollections.observableArrayList();

        for (Prodotto p : listaOsservabile) {
            if (
                    p.getNomeProdotto().toLowerCase().contains(testo) ||
                            p.getCategoria().toLowerCase().contains(testo)
            ) {
                filtrati.add(p);
            }
        }

        tabellaProdotti.setItems(filtrati);
    }

    public void showPopupLog(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}