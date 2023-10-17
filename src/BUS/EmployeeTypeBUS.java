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