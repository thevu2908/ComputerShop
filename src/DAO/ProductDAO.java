package DAO;

import DTO.ProductDTO;
import connection.MyConnection;

import java.sql.Connection;
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
}