package GUI;

import BUS.ExportBUS;
import BUS.ImportBUS;
import BUS.SupplierBUS;
import utils.AutoSuggestComboBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AddInvoiceGUI {
    private String invoiceName;
    private String employeeId;
    private StorageGUI storageGUI;
    private ImportBUS importBUS;
    private ExportBUS exportBUS;
    private SupplierBUS supplierBUS;
    private JTextField txtSupplierId;

    public AddInvoiceGUI(String invoiceName, String employeeId, StorageGUI storageGUI) {
        this.invoiceName = invoiceName;
        this.employeeId = employeeId;
        this.storageGUI = storageGUI;
        importBUS = new ImportBUS();
        exportBUS = new ExportBUS();
        supplierBUS = new SupplierBUS();
        setComponentsInfo();

        txtSupplierId = AutoSuggestComboBox.createAutoSuggest(cbxSupplierId, 0, supplierBUS::initSupplierSuggestion);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (invoiceName.equals("phiếu nhập")) {
                    if (importBUS.addImport(txtInvoiceId.getText(), txtEmployeeId.getText(), txtSupplierId.getText())) {
                        ImportDetailGUI importDetailGUI = new ImportDetailGUI(txtInvoiceId.getText(), storageGUI);
                        importDetailGUI.openImportDetailGUI();
                        storageGUI.initImportTableData();

                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                        frame.dispose();
                    }
                } else if (invoiceName.equals("phiếu xuất")) {
                    ExportDetailGUI exportDetailGUI = new ExportDetailGUI(txtInvoiceId.getText(), storageGUI);
                    exportDetailGUI.openExportDetailGUI();
                    storageGUI.initExportTableData();

                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                    frame.dispose();
                }
            }
        });

        cbxSupplierId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = supplierBUS.getNameById(cbxSupplierId.getSelectedItem().toString());
                txtSupplierName.setText(name);
            }
        });

        txtSupplierId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String name = supplierBUS.getNameById(cbxSupplierId.getSelectedItem().toString());
                txtSupplierName.setText(name);
            }
        });
    }

    public void setComponentsInfo() {
        lblTitle.setText(invoiceName.toUpperCase());
        lblInvoiceId.setText("Mã " + invoiceName);

        txtEmployeeId.setText(employeeId);

        if (invoiceName.equals("phiếu xuất")) {
            txtInvoiceId.setText(exportBUS.createNewId());
            lblSupplierId.setVisible(false);
            cbxSupplierId.setVisible(false);
            lblSupplierName.setVisible(false);
            txtSupplierName.setVisible(false);
        } else if (invoiceName.equals("phiếu nhập")) {
            txtInvoiceId.setText(importBUS.createNewId());
        }
    }

    public void openAddInvoiceGUI() {
        JFrame frame = new JFrame(invoiceName.toUpperCase());
        frame.setContentPane(new AddInvoiceGUI(invoiceName, employeeId, storageGUI).mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTextField txtInvoiceId;
    private JTextField txtEmployeeId;
    private JTextField txtSupplierName;
    private JButton btnSave;
    private JButton btnCancel;
    private JLabel lblTitle;
    private JLabel lblInvoiceId;
    private JLabel lblEmployeeId;
    private JLabel lblSupplierId;
    private JLabel lblSupplierName;
    private JComboBox cbxSupplierId;
}
