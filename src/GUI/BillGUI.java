package GUI;

import BUS.BillBUS;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BillGUI {
    private DefaultTableModel billModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private BillBUS billBUS;

    public BillGUI() {
        billBUS = new BillBUS();
        initBillDateChooser();
        initBillTable();
        initBillTableData();

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

        txtSearchBill.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filter();
            }
        });

        cbxFilterBillPrice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
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

                filter();
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetData();
            }
        });
    }

    public void resetData() {
        cbxBillSearchType.setSelectedIndex(0);
        txtSearchBill.setText("");
        cbxFilterBillPrice.setSelectedIndex(0);
        billDateFrom.setDate(null);
        billDateTo.setDate(null);
        sorter.setRowFilter(null);
        initBillTableData();
    }

    public void filter() {
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

            int billPrice = cbxFilterBillPrice.getSelectedIndex();
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

            sorter = new TableRowSorter<>(billModel);
            tblBills.setRowSorter(sorter);

            RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(RowFilter.Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    try {
                        String rowBillId = entry.getStringValue(0).toLowerCase();
                        String rowCustomerId = entry.getStringValue(1).toLowerCase();
                        String rowEmployeeId = entry.getStringValue(2).toLowerCase();
                        Date rowDate = formatter.parse(entry.getStringValue(3));
                        int rowPrice = Integer.parseInt(entry.getStringValue(4));

                        switch (searchType) {
                            case "Mã hóa đơn":
                                return rowBillId.contains(searchInfo)
                                        && rowDate.compareTo(dateFrom) >= 0 && rowDate.compareTo(dateTo) <= 0
                                        && rowPrice >= finalMinPrice && rowPrice <= finalMaxPrice;
                            case "Mã nhân viên":
                                return rowEmployeeId.contains(searchInfo)
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

            sorter.setRowFilter(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initBillTableData() {
        billBUS.renderToTable(billModel);
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

    public void initBillDateChooser() {
        billDateFrom = new JDateChooser();
        billDateFrom.setDateFormatString("dd-MM-yyyy");
        billDateFromPanel.add(billDateFrom);

        billDateTo = new JDateChooser();
        billDateTo.setDateFormatString("dd-MM-yyyy");
        billDateToPanel.add(billDateTo);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;
    private JComboBox cbxBillSearchType;
    private JTextField txtSearchBill;
    private JComboBox cbxFilterBillPrice;
    private JButton btnReset;
    private JPanel billDateFromPanel;
    private JPanel billDateToPanel;
    private JButton btnFilterBillDate;
    private JTable tblBills;
    private JDateChooser billDateFrom;
    private JDateChooser billDateTo;
}
