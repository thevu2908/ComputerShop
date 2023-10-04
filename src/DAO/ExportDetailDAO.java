package DAO;

import DTO.ExportDetailDTO;
import connection.MyConnection;

import java.sql.Connection;
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
}