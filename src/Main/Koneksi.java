package Main;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    Connection koneksi;
    
    public static Connection Koneksi(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_indojaya","root","");
            return koneksi;
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
}

