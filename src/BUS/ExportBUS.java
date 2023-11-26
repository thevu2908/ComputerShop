package BUS;

import DAO.ExportDAO;
import DTO.ExportDTO;
import utils.DateTime;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ExportBUS {
    private ArrayList<ExportDTO> exportList;
    private ExportDAO exportDAO;
    private EmployeeBUS employeeBUS;

    public ExportBUS() {
        exportDAO = new ExportDAO();
        employeeBUS = new EmployeeBUS();
    }

    public void loadData() {
        exportList = exportDAO.getData();
    }

    public boolean addExport(String exportId, String employeeId) {
        if (exportId.equals("") || employeeId.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String currentDate = DateTime.getStringCurrentDate();
        ExportDTO exportDTO = new ExportDTO(exportId, employeeId, currentDate, 0, "Chưa duyệt", 0);

        if (exportDAO.addExport(exportDTO) > 0) {
            JOptionPane.showMessageDialog(null, "Thêm phiếu xuất thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm phiếu xuất thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean confirmExport(String exportId) {
        if (getStatusById(exportId).equals("Đã duyệt")) {
            JOptionPane.showMessageDialog(null, "Phiếu xuất này đã được duyệt", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        ExportDTO exportDTO = getExportById(exportId);

        if (exportDTO != null) {
            exportDTO.setStatus("Đã duyệt");
        }

        if (exportDTO != null && exportDAO.updateExport(exportDTO) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ExportDTO getExportById(String exportId) {
        loadData();

        for (ExportDTO exportDTO : exportList) {
            if (exportDTO.getIsDeleted() == 0 && exportDTO.getExportId().equals(exportId)) {
                return exportDTO;
            }
        }

        return null;
    }

    public String getStatusById(String exportId) {
        loadData();

        for (ExportDTO exportDTO : exportList) {
            if (exportDTO.getIsDeleted() == 0 && exportDTO.getExportId().equals(exportId)) {
                return exportDTO.getStatus();
            }
        }

        return "";
    }

    public ArrayList<ExportDTO> getExportList() {
        loadData();
        return exportList;
    }

    public int setTotalQuantity(String id, int total) {
        return exportDAO.setTotalQuantity(id, total);
    }

    public String createNewId() {
        loadData();
        int id = exportList.size() + 1;
        return "PX" + String.format("%03d", id);
    }

    public void renderToTable(DefaultTableModel model, String employeeId) {
        model.setRowCount(0);
        loadData();

        for (ExportDTO exportDTO : exportList) {
            if (exportDTO.getIsDeleted() == 0 && (employeeBUS.getTypeById(employeeId).equals("LNV02")
                    || exportDTO.getEmployeeId().equals(employeeId))) {

                String status = exportDTO.getStatus().equals("Chưa duyệt")
                        ? "<html><font color='red' style='font-weight:bold;'>Chưa duyệt</font></html>"
                        : "<html><font color='green' style='font-weight: bold;'>Đã duyệt</font></html>";

                model.addRow(new Object[]{
                        exportDTO.getExportId(),
                        exportDTO.getEmployeeId(),
                        DateTime.formatDate(exportDTO.getExportDate()),
                        exportDTO.getTotalQuantity(),
                        status
                });
            }
        }

        model.fireTableDataChanged();
    }
}