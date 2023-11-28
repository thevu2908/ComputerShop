package GUI;

import BUS.ProductBUS;
import BUS.ProductTypeBUS;
import DTO.ProductDTO;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ProductGUI {
    private DefaultTableModel prodModel;
    private TableRowSorter<DefaultTableModel> productSorter;
    private ProductBUS productBUS;
    private ProductTypeBUS productTypeBUS;

    public ProductGUI() {
        productBUS = new ProductBUS();
        productTypeBUS = new ProductTypeBUS();
        initTable();
        initTableData();
        initComboBoxTypeData();

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
                String ram = cbxRAM.getSelectedItem().toString();
                String storage = txtStorage.getText();
                String screen = txtScreen.getText();
                String screenCard = txtScreenCard.getText();

                if (productBUS.addProduct(id, name, type, price, cpu, ram, storage, screen, screenCard)
                        && productBUS.addProductStorage(id, 0)
                ) {
                    reset();
                }
            }
        });

        tblProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowSelected = tblProducts.getSelectedRow();

                if (rowSelected >= 0) {
                    String productId = tblProducts.getValueAt(rowSelected, 0).toString();
                    ProductDTO product = productBUS.getProductById(productId);

                    txtProductID.setText(product.getProductId());
                    txtProductName.setText(product.getProductName());
                    cbxProductType.setSelectedItem(productTypeBUS.getNameById(product.getProductType()));
                    txtProductPrice.setText(product.getProductPrice() + "");
                    txtCPU.setText(product.getProductCPU());
                    cbxRAM.setSelectedItem(product.getProductRAM());
                    txtStorage.setText(product.getProductStorage());
                    txtScreen.setText(product.getProductScreen());
                    txtScreenCard.setText(product.getProductScreenCard());
                }
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
                String ram = cbxRAM.getSelectedItem().toString();
                String storage = txtStorage.getText();
                String screen = txtScreen.getText();
                String screenCard = txtScreenCard.getText();

                if (productBUS.updateProduct(id, name, type, price, cpu, ram, storage, screen, screenCard)) {
                    reset();
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblProducts.getSelectedRow();

                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm muốn xoá", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String id = tblProducts.getValueAt(selectedRow, 0).toString();
                if (productBUS.deleteProduct(id)) {
                    reset();
                }
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterProduct();
            }
        });

        cbxFilterPrice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterProduct();
            }
        });

        cbxFilterProductType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterProduct();
            }
        });

        btnExportExcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                String defaultFileName = "sanpham.xlsx";
                fileChooser.setSelectedFile(new File(defaultFileName));

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel (.xlsx)", "xlsx");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showSaveDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fileChooser.getSelectedFile();
                        productBUS.exportExcel(file);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        btnImportExcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser= new JFileChooser();

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files (.xlsx)", "xlsx");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION){
                    try {
                        File file = fileChooser.getSelectedFile();

                        int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn import file excel này ?", "Hỏi",
                                JOptionPane.YES_NO_OPTION);

                        if (choice == JOptionPane.YES_OPTION && productBUS.importExcel(file.getAbsolutePath())) {
                            initTableData();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void initComboBoxTypeData(){
        cbxProductType.removeAllItems();
        productTypeBUS.renderToComboBox(cbxProductType);
        cbxFilterProductType.removeAllItems();
        cbxFilterProductType.addItem("Tất cả");
        productTypeBUS.renderToComboBox(cbxFilterProductType);
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
        tblProducts.getTableHeader().setBackground(new Color(86, 132, 242));
        tblProducts.getTableHeader().setForeground(Color.WHITE);

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
        cbxRAM.setSelectedIndex(0);
        txtStorage.setText("");
        txtScreen.setText("");
        txtScreenCard.setText("");
        txtSearch.setText("");
        cbxProductType.setSelectedIndex(0);
        cbSearchType.setSelectedIndex(0);
        cbxFilterPrice.setSelectedIndex(0);
        cbxFilterProductType.setSelectedIndex(0);
        initTableData();
        if (productSorter != null) {
            productSorter.setRowFilter(null);
        }
    }

    public void filterProduct() {
        String searchType = cbSearchType.getSelectedItem().toString();
        String productInfo = txtSearch.getText().toLowerCase();

        String productType = cbxFilterProductType.getSelectedItem() == null
                || cbxFilterProductType.getSelectedItem().toString().equals("Tất cả")
                ? ""
                : cbxFilterProductType.getSelectedItem().toString().toLowerCase();

        int productPrice = cbxFilterPrice.getSelectedIndex();
        int minPrice = 0;
        int maxPrice = 1999999999;

        if (productPrice == 1) {
            minPrice = 0;
            maxPrice = 20000000 - 1;
        } else if (productPrice == 2) {
            minPrice = 20000000;
            maxPrice = 40000000;
        }
        else if (productPrice == 3){
            minPrice = 40000000 + 1;
            maxPrice = 1999999999;
        }

        int finalMinPrice = minPrice;
        int finalMaxPrice = maxPrice;

        productSorter = new TableRowSorter<>(prodModel);
        tblProducts.setRowSorter(productSorter);

        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                String rowId = entry.getStringValue(0).toLowerCase();
                String rowName = entry.getStringValue(1).toLowerCase();
                String rowType = entry.getStringValue(2).toLowerCase();
                int rowPrice = Integer.parseInt(entry.getStringValue(3));

                switch (searchType) {
                    case "Mã sản phẩm":
                        return rowId.contains(productInfo) && rowType.contains(productType)
                                && rowPrice >= finalMinPrice && rowPrice <= finalMaxPrice;
                    case "Tên sản phẩm":
                        return rowName.contains(productInfo) && rowType.contains(productType)
                                && rowPrice >= finalMinPrice && rowPrice <= finalMaxPrice;
                    default:
                        return true;
                }
            }
        };

        productSorter.setRowFilter(filter);
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
    private JTextField txtStorage;
    private JTextField txtScreen;
    private JTextField txtScreenCard;
    private JComboBox cbxFilterPrice;
    private JComboBox cbxFilterProductType;
    private JComboBox cbxRAM;
}