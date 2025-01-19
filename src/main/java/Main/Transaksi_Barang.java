package Main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Transaksi_Barang extends javax.swing.JPanel {
    public Statement st;
    public ResultSet rs;
    public DefaultTableModel tabModel;
    Connection cn = Koneksi.Koneksi();
    
    public void judul(){
        Object[] judul = {"No Transaksi", "Kode Barang", "Jenis Barang", "Total Harga", "Tanggal Masuk"};
        
        tabModel = new DefaultTableModel(null, judul){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tb_transBarang.setModel(tabModel);
    }
    
    public void tampilData(String where) {
        tabModel.setRowCount(0);
        try {
            st = cn.createStatement();
            String sql = "SELECT no_transaksiMsk, kode_barangMsk, jenis_barang, total_harga, tanggal_masuk FROM tb_transmasuk WHERE "
                    + "no_transaksiMsk LIKE '%" + txt_pencarian.getText() + "%' OR "
                    + "kode_barangMsk LIKE '%" + txt_pencarian.getText() + "%' OR "
                    + "jenis_barang LIKE '%" + txt_pencarian.getText() + "%' OR "
                    + "total_harga LIKE '%" + txt_pencarian.getText() + "%' OR "
                    + "tanggal_masuk LIKE '%" + txt_pencarian.getText() + "%'";
            rs = st.executeQuery(sql);
            
            while (rs.next()) {
                Object[] data = {
                    rs.getString("no_transaksiMsk"),
                    rs.getString("kode_barangMsk"),
                    rs.getString("jenis_barang"),
                    rs.getString("total_harga"),
                    rs.getDate("tanggal_masuk")
                };
                tabModel.addRow(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void simpan(String where) {
        try {
            // Validation
            if (txt_noTransBarang.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No Transaksi Diperlukan", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txt_kodeBarang.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Kode Barang Diperlukan", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (cb_jenisBarang.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Jenis Barang Diperlukan", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (txt_totalHarga.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Total Harga Diperlukan", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (date_tglBarang.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Tanggal Masuk Diperlukan", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Debug prints
            System.out.println("Attempting to save transaction:");
            System.out.println("No Transaksi: " + txt_noTransBarang.getText());
            System.out.println("Kode Barang: " + txt_kodeBarang.getText());
            System.out.println("Jenis Barang: " + cb_jenisBarang.getSelectedItem());
            System.out.println("Total Harga: " + txt_totalHarga.getText());
            System.out.println("Tanggal: " + date_tglBarang.getDate());

            // Insert new transaction
            String sql = "INSERT INTO tb_transmasuk (no_transaksiMsk, kode_barangMsk, " +
                        "jenis_barang, total_harga, tanggal_masuk) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = cn.prepareStatement(sql);
            
            ps.setString(1, txt_noTransBarang.getText());
            ps.setString(2, txt_kodeBarang.getText());
            ps.setString(3, cb_jenisBarang.getSelectedItem().toString());
            ps.setInt(4, Integer.parseInt(txt_totalHarga.getText().replace(",", "").replace(".", ""))); // Convert to int
            ps.setDate(5, new java.sql.Date(date_tglBarang.getDate().getTime()));
            
            int result = ps.executeUpdate();
            if (result > 0) {
                tampilData("");
                reset();
                JOptionPane.showMessageDialog(null, "Data Transaksi Berhasil Disimpan");
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menyimpan transaksi", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error SQL: " + e.getMessage());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: Total harga harus berupa angka");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    
    
    public void edit(String where){
        try {
            String noTransMsk = txt_noTransBarang.getText();
            String kodeBarangMsk = txt_kodeBarang.getText();
            String jenisBarang = cb_jenisBarang.getSelectedItem().toString();
            String totalHarga = txt_totalHarga.getText();
            java.util.Date tglMasuk = date_tglBarang.getDate();
            
            if (tglMasuk != null) {
                java.sql.Date sqlDate = new java.sql.Date(tglMasuk.getTime());
                
                st = cn.createStatement();
                st.executeUpdate("UPDATE tb_transmasuk SET "
                    + "kode_barangMsk='" + kodeBarangMsk + "', "
                    + "jenis_barang='" + jenisBarang + "', "
                    + "total_harga='" + totalHarga + "', "
                    + "tanggal_masuk='" + sqlDate + "' WHERE no_transaksiMsk='" + noTransMsk + "'");

                tampilData("");
                reset();
                JOptionPane.showMessageDialog(null, "Update Berhasil");
                
                btn_simpan.setEnabled(true);
                btn_edit.setEnabled(false);
                btn_hapus.setEnabled(false);
            }
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
            st.executeUpdate("DELETE FROM tb_transmasuk WHERE no_transaksiMsk='"
                + tabModel.getValueAt(tb_transBarang.getSelectedRow(), 0).toString() + "'");
            
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
        txt_noTransBarang.setText("");
        txt_kodeBarang.setText("");
        cb_jenisBarang.setSelectedIndex(0);
        txt_totalHarga.setText("");
        date_tglBarang.setDate(null);
        
        txt_kodeBarang.setEnabled(true);
        btn_simpan.setEnabled(true);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }
    
    
    public void klikTabel(String where){
        int selectedRow = tb_transBarang.getSelectedRow();
    
        if (selectedRow != -1) {
            txt_noTransBarang.setText(tb_transBarang.getValueAt(selectedRow, 0).toString());
            txt_kodeBarang.setText(tb_transBarang.getValueAt(selectedRow, 1).toString());
            cb_jenisBarang.setSelectedItem(tb_transBarang.getValueAt(selectedRow, 2).toString());
            txt_totalHarga.setText(tb_transBarang.getValueAt(selectedRow, 3).toString());

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String tanggalString = tb_transBarang.getValueAt(selectedRow, 4).toString();
                java.util.Date tanggal = sdf.parse(tanggalString);
                date_tglBarang.setDate(tanggal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            txt_kodeBarang.setEnabled(false);
            btn_simpan.setEnabled(false);
            btn_edit.setEnabled(true);
            btn_hapus.setEnabled(true);
        }
    }
    
    
    private void fetchItemDetails(String kodeBarang) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // First, check if the kode_barangMsk exists
            String query = "SELECT jenis_barang, total_harga, tanggal_masuk " +
                          "FROM tb_barangmasuk " +
                          "WHERE kode_barangMsk = ?";
            ps = cn.prepareStatement(query);
            ps.setString(1, kodeBarang);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                // Auto populate fields from tb_barangmasuk
                String jenisBarang = rs.getString("jenis_barang");
                String totalHarga = rs.getString("total_harga");
                java.sql.Date tanggalMasuk = rs.getDate("tanggal_masuk");
                
                // Set the values to form fields
                cb_jenisBarang.setSelectedItem(jenisBarang);
                txt_totalHarga.setText(totalHarga);
                if (tanggalMasuk != null) {
                    date_tglBarang.setDate(new java.util.Date(tanggalMasuk.getTime()));
                }
                
                System.out.println("Data found: " + jenisBarang + ", " + totalHarga); // Debug line
                JOptionPane.showMessageDialog(null, "Data barang ditemukan!");
            } else {
                JOptionPane.showMessageDialog(null, "Kode barang tidak ditemukan!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                reset();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    

    public Transaksi_Barang() {
        initComponents();
        
        judul();
        tampilData("");
        
        // Add KeyListener to txt_kodeBarang
        txt_kodeBarang.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String kodeBarang = txt_kodeBarang.getText().trim();
                    System.out.println("Enter pressed with kode: " + kodeBarang); // Debug line
                    if (!kodeBarang.isEmpty()) {
                        fetchItemDetails(kodeBarang);
                    }
                }
            }
        });
        
        // AUTO HURUF BESAR PADA TEXTFIELD
        txt_noTransBarang.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isLowerCase(c)) {
                    e.setKeyChar(Character.toUpperCase(c));
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pn_transBarang = new javax.swing.JPanel();
        lb_transBarang = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        lb_pencarian = new javax.swing.JLabel();
        txt_pencarian = new javax.swing.JTextField();
        scroll_table = new javax.swing.JScrollPane();
        tb_transBarang = new Custom.TableCustom();
        lb_formTransBarang = new javax.swing.JLabel();
        lb_kodeBarang = new javax.swing.JLabel();
        txt_kodeBarang = new javax.swing.JTextField();
        lb_namaBarang = new javax.swing.JLabel();
        cb_jenisBarang = new javax.swing.JComboBox<>();
        lb_tglBarang = new javax.swing.JLabel();
        date_tglBarang = new com.toedter.calendar.JDateChooser();
        lb_noTransaksi = new javax.swing.JLabel();
        txt_noTransBarang = new javax.swing.JTextField();
        lb_totalHarga = new javax.swing.JLabel();
        txt_totalHarga = new javax.swing.JTextField();

        setLayout(new java.awt.CardLayout());

        pn_transBarang.setBackground(new java.awt.Color(255, 255, 255));
        pn_transBarang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lb_transBarang.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lb_transBarang.setText("Transaksi Barang");
        pn_transBarang.add(lb_transBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

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
        pn_transBarang.add(btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 72, 112, -1));

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
        pn_transBarang.add(btn_edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(124, 72, -1, -1));

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
        pn_transBarang.add(btn_hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 72, -1, -1));

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
        pn_transBarang.add(btn_reset, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 72, -1, -1));

        lb_pencarian.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_pencarian.setText("PENCARIAN");
        pn_transBarang.add(lb_pencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(953, 79, -1, -1));

        txt_pencarian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_pencarianKeyReleased(evt);
            }
        });
        pn_transBarang.add(txt_pencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(1064, 74, 200, 30));

        tb_transBarang.setModel(new javax.swing.table.DefaultTableModel(
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
        tb_transBarang.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tb_transBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_transBarangMouseClicked(evt);
            }
        });
        scroll_table.setViewportView(tb_transBarang);

        pn_transBarang.add(scroll_table, new org.netbeans.lib.awtextra.AbsoluteConstraints(519, 110, 745, 564));

        lb_formTransBarang.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        lb_formTransBarang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_formTransBarang.setText("Form Transaksi Data Barang");
        pn_transBarang.add(lb_formTransBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 144, 460, -1));

        lb_kodeBarang.setFont(new java.awt.Font("Times New Roman", 1, 16));
        lb_kodeBarang.setText("Kode Barang");
        pn_transBarang.add(lb_kodeBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 196, -1, -1));

        txt_kodeBarang.setFont(new java.awt.Font("Times New Roman", 0, 16));
        pn_transBarang.add(txt_kodeBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 193, 250, -1));

        lb_namaBarang.setFont(new java.awt.Font("Times New Roman", 1, 16));
        lb_namaBarang.setText("Jenis Barang");
        pn_transBarang.add(lb_namaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));

        cb_jenisBarang.setFont(new java.awt.Font("Times New Roman", 0, 16));
        cb_jenisBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { 
            "--Pilih Jenis Barang--",
            "AC Portable 1 PK",
            "AC Portable 1,5 PK",
            "AC Split 1 PK",
            "AC Split 2 PK",
            "AC Standing 3 PK",
            "AC Standing 5 PK",
            "Misty Cool"
        }));
        pn_transBarang.add(cb_jenisBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 237, 250, -1));

        lb_tglBarang.setFont(new java.awt.Font("Times New Roman", 1, 16));
        lb_tglBarang.setText("Tanggal Masuk");
        pn_transBarang.add(lb_tglBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 284, -1, 26));

        date_tglBarang.setFont(new java.awt.Font("Times New Roman", 0, 16));
        pn_transBarang.add(date_tglBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 284, 250, 26));

        lb_noTransaksi.setFont(new java.awt.Font("Times New Roman", 1, 16));
        lb_noTransaksi.setText("No Transaksi");
        pn_transBarang.add(lb_noTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 328, -1, -1));

        txt_noTransBarang.setFont(new java.awt.Font("Times New Roman", 0, 16));
        pn_transBarang.add(txt_noTransBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 325, 250, -1));

        lb_totalHarga.setFont(new java.awt.Font("Times New Roman", 1, 16));
        lb_totalHarga.setText("Total Harga");
        pn_transBarang.add(lb_totalHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 372, -1, -1));

        txt_totalHarga.setFont(new java.awt.Font("Times New Roman", 0, 16));
        pn_transBarang.add(txt_totalHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 369, 250, -1));

        add(pn_transBarang, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        reset();
    }//GEN-LAST:event_btn_resetActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        hapus("");
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        simpan("");
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        edit("");
    }//GEN-LAST:event_btn_editActionPerformed

    private void txt_pencarianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pencarianKeyReleased
        tampilData("");
    }//GEN-LAST:event_txt_pencarianKeyReleased

    private void tb_transBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_transBarangMouseClicked
        klikTabel("");
    }//GEN-LAST:event_tb_transBarangMouseClicked

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    private boolean isKodeBarangUnique(String kodeBarang) {
        try {
            PreparedStatement ps = cn.prepareStatement(
                "SELECT COUNT(*) FROM tb_barangmasuk WHERE kode_barangMsk = ?");
            ps.setString(1, kodeBarang);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) == 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JComboBox<String> cb_jenisBarang;
    private com.toedter.calendar.JDateChooser date_tglBarang;
    private javax.swing.JLabel lb_formTransBarang;
    private javax.swing.JLabel lb_kodeBarang;
    private javax.swing.JLabel lb_namaBarang;
    private javax.swing.JLabel lb_noTransaksi;
    private javax.swing.JLabel lb_pencarian;
    private javax.swing.JLabel lb_tglBarang;
    private javax.swing.JLabel lb_totalHarga;
    private javax.swing.JLabel lb_transBarang;
    private javax.swing.JPanel pn_transBarang;
    private javax.swing.JScrollPane scroll_table;
    private Custom.TableCustom tb_transBarang;
    private javax.swing.JTextField txt_kodeBarang;
    private javax.swing.JTextField txt_noTransBarang;
    private javax.swing.JTextField txt_pencarian;
    private javax.swing.JTextField txt_totalHarga;
    // End of variables declaration//GEN-END:variables
}
