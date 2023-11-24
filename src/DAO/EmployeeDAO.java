package DAO;

import DTO.EmployeeDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    public int addEmployee(EmployeeDTO t) {
        try {
            Connection connection = MyConnection.getConnect();
            String sql = "insert into `nhan_vien` values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ptmt = connection.prepareStatement(sql);

            ptmt.setString(1, t.getEmployeeId());
            ptmt.setString(2, t.getEmployeeType());
            ptmt.setString(3, t.getEmployeeName());
            ptmt.setString(4, t.getEmployeeAddress());
            ptmt.setString(5, t.getEmployeePhone());
            ptmt.setString(6, t.getEmployeeDOB());
            ptmt.setString(7, t.getEmployeeGender());
            ptmt.setString(8, t.getEmployeeEmail());
            ptmt.setString(9, t.getEmployeePassword());
            ptmt.setInt(10, 0);

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateEmployee(EmployeeDTO employee) {
       try {
           Connection connection = MyConnection.getConnect();
           String sql = "update `nhan_vien` set ho_ten = ?, dia_chi = ?, so_dien_thoai = ?, ngay_sinh = ?, gioi_tinh = ?, email = ?, " +
                   "mat_khau = ?, ma_lnv = ? where ma_nv = ?";
           PreparedStatement pst = connection.prepareStatement(sql);

           pst.setString(1, employee.getEmployeeName());
           pst.setString(2, employee.getEmployeeAddress());
           pst.setString(3, employee.getEmployeePhone());
           pst.setString(4, employee.getEmployeeDOB());
           pst.setString(5, employee.getEmployeeGender());
           pst.setString(6, employee.getEmployeeEmail());
           pst.setString(7, employee.getEmployeePassword());
           pst.setString(8, employee.getEmployeeType());
           pst.setString(9, employee.getEmployeeId());

           return pst.executeUpdate();
       } catch (Exception e) {
            e.printStackTrace();
            return 0;
       }
    }

    public int deleteEmployee(String id) {
       try {
            Connection connection = MyConnection.getConnect();
            String sql = "update `nhan_vien` set trang_thai = 1 where ma_nv = ?";
            PreparedStatement pst = connection.prepareStatement(sql);

            pst.setString(1, id);

            return pst.executeUpdate();
       } catch (Exception e) {
            e.printStackTrace();
            return 0;
       }
    }
}