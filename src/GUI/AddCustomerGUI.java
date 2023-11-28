package GUI;

import BUS.CustomerBUS;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCustomerGUI {
    private CustomerBUS customerBUS;

    public AddCustomerGUI() {
        customerBUS = new CustomerBUS();
        initDateChooser();
        setNewCustomerId();

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtCustomerId.getText();
                String name = txtCustomerName.getText();
                String address = txtCustomerAddress.getText();
                String phone = txtCustomerPhone.getText();
                String dob = ((JTextField) customerDOB.getDateEditor().getUiComponent()).getText();
                String gender = cbxCustomerGender.getSelectedItem().toString();

                if (customerBUS.addCustomer(id, name, address, phone, dob, gender)) {
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                    frame.dispose();
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                frame.dispose();
            }
        });
    }

    public void setNewCustomerId() {
        String id = customerBUS.createNewId();
        txtCustomerId.setText(id);
    }

    public void initDateChooser() {
        customerDOB = new JDateChooser();
        customerDOB.setDateFormatString("dd-MM-yyyy");
        datePanel.add(customerDOB);
    }

    public void openAddCustomerGUI() {
        JFrame frame = new JFrame("AddCustomerGUI");
        frame.setContentPane(new AddCustomerGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTextField txtCustomerId;
    private JTextField txtCustomerName;
    private JTextField txtCustomerPhone;
    private JTextField txtCustomerAddress;
    private JComboBox cbxCustomerGender;
    private JButton btnAdd;
    private JButton btnCancel;
    private JPanel datePanel;
    private JDateChooser customerDOB;
}
