package BUS;

import DAO.CustomerDAO;
import DTO.CustomerDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class CustomerBUS {
    private CustomerDAO customerDAO;
    private ArrayList<CustomerDTO> customerList;

    public CustomerBUS() {
        customerDAO = new CustomerDAO();
    }

    public void loadData() {
        customerList = customerDAO.getData();
    }

    public CustomerDTO getCustomerByPhone(String phone){
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
            model.addRow(new Object[]{
                    customerDTO.getCustomerId(),
                    customerDTO.getCustomerName(),
                    customerDTO.getCustomerPhone(),
                    customerDTO.getCustomerGender(),
                    customerDTO.getCustomerPoint()
            });
        }

        model.fireTableDataChanged();
    }
}