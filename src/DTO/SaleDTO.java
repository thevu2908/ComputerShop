package DTO;

public class SaleDTO {
    private String saleId;
    private String saleInfo;
    private String startDate;
    private String endDate;
    private String saleStatus;
    private int isDeleted;

    public SaleDTO() {
    }

    public SaleDTO(String saleId, String saleInfo, String startDate, String endDate, String saleStatus, int isDeleted) {
        this.saleId = saleId;
        this.saleInfo = saleInfo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.saleStatus = saleStatus;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(String saleStatus) {
        this.saleStatus = saleStatus;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}