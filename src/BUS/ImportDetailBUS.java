package BUS;

import DAO.ImportDetailDAO;
import DTO.ImportDetailDTO;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ImportDetailBUS {
    private ArrayList<ImportDetailDTO> importDetailList;
    private ImportDetailDAO importDetailDAO;
    private ProductBUS productBUS;
    private ImportBUS importBUS;

    public ImportDetailBUS() {
        importDetailDAO = new ImportDetailDAO();
        productBUS = new ProductBUS();
        importBUS = new ImportBUS();
    }

    public void loadData() {
        importDetailList = importDetailDAO.getData();
    }

    public int calculateTotalPrice(String importId) {
        int total = 0;
        for (ImportDetailDTO importDetailDTO : importDetailList) {
            int price = productBUS.getPriceById(importDetailDTO.getProductId());
            total += price * importDetailDTO.getQuantity();
        }
        importBUS.setTotalPrice(importId, total);
        return total;
    }

    public void renderToTable(DefaultTableModel model, String importId) {
        model.setRowCount(0);
        loadData();

        for (ImportDetailDTO importDetailDTO : importDetailList) {
            if (importDetailDTO.getImportId().equals(importId)) {
                int price = productBUS.getPriceById(importDetailDTO.getProductId());
                model.addRow(new Object[]{
                        importDetailDTO.getProductId(),
                        productBUS.getNameById(importDetailDTO.getProductId()),
                        price,
                        importDetailDTO.getQuantity(),
                        price * importDetailDTO.getQuantity()
                });
            }
        }

        model.fireTableDataChanged();
    }
}