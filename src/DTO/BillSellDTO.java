package DTO;

public class BillSellDTO extends BillDetailDTO {
//    private String productId;
    private String productName;
    private int productPrice;
//    private int productQuantity;
    private long totalPrice;

    public BillSellDTO() {
    }

//    public BillSellDTO(String productId, String productName, int productPrice, int productQuantity, long totalPrice) {
//        this.productId = productId;
//        this.productName = productName;
//        this.productPrice = productPrice;
//        this.productQuantity = productQuantity;
//        this.totalPrice = totalPrice;
//    }

    public BillSellDTO( String productId,  String productName, int productPrice, int quantity, long totalPrice) {
        super( productId, quantity);
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalPrice = totalPrice;
    }

//    public String getProductId() {
//        return productId;
//    }

//    public void setProductId(String productId) {
//        this.productId = productId;
//    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

//    public int getProductQuantity() {
//        return productQuantity;
//    }
//
//    public void setProductQuantity(int productQuantity) {
//        this.productQuantity = productQuantity;
//    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
