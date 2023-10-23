package BUS;

import DAO.CustomerDAO;
import DTO.CustomerDTO;
import utils.DateTime;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
            JOptionPane.showMessageDialog(null, "Số điện thoại phải là 10 chữ số", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

        if (!Validate.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại phải là 10 chữ số", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!getCustomerByPhone(phone).getCustomerId().equals(id) && checkExistedPhone(phone)) {
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

    public boolean checkExistedPhone(String phone) {
        loadData();

        for (CustomerDTO customerDTO : customerList) {
            if (customerDTO.getCustomerPhone().equals(phone)) {
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
            if (customerDTO.getCustomerId().equals(id)) {
                return customerDTO;
            }
        }

        return null;
    }

    public CustomerDTO getCustomerByPhone(String phone) {
        loadData();

        for (CustomerDTO customerDTO : customerList) {
            if (customerDTO.getCustomerPhone().equals(phone)) {
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