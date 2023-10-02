package GUI;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BillDetailGUI {
    private DefaultTableModel model;

    public BillDetailGUI() {
        initTable();
    }

    public void initTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã SP", "Tên SP", "Đơn giá", "Số lượng", "Thành tiền"};
        model.setColumnIdentifiers(cols);
        tblProducts.setModel(model);
        tblProducts.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblProducts.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        JFrame frame = new JFrame("BillDetailGUI");
        frame.setContentPane(new BillDetailGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTable tblProducts;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnReset;
    private JTextField txtTotalPrice;
}
