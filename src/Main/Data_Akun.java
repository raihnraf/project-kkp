package Main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;



public class Data_Akun extends javax.swing.JPanel {
    public Statement st;
    public ResultSet rs;
    public DefaultTableModel tabModel;
    Connection cn = Koneksi.Koneksi();
    
    public void judul(){
        Object[] judul = {"ID", "Nama Lengkap", "Username", "Password", "Status"};
        
        tabModel = new DefaultTableModel(null, judul){
            @Override
            public boolean isCellEditable(int row, int column) {    //CELL TABLE TIDAK BISA DI EDIT
                return false; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
            }
            
        };
        tb_dataAkun.setModel(tabModel);
    }
    
    
    public void tampilData(String where){
        tabModel.setRowCount(0);
    
        String cari = txt_pencarian.getText();
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT * FROM tb_akun WHERE "
                    + "id LIKE '%" + cari + "%' OR "
                    + "nama_lengkap LIKE '%" + cari + "%' OR "
                    + "username LIKE '%" + cari + "%' OR "
                    + "password LIKE '%" + cari + "%' OR "
                    + "status LIKE '%" + cari + "%'");

            while (rs.next()) {
                Object[] data = {
                    rs.getString("id"),
                    rs.getString("nama_lengkap"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("status")
                };
                tabModel.addRow(data);
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }
    
    
    public void simpan(String where){
        try {
            if (txt_id.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Id Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (txt_namaLengkap.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Nama Lengkap Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (txt_username.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Username Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (txt_password.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Password Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (txt_status.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Status Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                st = cn.createStatement();
                st.executeUpdate("INSERT INTO tb_akun VALUES('"
                    + txt_id.getText() + "', '"
                    + txt_namaLengkap.getText() + "', '"
                    + txt_username.getText() + "', '"
                    + txt_password.getText() + "', '"
                    + txt_status.getText() + "')");
                
                reset();
                tampilData("");
                JOptionPane.showMessageDialog(null, "Akun Berhasil Dibuat");
                  //Aktifkan tombol Simpan
                btn_edit.setEnabled(false); //Nonaktifkan tombol Update
                btn_hapus.setEnabled(false);  //Nonaktifkan tombol Hapus
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public void edit(String where){
        String id = txt_id.getText();
        String namaLengkap = txt_namaLengkap.getText();
        String username = txt_username.getText();
        String password = txt_password.getText();
        String status = txt_status.getText();

        try {
            st = cn.createStatement();
            st.executeUpdate("UPDATE tb_akun SET "
                + "nama_lengkap='" + namaLengkap + "', "
                + "username='" + username + "', "
                + "password='" + password + "', "
                + "status='" + status + "' WHERE id='" + id + "'");

            tampilData(""); // Refresh data di tabel
            reset(); // Reset input field
            JOptionPane.showMessageDialog(null, "Update Berhasil");
            //txt_id.setEnabled(false); //Nonaktifkan TextField Id
            btn_simpan.setEnabled(true);  //Aktifkan tombol Simpan
            btn_edit.setEnabled(false); //Nonaktifkan tombol Update
            btn_hapus.setEnabled(false);  //Nonaktifkan tombol Hapus
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public void hapus(String where){
        try {
        int jawab;
        
        // Tampilkan dialog konfirmasi dan simpan hasilnya
        jawab = JOptionPane.showConfirmDialog(null, "Apakah anda yakin ingin menghapus Data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        // Jika pengguna memilih "Yes"
        if (jawab == JOptionPane.YES_OPTION) {
            st = cn.createStatement();
            st.executeUpdate("DELETE FROM tb_akun WHERE id='"
                + tabModel.getValueAt(tb_dataAkun.getSelectedRow(), 0).toString() + "'");
            
            tampilData("");  // Refresh data di tabel
            reset();  // Reset input field
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
            btn_simpan.setEnabled(true);  // Aktifkan tombol Simpan
            btn_edit.setEnabled(false);  // Nonaktifkan tombol Update
            btn_hapus.setEnabled(false);  // Nonaktifkan tombol Hapus
        }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public void reset(){
        txt_id.setText("");
        txt_namaLengkap.setText("");
        txt_username.setText("");
        txt_password.setText("");
        txt_status.setText("");
        
        txt_id.setEnabled(true);
        btn_simpan.setEnabled(true);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }
    
    
    public void klikTabel(String where){
        int selectedRow = tb_dataAkun.getSelectedRow();
    
        if (selectedRow != -1) {
            System.out.println("Baris yang dipilih: " + selectedRow);  // Debug untuk memastikan baris terpilih

            txt_id.setText(tb_dataAkun.getValueAt(selectedRow, 0).toString());
            txt_namaLengkap.setText(tb_dataAkun.getValueAt(selectedRow, 1).toString());
            txt_username.setText(tb_dataAkun.getValueAt(selectedRow, 2).toString());
            txt_password.setText(tb_dataAkun.getValueAt(selectedRow, 3).toString());
            txt_status.setText(tb_dataAkun.getValueAt(selectedRow, 4).toString());
            
            txt_id.setEnabled(false); //Nonaktifkan Texfield Id
            btn_simpan.setEnabled(false); //Nonaktifkan tombol Simpan
            btn_edit.setEnabled(true);  //Aktifkan tombol Update
            btn_hapus.setEnabled(true);   //Aktifkan tombol hapus
        } else {
            System.out.println("Tidak ada baris yang dipilih");  // Debug jika tidak ada baris yang dipilih
        }
    }
    
    
    
    public Data_Akun() {
        initComponents();
        
        //AUTO HURUF BESAR PADA TEXTFIELD USERNAME
        txt_username.addKeyListener(new KeyAdapter() {
        @Override
            public void keyTyped(KeyEvent e) {
                // Mendapatkan karakter yang diketik
                char c = e.getKeyChar();
                // Jika karakter adalah huruf kecil, ubah menjadi huruf besar
                if (Character.isLowerCase(c)) {
                    e.setKeyChar(Character.toUpperCase(c));
                }
            }
        });
        
        //AUTO HURUF BESAR PADA TEXTFIELD NAMA LENGKAP
        txt_namaLengkap.addKeyListener(new KeyAdapter() {
        @Override
            public void keyTyped(KeyEvent e) {
                // Mendapatkan karakter yang diketik
                char c = e.getKeyChar();
                // Jika karakter adalah huruf kecil, ubah menjadi huruf besar
                if (Character.isLowerCase(c)) {
                    e.setKeyChar(Character.toUpperCase(c));
                }
            }
        });
        
        judul();
        tampilData("");
        
        btn_hapus.setEnabled(false);
        btn_edit.setEnabled(false);
    }
    
    
    public void hurufBesar(String where){
        int position1 = txt_namaLengkap.getCaretPosition();
        int position2 = txt_username.getCaretPosition();
        txt_namaLengkap.setText(txt_namaLengkap.getText().toUpperCase());
        txt_username.setText(txt_username.getText().toUpperCase());
        txt_namaLengkap.setCaretPosition(position1);
        txt_username.setCaretPosition(position2);
        tampilData("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pn_dataAkun = new javax.swing.JPanel();
        lb_dataAkun = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        lb_pencarian = new javax.swing.JLabel();
        txt_pencarian = new javax.swing.JTextField();
        lb_formDataAkun = new javax.swing.JLabel();
        lb_id = new javax.swing.JLabel();
        txt_id = new javax.swing.JTextField();
        lb_namaLengkap = new javax.swing.JLabel();
        txt_namaLengkap = new javax.swing.JTextField();
        lb_username = new javax.swing.JLabel();
        txt_username = new javax.swing.JTextField();
        lb_password = new javax.swing.JLabel();
        txt_password = new javax.swing.JPasswordField();
        lb_status = new javax.swing.JLabel();
        txt_status = new javax.swing.JTextField();
        scroll_table = new javax.swing.JScrollPane();
        tb_dataAkun = new Custom.TableCustom();

        setPreferredSize(new java.awt.Dimension(1210, 685));
        setLayout(new java.awt.CardLayout());

        pn_dataAkun.setBackground(new java.awt.Color(255, 255, 255));
        pn_dataAkun.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lb_dataAkun.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lb_dataAkun.setText("Data Akun");
        pn_dataAkun.add(lb_dataAkun, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        btn_simpan.setBackground(new java.awt.Color(102, 255, 102));
        btn_simpan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_simpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/iconAdd_25px.png"))); // NOI18N
        btn_simpan.setText("SIMPAN");
        btn_simpan.setBorderPainted(false);
        btn_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_simpanActionPerformed(evt);
            }
        });
        pn_dataAkun.add(btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 72, 112, -1));

        btn_edit.setBackground(new java.awt.Color(51, 204, 255));
        btn_edit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_edit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/iconUpdate_25px.png"))); // NOI18N
        btn_edit.setText("EDIT");
        btn_edit.setBorderPainted(false);
        btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editActionPerformed(evt);
            }
        });
        pn_dataAkun.add(btn_edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(124, 72, -1, -1));

        btn_hapus.setBackground(new java.awt.Color(255, 51, 0));
        btn_hapus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_hapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/iconDelete_25px.png"))); // NOI18N
        btn_hapus.setText("HAPUS");
        btn_hapus.setBorderPainted(false);
        btn_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_hapusActionPerformed(evt);
            }
        });
        pn_dataAkun.add(btn_hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 72, -1, -1));

        btn_reset.setBackground(java.awt.Color.yellow);
        btn_reset.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_reset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/iconCancel_25px.png"))); // NOI18N
        btn_reset.setText("RESET");
        btn_reset.setBorderPainted(false);
        btn_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_resetActionPerformed(evt);
            }
        });
        pn_dataAkun.add(btn_reset, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 72, -1, -1));

        lb_pencarian.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_pencarian.setText("PENCARIAN");
        pn_dataAkun.add(lb_pencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(953, 79, -1, -1));

        txt_pencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_pencarianKeyReleased(evt);
            }
        });
        pn_dataAkun.add(txt_pencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(1064, 74, 200, 30));

        lb_formDataAkun.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        lb_formDataAkun.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_formDataAkun.setText("Form Data Akun");
        pn_dataAkun.add(lb_formDataAkun, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 144, 460, -1));

        lb_id.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_id.setText("Id");
        pn_dataAkun.add(lb_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 193, 150, 26));

        txt_id.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txt_id.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_idKeyTyped(evt);
            }
        });
        pn_dataAkun.add(txt_id, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 193, 250, -1));

        lb_namaLengkap.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_namaLengkap.setText("Nama Lengkap");
        pn_dataAkun.add(lb_namaLengkap, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 237, 150, 26));

        txt_namaLengkap.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txt_namaLengkap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_namaLengkapKeyTyped(evt);
            }
        });
        pn_dataAkun.add(txt_namaLengkap, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 237, 250, -1));

        lb_username.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_username.setText("Username");
        pn_dataAkun.add(lb_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 281, 150, 26));

        txt_username.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txt_username.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_usernameKeyTyped(evt);
            }
        });
        pn_dataAkun.add(txt_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 281, 250, -1));

        lb_password.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_password.setText("Passsword");
        pn_dataAkun.add(lb_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 325, 150, 26));

        txt_password.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txt_password.setText("Password");
        txt_password.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_passwordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_passwordFocusLost(evt);
            }
        });
        pn_dataAkun.add(txt_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 325, 250, -1));

        lb_status.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_status.setText("Status");
        pn_dataAkun.add(lb_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 369, 150, 23));

        txt_status.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_dataAkun.add(txt_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 369, 250, -1));

        tb_dataAkun.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tb_dataAkun.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        tb_dataAkun.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_dataAkunMouseClicked(evt);
            }
        });
        scroll_table.setViewportView(tb_dataAkun);

        pn_dataAkun.add(scroll_table, new org.netbeans.lib.awtextra.AbsoluteConstraints(519, 110, 745, 564));

        add(pn_dataAkun, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void txt_passwordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_passwordFocusGained
        String pass = txt_password.getText();
        if(pass.equals("Password")){
            txt_password.setText("");
        }
    }//GEN-LAST:event_txt_passwordFocusGained

    private void txt_passwordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_passwordFocusLost
        String pass = txt_password.getText();
        if(pass.equals("")||pass.equals("Password")){
            txt_password.setText("Password");
        }
    }//GEN-LAST:event_txt_passwordFocusLost

    private void txt_idKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_idKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txt_idKeyTyped

    private void txt_namaLengkapKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_namaLengkapKeyTyped
        hurufBesar("");
    }//GEN-LAST:event_txt_namaLengkapKeyTyped

    private void txt_usernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_usernameKeyTyped
        hurufBesar("");
    }//GEN-LAST:event_txt_usernameKeyTyped

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        simpan("");
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        edit("");
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        hapus("");
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        reset();
    }//GEN-LAST:event_btn_resetActionPerformed

    private void txt_pencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pencarianKeyReleased
        tampilData("");
    }//GEN-LAST:event_txt_pencarianKeyReleased

    private void tb_dataAkunMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_dataAkunMouseClicked
        klikTabel("");
    }//GEN-LAST:event_tb_dataAkunMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JLabel lb_dataAkun;
    private javax.swing.JLabel lb_formDataAkun;
    private javax.swing.JLabel lb_id;
    private javax.swing.JLabel lb_namaLengkap;
    private javax.swing.JLabel lb_password;
    private javax.swing.JLabel lb_pencarian;
    private javax.swing.JLabel lb_status;
    private javax.swing.JLabel lb_username;
    private javax.swing.JPanel pn_dataAkun;
    private javax.swing.JScrollPane scroll_table;
    private Custom.TableCustom tb_dataAkun;
    private javax.swing.JTextField txt_id;
    private javax.swing.JTextField txt_namaLengkap;
    private javax.swing.JPasswordField txt_password;
    private javax.swing.JTextField txt_pencarian;
    private javax.swing.JTextField txt_status;
    private javax.swing.JTextField txt_username;
    // End of variables declaration//GEN-END:variables

}
