/*package org.example.progettouid.dao;
import org.example.progettouid.model.Prodotto;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import org.example.progettouid.model.Prodotto;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.concurrent.Task;
import java.util.List;
import org.example.progettouid.model.Prodotto;
import org.example.progettouid.dao.ProdottiDAO;


public class StatisticheProdottiDAO {
    public Map<String, Integer> getTuttiProdotti() {

        Map<String,Integer> statistiche = new HashMap<>();
        String sql = "SELECT categoria, SUM(quantitaProdotto) as totaleQuantita FROM Prodotti GROUP BY categoria";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String categoria = rs.getString("categoria");
                int quantita = rs.getInt("totaleQuantita");
                statistiche.put(categoria, quantita);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statistiche;
    }


    public Map<String, Integer> getValoreEconomici(){
        Map<String,Integer> statistiche = new HashMap<>();
        String sql = "SELECT categoria, SUM(prezzoUnitario*quantitaProdotto) as valoreTotale FROM Prodotti GROUP BY categoria";
        try(Connection con = DataBaseManager.getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery()){

            while (rs.next()){
                String categoria = rs.getString("categoria");
                int valore = rs.getInt("valoreTotale");
                statistiche.put(categoria, valore);
            }
        }catch (SQLException e){
            e.printStackTrace();

        }
        return statistiche;
    }

}*/

package org.example.progettouid.dao;
import org.example.progettouid.model.Prodotto;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import org.example.progettouid.model.Prodotto;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.concurrent.Task;
import java.util.List;
import org.example.progettouid.model.Prodotto;
import org.example.progettouid.dao.ProdottiDAO;
import org.example.progettouid.model.UserSession;


public class StatisticheProdottiDAO {
    public Map<String, Integer> getTuttiProdotti() {

        Map<String,Integer> statistiche = new HashMap<>();
        String sql = "SELECT categoria, SUM(quantitaProdotto) as totaleQuantita FROM Prodotti WHERE owner_mail = ? GROUP BY categoria";
        try (Connection conn = DataBaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, UserSession.getInstance().getEmail());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String categoria = rs.getString("categoria");
                    int quantita = rs.getInt("totaleQuantita");
                    statistiche.put(categoria, quantita);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statistiche;
    }


    public Map<String, Integer> getValoreEconomici(){
        Map<String,Integer> statistiche = new HashMap<>();
        String sql = "SELECT categoria, SUM(prezzoUnitario*quantitaProdotto) as valoreTotale FROM Prodotti WHERE owner_mail = ? GROUP BY categoria";
        try(Connection con = DataBaseManager.getConnection();
            PreparedStatement pst = con.prepareStatement(sql)){

            pst.setString(1, UserSession.getInstance().getEmail());

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()){
                    String categoria = rs.getString("categoria");
                    int valore = rs.getInt("valoreTotale");
                    statistiche.put(categoria, valore);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();

        }
        return statistiche;
    }

}