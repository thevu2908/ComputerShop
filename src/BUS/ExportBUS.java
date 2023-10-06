package BUS;

import DAO.ExportDAO;
import DTO.ExportDTO;

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

    public void renderToTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadData();

        for (ExportDTO exportDetailDTO : exportDetailList) {
            if (exportDetailDTO.getIsDeleted() == 1) {
                model.addRow(new Object[]{
                        exportDetailDTO.getExportId(),
                        exportDetailDTO.getEmployeeId(),
                        exportDetailDTO.getExportDate(),
                        exportDetailDTO.getTotalQuantity(),
                        exportDetailDTO.getStatus()
                });
            }
        }

        model.fireTableDataChanged();
    }
}