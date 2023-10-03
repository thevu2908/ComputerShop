package DAO;

import DTO.ProductTypeDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductTypeDAO {
    public ArrayList<ProductTypeDTO> getData() {
        ArrayList<ProductTypeDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `hang_san_pham`");

            while (rs.next()) {
                String id = rs.getString("ma_hsp");
                String name = rs.getString("ten_hsp");
                int isDeleted = rs.getInt("trang_thai");

                list.add(new ProductTypeDTO(id, name, isDeleted));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}