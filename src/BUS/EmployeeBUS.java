package BUS;

import DAO.EmployeeDAO;
import DTO.EmployeeDTO;
import utils.DateTime;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class EmployeeBUS {
    private ArrayList<EmployeeDTO> employeeList = new ArrayList<>();
    private EmployeeDAO employeeDAO;
    private EmployeeTypeBUS employeeTypeBUS;

    public EmployeeBUS() {
        employeeDAO = new EmployeeDAO();
        employeeTypeBUS = new EmployeeTypeBUS();
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

        if (!checkValidPassword(username, password)) {
            JOptionPane.showMessageDialog(null, "Mật khẩu không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean addEmployee(String id, String name, String gender, String dob, String phone, String address,
                               String email, String password, String type) {
        if (id.equals("") || name.equals("") || gender.equals("") || dob.equals("") || phone.equals("") || address.equals("")
                || email.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedEmployeeId(id)) {
            JOptionPane.showMessageDialog(null, "Mã nhân viên đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidDate(dob)) {
            JOptionPane.showMessageDialog(null, "Ngày sinh không hợp lệ (dd-MM-yyyy)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidDOB(dob)) {
            JOptionPane.showMessageDialog(null, "Ngày sinh chưa đủ 18 tuổi", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ (10 chữ số)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidEmail(email)) {
            JOptionPane.showMessageDialog(null, "Email không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedPhone(phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại này đã được sử dụng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedEmail(email)) {
            JOptionPane.showMessageDialog(null, "Email này đã được sử dụng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        EmployeeDTO employee = new EmployeeDTO(id, type, name, address, phone, DateTime.formatDate(dob), gender, email, password, 0);

        if (employeeDAO.addEmployee(employee) > 0) {
            JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm nhân viên thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateEmployee(String id, String name, String gender, String dob, String phone, String address,
                                  String email, String password, String type) {
        if (id.equals("") || name.equals("") || gender.equals("") || dob.equals("") || phone.equals("") || address.equals("")
                || email.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidDate(dob)) {
            JOptionPane.showMessageDialog(null, "Ngày sinh không hợp lệ (dd-MM-yyyy)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidDOB(dob)) {
            JOptionPane.showMessageDialog(null, "Ngày sinh chưa đủ 18 tuổi", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ (10 chữ số)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidEmail(email)) {
            JOptionPane.showMessageDialog(null, "Email không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!getIdByPhone(phone).equals(id) && checkExistedPhone(phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại này đã được sử dụng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!getIdByEmail(email).equals(id) && checkExistedEmail(email)) {
            JOptionPane.showMessageDialog(null, "Email này đã được sử dụng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        EmployeeDTO employee = new EmployeeDTO(id, type, name, address, phone, DateTime.formatDate(dob), gender, email, password, 0);

        if (employeeDAO.updateEmployee(employee) > 0) {
            JOptionPane.showMessageDialog(null, "Sửa nhân viên thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Sửa nhân viên thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteEmployee(String id) {
        if (id.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhân viên muốn xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn xoá nhân viên này không ?", "Câu hỏi",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (employeeDAO.deleteEmployee(id) > 0) {
                JOptionPane.showMessageDialog(null, "Xoá nhân viên thành công");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Xoá nhân viên thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return false;
    }

    public boolean checkExistedEmployeeId(String employeeID) {
        loadData();

        for (EmployeeDTO employeeDTO: employeeList) {
            if (employeeDTO.getEmployeeId().equals(employeeID)) {
                return true;
            }
        }

        return false;
    }

    public boolean checkValidPassword(String username, String password) {
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

    public boolean checkExistedPhone(String phone) {
        loadData();

        for (EmployeeDTO employeeDTO : employeeList) {
            if (employeeDTO.getEmployeePhone().equals(phone)) {
                return true;
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

    public String createNewEmployeeID() {
        loadData();
        int id = employeeList.size() + 1;
        return "NV" + String.format("%02d", id);
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

    public String getIdByPhone(String phone) {
        loadData();

        for (EmployeeDTO employeeDTO : employeeList) {
            if (employeeDTO.getEmployeePhone().equals(phone)) {
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

    public EmployeeDTO getEmployeeById(String employeeID) {
        loadData();

        for (EmployeeDTO employeeDTO : employeeList){
            if (employeeDTO.getEmployeeId().equals(employeeID)){
                return employeeDTO;
            }
        }
        return null;
    }

    public ArrayList<EmployeeDTO> getEmployeeList() {
        loadData();
        return employeeList;
    }

    public void renderToTable(DefaultTableModel model) {
        loadData();
        model.setRowCount(0);

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