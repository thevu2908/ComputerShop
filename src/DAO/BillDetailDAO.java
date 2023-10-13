package DAO;

import DTO.BillDetailDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class BillDetailDAO {
    public ArrayList<BillDetailDTO> getData() {
        ArrayList<BillDetailDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `chi_tiet_hoa_don`");

            while (rs.next()) {
                String billId = rs.getString("ma_hd");
                String productId = rs.getString("ma_sp");
                int quantity = rs.getInt("so_luong");

                list.add(new BillDetailDTO(billId, productId, quantity));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addBillDetail(BillDetailDTO billDetailDTO) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "insert into `chi_tiet_hoa_don` values (?, ?, ?)";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, billDetailDTO.getBillId());
            ptmt.setString(2, billDetailDTO.getProductId());
            ptmt.setInt(3, billDetailDTO.getQuantity());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}