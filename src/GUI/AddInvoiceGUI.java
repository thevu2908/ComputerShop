package GUI;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;

public class AddInvoiceGUI {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        JFrame frame = new JFrame("AddInvoiceGUI");
        frame.setContentPane(new AddInvoiceGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTextField txtInvoiceId;
    private JTextField txtEmployeeId;
    private JTextField txtSupplierid;
    private JTextField txtSupplierName;
    private JButton btnSave;
    private JButton btnCancel;
    private JLabel lblTitle;
    private JLabel lblInvoiceId;
    private JLabel lblEmployeeId;
    private JLabel lblSupplierId;
    private JLabel lblSupplierName;
}
