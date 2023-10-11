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
        loadData();
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

    public void renderToComboBox(JComboBox cbx) {
        loadData();

        for (EmployeeTypeDTO employeeTypeDTO : employeeTypeList) {
            if (employeeTypeDTO.getIsDeleted() == 0 && !employeeTypeDTO.getTypeId().equals("LNV01")) {
                cbx.addItem(employeeTypeDTO.getTypeName());
            }
        }
    }

    public void renderToTable(DefaultTableModel model) {
        loadData();

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