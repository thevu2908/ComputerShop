package BUS;

import DAO.AccessDAO;
import DTO.AccessDTO;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class AccessBUS {
    private ArrayList<AccessDTO> accessList;
    private AccessDAO accessDAO;

    public AccessBUS() {
        accessDAO = new AccessDAO();
    }

    public void loadData() {
        accessList = accessDAO.getData();
    }

    public void renderToTable(DefaultTableModel model) {
        loadData();

        for (AccessDTO accessDTO : accessList) {
            if (accessDTO.getIsDeleted() == 0 && !accessDTO.getAccessId().equals("LNV01")) {
                model.addRow(new Object[]{
                        accessDTO.getAccessId(),
                        accessDTO.getAccessName()
                });
            }
        }

        model.fireTableDataChanged();
    }
}