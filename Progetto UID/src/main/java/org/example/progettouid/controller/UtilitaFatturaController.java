package org.example.progettouid.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.example.progettouid.dao.ClienteDAO;
import org.example.progettouid.dao.ProdottiDAO;
import org.example.progettouid.model.Cliente;
import org.example.progettouid.model.Prodotto;
import org.example.progettouid.model.FattureHandler;
import org.openpdf.text.Document;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;


public class UtilitaFatturaController {

    @FXML
    private TextField numeroRicevutaText;
    @FXML
    private TextField numeroOrdineText;
    @FXML
    private DatePicker ordineDate;
    @FXML
    private DatePicker scadenzaDate;

    @FXML
    private ChoiceBox<Cliente> ClienteChoiceBox;
    @FXML
    private TextField IvaClienteText;

    @FXML
    private ChoiceBox<Prodotto> inserisciProdottoChoice;
    @FXML
    private TextField quantitaProdottoText;
    @FXML
    private TextField ivaProdottoText;

    @FXML
    private TableView<Prodotto> prodottiTable;
    @FXML
    private TableColumn<Prodotto, String> columnIdProdottoProdotto;
    @FXML
    private TableColumn<Prodotto, String> columnNomeProdotto;
    @FXML
    private TableColumn<Prodotto, String> columnCategoriaProdotto;
    @FXML
    private TableColumn<Prodotto, Integer> columnQuantitaProdotto;
    @FXML
    private TableColumn<Prodotto, Float> columnPrezzoProdotto;
    @FXML
    private TableColumn<Prodotto, Integer> columnIvaProdotto;

    @FXML
    private Label IvaLabelTot;
    @FXML
    private Label prezzoLabelTot;

    @FXML
    private TextField inserisciScontoText;

    @FXML
    private Button esportaPdfButton;

    private FattureHandler fh;

