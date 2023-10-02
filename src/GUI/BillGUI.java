package GUI;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BillGUI {
    private DefaultTableModel orderModel;

    public BillGUI() {
        initOrderDateChooser();
        initOrderTable();
    }

    public void initOrderTable() {
        orderModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã hóa đơn", "Mã khách hàng", "Mã nhân viên", "Ngày lập", "Tổng tiền"};
        orderModel.setColumnIdentifiers(cols);
        tblOrders.setModel(orderModel);
        tblOrders.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblOrders.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void initOrderDateChooser() {
        orderDateFrom = new JDateChooser();
        orderDateFromPanel.add(orderDateFrom);
        orderDateTo = new JDateChooser();
        orderDateToPanel.add(orderDateTo);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;
    private JComboBox cbxOrderSearchType;
    private JTextField txtSearchOrder;
    private JComboBox cbxOrderPrice;
    private JButton btnResetOrder;
    private JPanel orderDateFromPanel;
    private JPanel orderDateToPanel;
    private JButton btnFilterOrderDate;
    private JTable tblOrders;
    private JDateChooser orderDateFrom;
    private JDateChooser orderDateTo;
}
