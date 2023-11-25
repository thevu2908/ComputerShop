package BUS;

import DAO.CustomerDAO;
import DTO.CustomerDTO;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import utils.DateTime;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class CustomerBUS {
    private ArrayList<CustomerDTO> customerList;
    private CustomerDAO customerDAO;

    public CustomerBUS() {
        customerDAO = new CustomerDAO();
    }

    public void loadData() {
        customerList = customerDAO.getData();
    }

    public boolean addCustomer(String id, String name, String address, String phone, String dob, String gender) {
        if (id.equals("") || name.equals("") || address.equals("") || phone.equals("") || dob.equals("")
                || gender.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedId(id)) {
            JOptionPane.showMessageDialog(null, "Mã khách hàng đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ (10 chữ số và bắt đầu bằng số 0)", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedPhone(phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại này đã thuộc khách hàng khác", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidDate(dob)) {
            JOptionPane.showMessageDialog(null, "Ngày sinh không hợp lệ (dd-MM-yyyy)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidDOB(dob)) {
            JOptionPane.showMessageDialog(null, "Khách hàng chưa đủ 18 tuổi", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        CustomerDTO customer = new CustomerDTO(id, name, address, phone, DateTime.formatDate(dob), gender, 0, 0);

        if (customerDAO.addCustomer(customer) > 0) {
            JOptionPane.showMessageDialog(null, "Thêm khách hàng thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm khách hàng thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateCustomer(String id, String name, String address, String phone, String dob, String gender) {
        if (id.equals("") || name.equals("") || address.equals("") || phone.equals("") || dob.equals("")
                || gender.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!checkExistedId(id)) {
            JOptionPane.showMessageDialog(null, "Không tồn tại mã khách hàng này", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại không hợp lệ (10 chữ số và bắt đầu bằng số 0)", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!phone.equals(getCustomerById(id).getCustomerPhone()) && checkExistedPhone(phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại này đã thuộc khách hàng khác", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidDate(dob)) {
            JOptionPane.showMessageDialog(null, "Ngày sinh không hợp lệ (dd-MM-yyyy)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidDOB(dob)) {
            JOptionPane.showMessageDialog(null, "Khách hàng chưa đủ 18 tuổi", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int point = getCustomerById(id).getCustomerPoint();
        CustomerDTO customer = new CustomerDTO(id, name, address, phone, DateTime.formatDate(dob), gender, point, 0);

        if (customerDAO.updateCustomer(customer) > 0) {
            JOptionPane.showMessageDialog(null, "Sửa thông tin khách hàng thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Sửa thông tin khách hàng thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteCustomer(String id) {
        if (id.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn khách hàng muốn xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (customerDAO.deleteCustomer(id) > 0) {
            JOptionPane.showMessageDialog(null, "Xóa khách hàng thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Xóa khách hàng thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public String createNewId() {
        loadData();
        int id = customerList.size() + 1;
        return "KH" + String.format("%02d", id);
    }

    public void exportExcel(File path) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet("Khách hàng");
        sheet.trackAllColumnsForAutoSizing();

        int rowIndex = 0;

        writeExcelTitle(sheet, rowIndex);

        loadData();
        for (CustomerDTO customerDTO : customerList) {
            if (customerDTO.getIsDelete() == 0) {
                rowIndex++;
                SXSSFRow row = sheet.createRow(rowIndex);
                writeExcelData(customerDTO, row);
            }
        }

        autoResizeColumn(sheet, 11);

        if(writeExcel(workbook, path)) {
            JOptionPane.showMessageDialog(null, "Xuất danh sách khách hàng thành file excel thành công");
        } else {
            JOptionPane.showMessageDialog(null, "Xuất danh sách khách hàng thành file excel thất bại", "Error",
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

    public void writeExcelData(CustomerDTO customerDTO, SXSSFRow row) {
        SXSSFCell cell = row.createCell(0);
        cell.setCellValue(customerDTO.getCustomerId());

        cell = row.createCell(1);
        cell.setCellValue(customerDTO.getCustomerName());

        cell = row.createCell(2);
        cell.setCellValue(customerDTO.getCustomerPhone());

        cell = row.createCell(3);
        cell.setCellValue(customerDTO.getCustomerAddress());

        cell = row.createCell(4);
        cell.setCellValue(DateTime.formatDate(customerDTO.getCustomerDOB()));

        cell = row.createCell(5);
        cell.setCellValue(customerDTO.getCustomerGender());

        cell = row.createCell(6);
        cell.setCellValue(customerDTO.getCustomerPoint());
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
        cell.setCellValue("Mã khách hàng");

        cell = row.createCell(1);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tên khách hàng");

        cell = row.createCell(2);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Số điện thoại");

        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Địa chỉ");

        cell = row.createCell(4);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ngày sinh");

        cell = row.createCell(5);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Giới tính");

        cell = row.createCell(6);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Điểm tích lũy");
    }

    public void autoResizeColumn(SXSSFSheet sheet, int columns) {
        for (int i = 0; i < columns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public boolean checkExistedPhone(String phone) {
        loadData();

        for (CustomerDTO customerDTO : customerList) {
            if (customerDTO.getIsDelete() == 0 && customerDTO.getCustomerPhone().equals(phone)) {
                return true;
            }
        }

        return false;
    }

    public boolean checkExistedId(String id) {
        loadData();

        for (CustomerDTO customerDTO : customerList) {
            if (customerDTO.getCustomerId().equals(id)) {
                return true;
            }
        }

        return false;
    }

    public CustomerDTO getCustomerById(String id) {
        loadData();

        for (CustomerDTO customerDTO : customerList) {
            if (customerDTO.getIsDelete() == 0 && customerDTO.getCustomerId().equals(id)) {
                return customerDTO;
            }
        }

        return null;
    }

    public CustomerDTO getCustomerByPhone(String phone) {
        loadData();

        for (CustomerDTO customerDTO : customerList) {
            if (customerDTO.getIsDelete() == 0 && customerDTO.getCustomerPhone().equals(phone)) {
                return customerDTO;
            }
        }

        return null;
    }

    public boolean checkPoint(String phone, int discount) {
        CustomerDTO customerDTO = getCustomerByPhone(phone);
        if (customerDTO.getCustomerPoint() < discount) {
            JOptionPane.showMessageDialog(null, "Điểm tích lũy không đủ để quy đổi", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean decreasePoint(String phone, int usedPoint) {
        CustomerDTO customerDTO = getCustomerByPhone(phone);

        if (customerDAO.updatePoint(customerDTO.getCustomerId(), customerDTO.getCustomerPoint() - usedPoint) <= 0) {
            JOptionPane.showMessageDialog(null, "Thất bại khi cập nhật điểm tích lũy sau khi thanh toán", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean increasePoint(String phone, int point) {
        CustomerDTO customerDTO = getCustomerByPhone(phone);

        if (customerDAO.updatePoint(customerDTO.getCustomerId(), customerDTO.getCustomerPoint() + point) <= 0) {
            JOptionPane.showMessageDialog(null, "Thất bại khi cập nhật điểm tích lũy sau khi thanh toán", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public void renderToTable(DefaultTableModel model) {
        loadData();
        model.setRowCount(0);

        for (CustomerDTO customerDTO : customerList) {
            if (customerDTO.getIsDelete() == 0) {
                model.addRow(new Object[]{
                        customerDTO.getCustomerId(),
                        customerDTO.getCustomerName(),
                        customerDTO.getCustomerPhone(),
                        customerDTO.getCustomerGender(),
                        customerDTO.getCustomerPoint()
                });
            }
        }

        model.fireTableDataChanged();
    }
}