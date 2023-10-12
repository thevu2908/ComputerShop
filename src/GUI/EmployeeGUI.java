package GUI;

import BUS.EmployeeBUS;
import BUS.EmployeeTypeBUS;
import DTO.EmployeeDTO;
import com.toedter.calendar.JDateChooser;
import utils.DateTime;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

public class EmployeeGUI {
    private DefaultTableModel employeeModel;
    private EmployeeBUS employeeBUS;
    private EmployeeTypeBUS employeeTypeBUS;

    public EmployeeGUI() {
        employeeBUS = new EmployeeBUS();
        employeeBUS = new EmployeeBUS();
        employeeTypeBUS = new EmployeeTypeBUS();
        intiDateChooser();
        initTable();
        initTableData();
        initComboBoxData();

        btnCreateId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtEmpId.setText(employeeBUS.createNewEmployeeID());}
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtEmpId.getText();
                String name = txtEmpName.getText();
                String gender = cbxEmpGender.getSelectedItem().toString();
                String dob = ((JTextField) employeeDOB.getDateEditor().getUiComponent()).getText();
                String phone = txtEmpPhone.getText();
                String address = txtEmpAddress.getText();
                String email = txtEmpEmail.getText();
                String password = txtEmpPassword.getText();
                String type = employeeTypeBUS.getIDByTypeName(cbxEmpType.getSelectedItem().toString());

                if (employeeBUS.addEmployee(id, name, gender, dob, phone, address, email, password, type)) {
                    resetData();
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtEmpId.getText();
                String name = txtEmpName.getText();
                String gender = cbxEmpGender.getSelectedItem().toString();
                String dob = ((JTextField) employeeDOB.getDateEditor().getUiComponent()).getText();
                String phone = txtEmpPhone.getText();
                String address = txtEmpAddress.getText();
                String email = txtEmpEmail.getText();
                String password = txtEmpPassword.getText();
                String type = employeeTypeBUS.getIDByTypeName(cbxEmpType.getSelectedItem().toString());

                if (employeeBUS.updateEmployee(id, name, gender, dob, phone, address, email, password, type)) {
                    resetData();
                }
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetData();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtEmpId.getText();
                if (employeeBUS.deleteEmployee(id)) {
                    resetData();
                }
            }
        });

        tblEmployees.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowSelected = tblEmployees.getSelectedRow();

                if (rowSelected >= 0) {
                    String employeeId = tblEmployees.getValueAt(rowSelected, 0).toString();
                    EmployeeDTO employee = employeeBUS.getEmployeeById(employeeId);

                    txtEmpId.setText(employeeId);
                    txtEmpName.setText(employee.getEmployeeName());
                    cbxEmpGender.setSelectedItem(employee.getEmployeeGender());
                    String dob = employee.getEmployeeDOB();
                    try {
                        employeeDOB.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(dob));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    txtEmpPhone.setText(employee.getEmployeePhone());
                    txtEmpAddress.setText(employee.getEmployeeAddress());
                    txtEmpEmail.setText(employee.getEmployeeEmail());
                    txtEmpPassword.setText(employee.getEmployeePassword());
                    cbxEmpType.setSelectedItem(employeeTypeBUS.getTypeNameById(employee.getEmployeeType()));
                }
            }
        });
    }

    public void resetData() {
        txtEmpId.setText("");
        txtEmpName.setText("");
        cbxEmpGender.setSelectedIndex(0);
        employeeDOB.setDate(null);
        txtEmpPhone.setText("");
        txtEmpAddress.setText("");
        txtEmpEmail.setText("");
        txtEmpPassword.setText("");
        cbxEmpType.setSelectedIndex(0);
        initTableData();
    }

    public void initComboBoxData() {
        employeeTypeBUS.renderToComboBox(cbxFilterEmpType, "filter");
        employeeTypeBUS.renderToComboBox(cbxEmpType, "");
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

    public void intiDateChooser() {
        employeeDOB = new JDateChooser();
        employeeDOB.setDateFormatString("dd-MM-yyyy");
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
