package GUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

import BUS.SupplierBUS;
import DTO.SupplierDTO;

public class SupplierGUI {
    private DefaultTableModel supplierModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private SupplierBUS supplierBUS;

    public SupplierGUI() {
        supplierBUS = new SupplierBUS();
        initTable();
        initTableData();

        btnCreateId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtSupplierId.setText(supplierBUS.createNewSupplierID());
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtSupplierId.getText();
                String name = txtSupplierName.getText();
                String address = txtSupplierAddress.getText();
                String phone = txtSupplierPhone.getText();

                if (supplierBUS.addSupplier(id, name, address, phone)) {
                    reset();
                }
            }
        });

        tblSuppliers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowSelected = tblSuppliers.getSelectedRow();

                if (rowSelected >= 0) {
                    String SupplierId = tblSuppliers.getValueAt(rowSelected, 0).toString();
                    SupplierDTO supplier = supplierBUS.getSupplierById(SupplierId);

                    txtSupplierId.setText(supplier.getSupplierId());
                    txtSupplierName.setText(supplier.getSupplierName());
                    txtSupplierAddress.setText(supplier.getSupplierAddress());
                    txtSupplierPhone.setText(supplier.getSupplierPhone());
                }
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtSupplierId.getText();
                String name = txtSupplierName.getText();
                String address = txtSupplierAddress.getText();
                String phone = txtSupplierPhone.getText();

                if (supplierBUS.updateSupplier(id, name, address, phone)) {
                    reset();
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblSuppliers.getSelectedRow();

                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhà cung cấp muốn xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String id = tblSuppliers.getValueAt(selectedRow, 0).toString();
                if (supplierBUS.deleteSupplier(id)) {
                    reset();
                }
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filter();
            }
        });

        btnExportExcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                String defaultFileName = "dsncc.xlsx";
                fileChooser.setSelectedFile(new File(defaultFileName));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel (.xlsx)", "xlsx");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showSaveDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fileChooser.getSelectedFile();
                        supplierBUS.exportExcel(file);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void filter() {
        String searchType = cbxSearchType.getSelectedItem().toString();
        String searchInfo = txtSearch.getText().toLowerCase();

        sorter = new TableRowSorter<>(supplierModel);
        tblSuppliers.setRowSorter(sorter);

        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                String rowId = entry.getStringValue(0).toLowerCase();
                String rowName = entry.getStringValue(1).toLowerCase();
                String rowPhone = entry.getStringValue(2).toLowerCase();
                String rowAddress = entry.getStringValue(3).toLowerCase();

                switch (searchType) {
                    case "Mã nhà cung cấp":
                        return rowId.contains(searchInfo);
                    case "Tên nhà cung cấp":
                        return rowName.contains(searchInfo);
                    case "Số điện thoại":
                        return rowPhone.contains(searchInfo);
                    case "Địa chỉ":
                        return rowAddress.contains(searchInfo);
                    default:
                        return true;
                }
            }
        };

        sorter.setRowFilter(filter);
    }

    public void reset() {
        txtSupplierId.setText("");
        txtSupplierName.setText("");
        txtSupplierAddress.setText("");
        txtSupplierPhone.setText("");
        txtSearch.setText("");
        cbxSearchType.setSelectedIndex(0);
        initTableData();
        if (sorter != null) {
            sorter.setRowFilter(null);
        }
    }

    public void initTableData() {
        supplierBUS.renderToSupplierTable(supplierModel);
    }

    public void initTable() {
        supplierModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã nhà cung cấp", "Tên nhà cung cấp", "Số điện thoại", "Địa chỉ"};
        supplierModel.setColumnIdentifiers(cols);
        tblSuppliers.setModel(supplierModel);
        tblSuppliers.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));
        tblSuppliers.getTableHeader().setBackground(new Color(86, 132, 242));
        tblSuppliers.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 0; i < tblSuppliers.getColumnCount(); i++){
            tblSuppliers.getColumnModel().getColumn(i).setCellRenderer(centerRender);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JTable tblSuppliers;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JTextField txtSupplierId;
    private JTextField txtSupplierName;
    private JTextField txtSupplierPhone;
    private JTextField txtSupplierAddress;
    private JPanel mainPanel;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnReset;
    private JButton btnCreateId;
    private JButton btnExportExcel;
}