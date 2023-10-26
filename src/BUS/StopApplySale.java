package BUS;

import DAO.SaleDAO;
import DTO.SaleDTO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

public class StopApplySale extends TimerTask {
    private ArrayList<SaleDTO> salesList;
    private SaleDAO saleDAO;
    private ProductBUS productBUS;

    public StopApplySale() {
        saleDAO = new SaleDAO();
        productBUS = new ProductBUS();
        salesList = saleDAO.getData();
    }

    @Override
    public void run() {
        try {
            Date currentDate = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            for (SaleDTO saleDTO : salesList) {
                if (currentDate.after(df.parse(saleDTO.getEndDate()))) {
                    saleDAO.stopApplySale(saleDTO.getSaleId());
                    productBUS.stopApplySaleToProduct();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}