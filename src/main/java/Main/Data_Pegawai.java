package Main;

//import java.sql.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ramadhani
 */
public class Data_Pegawai extends javax.swing.JPanel {
    
    public Statement st;
    public ResultSet rs;
    public DefaultTableModel tabModel;
    Connection cn = Koneksi.Koneksi();
    
    public void judul(){
        Object[] judul = {"Id Pegawai", "Nama Pegawai", "Jenis Kelamin", "Tempat Lahir", "Tanggal Lahir", "No Telepon", "Alamat"};
        
        tabModel = new DefaultTableModel(null, judul){
            @Override
            public boolean isCellEditable(int row, int column) {    //CELL TABLE TIDAK BISA DI EDIT
                return false; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
            }
            
        };
        tb_dataPegawai.setModel(tabModel);
    }
    
    public void tampilData(String where){
        tabModel.setRowCount(0);
    
        String cari = txt_pencarian.getText();
        try {
            st = cn.createStatement();
            rs = st.executeQuery("SELECT * FROM tb_pegawai WHERE "
                    + "id_pegawai LIKE '%" + cari + "%' OR "
                    + "nama_pegawai LIKE '%" + cari + "%' OR "
                    + "jenis_kelamin LIKE '%" + cari + "%' OR "
                    + "tempat_lahir LIKE '%" + cari + "%' OR "
                    + "tanggal_lahir LIKE '%" + cari + "%' OR "
                    + "no_telepon LIKE '%" + cari + "%' OR "
                    + "alamat LIKE '%" + cari + "%'");

            while (rs.next()) {
                Object[] data = {
                    rs.getString("id_pegawai"),
                    rs.getString("nama_pegawai"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("tempat_lahir"),
                    rs.getString("tanggal_lahir"),
                    rs.getString("no_telepon"),
                    rs.getString("alamat")
                };
                tabModel.addRow(data);
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }
    
    public void simpan(String where){
        try {
            if (txt_idPegawai.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Id Pegawai Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (txt_namaPegawai.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Nama Pegawai Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (cb_JKL.getSelectedItem() == null || cb_JKL.getSelectedItem().toString().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Jenis Kelamin Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (txt_tempatLahir.getText().isEmpty()) {
                JOptionPane.showMessageDialog(new JFrame(), "Tempat Lahir Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (date_tglLahir.getDate()== null) {
                JOptionPane.showMessageDialog(new JFrame(), "Tanggal Lahir Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (txt_noTelepon.getText().isEmpty ()) {
                JOptionPane.showMessageDialog(new JFrame(), "No Telepon Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            }else if (txt_alamatPegawai.getText().isEmpty ()) {
                JOptionPane.showMessageDialog(new JFrame(), "Alamat Diperlukan", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                
                // Format tanggal
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String tanggalLahir = sdf.format(date_tglLahir.getDate());
                
                st = cn.createStatement();
                st.executeUpdate("INSERT INTO tb_pegawai VALUES('"
                    + txt_idPegawai.getText() + "','"
                    + txt_namaPegawai.getText() + "', '"
                    + cb_JKL.getSelectedItem().toString() + "', '"
                    + txt_tempatLahir.getText() + "', '"
                    + tanggalLahir + "', '"
                    + txt_noTelepon.getText() + "', '"
                    + txt_alamatPegawai.getText() + "')");
                
                reset();
                tampilData("");
                JOptionPane.showMessageDialog(null, "Data Pegawai Berhasil Dibuat");
                  //Aktifkan tombol Simpan
                btn_edit.setEnabled(false); //Nonaktifkan tombol Update
                btn_hapus.setEnabled(false);  //Nonaktifkan tombol Hapus
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void edit(String where) {
        // Mendapatkan nilai dari field input
        String idPegawai = txt_idPegawai.getText();
        String namaPegawai = txt_namaPegawai.getText();
        String jenisKelamin = cb_JKL.getSelectedItem().toString();
        String tempatLahir = txt_tempatLahir.getText();
        java.util.Date tglLahir_Util = date_tglLahir.getDate(); // Mendapatkan java.util.Date
        String noTelepon = txt_noTelepon.getText();
        String alamatPegawai = txt_alamatPegawai.getText();

        try {
            // Konversi dari java.util.Date ke java.sql.Date
            java.sql.Date sql_TglLahir = null;
            if (tglLahir_Util != null) {
                sql_TglLahir = new java.sql.Date(tglLahir_Util.getTime());
            }

            // Buat statement dan eksekusi query update
            st = cn.createStatement();
            st.executeUpdate("UPDATE tb_pegawai SET "
                + "id_pegawai='" + idPegawai + "', "
                + "nama_pegawai='" + namaPegawai + "', "
                + "jenis_kelamin='" + jenisKelamin + "', "
                + "tempat_lahir='" + tempatLahir + "', "
                + "tanggal_lahir='" + sql_TglLahir + "', " // Menggunakan java.sql.Date
                + "no_telepon='" + noTelepon + "', "
                + "alamat='" + alamatPegawai + "' WHERE id_pegawai='" + idPegawai + "'");

            tampilData(""); // Refresh data di tabel
            reset(); // Reset input field
            JOptionPane.showMessageDialog(null, "Edit Berhasil");

            btn_simpan.setEnabled(true);  // Aktifkan tombol Simpan
            btn_edit.setEnabled(false);   // Nonaktifkan tombol Edit
            btn_hapus.setEnabled(false);  // Nonaktifkan tombol Hapus
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
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
            st.executeUpdate("DELETE FROM tb_pegawai WHERE id_pegawai='"
                + tabModel.getValueAt(tb_dataPegawai.getSelectedRow(), 0).toString() + "'");
            
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
        txt_idPegawai.setText("");
        txt_namaPegawai.setText("");
        cb_JKL.setSelectedItem(0);
        txt_tempatLahir.setText("");
        date_tglLahir.setDate(null);
        txt_noTelepon.setText("");
        txt_alamatPegawai.setText("");

        // Mengatur tombol
        txt_idPegawai.setEnabled(true);
        btn_simpan.setEnabled(true);
        btn_edit.setEnabled(false);
        btn_hapus.setEnabled(false);
    }

    
    public void klikTabel(String where){
        int selectedRow = tb_dataPegawai.getSelectedRow();
    
        if (selectedRow != -1) {
            System.out.println("Baris yang dipilih: " + selectedRow);  // Debug untuk memastikan baris terpilih

            // Mengambil data dari baris yang dipilih
            txt_idPegawai.setText(tb_dataPegawai.getValueAt(selectedRow, 0).toString());
            txt_namaPegawai.setText(tb_dataPegawai.getValueAt(selectedRow, 1).toString());
            cb_JKL.setSelectedItem(tb_dataPegawai.getValueAt(selectedRow, 2).toString());    
            txt_tempatLahir.setText(tb_dataPegawai.getValueAt(selectedRow, 3).toString());
            txt_noTelepon.setText(tb_dataPegawai.getValueAt(selectedRow, 5).toString());
            txt_alamatPegawai.setText(tb_dataPegawai.getValueAt(selectedRow, 6).toString());
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String tanggalString = tb_dataPegawai.getValueAt(selectedRow, 4).toString();
                java.util.Date tanggal = sdf.parse(tanggalString);  // Konversi String ke Date

                // Set tanggal ke JDateChooser
                date_tglLahir.setDate(tanggal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Nonaktifkan atau aktifkan tombol dan textfield
            txt_idPegawai.setEnabled(false); // Nonaktifkan ComboBox kode barang
            btn_simpan.setEnabled(false); // Nonaktifkan tombol Simpan
            btn_edit.setEnabled(true);  // Aktifkan tombol Update
            btn_hapus.setEnabled(true);   // Aktifkan tombol Hapus
        } else {
            System.out.println("Tidak ada baris yang dipilih");  // Debug jika tidak ada baris yang dipilih
        }
    }

    public Data_Pegawai() {
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

        pn_dataPegawai = new javax.swing.JPanel();
        lb_dataPegawai = new javax.swing.JLabel();
        btn_simpan = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_hapus = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        lb_pencarian = new javax.swing.JLabel();
        txt_pencarian = new javax.swing.JTextField();
        lb_formDataPgw = new javax.swing.JLabel();
        lb_idPegawai = new javax.swing.JLabel();
        txt_idPegawai = new javax.swing.JTextField();
        lb_namaPegawai = new javax.swing.JLabel();
        txt_namaPegawai = new javax.swing.JTextField();
        lb_JKL = new javax.swing.JLabel();
        cb_JKL = new javax.swing.JComboBox<>();
        lb_tempatLahir = new javax.swing.JLabel();
        txt_tempatLahir = new javax.swing.JTextField();
        lb_tanggalLahir = new javax.swing.JLabel();
        date_tglLahir = new com.toedter.calendar.JDateChooser();
        lb_noTelepon = new javax.swing.JLabel();
        txt_noTelepon = new javax.swing.JTextField();
        lb_alamat = new javax.swing.JLabel();
        scroll_alamat = new javax.swing.JScrollPane();
        txt_alamatPegawai = new javax.swing.JTextArea();
        scroll_table = new javax.swing.JScrollPane();
        tb_dataPegawai = new Custom.TableCustom();

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLayout(new java.awt.CardLayout());

        pn_dataPegawai.setBackground(new java.awt.Color(255, 255, 255));
        pn_dataPegawai.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_dataPegawai.setPreferredSize(new java.awt.Dimension(1270, 680));
        pn_dataPegawai.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lb_dataPegawai.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lb_dataPegawai.setText("Data Pegawai");
        pn_dataPegawai.add(lb_dataPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

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
        pn_dataPegawai.add(btn_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 72, 112, -1));

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
        pn_dataPegawai.add(btn_edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(124, 72, -1, -1));

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
        pn_dataPegawai.add(btn_hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 72, -1, -1));

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
        pn_dataPegawai.add(btn_reset, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 72, -1, -1));

        lb_pencarian.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_pencarian.setText("PENCARIAN");
        pn_dataPegawai.add(lb_pencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(953, 79, -1, -1));
        pn_dataPegawai.add(txt_pencarian, new org.netbeans.lib.awtextra.AbsoluteConstraints(1064, 74, 200, 30));

        lb_formDataPgw.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        lb_formDataPgw.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_formDataPgw.setText("Form Data Pegawai");
        pn_dataPegawai.add(lb_formDataPgw, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 144, 460, -1));

        lb_idPegawai.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_idPegawai.setText("Id Pegawai");
        pn_dataPegawai.add(lb_idPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 196, 150, -1));

        txt_idPegawai.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_dataPegawai.add(txt_idPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 193, 250, -1));

        lb_namaPegawai.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_namaPegawai.setText("Nama Pegawai");
        pn_dataPegawai.add(lb_namaPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 150, -1));

        txt_namaPegawai.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_dataPegawai.add(txt_namaPegawai, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 237, 250, -1));

        lb_JKL.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_JKL.setText("Jenis Kelamin");
        pn_dataPegawai.add(lb_JKL, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 284, 150, -1));

        cb_JKL.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        cb_JKL.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Pilih Jenis Kelamin--", "Laki-Laki", "Perempuan" }));
        pn_dataPegawai.add(cb_JKL, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 281, 250, -1));

        lb_tempatLahir.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_tempatLahir.setText("Tempat Lahir");
        pn_dataPegawai.add(lb_tempatLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 328, 150, -1));

        txt_tempatLahir.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_dataPegawai.add(txt_tempatLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 325, 250, -1));

        lb_tanggalLahir.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_tanggalLahir.setText("Tanggal Lahir");
        pn_dataPegawai.add(lb_tanggalLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 369, 150, 28));

        date_tglLahir.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_dataPegawai.add(date_tglLahir, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 369, 250, 28));

        lb_noTelepon.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_noTelepon.setText("No Telepon");
        pn_dataPegawai.add(lb_noTelepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 418, 150, -1));

        txt_noTelepon.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        pn_dataPegawai.add(txt_noTelepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 415, 250, -1));

        lb_alamat.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        lb_alamat.setText("Alamat");
        pn_dataPegawai.add(lb_alamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 459, 150, -1));

