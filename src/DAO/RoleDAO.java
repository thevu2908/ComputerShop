package DAO;

import DTO.RoleDTO;
import connection.MyConnection;

import java.sql.*;
import java.util.ArrayList;

public class RoleDAO {
    public ArrayList<RoleDTO> getData() {
        ArrayList<RoleDTO> roleList = new ArrayList<RoleDTO>();
        MyConnection myConnection = new MyConnection();
        try {
            Connection connection = myConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `loai_nhan_vien`");

            while (rs.next()) {
                RoleDTO role = new RoleDTO();
                role.setMaLoaiNhanVien(rs.getString("ma_lnv"));
                role.setTenLoaiNhanvien(rs.getString("ten_lnv"));
                role.setTrangThai(rs.getInt("trang_thai"));
                roleList.add(role);
            }
            myConnection.closeConnection();
            return roleList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public RoleDTO getById(String maLoaiNhanVien) {
        RoleDTO role = new RoleDTO();
        MyConnection myConnection = new MyConnection();

        try {
            Connection connection = myConnection.getConnect();
            String query = "select * from `loai_nhan_vien` where `ma_lnv` = ?";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1,maLoaiNhanVien);
            ResultSet rs = st.executeQuery();
            if (rs.next()){
                role.setMaLoaiNhanVien(rs.getString("ma_lnv"));
                role.setTenLoaiNhanvien(rs.getString("ten_lnv"));
                role.setTrangThai(rs.getInt("trang_thai"));
            }
            else return null;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            myConnection.closeConnection();
        }
        return role;
    }

    public boolean add(RoleDTO roleDTO) {
        boolean result = false;
        MyConnection myConnection = new MyConnection();

        try {
            Connection connection = myConnection.getConnect();
            String sql = "insert into `loai_nhan_vien` value (?,?,?)";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1,roleDTO.getMaLoaiNhanVien());
            st.setString(2,roleDTO.getTenLoaiNhanvien());
            st.setInt(3,roleDTO.getTrangThai());

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

    public boolean update(RoleDTO roleDTO) {
        boolean result = false;
        MyConnection myConnection = new MyConnection();

        try {
            Connection connection = myConnection.getConnect();
            String sql = "update `loai_nhan_vien` set `ma_lnv` = ?, `ten_lnv` = ?,`trang_thai` = ? where `ma_lnv` = ?";
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1,roleDTO.getTenLoaiNhanvien());
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

    public boolean delete(String maLoaiNhanVien) {
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
