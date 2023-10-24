package DAO;

import DTO.BillDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class BillDAO {
    public ArrayList<BillDTO> getData() {
        ArrayList<BillDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `hoa_don`");

            while (rs.next()) {
                String billId = rs.getString("ma_hd");
                String customerId = rs.getString("ma_kh");
                String employeeId = rs.getString("ma_nv");
                String date = rs.getString("ngay_lap");
                int total = rs.getInt("tong_tien");
                int discount = rs.getInt("giam_gia");
                int isDeleted = rs.getInt("trang_thai");

                list.add(new BillDTO(billId, customerId, employeeId, date, total, discount, isDeleted));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addBill(BillDTO billDTO) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "insert into `hoa_don` values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, billDTO.getBillId());
            ptmt.setString(2, billDTO.getCustomerId());
            ptmt.setString(3, billDTO.getEmployeeId());
            ptmt.setString(4, billDTO.getBillDate());
            ptmt.setInt(5, billDTO.getTotal());
            ptmt.setInt(6, billDTO.getDiscount());
            ptmt.setInt(7, billDTO.getIsDeleted());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}