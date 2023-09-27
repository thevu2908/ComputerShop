package GUI;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;

public class LoginGUI {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        JFrame frame = new JFrame("LoginGUI");
        frame.setContentPane(new LoginGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JButton đăngNhậpButton;
    private JTextField textField1;
    private JTextField textField2;
}
