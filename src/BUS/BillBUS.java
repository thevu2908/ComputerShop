package BUS;

import DAO.BillDAO;
import DTO.BillDTO;
import DTO.CustomerDTO;
import DTO.EmployeeDTO;
import utils.DateTime;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Collections;

public class BillBUS {
    private BillDAO billDAO;
    private CustomerBUS customerBUS;
    private ArrayList<BillDTO> billList;
    private EmployeeBUS employeeBUS;

    public BillBUS() {
        billDAO = new BillDAO();
        customerBUS = new CustomerBUS();
        employeeBUS = new EmployeeBUS();
        employeeBUS = new EmployeeBUS();
    }

    public void loadData() {
        billList = billDAO.getData();
    }

    public String getNewBillId() {
        loadData();
        int id = billList.size() + 1;
        return "HD" + String.format("%03d", id);
    }

    public boolean addBill(String employeeId, String phone, int total, int discount) {
        if (phone.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số điện thoại của khách hàng", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidPhone(phone)) {
            JOptionPane.showMessageDialog(null, "Số điện thoại phải là 10 chữ số", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        CustomerDTO customerDTO = customerBUS.getCustomerByPhone(phone);
        String billId = getNewBillId();
        String currentDate = DateTime.getStringCurrentDate();

        BillDTO billDTO = new BillDTO(billId, customerDTO.getCustomerId(), employeeId, currentDate, total, discount, 0);

        if (billDAO.addBill(billDTO) > 0) {
            JOptionPane.showMessageDialog(null, "Thanh toán thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thanh toán thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public double[] getRevenueBillOfMonths(int year) {
        loadData();

        double[] listDT = new double[12];

        for (int i = 0; i < listDT.length; i++) {
            listDT[i] = 0;
        }

        for (BillDTO billDTO : billList) {
            int monthOfBill = Integer.parseInt(String.valueOf(billDTO.getBillDate()).split("-")[1]);
            int yearOfBill = Integer.parseInt(String.valueOf(billDTO.getBillDate()).split("-")[0]);
            if (yearOfBill == year) {
                listDT[monthOfBill - 1] += (billDTO.getTotal() - billDTO.getDiscount()) * 1.0 / 1000000;
            }
        }

        return listDT;
    }

    public double getRevenueByEmployeeId(String employeeId, String month, int year) {
        double tong = 0;
        for (BillDTO billDTO : billList) {
            String[] date = billDTO.getBillDate().split("-");
            String billMonth = date[1];
            int billYear = Integer.parseInt(date[0]);

            if (billDTO.getEmployeeId().equals(employeeId) && billMonth.equals(month) && billYear == year) {
                tong += (billDTO.getTotal() - billDTO.getDiscount()) * 1.0 / 1000000;
            }
        }
        return tong;
    }

    public ArrayList<EmployeeDTO> getExcellentEmployee(String month, int year) {
        loadData();

        ArrayList<EmployeeDTO> listExcellentEmployee = new ArrayList<>();
        ArrayList<EmployeeDTO> empList = employeeBUS.getEmployeeList();

        for (EmployeeDTO employee : empList) {
            double total = getRevenueByEmployeeId(employee.getEmployeeId(), month, year);

            if (employee.getEmployeeType().equals("LNV03") && total > 0) {
                EmployeeDTO employeeDTO = new EmployeeDTO(employee.getEmployeeId(), total);

                listExcellentEmployee.add(employeeDTO);
            }
        }

        Collections.sort(listExcellentEmployee, (x, y) -> (int) (y.getTotal() - x.getTotal()));
        return listExcellentEmployee;
    }

    public ArrayList<String> getListBillIdInMonth(String month, int year) {
        loadData();
        ArrayList<String> ListBillIdInMonth = new ArrayList<>();

        for (BillDTO billDTO : billList) {
            String[] date = billDTO.getBillDate().split("-");
            String billMonth = date[1];
            int billYear = Integer.parseInt(date[0]);

            if (billMonth.equals(month) && billYear == year) {
                ListBillIdInMonth.add(billDTO.getBillId());
            }
        }

        return ListBillIdInMonth;
    }

    public BillDTO getBillById(String billId) {
        loadData();

        for (BillDTO billDTO : billList) {
            if (billDTO.getBillId().equals(billId)) {
                return billDTO;
            }
        }

        return null;
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
                        billDTO.getTotal(),
                        billDTO.getDiscount(),
                        billDTO.getTotal() - billDTO.getDiscount()
                });
            }
        }

        model.fireTableDataChanged();
    }

    public void renderToTable(DefaultTableModel model) {
        loadData();
        model.setRowCount(0);

        for (BillDTO billDTO : billList) {
            if (billDTO.getIsDeleted() == 0) {
                model.addRow(new Object[]{
                        billDTO.getBillId(),
                        billDTO.getCustomerId(),
                        billDTO.getEmployeeId(),
                        DateTime.formatDate(billDTO.getBillDate()),
                        billDTO.getTotal(),
                        billDTO.getDiscount(),
                        billDTO.getTotal() - billDTO.getDiscount()
                });
            }
        }

        model.fireTableDataChanged();
    }
}