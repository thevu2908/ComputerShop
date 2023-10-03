package DTO;

public class ProductDTO {
    private String productId;
    private String productType;
    private String productName;
    private int productPrice;
    private String productCPU;
    private String productRAM;
    private String productDisk;
    private String productScreen;
    private String productScreenCard;
    private int productQuantity;
    private int isDeleted;

    public ProductDTO() {
    }

    public ProductDTO(String productId, String productType, String productName, int productPrice, String productCPU,
                      String productRAM, String productDisk, String productScreen, String productScreenCard, int productQuantity,
                      int isDeleted) {
        this.productId = productId;
        this.productType = productType;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCPU = productCPU;
        this.productRAM = productRAM;
        this.productDisk = productDisk;
        this.productScreen = productScreen;
        this.productScreenCard = productScreenCard;
        this.productQuantity = productQuantity;
        this.isDeleted = isDeleted;
    }

    public ProductDTO(String productId, int productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

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

    public String getProductCPU() {
        return productCPU;
    }

    public void setProductCPU(String productCPU) {
        this.productCPU = productCPU;
    }

    public String getProductRAM() {
        return productRAM;
    }

    public void setProductRAM(String productRAM) {
        this.productRAM = productRAM;
    }

    public String getProductDisk() {
        return productDisk;
    }

    public void setProductDisk(String productDisk) {
        this.productDisk = productDisk;
    }

    public String getProductScreen() {
        return productScreen;
    }

    public void setProductScreen(String productScreen) {
        this.productScreen = productScreen;
    }

    public String getProductScreenCard() {
        return productScreenCard;
    }

    public void setProductScreenCard(String productScreenCard) {
        this.productScreenCard = productScreenCard;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}