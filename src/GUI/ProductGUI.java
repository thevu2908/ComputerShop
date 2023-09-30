package GUI;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductGUI {
    private DefaultTableModel prodModel;

    public ProductGUI() {
        initTable();
    }

    public void initTable() {
        prodModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã sản phẩm", "Tên sản phẩm", "Hãng", "Giá"};
        prodModel.setColumnIdentifiers(cols);
        tblProducts.setModel(prodModel);
        tblProducts.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblProducts.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        JFrame frame = new JFrame("ProductGUI");
        frame.setContentPane(new ProductGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private JPanel contentPanel;
    private JTable tblProducts;
    private JComboBox cbSearchType;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnExportExcel;
    private JButton btnDelete;
    private JButton btnImportExcel;
    private JButton btnReset;
    private JButton btnCreateId;
    private JTextField txtProductID;
    private JTextField txtProductName;
    private JTextField txtProductPrice;
    private JComboBox cbxProductType;
    private JPanel mainPanel;
    private JButton btnViewConfig;
    private JTextField txtSearch;
}
