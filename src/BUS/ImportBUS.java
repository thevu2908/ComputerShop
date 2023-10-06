package BUS;

import DAO.ImportDAO;
import DTO.ImportDTO;
import utils.DateFormat;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ImportBUS {
    private ImportDAO importDAO;
    private ArrayList<ImportDTO> importList;

    public ImportBUS() {
        importDAO = new ImportDAO();
    }

    public void loadData() {
        importList = importDAO.getData();
    }

    public int setTotalPrice(String id, int total) {
        return importDAO.setTotalPrice(id, total);
    }

    public void renderToTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadData();

        for (ImportDTO importDTO : importList) {
            if (importDTO.getIsDeleted() == 0) {
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