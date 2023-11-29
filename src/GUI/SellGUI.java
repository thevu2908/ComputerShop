package GUI;

import BUS.*;
import DTO.ProductDTO;
import com.toedter.calendar.JDateChooser;

import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import validation.Validate;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
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
    private BillDetailBUS billDetailBUS;
    private TableRowSorter<DefaultTableModel> productSorter;
    private TableRowSorter<DefaultTableModel> billSorter;

    public SellGUI(String employeeId) {
        this.employeeId = employeeId;
        productBUS = new ProductBUS();
        sellBUS = new SellBUS();
        customerBUS = new CustomerBUS();
        billBUS = new BillBUS();
        billDetailBUS = new BillDetailBUS();
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
                int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn đăng xuất ?", "Xác nhận",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    LoginGUI loginGUI = new LoginGUI();
                    loginGUI.openLoginGUI();
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                    frame.dispose();
                }
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

                if (quantity > Integer.parseInt(tblProducts.getValueAt(rowSelected, 5).toString())) {
                    JOptionPane.showMessageDialog(null, "Số lượng còn lại của sản phẩm không đủ", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                sellBUS.chooseProduct(sellBillModel, productId, quantity);

                ArrayList<ProductDTO> boughtProductList = new ArrayList<>();
                for (int i = 0; i < tblSellBills.getRowCount(); i++) {
                    String id = tblSellBills.getValueAt(i, 0).toString();
                    int billQuantity = Integer.parseInt(tblSellBills.getValueAt(i, 3).toString());
                    boughtProductList.add(new ProductDTO(id, billQuantity));
                }

                productBUS.renderToSellTable(productModel, boughtProductList);

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
                int rowSelected = tblSellBills.getSelectedRow();
                if (rowSelected < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn bỏ chọn sản phẩm này ?", "Câu hỏi",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    sellBUS.unchooseProduct(sellBillModel, tblSellBills.getValueAt(rowSelected, 0).toString());

                    ArrayList<ProductDTO> boughtProductList = new ArrayList<>();
                    for (int i = 0; i < tblSellBills.getRowCount(); i++) {
                        String id = tblSellBills.getValueAt(i, 0).toString();
                        int billQuantity = Integer.parseInt(tblSellBills.getValueAt(i, 3).toString());
                        boughtProductList.add(new ProductDTO(id, billQuantity));
                    }

                    productBUS.renderToSellTable(productModel, boughtProductList);

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

        btnAddBill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tblSellBills.getRowCount() <= 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm trước khi thanh toán", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn thanh toán ?","Question",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    int total = Integer.parseInt(txtTotal.getText());
                    int finalTotal = Integer.parseInt(txtFinalTotal.getText());
                    int discount = txtDiscount.getText().equals("") ? 0 : Integer.parseInt(txtDiscount.getText());
                    String phone = txtCustomerPhone.getText();
                    String usedPoint = txtCustomerPoint.getText();
                    int bonusPoint = calculateBonusPoint(finalTotal);

                    if (billBUS.addBill(employeeId, phone, total, discount) && sellBUS.addBillDetail()
                            && sellBUS.decreaseProductQuantity()) {
                        if (!phone.equals("")) {
                            customerBUS.increasePoint(phone, bonusPoint);
                        }
                        if (!usedPoint.equals("")) {
                            customerBUS.decreasePoint(phone, Integer.parseInt(usedPoint));
                        }

                        sellBUS.resetBillDetailList();
                        sellBUS.renderToTableBillSell(sellBillModel);
                        resetSellData();
                        resetBillData();
                        productBUS.renderToSellTable(productModel, null);
                    }
                }
            }
        });

        btnExchangePoint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!sellBUS.checkCustomerPhone(txtCustomerPhone.getText())) {
                    return;
                }

                if (txtCustomerPoint.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập điểm cần quy đổi", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!Validate.isValidNumber(txtCustomerPoint.getText(), "Điểm tích lũy")) {
                    return;
                }

                int point = Integer.parseInt(txtCustomerPoint.getText());
                if (point % 10 != 0) {
                    JOptionPane.showMessageDialog(null, "Điểm phải là số chia hết cho 10 (10, 1000, 2000...)", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (point > 5000) {
                    JOptionPane.showMessageDialog(null, "Chỉ được quy đổi tối đa 5000 điểm", "Lỗi", JOptionPane.ERROR_MESSAGE);
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

                    long discount = calculateDiscount(point);
                    txtDiscount.setText(discount + "");
                    txtTotal.setText(total + "");
                    txtFinalTotal.setText(total - discount + "");
                }
            }
        });

        btnViewCustomerInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phone = txtCustomerPhone.getText();
                sellBUS.showInfoCustomer(phone);
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

        tblBills.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    int rowSelected = tblBills.getSelectedRow();

                    if (rowSelected >= 0) {
                        String billId = tblBills.getValueAt(rowSelected, 0).toString();
                        BillDetailGUI billDetailGUI = new BillDetailGUI(billId);
                        billDetailGUI.openBillDetailGUI();
                    }
                }
            }
        });

        btnPrintBill.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblBills.getSelectedRow();

                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn hóa đơn muốn in", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String billId = tblBills.getValueAt(selectedRow, 0).toString();
                JFileChooser fileChooser = new JFileChooser();

                String defaultFileName = billId + ".pdf";
                fileChooser.setSelectedFile(new File(defaultFileName));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files (.pdf)", "pdf");
                fileChooser.setFileFilter(filter);

                int res = fileChooser.showSaveDialog(null);

                if (res == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getPath();
                    billDetailBUS.printBill(billId, path);
                }
            }
        });

        btnAddCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddCustomerGUI addCustomerGUI = new AddCustomerGUI();
                addCustomerGUI.openAddCustomerGUI();
            }
        });
    }

    public void resetBillData() {
        cbxBillSearchType.setSelectedIndex(0);
        txtSearchBill.setText("");
        cbxBillPrice.setSelectedIndex(0);
        billDateFrom.setDate(null);
        billDateTo.setDate(null);
        initBillTableData();
        if (billSorter != null) {
            billSorter.setRowFilter(null);
        }
    }

    public void resetSellData() {
        txtProdQuantity.setText("");
        txtCustomerPhone.setText("");
        txtCustomerPoint.setText("");
        txtTotal.setText("");
        txtDiscount.setText("");
        txtFinalTotal.setText("");
        initProductTableData();
        if (productSorter != null) {
            productSorter.setRowFilter(null);
        }
    }

    public int calculateBonusPoint(long totalPrice) {
        return (int) (totalPrice * 100 / 10000000);
    }

    public long calculateDiscount(long point) {
        return (long) ((point * 1.0 / 1000) * 1000000);
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
            Date dateFrom = billDateFrom.getDate() == null
                    ? formatter.parse("01-01-1970")
                    : formatter.parse(formatter.format(billDateFrom.getDate()));

            Date dateTo = billDateTo.getDate() == null
                    ? formatter.parse("31-12-2050")
                    : formatter.parse(formatter.format(billDateTo.getDate()));

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

        String[] cols = {"Mã hóa đơn", "Mã khách hàng", "Mã nhân viên", "Ngày lập", "Tạm tính", "Giảm giá", "Tổng tiền"};
        billModel.setColumnIdentifiers(cols);
        tblBills.setModel(billModel);
        tblBills.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));
        tblBills.getTableHeader().setBackground(new Color(86, 132, 242));
        tblBills.getTableHeader().setForeground(Color.WHITE);

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
        tblSellBills.getTableHeader().setBackground(new Color(86, 132, 242));
        tblSellBills.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblSellBills.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void initProductTableData() {
        productBUS.renderToSellTable(productModel, null);
    }

    public void initProductTable() {
        productModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã sản phẩm", "Tên sản phẩm", "Hãng", "Giá", "CTKM áp dụng", "Số lượng còn"};
        productModel.setColumnIdentifiers(cols);
        tblProducts.setModel(productModel);
        tblProducts.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));
        tblProducts.getTableHeader().setBackground(new Color(86, 132, 242));
        tblProducts.getTableHeader().setForeground(Color.WHITE);

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
    private JButton btnAddBill;
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
    private JButton btnAddCustomer;
    private JDateChooser billDateFrom;
    private JDateChooser billDateTo;
}
