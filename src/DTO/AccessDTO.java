package DTO;

public class AccessDTO {
    private String accessId;
    private String accessName;
    private int isDeleted;

    public AccessDTO() {
    }

    public AccessDTO(String accessId, String accessName, int isDeleted) {
        this.accessId = accessId;
        this.accessName = accessName;
        this.isDeleted = isDeleted;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }
}