package GUI;

import javax.swing.*;

public class ImportDetailGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("ImportDetailGUI");
        frame.setContentPane(new ImportDetailGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel mainPanel;
    private JTable tblImportDetails;
    private JComboBox cbxPrice;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnReset;
    private JComboBox cbxSearchType;
    private JTextField txtSearch;
}