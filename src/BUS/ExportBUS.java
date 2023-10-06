package BUS;

import DAO.ExportDAO;
import DTO.ExportDTO;
import utils.DateFormat;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ExportBUS {
    private ArrayList<ExportDTO> exportDetailList;
    private ExportDAO exportDAO;

    public ExportBUS() {
        exportDAO = new ExportDAO();
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
            if (exportDTO.getIsDeleted() == 0 && exportDTO.getEmployeeId().equals(employeeId)) {
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