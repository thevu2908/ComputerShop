package DAO;

import DTO.CustomerDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class CustomerDAO {
    public ArrayList<CustomerDTO> getData() {
        ArrayList<CustomerDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `khach_hang`");

            while (rs.next()) {
                String id = rs.getString("ma_kh");
                String name = rs.getString("ho_ten");
                String address = rs.getString("dia_chi");
                String phone = rs.getString("so_dien_thoai");
                String date = rs.getString("ngay_sinh");
                String sex = rs.getString("gioi_tinh");
                int point= rs.getInt("diem_tich_luy");
                int isDeleted = rs.getInt("trang_thai");

                list.add(new CustomerDTO(id, name, address, phone, date, sex,point ,isDeleted));
            }
            connection.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int updatePoint(String customerId, int point){
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `khach_hang` set `diem_tich_luy` = ? where `ma_kh` = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);
            ptmt.setInt(1, point);
            ptmt.setString(2, customerId);

            return ptmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

}
