package org.example.progettouid.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.example.progettouid.dao.ProdottiDAO;
import org.example.progettouid.model.PippoBot;
import org.example.progettouid.model.Prodotto;

import java.util.List;

public class PippoBotController {

    @FXML
    private ListView<String> listaSuggerimenti;
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField inputArea;
    @FXML
    private Button btnInvia;

    private ProdottiDAO prodottiDAO;
    private final PippoBot pippoBot = new PippoBot();

    @FXML
    public void initialize() {
        prodottiDAO = new ProdottiDAO();
        listaSuggerimenti.setOnMouseClicked(this::selezionaSuggerimento);
        listaSuggerimenti.setVisible(false);
    }

    @FXML
    void onKeyTyped(KeyEvent event) {
        String testoAttuale = inputArea.getText();
        int indiceChiocciola = testoAttuale.lastIndexOf("@");

        if (indiceChiocciola != -1) {
            String query = testoAttuale.substring(indiceChiocciola + 1);
            if (!query.isEmpty()) {
                List<Prodotto> risultati = prodottiDAO.cerca(query);
                if (!risultati.isEmpty()) {

                    listaSuggerimenti.getItems().clear();
                    for (Prodotto prodotto : risultati) {
                        listaSuggerimenti.getItems().add(prodotto.getNomeProdotto());
                    }

                    listaSuggerimenti.setVisible(true);
                } else {
                    listaSuggerimenti.setVisible(false);
                }
            } else {
                listaSuggerimenti.setVisible(false);
            }
        } else {
            listaSuggerimenti.setVisible(false);
        }
    }

    private void selezionaSuggerimento(MouseEvent event) {
        String selezionato = listaSuggerimenti.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            String testo = inputArea.getText();
            int indiceChiocciola = testo.lastIndexOf("@");
            String nuovoTesto = testo.substring(0, indiceChiocciola) + selezionato + " ";
            inputArea.setText(nuovoTesto);
            inputArea.positionCaret(nuovoTesto.length());
            listaSuggerimenti.setVisible(false);
        }
    }

    @FXML
    void AvvioPippoBot(ActionEvent event) {
        String domanda = inputArea.getText();
        if (domanda == null || domanda.trim().isEmpty()) {
            return;
        }

        chatArea.appendText("TU: " + domanda + "\n");
        inputArea.clear();
        chatArea.appendText("PippoBot sta scrivendo....\n");

        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                return pippoBot.chiedi(domanda);
            }
        };

        task.setOnSucceeded(e -> {
            String risposta = task.getValue();
            String currentText = chatArea.getText();
            currentText = currentText.substring(0, currentText.lastIndexOf("PippoBot sta scrivendo....\n"));
            chatArea.setText(currentText);
            chatArea.appendText("PippoBot: " + risposta + "\n");
            chatArea.setScrollTop(Double.MAX_VALUE);
        });

        task.setOnFailed(e -> {
            String currentText = chatArea.getText();
            currentText = currentText.substring(0, currentText.lastIndexOf("PippoBot sta scrivendo....\n"));
            chatArea.setText(currentText);
            chatArea.appendText("PippoBot: Scusa, ho avuto un errore.\n");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
}