package BUS;

import DAO.BillDAO;
import DTO.BillDTO;
import DTO.CustomerDTO;
import DTO.EmployeeDTO;
import utils.DateTime;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BillBUS {
    private BillDAO billDAO;
    private CustomerBUS customerBUS;
    private ArrayList<BillDTO> billList;
    EmployeeBUS employeeBUS;
    EmployeeDTO employeeDTO;
    private final int _MONTHS = 12;

    public BillBUS() {
        billDAO = new BillDAO();
        customerBUS = new CustomerBUS();
        employeeBUS = new EmployeeBUS();
        employeeDTO = new EmployeeDTO();
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
                        billDTO.getTotal()
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
                        billDTO.getTotal()
                });
            }
        }

        model.fireTableDataChanged();
    }

    public int[] getRevenueBillOfMonths(int year) {
        loadData();
        int[] listDT = new int[12];
        for (int i = 0; i < listDT.length; i++) {
            listDT[i] = 0;
        }

        for (BillDTO billDTO : billList) {
            int monthOfBill = Integer.parseInt(String.valueOf(billDTO.getBillDate()).split("-")[1]);
            int yearOfBill = Integer.parseInt(String.valueOf(billDTO.getBillDate()).split("-")[0]);
            if (yearOfBill == year) {
                listDT[monthOfBill - 1] += billDTO.getTotal();
            }
        }
        return listDT;
    }


    public int getRevenueByEmployeeId(String employeeId, int month) {
        int tong = 0;
        for (BillDTO billDTO : billList) {
            int monthOfBill = Integer.parseInt(String.valueOf(billDTO.getBillDate()).split("-")[1]);
            if (billDTO.getEmployeeId().equals(employeeId) && monthOfBill == month) {
                tong += billDTO.getTotal();
            }
        }
        return tong;
    }



    public ArrayList<BillDTO> getExcellentEmployee() {
        loadData();
        LocalDate currentDate = LocalDate.now();
        int month = Integer.parseInt(String.valueOf(currentDate.getMonthValue()));
        ArrayList<BillDTO> listExcellentEmployee = new ArrayList<>();
        ArrayList<String> listEmpId = employeeBUS.getListEmployeeId();

        for (String id : listEmpId) {
            BillDTO billDTO = new BillDTO(id, getRevenueByEmployeeId(id, month));
            listExcellentEmployee.add(billDTO);
//            }
        }
        Comparator<BillDTO> revenueComparator = (e1, e2) -> Integer.compare(e1.getTotal(), e2.getTotal());
        Collections.sort(listExcellentEmployee, revenueComparator);
        Collections.reverse(listExcellentEmployee);
        return listExcellentEmployee;
    }

    public ArrayList<String> getListBillIdInMonth() {
        loadData();
        ArrayList<String> ListBillIdInMonth = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        int currentMonth = Integer.parseInt(String.valueOf(currentDate.getMonthValue()));
        int currentYear = Integer.parseInt(String.valueOf(currentDate.getYear()));
        for (BillDTO billDTO : billList) {
//            String[] dateOfBill = billDTO.getBillDate().split("-");
            int monthOfBill = Integer.parseInt(billDTO.getBillDate().split("-")[1]);
            int yearOfBill = Integer.parseInt(billDTO.getBillDate().split("-")[0]);
            if ( monthOfBill == currentMonth && yearOfBill == currentYear ) {
                ListBillIdInMonth.add(billDTO.getBillId());
            }
        }
        return ListBillIdInMonth;
    }


}