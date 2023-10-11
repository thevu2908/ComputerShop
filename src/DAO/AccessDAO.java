package DAO;

import DTO.AccessDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AccessDAO {
    public ArrayList<AccessDTO> getData() {
        ArrayList<AccessDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `loai_nhan_vien`");

            while (rs.next()) {
                String id = rs.getString("ma_lnv");
                String name = rs.getString("ten_lnv");
                int isDeleted = rs.getInt("trang_thai");

                list.add(new AccessDTO(id, name, isDeleted));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}