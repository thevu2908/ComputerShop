package DAO;

import DTO.SaleDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SaleDAO {
    public ArrayList<SaleDTO> getData() {
        ArrayList<SaleDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `chuong_trinh_khuyen_mai`");

            while (rs.next()) {
                String id = rs.getString("ma_ctkm");
                String info = rs.getString("thong_tin");
                int isDeleted = rs.getInt("trang_thai");

                list.add(new SaleDTO(id, info, isDeleted));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}