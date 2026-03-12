package org.example.progettouid.dao;

import org.example.progettouid.model.Fornitore;
import org.example.progettouid.model.UserSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornitoreDAO {
    public void salvaFornitore(Fornitore fornitore) throws SQLException {
        String sql = "INSERT INTO fornitori(nome, cognome, telefono, cap, citta, via, ragione_sociale, partita_iva, owner_mail) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fornitore.getNome());
            ps.setString(2, fornitore.getCognome());
            ps.setString(3, fornitore.getTelefono());
            ps.setString(4, fornitore.getCAP());
            ps.setString(5, fornitore.getCittà());
            ps.setString(6, fornitore.getVia());
            ps.setString(7, fornitore.getRagioneSociale());
            ps.setString(8, fornitore.getPartitaIVA());
            
            String email = UserSession.getInstance().getEmail();
            ps.setString(9, email);
            
            ps.executeUpdate();
        }
    }

    public List<Fornitore> getFornitori() {
        List<Fornitore> fornitori = new ArrayList<>();
        String sql = "SELECT * FROM fornitori WHERE owner_mail = ?";
        try (Connection con = DataBaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            String email = UserSession.getInstance().getEmail();
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Fornitore f = new Fornitore();
                    f.setId(rs.getInt("id"));
                    f.setNome(rs.getString("nome"));
                    f.setCognome(rs.getString("cognome"));
                    f.setTelefono(rs.getString("telefono"));
                    f.setCAP(rs.getString("cap"));
                    f.setCittà(rs.getString("citta"));
                    f.setVia(rs.getString("via"));
                    f.setRagioneSociale(rs.getString("ragione_sociale"));
                    f.setPartitaIVA(rs.getString("partita_iva"));
                    fornitori.add(f);
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il recupero dei fornitori: " + e.getMessage());
            e.printStackTrace();
        }
        return fornitori;
    }


    public void eliminaFornitore(Fornitore fornitore) {
        String sql = "DELETE FROM fornitori WHERE id = ? AND owner_mail = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fornitore.getId());
            
            String email = UserSession.getInstance().getEmail();
            ps.setString(2, email);
            
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Errore durante l'eliminazione del fornitore: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void aggiornaFornitore(Fornitore fornitore) {
        String sql = """
            UPDATE fornitori
            SET nome = ?, cognome = ?, telefono = ?, cap = ?, citta = ?, via = ?, ragione_sociale = ?, partita_iva = ?
            WHERE id = ? AND owner_mail = ?
        """;

        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, fornitore.getNome());
            ps.setString(2, fornitore.getCognome());
            ps.setString(3, fornitore.getTelefono());
            ps.setString(4, fornitore.getCAP());
            ps.setString(5, fornitore.getCittà());
            ps.setString(6, fornitore.getVia());
            ps.setString(7, fornitore.getRagioneSociale());
            ps.setString(8, fornitore.getPartitaIVA());
            ps.setInt(9, fornitore.getId());

            String email = UserSession.getInstance().getEmail();
            ps.setString(10, email);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countFornitori() {
        String sql = "SELECT COUNT(*) FROM fornitori WHERE owner_mail = ?";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String email = UserSession.getInstance().getEmail();
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    public String getUltimoFornitoreInserito() {
        String sql = "SELECT nome FROM fornitori ORDER BY id DESC LIMIT 1";

        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("nome");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Nessun fornitore";
    }

}
