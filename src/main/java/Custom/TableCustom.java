
package Custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableCustom extends JTable {

    public TableCustom() {
        // Mengatur renderer khusus untuk header tabel
        getTableHeader().setDefaultRenderer(new TableDarkHeader());
        // Mengatur ukuran preferensi untuk header tabel
        getTableHeader().setPreferredSize(new Dimension(0, 35));
        // Mengatur renderer default untuk sel tabel
        setDefaultRenderer(Object.class, new TableDarkCell());
        // Mengatur tinggi baris tabel
        setRowHeight(30);
    }

    // Kelas untuk mengatur header tabel dengan warna gelap
    private class TableDarkHeader extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
            // Mendapatkan komponen dari superclass
            Component com = super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
            // Mengatur background dan foreground untuk header tabel
            com.setBackground(new Color(30, 30, 30));
            com.setForeground(new Color(200, 200, 200));
            // Mengatur font untuk header tabel
            com.setFont(com.getFont().deriveFont(Font.BOLD, 12));
            return com;
        }
    }

    // Kelas untuk mengatur sel tabel dengan warna gelap
    private class TableDarkCell extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
            // Mendapatkan komponen dari superclass
            Component com = super.getTableCellRendererComponent(jtable, o, bln, bln1, i, i1);
            // Anda bisa menambahkan pengaturan warna sel di sini jika diperlukan
            return com;
        }
    }
}
