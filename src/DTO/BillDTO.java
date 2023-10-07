package DTO;

public class BillDTO {
    private String billId;
    private String customerId;
    private String employeeId;
    private String billDate;
    private int totalQuantity;
    private int isDeleted;

    public BillDTO() {

    }

    public BillDTO(String billId, String customerId, String employeeId, String billDate, int totalQuantity, int isDeleted) {
        this.billId = billId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.billDate = billDate;
        this.totalQuantity = totalQuantity;
        this.isDeleted = isDeleted;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}
