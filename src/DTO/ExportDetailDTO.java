package DTO;

public class ExportDetailDTO {
    private String exportId;
    private String productId;
    private int quantity;

    public ExportDetailDTO() {
    }

    public ExportDetailDTO(String exportId, String productId, int quantity) {
        this.exportId = exportId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getExportId() {
        return exportId;
    }

    public void setExportId(String exportId) {
        this.exportId = exportId;
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