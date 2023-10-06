package BUS;

import DTO.BillSellDTO;
import DTO.ProductDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class SellBUS {
    private ArrayList<BillSellDTO> billSellList;
    private ProductBUS productBUS;
    private ArrayList<ProductDTO> productList;

    public final String ERROR_EMPTY_SELECT_ITEM = "Vui lòng chọn sản phẩm";
    public final String ERROR_EMPTY_QUANTITY = "Vui lòng chọn số lượng trước khi thêm vào hóa đơn";
    public final String ERROR_EXCEED_QUANTITY_REMAIN = "Sản phẩm còn lại không đủ";
    public final String ERROR_EXCEED_QUANTITY = "Chỉ có thể mua 10 sản phẩm một lần";
    public final String SUCCESS_CHOOSE = "Sản phẩm đã được thêm vào hóa đơn";

    public SellBUS() {
        billSellList = new ArrayList<>();
        productBUS = new ProductBUS();
        productList = productBUS.getProductList();
    }

    public int calculateTotalBillSellItem(int price, int quantity){
        return price * quantity;
    }

    public int calculateTotalBillSell(){
        int total = 0;
        for (BillSellDTO billSellDTO : billSellList) {
            total += billSellDTO.getTotalPrice();
        }
        return total;
    }

    public BillSellDTO getBillSellItemById(String productId){
        for (BillSellDTO billSellDTO : billSellList) {
            if(billSellDTO.getProductId().equals(productId)){
                return billSellDTO;
            }
        }
        return null;
    }

    public int getIndexBillSellItemById(String productId){
        for (BillSellDTO billSellDTO : billSellList) {
            if(billSellDTO.getProductId().equals(productId)){
                return billSellList.indexOf(billSellDTO);
            }
        }
        return -1;
    }


    public ProductDTO getProductById(String productId){
        for(ProductDTO item : productList){
            if(item.getProductId().equals(productId)){
                return item;
            }
        }
        return null;
    }

    public boolean insertBillSellItem(BillSellDTO billSellDTO){
        String productId = billSellDTO.getProductId();
        int quantityInsert = billSellDTO.getQuantity(); // Này là số lượng sản phẩm thêm vào

        // Lấy sản phẩm trong productList dựa vào productId của sản phẩm chọn thêm vào hóa đơn
        ProductDTO product = getProductById(productId);

        // kiểm tra số lượng sản phẩm mua có vượt quá số lượng sản phẩm hiện có của cửa hàng nếu có thì báo lỗi và return
        if(quantityInsert > product.getProductQuantity()){
            JOptionPane.showMessageDialog(null, ERROR_EXCEED_QUANTITY_REMAIN, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Điều kiện nếu tiền sản phẩm thêm vào + với tổng tiền trong hóa đơn lớn hơn max value thì bắt tạo hóa đơn mới
        if(calculateTotalBillSell() + calculateTotalBillSellItem(billSellDTO.getProductPrice(),quantityInsert) > Long.MAX_VALUE ){
            JOptionPane.showMessageDialog(null, "Giá trị hóa đơn đã tối đa vui lòng tạo hóa đơn mới", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }


        // kiểm tra sản phẩm thêm vào hóa đơn đã có trong hóa đơn chưa, nếu có thì tăng số lượng lên và kèm theo số lượng không vượt quá 10
        BillSellDTO billSellItem = getBillSellItemById(productId);

        if(billSellItem != null){
            int indexBillSellItem = getIndexBillSellItemById(productId); // này là chỉ số của phần tử trong hóa đơn thanh toán
            int quantityExist = getBillSellItemById(productId).getQuantity(); // Này là số lượng sản phẩm đã tồn tại trong hóa đơn thanh toán
//            if(quantityExist + quantityInsert > 10){
//                JOptionPane.showMessageDialog(null, "Chỉ có thể mua 10 sản phẩm một lần", "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return false;
//            }else{
                billSellDTO.setQuantity(quantityExist + quantityInsert);
                billSellDTO.setTotalPrice(calculateTotalBillSellItem(billSellDTO.getProductPrice(),billSellDTO.getQuantity()));
                increaseProductList(productId,quantityInsert);
                billSellList.set(indexBillSellItem,billSellDTO);
                return true;
//            }
        }


        billSellList.add(billSellDTO);
        increaseProductList(productId,quantityInsert);
        return true;
    }

    public void increaseProductList(String productId,int quantityInsert){

        ProductDTO product = getProductById(productId); // biến này dùng để trừ đi số lượng sản phẩm chọn mua vào trong danh sách số lượng sản phẩm của cửa hàng
        int indexProduct = productList.indexOf(product); // này là chỉ số của phần tử sản phẩm nằm trong danh sách sản phẩm hiện có của cửa hàng
        System.out.println(productList.get(indexProduct).getProductQuantity());
        int quantityProductRemain = product.getProductQuantity(); // này là số lượng sản phẩm nằm trong danh sách sản phẩm hiện có của cửa hàng
        product.setProductQuantity(quantityProductRemain - quantityInsert);
        productList.set(indexProduct,product);
        System.out.println(productList.get(indexProduct).getProductQuantity());
    }

    public void decreaseProductList(String productId, int quantityRemove){
        ProductDTO product = getProductById(productId); // biến này dùng để trừ đi số lượng sản phẩm  chọn mua vào trong danh sách số lượng sản phẩm của cửa hàng
        int indexProduct = productList.indexOf(product); // này là chỉ số của phần tử sản phẩm nằm trong danh sách sản phẩm hiện có của cửa hàng
        int quantityProductRemain = product.getProductQuantity(); // này là số lượng sản phẩm nằm trong danh sách sản phẩm hiện có của cửa hàng
        product.setProductQuantity(quantityProductRemain + quantityRemove);
        productList.set(indexProduct,product);
    }

    public boolean removeBillSellItem(String productId){
        BillSellDTO billSellDTO = getBillSellItemById(productId);
        int quantityRemove = billSellDTO.getQuantity(); // Này là số lượng sản phẩm bỏ chọn

        boolean resultRemove = billSellList.remove(billSellDTO);
        if(resultRemove){
            decreaseProductList(productId,quantityRemove);
            return true;
        }
        return false;
    }

    public void renderSellOrderTable(DefaultTableModel model){
        model.setRowCount(0);

        for (BillSellDTO billSellDTO : billSellList) {
            model.addRow(new Object[]{
                    billSellDTO.getProductId(),
                    billSellDTO.getProductName(),
                    billSellDTO.getProductPrice(),
                    billSellDTO.getQuantity(),
                    billSellDTO.getTotalPrice()
            });
        }

        model.fireTableDataChanged();
    }
}
