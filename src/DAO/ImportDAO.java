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

    public int setImportTotal(String id, int total) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `phieu_nhap` set tong_tien = ? where ma_pn = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setInt(1, total);
            ptmt.setString(2, id);

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}