package GUI;

import BUS.EmployeeBUS;
import BUS.EmployeeTypeBUS;
import BUS.ProductBUS;
import BUS.ProductTypeBUS;
import DTO.EmployeeDTO;
import DTO.ProductDTO;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.toedter.calendar.IDateEditor;
import com.toedter.calendar.JDateChooser;
import org.apache.poi.hssf.usermodel.HeaderFooter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EmployeeGUI {
    private DefaultTableModel employeeModel;
    private EmployeeBUS employeeBUS;

    private EmployeeTypeBUS employeeTypeBUS;


    public EmployeeGUI() {
        employeeBUS = new EmployeeBUS();
        intiDateChooser();
        initTable();
        initTableData();
        employeeBUS = new EmployeeBUS();
        employeeTypeBUS = new EmployeeTypeBUS();
        initTable();
        initTableData();
//        initComboboxTypeData();

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
                String gioi_tinh = cbxEmpGender.getSelectedItem().toString();

                java.sql.Date sqlDate = new java.sql.Date(employeeDOB.getDate().getTime());
                String ngay_sinh=sqlDate.toString();

                String sdt = txtEmpPhone.getText();
                String diachi = txtEmpAddress.getText();
                String email = txtEmpEmail.getText();
                String matkhau = txtEmpPassword.getText();

                String loainv = employeeTypeBUS.getIDByTypeName(cbxEmpType.getSelectedItem().toString());


                if (employeeBUS.addEmployee(id, name, gioi_tinh,ngay_sinh, sdt, diachi, email,matkhau,loainv)) {
                    initTableData();

                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtEmpId.getText();
                String name = txtEmpName.getText();
                String gioi_tinh = cbxEmpGender.getSelectedItem().toString();

                java.sql.Date sqlDate = new java.sql.Date(employeeDOB.getDate().getTime());
                String ngay_sinh=sqlDate.toString();

                String sdt = txtEmpPhone.getText();
                String diachi = txtEmpAddress.getText();
                String email = txtEmpEmail.getText();
                String matkhau = txtEmpPassword.getText();

                String loainv = employeeTypeBUS.getIDByTypeName(cbxEmpType.getSelectedItem().toString());


                if
                (employeeBUS.updateEmployee(id, name, gioi_tinh,ngay_sinh, sdt, diachi, email,matkhau,loainv)) {
                    initTableData();
                }
            }
        });
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetValues();

            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtEmpId.getText();
                if (employeeBUS.deleteEmployee(id)) {
                    resetValues();
                    initTableData();
                }
            }
        });



        tblEmployees.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowSelected = tblEmployees.getSelectedRow();

                if (rowSelected >= 0) {
                    String employeeid = tblEmployees.getValueAt(rowSelected, 0).toString();
                    EmployeeDTO employeedto = employeeBUS.getEmployeeById(employeeid);

                    txtEmpId.setText(employeedto.getEmployeeId());
                    txtEmpName.setText(employeedto.getEmployeeName());
                    cbxEmpGender.setSelectedItem(employeedto.getEmployeeGender());
                    String date = employeedto.getEmployeeDOB();
                    Date date2 = null;
                    try {
                        date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    employeeDOB.setDate(date2);
                    txtEmpPhone.setText(employeedto.getEmployeePhone());
                    txtEmpAddress.setText(employeedto.getEmployeeAddress());
                    txtEmpEmail.setText(employeedto.getEmployeeEmail());
                    txtEmpPassword.setText(employeedto.getEmployeePassword());
                    cbxEmpType.setSelectedItem(employeeTypeBUS.getTypeNameById(employeedto.getEmployeeType()));
                }
            }
        });
    }

    public void resetValues(){
        txtEmpId.setText("");
        txtEmpName.setText("");
        txtEmpAddress.setText("");
        txtEmpPassword.setText("");
        txtEmpEmail.setText("");
        txtEmpPhone.setText("");

        String date = "2003-1-1";
        Date date2 = null;
        try {
            date2 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        employeeDOB.setDate(date2);

        initTableData();

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
