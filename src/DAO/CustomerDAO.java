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

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addCustomer(CustomerDTO customer) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "insert into `khach_hang` values (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, customer.getCustomerId());
            ptmt.setString(2, customer.getCustomerName());
            ptmt.setString(3, customer.getCustomerAddress());
            ptmt.setString(4, customer.getCustomerPhone());
            ptmt.setString(5, customer.getCustomerDOB());
            ptmt.setString(6, customer.getCustomerGender());
            ptmt.setInt(7, customer.getCustomerPoint());
            ptmt.setInt(8, customer.getIsDelete());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateCustomer(CustomerDTO customer) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `khach_hang` set ho_ten = ?, dia_chi = ?, so_dien_thoai = ?, ngay_sinh = ?, gioi_tinh = ?, " +
                    "diem_tich_luy = ? where ma_kh = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, customer.getCustomerName());
            ptmt.setString(2, customer.getCustomerAddress());
            ptmt.setString(3, customer.getCustomerPhone());
            ptmt.setString(4, customer.getCustomerDOB());
            ptmt.setString(5, customer.getCustomerGender());
            ptmt.setInt(6, customer.getCustomerPoint());
            ptmt.setString(7, customer.getCustomerId());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updatePoint(String customerId, int point) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `khach_hang` set `diem_tich_luy` = ? where `ma_kh` = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setInt(1, point);
            ptmt.setString(2, customerId);

            return ptmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteCustomer(String customerId) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `khach_hang` set trang_thai = 1 where ma_kh = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, customerId);

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}