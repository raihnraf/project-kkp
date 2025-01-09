package Custom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class ModernScrollBarUI extends BasicScrollBarUI {

    private final int THUMB_SIZE = 40;
    
    @Override
    protected Dimension getMaximumThumbSize() {
        // Memeriksa apakah scrollbar berorientasi vertikal
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            // Jika vertikal, thumb akan memiliki lebar tetap sebesar THUMB_SIZE,
            // dan tinggi maksimum tidak terbatas (Integer.MAX_VALUE).
            return new Dimension(THUMB_SIZE, Integer.MAX_VALUE);
        } else {
            // Jika horizontal, thumb akan memiliki tinggi tetap sebesar THUMB_SIZE,
            // dan lebar maksimum tidak terbatas (Integer.MAX_VALUE).
            return new Dimension(Integer.MAX_VALUE, THUMB_SIZE);
        }
    }

    @Override
    protected Dimension getMinimumThumbSize() {
        // Memeriksa apakah scrollbar berorientasi vertikal
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            // Jika vertikal, thumb akan memiliki lebar dan tinggi minimum sebesar THUMB_SIZE.
            return new Dimension(THUMB_SIZE, THUMB_SIZE);
        } else {
            // Jika horizontal, thumb akan memiliki lebar dan tinggi minimum sebesar THUMB_SIZE.
            return new Dimension(THUMB_SIZE, THUMB_SIZE);
        }
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return new ScrollBarButton();
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return new ScrollBarButton();
    }


    
    
    @Override
    protected void paintTrack(Graphics grphcs, JComponent jc, Rectangle rctngl) {
        // Konversi objek Graphics ke Graphics2D untuk kontrol rendering yang lebih baik
        Graphics2D g2 = (Graphics2D) grphcs;
        // Mengaktifkan antialiasing untuk membuat rendering lebih halus
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Mengambil posisi dan ukuran track dari Rectangle rctngl
        int x = rctngl.x;
        int y = rctngl.y;
        int width = rctngl.width;
        int height = rctngl.height;
        // Mendapatkan orientasi scrollbar (VERTICAL atau HORIZONTAL)
        int orientation = scrollbar.getOrientation();

        if (orientation == JScrollBar.VERTICAL) {  // Jika scrollbar vertikal
            int size = rctngl.width / 2;           // Mengatur lebar track menjadi setengah dari lebar area
            x += (rctngl.width - size) / 2;        // Menggeser posisi x agar track berada di tengah secara horizontal
            width = size;                          // Mengatur lebar track
        } else {  // Jika scrollbar horizontal
            int size = rctngl.height / 2;          // Mengatur tinggi track menjadi setengah dari tinggi area
            y += (rctngl.height - size) / 2;       // Menggeser posisi y agar track berada di tengah secara vertikal
            height = size;                         // Mengatur tinggi track
        }

        // Menetapkan warna track menjadi abu-abu muda (RGB: 240, 240, 240)
        g2.setColor(new Color(240, 240, 240));
        // Menggambar persegi panjang yang mewakili track scrollbar
        g2.fillRect(x, y, width, height);
    }


    @Override
    protected void paintThumb(Graphics grphcs, JComponent jc, Rectangle rctngl) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = rctngl.x;
        int y = rctngl.y;
        int width = rctngl.width;
        int height = rctngl.height;
        int orientation = scrollbar.getOrientation();

        if (orientation == JScrollBar.VERTICAL) {  // Perbaikan dari JScrollbar ke JScrollBar
            y += 8;       // Menggeser thumb ke bawah sebesar 8px
            height -= 16; // Mengurangi tinggi thumb sebesar 16px (8px di atas dan bawah)
        } else {
            x += 8;       // Menggeser thumb ke kanan sebesar 8px
            width -= 16;  // Mengurangi lebar thumb sebesar 16px (8px di kiri dan kanan)
        }

        // Mengatur warna thumb sesuai dengan foreground dari scrollbar
        g2.setColor(scrollbar.getForeground());

        // Menggambar thumb dengan sudut-sudut yang melengkung (rounded corners)
        g2.fillRoundRect(x, y, width, height, 10, 10);
    }

    private class ScrollBarButton extends JButton {

        public ScrollBarButton(){
            setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        public void paint(Graphics grphcs){
            // Kode untuk menggambar komponen kustom pada tombol scrollbar akan ditulis di sini.
        }
    }

    
}
