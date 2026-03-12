package org.example.progettouid.controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.progettouid.dao.ClienteDAO;
import org.example.progettouid.dao.FornitoreDAO;
import org.example.progettouid.dao.ProdottiDAO;
import org.example.progettouid.model.UserSession;
import org.example.progettouid.controller.RegisterController;
import org.example.progettouid.utils.ThemeManager;

import javax.swing.*;
import java.io.IOException;

public class HomeController {
    @FXML
    private MenuButton menuButton;

    @FXML
    private Button openChatButton;

    @FXML
    private Button btnFornitori;

    private Stage chatStage;
    @FXML
    private AnchorPane scenaPrincipale;

    @FXML
    private AnchorPane contentArea;

    @FXML
    private AnchorPane sidebar;

    @FXML
    private AnchorPane contentarea;

    private static Scene mainScene;
    private boolean sidebarVisible = true;
    private static final double SIDEBAR_WIDTH = 206;

    public void initialize() {
        UserSession sessione = UserSession.getInstance();
        loadView("/org/example/progettouid/paneHome.fxml");
        if (sessione != null) {
            String email = sessione.getEmail();
            menuButton.setText(email);
        }
    }

    public static void setMainScene(Scene scene) {
        mainScene = scene;
    }

    public static Scene getMainScene() {
        return mainScene;
    }

    @FXML
    void openChatWindow(ActionEvent event) throws IOException {
        if (chatStage == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/progettouid/PippoBot.fxml"));
            Parent root = loader.load();

            chatStage = new Stage();
            chatStage.setTitle("PippoBot - Assistente IA");
            chatStage.setScene(new Scene(root));

            chatStage.setOnCloseRequest(e -> chatStage = null);
        }

        if (!chatStage.isShowing()) {
            chatStage.show();
        } else {
            chatStage.toFront();
        }
    }

    @FXML
    public void chiudiSessione(ActionEvent e){
        UserSession.cleanUserSession();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/progettouid/login.fxml"));
            Stage stage = (Stage)menuButton.getScene().getWindow();
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
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void loadView(String fxmlPath) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);

            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleSidebar() {

        TranslateTransition sidebarAnim = new TranslateTransition(Duration.millis(250), sidebar);
        if (sidebarVisible) {
            sidebarAnim.setToX(-SIDEBAR_WIDTH);
            AnchorPane.setLeftAnchor(contentArea, 0.0);
        } else {
            sidebarAnim.setToX(0);
            AnchorPane.setLeftAnchor(contentArea, SIDEBAR_WIDTH);
        }
        sidebarVisible = !sidebarVisible;
        sidebar.setMouseTransparent(!sidebarVisible);
        sidebarAnim.play();
    }


    @FXML
    private void apriHome() {
        loadView("/org/example/progettouid/paneHome.fxml");
    }

    @FXML
    public void mostraProdotti(ActionEvent event) throws IOException {
        loadView("/org/example/progettouid/paneAggiungiProdotti.fxml");
    }
    @FXML
    public void apriFornitore(ActionEvent event) throws IOException{
        loadView("/org/example/progettouid/paneFornitori.fxml");
    }

    @FXML
    public void apriClienti(ActionEvent event) throws IOException{
        loadView("/org/example/progettouid/paneCliente.fxml");
    }

    @FXML
    public void apriImpostazioni(ActionEvent event) throws IOException{
        loadView("/org/example/progettouid/impostazioni.fxml");
    }

    @FXML
    public void apriStatistiche(ActionEvent event) throws  IOException{
        loadView("/org/example/progettouid/statistiche.fxml");
    }
    
    @FXML
    public void apriMagazzino(ActionEvent event) throws IOException{
        loadView("/org/example/progettouid/paneMagazzino.fxml");
    }

    @FXML
    public void apriUtilita(ActionEvent event) throws IOException{
        loadView("/org/example/progettouid/Utilita.fxml");
    }
}