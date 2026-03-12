package org.example.progettouid.dao;

import org.example.progettouid.model.UserSession;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class StatisticheFornitoriDAO {

    public Map<String,Double> getTuttiFornitori(){
        Map<String,Double> statistiche = new HashMap<>();
        String sql = "SELECT f.ragione_sociale, AVG(p.prezzoUnitario) as prezzo_medio\n" +
                "FROM fornitori f\n" +
                "JOIN prodotti p ON TRIM(f.partita_iva) = TRIM(p.idFornitore)\n" +
                "WHERE f.owner_mail = ?\n" +
                "GROUP BY f.ragione_sociale\n" +
                "ORDER BY prezzo_medio ASC;";

        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String email = UserSession.getInstance().getEmail();
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String ragioneSociale = rs.getString("ragione_sociale");
                    Double prezzoMedio = rs.getDouble("prezzo_medio");
                    statistiche.put(ragioneSociale, prezzoMedio);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return statistiche;
    }


    public Map<String,Integer> getDistribuzioneProdotti(){
        Map<String,Integer> statistiche = new HashMap<>();
        String sql = "SELECT f.ragione_sociale, COUNT(p.id) as numero_prodotti\n" +
                "FROM fornitori f\n" +
                "JOIN prodotti p ON TRIM(f.partita_iva) = TRIM(p.idFornitore)\n" +
                "WHERE f.owner_mail = ?\n" +
                "GROUP BY f.ragione_sociale";


        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String email = UserSession.getInstance().getEmail();
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String ragioneSociale = rs.getString("ragione_sociale");
                    int NumeroProdotti = rs.getInt("numero_prodotti");
                    statistiche.put(ragioneSociale, NumeroProdotti);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return statistiche;
    }
}