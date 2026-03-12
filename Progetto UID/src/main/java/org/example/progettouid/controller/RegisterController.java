package org.example.progettouid.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.concurrent.Task;

import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import javafx.scene.control.TextField;

import java.io.IOException;


import org.example.progettouid.dao.UserDAO;
import org.example.progettouid.utils.EmailValidator;
import org.example.progettouid.utils.PasswordValidator;
import org.example.progettouid.utils.ThemeManager;


public class
RegisterController {

    @FXML
    private TextField textRegEmaill;
    @FXML
    private PasswordField textRegPasswordd;


    private final UserDAO userDAO = new UserDAO();

    public void register() {
        String email = textRegEmaill.getText();
        String password = textRegPasswordd.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showPopupReg(Alert.AlertType.ERROR, "Errore", "Compila tutti i campi!");
            return;
        }

        if (!EmailValidator.isValid(email)) {
            showPopupReg(Alert.AlertType.ERROR, "Errore", "Formato email non valido!");
            return;
        }

        if (!PasswordValidator.isValid(password)) {
            showPopupReg(Alert.AlertType.ERROR, "Errore", "Password non valida!");
            return;
        }


        Task<Boolean> task = new Task<>(){
            @Override
            protected Boolean call() throws Exception {
                return userDAO.register(email, password);
            }
        };

        task.setOnSucceeded(e->{
            boolean success = task.getValue();
            if(success){
                showPopupReg(Alert.AlertType.INFORMATION, "Registrazione", "Registrazione effettuata con successo!");
            }
            else{
                showPopupReg(Alert.AlertType.ERROR, "Errore", "Utente già registrato!");
            }

        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            ex.printStackTrace();
            showPopupReg(Alert.AlertType.ERROR, "Errore Critico", "Errore di connessione al database.");
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


    @FXML
    public void backToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/progettouid/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        root.setOpacity(0);
        Scene newScene = new Scene(root);
        ThemeManager.applySavedTheme(newScene);
        stage.setScene(newScene);

        stage.setTitle("Login");
        stage.setResizable(false);

        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(400), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

}