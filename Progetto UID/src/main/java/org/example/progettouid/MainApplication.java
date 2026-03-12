package org.example.progettouid;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.progettouid.dao.DataBaseManager;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        DataBaseManager.InizializzaTabFornitori();
        DataBaseManager.InizializzaProdotti();
        DataBaseManager.InizializzaTabClienti();
        DataBaseManager.InizializzaTabAzienda();

        
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 719, 481);

        stage.setScene(scene);
        stage.setTitle("OmniManage");
        stage.getIcons().add(
                new Image(getClass().getResourceAsStream("/org/example/images/logoUID2.png"))
        );
        stage.setResizable(false);
        stage.show();
    }
}