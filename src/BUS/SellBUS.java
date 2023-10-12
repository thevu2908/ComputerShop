package BUS;

import DTO.BillDetailDTO;
import DTO.CustomerDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class SellBUS {
    private ArrayList<BillDetailDTO> billDetailList = new ArrayList<>();
    private ProductBUS productBUS;
    private BillBUS billBUS;
    private BillDetailBUS billDetailBUS;
    private CustomerBUS customerBUS;

    public SellBUS() {
        productBUS = new ProductBUS();
        billDetailBUS = new BillDetailBUS();
        billBUS = new BillBUS();
        customerBUS = new CustomerBUS();
    }

    public void chooseProduct(DefaultTableModel model, String productId, int quantity) {
        String billId = billBUS.getNewBillId();
        BillDetailDTO billDetailDTO = new BillDetailDTO(billId,productId, quantity);
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

//    public void paySellBill (String employeeId,DefaultTableModel modelBillDetail,DefaultTableModel modelProduct,String phone, int finalTotal){
//        CustomerDTO customerDTO = customerBUS.getCustomerByPhone(phone);
//        String billId = billBUS.getNewBillId();
//        int res = JOptionPane.showConfirmDialog(null,"Bạn có chắc chắn muốn thanh toán ?","Question", JOptionPane.YES_NO_OPTION);
//        if(res == JOptionPane.YES_OPTION){
//            billBUS.addBill(billId,employeeId,customerDTO.getCustomerId(),finalTotal);
//            billDetailBUS.addBillDetail(billDetailList);
//            productBUS.decreaseQuantityProduct(billDetailList);
//            resetBillDetailList();
//            billDetailBUS.renderToTable(modelBillDetail,billDetailList);
//            productBUS.renderToProductTable(modelProduct);
//        }
//
//    }

    public void addBillDetailList(){
        if(!billDetailBUS.addBillDetail(billDetailList)){
            JOptionPane.showMessageDialog(null, "Thất bại khi thêm chi tiết hóa đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void decreaseQuantityProduct(){
        if(!productBUS.decreaseQuantityProduct(billDetailList)){
            JOptionPane.showMessageDialog(null, "Thất bại khi cập nhật số lượng sản phẩm sau khi thanh toán", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void renderToTableBillSell(DefaultTableModel model){
        billDetailBUS.renderToTable(model,billDetailList);
    }





    public void resetBillDetailList(){
        billDetailList.removeAll(billDetailList);
    }

}