package DAO;

import DTO.ExportDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ExportDAO {
    public ArrayList<ExportDTO> getData() {
        ArrayList<ExportDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `phieu_xuat`");

            while (rs.next()) {
                String exportId = rs.getString("ma_px");
                String employeeId = rs.getString("ma_nv");
                String date = rs.getString("ngay_xuat");
                int quantity = rs.getInt("tong_so_luong");
                String status = rs.getString("tinh_trang");
                int isDeleted = rs.getInt("trang_thai");

                list.add(new ExportDTO(exportId, employeeId, date, quantity, status, isDeleted));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int setTotalQuantity(String id, int total) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `phieu_xuat` set tong_so_luong = ? where ma_px = ?";
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