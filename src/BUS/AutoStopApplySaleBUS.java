package BUS;

import DAO.SaleDAO;
import DTO.SaleDTO;
import utils.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;

public class AutoStopApplySaleBUS extends TimerTask {
    private ArrayList<SaleDTO> salesList;
    private SaleDAO saleDAO;
    private ProductBUS productBUS;

    public AutoStopApplySaleBUS() {
        saleDAO = new SaleDAO();
        productBUS = new ProductBUS();
        salesList = saleDAO.getData();
    }

    @Override
    public void run() {
        try {
            Date currentDate = DateTime.getCurrentDate();

            for (SaleDTO saleDTO : salesList) {
                if (
                        saleDTO.getSaleStatus().equals("Đang áp dụng")
                        &&
                        currentDate.after(DateTime.parseDate(saleDTO.getEndDate()))
                ) {
                    saleDAO.stopApplySale(saleDTO.getSaleId());
                    productBUS.autoStopApplySale();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}