    @FXML
    public void initialize() {
        fh = new FattureHandler();

        ClienteDAO clienteDAO = new ClienteDAO();
        inserisciClienti(clienteDAO);

        ProdottiDAO prodottiDAO = new ProdottiDAO();
        inserisciProdotti(prodottiDAO);

        columnIdProdottoProdotto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCodiceProdotto()));
        columnNomeProdotto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNomeProdotto()));
        columnCategoriaProdotto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategoria()));
        columnQuantitaProdotto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantitaProdotto()).asObject());
        columnPrezzoProdotto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleFloatProperty(cellData.getValue().getPrezzoUnitario()).asObject());
        columnIvaProdotto.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getIva()).asObject());
    }

    private void inserisciClienti(ClienteDAO dao) {
        ClienteChoiceBox.setItems(FXCollections.observableArrayList(dao.getClienti()));
    }

    private void inserisciProdotti(ProdottiDAO dao) {
        inserisciProdottoChoice.setItems(FXCollections.observableArrayList(dao.getProdotti()));
    }

    @FXML
    public void addProdotto() {
        Prodotto p = inserisciProdottoChoice.getValue();
        if (p == null) return;

        int quantita, iva;
        try {
            quantita = Integer.parseInt(quantitaProdottoText.getText());
            iva = Integer.parseInt(ivaProdottoText.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Quantità e IVA devono essere numeri interi").showAndWait();
            return;
        }

        fh.addProdotto(p, quantita, iva);

        prodottiTable.setItems(FXCollections.observableArrayList(fh.getProdottiFattura()));

        prezzoLabelTot.setText(String.format("%.2f", fh.getTotalePrezzo()));
        IvaLabelTot.setText(String.valueOf(fh.getTotaleIva()));

        quantitaProdottoText.clear();
        ivaProdottoText.clear();
    }

    @FXML
    public void clearAll() {
        fh.clearProdottiEnd();
        prodottiTable.getItems().clear();

        prezzoLabelTot.setText("0");
        IvaLabelTot.setText("0");

        numeroRicevutaText.clear();
        numeroOrdineText.clear();
        ordineDate.setValue(null);
        scadenzaDate.setValue(null);
        ClienteChoiceBox.setValue(null);
        IvaClienteText.clear();
        inserisciProdottoChoice.setValue(null);
        quantitaProdottoText.clear();
        ivaProdottoText.clear();
    }

    @FXML
    private void esportaPdf() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salva PDF fattura");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
            );

            File file = fileChooser.showSaveDialog(esportaPdfButton.getScene().getWindow());
            if (file == null) return;

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Font titoloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Font normale = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Font bold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

            PdfPTable headerTable = new PdfPTable(3);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new int[]{2, 4, 2});

            try {
                String logoPath = "src/main/resources/org/example/images/logoUID2.png";
                org.openpdf.text.Image logo = org.openpdf.text.Image.getInstance(logoPath);
                logo.scaleToFit(80, 80);
                headerTable.addCell(logo);
            } catch (Exception e) {
                headerTable.addCell("");
            }

            Paragraph titolo = new Paragraph("FATTURA", titoloFont);
            titolo.setAlignment(Paragraph.ALIGN_CENTER);
            PdfPCell titoloCell = new PdfPCell(titolo);
            titoloCell.setBorder(0);
            titoloCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            titoloCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            headerTable.addCell(titoloCell);

            java.time.LocalDate today = java.time.LocalDate.now();
            PdfPCell dataCell = new PdfPCell(new Paragraph("Data: " + today.toString(), normale));
            dataCell.setBorder(0);
            dataCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
            dataCell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
            headerTable.addCell(dataCell);

            document.add(headerTable);
            document.add(new Paragraph(" "));

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            Cliente clienteSelezionato = ClienteChoiceBox.getValue();
            if (clienteSelezionato != null) {

                PdfPTable clienteTable = new PdfPTable(2);
                clienteTable.setWidthPercentage(100);

                clienteTable.addCell(new Phrase("Cliente:", bold));
                clienteTable.addCell(new Phrase(clienteSelezionato.toString(), normale));

                String partitaIVA = IvaClienteText.getText();

                clienteTable.addCell(new Phrase("Partita IVA:", bold));
                clienteTable.addCell(new Phrase(
                        (partitaIVA != null && !partitaIVA.isBlank()) ? partitaIVA : "-",
                        normale
                ));

                document.add(clienteTable);
                document.add(new Paragraph(" "));
            }

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4, 1, 2, 1, 2});

            table.addCell(new Phrase("Nome Prodotto", bold));
            table.addCell(new Phrase("Qtà", bold));
            table.addCell(new Phrase("Prezzo Unitario", bold));
            table.addCell(new Phrase("IVA (%)", bold));
            table.addCell(new Phrase("Totale", bold));

            double totaleFattura = 0.0;
            for (Prodotto p : prodottiTable.getItems()) {
                double totale = p.getPrezzoUnitario() * p.getQuantitaProdotto() * (1 + p.getIva() / 100.0);
                totaleFattura += totale;

                table.addCell(new Phrase(p.getNomeProdotto(), normale));
                table.addCell(new Phrase(String.valueOf(p.getQuantitaProdotto()), normale));
                table.addCell(new Phrase(String.format("%.2f", p.getPrezzoUnitario()), normale));
                table.addCell(new Phrase(String.valueOf(p.getIva()), normale));
                table.addCell(new Phrase(String.format("%.2f", totale), normale));
            }

            document.add(table);
            document.add(new Paragraph(" "));

            float sconto = 0;
            try {
                sconto = Float.parseFloat(inserisciScontoText.getText());
            } catch (NumberFormatException ignored) {}

            fh.setSconto(sconto);

            document.add(new Paragraph(
                    "Imponibile: " + String.format("%.2f", fh.getImponibile()) + " €",
                    normale
            ));

            document.add(new Paragraph(
                    "Sconto (" + sconto + "%): -"
                            + String.format("%.2f", fh.getValoreSconto()) + " €",
                    normale
            ));

            document.add(new Paragraph(
                    "IVA: " + String.format("%.2f", fh.getTotaleIvaScontata()) + " €",
                    normale
            ));

            Paragraph totalePar = new Paragraph(
                    "Totale Fattura: " +
                            String.format("%.2f", fh.getTotaleFattura()) + " €",
                    bold
            );
            totalePar.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(totalePar);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            Paragraph firma = new Paragraph("Firma Cliente: _______________________", normale);
            firma.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(firma);

            document.close();

            System.out.println("PDF generato: " + file.getAbsolutePath());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successo");
            alert.setHeaderText(null);
            alert.setContentText("PDF generato con successo!");
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText(null);
            alert.setContentText("Errore durante la generazione del PDF.");
            alert.showAndWait();
            e.printStackTrace();
        }


    }
}