package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ExportDetailGUI {
    private DefaultTableModel model;

    public ExportDetailGUI() {
        initTable();
    }

    public void initTable() {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã sản phẩm", "Tên sản phẩm", "Số lượng"};
        model.setColumnIdentifiers(cols);
        tblExportDetails.setModel(model);
        tblExportDetails.getTableHeader().setFont(new Font("Time News Roman", Font.PLAIN, 16));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblExportDetails.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ExportDetailGUI");
        frame.setContentPane(new ExportDetailGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTable tblExportDetails;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JComboBox comboBox2;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnReset;
    private JPanel cbxQuantity;
}
