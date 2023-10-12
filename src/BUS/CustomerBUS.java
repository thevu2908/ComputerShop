package BUS;

import DAO.CustomerDAO;
import DTO.CustomerDTO;


import javax.swing.*;
import java.util.ArrayList;

public class CustomerBUS {
    private CustomerDAO customerDAO;
    private ArrayList<CustomerDTO> customerList = new ArrayList<>();

    public CustomerBUS() {
        customerDAO = new CustomerDAO();
    }

    public void loadProductData() {
        customerList = customerDAO.getData();
    }

    public CustomerDTO getCustomerByPhone(String phone){
        loadProductData();
        for(CustomerDTO customerDTO : customerList){
            if(customerDTO.getCustomerPhone().equals(phone)){
                return customerDTO;
            }
        }
        return null;
    }

    public boolean checkPoint(String phone, int discount){
        CustomerDTO customerDTO = getCustomerByPhone(phone);
        if(customerDTO.getCustomerPoint() < discount){
            JOptionPane.showMessageDialog(null, "Điểm tích lũy không đủ để quy đổi", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean updatePoint(String phone, int discount){
        CustomerDTO customerDTO = getCustomerByPhone(phone);
        if(customerDAO.updatePoint(customerDTO.getCustomerId(),customerDTO.getCustomerPoint() - discount) == -1){
            JOptionPane.showMessageDialog(null, "Thất bại khi cập nhật điểm tích lũy sau khi thanh toán", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }




}
