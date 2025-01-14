package Main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import com.toedter.calendar.JDateChooser;

public class Data_Barang extends javax.swing.JPanel {
    
    public Statement st;
    public ResultSet rs;
    public DefaultTableModel tabModel;
    Connection cn = Koneksi.Koneksi();
    
    private javax.swing.JLabel lb_dataBarang;
    private javax.swing.JLabel lb_pencarian;
    private javax.swing.JLabel lb_formDataBarang;
    private javax.swing.JLabel lb_kodeBarang;
    private javax.swing.JLabel lb_jenisBarang;
    private javax.swing.JLabel lb_totalHarga;
    private javax.swing.JLabel lb_status;
    private javax.swing.JTextField txt_kodeBarang;
    private javax.swing.JTextField txt_totalHarga;
    private javax.swing.JTextField txt_pencarian;
    private javax.swing.JComboBox<String> cb_jenisBarang;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_reset;
    private javax.swing.JScrollPane scroll_table;
    private Custom.TableCustom tb_dataBarang;
    private com.toedter.calendar.JDateChooser date_tglMasuk;
    
    public void judul(){
        Object[] judul = {"Kode Barang", "Jenis Barang", "Total Harga", "Tanggal Masuk"};
        
        tabModel = new DefaultTableModel(null, judul){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tb_dataBarang.setModel(tabModel);
    }
    
    public void tampilData(String where) {
        tabModel.setRowCount(0);
        try {
            st = cn.createStatement();
            String sql = "SELECT kode_barangMsk, jenis_barang, total_harga, tanggal_masuk FROM tb_barangmasuk WHERE "
                    + "kode_barangMsk LIKE '%" + txt_pencarian.getText() + "%' OR "
                    + "jenis_barang LIKE '%" + txt_pencarian.getText() + "%' OR "
                    + "total_harga LIKE '%" + txt_pencarian.getText() + "%' OR "
                    + "tanggal_masuk LIKE '%" + txt_pencarian.getText() + "%'";
            rs = st.executeQuery(sql);
            
            while (rs.next()) {
                Object[] data = {
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
    
    public void simpan() {
        try {
            if (txt_kodeBarang.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Kode Barang Masih Kosong");
            } else if (cb_jenisBarang.getSelectedItem().equals("--Pilih Jenis Barang--")) {
                JOptionPane.showMessageDialog(null, "Jenis Barang Belum Dipilih");
            } else if (txt_totalHarga.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Total Harga Masih Kosong");
            } else if (date_tglMasuk.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Tanggal Masuk Diperlukan");
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String tanggalMasuk = sdf.format(date_tglMasuk.getDate());
                
                st = cn.createStatement();
                st.executeUpdate("INSERT INTO tb_barangmasuk VALUES ('" 
                    + txt_kodeBarang.getText() + "','" 
                    + cb_jenisBarang.getSelectedItem() + "','" 
                    + txt_totalHarga.getText() + "','"
                    + tanggalMasuk + "')");
                
                tampilData("");
                reset();
                JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void edit(String where) {
        try {
            st = cn.createStatement();
            st.executeUpdate("UPDATE tb_barangmasuk SET "
                + "jenis_barang='" + cb_jenisBarang.getSelectedItem() + "',"
                + "total_harga='" + txt_totalHarga.getText() + "',"
                + "status='" + date_tglMasuk.getDate() + "' "
                + "WHERE kode_barangMsk='" + txt_kodeBarang.getText() + "'");

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
            st.executeUpdate("DELETE FROM tb_barangmasuk WHERE kode_barangMsk='"
                + tabModel.getValueAt(tb_dataBarang.getSelectedRow(), 0).toString() + "'");
            
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
        txt_kodeBarang.setText("");
        cb_jenisBarang.setSelectedIndex(0);
        txt_totalHarga.setText("");
        date_tglMasuk.setDate(null);
        
        txt_kodeBarang.setEnabled(true);
        btn_simpan.setEnabled(true);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }
    
    
    public void klikTabel(String where) {
        int selectedRow = tb_dataBarang.getSelectedRow();

        if (selectedRow != -1) {
            txt_kodeBarang.setText(tb_dataBarang.getValueAt(selectedRow, 0).toString());
            cb_jenisBarang.setSelectedItem(tb_dataBarang.getValueAt(selectedRow, 1).toString());
            txt_totalHarga.setText(tb_dataBarang.getValueAt(selectedRow, 2).toString());
            date_tglMasuk.setDate((java.util.Date) tb_dataBarang.getValueAt(selectedRow, 3));

            txt_kodeBarang.setEnabled(false);
            btn_simpan.setEnabled(false);
            btn_edit.setEnabled(true);
            btn_hapus.setEnabled(true);
        }
    }
    
    
    
    public Data_Barang() {
        initComponents();
    
        // Membuat scrollbar vertikal kustom dan menetapkannya ke scroll_table
        //scroll_table.setVerticalScrollBar(new ScrollBarCustom());
        // Membuat scrollbar horizontal kustom
//        ScrollBarCustom sbc = new ScrollBarCustom();
//        sbc.setOrientation(JScrollBar.HORIZONTAL);
//        scroll_table.setHorizontalScrollBar(sbc);
        
        //AUTO HURUF BESAR PADA TEXTFIELD NAMA LENGKAP
        txt_kodeBarang.addKeyListener(new KeyAdapter() {
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
    
    // Mendapatkan nilai yang dipilih di ComboBox kode barang
//        String selectedKode = (String) cb_kodeBarangMsk.getSelectedItem();
        
        // Menggunakan logika if-else untuk mengatur pilihan di ComboBox nama barang
//        switch (selectedKode) {
//            case "PO-1" -> cb_namaBarangMsk.setSelectedItem("AC Portable 1 PK");
//            case "PO-15" -> cb_namaBarangMsk.setSelectedItem("AC Portable 1,5 PK");
//            case "SP-1" -> cb_namaBarangMsk.setSelectedItem("AC Split 1 PK");
//            case "SP-2" -> cb_namaBarangMsk.setSelectedItem("AC Split 2 PK");
//            case "ST-3" -> cb_namaBarangMsk.setSelectedItem("AC Standing 3 PK");
//            case "ST-5" -> cb_namaBarangMsk.setSelectedItem("AC Standing 5 PK");
//            case "MC-1" -> cb_namaBarangMsk.setSelectedItem("Misty Cool");
//            default -> cb_namaBarangMsk.setSelectedItem(""); // Jika tidak ada yang cocok, kosongkan pilihan
//        }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel pn_dataBarang = new javax.swing.JPanel();
        lb_dataBarang = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        lb_pencarian = new javax.swing.JLabel();
        txt_pencarian = new javax.swing.JTextField();
        scroll_table = new javax.swing.JScrollPane();
        tb_dataBarang = new Custom.TableCustom();
        lb_formDataBarang = new javax.swing.JLabel();
        lb_kodeBarang = new javax.swing.JLabel();
        txt_kodeBarang = new javax.swing.JTextField();
        lb_jenisBarang = new javax.swing.JLabel();
        cb_jenisBarang = new javax.swing.JComboBox<>();
        lb_totalHarga = new javax.swing.JLabel();
        txt_totalHarga = new javax.swing.JTextField();
        lb_status = new javax.swing.JLabel();
        date_tglMasuk = new com.toedter.calendar.JDateChooser();

        setLayout(new java.awt.CardLayout());

        pn_dataBarang.setBackground(new java.awt.Color(255, 255, 255));
        pn_dataBarang.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lb_dataBarang.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lb_dataBarang.setText("Data Barang");
        pn_dataBarang.add(lb_dataBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

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
        pn_dataBarang.add(btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 72, 112, -1));

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
        pn_dataBarang.add(btn_edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(124, 72, -1, -1));

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
        pn_dataBarang.add(btn_hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 72, -1, -1));

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
        pn_dataBarang.add(btn_reset, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 72, -1, -1));

        lb_pencarian.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_pencarian.setText("PENCARIAN");
        pn_dataBarang.add(lb_pencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(953, 79, -1, -1));
        pn_dataBarang.add(txt_pencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(1064, 74, 200, 30));

        tb_dataBarang.setModel(new javax.swing.table.DefaultTableModel(
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
        tb_dataBarang.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tb_dataBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_dataBarangMouseClicked(evt);
            }
        });
        scroll_table.setViewportView(tb_dataBarang);

        pn_dataBarang.add(scroll_table, new org.netbeans.lib.awtextra.AbsoluteConstraints(519, 110, 745, 565));

        lb_formDataBarang.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        lb_formDataBarang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_formDataBarang.setText("Form Data Barang");
        pn_dataBarang.add(lb_formDataBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 144, 460, -1));

        lb_kodeBarang.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_kodeBarang.setText("Kode Barang");
        pn_dataBarang.add(lb_kodeBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 196, -1, -1));

        txt_kodeBarang.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_dataBarang.add(txt_kodeBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 193, 250, -1));

        lb_jenisBarang.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_jenisBarang.setText("Jenis Barang");
        pn_dataBarang.add(lb_jenisBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));

        cb_jenisBarang.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        cb_jenisBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
            "--Pilih Jenis Barang--",
            "AC Portable",
            "AC Split",
            "AC Standing",
            "Misty Fan"
        }));
        pn_dataBarang.add(cb_jenisBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 237, 250, -1));

        lb_totalHarga.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_totalHarga.setText("Total Harga");
        pn_dataBarang.add(lb_totalHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 284, -1, -1));

