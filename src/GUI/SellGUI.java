package GUI;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SellGUI {
    private DefaultTableModel productModel;
    private DefaultTableModel sellOrderModel;
    private DefaultTableModel orderModel;

    public SellGUI() {
        initSell();
        initOrder();

        menuSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SystemGUI systemGUI = new SystemGUI();
                systemGUI.openSystemGUI();
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                frame.dispose();
            }
        });

        menuLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGUI loginGUI = new LoginGUI();
                loginGUI.openLoginGUI();
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                frame.dispose();
            }
        });
    }

    public void initSell() {
        initProducTable();
        initSellOrderTable();
    }

    public void initOrder() {
        initOrderDateChooser();
        initOrderTable();
    }

    public void initOrderDateChooser() {
        orderDateFrom = new JDateChooser();
        orderDateFromPanel.add(orderDateFrom);
        orderDateTo = new JDateChooser();
        orderDateToPanel.add(orderDateTo);
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

    public void initSellOrderTable() {
        sellOrderModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"};
        sellOrderModel.setColumnIdentifiers(cols);
        tblSellOrders.setModel(sellOrderModel);
        tblSellOrders.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblSellOrders.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void initProducTable() {
        productModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã sản phẩm", "Tên sản phẩm", "Hãng", "Số lượng còn"};
        productModel.setColumnIdentifiers(cols);
        tblProducts.setModel(productModel);
        tblProducts.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblProducts.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void openSellGUI() {
        JFrame frame = new JFrame("Bán hàng");
        frame.setContentPane(new SellGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JTable tblSellOrders;
    private JTable tblProducts;
    private JComboBox cbxSearchProdType;
    private JTextField txtSearchProd;
    private JButton btnAddOrder;
    private JTextField txtTotal;
    private JTable tblOrders;
    private JComboBox cbxOrderPrice;
    private JButton btnResetOrder;
    private JButton btnFilterOrderDate;
    private JPanel orderDateFromPanel;
    private JPanel orderDateToPanel;
    private JComboBox cbxOrderSearchType;
    private JTextField txtSearchOrder;
    private JTextField txtProdQuantity;
    private JButton btnChooseProd;
    private JButton btnUnchooseProd;
    private JPanel orderPanel;
    private JMenuItem menuSystem;
    private JMenuItem menuLogout;
    private JDateChooser orderDateFrom;
    private JDateChooser orderDateTo;
}
