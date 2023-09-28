package GUI;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;

public class AddProductGUI {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        JFrame frame = new JFrame("AddProductGUI");
        frame.setContentPane(new AddProductGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTextField txtProductName;
    private JTextField txtProductQuantity;
    private JTextField txtProductPrice;
    private JButton btnAdd;
    private JButton btnCancel;
    private JComboBox cbxProductId;
}
