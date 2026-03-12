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
import org.example.progettouid.dao.ClienteDAO;
import org.example.progettouid.model.Cliente;
import org.openpdf.text.*;
import org.openpdf.text.pdf.PdfPCell;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.awt.Color;


import java.io.IOException;
import java.util.List;

public class ListaClientiController {

    @FXML
    private TableView<Cliente> tabellaClienti;
    @FXML
    private TableColumn<Cliente, String> colonnaNome;
    @FXML
    private TableColumn<Cliente, String> colonnaCognome;
    @FXML
    private TableColumn<Cliente, String> colonnaCitta;
    @FXML
    private TableColumn<Cliente, String> colonnaTelefono;
    @FXML
    private TextField txtCerca;

    private ClienteDAO clienteDAO;
    private ObservableList<Cliente> listaOsservabile;

    @FXML
    public void apriDialogCliente(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/progettouid/paneAggiungiClienti.fxml"));
            Parent root = loader.load();

            AggiungiClienteController aggiungiController = loader.getController();

            aggiungiController.setOnSalvataggioCompletato(() -> {
                this.caricaDatiCliente();
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Aggiungi Cliente");
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void initialize(){
        clienteDAO = new ClienteDAO();
        listaOsservabile = FXCollections.observableArrayList();

        colonnaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colonnaCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colonnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colonnaCitta.setCellValueFactory(new PropertyValueFactory<>("citta"));

        tabellaClienti.setItems(listaOsservabile);
        caricaDatiCliente();
    }

    private void caricaDatiCliente(){
        listaOsservabile.clear();
        List<Cliente> listaTemp = clienteDAO.getClienti();
        listaOsservabile.addAll(listaTemp);
    }

    @FXML
    private void eliminaClienti(ActionEvent event){
        Cliente selezionato = tabellaClienti.getSelectionModel().getSelectedItem();
        if (selezionato!=null){
            clienteDAO.eliminaCliente(selezionato);
            tabellaClienti.getItems().remove(selezionato);
            caricaDatiCliente();
        }
        else{
            showPopupLog(Alert.AlertType.ERROR, "Errore", "Seleziona un Cliente!");
        }
    }

    @FXML
    public void modificaCliente(ActionEvent event) {
        Cliente selezionato = tabellaClienti.getSelectionModel().getSelectedItem();

        if (selezionato == null) {
            showPopupLog(Alert.AlertType.ERROR, "Errore", "Seleziona un cliente da modificare");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/progettouid/paneModificaCliente.fxml")
            );
            Parent root = loader.load();

            AggiungiClienteController controller = loader.getController();

            controller.setClienteDaModificare(selezionato);

            controller.setOnSalvataggioCompletato(() -> caricaDatiCliente());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifica Cliente");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void scaricaPdfClienti() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salva PDF clienti");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
            );
            java.io.File file = fileChooser.showSaveDialog(tabellaClienti.getScene().getWindow());
            if (file == null) {
                return;
            }

            Document document = new Document(PageSize.A4);

            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Font titolo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            document.add(new Paragraph("Elenco Clienti", titolo));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            table.addCell(new PdfPCell(new Phrase("Nome")));
            table.addCell(new PdfPCell(new Phrase("Cognome")));
            table.addCell(new PdfPCell(new Phrase("Telefono")));
            table.addCell(new PdfPCell(new Phrase("Città")));

            List<Cliente> lista = tabellaClienti.getItems();
            for (Cliente c : lista) {
                table.addCell(c.getNome());
                table.addCell(c.getCognome());
                table.addCell(c.getTelefono());
                table.addCell(c.getCitta());
            }

            document.add(table);
            document.close();

            showPopupLog(Alert.AlertType.INFORMATION, "Successo", "PDF generato con successo!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cercaCliente() {
        String testo = txtCerca.getText().toLowerCase().trim();

        if (testo.isEmpty()) {
            tabellaClienti.setItems(listaOsservabile);
            return;
        }

        ObservableList<Cliente> filtrati = FXCollections.observableArrayList();

        for (Cliente c : listaOsservabile) {
            if (
                    c.getNome().toLowerCase().contains(testo) ||
                            c.getCognome().toLowerCase().contains(testo) ||
                            c.getCitta().toLowerCase().contains(testo) ||
                            c.getTelefono().toLowerCase().contains(testo)
            ) {
                filtrati.add(c);
            }
        }

        tabellaClienti.setItems(filtrati);
    }

    public void showPopupLog(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}