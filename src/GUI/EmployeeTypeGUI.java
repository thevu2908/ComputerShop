package GUI;

import BUS.EmployeeTypeBUS;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeeTypeGUI {
    private DefaultTableModel accessModel;
    private EmployeeTypeBUS employeeTypeBUS;

    public EmployeeTypeGUI() {
        employeeTypeBUS = new EmployeeTypeBUS();
        initTable();
        initTableData();
    }

    public void initTableData() {
        employeeTypeBUS.renderToTable(accessModel);
    }

    public void initTable() {
        accessModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã quyền", "Tên quyền"};
        accessModel.setColumnIdentifiers(cols);
        tblAccesses.setModel(accessModel);
        tblAccesses.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblAccesses.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;
    private JPanel contentPanel;
    private JTable tblAccesses;
    private JPanel SearchPanel;
    private JTextField txtAccessName;
    private JTextField txtAccessId;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JButton btnCreateNewId;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnUpdate;
    private JButton btnReset;
    private JButton btnGiveAccess;
}