package DTO;

public class EmployeeTypeDTO {
    private String typeId;
    private String typeName;
    private int isDeleted;

    public EmployeeTypeDTO() {

    }
    public EmployeeTypeDTO(String typeName) {
        this.typeName = typeName;
    }
    public EmployeeTypeDTO(String typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public EmployeeTypeDTO(String typeId, String typeName, int isDeleted) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.isDeleted = isDeleted;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return this.typeId + " " + this.typeName + " " + this.isDeleted;
    }
}