package GUI;

import BUS.SaleBUS;
import BUS.StopApplySale;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Timer;

public class SaleGUI {
    private DefaultTableModel model;
    private SaleBUS saleBUS;

    public SaleGUI() {
        saleBUS = new SaleBUS();
        initDateChooser();
        initTable();
        initTableData();
        stopApplySale();
    }

    public void stopApplySale() {
        Timer timer = new Timer();
        timer.schedule(new StopApplySale(), 0, 3600000);
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

        String[] cols = {"Mã CTKM", "Thông tin KM", "Ngày bắt đầu", "Ngày kết thúc", "Tình trạng"};
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
        startDate = new JDateChooser();
        startDate.setDateFormatString("dd-MM-yyyy");
        startDate.setPreferredSize(new Dimension(-1, 30));
        startDatePanel.add(startDate);

        endDate = new JDateChooser();
        endDate.setDateFormatString("dd-MM-yyyy");
        endDate.setPreferredSize(new Dimension(-1, 30));
        endDatePanel.add(endDate);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;
    private JTable tblSales;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnApply;
    private JButton btnDelete;
    private JButton btnReset;
    private JPanel buttonPanel;
    private JTextField txtSaleId;
    private JTextField txtSaleInfo;
    private JButton btnCreateNewId;
    private JTextField textField1;
    private JPanel startDatePanel;
    private JPanel endDatePanel;
    private JDateChooser startDate;
    private JDateChooser endDate;
}