package BUS;

import DAO.ExportDetailDAO;
import DTO.ExportDetailDTO;
import DTO.ImportDetailDTO;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ExportDetailBUS {
    private ArrayList<ExportDetailDTO> exportDetailList;
    private ExportDetailDAO exportDetailDAO;
    private ProductBUS productBUS;
    private ExportBUS exportBUS;

    public ExportDetailBUS() {
        exportDetailDAO = new ExportDetailDAO();
        productBUS = new ProductBUS();
        exportBUS = new ExportBUS();
    }

    public void loadData() {
        exportDetailList = exportDetailDAO.getData();
    }

    public int calculateTotalQuantity(String exportId) {
        int total = 0;
        for (ExportDetailDTO exportDetailDTO : exportDetailList) {
            total += exportDetailDTO.getQuantity();
        }
        exportBUS.setTotalQuantity(exportId, total);
        return total;
    }

    public void renderToTable(DefaultTableModel model, String exportId) {
        model.setRowCount(0);
        loadData();

        for (ExportDetailDTO exportDetailDTO : exportDetailList) {
            if (exportDetailDTO.getExportId().equals(exportId)) {
                model.addRow(new Object[]{
                        exportDetailDTO.getProductId(),
                        productBUS.getNameById(exportDetailDTO.getProductId()),
                        exportDetailDTO.getQuantity()
                });
            }
        }

        model.fireTableDataChanged();
    }
}