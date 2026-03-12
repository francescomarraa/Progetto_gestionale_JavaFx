package org.example.progettouid.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import org.example.progettouid.model.UserSession;
import org.example.progettouid.utils.EmailValidator;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;


import java.io.IOException;

import org.example.progettouid.dao.UserDAO;
import org.example.progettouid.utils.ThemeManager;

public class LoginController {

    @FXML
    private TextField textSignEmaill;
    @FXML
    private PasswordField textSignPasswordd;
    @FXML
    private Label lblHelp;

    private final UserDAO userDAO = new UserDAO();

    public void login(ActionEvent event) {
        String email = textSignEmaill.getText();
        String password = textSignPasswordd.getText();

        if(email.isEmpty() || password.isEmpty()){
            System.out.println("Errore: Email e password non possono essere vuoti");
            showPopupLog(Alert.AlertType.ERROR, "Errore", "Email e password non possono essere vuoti!");
            return;
        }

        if(!EmailValidator.isValid(email)){
            System.out.println("Errore: Formato email non valido");
            showPopupLog(Alert.AlertType.ERROR, "Errore", "Formato email non valido!");
            return;
        }

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return userDAO.login(email, password);
            }
        };

        task.setOnSucceeded(e -> {
            boolean isAutenticato = task.getValue();

            if(isAutenticato){
                try {
                    UserSession.getInstance(email);

                    Parent root2 = FXMLLoader.load(getClass().getResource("/org/example/progettouid/Home.fxml"));
                    root2.setOpacity(0);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene newScene = new Scene(root2);
                    ThemeManager.applySavedTheme(newScene);
                    stage.setScene(newScene);
                    stage.setTitle("Home Page");
                    javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(500), root2);
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    stage.setResizable(true);
                    stage.setMaximized(true);
                    ft.play();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    showPopupLog(Alert.AlertType.ERROR, "Errore", "Impossibile caricare la Home Page.");
                }
            } else {
                System.out.println("Errore: Email o password errati");
                showPopupLog(Alert.AlertType.ERROR, "Errore", "Email o password errati!");
            }
        });

        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            ex.printStackTrace();
            showPopupLog(Alert.AlertType.ERROR, "Errore Critico", "Errore di connessione al database.");
        });

        new Thread(task).start();
    }

    public void showPopupLog(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void goToRegister(ActionEvent event) throws IOException {
        System.out.println("Vai alla pagina di registrazione!");

        Parent root = FXMLLoader.load(getClass().getResource("/org/example/progettouid/register.fxml"));

        root.setOpacity(0);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene newScene = new Scene(root);
        ThemeManager.applySavedTheme(newScene);
        stage.setScene(newScene);
        stage.setTitle("Register");
        stage.setResizable(false);

        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(500), root);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }
}