package DTO;

public class RoleDTO {
    public String maLoaiNhanVien;
    public String tenLoaiNhanvien;
    public int trangThai;

    public RoleDTO(){

    }
    public RoleDTO(String maLoaiNhanVien, String tenLoaiNhanvien, int trangThai){
        this.maLoaiNhanVien = maLoaiNhanVien;
        this.tenLoaiNhanvien = tenLoaiNhanvien;
        this.trangThai = trangThai;
    }
    public String getMaLoaiNhanVien(){
        return maLoaiNhanVien;
    }
    public void setMaLoaiNhanVien(String maLoaiNhanVien){
        this.maLoaiNhanVien = maLoaiNhanVien;
    }
    public String getTenLoaiNhanvien(){
        return tenLoaiNhanvien;
    }
    public void setTenLoaiNhanvien(String tenLoaiNhanvien){
        this.tenLoaiNhanvien = tenLoaiNhanvien;
    }
    public int getTrangThai(){
        return trangThai;
    }
    public void setTrangThai(int trangThai){
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return this.maLoaiNhanVien + " " + this.tenLoaiNhanvien + " " + this.trangThai;
    }
}
