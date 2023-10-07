package BUS;

import DAO.SupplierDAO;
import DTO.SupplierDTO;

import java.util.ArrayList;
import java.util.List;

public class SupplierBUS {
    private ArrayList<SupplierDTO> supplierList;
    private SupplierDAO supplierDAO;

    public SupplierBUS() {
        supplierDAO = new SupplierDAO();

    }

    public void loadData() {
        supplierList = supplierDAO.getData();
    }

    public String getNameById(String id) {
        loadData();

        for (SupplierDTO supplierDTO : supplierList) {
            if (supplierDTO.getSupplierId().equals(id)) {
                return supplierDTO.getSupplierName();
            }
        }

        return "";
    }

    public ArrayList<String> initSupplierSuggestion(int col) {
        loadData();
        ArrayList<String> list = new ArrayList<>();

        for (SupplierDTO supplierDTO : supplierList) {
            if (supplierDTO.getIsDeleted() == 0) {
                list.add(supplierDTO.getSupplierId());
            }
        }

        return list;
    }
}