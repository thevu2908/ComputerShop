package BUS;

import DTO.BillDetailDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class SellBUS {
    private ArrayList<BillDetailDTO> billDetailList = new ArrayList<>();
    private ProductBUS productBUS;
    private BillDetailBUS billDetailBUS;

    public SellBUS() {
        productBUS = new ProductBUS();
        billDetailBUS = new BillDetailBUS();
    }

    public void chooseProduct(DefaultTableModel model, String productId, int quantity) {
        BillDetailDTO billDetailDTO = new BillDetailDTO(productId, quantity);

        if (billDetailDTO != null) {
            boolean flag = true;

            for (BillDetailDTO billDetail : billDetailList) {
                if (billDetail.getProductId().equals(productId)) {
                    billDetail.setQuantity(billDetail.getQuantity() + quantity);
                    flag = false;
                    break;
                }
            }

            if (flag) {
                billDetailList.add(billDetailDTO);
            }

            billDetailBUS.renderToTable(model, billDetailList);
            JOptionPane.showMessageDialog(null, "Chọn sản phẩm thành công");
        } else {
            JOptionPane.showMessageDialog(null, "Chọn sản phẩm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void unchooseProduct(DefaultTableModel model, String productId) {
        for (BillDetailDTO billDetailDTO : billDetailList) {
            if (billDetailDTO.getProductId().equals(productId)) {
                billDetailList.remove(billDetailDTO);
                break;
            }
        }
        billDetailBUS.renderToTable(model, billDetailList);
    }

    public int calculateTotalPrice() {
        int total = 0;
        for (BillDetailDTO billDetailDTO : billDetailList) {
            total += productBUS.getPriceById(billDetailDTO.getProductId()) * billDetailDTO.getQuantity();
        }
        return total;
    }
}