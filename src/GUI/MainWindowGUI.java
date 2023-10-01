package GUI;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainWindowGUI {
    private StatisticsGUI statisticsGUI;
    private AccessGUI accessGUI;
    private EmployeeGUI employeeGUI;
    private ProductGUI productGUI;
    private StorageGUI storageGUI;
    private SupplierGUI supplierGUI;

    public MainWindowGUI() {
        setCursor();
        setHover();
        setCurrentDate();
        intiContentPanel();

        MouseAdapter showContent = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout card = (CardLayout) contentPanel.getLayout();

                if (e.getSource() == lblStatistics) {
                    card.show(contentPanel, "Statistics");
                } else if (e.getSource() == lblAccess) {
                    card.show(contentPanel, "Access");
                } else if (e.getSource() == lblEmployee) {
                    card.show(contentPanel, "Employee");
                } else if (e.getSource() == lblProduct) {
                    card.show(contentPanel, "Product");
                } else if (e.getSource() == lblOrder) {
                    card.show(contentPanel, "Order");
                } else if (e.getSource() == lblCustomer) {
                    card.show(contentPanel, "Customer");
                } else if (e.getSource() == lblStorage) {
                    card.show(contentPanel, "Storage");
                } else if (e.getSource() == lblSupplier) {
                    card.show(contentPanel, "Supplier");
                }
            }
        };
        lblStatistics.addMouseListener(showContent);
        lblAccess.addMouseListener(showContent);
        lblEmployee.addMouseListener(showContent);
        lblProduct.addMouseListener(showContent);
        lblOrder.addMouseListener(showContent);
        lblCustomer.addMouseListener(showContent);
        lblStorage.addMouseListener(showContent);
        lblSupplier.addMouseListener(showContent);
    }

    public void intiContentPanel() {
        statisticsGUI = new StatisticsGUI();
        accessGUI = new AccessGUI();
        employeeGUI = new EmployeeGUI();
        productGUI = new ProductGUI();
        storageGUI = new StorageGUI();
        supplierGUI = new SupplierGUI();

        contentPanel.add("Statistics", statisticsGUI.getMainPanel());
        contentPanel.add("Access", accessGUI.getMainPanel());
        contentPanel.add("Employee", employeeGUI.getMainPanel());
        contentPanel.add("Product", productGUI.getMainPanel());
        contentPanel.add("Storage", storageGUI.getMainPanel());
        contentPanel.add("Supplier", supplierGUI.getMainPanel());
    }

    public void setCurrentContent(JPanel panel, JLabel label) {
        for (Component component : contentPanel.getComponents()) {
            if (component.isVisible()) {
                setColor(panel, label);
                break;
            }
        }
    }

    public void setColor(JPanel panel, JLabel label) {
        panel.setBackground(new Color(86, 132, 242));
        label.setForeground(new Color(255, 255, 255));
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
                    statisticsPanel.setBackground(new Color(86, 132, 242));
                    lblStatistics.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblAccess) {
                    accessPanel.setBackground(new Color(86, 132, 242));
                    lblAccess.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblEmployee) {
                    employeePanel.setBackground(new Color(86, 132, 242));
                    lblEmployee.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblProduct) {
                    productPanel.setBackground(new Color(86, 132, 242));
                    lblProduct.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblOrder) {
                    orderPanel.setBackground(new Color(86, 132, 242));
                    lblOrder.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblCustomer) {
                    customerPanel.setBackground(new Color(86, 132, 242));
                    lblCustomer.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblStorage) {
                    storagePanel.setBackground(new Color(86, 132, 242));
                    lblStorage.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblSupplier) {
                    supplierPanel.setBackground(new Color(86, 132, 242));
                    lblSupplier.setForeground(new Color(255, 255, 255));
                } else if (e.getSource() == lblLogout) {
                    lblLogout.setForeground(new Color(255, 255, 255));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (e.getSource() == lblStatistics) {
                    statisticsPanel.setBackground(new Color(255, 255, 255));
                    lblStatistics.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblAccess) {
                    accessPanel.setBackground(new Color(255, 255, 255));
                    lblAccess.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblEmployee) {
                    employeePanel.setBackground(new Color(255, 255, 255));
                    lblEmployee.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblProduct) {
                    productPanel.setBackground(new Color(255, 255, 255));
                    lblProduct.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblOrder) {
                    orderPanel.setBackground(new Color(255, 255, 255));
                    lblOrder.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblCustomer) {
                    customerPanel.setBackground(new Color(255, 255, 255));
                    lblCustomer.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblStorage) {
                    storagePanel.setBackground(new Color(255, 255, 255));
                    lblStorage.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblSupplier) {
                    supplierPanel.setBackground(new Color(255, 255, 255));
                    lblSupplier.setForeground(new Color(0, 0, 0));
                } else if (e.getSource() == lblLogout) {
                    lblLogout.setForeground(new Color(255, 255, 255));
                }
            }
        };
        lblStatistics.addMouseListener(hover);
        lblAccess.addMouseListener(hover);
        lblEmployee.addMouseListener(hover);
        lblProduct.addMouseListener(hover);
        lblOrder.addMouseListener(hover);
        lblCustomer.addMouseListener(hover);
        lblStorage.addMouseListener(hover);
        lblSupplier.addMouseListener(hover);
        lblLogout.addMouseListener(hover);
    }

    public void setCursor() {
        lblStatistics.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblAccess.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblEmployee.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblProduct.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblCustomer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblStorage.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblSupplier.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        JFrame frame = new JFrame("Quản trị hệ thống");
        frame.setContentPane(new MainWindowGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel taskbarPanel;
    private JLabel lbUserName;
    private JLabel lblStatistics;
    private JLabel lblAccess;
    private JLabel lblEmployee;
    private JLabel lblProduct;
    private JLabel lblOrder;
    private JLabel lblCustomer;
    private JLabel lblStorage;
    private JLabel lblSupplier;
    private JLabel lblLogout;
    private JPanel statisticsPanel;
    private JPanel accessPanel;
    private JPanel employeePanel;
    private JPanel productPanel;
    private JPanel orderPanel;
    private JPanel customerPanel;
    private JPanel storagePanel;
    private JPanel supplierPanel;
    private JLabel lblCurrentDate;
    private JPanel logoutPanel;
}
