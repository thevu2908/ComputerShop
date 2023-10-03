package DTO;

public class ImportDTO {
    private String importId;
    private String employeeId;
    private String supplierId;
    private String importDate;
    private int importTotalPrice;
    private String importStatus;
    private int isDeleted;

    public ImportDTO() {
    }

    public ImportDTO(String importId, String employeeId, String supplierId, String importDate, int importTotalPrice,
                     String importStatus, int isDeleted) {
        this.importId = importId;
        this.employeeId = employeeId;
        this.supplierId = supplierId;
        this.importDate = importDate;
        this.importTotalPrice = importTotalPrice;
        this.importStatus = importStatus;
        this.isDeleted = isDeleted;
    }

    public String getImportId() {
        return importId;
    }

    public void setImportId(String importId) {
        this.importId = importId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getImportDate() {
        return importDate;
    }

    public void setImportDate(String importDate) {
        this.importDate = importDate;
    }

    public int getImportTotalPrice() {
        return importTotalPrice;
    }

    public void setImportTotalPrice(int importTotalPrice) {
        this.importTotalPrice = importTotalPrice;
    }

    public String getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}