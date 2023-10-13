package BUS;

import DAO.ExportDetailDAO;
import DTO.ExportDetailDTO;
import DTO.ProductDTO;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ExportDetailBUS {
    private ArrayList<ExportDetailDTO> exportDetailList;
    private ExportDetailDAO exportDetailDAO;
    private ProductBUS productBUS;
    private ExportBUS exportBUS;

    public ExportDetailBUS() {
        exportDetailDAO = new ExportDetailDAO();
        productBUS = new ProductBUS();
        exportBUS = new ExportBUS();
    }

    public void loadData() {
        exportDetailList = exportDetailDAO.getData();
    }

    public boolean addExportDetail(String exportId, String productId, String quantity) {
        if (exportBUS.getStatusById(exportId).equals("Đã duyệt")) {
            JOptionPane.showMessageDialog(null, "Không thể thêm chi tiết phiếu xuất đã được duyệt", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (exportId.equals("") || productId.equals("") || quantity.equals("")) {
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

        int numQuantity = Integer.parseInt(quantity);
        if (productBUS.getStorageProductQuantityById(productId) < numQuantity) {
            JOptionPane.showMessageDialog(null, "Số lượng còn lại của sản phẩm trong kho không đủ", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        boolean flag = false;
        ExportDetailDTO exportDetail = getExportDetailById(exportId, productId);

        if (exportDetail != null) { // case importDetail have already existed in list
            exportDetail.setQuantity(exportDetail.getQuantity() + numQuantity);
            if (updateExportDetailQuantity(exportDetail)) {
                flag = true;
            }
        } else { // case importDetail haven't existed in list
            exportDetail = new ExportDetailDTO(exportId, productId, numQuantity);
            if (exportDetailDAO.addExportDetail(exportDetail) > 0) {
                flag = true;
            }
        }

        if (flag) {
            JOptionPane.showMessageDialog(null, "Thêm chi tiết phiếu xuất thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm chi tiết phiếu xuất thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateExportDetailQuantity(ExportDetailDTO exportDetail) {
        return exportDetailDAO.updateExportDetailQuantity(exportDetail) > 0;
    }

    public boolean confirmExport(String exportId) {
        if (exportBUS.confirmExport(exportId) && changeProductQuantity(exportId)) {
            JOptionPane.showMessageDialog(null, "Duyệt phiếu xuất thành công");
            return true;
        } else {
            return false;
        }
    }

    public boolean changeProductQuantity(String exportId) {
        loadData();
        boolean flag = false;

        for (ExportDetailDTO exportDetailDTO : exportDetailList) {
            if (exportDetailDTO.getExportId().equals(exportId)) {
                String productId = exportDetailDTO.getProductId();
                int quantity = exportDetailDTO.getQuantity();

                ProductDTO product = productBUS.getProductById(productId);
                ProductDTO storageProduct = productBUS.getProductStorageById(productId);

                if (
                        productBUS.updateProductQuantity(productId, product.getProductQuantity() + quantity)
                        &&
                        productBUS.updateProductStorageQuantity(productId, storageProduct.getProductQuantity() - quantity)
                ) {
                    flag = true;
                } else {
                    flag = false;
                }
            }
        }

        return flag;
    }

    public boolean deleteExportDetail(String exportId, String productId) {
        if (exportBUS.getStatusById(exportId).equals("Đã duyệt")) {
            JOptionPane.showMessageDialog(null, "Không thể xóa chi tiết phiếu xuất đã được duyệt", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (exportDetailDAO.deleteExportDetail(exportId, productId) > 0) {
            JOptionPane.showMessageDialog(null, "Xóa chi tiết phiếu xuất thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Xóa chi tiết phiếu xuất thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public ExportDetailDTO getExportDetailById(String exportId, String productId) {
        loadData();

        for (ExportDetailDTO exportDetailDTO : exportDetailList) {
            if (exportDetailDTO.getExportId().equals(exportId) && exportDetailDTO.getProductId().equals(productId)) {
                return exportDetailDTO;
            }
        }

        return null;
    }

    public int calculateTotalQuantity(String exportId) {
        int total = 0;
        for (ExportDetailDTO exportDetailDTO : exportDetailList) {
            if (exportDetailDTO.getExportId().equals(exportId)) {
               total += exportDetailDTO.getQuantity();
            }
        }
        exportBUS.setTotalQuantity(exportId, total);
        return total;
    }

    public void renderToTable(DefaultTableModel model, String exportId) {
        model.setRowCount(0);
        loadData();

        for (ExportDetailDTO exportDetailDTO : exportDetailList) {
            if (exportDetailDTO.getExportId().equals(exportId)) {
                model.addRow(new Object[]{
                        exportDetailDTO.getProductId(),
                        productBUS.getNameById(exportDetailDTO.getProductId()),
                        exportDetailDTO.getQuantity()
                });
            }
        }

        model.fireTableDataChanged();
    }
}