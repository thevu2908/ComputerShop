package GUI;

import BUS.EmployeeBUS;
import BUS.EmployeeTypeBUS;
import DTO.EmployeeDTO;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

public class EmployeeGUI {
    private DefaultTableModel employeeModel;
    private EmployeeBUS employeeBUS;
    private EmployeeTypeBUS employeeTypeBUS;
    private TableRowSorter<DefaultTableModel> employeeSorter;

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
                int selectedRow = tblEmployees.getSelectedRow();

                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên muốn xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String id = tblEmployees.getValueAt(selectedRow, 0).toString();
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

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterEmployee();
            }
        });

        cbxFilterEmpType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterEmployee();
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
        cbxEmpType.setSelectedIndex(0);
        cbxSearchType.setSelectedIndex(0);
        cbxFilterEmpType.setSelectedIndex(0);
        employeeSorter.setRowFilter(null);
        txtSearch.setText((""));
        initTableData();
        if (employeeSorter != null) {
            employeeSorter.setRowFilter(null);
        }
    }

    public void filterEmployee() {
        String searchType = cbxSearchType.getSelectedItem().toString();
        String employeeInfo = txtSearch.getText().toLowerCase();

        String employeeType = cbxFilterEmpType.getSelectedItem() == null
                || cbxFilterEmpType.getSelectedItem().toString().equals("Tất cả")
                ? ""
                : cbxFilterEmpType.getSelectedItem().toString().toLowerCase();

        employeeSorter = new TableRowSorter<>(employeeModel);
        tblEmployees.setRowSorter(employeeSorter);

        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                String rowId = entry.getStringValue(0).toLowerCase();
                String rowName = entry.getStringValue(1).toLowerCase();
                String rowType = entry.getStringValue(2).toLowerCase();
                String rowGender = entry.getStringValue(3).toLowerCase();
                String rowPhone = entry.getStringValue(4).toLowerCase();

                switch (searchType) {
                    case "Mã nhân viên":
                        return rowId.contains(employeeInfo) && rowType.contains(employeeType);
                    case "Tên nhân viên":
                        return rowName.contains(employeeInfo) && rowType.contains(employeeType);
                    case "Giới tính":
                        return rowGender.contains(employeeInfo) && rowType.contains(employeeType);
                    case "Số điện thoại":
                        return rowPhone.contains(employeeInfo) && rowType.contains(employeeType);
                    default:
                        return true;
                }
            }
        };

        employeeSorter.setRowFilter(filter);
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
        tblEmployees.getTableHeader().setBackground(new Color(86, 132, 242));
        tblEmployees.getTableHeader().setForeground(Color.WHITE);

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
