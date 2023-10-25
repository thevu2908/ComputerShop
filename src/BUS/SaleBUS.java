package BUS;

import DAO.SaleDAO;
import DTO.SaleDTO;

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
}