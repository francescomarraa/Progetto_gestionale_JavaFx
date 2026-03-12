package org.example.progettouid.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.progettouid.dao.ClienteDAO;
import org.example.progettouid.model.Cliente;

public class AggiungiClienteController {

    @FXML
    private TextField nomeField;
    @FXML
    private TextField cognomeField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField cittaField;
    @FXML
    private Button btnSalva;

    private Cliente clienteDaModificare;
    private boolean isModifica = false;


    private Runnable onSalvataggioCompletato;
    private ClienteDAO clienteDAO;

    @FXML
    public void initialize() {
        this.clienteDAO = new ClienteDAO();
    }

    public void setOnSalvataggioCompletato(Runnable onSalvataggioCompletato) {
        this.onSalvataggioCompletato = onSalvataggioCompletato;
    }

    @FXML
    void salvaCliente(ActionEvent event) {

        String nome = nomeField.getText();
        String cognome = cognomeField.getText();
        String telefono = telefonoField.getText();
        String citta = cittaField.getText();

        try {
            if (isModifica) {
                clienteDaModificare.setNome(nome);
                clienteDaModificare.setCognome(cognome);
                clienteDaModificare.setTelefono(telefono);
                clienteDaModificare.setCitta(citta);
                clienteDAO.aggiornaCliente(clienteDaModificare);
            } else {
                Cliente nuovoCliente = new Cliente(nome, cognome, telefono, citta);
                clienteDAO.salvaCliente(nuovoCliente);
            }

            if (onSalvataggioCompletato != null) {
                onSalvataggioCompletato.run();
            }

            closeStage(event);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void annulla(ActionEvent event) {
        // Anche annulla deve chiudere la finestra
        closeStage(event);
    }

    public void setClienteDaModificare(Cliente cliente) {
        this.clienteDaModificare = cliente;
        this.isModifica = true;

        nomeField.setText(cliente.getNome());
        cognomeField.setText(cliente.getCognome());
        telefonoField.setText(cliente.getTelefono());
        cittaField.setText(cliente.getCitta());

    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
