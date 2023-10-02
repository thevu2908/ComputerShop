package DAO;

import DTO.EmployeeDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class EmployeeDAO {
    public ArrayList<EmployeeDTO> getData() {
        ArrayList<EmployeeDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `nhan_vien`");

            while (rs.next()) {
                String id = rs.getString("ma_nv");
                String type = rs.getString("ma_lnv");
                String name = rs.getString("ho_ten");
                String address = rs.getString("dia_chi");
                String phone = rs.getString("so_dien_thoai");
                String dob = rs.getString("ngay_sinh");
                String gender = rs.getString("gioi_tinh");
                String email = rs.getString("email");
                String password = rs.getString("mat_khau");
                int isDeleted = rs.getInt("trang_thai");

                list.add(new EmployeeDTO(id, type, name, address, phone, dob, gender, email, password, isDeleted));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}