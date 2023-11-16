package GUI;

import BUS.EmployeeBUS;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SystemGUI {
    private StatisticsGUI statisticsGUI;
    private EmployeeTypeGUI accessGUI;
    private EmployeeGUI employeeGUI;
    private SaleGUI saleGUI;
    private ProductGUI productGUI;
    private BillGUI billGUI;
    private CustomerGUI customerGUI;
    private StorageGUI storageGUI;
    private SupplierGUI supplierGUI;
    private EmployeeBUS employeeBUS;
    private String employeeId;
    private String employeeType;

    public SystemGUI(String employeeId, String employeeType) {
        employeeBUS = new EmployeeBUS();
        this.employeeId = employeeId;
        this.employeeType = employeeType;
        setCursor();
        setHover();
        setCurrentDate();
        setName();
        setMargin();
        intiContentPanel();
        showContentListener();
        giveAccess();

        lblLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!employeeType.equals("LNV03")) {
                    int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn đăng xuất ?", "Xác nhận",
                            JOptionPane.YES_NO_OPTION);

                    if (choice == JOptionPane.YES_OPTION) {
                        LoginGUI loginGUI = new LoginGUI();
                        loginGUI.openLoginGUI();
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                        frame.dispose();
                    }
                } else {
                    SellGUI sellGUI = new SellGUI(employeeId);
                    sellGUI.openSellGUI();
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                    frame.dispose();
                }
            }
        });
    }

    public void giveAccess() {
        CardLayout card = (CardLayout) contentPanel.getLayout();
        ArrayList<JPanel> panels = new ArrayList<>();
        List<JPanel> list;
        if (employeeType.equals("LNV01")) {
            list = Arrays.asList(statisticsPanel, salePanel, productPanel, billPanel, customerPanel,
                    storagePanel, supplierPanel);
            panels.addAll(list);
            hidePanel(panels);
            card.show(contentPanel, "Access");
        } else if (employeeType.equals("LNV02")) {
            list = Arrays.asList(accessPanel, employeePanel);
            panels.addAll(list);
            hidePanel(panels);
            card.show(contentPanel, "Statistics");
        } else if (employeeType.equals("LNV03")) {
            lblLogout.setText("Về trang bán hàng");
            list = Arrays.asList(statisticsPanel, accessPanel, employeePanel, salePanel,
                    productPanel, storagePanel, supplierPanel, billPanel);
            panels.addAll(list);
            hidePanel(panels);
            card.show(contentPanel, "Customer");
        } else if (employeeType.equals("LNV04")) {
            list = Arrays.asList(statisticsPanel, accessPanel, employeePanel, salePanel, productPanel, billPanel, customerPanel,
                    supplierPanel);
            panels.addAll(list);
            hidePanel(panels);
            card.show(contentPanel, "Storage");
        }
    }

    public void hidePanel(ArrayList<JPanel> panels) {
        for (JPanel panel : panels) {
            panel.setVisible(false);
        }
    }

    public void showContentListener() {
        MouseAdapter showContent = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout card = (CardLayout) contentPanel.getLayout();
                if (e.getSource() == lblStatistics) {
                    card.show(contentPanel, "Statistics");
                } else if (e.getSource() == lblAccess) {
                    accessGUI.initTableData();
                    card.show(contentPanel, "Access");
                } else if (e.getSource() == lblEmployee) {
                    employeeGUI.initComboBoxData();
                    employeeGUI.initTableData();
                    card.show(contentPanel, "Employee");
                } else if (e.getSource() == lblSale) {
                    saleGUI.initTableData();
                    card.show(contentPanel, "Sale");
                } else if (e.getSource() == lblProduct) {
                    productGUI.initComboBoxTypeData();
                    productGUI.initTableData();
                    card.show(contentPanel, "Product");
                } else if (e.getSource() == lblBill) {
                    card.show(contentPanel, "Bill");
                } else if (e.getSource() == lblCustomer) {
                    customerGUI.initTableData();
                    card.show(contentPanel, "Customer");
                } else if (e.getSource() == lblStorage) {
                    storageGUI.initProductTypeComboBoxData();
                    storageGUI.initProductTableData();
                    storageGUI.initExportTableData();
                    storageGUI.initImportTableData();
                    card.show(contentPanel, "Storage");
                } else if (e.getSource() == lblSupplier) {
                    card.show(contentPanel, "Supplier");
                }
            }
        };
        lblStatistics.addMouseListener(showContent);
        lblAccess.addMouseListener(showContent);
        lblEmployee.addMouseListener(showContent);
        lblSale.addMouseListener(showContent);
        lblProduct.addMouseListener(showContent);
        lblBill.addMouseListener(showContent);
        lblCustomer.addMouseListener(showContent);
        lblStorage.addMouseListener(showContent);
        lblSupplier.addMouseListener(showContent);
    }

    public void intiContentPanel() {
        statisticsGUI = new StatisticsGUI();
        accessGUI = new EmployeeTypeGUI();
        employeeGUI = new EmployeeGUI();
        saleGUI = new SaleGUI();
        productGUI = new ProductGUI();
        billGUI = new BillGUI();
        customerGUI = new CustomerGUI();
        storageGUI = new StorageGUI(employeeId);
        supplierGUI = new SupplierGUI();

        contentPanel.add("Statistics", statisticsGUI.getMainPanel());
        contentPanel.add("Access", accessGUI.getMainPanel());
        contentPanel.add("Employee", employeeGUI.getMainPanel());
        contentPanel.add("Sale", saleGUI.getMainPanel());
        contentPanel.add("Product", productGUI.getMainPanel());
        contentPanel.add("Bill", billGUI.getMainPanel());
        contentPanel.add("Customer", customerGUI.getMainPanel());
        contentPanel.add("Storage", storageGUI.getMainPanel());
        contentPanel.add("Supplier", supplierGUI.getMainPanel());
    }

    public void setName() {
        String name = employeeBUS.getNameById(employeeId);
        lblUserName.setText(name);
    }

    public void setMargin() {
        Border border = BorderFactory.createEmptyBorder(12, 14, 12, 14);
        lblStatistics.setBorder(border);
        lblAccess.setBorder(border);
        lblEmployee.setBorder(border);
        lblSale.setBorder(border);
        lblProduct.setBorder(border);
        lblBill.setBorder(border);
        lblCustomer.setBorder(border);
        lblStorage.setBorder(border);
        lblSupplier.setBorder(border);
    }

    public void setCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String[] arr = dtf.format(now).split("/");
        lblCurrentDate.setText("Ngày " + arr[2] + " tháng " + arr[1] + ", " + arr[0]);
    }

    public void setHover() {
        MouseAdapter hover = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (e.getSource() == lblStatistics) {
                    lblStatistics.setBackground(new Color(86, 132, 242));
                    lblStatistics.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblAccess) {
                    lblAccess.setBackground(new Color(86, 132, 242));
                    lblAccess.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblEmployee) {
                    lblEmployee.setBackground(new Color(86, 132, 242));
                    lblEmployee.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblSale) {
                    lblSale.setBackground(new Color(86, 132, 242));
                    lblSale.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblProduct) {
                    lblProduct.setBackground(new Color(86, 132, 242));
                    lblProduct.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblBill) {
                    lblBill.setBackground(new Color(86, 132, 242));
                    lblBill.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblCustomer) {
                    lblCustomer.setBackground(new Color(86, 132, 242));
                    lblCustomer.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblStorage) {
                    lblStorage.setBackground(new Color(86, 132, 242));
                    lblStorage.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblSupplier) {
                    lblSupplier.setBackground(new Color(86, 132, 242));
                    lblSupplier.setForeground(new Color(255, 255, 255));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (e.getSource() == lblStatistics) {
                    lblStatistics.setBackground(new Color(255, 255, 255));
                    lblStatistics.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblAccess) {
                    lblAccess.setBackground(new Color(255, 255, 255));
                    lblAccess.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblEmployee) {
                    lblEmployee.setBackground(new Color(255, 255, 255));
                    lblEmployee.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblSale) {
                    lblSale.setBackground(new Color(255, 255, 255));
                    lblSale.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblProduct) {
                    lblProduct.setBackground(new Color(255, 255, 255));
                    lblProduct.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblBill) {
                    lblBill.setBackground(new Color(255, 255, 255));
                    lblBill.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblCustomer) {
                    lblCustomer.setBackground(new Color(255, 255, 255));
                    lblCustomer.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblStorage) {
                    lblStorage.setBackground(new Color(255, 255, 255));
                    lblStorage.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblSupplier) {
                    lblSupplier.setBackground(new Color(255, 255, 255));
                    lblSupplier.setForeground(new Color(0, 0, 0));
                }
            }
        };
        lblStatistics.addMouseListener(hover);
        lblAccess.addMouseListener(hover);
        lblEmployee.addMouseListener(hover);
        lblSale.addMouseListener(hover);
        lblProduct.addMouseListener(hover);
        lblBill.addMouseListener(hover);
        lblCustomer.addMouseListener(hover);
        lblStorage.addMouseListener(hover);
        lblSupplier.addMouseListener(hover);
        lblLogout.addMouseListener(hover);
    }

    public void setCursor() {
        lblStatistics.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblAccess.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblEmployee.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblSale.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblProduct.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblBill.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblCustomer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblStorage.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblSupplier.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void openSystemGUI() {
        JFrame frame = new JFrame("Quản trị hệ thống");
        frame.setContentPane(new SystemGUI(employeeId, employeeType).mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel taskbarPanel;
    private JLabel lblUserName;
    private JLabel lblStatistics;
    private JLabel lblAccess;
    private JLabel lblEmployee;
    private JLabel lblProduct;
    private JLabel lblBill;
    private JLabel lblCustomer;
    private JLabel lblStorage;
    private JLabel lblSupplier;
    private JLabel lblLogout;
    private JPanel statisticsPanel;
    private JPanel accessPanel;
    private JPanel employeePanel;
    private JPanel productPanel;
    private JPanel billPanel;
    private JPanel customerPanel;
    private JPanel storagePanel;
    private JPanel supplierPanel;
    private JLabel lblCurrentDate;
    private JPanel salePanel;
    private JLabel lblSale;
}
