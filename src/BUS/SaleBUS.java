package BUS;

import DAO.SaleDAO;
import DTO.SaleDTO;
import utils.DateTime;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class SaleBUS {
    private ArrayList<SaleDTO> salesList;
    private SaleDAO saleDAO;

    public SaleBUS() {
        saleDAO = new SaleDAO();
    }

    public void loadData() {
        salesList = saleDAO.getData();
    }

    public SaleDTO getSaleById(String id) {
        loadData();

        for (SaleDTO saleDTO : salesList) {
            if (saleDTO.getSaleId().equals(id)) {
                return saleDTO;
            }
        }

        return null;
    }

    public void renderToTable(DefaultTableModel model) {
        loadData();
        model.setRowCount(0);

        for (SaleDTO saleDTO : salesList) {
            if (saleDTO.getIsDeleted() == 0) {
                model.addRow(new Object[]{
                        saleDTO.getSaleId(),
                        saleDTO.getSaleInfo(),
                        DateTime.formatDate(saleDTO.getStartDate()),
                        DateTime.formatDate(saleDTO.getEndDate()),
                        saleDTO.getSaleStatus()
                });
            }
        }
    }
}