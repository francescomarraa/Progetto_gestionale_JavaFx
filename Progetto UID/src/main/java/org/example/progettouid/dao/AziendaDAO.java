package org.example.progettouid.dao;

import org.example.progettouid.model.Azienda;

import java.sql.*;

//questo serve solo per salvare l'azienda nel db da impostazioni

public class AziendaDAO {

    public Azienda getUltimaAzienda() {

        String sql = """
            SELECT nome, piva, indirizzo, telefono
            FROM azienda
            ORDER BY rowid DESC
            LIMIT 1
        """;

        try (Connection conn = DataBaseManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return new Azienda(
                        rs.getString("nome"),
                        rs.getString("piva"),
                        rs.getString("indirizzo"),
                        rs.getString("telefono")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // nessuna azienda salvata
    }
}
