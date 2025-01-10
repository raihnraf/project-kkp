package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private static Connection koneksi;
    
    public static Connection Koneksi() {
        if (koneksi == null) {
            try {
                String url = "jdbc:mariadb://localhost:3306/db_indojaya?useSSL=false";
                String user = "root";
                String password = "maharta123";
                
                Class.forName("org.mariadb.jdbc.Driver");
                koneksi = DriverManager.getConnection(url, user, password);
                System.out.println("Koneksi Berhasil");
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println("Error Koneksi: " + e.getMessage());
            }
        }
        return koneksi;
    }
}

