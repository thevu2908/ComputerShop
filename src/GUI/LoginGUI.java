package GUI;

import BUS.EmployeeBUS;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginGUI {
    private EmployeeBUS employeeBUS;

    public LoginGUI() {
        employeeBUS = new EmployeeBUS();

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        chkShowPassword.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (chkShowPassword.isSelected()) {
                    txtPassword.setEchoChar((char)0);
                } else {
                    txtPassword.setEchoChar('•');
                }
            }
        });

        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnLogin.doClick();
                }
            }
        });
    }

    public void login() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (employeeBUS.login(username, password)) {
            String employeeId = employeeBUS.getIdByEmail(username);
            String employeeType = employeeBUS.getTypeByEmail(username);

            if (employeeType.equals("LNV03")) {
                SellGUI sellGUI = new SellGUI(employeeId);
                sellGUI.openSellGUI();
            } else {
                SystemGUI systemGUI = new SystemGUI(employeeId, employeeType);
                systemGUI.openSystemGUI();
            }

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.dispose();
        }
    }

    public void openLoginGUI() {
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
    private JPasswordField txtPassword;
    private JCheckBox chkShowPassword;
}
