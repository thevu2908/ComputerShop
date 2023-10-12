package DAO;


import DTO.BillDetailDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BillDetailDAO {

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
