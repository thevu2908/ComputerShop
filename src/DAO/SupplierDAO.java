package DAO;

import DTO.SupplierDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SupplierDAO {
    public ArrayList<SupplierDTO> getData() {
        ArrayList<SupplierDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `nha_cung_cap`");

            while (rs.next()) {
                String id = rs.getString("ma_ncc");
                String name = rs.getString("ho_ten");
                String address = rs.getString("dia_chi");
                String phone = rs.getString("so_dien_thoai");
                int isDeleted = rs.getInt("trang_thai");

                list.add(new SupplierDTO(id, name, address, phone, isDeleted));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}