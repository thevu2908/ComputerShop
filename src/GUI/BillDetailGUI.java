package GUI;

import BUS.BillBUS;
import BUS.BillDetailBUS;
import DTO.BillDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BillDetailGUI {
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private String billId;
    private BillBUS billBUS;
    private BillDetailBUS billDetailBUS;

    public BillDetailGUI(String billId) {
        this.billId = billId;
        billBUS = new BillBUS();
        billDetailBUS = new BillDetailBUS();
        initTable();
        initTableData();
        setTotalPrice();

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
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
        cbxSearchType.setSelectedIndex(0);
        txtSearch.setText("");
        sorter.setRowFilter(null);
        if (sorter != null) {
            sorter.setRowFilter(null);
        }
    }

    public void filter() {
        String searchType = cbxSearchType.getSelectedItem().toString();
        String searchInfo = txtSearch.getText().toLowerCase();

        sorter = new TableRowSorter<>(model);
        tblProducts.setRowSorter(sorter);

        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                String rowId = entry.getStringValue(0).toLowerCase();
                String rowName = entry.getStringValue(1).toLowerCase();

                switch (searchType) {
                    case "Mã sản phẩm":
                        return rowId.contains(searchInfo);
                    case "Tên sản phẩm":
                        return rowName.contains(searchInfo);
                    default:
                        return true;
                }
            }
        };

        sorter.setRowFilter(filter);
    }

    public void setTotalPrice() {
        BillDTO bill = billBUS.getBillById(billId);
        if (bill != null) {
            txtTotalPrice.setText(bill.getTotal() - bill.getDiscount() + "");
        }
    }

    public void initTableData() {
        billDetailBUS.renderToTable(model, billId);
    }

    public void initTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã SP", "Tên SP", "Đơn giá", "Số lượng", "Thành tiền"};
        model.setColumnIdentifiers(cols);
        tblProducts.setModel(model);
        tblProducts.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));
        tblProducts.getTableHeader().setBackground(new Color(86, 132, 242));
        tblProducts.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblProducts.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void openBillDetailGUI() {
        JFrame frame = new JFrame("Chi tiết hóa đơn");
        frame.setContentPane(new BillDetailGUI(billId).mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTable tblProducts;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JButton btnReset;
    private JTextField txtTotalPrice;
}