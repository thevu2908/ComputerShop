package GUI;

import BUS.ExportDetailBUS;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExportDetailGUI {
    private DefaultTableModel model;
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
                AddProductGUI addProductGUI = new AddProductGUI(exportId, null, ExportDetailGUI.this);
                addProductGUI.openAddProductGUI();
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
    }

    public void setTotalQuantity() {
        int total = exportDetailBUS.calculateTotalQuantity(exportId);
        if (total > 0) {
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
