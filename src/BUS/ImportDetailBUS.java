package BUS;

import DAO.ImportDetailDAO;
import DTO.ImportDetailDTO;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ImportDetailBUS {
    private ArrayList<ImportDetailDTO> importDetailList;
    private ImportDetailDAO importDetailDAO;
    private ProductBUS productBUS;
    private ImportBUS importBUS;

    public ImportDetailBUS() {
        importDetailDAO = new ImportDetailDAO();
        productBUS = new ProductBUS();
        importBUS = new ImportBUS();
    }

    public void loadData() {
        importDetailList = importDetailDAO.getData();
    }

    public boolean addImportDetail(String importId, String productId, String quantity) {
        if (importId.equals("") || productId.equals("") || quantity.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidNumber(quantity, "Số lượng")) {
            return false;
        }

        if (!productBUS.checkExistedProductId(productId)) {
            JOptionPane.showMessageDialog(null, "Sản phẩm không tồn tại \nVui lòng nhập lại mã sản phẩm", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        boolean flag = false;
        int numQuantity = Integer.parseInt(quantity);
        ImportDetailDTO importDetail = getImportDetailById(importId, productId);

        if (importDetail != null) { // case importDetail have already existed in list
            importDetail.setQuantity(importDetail.getQuantity() + numQuantity);
            if (updateImportDetailQuantity(importDetail)) {
                flag = true;
            }
        } else { // case importDetail haven't existed in list
            importDetail = new ImportDetailDTO(importId, productId, numQuantity);
            if (importDetailDAO.addImportDetail(importDetail) > 0) {
                flag = true;
            }
        }

        if (flag) {
            JOptionPane.showMessageDialog(null, "Thêm chi tiết phiếu nhập thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm chi tiết phiếu nhập thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateImportDetailQuantity(ImportDetailDTO importDetail) {
        return importDetailDAO.updateImportDetailQuantity(importDetail) > 0;
    }

    public ImportDetailDTO getImportDetailById(String importId, String productId) {
        loadData();

        for (ImportDetailDTO importDetailDTO : importDetailList) {
            if (importDetailDTO.getImportId().equals(importId) && importDetailDTO.getProductId().equals(productId)) {
                return importDetailDTO;
            }
        }

        return null;
    }

    public boolean checkExistedImportDetail(String importId, String productId) {
        loadData();

        for (ImportDetailDTO importDetailDTO : importDetailList) {
            if (importDetailDTO.getImportId().equals(importId) && importDetailDTO.getProductId().equals(productId)) {
                return true;
            }
        }

        return false;
    }

    public int calculateTotalPrice(String importId) {
        int total = 0;
        for (ImportDetailDTO importDetailDTO : importDetailList) {
            if (importDetailDTO.getImportId().equals(importId)) {
                int price = productBUS.getPriceById(importDetailDTO.getProductId());
                total += price * importDetailDTO.getQuantity();
            }
        }
        importBUS.setTotalPrice(importId, total);
        return total;
    }

    public void renderToTable(DefaultTableModel model, String importId) {
        model.setRowCount(0);
        loadData();

        for (ImportDetailDTO importDetailDTO : importDetailList) {
            if (importDetailDTO.getImportId().equals(importId)) {
                int price = productBUS.getPriceById(importDetailDTO.getProductId());
                model.addRow(new Object[]{
                        importDetailDTO.getProductId(),
                        productBUS.getNameById(importDetailDTO.getProductId()),
                        price,
                        importDetailDTO.getQuantity(),
                        price * importDetailDTO.getQuantity()
                });
            }
        }

        model.fireTableDataChanged();
    }
}