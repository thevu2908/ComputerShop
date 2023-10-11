package BUS;

import DAO.ProductDAO;
import DTO.ProductDTO;
import validation.Validate;

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

    public boolean addProduct(String id, String name, String type, String price, String cpu, String ram, String oCung,
                          String screen, String screenCard) {
        if (id.equals("") || name.equals("") ||type.equals("") || price.equals("") || cpu.equals("") || ram.equals("")
                || oCung.equals("") || screen.equals("") || screenCard.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedProductId(id)){
            JOptionPane.showMessageDialog(null, "Mã sản phẩm đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidNumber(price, "Giá")) {
            return false;
        }

        int intPrice = Integer.parseInt(price);
        String typeId = productTypeBUS.getIdByName(type);
        ProductDTO product = new ProductDTO(id, typeId, name, intPrice, cpu, ram, oCung, screen, screenCard, 0, 0);

        if (productDAO.addProduct(product) > 0) {
            JOptionPane.showMessageDialog(null, "Thêm sản phẩm thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm sản phẩm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteProduct(String id) {
        if (id.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm muốn xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn xoá sản phẩm " + id + " không ?", "Câu hỏi",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (productDAO.deleteProduct(id) > 0) {
                JOptionPane.showMessageDialog(null, "Xoá sản phẩm thành công");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Xoá sản phẩm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return false;
    }

    public boolean updateProduct(String id, String name, String type, String price, String cpu, String ram, String oCung,
                              String screen, String screenCard) {
        if (id.equals("")){
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm muốn sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (name.equals("") ||type.equals("") || price.equals("") || cpu.equals("") || ram.equals("")
                || oCung.equals("") || screen.equals("") || screenCard.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidNumber(price, "Giá")) {
            return false;
        }

        if (!checkExistedProductId(id)) {
            JOptionPane.showMessageDialog(null, "Không tồn tại mã sản phẩm này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int intPrice = Integer.parseInt(price);
        String typeId = productTypeBUS.getIdByName(type);
        ProductDTO product = new ProductDTO(id, typeId, name, intPrice, cpu, ram, oCung, screen, screenCard);

        if (productDAO.updateProduct(product) > 0) {
            JOptionPane.showMessageDialog(null, "Sửa thông tin sản phẩm thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Sửa thông tin sản phẩm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
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

    public String createNewProductID() {
        loadProductData();
        int id = productList.size() + 1;
        return "SP" + String.format("%03d", id);
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

        return "";
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

        return "";
    }

    public int getStorageProductQuantityById(String productId) {
        loadStorageProductData();

        for (ProductDTO productDTO : storageProductList) {
            if (productDTO.getProductId().equals(productId)) {
                return productDTO.getProductQuantity();
            }
        }

        return 0;
    }

    public ArrayList<String> initAvailableProductIdSuggestion(int col) {
        loadProductData();;
        ArrayList<String> list = new ArrayList<>();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getProductQuantity() > 0) {
                list.add(productDTO.getProductId());
            }
        }

        return list;
    }

    public ArrayList<String> initProductIdSuggestion(int col) {
        loadProductData();;
        ArrayList<String> list = new ArrayList<>();

        for (ProductDTO productDTO : productList) {
            list.add(productDTO.getProductId());
        }

        return list;
    }

    public void renderToSellTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getIsDeleted() == 0) {
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
            if (productDTO.getIsDeleted() == 0) {
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
            if (getIsDeletedById(productDTO.getProductId()) == 0) {
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

    public ArrayList<ProductDTO> getProductList() {
        loadProductData();
        return productList;
    }
}