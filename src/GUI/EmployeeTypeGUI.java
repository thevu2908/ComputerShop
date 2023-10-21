package GUI;

import BUS.EmployeeTypeBUS;
import DTO.EmployeeTypeDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;

public class EmployeeTypeGUI {
    private DefaultTableModel accessModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private EmployeeTypeBUS employeeTypeBUS;

    public EmployeeTypeGUI() {
        employeeTypeBUS = new EmployeeTypeBUS();
        initTable();
        initTableData();

        btnCreateNewId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtAccessId.setText(employeeTypeBUS.createNewEmployeeTypeId());
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maLoaiNhanVien = txtAccessId.getText();
                String tenLoaiNhanVien = txtAccessName.getText();
                if (employeeTypeBUS.addEmployeeType(maLoaiNhanVien, tenLoaiNhanVien)) {
                    resetData();
                }
            }
        });

        tblAccesses.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowSelected = tblAccesses.getSelectedRow();

                if (rowSelected >= 0) {
                    String employeeTypeId = tblAccesses.getValueAt(rowSelected, 0).toString();
                    EmployeeTypeDTO employeeType = employeeTypeBUS.getEmployeeTypeById(employeeTypeId);

                    txtAccessId.setText(employeeTypeId);
                    txtAccessName.setText(employeeType.getTypeName());
                }
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetData();
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filter();
            }
        });
    }

    public void filter() {
        String searchType = cbxSearchType.getSelectedItem().toString();
        String searchInfo = txtSearch.getText().toLowerCase();

        sorter = new TableRowSorter<>(accessModel);
        tblAccesses.setRowSorter(sorter);

        RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                String rowId = entry.getStringValue(0).toLowerCase();
                String rowName = entry.getStringValue(1).toLowerCase();

                switch (searchType) {
                    case "Mã quyền":
                        return rowId.contains(searchInfo);
                    case "Tên quyền":
                        return rowName.contains(searchInfo);
                    default:
                        return true;
                }
            }
        };

        sorter.setRowFilter(filter);
    }

    public void resetData() {
        txtAccessId.setText("");
        txtAccessName.setText("");
        cbxSearchType.setSelectedIndex(0);
        txtSearch.setText("");
        initTableData();
        if (sorter != null) {
            sorter.setRowFilter(null);
        }
    }
    
    public void initTableData() {
        employeeTypeBUS.renderToTable(accessModel);
    }

    public void initTable() {
        accessModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] cols = {"Mã quyền", "Tên quyền"};
        accessModel.setColumnIdentifiers(cols);
        tblAccesses.setModel(accessModel);
        tblAccesses.getTableHeader().setFont(new Font("Time News Roman", Font.BOLD, 14));
        tblAccesses.getTableHeader().setBackground(new Color(86, 132, 242));
        tblAccesses.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < cols.length; i++) {
            tblAccesses.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;
    private JPanel contentPanel;
    private JTable tblAccesses;
    private JPanel SearchPanel;
    private JTextField txtAccessName;
    private JTextField txtAccessId;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
    private JButton btnCreateNewId;
    private JButton btnAdd;
    private JButton btnReset;
}