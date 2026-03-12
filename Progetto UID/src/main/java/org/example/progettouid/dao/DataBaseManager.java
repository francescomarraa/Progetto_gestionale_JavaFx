package org.example.progettouid.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBaseManager {
    private static final String DB_URL = "jdbc:sqlite:database.db";
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(DB_URL);
    }

    public static void InizializzaProdotti(){
        String query = "CREATE TABLE IF NOT EXISTS prodotti ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nomeProdotto TEXT NOT NULL,"+
                "prezzoUnitario FLOAT NOT NULL,"+
                "quantitaProdotto INT NOT NULL,"+
                "categoria TEXT NOT NULL,"+
                "idFornitore TEXT NOT NULL,"+
                "dataCarico TEXT NOT NULL,"+
                "immagine TEXT NOT NULL,"+
                "owner_mail TEXT NOT NULL" +
                ");";

        try(Connection conn = getConnection();
            Statement smt = conn.createStatement()){
            smt.execute(query);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public static void InizializzaTabFornitori(){
        String SQLQuery =  "CREATE TABLE IF NOT EXISTS fornitori (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "cognome TEXT NOT NULL," +
                "telefono TEXT," +
                "cap TEXT," +
                "citta TEXT," +
                "via TEXT," +
                "ragione_sociale TEXT UNIQUE," +
                "partita_iva TEXT NOT NULL UNIQUE," +
                "owner_mail TEXT NOT NULL" +
                ");";

        try (Connection con = getConnection();
             Statement stato = con.createStatement()){
            stato.execute(SQLQuery);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }


    }

    public static void InizializzaTabClienti(){
        String SQLQuery =  "CREATE TABLE IF NOT EXISTS clienti (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "cognome TEXT NOT NULL," +
                "telefono TEXT," +
                "citta TEXT," +
                "owner_mail TEXT NOT NULL" +
                ");";

        try (Connection con = getConnection();
             Statement stato = con.createStatement()){
            stato.execute(SQLQuery);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void InizializzaTabAzienda(){
        String SQLQuery = "CREATE TABLE IF NOT EXISTS azienda (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "piva TEXT NOT NULL," +
                "indirizzo TEXT," +
                "telefono TEXT" +
                ");";

        try (Connection con = getConnection();
             Statement stato = con.createStatement()){
            stato.execute(SQLQuery);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}