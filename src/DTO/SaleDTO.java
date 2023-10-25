package DTO;

public class SaleDTO {
    private String saleId;
    private String saleInfo;
    private int isDeleted;

    public SaleDTO() {
    }

    public SaleDTO(String saleId, String saleInfo, int isDeleted) {
        this.saleId = saleId;
        this.saleInfo = saleInfo;
        this.isDeleted = isDeleted;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public String getSaleInfo() {
        return saleInfo;
    }

    public void setSaleInfo(String saleInfo) {
        this.saleInfo = saleInfo;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}