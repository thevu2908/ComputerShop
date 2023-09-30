package GUI;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


public class UserGUI {
    private DefaultTableModel userModel;
    public UserGUI() {
        initTable();
    }

    public void initTable() {
        userModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã nhân viên", "Tên nhân viên", "Loại nhân viên", "Giới tính", "Số điện thoại"};
        userModel.setColumnIdentifiers(cols);
        tblUsers.setModel(userModel);
        tblUsers.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblUsers.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        JFrame frame = new JFrame("ProductGUI");
        frame.setContentPane(new UserGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JTable tblUsers;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JComboBox comboBox2;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnReset;
    private JButton btnCreateId;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JComboBox comboBox3;
    private JTextField textField5;
    private JTextField textField6;
    private JTextField textField7;
    private JComboBox comboBox4;
    private JDateChooser JDateChooser1;
}
