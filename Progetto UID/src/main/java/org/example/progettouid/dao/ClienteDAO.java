package org.example.progettouid.dao;

import org.example.progettouid.model.Cliente;
import org.example.progettouid.model.UserSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public void salvaCliente(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clienti(nome, cognome, telefono, citta, owner_mail) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCognome());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCitta());
            String email = UserSession.getInstance().getEmail();
            ps.setString(5, email);

            ps.executeUpdate();
        }
    }

    public List<Cliente> getClienti() {
        List<Cliente> clienti = new ArrayList<>();
        String sql = "SELECT * FROM clienti WHERE owner_mail = ?";
        try (Connection con = DataBaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String email = UserSession.getInstance().getEmail();
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setId(rs.getInt("id"));
                    c.setNome(rs.getString("nome"));
                    c.setCognome(rs.getString("cognome"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setCitta(rs.getString("citta"));
                    clienti.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il recupero dei clienti: " + e.getMessage());
            e.printStackTrace();
        }
        return clienti;
    }

    public void eliminaCliente(Cliente cliente) {
        String sql = "DELETE FROM clienti WHERE id = ? AND owner_mail = ?";

        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cliente.getId());
            String email = UserSession.getInstance().getEmail();
            ps.setString(2, email);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void aggiornaCliente(Cliente cliente) {
        String sql = """
        UPDATE clienti
        SET nome = ?, cognome = ?, telefono = ?, citta = ?
        WHERE id = ? AND owner_mail = ?
    """;

        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNome());
            ps.setString(2, cliente.getCognome());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCitta());
            ps.setInt(5, cliente.getId());

            String email = UserSession.getInstance().getEmail();
            ps.setString(6, email);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countClienti() {
        String sql = "SELECT COUNT(*) FROM clienti WHERE owner_mail = ?";
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

    public String getUltimoClienteInserito() {
        String sql = "SELECT nome FROM clienti ORDER BY id DESC LIMIT 1";

        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("nome");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Nessun cliente";
    }
}
