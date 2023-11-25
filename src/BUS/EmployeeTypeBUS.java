package BUS;

import DAO.EmployeeTypeDAO;
import DTO.EmployeeTypeDTO;

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

    public boolean addEmployeeType(String employeeTypeId, String employeeTypeName) {
        if (employeeTypeId.isEmpty() || employeeTypeName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedEmployeeTypeId(employeeTypeId)) {
            JOptionPane.showMessageDialog(null, "Mã loại nhân viên đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedEmployeeTypeName(employeeTypeName)) {
            JOptionPane.showMessageDialog(null, "Loại nhân viên đã có trong hệ thống", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        EmployeeTypeDTO employeeType = new EmployeeTypeDTO(employeeTypeId, employeeTypeName, 0);

        if (employeeTypeDAO.addEmployeeType(employeeType) > 0) {
            JOptionPane.showMessageDialog(null, "Thêm loại nhân viên thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm loại nhân viên thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

   public String createNewEmployeeTypeId() {
       loadData();
       int id = employeeTypeList.size() + 1;
       return "LNV" + String.format("%02d", id);
   }

   public boolean checkExistedEmployeeTypeId(String employeeTypeID) {
       loadData();

       for (EmployeeTypeDTO employeeTypeDTO : employeeTypeList) {
           if (employeeTypeDTO.getIsDeleted() == 0 && employeeTypeDTO.getTypeId().equals(employeeTypeID)) {
               return true;
           }
       }

       return false;
   }

    public boolean checkExistedEmployeeTypeName(String employeeTypeName) {
        loadData();

        for (EmployeeTypeDTO employeeTypeDTO : employeeTypeList) {
            if (employeeTypeDTO.getIsDeleted() == 0 && employeeTypeDTO.getTypeName().equals(employeeTypeName)) {
                return true;
            }
        }

        return false;
    }

   public EmployeeTypeDTO getEmployeeTypeById(String employeeTypeID) {
       loadData();

       for (EmployeeTypeDTO employeeTypeDTO : employeeTypeList){
           if (employeeTypeDTO.getIsDeleted() == 0 && employeeTypeDTO.getTypeId().equals(employeeTypeID)){
               return employeeTypeDTO;
           }
       }

       return null;
   }

   public String getTypeNameById(String id) {
       loadData();

       for (EmployeeTypeDTO employeeTypeDTO : employeeTypeList) {
           if (employeeTypeDTO.getIsDeleted() == 0 && employeeTypeDTO.getTypeId().equals(id)) {
               return employeeTypeDTO.getTypeName();
           }
       }

       return "";
   }

   public String getIDByTypeName(String name) {
       loadData();

       for (EmployeeTypeDTO employeeTypeDTO : employeeTypeList) {
           if (employeeTypeDTO.getIsDeleted() == 0 && employeeTypeDTO.getTypeName().equals(name)) {
               return employeeTypeDTO.getTypeId();
           }
       }

       return "";
   }

   public void renderToComboBox(JComboBox cbx, String type) {
       loadData();
       cbx.removeAllItems();

       if (type.equals("filter")) {
           cbx.addItem("Tất cả");
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
}