package GUI;

import BUS.CustomerBUS;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerGUI {
    private DefaultTableModel model;
    private CustomerBUS customerBUS;

    public CustomerGUI() {
        customerBUS = new CustomerBUS();
        initDateChooser();
        initTable();
        initTableData();
    }

    public void initTableData() {
        customerBUS.renderToTable(model);
    }

    public void initTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Giới tính", "Điểm tích lũy"};
        model.setColumnIdentifiers(cols);
        tblCustomers.setModel(model);
        tblCustomers.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblCustomers.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void initDateChooser() {
        customerDOB = new JDateChooser();
        customerDOB.setDateFormatString("dd-MM-yyyy");
        customerDOB.setPreferredSize(new Dimension(-1, 30));
        datePanel.add(customerDOB);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;
    private JPanel panelBottom;
    private JPanel Left;
    private JTable tblCustomers;
    private JPanel Right;
    private JComboBox cbxFilterGender;
    private JComboBox cbxFilterPoint;
    private JTextField txtCustomerPoint;
    private JTextField txtCustomerName;
    private JTextField txtCustomerPhone;
    private JTextField txtCustomerAddress;
    private JComboBox cbxGender;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnReset;
    private JTextField txtCustomerId;
    private JPanel datePanel;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JButton btnCreateNewId;
    private JDateChooser customerDOB;
}