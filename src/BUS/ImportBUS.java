package BUS;

import DAO.ImportDAO;
import DTO.ImportDTO;

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

    public void renderToTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadData();

        for (ImportDTO importDTO : importList) {
            model.addRow(new Object[]{
                    importDTO.getImportId(),
                    importDTO.getEmployeeId(),
                    importDTO.getSupplierId(),
                    importDTO.getImportDate(),
                    importDTO.getImportTotalPrice(),
                    importDTO.getImportStatus()
            });
        }

        model.fireTableDataChanged();
    }
}