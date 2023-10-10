package GUI;

import BUS.ExportBUS;
import BUS.ImportBUS;
import BUS.ProductBUS;
import BUS.ProductTypeBUS;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageGUI {
    private DefaultTableModel prodModel;
    private DefaultTableModel importModel;
    private DefaultTableModel exportModel;
    private TableRowSorter<DefaultTableModel> productSorter;
    private TableRowSorter<DefaultTableModel> importSorter;
    private TableRowSorter<DefaultTableModel> exportSorter;
    private ProductBUS productBUS;
    private ProductTypeBUS productTypeBUS;
    private ImportBUS importBUS;
    private ExportBUS exportBUS;
    private String employeeId;

    public StorageGUI(String employeeId) {
        productBUS = new ProductBUS();
        productTypeBUS = new ProductTypeBUS();
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

        txtSearchProd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterProduct();
            }
        });

        cbxProductType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterProduct();
            }
        });

        cbxProductQuantity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterProduct();
            }
        });

        txtSearchImport.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterImport();
            }
        });

        cbxImportStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterImport();
            }
        });

        btnFilterDateImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (importDateFrom.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày bắt đầu", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (importDateTo.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày kết thúc", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                filterImport();
            }
        });

        btnResetImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restImportData();
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
        initProductTypeComboboxData();
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

    public void restImportData() {
        cbxImportSearchType.setSelectedIndex(0);
        txtSearchImport.setText("");
        cbxImportStatus.setSelectedIndex(0);
        importDateTo.setDate(null);
        importDateFrom.setDate(null);
        importSorter.setRowFilter(null);
    }

    public void filterImport() {
        try {
            String searchType = cbxImportSearchType.getSelectedItem().toString();
            String importInfo = txtSearchImport.getText().toLowerCase();
            String importStatus = cbxImportStatus.getSelectedItem().toString().equals("Tất cả")
                    ? ""
                    : cbxImportStatus.getSelectedItem().toString();

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date dateFrom = importDateFrom.getDate() == null
                    ? formatter.parse("01-01-1970")
                    : importDateFrom.getDate();

            Date dateTo = importDateTo.getDate() == null
                    ? formatter.parse("31-12-2050")
                    : importDateTo.getDate();

            if (dateTo.compareTo(dateFrom) < 0) {
                JOptionPane.showMessageDialog(null, "Ngày kết thúc phải sau ngày bắt đầu", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            importSorter = new TableRowSorter<>(importModel);
            tblImports.setRowSorter(importSorter);

            RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                    try {
                        String rowImportId = entry.getStringValue(0).toLowerCase();
                        String rowEmployeeId = entry.getStringValue(1).toLowerCase();
                        String rowSupplierId = entry.getStringValue(2).toLowerCase();
                        Date rowDate = formatter.parse(entry.getStringValue(3));
                        String rowStatus = entry.getStringValue(5);

                        switch (searchType) {
                            case "Mã phiếu nhập":
                                return rowImportId.contains(importInfo) && rowStatus.contains(importStatus)
                                        && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0;
                            case "Mã nhân viên":
                                return rowEmployeeId.contains(importInfo) && rowStatus.contains(importStatus)
                                        && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0;
                            case "Mã nhà cung cấp":
                                return rowSupplierId.contains(importInfo) && rowStatus.contains(importStatus)
                                        && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0;
                            default:
                                return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return true;
                    }
                }
            };

            importSorter.setRowFilter(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void filterProduct() {
        String searchType = cbxProdSearchType.getSelectedItem().toString();
        String productInfo = txtSearchProd.getText().toLowerCase();
        String productType = cbxProductType.getSelectedItem().toString().equals("Tất cả")
                ? ""
                : cbxProductType.getSelectedItem().toString().toLowerCase();

        String quantityText = cbxProductQuantity.getSelectedItem().toString();
        int minQuantity = 0;
        int maxQuantity = 999999999;

        if (quantityText.equals("Dưới 50")) {
            maxQuantity = 50;
        } else if (quantityText.equals("Từ 50 đến 200")) {
            minQuantity = 50;
            maxQuantity = 200;
        } else if (quantityText.equals("Trên 200")) {
            minQuantity = 200;
        }

        int finalMinQuantity = minQuantity;
        int finalMaxQuantity = maxQuantity;

        productSorter = new TableRowSorter<>(prodModel);
        tblProducts.setRowSorter(productSorter);

        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                String rowId = entry.getStringValue(0).toLowerCase();
                String rowName = entry.getStringValue(1).toLowerCase();
                String rowType = entry.getStringValue(2).toLowerCase();
                int rowQuantity = Integer.parseInt(entry.getStringValue(3));

                switch (searchType) {
                    case "Mã sản phẩm":
                        return rowId.contains(productInfo) && rowType.contains(productType)
                                && rowQuantity > finalMinQuantity && rowQuantity <= finalMaxQuantity;
                    case "Tên sản phẩm":
                        return rowName.contains(productInfo) && rowType.contains(productType)
                                && rowQuantity > finalMinQuantity && rowQuantity <= finalMaxQuantity;
                    default:
                        return true;
                }
            }
        };

        productSorter.setRowFilter(filter);
    }

    public void initProductTypeComboboxData() {
        cbxProductType.addItem("Tất cả");
        productTypeBUS.renderToCombobox(cbxProductType);
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
    private JComboBox cbxProdSearchType;
    private JComboBox cbxProductType;
    private JComboBox cbxProductQuantity;
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
    private JComboBox cbxImportStatus;
    private JComboBox cbxExportStatus;
    private JComboBox cbxExportSearchType;
    private JTextField txtSearchExports;
    private JDateChooser importDateFrom;
    private JDateChooser importDateTo;
    private JDateChooser exportDateFrom;
    private JDateChooser exportDateTo;
}