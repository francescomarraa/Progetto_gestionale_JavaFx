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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.progettouid.dao.FornitoreDAO;
import org.example.progettouid.model.Cliente;
import org.example.progettouid.model.Fornitore;
import javafx.concurrent.Task;
import org.example.progettouid.model.PippoBot;
import org.example.progettouid.model.Prodotto;
import org.openpdf.text.*;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ListaFornitoriController {

    @FXML
    private TableView<Fornitore> tabellaFornitori;
    @FXML
    private TableColumn<Fornitore, String> colonnaNome;
    @FXML
    private TableColumn<Fornitore, String> colonnaCognome;
    @FXML
    private TableColumn<Fornitore, String> colonnaTelefono;
    @FXML
    private TableColumn<Fornitore, String> colonnaCap;
    @FXML
    private TableColumn<Fornitore, String> colonnaCitta;
    @FXML
    private TableColumn<Fornitore, String> colonnaVia;
    @FXML
    private TableColumn<Fornitore, String> colonnaRagioneSociale;
    @FXML
    private TableColumn<Fornitore, String> colonnaPartitaIva;

    @FXML
    private TextField txtCercaF;

    private FornitoreDAO fornitoreDAO;
    private ObservableList<Fornitore> listaOsservabile;


    @FXML
    public void apriDialog(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/progettouid/paneAggiungiFornitori.fxml"));
            Parent root = loader.load();

            AggiungiFornitoreController aggiungiController = loader.getController();
            
            aggiungiController.setOnSalvataggioCompletato(() -> {
                this.caricaDati();
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Aggiungi Fornitore");
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        fornitoreDAO = new FornitoreDAO();
        listaOsservabile = FXCollections.observableArrayList();

        colonnaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colonnaCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colonnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colonnaCap.setCellValueFactory(new PropertyValueFactory<>("CAP"));
        colonnaCitta.setCellValueFactory(new PropertyValueFactory<>("città"));
        colonnaVia.setCellValueFactory(new PropertyValueFactory<>("via"));
        colonnaRagioneSociale.setCellValueFactory(new PropertyValueFactory<>("ragioneSociale"));
        colonnaPartitaIva.setCellValueFactory(new PropertyValueFactory<>("partitaIVA"));

        tabellaFornitori.setItems(listaOsservabile);
        caricaDati();
    }

    private void caricaDati(){
        Task<List<Fornitore>> task = new Task<>() {
            @Override
            protected List<Fornitore> call() throws Exception {
                return fornitoreDAO.getFornitori();
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
    private void eliminaFornitori(ActionEvent event){
        Fornitore selezionato = tabellaFornitori.getSelectionModel().getSelectedItem();
        if (selezionato == null){
            return;
        }
            Task<Void> task = new Task<>() {
            @Override
                protected Void call() throws Exception {
                    fornitoreDAO.eliminaFornitore(selezionato);
                    return null;
                }
            };

            task.setOnSucceeded(e -> {
                tabellaFornitori.getItems().remove(selezionato);
                caricaDati();
            });

            task.setOnFailed(e -> {
                task.getException().printStackTrace();
            });

            new Thread(task).start();
        }

    @FXML
    public void modificaFornitore(ActionEvent event) {
        Fornitore selezionato = tabellaFornitori.getSelectionModel().getSelectedItem();

        if (selezionato == null) {
            showPopupLog(Alert.AlertType.ERROR, "Errore", "Seleziona un fornitore da modificare");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/progettouid/paneModificaFornitore.fxml")
            );
            Parent root = loader.load();

            AggiungiFornitoreController controller = loader.getController();

            controller.setFornitoreDaModificare(selezionato);

            controller.setOnSalvataggioCompletato(() -> caricaDati());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifica Fornitore");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void scaricaPdfFornitori(){

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salva PDF Fornitori");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
            );
            java.io.File file = fileChooser.showSaveDialog(tabellaFornitori.getScene().getWindow());
            if (file == null) {
                return;
            }

            Document document = new Document(PageSize.A4);

            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Font titolo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            document.add(new Paragraph("Elenco Fornitori", titolo));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);

            table.addCell(new PdfPCell(new Phrase("Nome")));
            table.addCell(new PdfPCell(new Phrase("Cognome")));
            table.addCell(new PdfPCell(new Phrase("Via")));
            table.addCell(new PdfPCell(new Phrase("CAP")));
            table.addCell(new PdfPCell(new Phrase("Città")));
            table.addCell(new PdfPCell(new Phrase("Regione Sociale")));
            table.addCell(new PdfPCell(new Phrase("Partita Iva")));
            table.addCell(new PdfPCell(new Phrase("Telefono")));



            List<Fornitore> lista = tabellaFornitori.getItems();
            for (Fornitore f : lista) {
                table.addCell(f.getNome());
                table.addCell(f.getCognome());
                table.addCell(f.getVia());
                table.addCell(f.getCAP());
                table.addCell(f.getCittà());
                table.addCell(f.getRagioneSociale());
                table.addCell(f.getPartitaIVA());
                table.addCell(f.getTelefono());
            }

            document.add(table);
            document.close();

            showPopupLog(Alert.AlertType.INFORMATION, "Successo", "PDF generato con successo!");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML
    public void cercaFornitore() {
        String testo = txtCercaF.getText().toLowerCase().trim();

        if (testo.isEmpty()) {
            tabellaFornitori.setItems(listaOsservabile);
            return;
        }

        ObservableList<Fornitore> filtrati = FXCollections.observableArrayList();

        for (Fornitore f : listaOsservabile) {
            if (
                    f.getNome().toLowerCase().contains(testo) ||
                            f.getCognome().toLowerCase().contains(testo) ||
                            f.getVia().toLowerCase().contains(testo) ||
                            f.getCAP().toLowerCase().contains(testo) ||
                            f.getCittà().toLowerCase().contains(testo) ||
                            f.getTelefono().toLowerCase().contains(testo) ||
                            f.getRagioneSociale().toLowerCase().contains(testo) ||
                            f.getPartitaIVA().toLowerCase().contains(testo)

            ) {
                filtrati.add(f);
            }
        }

        tabellaFornitori.setItems(filtrati);
    }

    public void showPopupLog(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}