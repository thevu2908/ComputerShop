package GUI;

import BUS.EmployeeBUS;
import BUS.EmployeeTypeBUS;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EmployeeGUI {
    private DefaultTableModel employeeModel;
    private EmployeeBUS employeeBUS;
    private EmployeeTypeBUS employeeTypeBUS;

    public EmployeeGUI() {
        employeeBUS = new EmployeeBUS();
        employeeTypeBUS = new EmployeeTypeBUS();
        intiDateChooser();
        initTable();
        initTableData();
        initComboBoxData();
    }

    public void initTableData() {
        employeeBUS.renderToTable(employeeModel);
    }

    public void initTable() {
        employeeModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã nhân viên", "Tên nhân viên", "Loại nhân viên", "Giới tính", "Số điện thoại"};
        employeeModel.setColumnIdentifiers(cols);
        tblEmployees.setModel(employeeModel);
        tblEmployees.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblEmployees.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void initComboBoxData() {
        employeeTypeBUS.renderToComboBox(cbxEmpType);
        employeeTypeBUS.renderToComboBox(cbxFilterEmpType);
    }

    public void intiDateChooser() {
        employeeDOB = new JDateChooser();
        employeeDOB.setPreferredSize(new Dimension(-1, 30));
        datePanel.add(employeeDOB);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;
    private JPanel contentPanel;
    private JTable tblEmployees;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JComboBox cbxFilterEmpType;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnReset;
    private JButton btnCreateId;
    private JTextField txtEmpId;
    private JTextField txtEmpName;
    private JComboBox cbxEmpGender;
    private JTextField txtEmpAddress;
    private JTextField txtEmpEmail;
    private JTextField txtEmpPassword;
    private JComboBox cbxEmpType;
    private JPanel datePanel;
    private JTextField txtEmpPhone;
    private JDateChooser employeeDOB;
}
