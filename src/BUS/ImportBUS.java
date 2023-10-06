package BUS;

import DAO.ImportDAO;
import DTO.ImportDTO;
import utils.DateFormat;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ImportBUS {
    private ArrayList<ImportDTO> importList;
    private ImportDAO importDAO;
    private EmployeeBUS employeeBUS;

    public ImportBUS() {
        importDAO = new ImportDAO();
        employeeBUS = new EmployeeBUS();
    }

    public void loadData() {
        importList = importDAO.getData();
    }

    public int setTotalPrice(String id, int total) {
        return importDAO.setTotalPrice(id, total);
    }

    public String createNewId() {
        loadData();
        int id = importList.size() + 1;
        return "PN" + String.format("%03d", id);
    }

    public void renderToTable(DefaultTableModel model, String employeeId) {
        model.setRowCount(0);
        loadData();

        for (ImportDTO importDTO : importList) {
            if (importDTO.getIsDeleted() == 0 && (employeeBUS.getTypeById(employeeId).equals("LNV02")
                    || importDTO.getEmployeeId().equals(employeeId))) {
                model.addRow(new Object[]{
                        importDTO.getImportId(),
                        importDTO.getEmployeeId(),
                        importDTO.getSupplierId(),
                        DateFormat.formatDate(importDTO.getImportDate()),
                        importDTO.getImportTotalPrice(),
                        importDTO.getImportStatus()
                });
            }
        }

        model.fireTableDataChanged();
    }
}