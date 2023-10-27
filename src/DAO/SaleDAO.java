package DAO;

import DTO.SaleDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
                String startDate = rs.getString("ngay_bat_dau");
                String endDate = rs.getString("ngay_ket_thuc");
                String status = rs.getString("tinh_trang");
                int isDeleted = rs.getInt("trang_thai");

                list.add(new SaleDTO(id, info, startDate, endDate, status, isDeleted));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addSale(SaleDTO saleDTO) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "insert into `chuong_trinh_khuyen_mai` values (?, ?, ?, ?, ?, ?)";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, saleDTO.getSaleId());
            ptmt.setString(2, saleDTO.getSaleInfo());
            ptmt.setString(3, saleDTO.getStartDate());
            ptmt.setString(4, saleDTO.getEndDate());
            ptmt.setString(5, saleDTO.getSaleStatus());
            ptmt.setInt(6, saleDTO.getIsDeleted());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteSale(String saleId) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `chuong_trinh_khuyen_mai` set trang_thai = 1 where ma_ctkm = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, saleId);

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int applySale(String saleId) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `chuong_trinh_khuyen_mai` set tinh_trang = ? where ma_ctkm = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, "Đang áp dụng");
            ptmt.setString(2, saleId);

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int stopApplySale(String saleId) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `chuong_trinh_khuyen_mai` set tinh_trang = ? where ma_ctkm = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, "Ngưng áp dụng");
            ptmt.setString(2, saleId);

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}