package BUS;

import DAO.ProductTypeDAO;
import DTO.ProductTypeDTO;

import java.util.ArrayList;

public class ProductTypeBUS {
    private ProductTypeDAO productTypeDAO;
    private ArrayList<ProductTypeDTO> list = new ArrayList<>();

    public ProductTypeBUS() {
        productTypeDAO = new ProductTypeDAO();
        loadData();
    }

    public void loadData() {
        list = productTypeDAO.getData();
    }

    public String getNameById(String id) {
        loadData();

        for (ProductTypeDTO productTypeDTO : list) {
            if (productTypeDTO.getTypeId().equals(id)) {
                return productTypeDTO.getTypeName();
            }
        }

        return null;
    }
}