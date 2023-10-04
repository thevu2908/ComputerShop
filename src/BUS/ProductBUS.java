package BUS;

import DAO.ProductDAO;
import DTO.ProductDTO;
import Validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ProductBUS {
    private ProductDAO productDAO;
    private ProductTypeBUS productTypeBUS;
    private ArrayList<ProductDTO> productList = new ArrayList<>();
    private ArrayList<ProductDTO> storageProductList = new ArrayList<>();

    public ProductBUS() {
        productDAO = new ProductDAO();
        productTypeBUS = new ProductTypeBUS();
    }

    public void loadProductData() {
        productList = productDAO.getData();
    }

    public void loadStorageProductData() {
        storageProductList = productDAO.getStorageData();
    }

    public void addProduct(String id, String name, String type, String price, String cpu, String ram, String oCung,
                          String screen, String screenCard) {
        if (id.equals("") || name.equals("") ||type.equals("") || price.equals("") || cpu.equals("") || ram.equals("")
                || oCung.equals("") || screen.equals("") || screenCard.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (checkExistedProductId(id)){
            JOptionPane.showMessageDialog(null, "Mã sản phẩm đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Validate.isValidNumber(price, "Giá")) {
            return;
        }

        int intPrice = Integer.parseInt(price);
        ProductDTO product = new ProductDTO(id, name, type, intPrice, cpu, ram, oCung, screen, screenCard, 0, 1);

        if (productDAO.addProduct(product) == 1) {
            JOptionPane.showMessageDialog(null, "Thêm sản phẩm thành công");
        } else {
            JOptionPane.showMessageDialog(null, "Thêm sản phẩm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean checkExistedProductId(String productID) {
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getProductId().equals(productID)) {
                return true;
            }
        }
        return false;
    }

    public ProductDTO getProductById(String productID) {
        loadProductData();

        for (ProductDTO productDTO : productList){
            if (productDTO.getProductId().equals(productID)){
                return productDTO;
            }
        }
        return null;
    }

    public String getNameById(String id) {
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getProductId().equals(id)) {
                return productDTO.getProductName();
            }
        }

        return null;
    }

    public int getPriceById(String id) {
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getProductId().equals(id)) {
                return productDTO.getProductPrice();
            }
        }

        return 0;
    }

    public int getIsDeletedById(String id) {
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getProductId().equals(id)) {
                return productDTO.getIsDeleted();
            }
        }

        return 0;
    }

    public String getTypeNameById(String id) {
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getProductId().equals(id)) {
                return productDTO.getProductType();
            }
        }

        return null;
    }

    public void renderToSellTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getIsDeleted() == 1) {
                model.addRow(new Object[]{
                        productDTO.getProductId(),
                        productDTO.getProductName(),
                        productTypeBUS.getNameById(productDTO.getProductType()),
                        productDTO.getProductQuantity()
                });
            }
        }

        model.fireTableDataChanged();
    }

    public void renderToProductTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getIsDeleted() == 1) {
                model.addRow(new Object[]{
                        productDTO.getProductId(),
                        productDTO.getProductName(),
                        productTypeBUS.getNameById(productDTO.getProductType()),
                        productDTO.getProductPrice()
                });
            }
        }

        model.fireTableDataChanged();
    }

    public void renderToStorageProductTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadStorageProductData();

        for (ProductDTO productDTO : storageProductList) {
            if (getIsDeletedById(productDTO.getProductId()) == 1) {
                model.addRow(new Object[]{
                        productDTO.getProductId(),
                        getNameById(productDTO.getProductId()),
                        productTypeBUS.getNameById(getTypeNameById(productDTO.getProductId())),
                        productDTO.getProductQuantity()
                });
            }
        }

        model.fireTableDataChanged();
    }
}