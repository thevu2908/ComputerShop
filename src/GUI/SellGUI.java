package GUI;

import BUS.BillBUS;
import BUS.CustomerBUS;
import BUS.ProductBUS;
import BUS.SellBUS;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import utils.DateTime;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;

public class SellGUI {
    private DefaultTableModel productModel;
    private DefaultTableModel sellBillModel;
    private DefaultTableModel billModel;
    private String employeeId;

    private ProductBUS productBUS;
    private SellBUS sellBUS;
    private CustomerBUS customerBUS;
    private BillBUS billBUS;
    private TableRowSorter<DefaultTableModel> productSorter;
    private TableRowSorter<DefaultTableModel> orderSorter;

    public SellGUI(String employeeId) {
        productBUS = new ProductBUS();
        sellBUS = new SellBUS();
        customerBUS = new CustomerBUS();
        billBUS = new BillBUS();
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
                String discount = txtDiscount.getText();


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
                txtTotal.setText(total + "");
                if (total >= 0 &&  discount.equals("")) {
                    txtFinalTotal.setText(total + "");
                }else{
                    txtFinalTotal.setText(total-Integer.parseInt(discount) + "");
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
                    String discount = txtDiscount.getText();
                    txtTotal.setText(total + "");

                    if (total >= 0 &&  discount.equals("")) {
                        txtFinalTotal.setText(total + "");
                    }else{
                        if(total-Integer.parseInt(discount) >= 0){
                            txtFinalTotal.setText(total-Integer.parseInt(discount) + "");
                        }else{
                            txtFinalTotal.setText("0");
                        }
                    }

                }
            }
        });

        btnAddOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtCustomerPhone.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập số điện thoại của khách hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!Validate.isValidPhone(txtCustomerPhone.getText())) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng của số điện thoại(Gồm 10 chữ số và bắt đầu bằng số 0)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(tblSellBills.getRowCount() <= 0){
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm trước khi thanh toán", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String phone = txtCustomerPhone.getText();
                int finalTotal = Integer.parseInt(txtFinalTotal.getText());
                String discount = txtDiscount.getText();


                int choice = JOptionPane.showConfirmDialog(null,"Bạn có chắc chắn muốn thanh toán ?","Question", JOptionPane.YES_NO_OPTION);
                if(choice == JOptionPane.YES_OPTION){
                    billBUS.addBill(employeeId,phone,finalTotal);
                    sellBUS.addBillDetailList();
                    sellBUS.decreaseQuantityProduct();
                    sellBUS.resetBillDetailList();
                    sellBUS.renderToTableBillSell(sellBillModel);
                    productBUS.renderToProductTable(productModel);
                    if(!discount.equals("")){
                        customerBUS.updatePoint(phone,Integer.parseInt(discount));
                    }
                }
//                sellBUS.paySellBill(employeeId,sellBillModel,productModel,phone,finalTotal);
                initOrdersTableData();
                txtCustomerPhone.setText("");
                txtCustomerPoint.setText("");
                txtTotal.setText("");
                txtDiscount.setText("");
                txtFinalTotal.setText("");
            }
        });

        btnExchangePoint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (txtCustomerPoint.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập điểm cần quy đổi", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!Validate.isValidNumber(txtCustomerPoint.getText(), "Điểm tích lũy")) {
                    return;
                }
                if (txtCustomerPhone.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập số điện thoại của khách hàng", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!Validate.isValidPhone(txtCustomerPhone.getText())) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng của số điện thoại(Gồm 10 chữ số và bắt đầu bằng số 0)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int point = Integer.parseInt(txtCustomerPoint.getText());
                String phone = txtCustomerPhone.getText();
                int total = sellBUS.calculateTotalPrice();

                if(customerBUS.checkPoint(phone,point)){
                    int choice = JOptionPane.showConfirmDialog(null, "Bạn có quy đổi điểm tích lũy ?", "Question",
                            JOptionPane.YES_NO_OPTION);
                    if(choice == JOptionPane.YES_OPTION){
                        txtDiscount.setText(point + "");
                        txtTotal.setText(total + "");
                        txtFinalTotal.setText(total-point + "");
                    }
                }


            }
        });

        txtSearchProd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterSearchProduct();
            }
        });

        txtSearchOrder.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterSearchOrder();
            }
        });

    }

    public void filterSearchProduct() {
        String searchType = cbxSearchProdType.getSelectedItem().toString();
        String searchInfo = txtSearchProd.getText().toLowerCase();

        productSorter = new TableRowSorter<>(productModel);
        tblProducts.setRowSorter(productSorter);

        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                String rowId = entry.getStringValue(0).toLowerCase();
                String rowProductName = entry.getStringValue(1).toLowerCase();
                String rowProductTypeName = entry.getStringValue(2).toLowerCase();
                switch (searchType) {
                    case "Mã sản phẩm":
                        return rowId.contains(searchInfo);
                    case "Tên sản phẩm":
                        return rowProductName.contains(searchInfo);
                    case "Hãng sản phẩm":
                        return rowProductTypeName.contains(searchInfo);
                    default:
                        return true;
                }
            }
        };

        productSorter.setRowFilter(filter);
    }

    public void filterSearchOrder(){

        try{
            String searchType = cbxOrderSearchType.getSelectedItem().toString();
            String searchInfo = txtSearchOrder.getText().toLowerCase();
            String searchPriceType = cbxOrderPrice.getSelectedItem().toString();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            Date dateFrom = orderDateFrom.getDate() == null ? formatter.parse("1970-01-01") : orderDateFrom.getDate();
            Date dateTo = orderDateTo.getDate() == null ? formatter.parse("2030-01-01") : orderDateTo.getDate();


            if (dateFrom.compareTo(dateTo) > 0) {
                JOptionPane.showMessageDialog(null, "Ngày đến phải sau ngày bắt đầu", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            orderSorter = new TableRowSorter<>(billModel);
            tblOrders.setRowSorter(orderSorter);

            RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
                public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    try {
                        String rowBillId = entry.getStringValue(0).toLowerCase();
                        String rowCustomerId = entry.getStringValue(1).toLowerCase();
                        String rowEmployeeId = entry.getStringValue(2).toLowerCase();
                        Date rowDate = formatter.parse(entry.getStringValue(3));
                        int rowPrice = Integer.parseInt(entry.getStringValue(4));

                        switch (searchType) {
                            case "Mã hóa đơn":
                                if(searchPriceType.equals("Dưới 30 triệu")){
                                    return rowBillId.contains(searchInfo) && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0
                                            && rowPrice <= 30000000;
                                }else{
                                    if(searchPriceType.equals("Từ 30 đến 70 triệu")){
                                        return rowBillId.contains(searchInfo) && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0
                                                && rowPrice >= 30000000 && rowPrice <= 70000000;
                                    }else{
                                        return rowBillId.contains(searchInfo) && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0
                                                && rowPrice >= 70000000;
                                    }
                                }
                            case "Mã nhân viên":
                                if(searchPriceType.equals("Dưới 30 triệu")){
                                    return rowEmployeeId.contains(searchInfo) && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0
                                            && rowPrice <= 30000000;
                                }else{
                                    if(searchPriceType.equals("Từ 30 đến 70 triệu")){
                                        return rowEmployeeId.contains(searchInfo) && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0
                                                && rowPrice >= 30000000 && rowPrice <= 70000000;
                                    }else{
                                        return rowEmployeeId.contains(searchInfo) && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0
                                                && rowPrice >= 70000000;
                                    }
                                }
                            case "Mã khách hàng":
                                if(searchPriceType.equals("Dưới 30 triệu")){
                                    return rowCustomerId.contains(searchInfo) && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0
                                            && rowPrice <= 30000000;
                                }else{
                                    if(searchPriceType.equals("Từ 30 đến 70 triệu")){
                                        return rowCustomerId.contains(searchInfo) && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0
                                                && rowPrice >= 30000000 && rowPrice <= 70000000;
                                    }else{
                                        return rowCustomerId.contains(searchInfo) && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0
                                                && rowPrice >= 70000000;
                                    }
                                }
                            default:
                                return true;
                        }
                    } catch (Exception e) {
                        return true;
                    }
                }
            };

            orderSorter.setRowFilter(filter);

        }catch (Exception ex){
            return;
        }

    }



    public void initSell() {
        initProductTable();
        initSellBillTable();
        initProductTableData();
    }

    public void initBill() {
        initOrderDateChooser();
        initOrderTable();
        initOrdersTableData();
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

    public void initOrdersTableData(){
        billBUS.renderToOrdersTable(billModel,employeeId);
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
    private JTextField txtCustomerPhone;
    private JTextField txtCustomerPoint;
    private JButton btnExchangePoint;
    private JTextField txtDiscount;
    private JTextField txtFinalTotal;
    private JButton btnViewCustomerInfo;
    private JDateChooser orderDateFrom;
    private JDateChooser orderDateTo;
}
