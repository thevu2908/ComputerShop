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
    private String invoiceId;
    private String invoiceName;
    private String employeeId;
    private StorageGUI storageGUI;
    private ImportBUS importBUS;
    private ExportBUS exportBUS;
    private SupplierBUS supplierBUS;
    private JTextField txtSupplierId;

    public AddInvoiceGUI(String invoiceId, String invoiceName, String employeeId, StorageGUI storageGUI) {
        this.invoiceName = invoiceName;
        this.employeeId = employeeId;
        this.storageGUI = storageGUI;
        this.invoiceId = invoiceId;
        importBUS = new ImportBUS();
        exportBUS = new ExportBUS();
        supplierBUS = new SupplierBUS();

        txtSupplierId = AutoSuggestComboBox.createAutoSuggest(cbxSupplierId, 0, supplierBUS::initSupplierSuggestion);
        setComponentsInfo();

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (invoiceName.equals("phiếu nhập")) {
                    String importId = txtInvoiceId.getText();
                    String employeeId = txtEmployeeId.getText();
                    String supplierId = txtSupplierId.getText();

                    if (invoiceId.equals("")) {
                        if (importBUS.addImport(importId, employeeId, supplierId)) {
                            ImportDetailGUI importDetailGUI = new ImportDetailGUI(txtInvoiceId.getText(), storageGUI);
                            importDetailGUI.openImportDetailGUI();
                            storageGUI.initImportTableData();
                            closeInvoiceGUI();
                        }
                    } else {
                        if (importBUS.updateImportSupplier(importId, supplierId)) {
                            storageGUI.initImportTableData();
                            closeInvoiceGUI();
                        }
                    }
                }
                else if (invoiceName.equals("phiếu xuất")) {
                    String exportId = txtInvoiceId.getText();
                    String employeeId = txtEmployeeId.getText();

                    if (invoiceId.equals("")) {
                        if (exportBUS.addExport(exportId, employeeId)) {
                            ExportDetailGUI exportDetailGUI = new ExportDetailGUI(txtInvoiceId.getText(), storageGUI);
                            exportDetailGUI.openExportDetailGUI();
                            storageGUI.initExportTableData();
                            closeInvoiceGUI();
                        }
                    }
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

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeInvoiceGUI();
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
        }
        else if (invoiceName.equals("phiếu nhập")) {
            txtInvoiceId.setText(importBUS.createNewId());

            if (!invoiceId.equals("")) {
                txtInvoiceId.setText(invoiceId);
                txtSupplierId.setText(importBUS.getSupplierById(invoiceId));
                txtSupplierName.setText(supplierBUS.getNameById(txtSupplierId.getText()));
            }
        }
    }

    public void openInvoiceGUI() {
        JFrame frame = new JFrame(invoiceName.toUpperCase());
        frame.setContentPane(new AddInvoiceGUI(invoiceId, invoiceName, employeeId, storageGUI).mainPanel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void closeInvoiceGUI() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        frame.dispose();
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