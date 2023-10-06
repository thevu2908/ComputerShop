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

    public int getIndexProductById(String productId){
        for(ProductDTO item : productList){
            if(item.getProductId().equals(productId)){
                return productList.indexOf(item);
            }
        }
        return -1;
    }

    public boolean insertBillSellItem(BillSellDTO billSellDTO){
        String productId = billSellDTO.getProductId();
        int quantityInsert = billSellDTO.getQuantity(); // Này là số lượng sản phẩm thêm vào

        // vòng lặp để kiểm tra số lượng sản phẩm mua có vượt quá số lượng sản phẩm hiện có của cửa hàng nếu có thì báo lỗi và return
        for(ProductDTO item : productList){
            if(item.getProductId().equals(productId)){
                if(quantityInsert > item.getProductQuantity()){
                    JOptionPane.showMessageDialog(null, "Sản phẩm còn lại không đủ để thực hiện mua hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }

        // vòng lặp kiểm tra sản phẩm thêm vào hóa đơn đã có trong hóa đơn chưa, nếu có thì tăng số lượng lên và kèm theo số lượng không vượt quá 10
        for (BillSellDTO item : billSellList) {
            if(item.getProductId().equals(productId)){
                int indexBillSellItem = getIndexBillSellItemById(productId); // này là chỉ số của phần tử trong hóa đơn thanh toán
                int quantityExist = getBillSellItemById(productId).getQuantity(); // Này là số lượng sản phẩm đã tồn tại trong hóa đơn thanh toán
                if(quantityExist + quantityInsert > 10){
                    JOptionPane.showMessageDialog(null, "Chỉ có thể mua 10 sản phẩm một lần", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }else{
                    billSellDTO.setQuantity(quantityExist + quantityInsert);
                    billSellDTO.setTotalPrice(calculateTotalBillSellItem(billSellDTO.getProductPrice(),billSellDTO.getQuantity()));
                    increaseProductList(productId,quantityInsert);
                    billSellList.set(indexBillSellItem,billSellDTO);
                    return true;
                }
            }
        }

        billSellList.add(billSellDTO);
        increaseProductList(productId,quantityInsert);
        return true;
    }

    public void increaseProductList(String productId,int quantityInsert){
        ProductDTO product = getProductById(productId); // biến này dùng để trừ đi số lượng sản phẩm chọn mua vào trong danh sách số lượng sản phẩm của cửa hàng
        int indexProduct = getIndexProductById(productId); // này là chỉ số của phần tử sản phẩm nằm trong danh sách sản phẩm hiện có của cửa hàng
        int quantityProductRemain = product.getProductQuantity(); // này là số lượng sản phẩm nằm trong danh sách sản phẩm hiện có của cửa hàng
        product.setProductQuantity(quantityProductRemain - quantityInsert);
        productList.set(indexProduct,product);
    }

    public void decreaseProductList(String productId, int quantityRemove){
        ProductDTO product = getProductById(productId); // biến này dùng để trừ đi số lượng sản phẩm  chọn mua vào trong danh sách số lượng sản phẩm của cửa hàng
        int indexProduct = getIndexProductById(productId); // này là chỉ số của phần tử sản phẩm nằm trong danh sách sản phẩm hiện có của cửa hàng
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
