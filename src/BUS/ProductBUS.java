package BUS;

import DAO.ProductDAO;
import DTO.ProductDTO;

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

    public String getNameById(String id) {
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getProductId().equals(id)) {
                return productDTO.getProductName();
            }
        }

        return null;
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
        loadProductData();
        model.setRowCount(0);

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
        loadProductData();
        model.setRowCount(0);

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
        loadStorageProductData();
        model.setRowCount(0);

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