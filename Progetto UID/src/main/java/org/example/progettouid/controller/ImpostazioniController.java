package org.example.progettouid.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.progettouid.dao.DataBaseManager;
import org.example.progettouid.dao.UserDAO;
import org.example.progettouid.model.UserSession;
import org.example.progettouid.utils.EmailValidator;
import org.example.progettouid.utils.PasswordValidator;
import org.example.progettouid.utils.ThemeManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.Properties;

public class ImpostazioniController {

    @FXML private Label lblEmailAccount;
    @FXML private PasswordField txtPassword;

    @FXML private TextField txtNomeAzienda;
    @FXML private TextField txtPiva;
    @FXML private TextField txtIndirizzo;
    @FXML private TextField txtTelefono;

    @FXML private ChoiceBox<String> choiceTema;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final Path prefsPath = Paths.get(System.getProperty("user.home"), ".progettouid", "preferences.properties");

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {

        if (choiceTema != null) {
            choiceTema.getItems().setAll("Chiaro", "Scuro");

            choiceTema.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldVal, newVal) -> {
                        if (newVal != null) {
                            ThemeManager.applyTheme(choiceTema.getScene(), newVal);
                            salvaPreferenzeSuFile();
                        }
                    }
            );
        }

        caricaPreferenzeDaFile();

        UserSession session = UserSession.getInstance();
        if (session != null) {
            System.out.println("EMAIL SESSIONE: " + session.getEmail());
            lblEmailAccount.setText(session.getEmail());
        }

        initAziendaTableIfNeeded();
        caricaAziendaDaDb();

    }

    @FXML
    private void salvaAccount(ActionEvent event) {

        String passwordHash = encoder.encode(txtPassword.getText());
        String emailSessione = UserSession.getInstance().getEmail();

        boolean ok = userDAO.updatePassword(emailSessione, passwordHash);

        if(ok){
            showPopupReg(Alert.AlertType.INFORMATION, "Salvataggio completato", "Password aggiornata correttamente.");
        }else{
            showPopupReg(Alert.AlertType.ERROR, "Errore salvataggio", "Errore durante l'aggiornamento della password.");
        }

    }



    private void showPopupReg(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void salvaAzienda(ActionEvent event) {
        String nomeAzienda = safe(txtNomeAzienda);
        String piva = safe(txtPiva);
        String indirizzo = safe(txtIndirizzo);
        String telefono = safe(txtTelefono);

        if (nomeAzienda.isEmpty()) {
            showError("Azienda", "Nome azienda mancante", "Inserisci il nome dell'azienda.");
            return;
        }
        if (piva.isEmpty()) {
            showError("Azienda", "Partita IVA mancante", "Inserisci la Partita IVA.");
            return;
        }

        try {
            initAziendaTableIfNeeded();

            String sql = """
                    INSERT INTO azienda(nome, piva, indirizzo, telefono)
                    VALUES(?, ?, ?, ?)
                    ON CONFLICT(piva) DO UPDATE SET
                      nome = excluded.nome,
                      indirizzo = excluded.indirizzo,
                      telefono = excluded.telefono
                    """;

            try (Connection conn = DataBaseManager.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, nomeAzienda);
                ps.setString(2, piva);
                ps.setString(3, indirizzo);
                ps.setString(4, telefono);

                ps.executeUpdate();
            }

            showInfo("Azienda", "Salvataggio completato", "Dati aziendali salvati correttamente.");

        } catch (SQLException ex) {
            ex.printStackTrace();
            showError("Azienda", "Errore salvataggio", ex.getMessage());
        }
    }

    private void caricaAziendaDaDb() {
        try {
            initAziendaTableIfNeeded();

            String sql = "SELECT nome, piva, indirizzo, telefono FROM azienda ORDER BY rowid DESC LIMIT 1";

            try (Connection conn = DataBaseManager.getConnection();
                 Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                if (rs.next()) {
                    txtNomeAzienda.setText(nvl(rs.getString("nome")));
                    txtPiva.setText(nvl(rs.getString("piva")));
                    txtIndirizzo.setText(nvl(rs.getString("indirizzo")));
                    txtTelefono.setText(nvl(rs.getString("telefono")));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void salvaPreferenze(ActionEvent event) {
        String tema = choiceTema != null ? choiceTema.getValue() : null;

        if (tema == null) tema = "Chiaro";

        try {
            Files.createDirectories(prefsPath.getParent());

            Properties props = new Properties();
            props.setProperty("tema", tema);

            try (OutputStream os = Files.newOutputStream(prefsPath)) {
                props.store(os, "ProgettoUID preferences");
            }

            showInfo("Preferenze", "Salvataggio completato", "Preferenze salvate correttamente.");

        } catch (IOException ex) {
            ex.printStackTrace();
            showError("Preferenze", "Errore salvataggio", ex.getMessage());
        }
    }

    private void salvaPreferenzeSuFile() {
        try {
            Files.createDirectories(prefsPath.getParent());

            Properties props = new Properties();
            if (choiceTema != null)
                props.setProperty("tema", choiceTema.getValue());

            try (OutputStream out = Files.newOutputStream(prefsPath)) {
                props.store(out, "Preferenze Progetto UID");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void caricaPreferenzeDaFile() {
        if (!Files.exists(prefsPath)) return;

        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(prefsPath)) {
            props.load(in);

            String tema = props.getProperty("tema", "Chiaro");

            if (choiceTema != null) {
                choiceTema.setValue(tema);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void esportaDatabase(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Esporta database");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database SQLite (*.db)", "*.db"));
        fc.setInitialFileName("database.db");

        File target = fc.showSaveDialog(getStage(event));
        if (target == null) return;

        try {
            Path source = Paths.get("database.db").toAbsolutePath();
            Files.copy(source, target.toPath(), StandardCopyOption.REPLACE_EXISTING);

            showInfo("Database", "Esportazione completata", "Database esportato correttamente.");

        } catch (IOException ex) {
            ex.printStackTrace();
            showError("Database", "Errore esportazione", ex.getMessage());
        }
    }

    private void initAziendaTableIfNeeded() {
        String sql = """
                CREATE TABLE IF NOT EXISTS azienda (
                  nome TEXT NOT NULL,
                  piva TEXT NOT NULL UNIQUE,
                  indirizzo TEXT,
                  telefono TEXT
                );
                """;
        try (Connection conn = DataBaseManager.getConnection();
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Stage getStage(ActionEvent event) {
        return (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    }

    private String safe(TextInputControl c) {
        if (c == null || c.getText() == null) return "";
        return c.getText().trim();
    }

    private String nvl(String s) {
        return s == null ? "" : s;
    }

    private void showInfo(String header, String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String header, String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}