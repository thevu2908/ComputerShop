package DTO;

public class ProductDTO{
    private String productId;
    private String productType;
    private String saleId;
    private String productName;
    private int productPrice;
    private String productCPU;
    private String productRAM;
    private String productStorage;
    private String productScreen;
    private String productScreenCard;
    private int productQuantity;
    private int isDeleted;

    public ProductDTO() {
    }

    public ProductDTO(String productId, String productType, String saleId, String productName, int productPrice, String productCPU,
                      String productRAM, String productStorage, String productScreen, String productScreenCard, int productQuantity,
                      int isDeleted) {
        this.productId = productId;
        this.productType = productType;
        this.saleId = saleId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCPU = productCPU;
        this.productRAM = productRAM;
        this.productStorage = productStorage;
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

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
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

    public String getProductStorage() {
        return productStorage;
    }

    public void setProductStorage(String productStorage) {
        this.productStorage = productStorage;
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