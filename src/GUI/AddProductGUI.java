package GUI;

import BUS.ExportDetailBUS;
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
    private ExportDetailGUI exportDetailGUI;
    private JTextField txtProductId;
    private ProductBUS productBUS;
    private ImportDetailBUS importDetailBUS;
    private ExportDetailBUS exportDetailBUS;

    public AddProductGUI(String invoiceId, ImportDetailGUI importDetailGUI, ExportDetailGUI exportDetailGUI) {
        this.invoiceId = invoiceId;
        this.importDetailGUI = importDetailGUI;
        this.exportDetailGUI = exportDetailGUI;
        productBUS = new ProductBUS();
        importDetailBUS = new ImportDetailBUS();
        exportDetailBUS = new ExportDetailBUS();

        if (importDetailGUI != null) {
            txtProductId = AutoSuggestComboBox.createAutoSuggest(cbxProductId, 0, productBUS::initProductIdSuggestion);
        } else if (exportDetailGUI != null) {
            txtProductId = AutoSuggestComboBox.createAutoSuggest(cbxProductId, 0, productBUS::initStorageProductIdSuggestion);
        }
        setComponent();

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

                // add importDetail
                if (importDetailGUI != null && importDetailBUS.addImportDetail(invoiceId, productId, quantity)) {
                    importDetailGUI.initTableData();
                    importDetailGUI.setTotalPrice();
                    closeAddProductGUI();
                }

                // add exportDetail
                if (exportDetailGUI != null && exportDetailBUS.addExportDetail(invoiceId, productId, quantity)) {
                    exportDetailGUI.initTableData();
                    exportDetailGUI.setTotalQuantity();
                    closeAddProductGUI();
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeAddProductGUI();
            }
        });
    }

    public void setComponent() {
        if (exportDetailGUI != null) {
            lblProductPrice.setVisible(false);
            txtProductPrice.setVisible(false);
        }
    }

    public void openAddProductGUI() {
        JFrame frame = new JFrame("Thêm sản phẩm");
        frame.setContentPane(new AddProductGUI(invoiceId, importDetailGUI, exportDetailGUI).mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void closeAddProductGUI() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        frame.dispose();
    }

    private JPanel mainPanel;
    private JTextField txtProductName;
    private JTextField txtProductQuantity;
    private JTextField txtProductPrice;
    private JButton btnAdd;
    private JButton btnCancel;
    private JComboBox cbxProductId;
    private JLabel lblProductPrice;
}
