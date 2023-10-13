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
    public EmployeeTypeDTO getById(String maLoaiNhanVien) {
        EmployeeTypeDTO employeeTypeDTO = new EmployeeTypeDTO();
        MyConnection myConnection = new MyConnection();

        try {
            Connection connection = myConnection.getConnect();
            String query = "select * from `loai_nhan_vien` where `ma_lnv` = ?";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1,maLoaiNhanVien);
            ResultSet rs = st.executeQuery();
            if (rs.next()){
                employeeTypeDTO.setTypeId(rs.getString("ma_lnv"));
                employeeTypeDTO.setTypeName(rs.getString("ten_lnv"));
                employeeTypeDTO.setIsDeleted(rs.getInt("trang_thai"));
            }
            else return null;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            myConnection.closeConnection();
        }
        return employeeTypeDTO;
    }
    public boolean addEmployeeType(EmployeeTypeDTO employeeTypeDTO) {
        boolean result = false;
        MyConnection myConnection = new MyConnection();

        try {
            Connection connection = myConnection.getConnect();
            String sql = "insert into `loai_nhan_vien` value (?,?,?)";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1,employeeTypeDTO.getTypeId());
            st.setString(2,employeeTypeDTO.getTypeName());
            st.setInt(3,employeeTypeDTO.getIsDeleted());

            if (st.executeUpdate()>=2)
                result = true;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        finally {
            myConnection.closeConnection();
        }
        return result;
    }

    public boolean updateEmployeeType(EmployeeTypeDTO employeeTypeDTO) {
        boolean result = false;
        MyConnection myConnection = new MyConnection();

        try {
            Connection connection = myConnection.getConnect();
            String sql = "update `loai_nhan_vien` set `ma_lnv` = ?, `ten_lnv` = ?,`trang_thai` = ? where `ma_lnv` = ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1,employeeTypeDTO.getTypeName());
            if (st.executeUpdate()>=2)
                result = true;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        finally {
            myConnection.closeConnection();
        }
        return result;
    }

    public boolean deleteEmployeeType(String maLoaiNhanVien) {
        boolean result = false;
        MyConnection myConnection = new MyConnection();

        try {
            Connection connection = myConnection.getConnect();
            String sql = "delete from `loai_nhan_vien` where `ma_lnv` = ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1,maLoaiNhanVien);
            if (st.executeUpdate()>=2)
                result = true;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        finally {
            myConnection.closeConnection();
        }
        return result;
    }

    public boolean checkExistById(String maLoaiNhanVien) {
        boolean result = false;
        MyConnection myConnection = new MyConnection();

        try {
            Connection connection = myConnection.getConnect();
            String sql = "select * from `loai_nhan_vien` where `ma_lnv`=?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1,maLoaiNhanVien);
            ResultSet rs = st.executeQuery();
            result = rs.next();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        finally {
            myConnection.closeConnection();
        }
        return result;
    }
}