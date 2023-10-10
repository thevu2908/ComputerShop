package GUI;

import BUS.ProductBUS;
import BUS.ProductTypeBUS;
import DTO.ProductDTO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class ProductGUI {
    private DefaultTableModel prodModel;
    private ProductBUS productBUS;
    private ProductTypeBUS productTypeBUS;

    public ProductGUI() {
        productBUS = new ProductBUS();
        productTypeBUS = new ProductTypeBUS();
        initTable();
        initTableData();
        initComboboxTypeData();

        btnCreateId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtProductID.setText(productBUS.createNewProductID());
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtProductID.getText();
                String name = txtProductName.getText();
                String type = cbxProductType.getSelectedItem().toString();
                String price = txtProductPrice.getText();
                String cpu = txtCPU.getText();
                String ram = txtRAM.getText();
                String oCung = txtOCung.getText();
                String screen = txtScreen.getText();
                String screenCard = txtScreenCard.getText();

                if (productBUS.addProduct(id, name, type, price, cpu, ram, oCung, screen, screenCard)) {
                    reset();
                }
            }
        });

        tblProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowSelected = tblProducts.getSelectedRow();
                String productId = tblProducts.getValueAt(rowSelected, 0).toString();
                ProductDTO product = productBUS.getProductById(productId);

                txtProductID.setText(product.getProductId());
                txtProductName.setText(product.getProductName());
                cbxProductType.setSelectedItem(productTypeBUS.getNameById(product.getProductType()));
                txtProductPrice.setText(product.getProductPrice() + "");
                txtCPU.setText(product.getProductCPU());
                txtRAM.setText(product.getProductRAM());
                txtOCung.setText(product.getProductDisk());
                txtScreen.setText(product.getProductScreen());
                txtScreenCard.setText(product.getProductScreenCard());
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtProductID.getText();
                String name = txtProductName.getText();
                String type = cbxProductType.getSelectedItem().toString();
                String price = txtProductPrice.getText();
                String cpu = txtCPU.getText();
                String ram = txtRAM.getText();
                String oCung = txtOCung.getText();
                String screen = txtScreen.getText();
                String screenCard = txtScreenCard.getText();

                if
                (productBUS.updateProduct(id, name, type, price, cpu, ram, oCung, screen, screenCard)) {
                    reset();
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtProductID.getText();
                if (productBUS.deleteProduct(id)) {
                    reset();
                }
            }
        });
    }

    public void initComboboxTypeData(){
        productTypeBUS.renderToCombobox(cbxProductType);
    }

    public void initTableData() {
        productBUS.renderToProductTable(prodModel);
    }

    public void initTable() {
        prodModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã sản phẩm", "Tên sản phẩm", "Hãng", "Giá"};
        prodModel.setColumnIdentifiers(cols);
        tblProducts.setModel(prodModel);
        tblProducts.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblProducts.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public void reset() {
        txtProductID.setText("");
        txtProductName.setText("");
        txtProductPrice.setText("");
        txtCPU.setText("");
        txtRAM.setText("");
        txtOCung.setText("");
        txtScreen.setText("");
        txtScreenCard.setText("");
        txtSearch.setText("");
        cbxProductType.setSelectedIndex(0);
        cbSearchType.setSelectedIndex(0);
        cbxPrice.setSelectedIndex(0);
        initTableData();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel contentPanel;
    private JTable tblProducts;
    private JComboBox cbSearchType;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnExportExcel;
    private JButton btnDelete;
    private JButton btnImportExcel;
    private JButton btnReset;
    private JButton btnCreateId;
    private JTextField txtProductID;
    private JTextField txtProductName;
    private JTextField txtProductPrice;
    private JComboBox cbxProductType;
    private JPanel mainPanel;
    private JTextField txtSearch;
    private JTextField txtCPU;
    private JTextField txtOCung;
    private JTextField txtRAM;
    private JTextField txtScreen;
    private JTextField txtScreenCard;
    private JComboBox cbxPrice;
    private JPanel cbxFilterPrice;
}
