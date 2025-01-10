
package Custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Table_Custom2 extends JTable {
    private int selectedRow= -1;
    
    public Table_Custom2(){
        setShowHorizontalLines(true);
        setGridColor(new Color(230,230,230));
        setRowHeight(40);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1){
                TableHeader header = new TableHeader(o + "");
                if(i1==10){
                    header.setHorizontalAlignment(JLabel.CENTER);
                }
                return header;
            }
        });
        
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object o, boolean isSelected, boolean hasFocus, int row, int column){
                Component com = super.getTableCellRendererComponent(table, o, isSelected, hasFocus, row, column);
                com.setBackground(Color.WHITE);
                setBorder(noFocusBorder);
                if(isSelected){
                    com.setForeground(new Color(15,89,140));
                    com.setBackground(new Color(204,204,204));
                }else{
                    com.setForeground(new Color(102,102,102));
                }
                return com;
            }
        });
        
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                int row = getSelectedRow();
                if(row == selectedRow){
                    clearSelection();
                    selectedRow = -1;
                }else{
                    selectedRow = row;
                }
            }
        });
    }
    
    public void addRow(Object[] row){
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(row);
    }
    
    private class TableHeader extends JLabel {
        public TableHeader(String text){
            super(text);
            setOpaque(true);
            setBackground(Color.WHITE);
            setFont(new Font("sansserif",1,12));
            setForeground(new Color(102,102,102));
            setBorder(new EmptyBorder(10,5,10,5));
        }
        
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            g.setColor(new Color(230,230,230));
            g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        }
    }
}
