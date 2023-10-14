package GUI;

import BUS.BillBUS;
import BUS.CustomerBUS;
import BUS.ProductBUS;
import BUS.SellBUS;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
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
    private TableRowSorter<DefaultTableModel> billSorter;

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
                    JOptionPane.showMessageDialog(null, "Số lượng còn lại của sản phẩm không đủ", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                sellBUS.chooseProduct(sellBillModel, productId, quantity);

                int total = sellBUS.calculateTotalPrice();
                txtTotal.setText(total + "");

                if (total >= 0 && discount.equals("")) {
                    txtFinalTotal.setText(total + "");
                } else {
                    txtFinalTotal.setText(total - Integer.parseInt(discount) + "");
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

                int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn bỏ chọn sản phẩm này ?", "Câu hỏi",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    sellBUS.unchooseProduct(sellBillModel, tblProducts.getValueAt(rowSelected, 0).toString());
                    JOptionPane.showMessageDialog(null, "Bỏ chọn sản phẩm thành công");

                    int total = sellBUS.calculateTotalPrice();
                    String discount = txtDiscount.getText();
                    txtTotal.setText(total + "");

                    if (total >= 0 && discount.equals("")) {
                        txtFinalTotal.setText(total + "");
                    } else {
                        if (total - Integer.parseInt(discount) >= 0) {
                            txtFinalTotal.setText(total - Integer.parseInt(discount) + "");
                        } else {
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
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập số điện thoại của khách hàng", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!Validate.isValidPhone(txtCustomerPhone.getText())) {
                    JOptionPane.showMessageDialog(null, "Số điện thoại phải là 10 chữ số", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (tblSellBills.getRowCount() <= 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm trước khi thanh toán", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null,"Bạn có chắc chắn muốn thanh toán ?","Question",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    int finalTotal = Integer.parseInt(txtFinalTotal.getText());
                    String phone = txtCustomerPhone.getText();
                    String usedPoint = txtCustomerPoint.getText();
                    int bonusPoint = calculateBonusPoint(finalTotal);

                    if (billBUS.addBill(employeeId, phone, finalTotal) && sellBUS.addBillDetail()
                            && sellBUS.decreaseProductQuantity() && customerBUS.increasePoint(phone, bonusPoint)) {

                        sellBUS.resetBillDetailList();
                        sellBUS.renderToTableBillSell(sellBillModel);
                        resetSellData();
                        resetBillData();

                        if (!usedPoint.equals("")) {
                            customerBUS.decreasePoint(phone, Integer.parseInt(usedPoint));
                        }
                    }
                }
            }
        });

        btnExchangePoint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtCustomerPhone.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập số điện thoại của khách hàng", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (txtCustomerPoint.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập điểm cần quy đổi", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!Validate.isValidNumber(txtCustomerPoint.getText(), "Điểm tích lũy")) {
                    return;
                }

                if (!Validate.isValidPhone(txtCustomerPhone.getText())) {
                    JOptionPane.showMessageDialog(null, "Số điện thoại phải là 10 chữ số", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int point = Integer.parseInt(txtCustomerPoint.getText());
                if (point % 10 != 0) {
                    JOptionPane.showMessageDialog(null, "Điểm phải là số chia hết cho 10 (10, 1000, 2000...)", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn quy đổi điểm tích lũy ?", "Câu hỏi",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    String phone = txtCustomerPhone.getText();
                    int total = sellBUS.calculateTotalPrice();

                    if (!customerBUS.checkPoint(phone, point)) {
                        return;
                    }

                    int discount = calculateDiscount(point);
                    txtDiscount.setText(discount + "");
                    txtTotal.setText(total + "");
                    txtFinalTotal.setText(total - discount + "");
                }
            }
        });

        txtSearchProd.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterProduct();
            }
        });

        txtSearchBill.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterBill();
            }
        });

        cbxBillPrice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterBill();
            }
        });

        btnFilterBillDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (billDateFrom.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày bắt đầu", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (billDateFrom.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày kết thúc", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                filterBill();
            }
        });

        btnResetBill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetBillData();
            }
        });
    }

    public void resetBillData() {
        cbxBillSearchType.setSelectedIndex(0);
        txtSearchBill.setText("");
        cbxBillPrice.setSelectedIndex(0);
        billDateFrom.setDate(null);
        billDateTo.setDate(null);
        billSorter.setRowFilter(null);
        initBillTableData();
    }

    public void resetSellData() {
        txtProdQuantity.setText("");
        txtCustomerPhone.setText("");
        txtCustomerPoint.setText("");
        txtTotal.setText("");
        txtDiscount.setText("");
        txtFinalTotal.setText("");
        productSorter.setRowFilter(null);
        initProductTableData();
    }

    public int calculateBonusPoint(long totalPrice) {
        return (int) (totalPrice * 100 / 1000000);
    }

    public int calculateDiscount(int point) {
        return point * 1000000 / 1000;
    }

    public void filterProduct() {
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

    public void filterBill() {
        try {
            String searchType = cbxBillSearchType.getSelectedItem().toString();
            String searchInfo = txtSearchBill.getText().toLowerCase();

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date dateFrom = billDateFrom.getDate() == null ? formatter.parse("01-01-1970") : billDateFrom.getDate();
            Date dateTo = billDateTo.getDate() == null ? formatter.parse("31-12-2050") : billDateTo.getDate();

            if (dateFrom.compareTo(dateTo) > 0) {
                JOptionPane.showMessageDialog(null, "Ngày đến phải sau ngày bắt đầu", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int billPrice = cbxBillPrice.getSelectedIndex();
            int minPrice = 0;
            int maxPrice = 1999999999;

            if (billPrice == 1) {
                maxPrice = 30000000 - 1;
            } else if (billPrice == 2) {
                minPrice = 30000000;
                maxPrice = 70000000;
            }
            else if (billPrice == 3){
                minPrice = 70000000 + 1;
            }

            int finalMinPrice = minPrice;
            int finalMaxPrice = maxPrice;

            billSorter = new TableRowSorter<>(billModel);
            tblBills.setRowSorter(billSorter);

            RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    try {
                        String rowBillId = entry.getStringValue(0).toLowerCase();
                        String rowCustomerId = entry.getStringValue(1).toLowerCase();
                        Date rowDate = formatter.parse(entry.getStringValue(3));
                        int rowPrice = Integer.parseInt(entry.getStringValue(4));

                        switch (searchType) {
                            case "Mã hóa đơn":
                                return rowBillId.contains(searchInfo)
                                        && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0
                                        && rowPrice >= finalMinPrice && rowPrice <= finalMaxPrice;
                            case "Mã khách hàng":
                                return rowCustomerId.contains(searchInfo)
                                        && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0
                                        && rowPrice >= finalMinPrice && rowPrice <= finalMaxPrice;
                            default:
                                return true;
                        }
                    } catch (Exception e) {
                        return true;
                    }
                }
            };

            billSorter.setRowFilter(filter);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initSell() {
        initProductTable();
        initSellBillTable();
        initProductTableData();
    }

    public void initBill() {
        initBillDateChooser();
        initBillTable();
        initBillTableData();
    }

    public void initBillDateChooser() {
        billDateFrom = new JDateChooser();
        billDateFrom.setDateFormatString("dd-MM-yyyy");
        billDateFromPanel.add(billDateFrom);

        billDateTo = new JDateChooser();
        billDateTo.setDateFormatString("dd-MM-yyyy");
        billDateToPanel.add(billDateTo);
    }

    public void initBillTableData() {
        billBUS.renderToTable(billModel, employeeId);
    }

    public void initBillTable() {
        billModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã hóa đơn", "Mã khách hàng", "Mã nhân viên", "Ngày lập", "Tổng tiền"};
        billModel.setColumnIdentifiers(cols);
        tblBills.setModel(billModel);
        tblBills.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblBills.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
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

        String[] cols = {"Mã sản phẩm", "Tên sản phẩm", "Hãng", "Giá", "Số lượng còn"};
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
    private JTable tblBills;
    private JComboBox cbxBillPrice;
    private JButton btnResetBill;
    private JButton btnFilterBillDate;
    private JPanel billDateFromPanel;
    private JPanel billDateToPanel;
    private JComboBox cbxBillSearchType;
    private JTextField txtSearchBill;
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
    private JButton btnPrintBill;
    private JDateChooser billDateFrom;
    private JDateChooser billDateTo;
}
