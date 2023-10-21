package BUS;

import DAO.SupplierDAO;
import DTO.SupplierDTO;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
            JOptionPane.showMessageDialog(null, "Số điện thoại phải là 10 chữ số", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
        if (id.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn nhà cung cấp muốn xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

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
            JOptionPane.showMessageDialog(null, "Số điện thoại phải là 10 chữ số", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!checkExistedId(Id)) {
            JOptionPane.showMessageDialog(null, "Không tồn tại mã nhà cung cấp này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!getIdByPhone(Phone).equals(Id) && checkExistedPhone(Phone)) {
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

    public String getNameById(String id) {
        loadData();

        for (SupplierDTO supplierDTO : supplierList) {
            if (supplierDTO.getSupplierId().equals(id)) {
                return supplierDTO.getSupplierName();
            }
        }

        return "";
    }

    public SupplierDTO getSupplierById(String id) {
        loadData();

        for (SupplierDTO supplierDTO : supplierList) {
            if (supplierDTO.getSupplierId().equals(id)) {
                return supplierDTO;
            }
        }

        return null;
    }

    public String getIdByPhone(String phone) {
        loadData();

        for (SupplierDTO supplierDTO : supplierList) {
            if (supplierDTO.getSupplierPhone().equals(phone)) {
                return supplierDTO.getSupplierId();
            }
        }

        return "";
    }

    public String createNewSupplierID() {
        loadData();
        int id = supplierList.size() + 1;
        return "NCC" + String.format("%02d", id);
    }

    public boolean checkExistedPhone(String phone) {
        loadData();

        for (SupplierDTO supplierDTO : supplierList) {
            if (supplierDTO.getSupplierPhone().equals(phone)) {
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