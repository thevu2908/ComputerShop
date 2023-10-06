package BUS;

import DAO.ExportDAO;
import DTO.ExportDTO;
import utils.DateFormat;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ExportBUS {
    private ArrayList<ExportDTO> exportDetailList;
    private ExportDAO exportDAO;
    private EmployeeTypeBUS employeeTypeBUS;

    public ExportBUS() {
        exportDAO = new ExportDAO();
        employeeTypeBUS = new EmployeeTypeBUS();
    }

    public void loadData() {
        exportDetailList = exportDAO.getData();
    }

    public int setTotalQuantity(String id, int total) {
        return exportDAO.setTotalQuantity(id, total);
    }

    public void renderToTable(DefaultTableModel model, String employeeId) {
        model.setRowCount(0);
        loadData();

        for (ExportDTO exportDTO : exportDetailList) {
            System.out.println(exportDTO.getIsDeleted() == 0 && (employeeTypeBUS.getTypeNameById(employeeId).equals("Quản lý")
                    || exportDTO.getEmployeeId().equals(employeeId)));
            if (exportDTO.getIsDeleted() == 0 && (employeeTypeBUS.getTypeNameById(employeeId).equals("Quản lý")
                    || exportDTO.getEmployeeId().equals(employeeId))) {
                model.addRow(new Object[]{
                        exportDTO.getExportId(),
                        exportDTO.getEmployeeId(),
                        DateFormat.formatDate(exportDTO.getExportDate()),
                        exportDTO.getTotalQuantity(),
                        exportDTO.getStatus()
                });
            }
        }

        model.fireTableDataChanged();
    }
}