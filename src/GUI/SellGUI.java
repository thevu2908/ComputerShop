package GUI;

import BUS.ProductBUS;
import BUS.SellBUS;
import DTO.BillSellDTO;
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
    private String employeeId;

    private ProductBUS productBUS;
    private SellBUS sellBUS;

    private final String ERROR_EMPTY_SELECT_ITEM = "Vui lòng chọn sản phẩm";
    private final String ERROR_EMPTY_QUANTITY = "Vui lòng chọn số lượng trước khi thêm vào hóa đơn";
    private final String ERROR_EXCEED_QUANTITY_REMAIN = "Sản phẩm còn lại không đủ";
    private final String ERROR_EXCEED_QUANTITY = "Chỉ có thể mua 10 sản phẩm một lần";
    private final String SUCCESS_CHOOSE = "Sản phẩm đã được thêm vào hóa đơn";


    public SellGUI(String employeeId) {
        productBUS = new ProductBUS();
        sellBUS =  new SellBUS();
        this.employeeId = employeeId;
        initSell();
        initOrder();

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
                int row = tblProducts.getSelectedRow();
                if(row == -1){
                    JOptionPane.showMessageDialog(null, ERROR_EMPTY_SELECT_ITEM, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String quantityBuy = txtProdQuantity.getText();
                String quantityRemain = String.valueOf(tblProducts.getValueAt(row,3));
                String productId = String.valueOf(tblProducts.getValueAt(row,0));
                String productName = String.valueOf(tblProducts.getValueAt(row,1));
                int productPrice = productBUS.getPriceById(productId);

                if(quantityBuy.equals("")){
                    JOptionPane.showMessageDialog(null, ERROR_EMPTY_QUANTITY, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(Integer.parseInt(quantityBuy) > 10){
                    JOptionPane.showMessageDialog(null, ERROR_EXCEED_QUANTITY, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(Integer.parseInt(quantityBuy) > Integer.parseInt(quantityRemain)){
                    JOptionPane.showMessageDialog(null, ERROR_EXCEED_QUANTITY_REMAIN, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                BillSellDTO billSellDTO = new BillSellDTO(productId,productName,productPrice,Integer.parseInt(quantityBuy),
                        sellBUS.calculateTotalBillSellItem(productPrice,Integer.parseInt(quantityBuy)));

                if(sellBUS.insertBillSellItem(billSellDTO)){
                    JOptionPane.showMessageDialog(null, SUCCESS_CHOOSE, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                }
                initSellOrderTableData();
                resetSellOrderTotalPrice();


            }
        });

        btnUnchooseProd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = tblSellOrders.getSelectedRow();
                if(row == -1){
                    JOptionPane.showMessageDialog(null, ERROR_EMPTY_SELECT_ITEM, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String productId = String.valueOf(tblSellOrders.getValueAt(row,0));

                if(row == -1){
                    JOptionPane.showMessageDialog(null, ERROR_EMPTY_SELECT_ITEM, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int qes= JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn bỏ chọn sản phẩm này ?", "Question",JOptionPane.YES_NO_OPTION);
                if(qes == JOptionPane.YES_OPTION){
                    boolean resultRemove = sellBUS.removeBillSellItem(productId);
                    initSellOrderTableData();
                    resetSellOrderTotalPrice();
                }
            }
        });

    }

    public void initSell() {
        initProductTable();
        initSellOrderTable();
        initProductTableData();
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

    public void initProductTableData() {
        productBUS.renderToSellTable(productModel);
    }

    public void initSellOrderTableData() {
        sellBUS.renderSellOrderTable(sellOrderModel);
    }

    public void resetSellOrderTotalPrice(){
        txtTotal.setText(String.valueOf(sellBUS.calculateTotalBillSell()));
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
