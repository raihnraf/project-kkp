
package Custom;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JScrollBar;

public class ScrollBarCustom extends JScrollBar {
    
    public ScrollBarCustom() {
        // Mengatur UI (User Interface) kustom untuk scrollbar menggunakan kelas ModernScrollBarUI.
        // Ini menentukan bagaimana scrollbar akan digambar dan berfungsi.
        setUI(new ModernScrollBarUI());
        // Mengatur ukuran preferensi scrollbar ke 8 piksel lebar dan 8 piksel tinggi.
        // Ini memberi tahu layout manager bahwa ukuran scrollbar yang diinginkan adalah 8x8 piksel.
        setPreferredSize(new Dimension(8, 8));
        // Mengatur warna foreground (biasanya warna thumb atau bagian yang dapat digeser) ke warna biru kustom.
        // Warna RGB (48, 144, 216) menghasilkan warna biru yang digunakan untuk thumb.
        setForeground(new Color(48, 144, 216));
        // Mengatur warna background scrollbar menjadi putih.
        // Warna background biasanya digunakan untuk track scrollbar.
        setBackground(Color.WHITE);
    }
    
}
