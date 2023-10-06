package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AccessGUI {
    private DefaultTableModel decentralizationModel;

    public AccessGUI() {
        initTable();
    }

    public void initTable() {
        decentralizationModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã quyền","Tên quyền"};
        decentralizationModel.setColumnIdentifiers(cols);
        tblDSQuyen.setModel(decentralizationModel);
        tblDSQuyen.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblDSQuyen.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;
    private JPanel contentPanel;
    private JTable tblDSQuyen;
    private JPanel SearchPanel;
    private JTextField txtTenQuyen;
    private JTextField txtMaQuyen;
    private JComboBox cbxKieuTimKiem;
    private JTextField txtTimKiem;
    private JButton btnTaoMoi;
    private JButton btnThem;
    private JButton btnXoa;
    private JButton btnSua;
    private JButton btnReset;
    private JButton btnPhanQuyen;
}