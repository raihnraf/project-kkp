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
        Object[] judul = {"No Transaksi", "Kode Barang", "Nama Barang", "Jumlah Barang Masuk", "Harga Satuan", "Total Harga", "Tanggal Barang Masuk"};
        
        tabModel = new DefaultTableModel(null, judul){
            @Override
            public boolean isCellEditable(int row, int column) {    //CELL TABLE TIDAK BISA DI EDIT
                return false; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
            }
            
        };
        tb_transBarang.setModel(tabModel);
    }
    
    public void tampilData(String where){
        tabModel.setRowCount(0);
    
        String cari = txt_pencarian.getText();
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT * FROM tb_transmasuk WHERE "
                    + "no_transaksimsk LIKE '%" + cari + "%' OR "
                    + "kode_barangMsk LIKE '%" + cari + "%' OR "
                    + "nama_barangMsk LIKE '%" + cari + "%' OR "
                    + "jumlah_barangMsk LIKE '%" + cari + "%' OR "
                    + "harga_satuan LIKE '%" + cari + "%' OR "
                    + "total_harga LIKE '%" + cari + "%' OR "
                    + "tanggal_masuk LIKE '%" + cari + "%'");

            while (rs.next()) {
                Object[] data = {
                    rs.getString("no_transaksiMsk"),
                    rs.getString("kode_barangMsk"),
                    rs.getString("nama_barangMsk"),
                    rs.getString("jumlah_barangMsk"),
                    rs.getString("harga_satuan"),
                    rs.getString("total_harga"),
                    rs.getString("tanggal_masuk")
                };
                tabModel.addRow(data);
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }
    
    
    public void simpan(String where){
        try {
             if (txt_noTransBarang.getText().isEmpty()){
                JOptionPane.showMessageDialog(new JFrame(), "No Transaksi Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (txt_kodeBarang.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Kode Barang Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);    
            } else if (cb_namaBarang.getSelectedItem() == null || cb_namaBarang.getSelectedItem().toString().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Nama Barang Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (txt_noTransBarang.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Jumlah Barang Masuk Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (txt_hargaSatuan.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Harga Satuan Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (txt_totalHarga.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Total Harga Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (date_tglBarang.getDate()== null) {
                JOptionPane.showMessageDialog(new JFrame(), "Tanggal Barang Masuk Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                
                // Format tanggal
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String tanggalMasuk = sdf.format(date_tglBarang.getDate());
                
                st = cn.createStatement();
                st.executeUpdate("INSERT INTO tb_transmasuk (no_transaksiMsk, kode_barangMsk, nama_barangMsk, jumlah_barangMsk, harga_satuan, total_harga, tanggal_masuk) VALUES('"
                    + txt_noTransBarang.getText() + "', '"
                    + txt_kodeBarang.getText() + "', '"
                    + cb_namaBarang.getSelectedItem().toString() + "', '"
                    + txt_jmlBarang.getText() + "', '"
                    + txt_hargaSatuan.getText() + "', '"
                    + txt_totalHarga.getText() + "', '"
                    + tanggalMasuk + "')");
                
                reset();
                tampilData("");
                JOptionPane.showMessageDialog(null, "Data Barang Masuk Berhasil Di Simpan");
                  //Aktifkan tombol Simpan
                btn_edit.setEnabled(false); //Nonaktifkan tombol Update
                btn_hapus.setEnabled(false);  //Nonaktifkan tombol Hapus
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    public void edit(String where){
        String noTransMsk = txt_kodeBarang.getText();
        String kodeBarangMsk = txt_jmlBarang.getText();
        String namaBarangMsk = cb_namaBarang.getSelectedItem().toString();
        String jumlahBarangMsk = txt_noTransBarang.getText();
        String hargaSatuan = txt_hargaSatuan.getText();
        String totalHarga = txt_totalHarga.getText();
        java.util.Date tglBarangMsk_Util = date_tglBarang.getDate(); // Mendapatkan java.util.Date

        try {
            // Konversi dari java.util.Date ke java.sql.Date
            java.sql.Date sql_TglBarangMsk = null;
            if (tglBarangMsk_Util != null) {
                sql_TglBarangMsk = new java.sql.Date(tglBarangMsk_Util.getTime());
            }
            
            st = cn.createStatement();
            st.executeUpdate("UPDATE tb_transmasuk SET "
                + "no_transaksiMsk='" + noTransMsk + "', "
                + "kode_barangMsk='" + kodeBarangMsk + "', "
                + "nama_barangMsk='" + namaBarangMsk + "', "
                + "jumlah_barangMsk='" + jumlahBarangMsk + "', "
                + "harga_satuan='" + hargaSatuan + "', "
                + "total_harga='" + totalHarga + "', "
                + "tanggal_masuk='" + sql_TglBarangMsk + "' WHERE no_transaksiMsk='" + noTransMsk + "'");

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
        // Mengatur ulang nilai pada ComboBox dan field input
        txt_kodeBarang.setText("");
        txt_jmlBarang.setText("");
        cb_namaBarang.setSelectedItem(0); // Mengatur ComboBox ke kondisi tidak ada pilihan
        txt_noTransBarang.setText("");
        txt_hargaSatuan.setText("");
        txt_totalHarga.setText("");
        date_tglBarang.setDate(null);

        // Mengatur tombol
        txt_kodeBarang.setEnabled(true);
        txt_jmlBarang.setEnabled(true);
        btn_simpan.setEnabled(true);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }
    
    
    public void klikTabel(String where){
        int selectedRow = tb_transBarang.getSelectedRow();
    
        if (selectedRow != -1) {
            System.out.println("Baris yang dipilih: " + selectedRow);  // Debug untuk memastikan baris terpilih

            // Mengambil data dari baris yang dipilih
            txt_kodeBarang.setText(tb_transBarang.getValueAt(selectedRow, 0).toString());
            txt_jmlBarang.setText(tb_transBarang.getValueAt(selectedRow, 1).toString());
            cb_namaBarang.setSelectedItem(tb_transBarang.getValueAt(selectedRow, 2).toString());
            txt_noTransBarang.setText(tb_transBarang.getValueAt(selectedRow, 3).toString());
            txt_hargaSatuan.setText(tb_transBarang.getValueAt(selectedRow, 4).toString());
            txt_totalHarga.setText(tb_transBarang.getValueAt(selectedRow, 5).toString());
            

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String tanggalString = tb_transBarang.getValueAt(selectedRow, 6).toString();
                java.util.Date tanggal = sdf.parse(tanggalString);  // Konversi String ke Date

                // Set tanggal ke JDateChooser
                date_tglBarang.setDate(tanggal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Nonaktifkan atau aktifkan tombol dan textfield
            txt_kodeBarang.setEnabled(false); //Nonaktifkan No Transaksi
            txt_jmlBarang.setEnabled(false); // Nonaktifkan kode barang
            btn_simpan.setEnabled(false); // Nonaktifkan tombol Simpan
            btn_edit.setEnabled(true);  // Aktifkan tombol Update
            btn_hapus.setEnabled(true);   // Aktifkan tombol Hapus
        } else {
            System.out.println("Tidak ada baris yang dipilih");  // Debug jika tidak ada baris yang dipilih
        }
    }
    
    
    // Fungsi untuk menghitung total harga
    private void hitungTotalHarga() {
        try {
            int jumlah = Integer.parseInt(txt_noTransBarang.getText());
            int hargaSatuan = Integer.parseInt(txt_hargaSatuan.getText());
            int totalHarga = jumlah * hargaSatuan;
            txt_totalHarga.setText(String.valueOf(totalHarga));
        } catch (NumberFormatException ex) {
            // Jika input tidak valid (misalnya input kosong atau bukan angka), set total harga menjadi 0
            txt_totalHarga.setText("0");
        }
    }

    private void fetchItemDetails(String kodeBarang) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM tb_barangmasuk WHERE kode_barangMsk = ?";
            ps = cn.prepareStatement(query);
            ps.setString(1, kodeBarang);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                // Auto populate fields
                cb_namaBarang.setSelectedItem(rs.getString("nama_barangMsk"));
                txt_jmlBarang.setText(rs.getString("jumlah_barangMsk"));
                
                // Convert SQL date to Java date for JDateChooser
                java.sql.Date sqlDate = rs.getDate("tanggal_masuk");
                if (sqlDate != null) {
                    date_tglBarang.setDate(new java.util.Date(sqlDate.getTime()));
                }
            } else {
                JOptionPane.showMessageDialog(null, "Kode barang tidak ditemukan!");
                reset(); // Using your existing reset() method instead of resetFields()
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error mengambil data barang: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
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
                    if (!kodeBarang.isEmpty()) {
                        fetchItemDetails(kodeBarang);
                    } else {
                        // Clear the fields when kode barang is empty
                        cb_namaBarang.setSelectedIndex(0); // Reset to default "Pilih Nama Barang"
                        txt_jmlBarang.setText("");
                        date_tglBarang.setDate(null);
                    }
                }
            }
        });
        
        //AUTO HURUF BESAR PADA TEXTFIELD NAMA LENGKAP
        txt_jmlBarang.addKeyListener(new KeyAdapter() {
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
        
        
        // Tambahkan event listener pada txt_jumlahBarangMsk dan txt_hargaSatuan
        txt_noTransBarang.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                hitungTotalHarga();
            }
        });

        txt_hargaSatuan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                hitungTotalHarga();
            }
        });
        
        // Add KeyListener to txt_kodeBarang in constructor
txt_kodeBarang.addKeyListener(new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            String kodeBarang = txt_kodeBarang.getText().trim();
            if (!kodeBarang.isEmpty()) {
                fetchItemDetails(kodeBarang);
            }
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
        cb_namaBarang = new javax.swing.JComboBox<>();
        lb_jmlBarang = new javax.swing.JLabel();
        txt_jmlBarang = new javax.swing.JTextField();
        lb_tglBarang = new javax.swing.JLabel();
        date_tglBarang = new com.toedter.calendar.JDateChooser();
        lb_noTransaksi = new javax.swing.JLabel();
        txt_noTransBarang = new javax.swing.JTextField();
        lb_hargaSatuan = new javax.swing.JLabel();
        txt_hargaSatuan = new javax.swing.JTextField();
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

        lb_kodeBarang.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_kodeBarang.setText("Kode Barang");
        pn_transBarang.add(lb_kodeBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 196, -1, -1));

        txt_kodeBarang.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_transBarang.add(txt_kodeBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 193, 250, -1));

        lb_namaBarang.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_namaBarang.setText("Nama Barang");
        pn_transBarang.add(lb_namaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, -1, -1));

        cb_namaBarang.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        cb_namaBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Pilih Nama Barang--", "AC Portable 1 PK", "AC Portable 1,5 PK", "AC Split 1 PK", "AC Split 2 PK", "AC Standing 3 PK", "AC Standing 5 PK", "Misty Cool" }));
        pn_transBarang.add(cb_namaBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 237, 250, -1));

        lb_jmlBarang.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_jmlBarang.setText("Jumlah Barang");
        pn_transBarang.add(lb_jmlBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 284, -1, -1));

        txt_jmlBarang.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txt_jmlBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_jmlBarangActionPerformed(evt);
            }
        });
        pn_transBarang.add(txt_jmlBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 281, 250, -1));

        lb_tglBarang.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_tglBarang.setText("Tanggal Barang");
        pn_transBarang.add(lb_tglBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 325, -1, 26));

        date_tglBarang.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_transBarang.add(date_tglBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 325, 250, 26));

        lb_noTransaksi.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_noTransaksi.setText("No Transaksi");
        pn_transBarang.add(lb_noTransaksi, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 372, -1, -1));

        txt_noTransBarang.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_transBarang.add(txt_noTransBarang, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 369, 250, -1));

        lb_hargaSatuan.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_hargaSatuan.setText("Harga Satuan");
        pn_transBarang.add(lb_hargaSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 416, -1, -1));

        txt_hargaSatuan.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_transBarang.add(txt_hargaSatuan, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 413, 250, -1));

        lb_totalHarga.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_totalHarga.setText("Total Harga");
        pn_transBarang.add(lb_totalHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 460, -1, -1));

        txt_totalHarga.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_transBarang.add(txt_totalHarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 457, 250, -1));

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

    private void txt_jmlBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_jmlBarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_jmlBarangActionPerformed

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    private void addQuantityAndPriceListeners() {
        KeyListener calculateListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                hitungTotalHarga();
            }
        };
        
        txt_jmlBarang.addKeyListener(calculateListener);
        txt_hargaSatuan.addKeyListener(calculateListener);
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
    private javax.swing.JComboBox<String> cb_namaBarang;
    private com.toedter.calendar.JDateChooser date_tglBarang;
    private javax.swing.JLabel lb_formTransBarang;
    private javax.swing.JLabel lb_hargaSatuan;
    private javax.swing.JLabel lb_jmlBarang;
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
    private javax.swing.JTextField txt_hargaSatuan;
    private javax.swing.JTextField txt_jmlBarang;
    private javax.swing.JTextField txt_kodeBarang;
    private javax.swing.JTextField txt_noTransBarang;
    private javax.swing.JTextField txt_pencarian;
    private javax.swing.JTextField txt_totalHarga;
    // End of variables declaration//GEN-END:variables
}
