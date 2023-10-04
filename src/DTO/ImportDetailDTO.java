package DTO;

public class ImportDetailDTO {
    private String importId;
    private String productId;
    private int quantity;

    public ImportDetailDTO() {
    }

    public ImportDetailDTO(String importId, String productId, int quantity) {
        this.importId = importId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getImportId() {
        return importId;
    }

    public void setImportId(String importId) {
        this.importId = importId;
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