package DAO;

import DTO.ExportDetailDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ExportDetailDAO {
    public ArrayList<ExportDetailDTO> getData() {
        ArrayList<ExportDetailDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `chi_tiet_phieu_xuat`");

            while (rs.next()) {
                String exportId = rs.getString("ma_px");
                String productId = rs.getString("ma_sp");
                int quantity = rs.getInt("so_luong");

                list.add(new ExportDetailDTO(exportId, productId, quantity));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addExportDetail(ExportDetailDTO exportDetail) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "insert into `chi_tiet_phieu_xuat` values (?, ?, ?)";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, exportDetail.getExportId());
            ptmt.setString(2, exportDetail.getProductId());
            ptmt.setInt(3, exportDetail.getQuantity());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateExportDetailQuantity(ExportDetailDTO exportDetail) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `chi_tiet_phieu_xuat` set so_luong = ? where ma_px = ? and ma_sp = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setInt(1, exportDetail.getQuantity());
            ptmt.setString(2, exportDetail.getExportId());
            ptmt.setString(3, exportDetail.getProductId());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteExportDetail(String exportId, String productId) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "delete from `chi_tiet_phieu_xuat` where ma_px = ? and ma_sp = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, exportId);
            ptmt.setString(2, productId);

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}