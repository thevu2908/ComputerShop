package DTO;

public class BillDetailDTO {
    private String billId;
    private String productId;
    private int quantity;

    public BillDetailDTO() {
    }
    
    public BillDetailDTO(String billId, String productId, int quantity) {
        this.billId = billId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public BillDetailDTO(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}