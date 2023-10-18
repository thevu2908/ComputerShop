package DAO;

import DTO.EmployeeTypeDTO;
import connection.MyConnection;

import java.sql.*;
import java.util.ArrayList;

public class EmployeeTypeDAO {
    public ArrayList<EmployeeTypeDTO> getData() {
        ArrayList<EmployeeTypeDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `loai_nhan_vien`");

            while (rs.next()) {
                String id = rs.getString("ma_lnv");
                String name = rs.getString("ten_lnv");
                int isDeleted = rs.getInt("trang_thai");

                list.add(new EmployeeTypeDTO(id, name, isDeleted));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addEmployeeType(EmployeeTypeDTO employeeTypeDTO) {
        MyConnection myConnection = new MyConnection();
        try {
            Connection connection = myConnection.getConnect();
            String sql = "insert into `loai_nhan_vien` value (?,?,?)";
            PreparedStatement st = connection.prepareStatement(sql);

            st.setString(1,employeeTypeDTO.getTypeId());
            st.setString(2,employeeTypeDTO.getTypeName());
            st.setInt(3,employeeTypeDTO.getIsDeleted());

            return st.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}