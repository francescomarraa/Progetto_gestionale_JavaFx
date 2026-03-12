package org.example.progettouid.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.progettouid.model.CalendarioHandler;
import org.example.progettouid.model.Evento;

public class UtilitaCalendarioController {

    @FXML
    private TextField inserisciTitoloText;

    @FXML
    private TextArea inserisciDescrizioneText;

    @FXML
    private TextField inserisciLuogoText;

    @FXML
    private DatePicker InserisciDatePicker;

    @FXML
    private GridPane gridPane;

    @FXML
    private ScrollPane scrollPane;

    private final CalendarioHandler ch = new CalendarioHandler();

    private int colonna = 0;
    private int riga = 1; // iniziamo da 1 perché a riga 0 c'è il titolo "Eventi Registrati"
    private final int MAX_COLONNE = 2;

    @FXML
    public void initialize() {
        refreshDashboard();
    }

    public void addEvent() {
        if (inserisciTitoloText.getText().isEmpty() || InserisciDatePicker.getValue() == null) {
            return;
        }

        Evento evento = new Evento(
                inserisciTitoloText.getText(),
                inserisciDescrizioneText.getText(),
                inserisciLuogoText.getText(),
                InserisciDatePicker.getValue()
        );

        ch.aggiungiEvento(evento);

        creaPostIt(evento);
        clearAll();
    }

    private void refreshDashboard() {
        gridPane.getChildren().clear();
        riga = 1;

        for (Evento e : ch.getEventi()) {
            creaPostIt(e);
        }
    }

    public void creaPostIt(Evento evento) {
        VBox card = new VBox(8);
        card.getStyleClass().add("event-card");

        Label t = new Label(evento.getTitolo());
        t.getStyleClass().add("title");

        Label d = new Label(evento.getDescrizione());
        d.getStyleClass().add("details");
        d.setWrapText(true);

        Label info = new Label("Luogo: " + evento.getLuogo() + "\nData: " + evento.getData());
        info.getStyleClass().add("details");

        // Tasto Elimina
        Button btnElimina = new Button("Elimina");
        btnElimina.getStyleClass().add("delete-button"); // Aggiungeremo lo stile nel CSS

        btnElimina.setOnAction(event -> {
            ch.rimuoviEvento(evento); // Rimuove dalla lista e dal file .dat
            refreshDashboard();       // Aggiorna la vista
        });

        card.getChildren().addAll(t, d, info, btnElimina);
        gridPane.add(card, 0, riga++);
    }

    @FXML
    public void removeEvent() {
        if (!ch.getEventi().isEmpty()) {
            Evento ultimo = ch.getEventi().get(ch.getEventi().size() - 1);

            ch.rimuoviEvento(ultimo);

            gridPane.getChildren().clear();
            riga = 1; // resetta il contatore delle righe

            for (Evento e : ch.getEventi()) {
                creaPostIt(e);
            }
        }
    }

    public void clearAll() {
        inserisciTitoloText.clear();
        inserisciDescrizioneText.clear();
        inserisciLuogoText.clear();
        InserisciDatePicker.setValue(null);
    }

}

