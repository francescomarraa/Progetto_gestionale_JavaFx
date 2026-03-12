package org.example.progettouid.controller;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.time.LocalDate;
import org.example.progettouid.dao.UserDAO;
import org.example.progettouid.model.Fornitore;
import org.example.progettouid.controller.ListaFornitoriController;
import org.example.progettouid.dao.FornitoreDAO;
import org.example.progettouid.model.Prodotto;
import org.example.progettouid.dao.ProdottiDAO;
import java.io.File;
import java.util.List;

public class AggiungiProdottiController {

    @FXML private TextField txtCodiceProdotto;
    @FXML private TextField txtNomeProdotto;
    @FXML private TextField txtPrezzoUnitario;
    @FXML private TextField txtQuantitaProdotto;
    @FXML private TextField txtCategoria;
    @FXML private ChoiceBox<Fornitore> cbFornitore;
    @FXML private DatePicker dpDataCarico;
    @FXML private TextField txtImmagine;
    @FXML private Button btnCaricaProdotto;
    @FXML private Button btnImmagine;
    private FornitoreDAO fornitoreDAO;
    private ProdottiDAO prodottiDAO;
    private Runnable onSalvataggioCompletato;

    public void setOnSalvataggioCompletato(Runnable onSalvataggioCompletato) {
        this.onSalvataggioCompletato = onSalvataggioCompletato;
    }

    public void initialize(){
        fornitoreDAO = new FornitoreDAO();
        prodottiDAO = new ProdottiDAO();
        List<Fornitore> listaFornitori = fornitoreDAO.getFornitori();

        if(listaFornitori != null && !listaFornitori.isEmpty()){
            cbFornitore.getItems().addAll(listaFornitori);
        }
    }

    public void aggiungiProdotto(ActionEvent event){
        Fornitore fornitoreSelezionato = cbFornitore.getValue();
        String data = dpDataCarico.getValue().toString();
        if(txtCodiceProdotto.getText().isEmpty()){
            return;
        }

        if(txtNomeProdotto.getText().isEmpty()){
            return;
        }

        if(txtPrezzoUnitario.getText().isEmpty()){
            return;
        }
        if(txtQuantitaProdotto.getText().isEmpty()){
            return;
        }
        if(txtCategoria.getText().isEmpty()){
            return;
        }

        if(fornitoreSelezionato == null){
            return;
        }
        if(data == ""){
            return;
        }

        if(txtImmagine.getText().isEmpty()){
            return;
        }

        Prodotto prodotto = new Prodotto(
                txtNomeProdotto.getText(),
                txtNomeProdotto.getText(),
                Float.parseFloat(txtPrezzoUnitario.getText()),
                Integer.parseInt(txtQuantitaProdotto.getText()),
                txtCategoria.getText(),
                fornitoreSelezionato.getPartitaIVA(),
                data,
                txtImmagine.getText()
        );
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                prodottiDAO.salvaProdotto(prodotto);
                return null;
            }
        };

        task.setOnSucceeded(workerStateEvent -> {
            showPopupReg(Alert.AlertType.INFORMATION, "Prodotto Aggiunto", "Prodotto Aggiunto con successo!!");
            pulisciForm();
            if (onSalvataggioCompletato != null) {
                onSalvataggioCompletato.run();
            }
        });

        task.setOnFailed(workerStateEvent -> {
            Throwable eccezione = task.getException();
            showPopupReg(Alert.AlertType.ERROR, "Errore", "Errore durante il salvataggio: " + eccezione.getMessage());
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
    public void pulisciForm(){
        txtCodiceProdotto.clear();
        txtNomeProdotto.clear();
        txtPrezzoUnitario.clear();
        txtQuantitaProdotto.clear();
        txtCategoria.clear();
        cbFornitore.getSelectionModel().clearSelection();
        dpDataCarico.setValue(null);
        txtImmagine.clear();

    }
    @FXML
    public void apriFilePicker(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona Immagine Prodotto");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Tutte le Immagini", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("Immagini PNG", "*.png"),
                new FileChooser.ExtensionFilter("Immagini JPG", "*.jpg")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if(file != null){
            txtImmagine.setText(file.getAbsolutePath());
        }
    }

}