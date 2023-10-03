package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ImportDetailGUI {
    private DefaultTableModel model;
    private String importId;

    public ImportDetailGUI(String importId) {
        this.importId = importId;
        System.out.println(this.importId);
        initTable();
    }

    public void initTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã SP", "Tên SP", "Số lượng", "Đơn giá", "Thành tiền"};
        model.setColumnIdentifiers(cols);
        tblImportDetails.setModel(model);
        tblImportDetails.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblImportDetails.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void openImportDetailGUI() {
        JFrame frame = new JFrame("Chi tiết phiếu nhập");
        frame.setContentPane(new ImportDetailGUI(importId).mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTable tblImportDetails;
    private JComboBox cbxPrice;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnReset;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JTextField txtTotal;
}