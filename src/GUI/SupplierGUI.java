package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SupplierGUI {
    private DefaultTableModel supplierModel;

    public SupplierGUI() {
        initTable();
    }

    public void initTable(){
        supplierModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã nhà cung cấp", "Tên nhà cung cấp", "Số điện thoại", "Địa chỉ"};
        supplierModel.setColumnIdentifiers(cols);
        tblSuppliers.setModel(supplierModel);
        tblSuppliers.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 0; i< tblSuppliers.getColumnCount(); i++){
            tblSuppliers.getColumnModel().getColumn(i).setCellRenderer(centerRender);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JTable tblSuppliers;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JTextField txtSupplierId;
    private JTextField txtSuppplierName;
    private JTextField txtSupplierPhone;
    private JTextField txtSupplierAddress;
    private JPanel mainPanel;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnReset;
    private JButton btnCreateId;
}
