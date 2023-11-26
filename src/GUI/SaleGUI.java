package GUI;

import BUS.ProductBUS;
import BUS.SaleBUS;
import BUS.AutoStopApplySaleBUS;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class SaleGUI {
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private SaleBUS saleBUS;
    private ProductBUS productBUS;

    public SaleGUI() {
        saleBUS = new SaleBUS();
        productBUS = new ProductBUS();
        initDateChooser();
        initTable();
        initTableData();
        autoStopApplySale();

        tblSales.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int selectedRow = tblSales.getSelectedRow();

                if (selectedRow >= 0) {
                    try {
                        String id = tblSales.getValueAt(selectedRow, 0).toString();
                        String info = tblSales.getValueAt(selectedRow, 1).toString();
                        String startDateStr = tblSales.getValueAt(selectedRow, 2).toString();
                        String endDateStr = tblSales.getValueAt(selectedRow, 3).toString();
                        String status = tblSales.getValueAt(selectedRow, 4).toString();

                        if (status.contains("Đang áp dụng")) {
                            status = "Đang áp dụng";
                        } else if (status.contains("Ngưng áp dụng")) {
                            status = "Ngưng áp dụng";
                        } else {
                            status = "Chưa áp dụng";
                        }

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

                        txtSaleId.setText(id);
                        txtSaleInfo.setText(info);
                        saleStartDate.setDate(sdf.parse(startDateStr));
                        saleEndDate.setDate(sdf.parse(endDateStr));
                        txtSaleStatus.setText(status);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String saleId = txtSaleId.getText();
                String saleInfo = txtSaleInfo.getText();
                String sDate = ((JTextField) saleStartDate.getDateEditor().getUiComponent()).getText();
                String eDate = ((JTextField) saleEndDate.getDateEditor().getUiComponent()).getText();
                if (saleBUS.addSale(saleId, saleInfo, sDate, eDate)) {
                    reset();
                }
            }
        });

        btnCreateNewId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newId = saleBUS.createNewId();
                txtSaleId.setText(newId);
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblSales.getSelectedRow();

                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn chương trình khuyến mãi muốn xóa", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null, "Bạn muốn xóa chương trình khuyến mãi này ?", "Hỏi",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    String saleId = tblSales.getValueAt(selectedRow, 0).toString();

                    if (saleBUS.deleteSale(saleId)) {
                        reset();
                    }
                }
            }
        });

        btnApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblSales.getSelectedRow();

                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn chương trình khuyến mãi", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null, "Bạn muốn áp dụng chương trình khuyến mãi này ?", "Hỏi",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    String saleId = tblSales.getValueAt(selectedRow, 0).toString();

                    if (productBUS.applySale(saleId)) {
                        reset();
                    }
                }
            }
        });

        btnStopApply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblSales.getSelectedRow();

                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn chương trình khuyến mãi", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int choice = JOptionPane.showConfirmDialog(null, "Bạn muốn ngưng áp dụng chương trình khuyến mãi này ?", "Hỏi",
                        JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    String saleId = tblSales.getValueAt(selectedRow, 0).toString();

                    if (productBUS.stopApplySale(saleId)) {
                        reset();
                    }
                }
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                filter();
            }
        });

        cbxFilterStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });

        btnFilterDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filter();
            }
        });
    }

    public void autoStopApplySale() {
        Timer timer = new Timer();
        timer.schedule(new AutoStopApplySaleBUS(), 0, 3600000);
    }

    public void filter() {
        try {
            String searchType = cbxSearchType.getSelectedItem().toString();
            String info = txtSearch.getText().toLowerCase();
            String status = cbxFilterStatus.getSelectedItem() == null
                    || cbxFilterStatus.getSelectedItem().toString().equals("Tất cả")
                    ? ""
                    : cbxFilterStatus.getSelectedItem().toString();

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date dateFrom = startDate.getDate() == null
                    ? formatter.parse("01-01-1970")
                    : formatter.parse(formatter.format(startDate.getDate()));

            Date dateTo = endDate.getDate() == null
                    ? formatter.parse("31-12-2050")
                    : formatter.parse(formatter.format(endDate.getDate()));

            if (dateTo.compareTo(dateFrom) < 0) {
                JOptionPane.showMessageDialog(null, "Ngày kết thúc phải sau ngày bắt đầu", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            sorter = new TableRowSorter<>(model);
            tblSales.setRowSorter(sorter);

            RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
                @Override
                public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                    try {
                        String rowId = entry.getStringValue(0).toLowerCase();
                        String rowInfo = entry.getStringValue(1).toLowerCase();
                        Date rowStartDate = formatter.parse(entry.getStringValue(2));
                        Date rowEndDate = formatter.parse(entry.getStringValue(3));
                        String rowStatus = entry.getStringValue(4);

                        switch (searchType) {
                            case "Mã khuyến mãi":
                                return rowId.contains(info) && rowStatus.contains(status)
                                        && rowStartDate.compareTo(dateFrom) >= 0 && rowEndDate.compareTo(dateTo) <= 0;
                            case "Giá khuyến mãi":
                                return rowInfo.contains(info) && rowStatus.contains(status)
                                        && rowStartDate.compareTo(dateFrom) >= 0 && rowEndDate.compareTo(dateTo) <= 0;
                            default:
                                return true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return true;
                    }
                }
            };

            sorter.setRowFilter(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        cbxSearchType.setSelectedIndex(0);
        txtSearch.setText("");
        cbxFilterStatus.setSelectedIndex(0);
        startDate.setDate(null);
        endDate.setDate(null);
        txtSaleId.setText("");
        txtSaleInfo.setText("");
        saleStartDate.setDate(null);
        saleEndDate.setDate(null);
        txtSaleStatus.setText("");
        initTableData();
        if (sorter != null) {
            sorter.setRowFilter(null);
        }
    }

    public void initTableData() {
        saleBUS.renderToTable(model);
    }

    public void initTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã CTKM", "Giá khuyến mãi", "Ngày bắt đầu", "Ngày kết thúc", "Tình trạng"};
        model.setColumnIdentifiers(cols);
        tblSales.setModel(model);
        tblSales.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));
        tblSales.getTableHeader().setBackground(new Color(86, 132, 242));
        tblSales.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblSales.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void initDateChooser() {
        saleStartDate = new JDateChooser();
        saleStartDate.setDateFormatString("dd-MM-yyyy");
        saleStartDate.setPreferredSize(new Dimension(-1, 30));
        startDatePanel.add(saleStartDate);

        saleEndDate = new JDateChooser();
        saleEndDate.setDateFormatString("dd-MM-yyyy");
        saleEndDate.setPreferredSize(new Dimension(-1, 30));
        endDatePanel.add(saleEndDate);

        startDate = new JDateChooser();
        startDate.setDateFormatString("dd-MM-yyyy");
        filterStartDatePanel.add(startDate);

        endDate = new JDateChooser();
        endDate.setDateFormatString("dd-MM-yyyy");
        filterEndDatePanel.add(endDate);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;
    private JTable tblSales;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JButton btnAdd;
    private JButton btnApply;
    private JButton btnDelete;
    private JButton btnReset;
    private JPanel buttonPanel;
    private JTextField txtSaleId;
    private JTextField txtSaleInfo;
    private JButton btnCreateNewId;
    private JTextField txtSaleStatus;
    private JPanel startDatePanel;
    private JPanel endDatePanel;
    private JButton btnStopApply;
    private JComboBox cbxFilterStatus;
    private JPanel filterDatePanel;
    private JPanel filterStartDatePanel;
    private JPanel filterEndDatePanel;
    private JButton btnFilterDate;
    private JDateChooser saleStartDate;
    private JDateChooser saleEndDate;
    private JDateChooser startDate;
    private JDateChooser endDate;
}