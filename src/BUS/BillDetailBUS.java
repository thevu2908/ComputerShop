package BUS;

import DAO.BillDetailDAO;
import DTO.BillDetailDTO;

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

    public boolean addBillDetail(BillDetailDTO billDetailDTO) {
        if (billDetailDAO.addBillDetail(billDetailDTO) > 0) {
           return true;
        } else {
            return false;
        }
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
}