        txt_alamatPegawai.setColumns(20);
        txt_alamatPegawai.setFont(new java.awt.Font("Times New Roman", 0, 16)); // NOI18N
        txt_alamatPegawai.setRows(5);
        scroll_alamat.setViewportView(txt_alamatPegawai);

        pn_dataPegawai.add(scroll_alamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 459, 250, -1));

        tb_dataPegawai.setModel(new javax.swing.table.DefaultTableModel(
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
        tb_dataPegawai.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tb_dataPegawai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_dataPegawaiMouseClicked(evt);
            }
        });
        scroll_table.setViewportView(tb_dataPegawai);

        pn_dataPegawai.add(scroll_table, new org.netbeans.lib.awtextra.AbsoluteConstraints(519, 110, 745, 564));

        add(pn_dataPegawai, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        edit("");
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_hapusActionPerformed
        hapus("");
    }//GEN-LAST:event_btn_hapusActionPerformed

    private void btn_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resetActionPerformed
        reset();
    }//GEN-LAST:event_btn_resetActionPerformed

    private void btn_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_simpanActionPerformed
        simpan("");
    }//GEN-LAST:event_btn_simpanActionPerformed

    private void tb_dataPegawaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_dataPegawaiMouseClicked
        klikTabel("");
    }//GEN-LAST:event_tb_dataPegawaiMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_hapus;
    private javax.swing.JButton btn_reset;
    private javax.swing.JButton btn_simpan;
    private javax.swing.JComboBox<String> cb_JKL;
    private com.toedter.calendar.JDateChooser date_tglLahir;
    private javax.swing.JLabel lb_JKL;
    private javax.swing.JLabel lb_alamat;
    private javax.swing.JLabel lb_dataPegawai;
    private javax.swing.JLabel lb_formDataPgw;
    private javax.swing.JLabel lb_idPegawai;
    private javax.swing.JLabel lb_namaPegawai;
    private javax.swing.JLabel lb_noTelepon;
    private javax.swing.JLabel lb_pencarian;
    private javax.swing.JLabel lb_tanggalLahir;
    private javax.swing.JLabel lb_tempatLahir;
    private javax.swing.JPanel pn_dataPegawai;
    private javax.swing.JScrollPane scroll_alamat;
    private javax.swing.JScrollPane scroll_table;
    private Custom.TableCustom tb_dataPegawai;
    private javax.swing.JTextArea txt_alamatPegawai;
    private javax.swing.JTextField txt_idPegawai;
    private javax.swing.JTextField txt_namaPegawai;
    private javax.swing.JTextField txt_noTelepon;
    private javax.swing.JTextField txt_pencarian;
    private javax.swing.JTextField txt_tempatLahir;
    // End of variables declaration//GEN-END:variables
}
