package BUS;

import DAO.EmployeeTypeDAO;
import DTO.EmployeeTypeDTO;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class EmployeeTypeBUS {
    private EmployeeTypeDAO employeeTypeDAO;
    private ArrayList<EmployeeTypeDTO> employeeTypeList;
    public EmployeeTypeBUS() {
        employeeTypeDAO = new EmployeeTypeDAO();
    }

    public void loadData() {
        employeeTypeList = employeeTypeDAO.getData();
    }
    public String getTypeNameById(String id) {
        loadData();
        for (EmployeeTypeDTO employeeTypeDTO : employeeTypeList) {
            if (employeeTypeDTO.getTypeId().equals(id)) {
                return employeeTypeDTO.getTypeName();
            }
        }
        return "";
    }
    public String getIDByTypeName(String name)  {
        loadData();
        for (EmployeeTypeDTO employeeTypeDTO : employeeTypeList) {
            if (employeeTypeDTO.getTypeName().equals(name)) {
                return employeeTypeDTO.getTypeId();
            }
        }
        return "";
    }
    public void renderToComboBox(JComboBox cbx, String type) {
        loadData();
        cbx.removeAllItems();
        if (type.equals("filter")) {
            cbx.addItem("Loại nhân viên");
        }
        for (EmployeeTypeDTO employeeTypeDTO : employeeTypeList) {
            if (employeeTypeDTO.getIsDeleted() == 0 && !employeeTypeDTO.getTypeId().equals("LNV01")) {
                cbx.addItem(employeeTypeDTO.getTypeName());
            }
        }
    }
    public void renderToTable(DefaultTableModel model) {
        loadData();
        model.setRowCount(0);
        for (EmployeeTypeDTO employeeTypeDTO : employeeTypeList) {
            if (employeeTypeDTO.getIsDeleted() == 0 && !employeeTypeDTO.getTypeId().equals("LNV01")) {
                model.addRow(new Object[]{
                        employeeTypeDTO.getTypeId(),
                        employeeTypeDTO.getTypeName()
                });
            }
        }
        model.fireTableDataChanged();
    }
    public String createNewEmployeeTypeId() {
        loadData();
        int id = employeeTypeList.size() + 1;
        return "LNV" + String.format("%03d", id);
    }
    public boolean checkExistedEmployTypeId(String maLoaiNhanVien) {
        loadData();
        for (EmployeeTypeDTO employeeTypeDTO : employeeTypeList) {
            if (employeeTypeDTO.getTypeId().equals(maLoaiNhanVien)) {
                return true;
            }
        }
        return false;
    }
    public boolean addEmployeeType(String maLoaiNhanVien, String tenLoaiNhanVien, int trangThai) {
        if (maLoaiNhanVien.equals("") || tenLoaiNhanVien.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (checkExistedEmployTypeId(maLoaiNhanVien)){
            JOptionPane.showMessageDialog(null, "Mã loại nhân viên đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (trangThai != 0 && trangThai != 1) {
            JOptionPane.showMessageDialog(null, "Trạng thái không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        EmployeeTypeDTO employeeType = new EmployeeTypeDTO(maLoaiNhanVien, tenLoaiNhanVien, trangThai);
        if (employeeTypeDAO.addEmployeeType(employeeType)) {
            JOptionPane.showMessageDialog(null, "Thêm loại nhân viên thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm loại nhân viên thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteEmployeeType(String maLoaiNhanVien) {
        if (maLoaiNhanVien.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn mã loại nhân viên xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn mã loại nhân viên " + maLoaiNhanVien + " không ?", "Câu hỏi",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (employeeTypeDAO.deleteEmployeeType(maLoaiNhanVien)) {
                JOptionPane.showMessageDialog(null, "Xoá mã loại nhân viên " + maLoaiNhanVien + " thành công");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Xoá mã loại nhân viên thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    public boolean updateEmployeeType(String maLoaiNhanVien, String tenLoaiNhanVien, int trangThai) {
        if (maLoaiNhanVien.equals("")){
            JOptionPane.showMessageDialog(null, "Vui lòng chọn mã loại nhân viên muốn sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (maLoaiNhanVien.equals("") || tenLoaiNhanVien.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!checkExistedEmployTypeId(maLoaiNhanVien)) {
            JOptionPane.showMessageDialog(null, "Không tồn tại mã loại nhân viên này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (checkExistedEmployTypeId(maLoaiNhanVien)){
            JOptionPane.showMessageDialog(null, "Mã loại nhân viên đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (trangThai != 0 && trangThai != 1) {
            JOptionPane.showMessageDialog(null, "Trạng thái không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        EmployeeTypeDTO employeeType = new EmployeeTypeDTO(maLoaiNhanVien, tenLoaiNhanVien, trangThai);

        if (employeeTypeDAO.updateEmployeeType(employeeType)) {
            JOptionPane.showMessageDialog(null, "Sửa thông tin mã loại nhân viên");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Sửa thông tin mã loại nhân viên thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }


}