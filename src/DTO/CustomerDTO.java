package DTO;

public class CustomerDTO {
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String customerDOB;
    private String customerGender;
    private int customerPoint;
    private int isDelete;

    public CustomerDTO() {

    }
    public CustomerDTO(String customerId, String customerName, String customerAddress, String customerPhone,
                       String customerDOB,String customerGender, int customerPoint, int isDelete) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPhone = customerPhone;
        this.customerDOB = customerDOB;
        this.customerGender = customerGender;
        this.customerPoint = customerPoint;
        this.isDelete = isDelete;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerGender() {
        return customerGender;
    }

    public void setCustomerGender(String customerGender) {
        this.customerGender = customerGender;
    }

    public String getCustomerDOB() {
        return customerDOB;
    }

    public void setCustomerBOB(String customerDOB) {
        this.customerDOB = customerDOB;
    }

    public int getCustomerPoint() {
        return customerPoint;
    }

    public void setCustomerPoint(int customerAccumulatedPoints) {
        this.customerPoint = customerAccumulatedPoints;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}