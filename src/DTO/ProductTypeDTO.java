package DTO;

public class ProductTypeDTO {
    private String typeId;
    private String typeName;
    private int isDeleted;

    public ProductTypeDTO() {
    }

    public ProductTypeDTO(String typeId, String typeName, int isDeleted) {
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
}