package GUI;

import BUS.ImportDetailBUS;
import BUS.ProductBUS;
import utils.AutoSuggestComboBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AddProductGUI {
    private String invoiceId;
    private ImportDetailGUI importDetailGUI;
    private JTextField txtProductId;
    private ProductBUS productBUS;
    private ImportDetailBUS importDetailBUS;

    public AddProductGUI(String invoiceId, ImportDetailGUI importDetailGUI) {
        this.invoiceId = invoiceId;
        this.importDetailGUI = importDetailGUI;
        productBUS = new ProductBUS();
        importDetailBUS = new ImportDetailBUS();

        txtProductId = AutoSuggestComboBox.createAutoSuggest(cbxProductId, 0, productBUS::initProductIdSuggestion);

        cbxProductId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = productBUS.getNameById(cbxProductId.getSelectedItem().toString());
                int price = productBUS.getPriceById(cbxProductId.getSelectedItem().toString());
                txtProductName.setText(name);
                txtProductPrice.setText(price + "");
            }
        });

        txtProductId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String name = productBUS.getNameById(txtProductId.getText());
                int price = productBUS.getPriceById(txtProductId.getText());
                txtProductName.setText(name);
                txtProductPrice.setText(price + "");
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productId = txtProductId.getText();
                String quantity = txtProductQuantity.getText();

                if (importDetailBUS.addImportDetail(invoiceId, productId, quantity)) {
                    importDetailGUI.initTableData();
                    importDetailGUI.setTotalPrice();
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

    public void openAddProductGUI() {
        JFrame frame = new JFrame("Thêm sản phẩm");
        frame.setContentPane(new AddProductGUI(invoiceId, importDetailGUI).mainPanel);
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
