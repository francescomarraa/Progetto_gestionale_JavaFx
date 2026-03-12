package org.example.progettouid.dao;

import org.example.progettouid.model.DatabaseHelper;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class UserDAO {

    public boolean register(String email, String password) {
        String query = "INSERT INTO users (email,password) VALUES (?,?)";
        String passwordCriptata = BCrypt.hashpw(password, BCrypt.gensalt(12));
        try {
            Connection con = DatabaseHelper.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, passwordCriptata);
            stmt.executeUpdate();
            System.out.println("Registrazione OK");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean login(String email, String password) {
        String query = "SELECT password FROM users WHERE email = ?";
        try {
            Connection con = DatabaseHelper.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    return BCrypt.checkpw(password, storedHash);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePassword(String email, String newHash){
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try{
            Connection con = DatabaseHelper.getInstance().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, newHash);
            ps.setString(2, email);

            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}