package DTO;

public class EmployeeDTO {
    private String employeeId;
    private String employeeType;
    private String employeeName;
    private String employeeAddress;
    private String employeePhone;
    private String employeeDOB;
    private String employeeGender;
    private String employeeEmail;
    private String employeePassword;
    private int isDeleted;

    public EmployeeDTO() {
    }

    public EmployeeDTO(String employeeId, String employeeType, String employeeName, String employeeAddress, String employeePhone,
                       String employeeDOB, String employeeGender, String employeeEmail, String employeePassword, int isDeleted) {
        this.employeeId = employeeId;
        this.employeeType = employeeType;
        this.employeeName = employeeName;
        this.employeeAddress = employeeAddress;
        this.employeePhone = employeePhone;
        this.employeeDOB = employeeDOB;
        this.employeeGender = employeeGender;
        this.employeeEmail = employeeEmail;
        this.employeePassword = employeePassword;
        this.isDeleted = isDeleted;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }


    public void setEmployeeDOB(String employeeDOB) {
        this.employeeDOB = employeeDOB;
    }

    public String getEmployeeDOB() {
        return employeeDOB;
    }

    public String getEmployeeGender() {
        return employeeGender;
    }

    public void setEmployeeGender(String employeeGender) {
        this.employeeGender = employeeGender;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}