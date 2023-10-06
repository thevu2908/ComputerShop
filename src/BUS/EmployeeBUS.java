package BUS;

import DAO.EmployeeDAO;
import DTO.EmployeeDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class EmployeeBUS {
    private ArrayList<EmployeeDTO> employeeList;
    private EmployeeDAO employeeDAO;
    private EmployeeTypeBUS employeeTypeBUS;

    public EmployeeBUS() {
        employeeDAO = new EmployeeDAO();
        employeeTypeBUS = new EmployeeTypeBUS();
        loadData();
    }

    public void loadData() {
        employeeList = employeeDAO.getData();
    }

    public boolean login(String username, String password) {
        if (username.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ tài khoản và mật khẩu", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!checkExistedEmail(username)) {
            JOptionPane.showMessageDialog(null, "Tài khoản không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!checkPassword(username, password)) {
            JOptionPane.showMessageDialog(null, "Mật khẩu không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public String getNameById(String id) {
        loadData();

        for (EmployeeDTO employeeDTO : employeeList) {
            if (employeeDTO.getEmployeeId().equals(id)) {
                return employeeDTO.getEmployeeName();
            }
        }

        return "";
    }

    public String getIdByEmail(String email) {
        loadData();

        for (EmployeeDTO employeeDTO : employeeList) {
            if (employeeDTO.getEmployeeEmail().equals(email)) {
                return employeeDTO.getEmployeeId();
            }
        }

        return "";
    }

    public String getTypeById(String id) {
        loadData();

        for (EmployeeDTO employeeDTO : employeeList) {
            if (employeeDTO.getEmployeeId().equals(id)) {
                return employeeDTO.getEmployeeType();
            }
        }

        return "";
    }

    public String getTypeByEmail(String email) {
        loadData();

        for (EmployeeDTO employeeDTO : employeeList) {
            if (employeeDTO.getEmployeeEmail().equals(email)) {
                return employeeDTO.getEmployeeType();
            }
        }

        return "";
    }

    public boolean checkPassword(String username, String password) {
        loadData();

        for (EmployeeDTO employeeDTO : employeeList) {
            if (employeeDTO.getEmployeeEmail().equals(username)) {
                if (employeeDTO.getEmployeePassword().equals(password)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean checkExistedEmail(String email) {
        loadData();

        for (EmployeeDTO employeeDTO : employeeList) {
            if (employeeDTO.getEmployeeEmail().equals(email)) {
                return true;
            }
        }

        return false;
    }

    public void renderToTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadData();

        for (EmployeeDTO employeeDTO : employeeList) {
            if (employeeDTO.getIsDeleted() == 0 && !employeeDTO.getEmployeeId().equals("NV00")) {
                model.addRow(new Object[]{
                        employeeDTO.getEmployeeId(),
                        employeeDTO.getEmployeeName(),
                        employeeTypeBUS.getTypeNameById(employeeDTO.getEmployeeType()),
                        employeeDTO.getEmployeeGender(),
                        employeeDTO.getEmployeePhone()
                });
            }
        }

        model.fireTableDataChanged();
    }
}