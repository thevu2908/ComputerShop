package BUS;

import DAO.ImportDAO;
import DTO.ImportDTO;
import utils.DateTime;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ImportBUS {
    private ArrayList<ImportDTO> importList;
    private ImportDAO importDAO;
    private EmployeeBUS employeeBUS;
    private SupplierBUS supplierBUS;

    public ImportBUS() {
        importDAO = new ImportDAO();
        employeeBUS = new EmployeeBUS();
        supplierBUS = new SupplierBUS();
    }

    public void loadData() {
        importList = importDAO.getData();
    }

    public boolean addImport(String importId, String employeeId, String supplierId) {
        if (importId.equals("") || employeeId.equals("") || supplierId.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!supplierBUS.checkExistedId(supplierId)) {
            JOptionPane.showMessageDialog(null, "Nhà cung cấp không tồn tại \nVui lòng nhập lại mã nhà cung cấp", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String currentDate = DateTime.getStringCurrentDate();
        ImportDTO importDTO = new ImportDTO(importId, employeeId, supplierId, currentDate, 0, "Chưa duyệt", 0);

        if (importDAO.addImport(importDTO) > 0) {
            JOptionPane.showMessageDialog(null, "Thêm phiếu nhập thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm phiếu nhập thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateImportSupplier(String importId, String supplierId) {
        if (getStatusById(importId).equals("Đã duyệt")) {
            JOptionPane.showMessageDialog(null, "Không thể sửa phiếu nhập đã được duyệt", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (supplierId.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập mã nhà cung cấp", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!supplierBUS.checkExistedId(supplierId)) {
            JOptionPane.showMessageDialog(null, "Nhà cung cấp không tồn tại \nVui lòng nhập lại mã nhà cung cấp", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        ImportDTO importDTO = getImportById(importId);
        if (importDTO != null) {
            importDTO.setSupplierId(supplierId);

            if (importDAO.updateImport(importDTO) > 0) {
                JOptionPane.showMessageDialog(null, "Sửa thông tin phiếu nhập thành công");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Sửa thông tin phiếu nhập thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return false;
    }

    public boolean confirmImport(String importId) {
        if (getStatusById(importId).equals("Đã duyệt")) {
            JOptionPane.showMessageDialog(null, "Phiếu nhập này đã được duyệt", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        ImportDTO importDTO = getImportById(importId);

        if (importDTO != null) {
            importDTO.setImportStatus("Đã duyệt");

            if (importDAO.updateImport(importDTO) > 0) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public ImportDTO getImportById(String id) {
        loadData();

        for (ImportDTO importDTO : importList) {
            if (importDTO.getIsDeleted() == 0 && importDTO.getImportId().equals(id)) {
                return importDTO;
            }
        }

        return null;
    }

    public String getStatusById(String importId) {
        loadData();

        for (ImportDTO importDTO : importList) {
            if (importDTO.getIsDeleted() == 0 && importDTO.getImportId().equals(importId)) {
                return importDTO.getImportStatus();
            }
        }

        return "";
    }

    public String getSupplierById(String importId) {
        loadData();

        for (ImportDTO importDTO : importList) {
            if (importDTO.getIsDeleted() == 0 && importDTO.getImportId().equals(importId)) {
                return importDTO.getSupplierId();
            }
        }

        return "";
    }

    public int setTotalPrice(String id, int total) {
        return importDAO.setTotalPrice(id, total);
    }

    public String createNewId() {
        loadData();
        int id = importList.size() + 1;
        return "PN" + String.format("%03d", id);
    }

    public void renderToTable(DefaultTableModel model, String employeeId) {
        model.setRowCount(0);
        loadData();

        for (ImportDTO importDTO : importList) {
            if (importDTO.getIsDeleted() == 0 && (employeeBUS.getTypeById(employeeId).equals("LNV02")
                    || importDTO.getEmployeeId().equals(employeeId))) {

                String status = importDTO.getImportStatus().equals("Chưa duyệt")
                        ? "<html><font color='red' style='font-weight:bold;'>Chưa duyệt</font></html>"
                        : "<html><font color='green' style='font-weight: bold;'>Đã duyệt</font></html>";

                model.addRow(new Object[]{
                        importDTO.getImportId(),
                        importDTO.getEmployeeId(),
                        importDTO.getSupplierId(),
                        DateTime.formatDate(importDTO.getImportDate()),
                        importDTO.getImportTotalPrice(),
                        status
                });
            }
        }

        model.fireTableDataChanged();
    }
}