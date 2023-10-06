package DAO;

import DTO.ImportDetailDTO;
import connection.MyConnection;

import java.sql.Connection;
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
}