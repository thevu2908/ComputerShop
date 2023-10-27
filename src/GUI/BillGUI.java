package GUI;

import BUS.BillBUS;
import BUS.BillDetailBUS;
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
import java.util.Date;

public class BillGUI {
    private DefaultTableModel billModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private BillBUS billBUS;
    private BillDetailBUS billDetailBUS;

    public BillGUI() {
        billBUS = new BillBUS();
        billDetailBUS = new BillDetailBUS();
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

        btnPrint.addActionListener(new ActionListener() {
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
    }

    public void resetData() {
        cbxBillSearchType.setSelectedIndex(0);
        txtSearchBill.setText("");
        cbxFilterBillPrice.setSelectedIndex(0);
        billDateFrom.setDate(null);
        billDateTo.setDate(null);
        initBillTableData();
        if (sorter != null) {
            sorter.setRowFilter(null);
        }
    }

    public void filter() {
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
    private JButton btnPrint;
    private JDateChooser billDateFrom;
    private JDateChooser billDateTo;
}
