package DTO;

public class ExportDTO {
    private String exportId;
    private String employeeId;
    private String exportDate;
    private int totalQuantity;
    private String status;
    private int isDeleted;

    public ExportDTO() {
    }

    public ExportDTO(String exportId, String employeeId, String exportDate, int totalQuantity, String status, int isDeleted) {
        this.exportId = exportId;
        this.employeeId = employeeId;
        this.exportDate = exportDate;
        this.totalQuantity = totalQuantity;
        this.status = status;
        this.isDeleted = isDeleted;
    }

    public String getExportId() {
        return exportId;
    }

    public void setExportId(String exportId) {
        this.exportId = exportId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getExportDate() {
        return exportDate;
    }

    public void setExportDate(String exportDate) {
        this.exportDate = exportDate;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}