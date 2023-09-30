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
        JFrame frame = new JFrame("Đăng nhập");
        frame.setContentPane(new LoginGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JButton btnLogin;
    private JTextField txtUsername;
    private JTextField txtPassword;
}
