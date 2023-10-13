package BUS;

import DAO.BillDAO;
import DTO.BillDTO;
import DTO.CustomerDTO;
import utils.DateTime;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class BillBUS {
    private BillDAO billDAO;
    private CustomerBUS customerBUS;
    private ArrayList<BillDTO> billList;

    public BillBUS() {
        billDAO = new BillDAO();
        customerBUS = new CustomerBUS();
    }

    public void loadData(){
        billList = billDAO.getData();
    }

    public String getNewBillId(){
        loadData();
        int id = billList.size() + 1;
        return "HD" + String.format("%03d", id);
    }

    public boolean addBill(String employeeId, String phone, int finalTotal) {
        if (employeeId.equals("") || phone.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        CustomerDTO customerDTO = customerBUS.getCustomerByPhone(phone);
        String billId = getNewBillId();
        String currentDate = DateTime.getCurrentDate();

        BillDTO billDTO = new BillDTO(billId, customerDTO.getCustomerId(), employeeId, currentDate, finalTotal, 0);

        if (billDAO.addBill(billDTO) > 0) {
            JOptionPane.showMessageDialog(null, "Thanh toán thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thanh toán thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void renderToTable(DefaultTableModel model, String employeeId) {
        loadData();
        model.setRowCount(0);

        for (BillDTO billDTO : billList) {
            if (billDTO.getIsDeleted() == 0 && billDTO.getEmployeeId().equals(employeeId)) {
                model.addRow(new Object[]{
                        billDTO.getBillId(),
                        billDTO.getCustomerId(),
                        billDTO.getEmployeeId(),
                        DateTime.formatDate(billDTO.getBillDate()),
                        billDTO.getTotal()
                });
            }
        }

        model.fireTableDataChanged();
    }
}