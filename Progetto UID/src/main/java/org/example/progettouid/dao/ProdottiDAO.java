package org.example.progettouid.dao;

import org.example.progettouid.model.Fornitore;
import org.example.progettouid.model.Prodotto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.progettouid.model.UserSession;
import org.springframework.security.core.parameters.P;

public class ProdottiDAO
{
    public void salvaProdotto(Prodotto prodotto){
        String SQLQuery = "INSERT INTO prodotti(nomeProdotto, prezzoUnitario, quantitaProdotto, categoria, idFornitore, dataCarico, immagine, owner_mail) " +
                "VALUES (?,?,?,?,?,?,?,?);";
        try(Connection conn = DataBaseManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQLQuery)){
            ps.setString(1,prodotto.getNomeProdotto());
            ps.setFloat(2,prodotto.getPrezzoUnitario());
            ps.setInt(3,prodotto.getQuantitaProdotto());
            ps.setString(4,prodotto.getCategoria());
            ps.setString(5,prodotto.getIdFornitore());
            ps.setString(6,prodotto.getDataCarico());
            ps.setString(7,prodotto.getImmagine());
            
            String email = UserSession.getInstance().getEmail();
            ps.setString(8,email);
            ps.executeUpdate();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Prodotto> cerca(String ValoreDaCercare){
        List<Prodotto> listaProdotti = new ArrayList<>();
        String query = "SELECT * FROM prodotti WHERE nomeProdotto LIKE ? AND owner_mail = ?;";
        try(Connection conn = DataBaseManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(query)){
            ps.setString(1, ValoreDaCercare + "%");
            String email = UserSession.getInstance().getEmail();
            ps.setString(2,email);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
                Prodotto prodotto = new Prodotto();
                prodotto.setNomeProdotto(rs.getString("nomeProdotto"));
                prodotto.setPrezzoUnitario(rs.getFloat("prezzoUnitario"));
                prodotto.setQuantitaProdotto(rs.getInt("quantitaProdotto"));
                prodotto.setCategoria(rs.getString("categoria"));
                prodotto.setIdFornitore(rs.getString("idFornitore"));
                prodotto.setDataCarico(rs.getString("dataCarico"));
                prodotto.setImmagine(rs.getString("immagine"));
                listaProdotti.add(prodotto);
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }


        return listaProdotti;

    }

    public int countProdotti() {
        String sql = "SELECT COUNT(*) FROM prodotti WHERE owner_mail = ?";
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
        }
        return 0;
    }

    public float mediaProdotti(){
        String sql = "SELECT AVG(prezzoUnitario) AS prezzo_medio FROM prodotti WHERE owner_mail = ?;";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String email = UserSession.getInstance().getEmail();
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("prezzo_medio");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    public float sommaProdotti(){
        String sql = "SELECT SUM(prezzoUnitario) AS valore_totale FROM prodotti WHERE owner_mail = ?;";
        try(Connection conn = DataBaseManager.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            
            String email = UserSession.getInstance().getEmail();
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if(rs.next()){
                    return rs.getFloat("valore_totale");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    public String getUltimoProdottoInserito() {
        String sql = "SELECT nomeProdotto FROM prodotti ORDER BY id DESC LIMIT 1";

        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("nomeProdotto");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Nessun prodotto";
    }


    public List<Prodotto> getProdotti() {
        List<Prodotto> prodotti = new ArrayList<>();
        String sql = "SELECT * FROM prodotti WHERE owner_mail = ?";
        try(Connection con = DataBaseManager.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            String email = UserSession.getInstance().getEmail();
            ps.setString(1, email);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Prodotto p = new Prodotto();
                    p.setNomeProdotto(rs.getString("nomeProdotto"));
                    p.setPrezzoUnitario(rs.getFloat("prezzoUnitario"));
                    p.setQuantitaProdotto(rs.getInt("quantitaProdotto"));
                    p.setCategoria(rs.getString("categoria"));
                    p.setIdFornitore(rs.getString("idFornitore"));
                    p.setDataCarico(rs.getString("dataCarico"));
                    p.setImmagine(rs.getString("immagine"));
                    
                    prodotti.add(p);
                }
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
        return prodotti;


    }

    public void eliminaProdotto(Prodotto prodotto) {
        String sql = "DELETE FROM prodotti WHERE nomeProdotto = ? AND prezzoUnitario = ? AND quantitaProdotto = ? AND categoria = ? AND idFornitore = ? AND dataCarico = ? AND owner_mail = ?";
        
       
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prodotto.getNomeProdotto());
            ps.setFloat(2, prodotto.getPrezzoUnitario());
            ps.setInt(3, prodotto.getQuantitaProdotto());
            ps.setString(4, prodotto.getCategoria());
            ps.setString(5, prodotto.getIdFornitore());
            ps.setString(6, prodotto.getDataCarico());

            String email = UserSession.getInstance().getEmail();
            ps.setString(7, email);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}