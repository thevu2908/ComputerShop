package DAO;

import DTO.ProductDTO;
import connection.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductDAO {
    public ArrayList<ProductDTO> getData() {
        ArrayList<ProductDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `san_pham`");

            while (rs.next()) {
                String id = rs.getString("ma_sp");
                String type = rs.getString("ma_hsp");
                String name = rs.getString("ten_sp");
                int price = rs.getInt("gia");
                String cpu = rs.getString("cpu");
                String ram = rs.getString("ram");
                String oCung = rs.getString("o_cung");
                String screen = rs.getString("man_hinh");
                String screenCard = rs.getString("card_man_hinh");
                int quantity = rs.getInt("so_luong");
                int isDeleted = rs.getInt("trang_thai");

                list.add(new ProductDTO(id, type, name, price, cpu, ram, oCung, screen, screenCard, quantity, isDeleted));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<ProductDTO> getStorageData() {
        ArrayList<ProductDTO> list = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnect();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from `san_pham_kho`");

            while (rs.next()) {
                String id = rs.getString("ma_sp");
                int quantity = rs.getInt("so_luong");

                list.add(new ProductDTO(id, quantity));
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int addProduct(ProductDTO product) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "insert into `san_pham` values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1, product.getProductId());
            ptmt.setString(2, product.getProductType());
            ptmt.setString(3, product.getProductName());
            ptmt.setInt(4, product.getProductPrice());
            ptmt.setString(5, product.getProductCPU());
            ptmt.setString(6, product.getProductRAM());
            ptmt.setString(7, product.getProductDisk());
            ptmt.setString(8, product.getProductScreen());
            ptmt.setString(9, product.getProductScreenCard());
            ptmt.setInt(10, product.getProductQuantity());
            ptmt.setInt(11, product.getIsDeleted());

            return ptmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateProduct(ProductDTO product){
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `san_pham` set `ma_hsp` = ?, `ten_sp` = ?,`gia` = ?, `cpu` = ?, `ram` = ?, `o_cung` = ?, `man_hinh` = ?, `card_man_hinh` = ? where `ma_sp` = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);
            ptmt.setString(1, product.getProductType());
            ptmt.setString(2, product.getProductName());
            ptmt.setInt(3, product.getProductPrice());
            ptmt.setString(4, product.getProductCPU());
            ptmt.setString(5, product.getProductRAM());
            ptmt.setString(6, product.getProductDisk());
            ptmt.setString(7, product.getProductScreen());
            ptmt.setString(8, product.getProductScreenCard());
            ptmt.setString(9, product.getProductId());

            return ptmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteProduct(String productID) {
        try {
            Connection connection = MyConnection.getConnect();
            String query = "update `san_pham` set `trang_thai` = 1 where `ma_sp` = ?";
            PreparedStatement ptmt = connection.prepareStatement(query);

            ptmt.setString(1,productID);

            return ptmt.executeUpdate();
        }
        catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }
}