package GUI;

import BUS.ExportBUS;
import BUS.ImportBUS;
import BUS.ProductBUS;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StorageGUI {
    private DefaultTableModel prodModel;
    private DefaultTableModel importModel;
    private DefaultTableModel exportModel;
    private ProductBUS productBUS;
    private ImportBUS importBUS;
    private ExportBUS exportBUS;
    private String employeeId;

    public StorageGUI(String employeeId) {
        productBUS = new ProductBUS();
        importBUS = new ImportBUS();
        exportBUS = new ExportBUS();
        this.employeeId = employeeId;

        initProduct();
        initImport();
        initExport();

        tblImports.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    int rowSelected = tblImports.getSelectedRow();

                    if (rowSelected >= 0) {
                        String importId = tblImports.getValueAt(rowSelected, 0).toString();
                        ImportDetailGUI importDetailGUI = new ImportDetailGUI(importId, StorageGUI.this);
                        importDetailGUI.openImportDetailGUI();
                    }
                }
            }
        });

        tblExports.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    int rowSelected = tblExports.getSelectedRow();

                    if (rowSelected >= 0) {
                        String exportID = tblExports.getValueAt(rowSelected, 0).toString();
                        ExportDetailGUI exportDetailGUI = new ExportDetailGUI(exportID, StorageGUI.this);
                        exportDetailGUI.openExportDetailGUI();
                    }
                }
            }
        });

        btnAddImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InvoiceInformationGUI addInvoiceGUI = new InvoiceInformationGUI("", "phiếu nhập", employeeId, StorageGUI.this);
                addInvoiceGUI.openInvoiceGUI();
            }
        });

        btnAddExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InvoiceInformationGUI addInvoiceGUI = new InvoiceInformationGUI("", "phiếu xuất", employeeId, StorageGUI.this);
                addInvoiceGUI.openInvoiceGUI();
            }
        });

        btnUpdateImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowSelected = tblImports.getSelectedRow();

                if (rowSelected < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn phiếu nhập muốn sửa", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String importId = tblImports.getValueAt(rowSelected, 0).toString();

                InvoiceInformationGUI editInvoiceGUI = new InvoiceInformationGUI(importId, "phiếu nhập", employeeId, StorageGUI.this);
                editInvoiceGUI.openInvoiceGUI();
            }
        });
    }

    public void initExport() {
        initExportDateChooser();
        initExportTable();
        initExportTableData();
    }

    public void initImport() {
        initImportDateChooser();
        initImportTable();
        initImportTableData();
    }

    public void initProduct() {
        initProductTable();
        initProductTableData();
    }

    public void initExportTableData() {
        exportBUS.renderToTable(exportModel, employeeId);
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

    public void initImportTableData() {
        importBUS.renderToTable(importModel, employeeId);
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

    public void initProductTableData() {
        productBUS.renderToStorageProductTable(prodModel);
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

    public JPanel getMainPanel() {
        return mainPanel;
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