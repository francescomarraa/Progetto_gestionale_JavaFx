package org.example.progettouid.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.example.progettouid.dao.AziendaDAO;
import org.example.progettouid.dao.ClienteDAO;
import org.example.progettouid.dao.FornitoreDAO;
import org.example.progettouid.dao.ProdottiDAO;
import org.example.progettouid.model.Azienda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class paneHomeController {
    @FXML
    private Label dateLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label lblUltimoProdotto;

    @FXML
    private Label lblUltimoCliente;

    @FXML
    private Label lblUltimoFornitore;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy");

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML private Label lblTotClienti;
    @FXML private Label lblTotProdotti;
    @FXML private Label lblTotFornitori;
    @FXML private Label lblDashboardTitle;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdottiDAO prodottoDAO = new ProdottiDAO();
    private final FornitoreDAO fornitoreDAO = new FornitoreDAO();
    private final AziendaDAO aziendaDAO = new AziendaDAO();

    public void initialize(){
        updateDateTime();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> updateDateTime())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        aggiornaDashboard();
        aggiornaTitoloDashboard();
        caricaStatoGenerale();
    }

    private void updateDateTime(){
        LocalDateTime now = LocalDateTime.now();
        dateLabel.setText(dateFormatter.format(java.time.LocalDate.now()));
        timeLabel.setText(timeFormatter.format(java.time.LocalTime.now()));
    }

    public void aggiornaDashboard() {
        lblTotClienti.setText(String.valueOf(clienteDAO.countClienti()));
        lblTotProdotti.setText(String.valueOf(prodottoDAO.countProdotti()));
        lblTotFornitori.setText(String.valueOf(fornitoreDAO.countFornitori()));
    }

    private void aggiornaTitoloDashboard() {

        Azienda azienda = aziendaDAO.getUltimaAzienda();

        String nomeAzienda = (azienda != null)
                ? azienda.getNome()
                : "la tua azienda";

        lblDashboardTitle.setText(
                "Dashboard aziendale di " + nomeAzienda
        );
    }

    private void caricaStatoGenerale() {
        String ultimoProdotto = prodottoDAO.getUltimoProdottoInserito();
        lblUltimoProdotto.setText("• Ultimo prodotto inserito: " + ultimoProdotto);
        String ultimoCliente = clienteDAO.getUltimoClienteInserito();
        lblUltimoCliente.setText("• Ultimo cliente registrato: " + ultimoCliente);
        String ultimoFornitore = fornitoreDAO.getUltimoFornitoreInserito();
        lblUltimoFornitore.setText("• Ultimo fornitore aggiunto: " + ultimoFornitore);
    }
}
