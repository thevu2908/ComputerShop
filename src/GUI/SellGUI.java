package GUI;

import BUS.ProductBUS;
import BUS.SellBUS;
import com.toedter.calendar.JDateChooser;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SellGUI {
    private DefaultTableModel productModel;
    private DefaultTableModel sellBillModel;
    private DefaultTableModel billModel;
    private String employeeId;

    private ProductBUS productBUS;
    private SellBUS sellBUS;

    public SellGUI(String employeeId) {
        productBUS = new ProductBUS();
        sellBUS = new SellBUS();
        this.employeeId = employeeId;
        initSell();
        initBill();

        menuSystem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SystemGUI systemGUI = new SystemGUI(employeeId, "LNV03");
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

        btnChooseProd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowSelected = tblProducts.getSelectedRow();
                if (rowSelected < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (txtProdQuantity.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập số lượng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!Validate.isValidNumber(txtProdQuantity.getText(), "Số lượng")) {
                    return;
                }

                String productId = tblProducts.getValueAt(rowSelected, 0).toString();
                int quantity = Integer.parseInt(txtProdQuantity.getText());

                int boughtQuantity = 0;
                for (int i = 0; i < tblSellBills.getRowCount(); i++) {
                    if (tblSellBills.getValueAt(i, 0).equals(productId)) {
                        boughtQuantity = Integer.parseInt(tblSellBills.getValueAt(i, 3).toString());
                        break;
                    }
                }

                if (quantity > Integer.parseInt(tblProducts.getValueAt(rowSelected, 3).toString()) - boughtQuantity) {
                    JOptionPane.showMessageDialog(null, "Số lượng còn lại của sản phẩm không đủ", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                sellBUS.chooseProduct(sellBillModel, productId, quantity);

                int total = sellBUS.calculateTotalPrice();
                if (total > 0) {
                    txtTotal.setText(total + "");
                }

                txtProdQuantity.setText("");
            }
        });

        btnUnchooseProd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowSelected = tblProducts.getSelectedRow();
                if (rowSelected < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn bỏ chọn sản phẩm này ?", "Hỏi",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    sellBUS.unchooseProduct(sellBillModel, tblProducts.getValueAt(rowSelected, 0).toString());
                    JOptionPane.showMessageDialog(null, "Bỏ chọn sản phẩm thành công");

                    int total = sellBUS.calculateTotalPrice();
                    if (total > 0) {
                        txtTotal.setText(total + "");
                    } else {
                        txtTotal.setText("");
                    }
                }
            }
        });

    }

    public void initSell() {
        initProductTable();
        initSellBillTable();
        initProductTableData();
    }

    public void initBill() {
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
        billModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã hóa đơn", "Mã khách hàng", "Mã nhân viên", "Ngày lập", "Tổng tiền"};
        billModel.setColumnIdentifiers(cols);
        tblOrders.setModel(billModel);
        tblOrders.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblOrders.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void initSellBillTable() {
        sellBillModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"};
        sellBillModel.setColumnIdentifiers(cols);
        tblSellBills.setModel(sellBillModel);
        tblSellBills.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblSellBills.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void initProductTableData() {
        productBUS.renderToSellTable(productModel);
    }

    public void initProductTable() {
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
        frame.setContentPane(new SellGUI(employeeId).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JTable tblSellBills;
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
    private JTextField textField1;
    private JTextField textField2;
    private JButton đổiButton;
    private JTextField txtDiscount;
    private JTextField txtFinalTotal;
    private JDateChooser orderDateFrom;
    private JDateChooser orderDateTo;
}
