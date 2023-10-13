package DAO;

import DTO.ImportDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ImportDAO {
    public ArrayList<ImportDTO> getData() {
        ArrayList<ImportDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `phieu_nhap`");

            while (rs.next()) {
                String importId = rs.getString("ma_pn");
                String employeeId = rs.getString("ma_nv");
                String supplierId = rs.getString("ma_ncc");
                String date = rs.getString("ngay_nhap");
                int total = rs.getInt("tong_tien");
                String status = rs.getString("tinh_trang");
                int isDeleted = rs.getInt("trang_thai");

                list.add(new ImportDTO(importId, employeeId, supplierId, date, total, status, isDeleted));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addImport(ImportDTO importDTO) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "insert into `phieu_nhap` values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, importDTO.getImportId());
            ptmt.setString(2, importDTO.getEmployeeId());
            ptmt.setString(3, importDTO.getSupplierId());
            ptmt.setString(4, importDTO.getImportDate());
            ptmt.setInt(5, importDTO.getImportTotalPrice());
            ptmt.setString(6, importDTO.getImportStatus());
            ptmt.setInt(7, importDTO.getIsDeleted());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateImport(ImportDTO importDTO) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `phieu_nhap` set ma_ncc = ?, ngay_nhap = ?, tong_tien = ?, tinh_trang = ? where ma_pn = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, importDTO.getSupplierId());
            ptmt.setString(2, importDTO.getImportDate());
            ptmt.setInt(3, importDTO.getImportTotalPrice());
            ptmt.setString(4, importDTO.getImportStatus());
            ptmt.setString(5, importDTO.getImportId());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int setTotalPrice(String id, int total) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `phieu_nhap` set tong_tien = ? where ma_pn = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setInt(1, total);
            ptmt.setString(2, id);

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}