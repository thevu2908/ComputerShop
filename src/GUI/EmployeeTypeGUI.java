package GUI;

import BUS.EmployeeTypeBUS;
import DTO.EmployeeDTO;
import DTO.EmployeeTypeDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EmployeeTypeGUI {
    private DefaultTableModel accessModel;
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
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maLoaiNhanVien = txtAccessId.getText();
                String tenLoaiNhanVien = txtAccessName.getText();
                if (employeeTypeBUS.updateEmployeeType(maLoaiNhanVien, tenLoaiNhanVien)) {
                    resetData();
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maLoaiNhanVien = txtAccessId.getText();
                String tenLoaiNhanVien = txtAccessName.getText();
                if (employeeTypeBUS.deleteEmployeeType(maLoaiNhanVien, tenLoaiNhanVien)) {
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
    }
    public void resetData() {
        txtAccessId.setText("");
        txtAccessName.setText("");
        initTableData();
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
    private JButton btnDelete;
    private JButton btnUpdate;
    private JButton btnReset;
    private JButton btnGiveAccess;
}