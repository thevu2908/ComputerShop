package BUS;

import DAO.EmployeeDAO;
import DTO.EmployeeDTO;
import DTO.ProductDTO;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class EmployeeBUS {
    private ArrayList<EmployeeDTO> employeeList = new ArrayList<>();
    private EmployeeDAO employeeDAO;
    private EmployeeTypeBUS employeeTypeBUS;

    ArrayList<EmployeeDTO> listall = new ArrayList<>();

    public EmployeeBUS() {
        employeeDAO = new EmployeeDAO();
        employeeTypeBUS = new EmployeeTypeBUS();
        listall = employeeDAO.getData();
        loadData();

    }

    public void loadData() {
        this.employeeList.clear();
        listall = employeeDAO.getData();

        for (EmployeeDTO empl : listall)
            employeeList.add(empl);
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

    public String getGenderByID(String id) {
        loadData();

        for (EmployeeDTO employeeDTO : employeeList) {
            if (employeeDTO.getEmployeeGender().equals(id)) {
                return employeeDTO.getEmployeeGender();
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

    public boolean addEmployee(String id, String name, String gioi_tinh, String ngay_sinh, String sdt, String dia_chi, String email,String mat_khau,String loainv) {
        if (id.equals("") || name.equals("") ||gioi_tinh.equals("") || ngay_sinh.equals("") || sdt.equals("") || dia_chi.equals("")
                || email.equals("") || mat_khau.equals("") ) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedEmployeeId(id)){
            JOptionPane.showMessageDialog(null, "Mã nhân viên đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        EmployeeDTO employee = new EmployeeDTO(id, loainv, name, dia_chi, sdt,ngay_sinh, gioi_tinh, email, mat_khau, 1);

        if (employeeDAO.addEmployee(employee) == 1) {
            JOptionPane.showMessageDialog(null, "Thêm nhân viên thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm nhân viên thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        }
    public boolean updateEmployee(String id, String name, String gioi_tinh, String ngay_sinh, String sdt, String dia_chi, String email,String mat_khau,String loainv) {
        if (id.equals("") || name.equals("") ||gioi_tinh.equals("") || ngay_sinh.equals("") || sdt.equals("") || dia_chi.equals("")
                || email.equals("") || mat_khau.equals("") ) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        EmployeeDTO employee = new EmployeeDTO(id, loainv, name, dia_chi, sdt,ngay_sinh, gioi_tinh, email, mat_khau, 1);

        if (employeeDAO.updateData(employee) == 1) {
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
            if (employeeDAO.delete(id) > 0) {
                JOptionPane.showMessageDialog(null, "Xoá nhân viên thành công");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Xoá nhân viên thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return false;
    }
        public boolean checkExistedEmployeeId(String  employeeID) {
        loadData();

        for (EmployeeDTO employeeDTO: employeeList) {
            if (employeeDTO.getEmployeeId().equals(employeeID)) {
                return true;
            }
        }
        return false;
    }
    public String createNewEmployeeID() {
        loadData();
        int id = employeeList.size() + 1;
        return "NV" + String.format("%03d", id);
    }




}