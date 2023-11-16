package GUI;

import BUS.*;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageGUI {
    private DefaultTableModel prodModel;
    private DefaultTableModel importModel;
    private DefaultTableModel exportModel;
    private TableRowSorter<DefaultTableModel> productSorter;
    private TableRowSorter<DefaultTableModel> importSorter;
    private TableRowSorter<DefaultTableModel> exportSorter;
    private String employeeId;
    private ProductBUS productBUS;
    private ProductTypeBUS productTypeBUS;
    private ImportBUS importBUS;
    private ImportDetailBUS importDetailBUS;
    private ExportBUS exportBUS;
    private ExportDetailBUS exportDetailBUS;
    private EmployeeBUS employeeBUS;

    public StorageGUI(String employeeId) {
        this.employeeId = employeeId;
        productBUS = new ProductBUS();
        productTypeBUS = new ProductTypeBUS();
        importBUS = new ImportBUS();
        importDetailBUS = new ImportDetailBUS();
        exportBUS = new ExportBUS();
        exportDetailBUS = new ExportDetailBUS();
        employeeBUS = new EmployeeBUS();
        showHideConfirmButton();
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
                AddInvoiceGUI addInvoiceGUI = new AddInvoiceGUI("", "phiếu nhập", employeeId, StorageGUI.this);
                addInvoiceGUI.openInvoiceGUI();
            }
        });

        btnAddExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddInvoiceGUI addInvoiceGUI = new AddInvoiceGUI("", "phiếu xuất", employeeId, StorageGUI.this);
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

                AddInvoiceGUI editInvoiceGUI = new AddInvoiceGUI(importId, "phiếu nhập", employeeId, StorageGUI.this);
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
                resetImportData();
            }
        });

        txtSearchExport.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterExport();
            }
        });

        cbxExportStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterExport();
            }
        });

        btnFilterDateExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (exportDateFrom.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày bắt đầu", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (exportDateTo.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày kết thúc", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                filterExport();
            }
        });

        btnResetExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetExportData();
            }
        });

        btnConfirmImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowSelected = tblImports.getSelectedRow();

                if (rowSelected < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn phiếu nhập muốn duyệt", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn duyệt phiếu nhập này ?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    String importId = tblImports.getValueAt(rowSelected, 0).toString();
                    if (importDetailBUS.confirmImport(importId)) {
                        initImportTableData();
                        initProductTableData();
                    }
                }
            }
        });

        btnConfirmExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowSelected = tblExports.getSelectedRow();

                if (rowSelected < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn phiếu xuất muốn duyệt", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn duyệt phiếu xuất này ?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    String exportId = tblExports.getValueAt(rowSelected, 0).toString();
                    if (exportDetailBUS.confirmExport(exportId)) {
                        initExportTableData();
                        initProductTableData();
                    }
                }
            }
        });

        btnPrintImport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblImports.getSelectedRow();

                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn phiếu nhập muốn in", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String importId = tblImports.getValueAt(selectedRow, 0).toString();
                JFileChooser fileChooser = new JFileChooser();

                String defaultFileName = importId + ".pdf";
                fileChooser.setSelectedFile(new File(defaultFileName));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files (.pdf)", "pdf");
                fileChooser.setFileFilter(filter);

                int res = fileChooser.showSaveDialog(null);

                if (res == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getPath();
                    importDetailBUS.printImport(importId, path);
                }
            }
        });

        btnPrintExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblExports.getSelectedRow();

                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn phiếu xuất muốn in", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String exportId = tblExports.getValueAt(selectedRow, 0).toString();
                JFileChooser fileChooser = new JFileChooser();

                String defaultFileName = exportId + ".pdf";
                fileChooser.setSelectedFile(new File(defaultFileName));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files (.pdf)", "pdf");
                fileChooser.setFileFilter(filter);

                int res = fileChooser.showSaveDialog(null);

                if (res == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getPath();
                    exportDetailBUS.printExport(exportId, path);
                }
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
        initProductTypeComboBoxData();
    }

    public void resetExportData() {
        cbxExportSearchType.setSelectedIndex(0);
        txtSearchExport.setText("");
        cbxExportStatus.setSelectedIndex(0);
        exportDateFrom.setDate(null);
        exportDateTo.setDate(null);
        if (exportSorter != null) {
            exportSorter.setRowFilter(null);
        }
    }

    public void filterExport() {
        try {
            String searchType = cbxExportSearchType.getSelectedItem().toString();
            String importInfo = txtSearchExport.getText().toLowerCase();
            String importStatus = cbxExportStatus.getSelectedItem() == null
                    || cbxExportStatus.getSelectedItem().toString().equals("Tất cả")
                    ? ""
                    : cbxExportStatus.getSelectedItem().toString();

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date dateFrom = exportDateFrom.getDate() == null
                    ? formatter.parse("01-01-1970")
                    : formatter.parse(formatter.format(exportDateFrom.getDate()));

            Date dateTo = exportDateTo.getDate() == null
                    ? formatter.parse("31-12-2050")
                    : formatter.parse(formatter.format(exportDateTo.getDate()));

            if (dateTo.compareTo(dateFrom) < 0) {
                JOptionPane.showMessageDialog(null, "Ngày kết thúc phải sau ngày bắt đầu", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            exportSorter = new TableRowSorter<>(exportModel);
            tblExports.setRowSorter(exportSorter);

            RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                    try {
                        String rowImportId = entry.getStringValue(0).toLowerCase();
                        String rowEmployeeId = entry.getStringValue(1).toLowerCase();
                        Date rowDate = formatter.parse(entry.getStringValue(2));
                        String rowStatus = entry.getStringValue(4);

                        switch (searchType) {
                            case "Mã phiếu xuất":
                                return rowImportId.contains(importInfo) && rowStatus.contains(importStatus)
                                        && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0;
                            case "Mã nhân viên":
                                return rowEmployeeId.contains(importInfo) && rowStatus.contains(importStatus)
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

            exportSorter.setRowFilter(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        String[] cols = {"Mã phiếu xuất", "Mã nhân viên", "Ngày lập", "Tổng số lượng", "Tình trạng"};
        exportModel.setColumnIdentifiers(cols);
        tblExports.setModel(exportModel);
        tblExports.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));
        tblExports.getTableHeader().setBackground(new Color(86, 132, 242));
        tblExports.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblExports.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void initExportDateChooser() {
        exportDateFrom = new JDateChooser();
        exportDateFrom.setDateFormatString("dd-MM-yyyy");
        exportDateFromPanel.add(exportDateFrom);

        exportDateTo = new JDateChooser();
        exportDateTo.setDateFormatString("dd-MM-yyyy");
        exportDateToPanel.add(exportDateTo);
    }

    public void resetImportData() {
        cbxImportSearchType.setSelectedIndex(0);
        txtSearchImport.setText("");
        cbxImportStatus.setSelectedIndex(0);
        importDateFrom.setDate(null);
        importDateTo.setDate(null);
        if (importSorter != null) {
            importSorter.setRowFilter(null);
        }
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
                    : formatter.parse(formatter.format(importDateFrom.getDate()));

            Date dateTo = importDateTo.getDate() == null
                    ? formatter.parse("31-12-2050")
                    : formatter.parse(formatter.format(importDateTo.getDate()));

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

        String[] cols = {"Mã phiếu nhập", "Mã nhân viên", "Mã nhà cung cấp", "Ngày lập", "Tổng tiền", "Tình trạng"};
        importModel.setColumnIdentifiers(cols);
        tblImports.setModel(importModel);
        tblImports.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));
        tblImports.getTableHeader().setBackground(new Color(86, 132, 242));
        tblImports.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblImports.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void initImportDateChooser() {
        importDateFrom = new JDateChooser();
        importDateFrom.setDateFormatString("dd-MM-yyyy");
        importDateFromPanel.add(importDateFrom);

        importDateTo = new JDateChooser();
        importDateTo.setDateFormatString("dd-MM-yyyy");
        importDateToPanel.add(importDateTo);
    }

    public void filterProduct() {
        String searchType = cbxProdSearchType.getSelectedItem().toString();
        String productInfo = txtSearchProd.getText().toLowerCase();
        String productType = cbxProductType.getSelectedItem() == null
                || cbxProductType.getSelectedItem().toString().equals("Tất cả")
                ? ""
                : cbxProductType.getSelectedItem().toString().toLowerCase();

        String quantityText = cbxProductQuantity.getSelectedItem().toString();
        int minQuantity = 0;
        int maxQuantity = 999999999;

        if (quantityText.equals("Dưới 50")) {
            maxQuantity = 50 - 1;
        } else if (quantityText.equals("Từ 50 đến 200")) {
            minQuantity = 50;
            maxQuantity = 200;
        } else if (quantityText.equals("Trên 200")) {
            minQuantity = 200 + 1;
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
                                && rowQuantity >= finalMinQuantity && rowQuantity <= finalMaxQuantity;
                    case "Tên sản phẩm":
                        return rowName.contains(productInfo) && rowType.contains(productType)
                                && rowQuantity >= finalMinQuantity && rowQuantity <= finalMaxQuantity;
                    default:
                        return true;
                }
            }
        };

        productSorter.setRowFilter(filter);
    }

    public void initProductTypeComboBoxData() {
        cbxProductType.removeAllItems();
        cbxProductType.addItem("Tất cả");
        productTypeBUS.renderToComboBox(cbxProductType);
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
        tblProducts.getTableHeader().setBackground(new Color(86, 132, 242));
        tblProducts.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblProducts.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void showHideConfirmButton() {
        if (!employeeBUS.getTypeById(employeeId).equals("LNV02")) {
            btnConfirmImport.setVisible(false);
            btnConfirmExport.setVisible(false);
        } else {
            btnUpdateImport.setVisible(false);
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
    private JTextField txtSearchExport;
    private JButton btnConfirmImport;
    private JButton btnConfirmExport;
    private JDateChooser importDateFrom;
    private JDateChooser importDateTo;
    private JDateChooser exportDateFrom;
    private JDateChooser exportDateTo;
}