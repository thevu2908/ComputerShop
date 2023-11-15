package GUI;

import BUS.CustomerBUS;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;

public class CustomerGUI {
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private CustomerBUS customerBUS;

    public CustomerGUI() {
        customerBUS = new CustomerBUS();
        initDateChooser();
        initTable();
        initTableData();

        btnCreateNewId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = customerBUS.createNewId();
                txtCustomerId.setText(id);
            }
        });

        tblCustomers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int selectedRow = tblCustomers.getSelectedRow();

                if (selectedRow >= 0) {
                    String id = tblCustomers.getValueAt(selectedRow, 0).toString();
                    String name = tblCustomers.getValueAt(selectedRow, 1).toString();
                    String phone = tblCustomers.getValueAt(selectedRow, 2).toString();
                    String address = customerBUS.getCustomerById(id).getCustomerAddress();
                    String dob = customerBUS.getCustomerById(id).getCustomerDOB();
                    String gender = tblCustomers.getValueAt(selectedRow, 3).toString();
                    String point = tblCustomers.getValueAt(selectedRow, 4).toString();

                    try {
                        txtCustomerId.setText(id);
                        txtCustomerName.setText(name);
                        txtCustomerPhone.setText(phone);
                        txtCustomerAddress.setText(address);
                        customerDOB.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(dob));
                        cbxGender.setSelectedItem(gender);
                        txtCustomerPoint.setText(point);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtCustomerId.getText();
                String name = txtCustomerName.getText();
                String address = txtCustomerAddress.getText();
                String phone = txtCustomerPhone.getText();
                String dob = ((JTextField) customerDOB.getDateEditor().getUiComponent()).getText();
                String gender = cbxGender.getSelectedItem().toString();

                if (customerBUS.addCustomer(id, name, address, phone, dob, gender)) {
                    resetData();
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtCustomerId.getText();
                String name = txtCustomerName.getText();
                String address = txtCustomerAddress.getText();
                String phone = txtCustomerPhone.getText();
                String dob = ((JTextField) customerDOB.getDateEditor().getUiComponent()).getText();
                String gender = cbxGender.getSelectedItem().toString();

                if (customerBUS.updateCustomer(id, name, address, phone, dob, gender)) {
                    resetData();
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblCustomers.getSelectedRow();

                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn khách hàng muốn xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn xóa khách hàng này ?", "Hỏi",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    String id = txtCustomerId.getText();
                    if (customerBUS.deleteCustomer(id)) {
                        resetData();
                    }
                }
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetData();
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                filter();
            }
        });

        cbxFilterGender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });

        cbxFilterPoint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });

        btnExportExcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                String defaultFileName = "dskh.xlsx";
                fileChooser.setSelectedFile(new File(defaultFileName));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel (.xlsx)", "xlsx");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showSaveDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fileChooser.getSelectedFile();
                        customerBUS.exportExcel(file);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void filter() {
        String searchType = cbxSearchType.getSelectedItem().toString();
        String customerInfo = txtSearch.getText().toLowerCase();

        String customerGender = cbxFilterGender.getSelectedItem() == null
                || cbxFilterGender.getSelectedItem().toString().equals("Tất cả")
                ? ""
                : cbxFilterGender.getSelectedItem().toString().toLowerCase();

        String customerPoint = cbxFilterPoint.getSelectedItem().toString();
        int minPoint = 0;
        int maxPoint = 999999999;

        if (customerPoint.equals("Dưới 2000")) {
            maxPoint = 2000 - 1;
        } else if (customerPoint.equals("Từ 2000 tới 5000")) {
            minPoint = 2000;
            maxPoint = 5000;
        } else if (customerPoint.equals("Trên 5000")) {
            minPoint = 5000 + 1;
        }

        sorter = new TableRowSorter<>(model);
        tblCustomers.setRowSorter(sorter);

        int finalMinPoint = minPoint;
        int finalMaxPoint = maxPoint;

        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                String rowId = entry.getStringValue(0).toLowerCase();
                String rowName = entry.getStringValue(1).toLowerCase();
                String rowPhone = entry.getStringValue(2).toLowerCase();
                String rowGender = entry.getStringValue(3).toLowerCase();
                int rowPoint = Integer.parseInt(entry.getStringValue(4));

                switch (searchType) {
                    case "Mã khách hàng":
                        return rowId.contains(customerInfo) && rowGender.contains(customerGender)
                                && rowPoint >= finalMinPoint && rowPoint <= finalMaxPoint;
                    case "Tên khách hàng":
                        return rowName.contains(customerInfo) && rowGender.contains(customerGender)
                                && rowPoint >= finalMinPoint && rowPoint <= finalMaxPoint;
                    case "Số điện thoại":
                        return rowPhone.contains(customerInfo) && rowGender.contains(customerGender)
                                && rowPoint >= finalMinPoint && rowPoint <= finalMaxPoint;
                    default:
                        return true;
                }
            }
        };

        sorter.setRowFilter(filter);
    }

    public void resetData() {
        cbxSearchType.setSelectedIndex(0);
        txtSearch.setText("");
        cbxFilterGender.setSelectedIndex(0);
        cbxFilterPoint.setSelectedIndex(0);
        txtCustomerId.setText("");
        txtCustomerName.setText("");
        txtCustomerAddress.setText("");
        txtCustomerPhone.setText("");
        customerDOB.setDate(null);
        cbxGender.setSelectedIndex(0);
        txtCustomerPoint.setText("");
        initTableData();
        if (sorter != null) {
            sorter.setRowFilter(null);
        }
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
        tblCustomers.getTableHeader().setBackground(new Color(86, 132, 242));
        tblCustomers.getTableHeader().setForeground(Color.WHITE);

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
    private JTextField txtCustomerPoint;
    private JButton btnExportExcel;
    private JDateChooser customerDOB;
}