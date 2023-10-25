package GUI;

import BUS.SaleBUS;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SaleGUI {
    private DefaultTableModel model;
    private SaleBUS saleBUS;

    public SaleGUI() {
        saleBUS = new SaleBUS();
        initTable();
        initTableData();
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

        String[] cols = {"Mã CTKM", "Thông tin khuyến mãi"};
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
    private JButton btnStopApply;
    private JPanel buttonPanel;
    private JTextField txtSaleId;
    private JTextField txtSaleInfo;
    private JButton btnCreateNewId;
}
