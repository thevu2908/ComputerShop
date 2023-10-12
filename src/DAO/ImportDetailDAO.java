package DAO;

import DTO.ImportDetailDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ImportDetailDAO {
    public ArrayList<ImportDetailDTO> getData() {
        ArrayList<ImportDetailDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `chi_tiet_phieu_nhap`");

            while (rs.next()) {
                String importId = rs.getString("ma_pn");
                String productId = rs.getString("ma_sp");
                int quantity = rs.getInt("so_luong");

                list.add(new ImportDetailDTO(importId, productId, quantity));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addImportDetail(ImportDetailDTO importDetail) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "insert into `chi_tiet_phieu_nhap` values (?, ?, ?)";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, importDetail.getImportId());
            ptmt.setString(2, importDetail.getProductId());
            ptmt.setInt(3, importDetail.getQuantity());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateImportDetailQuantity(ImportDetailDTO importDetail) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `chi_tiet_phieu_nhap` set so_luong = ? where ma_pn = ? and ma_sp = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setInt(1, importDetail.getQuantity());
            ptmt.setString(2, importDetail.getImportId());
            ptmt.setString(3, importDetail.getProductId());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteImportDetail(String importId, String productId) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "delete from `chi_tiet_phieu_nhap` where ma_pn = ? and ma_sp = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, importId);
            ptmt.setString(2, productId);

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}