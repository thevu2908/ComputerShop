package DAO;

import DTO.BillDTO;

import DTO.CustomerDTO;
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
                int isDeleted = rs.getInt("trang_thai");

                list.add(new BillDTO(billId,customerId,employeeId,date,total,isDeleted));
            }
            connection.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<BillDTO> getBillListOfEmployee(String id) {
        ArrayList<BillDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            String query = String.format("select * from `hoa_don` where `ma_nv` = '%s' ", id);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String billId = rs.getString("ma_hd");
                String customerId = rs.getString("ma_kh");
                String employeeId = rs.getString("ma_nv");
                String date = rs.getString("ngay_lap");
                int total = rs.getInt("tong_tien");
                int isDeleted = rs.getInt("trang_thai");

                list.add(new BillDTO(billId,customerId,employeeId,date,total,isDeleted));
            }
            connection.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getNewBillId() {

        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `hoa_don`");
            int tong=0;

            while (rs.next()) {
              tong+=1;
            }
            connection.close();
            return "HD" + tong+1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addBill(BillDTO billDTO) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "insert into `hoa_don` values (?, ?, ?, ?, ?, ?)";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, billDTO.getBillId());
            ptmt.setString(2, billDTO.getCustomerId());
            ptmt.setString(3, billDTO.getEmployeeId());
            ptmt.setString(4, billDTO.getBillDate());
            ptmt.setInt(5, billDTO.getTotal());
            ptmt.setInt(6, billDTO.getIsDeleted());
            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}
