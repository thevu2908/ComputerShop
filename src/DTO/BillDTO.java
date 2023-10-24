package DTO;

public class BillDTO {
    private String billId;
    private String customerId;
    private String employeeId;
    private String billDate;
    private int total;
    private int discount;
    private int isDeleted;

    public BillDTO() {
    }

    public BillDTO(String billId, String customerId, String employeeId, String billDate, int total, int discount, int isDeleted) {
        this.billId = billId;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.billDate = billDate;
        this.total = total;
        this.discount = discount;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}