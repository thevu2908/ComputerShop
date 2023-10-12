package BUS;

import DAO.BillDAO;
import DTO.BillDTO;
import DTO.CustomerDTO;
import DTO.ImportDTO;
import DTO.ProductDTO;
import utils.DateTime;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class BillBUS {
    private BillDAO billDAO;
    private CustomerBUS customerBUS;
    private ArrayList<BillDTO> billList;
    private ArrayList<BillDTO> billListOfEmployee;
    public BillBUS() {
        billDAO = new BillDAO();
        customerBUS = new CustomerBUS();
        billList = new ArrayList<>();
        billListOfEmployee = new ArrayList<>();
    }

    public void loadBillList(){
        billList = billDAO.getData();
    }

    public String getNewBillId(){
        loadBillList();
        int id = billList.size() + 1;
        return "HD" + String.format("%01d", id);
    }


//    public boolean addBill(String billId, String employeeId, String customerId, int total ) {
//        if ( billId.equals("") || employeeId.equals("") || customerId.equals("")) {
//            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//
//        String currentDate = DateTime.getCurrentDate();
//
//        BillDTO billDTO = new BillDTO(billId, customerId, employeeId, currentDate, total, 0);
//
//        if (billDAO.addBill(billDTO) > 0) {
//            JOptionPane.showMessageDialog(null, "Thanh toán thành công");
//            return true;
//        } else {
//            JOptionPane.showMessageDialog(null, "Thanh toán thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//    }
    public boolean addBill(String employeeId, String phone, int finalTotal ) {
        if ( employeeId.equals("") || phone.equals("")) {
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

    public void renderToOrdersTable(DefaultTableModel model,String employeeId) {
        model.setRowCount(0);
        billListOfEmployee = billDAO.getBillListOfEmployee(employeeId);

        for (BillDTO billDTO : billListOfEmployee) {
            if (billDTO.getIsDeleted() == 0) {
                model.addRow(new Object[]{
                        billDTO.getBillId(),
                        billDTO.getCustomerId(),
                        billDTO.getEmployeeId(),
                        billDTO.getBillDate(),
                        billDTO.getTotal()
                });
            }
        }

        model.fireTableDataChanged();
    }


}
