
package Main;

import java.awt.event.KeyEvent;
import java.sql.*;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class Data_Pelanggan extends javax.swing.JPanel {
    public Statement st;
    public ResultSet rs;
    public DefaultTableModel tabModel;
    Connection cn = Koneksi.Koneksi();
    
    public void judul(){
        Object[] judul = {"Kode Pelanggan", "Nama Pelanggan", "No Telepon", "Alamat", "Tanggal Sewa"};
        
        tabModel = new DefaultTableModel(null, judul){
            @Override
            public boolean isCellEditable(int row, int column) {    //CELL TABLE TIDAK BISA DI EDIT
                return false; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
            }
            
        };
        tb_dataPelanggan.setModel(tabModel);
    }
    
    
    public void tampilData(String where){
        tabModel.setRowCount(0);
    
        String cari = txt_pencarian.getText();
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT * FROM tb_pelanggan WHERE "
                    + "kode_pelanggan LIKE '%" + cari + "%' OR "
                    + "nama_pelanggan LIKE '%" + cari + "%' OR "
                    + "no_tlpPelanggan LIKE '%" + cari + "%' OR "
                    + "alamat_pelanggan LIKE '%" + cari + "%' OR "
                    + "tanggal_sewa LIKE '%" + cari + "%'");

            while (rs.next()) {
                Object[] data = {
                    rs.getString("kode_pelanggan"),
                    rs.getString("nama_pelanggan"),
                    rs.getString("no_tlpPelanggan"),
                    rs.getString("alamat_pelanggan"),
                    rs.getString("tanggal_sewa")
                };
                tabModel.addRow(data);
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }
    
    
    public void simpan(String where){
        try {
            if (txt_kodePlg.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Kode Sewa Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (txt_namaPlg.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Nama Penyewa Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (txt_noTlpPlg.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "No Telepon Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (txt_alamatPlg.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Alamat Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (date_tglSewa.getDate()== null) {
                JOptionPane.showMessageDialog(new JFrame(), "Tanggal Sewa Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                
                // Format tanggal
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String tanggalSewa = sdf.format(date_tglSewa.getDate());
                
                st = cn.createStatement();
                st.executeUpdate("INSERT INTO tb_penyewa VALUES('"
                    + txt_kodePlg.getText() + "', '"
                    + txt_namaPlg.getText() + "', '"
                    + txt_noTlpPlg.getText() + "', '"
                    + txt_alamatPlg.getText() + "', '"
                    + tanggalSewa + "')");
                
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
        String kodeSewa = txt_kodePlg.getText();
        String namaPenyewa = txt_namaPlg.getText();
        String noTeleponPnw = txt_noTlpPlg.getText();
        String alamatPnw = txt_alamatPlg.getText();
        java.util.Date tglSewa_Util = date_tglSewa.getDate(); // Mendapatkan java.util.Date

        try {
            // Konversi dari java.util.Date ke java.sql.Date
            java.sql.Date sql_TglSewa = null;
            if (tglSewa_Util != null) {
                sql_TglSewa = new java.sql.Date(tglSewa_Util.getTime());
            }
            
            st = cn.createStatement();
            st.executeUpdate("UPDATE tb_penyewa SET "
                + "kode_sewa='" + kodeSewa + "', "
                + "nama_Penyewa='" + namaPenyewa + "', "
                + "no_teleponPnw='" + noTeleponPnw + "', "
                + "alamat_penyewa='" + alamatPnw + "', "
                + "tanggal_sewa='" + sql_TglSewa + "' WHERE kode_sewa='" + kodeSewa + "'");

            tampilData(""); // Refresh data di tabel
            reset(); // Reset input field
            JOptionPane.showMessageDialog(null, "Update Berhasil");
            
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
            st.executeUpdate("DELETE FROM tb_penyewa WHERE kode_sewa='"
                + tabModel.getValueAt(tb_dataPelanggan.getSelectedRow(), 0).toString() + "'");
            
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
        txt_kodePlg.setText("");
        txt_namaPlg.setText("");
        txt_noTlpPlg.setText("");
        txt_alamatPlg.setText("");
        date_tglSewa.setDate(null);
        
        txt_kodePlg.setEnabled(true);
        btn_simpan.setEnabled(true);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }
    
    
    public void klikTabel(String where){
        int selectedRow = tb_dataPelanggan.getSelectedRow();
    
        if (selectedRow != -1) {
            System.out.println("Baris yang dipilih: " + selectedRow);  // Debug untuk memastikan baris terpilih

            // Mengambil data dari baris yang dipilih
            txt_kodePlg.setText(tb_dataPelanggan.getValueAt(selectedRow, 0).toString());
            txt_namaPlg.setText(tb_dataPelanggan.getValueAt(selectedRow, 1).toString());
            txt_noTlpPlg.setText(tb_dataPelanggan.getValueAt(selectedRow, 2).toString());
            txt_alamatPlg.setText(tb_dataPelanggan.getValueAt(selectedRow, 3).toString());

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String tanggalString = tb_dataPelanggan.getValueAt(selectedRow, 4).toString();
                java.util.Date tanggal = sdf.parse(tanggalString);  // Konversi String ke Date

                // Set tanggal ke JDateChooser
                date_tglSewa.setDate(tanggal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Nonaktifkan atau aktifkan tombol dan textfield
            txt_kodePlg.setEnabled(false); // Nonaktifkan textfield kode sewa
            btn_simpan.setEnabled(false); // Nonaktifkan tombol Simpan
            btn_edit.setEnabled(true);  // Aktifkan tombol Update
            btn_hapus.setEnabled(true);   // Aktifkan tombol Hapus
        } else {
            System.out.println("Tidak ada baris yang dipilih");  // Debug jika tidak ada baris yang dipilih
        }
    }
    
 
    
    public Data_Pelanggan() {
        initComponents();
        
        judul();
        tampilData("");
        
        btn_hapus.setEnabled(false);
        btn_edit.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox<>();
        pn_dataPelanggan = new javax.swing.JPanel();
        lb_dataPlg = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        lb_pencarian = new javax.swing.JLabel();
        txt_pencarian = new javax.swing.JTextField();
        scroll_table = new javax.swing.JScrollPane();
        tb_dataPelanggan = new Custom.TableCustom();
        lb_formDataPlg = new javax.swing.JLabel();
        lb_kodePlg = new javax.swing.JLabel();
        txt_kodePlg = new javax.swing.JTextField();
        lb_namaPlg = new javax.swing.JLabel();
        txt_namaPlg = new javax.swing.JTextField();
        lb_noTlpPlg = new javax.swing.JLabel();
        txt_noTlpPlg = new javax.swing.JTextField();
        lb_alamatPlg = new javax.swing.JLabel();
        scroll_alamat = new javax.swing.JScrollPane();
        txt_alamatPlg = new javax.swing.JTextArea();
        lb_tglSewa = new javax.swing.JLabel();
        date_tglSewa = new com.toedter.calendar.JDateChooser();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setPreferredSize(new java.awt.Dimension(1210, 685));
        setLayout(new java.awt.CardLayout());

        pn_dataPelanggan.setBackground(new java.awt.Color(255, 255, 255));
        pn_dataPelanggan.setPreferredSize(new java.awt.Dimension(1170, 680));
        pn_dataPelanggan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lb_dataPlg.setBackground(new java.awt.Color(255, 255, 255));
        lb_dataPlg.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lb_dataPlg.setText("Data Pelanggan");
        pn_dataPelanggan.add(lb_dataPlg, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

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
        pn_dataPelanggan.add(btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 72, 112, -1));

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
        pn_dataPelanggan.add(btn_edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(124, 72, -1, -1));

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
        pn_dataPelanggan.add(btn_hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 72, -1, -1));

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
        pn_dataPelanggan.add(btn_reset, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 72, -1, -1));

        lb_pencarian.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_pencarian.setText("PENCARIAN");
        pn_dataPelanggan.add(lb_pencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(953, 79, -1, -1));

        txt_pencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_pencarianKeyReleased(evt);
            }
        });
        pn_dataPelanggan.add(txt_pencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(1064, 74, 200, 30));

        tb_dataPelanggan.setModel(new javax.swing.table.DefaultTableModel(
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
        tb_dataPelanggan.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tb_dataPelanggan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_dataPelangganMouseClicked(evt);
            }
        });
        scroll_table.setViewportView(tb_dataPelanggan);

        pn_dataPelanggan.add(scroll_table, new org.netbeans.lib.awtextra.AbsoluteConstraints(519, 110, 745, 565));

        lb_formDataPlg.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        lb_formDataPlg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_formDataPlg.setText("Form Data Pelanggan");
        pn_dataPelanggan.add(lb_formDataPlg, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 144, 460, -1));

        lb_kodePlg.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_kodePlg.setText("Kode Pelanggan");
        pn_dataPelanggan.add(lb_kodePlg, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 196, -1, -1));

        txt_kodePlg.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txt_kodePlg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kodePlgActionPerformed(evt);
            }
        });
        txt_kodePlg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_kodePlgKeyTyped(evt);
            }
        });
        pn_dataPelanggan.add(txt_kodePlg, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 193, 250, -1));

        lb_namaPlg.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_namaPlg.setText("Nama Pelanggan");
        pn_dataPelanggan.add(lb_namaPlg, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));

        txt_namaPlg.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txt_namaPlg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namaPlgActionPerformed(evt);
            }
        });
        pn_dataPelanggan.add(txt_namaPlg, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 237, 250, -1));

        lb_noTlpPlg.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_noTlpPlg.setText("No Telepon");
        pn_dataPelanggan.add(lb_noTlpPlg, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 284, -1, -1));

        txt_noTlpPlg.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_dataPelanggan.add(txt_noTlpPlg, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 281, 250, -1));

        lb_alamatPlg.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_alamatPlg.setText("Alamat");
        pn_dataPelanggan.add(lb_alamatPlg, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 322, -1, -1));

        txt_alamatPlg.setColumns(20);
        txt_alamatPlg.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txt_alamatPlg.setRows(5);
        scroll_alamat.setViewportView(txt_alamatPlg);

        pn_dataPelanggan.add(scroll_alamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 325, 250, 70));

        lb_tglSewa.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_tglSewa.setText("Tanggal Sewa");
        pn_dataPelanggan.add(lb_tglSewa, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 413, -1, 26));

        date_tglSewa.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_dataPelanggan.add(date_tglSewa, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 413, 250, 26));

        add(pn_dataPelanggan, "card2");
    }// </editor-fold>//GEN-END:initComponents

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

    private void txt_kodePlgKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_kodePlgKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_txt_kodePlgKeyTyped

    private void txt_pencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pencarianKeyReleased
        tampilData("");
    }//GEN-LAST:event_txt_pencarianKeyReleased

    private void tb_dataPelangganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_dataPelangganMouseClicked
        klikTabel("");
    }//GEN-LAST:event_tb_dataPelangganMouseClicked

    private void txt_namaPlgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_namaPlgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_namaPlgActionPerformed

    private void txt_kodePlgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kodePlgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_kodePlgActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_simpan;
    private com.toedter.calendar.JDateChooser date_tglSewa;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel lb_alamatPlg;
    private javax.swing.JLabel lb_dataPlg;
    private javax.swing.JLabel lb_formDataPlg;
    private javax.swing.JLabel lb_kodePlg;
    private javax.swing.JLabel lb_namaPlg;
    private javax.swing.JLabel lb_noTlpPlg;
    private javax.swing.JLabel lb_pencarian;
    private javax.swing.JLabel lb_tglSewa;
    private javax.swing.JPanel pn_dataPelanggan;
    private javax.swing.JScrollPane scroll_alamat;
    private javax.swing.JScrollPane scroll_table;
    private Custom.TableCustom tb_dataPelanggan;
    private javax.swing.JTextArea txt_alamatPlg;
    private javax.swing.JTextField txt_kodePlg;
    private javax.swing.JTextField txt_namaPlg;
    private javax.swing.JTextField txt_noTlpPlg;
    private javax.swing.JTextField txt_pencarian;
    // End of variables declaration//GEN-END:variables
}
