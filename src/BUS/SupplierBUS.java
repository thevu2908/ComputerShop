package BUS;

import DAO.SupplierDAO;
import DTO.SupplierDTO;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class SupplierBUS {
    private ArrayList<SupplierDTO> supplierList;
    private SupplierDAO supplierDAO;

    public SupplierBUS() {
        supplierDAO = new SupplierDAO();
    }

    public void loadData() {
        supplierList = supplierDAO.getData();
    }

    public boolean addSupplier(String Id, String Name, String Address, String Phone) {
        if (Id.equals("") || Name.equals("") || Address.equals("") || Phone.equals("")){
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedId(Id)) {
            JOptionPane.showMessageDialog(null, "Mã nhà cung cấp đã tồn tại!","Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidPhone(Phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ (10 chữ số và bắt đầu bằng số 0)", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedPhone(Phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại này đã thuộc nhà cung cấp khác", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        SupplierDTO Supplier = new SupplierDTO(Id ,Name, Address, Phone, 0);
        if (supplierDAO.addSupplier(Supplier) > 0) {
            JOptionPane.showMessageDialog(null, "Thêm nhà cung cấp thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm nhà cung cấp thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteSupplier(String id) {
        int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn xoá nhà cung cấp " + id + " không ?", "Câu hỏi",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (supplierDAO.deleteSupplier(id) > 0) {
                JOptionPane.showMessageDialog(null, "Xoá nhà cung cấp thành công");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Xoá nhà cung cấp thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return false;
    }

    public boolean updateSupplier(String Id,String Name, String Address, String Phone) {
        if (Id.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhà cung cấp muốn sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (Name.equals("") ||Address.equals("") || Phone.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidPhone(Phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ (10 chữ số và bắt đầu bằng số 0)", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!checkExistedId(Id)) {
            JOptionPane.showMessageDialog(null, "Không tồn tại mã nhà cung cấp này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Phone.equals(getSupplierById(Id).getSupplierPhone()) && checkExistedPhone(Phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại này đã thuộc nhà cung cấp khác", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        SupplierDTO supplier = new SupplierDTO(Id, Name, Address, Phone, 0);

        if (supplierDAO.updateSupplier(supplier) > 0) {
            JOptionPane.showMessageDialog(null, "Sửa thông tin nhà cung cáp thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Sửa thông tin nhà cung cấp thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public String createNewSupplierID() {
        loadData();
        int id = supplierList.size() + 1;
        return "NCC" + String.format("%02d", id);
    }

    public void exportExcel(File path) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet("Nhà cung cấp");
        sheet.trackAllColumnsForAutoSizing();

        int rowIndex = 0;

        writeExcelTitle(sheet, rowIndex);

        loadData();
        for (SupplierDTO supplierDTO : supplierList) {
            if (supplierDTO.getIsDeleted() == 0) {
                rowIndex++;
                SXSSFRow row = sheet.createRow(rowIndex);
                writeExcelData(supplierDTO, row);
            }
        }

        autoResizeColumn(sheet, 11);

        if(writeExcel(workbook, path)) {
            JOptionPane.showMessageDialog(null, "Xuất danh sách nhà cung cấp thành file excel thành công");
        } else {
            JOptionPane.showMessageDialog(null, "Xuất danh sách nhà cung cấp thành file excel thất bại", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean writeExcel(SXSSFWorkbook workbook, File path) {
        try {
            String fileName = path.getName();
            if (!fileName.endsWith(".xlsx")) {
                path = new File(path.getParentFile(), fileName + ".xlsx");
            }

            FileOutputStream fos = new FileOutputStream(path.toString());
            workbook.write(fos);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void writeExcelData(SupplierDTO supplierDTO, SXSSFRow row) {
        SXSSFCell cell = row.createCell(0);
        cell.setCellValue(supplierDTO.getSupplierId());

        cell = row.createCell(1);
        cell.setCellValue(supplierDTO.getSupplierName());

        cell = row.createCell(2);
        cell.setCellValue(supplierDTO.getSupplierPhone());

        cell = row.createCell(3);
        cell.setCellValue(supplierDTO.getSupplierAddress());
    }

    public void writeExcelTitle(SXSSFSheet sheet, int rowIndex) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);

        SXSSFRow row = sheet.createRow(rowIndex);

        SXSSFCell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Mã nhà cung cấp");

        cell = row.createCell(1);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tên nhà cung cấp");

        cell = row.createCell(2);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Số điện thoại");

        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Địa chỉ");
    }

    public void autoResizeColumn(SXSSFSheet sheet, int columns) {
        for (int i = 0; i < columns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public String getNameById(String id) {
        loadData();

        for (SupplierDTO supplierDTO : supplierList) {
            if (supplierDTO.getIsDeleted() == 0 && supplierDTO.getSupplierId().equals(id)) {
                return supplierDTO.getSupplierName();
            }
        }

        return "";
    }

    public SupplierDTO getSupplierById(String id) {
        loadData();

        for (SupplierDTO supplierDTO : supplierList) {
            if (supplierDTO.getIsDeleted() == 0 && supplierDTO.getSupplierId().equals(id)) {
                return supplierDTO;
            }
        }

        return null;
    }

    public boolean checkExistedPhone(String phone) {
        loadData();

        for (SupplierDTO supplierDTO : supplierList) {
            if (supplierDTO.getIsDeleted() == 0 && supplierDTO.getSupplierPhone().equals(phone)) {
                return true;
            }
        }

        return false;
    }

    public boolean checkExistedId(String id) {
        loadData();

        for (SupplierDTO supplierDTO : supplierList) {
            if (supplierDTO.getSupplierId().equals(id)) {
                return true;
            }
        }

        return false;
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

    public void renderToSupplierTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadData();

        for (SupplierDTO supplierDTO : supplierList) {
            if (supplierDTO.getIsDeleted() == 0) {
                model.addRow(new Object[]{
                        supplierDTO.getSupplierId(),
                        supplierDTO.getSupplierName(),
                        supplierDTO.getSupplierPhone(),
                        supplierDTO.getSupplierAddress()
                });
            }
        }

        model.fireTableDataChanged();
    }
}