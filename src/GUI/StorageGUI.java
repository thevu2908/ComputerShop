package GUI;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StorageGUI {
    private DefaultTableModel prodModel;
    private DefaultTableModel importModel;
    private DefaultTableModel exportModel;

    public StorageGUI() {
        initProduct();
        initImport();
        initExport();
    }

    public void initExport() {
        initExportDateChooser();
        initExportTable();
    }

    public void initImport() {
        initImportDateChooser();
        initImportTable();
    }


    public void initProduct() {
        initProductTable();
    }

    public void initExportTable() {
        exportModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã phiếu nhập", "Mã nhân viên", "Ngày xuất", "Tổng số lượng", "Tình trạng"};
        exportModel.setColumnIdentifiers(cols);
        tblExports.setModel(exportModel);
        tblExports.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblExports.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void initExportDateChooser() {
        exportDateFrom = new JDateChooser();
        exportDateFromPanel.add(exportDateFrom);
        exportDateTo = new JDateChooser();
        exportDateToPanel.add(exportDateTo);
    }

    public void initImportTable() {
        importModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã phiếu nhập", "Mã nhân viên", "Mã nhà cung cấp", "Ngày nhập", "Tổng tiền", "Tình trạng"};
        importModel.setColumnIdentifiers(cols);
        tblImports.setModel(importModel);
        tblImports.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblImports.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void initImportDateChooser() {
        importDateFrom = new JDateChooser();
        importDateFromPanel.add(importDateFrom);
        importDateTo = new JDateChooser();
        importDateToPanel.add(importDateTo);
    }

    public void initProductTable() {
        prodModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã sản phẩm", "Tên sản phẩm", "Hãng", "Số lượng"};
        prodModel.setColumnIdentifiers(cols);
        tblProducts.setModel(prodModel);
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
        JFrame frame = new JFrame("StorageGUI");
        frame.setContentPane(new StorageGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JComboBox cbxSearchProdType;
    private JComboBox cbxProductType;
    private JComboBox cbxProductPrice;
    private JTable tblProducts;
    private JPanel importDatePanel;
    private JPanel importDateFromPanel;
    private JPanel importDateToPanel;
    private JButton btnFilterDateImport;
    private JTextField txtSearchImport;
    private JTextField txtSearchProd;
    private JComboBox cbxImportSearchType;
    private JTable tblImports;
    private JButton btnAddImport;
    private JButton btnUpdateImport;
    private JButton btnResetImport;
    private JButton btnPrintImport;
    private JButton btnAddExport;
    private JButton btnUpdateExport;
    private JButton btnResetExport;
    private JButton btnPrintExport;
    private JTable tblExports;
    private JButton btnFilterDateExport;
    private JPanel exportDateFromPanel;
    private JPanel exportDateToPanel;
    private JDateChooser importDateFrom;
    private JDateChooser importDateTo;
    private JDateChooser exportDateFrom;
    private JDateChooser exportDateTo;
}
