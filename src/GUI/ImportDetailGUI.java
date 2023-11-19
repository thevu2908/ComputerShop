package GUI;

import BUS.ImportDetailBUS;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ImportDetailGUI {
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private String importId;
    private ImportDetailBUS importDetailBUS;
    private StorageGUI storageGUI;

    public ImportDetailGUI(String importId, StorageGUI storageGUI) {
        this.importId = importId;
        this.storageGUI = storageGUI;
        importDetailBUS = new ImportDetailBUS();

        initTable();
        initTableData();
        setTotalPrice();

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDetailGUI addDetailGUI = new AddDetailGUI(importId, ImportDetailGUI.this, null);
                addDetailGUI.openAddDetailGUI();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowSelected = tblImportDetails.getSelectedRow();

                if (rowSelected < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn chi tiết phiếu nhập muốn xóa", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa chi tiết phiếu nhập này ?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    String productId = tblImportDetails.getValueAt(rowSelected, 0).toString();

                    if (importDetailBUS.deleteImportDetail(importId, productId)) {
                        initTableData();
                        setTotalPrice();
                    }
                }
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filter();
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
    }

    public void filter() {
        String searchType = cbxSearchType.getSelectedItem().toString();
        String searchInfo = txtSearch.getText().toLowerCase();

        sorter = new TableRowSorter<>(model);
        tblImportDetails.setRowSorter(sorter);

        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                String rowId = entry.getStringValue(0).toLowerCase();
                String rowName = entry.getStringValue(1).toLowerCase();

                switch (searchType) {
                    case "Mã sản phẩm":
                        return rowId.contains(searchInfo);
                    case "Tên sản phẩm":
                        return rowName.contains(searchInfo);
                    default:
                        return true;
                }
            }
        };

        sorter.setRowFilter(filter);
    }

    public void reset() {
        cbxSearchType.setSelectedIndex(0);
        txtSearch.setText("");
        sorter.setRowFilter(null);
        if (sorter != null) {
            sorter.setRowFilter(null);
        }
    }

    public void setTotalPrice() {
        int total = importDetailBUS.calculateTotalPrice(importId);
        if (total >= 0) {
            txtTotalPrice.setText(total + "");
            storageGUI.initImportTableData();
        }
    }

    public void initTableData() {
        importDetailBUS.renderToTable(model, importId);
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
        tblImportDetails.setModel(model);
        tblImportDetails.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));
        tblImportDetails.getTableHeader().setBackground(new Color(86, 132, 242));
        tblImportDetails.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblImportDetails.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void openImportDetailGUI() {
        JFrame frame = new JFrame("Chi tiết phiếu nhập");
        frame.setContentPane(new ImportDetailGUI(importId, storageGUI).mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTable tblImportDetails;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnReset;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JTextField txtTotalPrice;
    private JLabel lblTitle;
}