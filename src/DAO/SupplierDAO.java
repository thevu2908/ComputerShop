package DAO;

import DTO.SupplierDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public int addSupplier(SupplierDTO Supplier) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "insert into `nha_cung_cap` values (?, ?, ?, ?, ?)";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, Supplier.getSupplierId());
            ptmt.setString(2, Supplier.getSupplierName());
            ptmt.setString(3, Supplier.getSupplierAddress());
            ptmt.setString(4, Supplier.getSupplierPhone());
            ptmt.setInt(5, Supplier.getIsDeleted());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateSupplier(SupplierDTO Supplier) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `nha_cung_cap` set `ho_ten` = ?, `dia_chi` = ?, `so_dien_thoai` = ? where `ma_ncc` = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, Supplier.getSupplierName());
            ptmt.setString(2, Supplier.getSupplierAddress());
            ptmt.setString(3, Supplier.getSupplierPhone());
            ptmt.setString(4, Supplier.getSupplierId());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteSupplier(String SupplierID) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `nha_cung_cap` set `trang_thai` = 1 where `ma_ncc` = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1,SupplierID);

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}