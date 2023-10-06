package BUS;

import DAO.EmployeeTypeDAO;
import DTO.EmployeeTypeDTO;

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
}