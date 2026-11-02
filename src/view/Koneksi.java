package view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Koneksi {

    private static final String URL =
            "jdbc:mysql://localhost:3306/db_inventori";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    URL, USER, PASSWORD
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Koneksi database gagal: " + e.getMessage()
            );
            return null;
        }
    }
}