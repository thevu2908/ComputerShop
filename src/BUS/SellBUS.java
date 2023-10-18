package BUS;

import DTO.BillDetailDTO;
import DTO.CustomerDTO;
import DTO.ProductDTO;

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

    public boolean addBillDetail() {
        boolean flag = false;

        for (BillDetailDTO billDetailDTO : billDetailList) {
            if (billDetailBUS.addBillDetail(billDetailDTO)) {
                flag = true;
            } else {
                flag = false;
            }
        }

        if (!flag) {
            JOptionPane.showMessageDialog(null, "Thất bại khi thêm chi tiết hóa đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean decreaseProductQuantity() {
        boolean flag = false;

        for (BillDetailDTO billDetailDTO : billDetailList) {
            String productId = billDetailDTO.getProductId();
            ProductDTO product = productBUS.getProductById(productId);

            if (productBUS.updateProductQuantity(productId, product.getProductQuantity() - billDetailDTO.getQuantity())) {
                flag = true;
            } else {
                flag = false;
            }
        }

        if(!flag) {
            JOptionPane.showMessageDialog(null, "Thất bại khi cập nhật số lượng sản phẩm sau khi thanh toán", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void resetBillDetailList() {
        billDetailList.removeAll(billDetailList);
    }

    public void renderToTableBillSell(DefaultTableModel model){
        billDetailBUS.renderToTable(model, billDetailList);
    }

    public void showInfoCustomer(String phone){
        CustomerDTO customerDTO = customerBUS.getCustomerByPhone(phone);

        if (customerDTO == null) {
            JOptionPane.showMessageDialog(null, "Không có khách hàng nào có số điện thoại này", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(
                null,
                String.format(" Mã khách hàng: \t%s\n Tên khách hàng: \t%s\n Địa chỉ: \t%s\n Số điện thoại: \t%s\n Ngày sinh: \t%s\n Giới tính: \t%s\n Điểm tích lũy: \t%s\n"
                        ,customerDTO.getCustomerId(), customerDTO.getCustomerName(), customerDTO.getCustomerAddress(), customerDTO.getCustomerPhone(), customerDTO.getCustomerDOB(), customerDTO.getCustomerGender(), customerDTO.getCustomerPoint())
                ,
                "Thông tin khách hàng",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}