package BUS;

import DAO.BillDetailDAO;
import DTO.BillDTO;
import DTO.BillDetailDTO;
import utils.DateTime;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class BillDetailBUS {
    private ArrayList<BillDetailDTO> billDetailList;
    private ProductBUS productBUS;

    private BillDetailDAO billDetailDAO;

    public BillDetailBUS() {
        productBUS = new ProductBUS();
        billDetailDAO = new BillDetailDAO();
    }

    public void renderToTable(DefaultTableModel model, ArrayList<BillDetailDTO> list) {
        model.setRowCount(0);

        for (BillDetailDTO billDetailDTO : list) {
            int price = productBUS.getPriceById(billDetailDTO.getProductId());
            int quantity = billDetailDTO.getQuantity();
            model.addRow(new Object[]{
                    billDetailDTO.getProductId(),
                    productBUS.getNameById(billDetailDTO.getProductId()),
                    price,
                    quantity,
                    price * quantity
            });
        }

        model.fireTableDataChanged();
    }

    public boolean addBillDetail(ArrayList<BillDetailDTO> billDetailList) {
        boolean flag= true;
        for(BillDetailDTO billDetailDTO : billDetailList){
            if(billDetailDAO.addBillDetail(billDetailDTO) == 1){
                continue;
            }else{
                flag=false;
            }
        }
        return flag;
    }


}