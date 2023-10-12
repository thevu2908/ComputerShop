package BUS;

import DAO.EmployeeDAO;
import DAO.RoleDAO;
import DTO.EmployeeDTO;
import DTO.ProductDTO;
import DTO.RoleDTO;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class RoleBUS {
    private ArrayList<RoleDTO> roleList;
    private RoleDAO roleDAO;

    public RoleBUS() {
        roleDAO = new RoleDAO();
        loadData();
    }

    public void loadData() {
        roleList = roleDAO.getData();
    }
    public RoleDTO getNameById(String maLoaiNhanVien) {
        loadData();

        for (RoleDTO roleDTO : roleList){
            if (roleDTO.getMaLoaiNhanVien().equals(maLoaiNhanVien)){
                return roleDTO;
            }
        }
        return null;
    }
    public boolean checkExistedId(String maLoaiNhanVien) {
        loadData();
        for (RoleDTO roleDTO : roleList) {
            if (roleDTO.getMaLoaiNhanVien().equals(maLoaiNhanVien)) {
                return true;
            }
        }

        return false;
    }
    public void renderToTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadData();

        for (RoleDTO roleDTO : roleList) {
            if (roleDTO.getTrangThai() == 0 && !roleDTO.getMaLoaiNhanVien().equals("LNV01")) {
                model.addRow(new Object[]{
                        roleDTO.getMaLoaiNhanVien(),
                        roleDTO.getTenLoaiNhanvien(),
                        roleDTO.getTrangThai()
                });
            }
        }

        model.fireTableDataChanged();
    }
}
