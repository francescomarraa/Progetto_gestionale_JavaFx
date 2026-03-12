package org.example.progettouid.controller;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.progettouid.dao.FornitoreDAO;
import org.example.progettouid.model.Fornitore;

public class AggiungiFornitoreController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField cognomeField;
    @FXML
    private TextField viaField;
    @FXML
    private TextField capField;
    @FXML
    private TextField cittaField;
    @FXML
    private TextField ragioneSocialeField;
    @FXML
    private TextField partitaIvaField;
    @FXML
    private TextField telefonoField;

    private Runnable onSalvataggioCompletato;
    private FornitoreDAO fornitoreDAO;
    private Fornitore fornitoreDaModificare;
    private boolean isModifica = false;

    @FXML
    public void initialize() {
        this.fornitoreDAO = new FornitoreDAO();
    }

    public void setOnSalvataggioCompletato(Runnable onSalvataggioCompletato) {
        this.onSalvataggioCompletato = onSalvataggioCompletato;
    }

    @FXML
    void salvaFornitore(ActionEvent event) {
        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String via = viaField.getText();
        String cap = capField.getText();
        String citta = cittaField.getText();
        String ragioneSociale = ragioneSocialeField.getText();
        String partitaIVA = partitaIvaField.getText();
        String telefono = telefonoField.getText();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                if (isModifica) {
                    fornitoreDaModificare.setNome(nome);
                    fornitoreDaModificare.setCognome(cognome);
                    fornitoreDaModificare.setVia(via);
                    fornitoreDaModificare.setCAP(cap);
                    fornitoreDaModificare.setCittà(citta);
                    fornitoreDaModificare.setRagioneSociale(ragioneSociale);
                    fornitoreDaModificare.setPartitaIVA(partitaIVA);
                    fornitoreDaModificare.setTelefono(telefono);
                    fornitoreDAO.aggiornaFornitore(fornitoreDaModificare);
                } else {
                    Fornitore nuovoFornitore = new Fornitore(nome, cognome, telefono, cap, citta, via, ragioneSociale, partitaIVA);
                    fornitoreDAO.salvaFornitore(nuovoFornitore);
                }
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            if (onSalvataggioCompletato != null) {
                onSalvataggioCompletato.run();
            }
            closeStage(event);
        });

        task.setOnFailed(e -> {
            Throwable eccezione = task.getException();
            if (eccezione.getMessage() != null && eccezione.getMessage().contains("UNIQUE constraint failed")) {
                showError("Errore Salvataggio", "Esiste già un fornitore con questa Ragione Sociale.");
            } else {
                showError("Errore", "Si è verificato un errore durante il salvataggio: " + eccezione.getMessage());
            }
            eccezione.printStackTrace();
        });

        new Thread(task).start();
    }

    @FXML
    void annulla(ActionEvent event) {
        closeStage(event);
    }

    public void setFornitoreDaModificare(Fornitore fornitore) {
        this.fornitoreDaModificare = fornitore;
        this.isModifica = true;

        nomeField.setText(fornitore.getNome());
        cognomeField.setText(fornitore.getCognome());
        viaField.setText(fornitore.getVia());
        capField.setText(fornitore.getCAP());
        cittaField.setText(fornitore.getCittà());
        ragioneSocialeField.setText(fornitore.getRagioneSociale());
        partitaIvaField.setText(fornitore.getPartitaIVA());
        telefonoField.setText(fornitore.getTelefono());
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}