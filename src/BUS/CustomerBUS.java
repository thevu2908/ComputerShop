package BUS;

import DAO.CustomerDAO;
import DTO.CustomerDTO;

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

    public String getNameById(String id){
        loadData();

        for(CustomerDTO customerDTO : customerList){
            if (customerDTO.getCustomerId().equals(id)){
                return customerDTO.getCustomerName();
            }
        }

        return "";
    }

    public void renderToTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadData();

        for (CustomerDTO customerDTO : customerList) {
            if (customerDTO.getIsDelete() == 0 && !customerDTO.getCustomerId().equals("KH00")) {
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