        txt_totalHarga.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_dataBarang.add(txt_totalHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 281, 250, -1));

        lb_status.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_status.setText("Tanggal Masuk");
        pn_dataBarang.add(lb_status, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 325, -1, 26));

        pn_dataBarang.add(date_tglMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 325, 250, 26));

        add(pn_dataBarang, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {
        simpan();
    }

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            st = cn.createStatement();
            st.executeUpdate("UPDATE tb_barangmasuk SET "
                    + "jenis_barang='" + cb_jenisBarang.getSelectedItem() + "',"
                    + "total_harga='" + txt_totalHarga.getText() + "',"
                    + "tanggal_masuk='" + new java.sql.Date(date_tglMasuk.getDate().getTime()) + "' "
                    + "WHERE kode_barangMsk='" + txt_kodeBarang.getText() + "'");
            tampilData("");
            reset();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        hapus("");
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        reset();
    }//GEN-LAST:event_btn_resetActionPerformed

    private void tb_dataBarangMouseClicked(java.awt.event.MouseEvent evt) {
        txt_kodeBarang.setText(tb_dataBarang.getValueAt(tb_dataBarang.getSelectedRow(), 0).toString());
        cb_jenisBarang.setSelectedItem(tb_dataBarang.getValueAt(tb_dataBarang.getSelectedRow(), 1).toString());
        txt_totalHarga.setText(tb_dataBarang.getValueAt(tb_dataBarang.getSelectedRow(), 2).toString());
        date_tglMasuk.setDate((java.util.Date) tb_dataBarang.getValueAt(tb_dataBarang.getSelectedRow(), 3));
        
        txt_kodeBarang.setEnabled(false);
        btn_simpan.setEnabled(false);
        btn_edit.setEnabled(true);
        btn_hapus.setEnabled(true);
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

}
