package BUS;

import DTO.BillDetailDTO;
import DTO.CustomerDTO;
import DTO.ProductDTO;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class SellBUS {
    private ArrayList<BillDetailDTO> billDetailList = new ArrayList<>();
    private ProductBUS productBUS;
    private BillBUS billBUS;
    private BillDetailBUS billDetailBUS;
    private CustomerBUS customerBUS;
    private SaleBUS saleBUS;

    public SellBUS() {
        productBUS = new ProductBUS();
        billDetailBUS = new BillDetailBUS();
        billBUS = new BillBUS();
        customerBUS = new CustomerBUS();
        saleBUS = new SaleBUS();
    }

    public void chooseProduct(DefaultTableModel model, String productId, int quantity) {
        String billId = billBUS.getNewBillId();
        BillDetailDTO billDetailDTO = new BillDetailDTO(billId, productId, quantity);

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
        JOptionPane.showMessageDialog(null, "Bỏ chọn sản phẩm thành công");
        billDetailBUS.renderToTable(model, billDetailList);
    }

    public int calculateTotalPrice() {
        int total = 0;
        for (BillDetailDTO billDetailDTO : billDetailList) {
            String saleId = productBUS.getProductById(billDetailDTO.getProductId()).getSaleId();
            int discount = saleId == null || saleId.equals("")
                    ? 0
                    : Integer.parseInt(saleBUS.getSaleById(saleId).getSaleInfo().substring(
                        0,
                        saleBUS.getSaleById(saleId).getSaleInfo().length() - 1));

            int price = productBUS.getPriceById(billDetailDTO.getProductId());
            price = price - (price * discount / 100);

            total += price * billDetailDTO.getQuantity();
        }
        return total;
    }

    public boolean addBillDetail() {
        for (BillDetailDTO billDetailDTO : billDetailList) {
            if (!billDetailBUS.addBillDetail(billDetailDTO)) {
                JOptionPane.showMessageDialog(null, "Thất bại khi thêm chi tiết hóa đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public boolean decreaseProductQuantity() {
        for (BillDetailDTO billDetailDTO : billDetailList) {
            String productId = billDetailDTO.getProductId();
            ProductDTO product = productBUS.getProductById(productId);

            if (!productBUS.updateProductQuantity(productId, product.getProductQuantity() - billDetailDTO.getQuantity())) {
                JOptionPane.showMessageDialog(null, "Thất bại khi cập nhật số lượng sản phẩm sau khi thanh toán", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return true;
    }

    public boolean checkCustomerPhone(String phone) {
        if (phone.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số điện thoại của khách hàng", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ (10 chữ số và bắt đầu bằng số 0)", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        CustomerDTO customerDTO = customerBUS.getCustomerByPhone(phone);

        if (customerDTO == null) {
            JOptionPane.showMessageDialog(null, "Không có khách hàng nào có số điện thoại này", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    public void showInfoCustomer(String phone) {
        if (!checkCustomerPhone(phone)) {
            return;
        }

        CustomerDTO customerDTO = customerBUS.getCustomerByPhone(phone);

        JOptionPane.showMessageDialog(
                null,
                String.format(
                        " Mã khách hàng: \t%s\n Tên khách hàng: \t%s\n Địa chỉ: \t%s\n Số điện thoại: \t%s\n Ngày sinh: \t%s\n Giới tính: \t%s\n Điểm tích lũy: \t%s\n"
                        , customerDTO.getCustomerId(), customerDTO.getCustomerName(), customerDTO.getCustomerAddress(), customerDTO.getCustomerPhone(), customerDTO.getCustomerDOB(), customerDTO.getCustomerGender(), customerDTO.getCustomerPoint()
                ),
                "Thông tin khách hàng",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void resetBillDetailList() {
        billDetailList.removeAll(billDetailList);
    }

    public void renderToTableBillSell(DefaultTableModel model){
        billDetailBUS.renderToTable(model, billDetailList);
    }
}