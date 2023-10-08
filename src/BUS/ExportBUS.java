package BUS;

import DAO.ExportDAO;
import DTO.ExportDTO;
import utils.DateTime;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ExportBUS {
    private ArrayList<ExportDTO> exportList;
    private ExportDAO exportDAO;
    private EmployeeBUS employeeBUS;

    public ExportBUS() {
        exportDAO = new ExportDAO();
        employeeBUS = new EmployeeBUS();
    }

    public void loadData() {
        exportList = exportDAO.getData();
    }

    public int setTotalQuantity(String id, int total) {
        return exportDAO.setTotalQuantity(id, total);
    }

    public String createNewId() {
        loadData();
        int id = exportList.size() + 1;
        return "PX" + String.format("%03d", id);
    }

    public void renderToTable(DefaultTableModel model, String employeeId) {
        model.setRowCount(0);
        loadData();

        for (ExportDTO exportDTO : exportList) {
            if (exportDTO.getIsDeleted() == 0 && (employeeBUS.getTypeById(employeeId).equals("LNV02")
                    || exportDTO.getEmployeeId().equals(employeeId))) {
                model.addRow(new Object[]{
                        exportDTO.getExportId(),
                        exportDTO.getEmployeeId(),
                        DateTime.formatDate(exportDTO.getExportDate()),
                        exportDTO.getTotalQuantity(),
                        exportDTO.getStatus()
                });
            }
        }

        model.fireTableDataChanged();
    }
}