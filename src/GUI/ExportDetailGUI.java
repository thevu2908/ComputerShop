package GUI;

import BUS.ExportDetailBUS;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ExportDetailGUI {
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private String exportId;
    private StorageGUI storageGUI;
    private ExportDetailBUS exportDetailBUS;

    public ExportDetailGUI(String exportId, StorageGUI storageGUI) {
        this.exportId = exportId;
        this.storageGUI = storageGUI;
        exportDetailBUS = new ExportDetailBUS();
        initTable();
        initTableData();
        setTotalQuantity();

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDetailGUI addDetailGUI = new AddDetailGUI(exportId, null, ExportDetailGUI.this);
                addDetailGUI.openAddDetailGUI();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowSelected = tblExportDetails.getSelectedRow();

                if (rowSelected < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn chi tiết phiếu xuất muốn xóa", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa chi tiết phiếu xuất này ?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    String productId = tblExportDetails.getValueAt(rowSelected, 0).toString();

                    if (exportDetailBUS.deleteExportDetail(exportId, productId)) {
                        initTableData();
                        setTotalQuantity();
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
        tblExportDetails.setRowSorter(sorter);

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
        if (sorter != null) {
            sorter.setRowFilter(null);
        }
    }

    public void setTotalQuantity() {
        int total = exportDetailBUS.calculateTotalQuantity(exportId);
        if (total >= 0) {
            txtTotalQuantity.setText(total + "");
            storageGUI.initExportTableData();
        }
    }

    public void initTableData() {
        exportDetailBUS.renderToTable(model, exportId);
    }

    public void initTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã sản phẩm", "Tên sản phẩm", "Số lượng"};
        model.setColumnIdentifiers(cols);
        tblExportDetails.setModel(model);
        tblExportDetails.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));
        tblExportDetails.getTableHeader().setBackground(new Color(86, 132, 242));
        tblExportDetails.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblExportDetails.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void openExportDetailGUI() {
        JFrame frame = new JFrame("Chi tiết phiếu xuất");
        frame.setContentPane(new ExportDetailGUI(exportId, storageGUI).mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTable tblExportDetails;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnReset;
    private JTextField txtTotalQuantity;
}
