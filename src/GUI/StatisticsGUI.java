package GUI;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class StatisticsGUI {
    public StatisticsGUI() {
        initDoanhThu();
        initBestSeller();
        initBestEmployee();
    }

    public void initBestEmployee() {
        initBarChart("Doanh thu đem về", bestEmployeeChartPanel);
    }

    public void initBestSeller() {
        initBarChart("Số lượng sản phẩm", bestSellerChartPanel);
    }

    public void initDoanhThu() {
        initBarChart("Doanh thu", doanhThuChartPanel);
    }

    public  void initBarChart(String name, JPanel panel) {
        CategoryDataset dataset = null;
        if (name.equals("Doanh thu")) {
            dataset = createDoanhThuDataset();
        } else if (name.equals("Số lượng sản phẩm")) {
            dataset = createBestSellerDataset();
        } else if (name.equals("Doanh thu đem về")) {
            dataset = createBestEmployeeDataset();
        }

        JFreeChart chart = createBarChart(dataset, name);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        panel.setLayout(new java.awt.BorderLayout());
        panel.add(chartPanel,BorderLayout.CENTER);
    }

    private CategoryDataset createBestEmployeeDataset() {
        var dataset = new DefaultCategoryDataset();
        dataset.setValue(22000000, "Doanh thu đem về", "Hoàng" );
        dataset.setValue(20000000, "Doanh thu đem về", "Vũ");
        dataset.setValue(18000000, "Doanh thu đem về", "Phú");
        return dataset;
    }

    private CategoryDataset createBestSellerDataset() {
        var dataset = new DefaultCategoryDataset();
        dataset.setValue(46, "Số lượng sản phẩm", "MSI Gaming GF63");
        dataset.setValue(38, "Số lượng sản phẩm", "Acer Aspire 7");
        dataset.setValue(29, "Số lượng sản phẩm", "Asus TUF");
        dataset.setValue(22, "Số lượng sản phẩm", "HP 15s fq5077TU");
        dataset.setValue(20, "Số lượng sản phẩm", "Apple MacBook Air");
        dataset.setValue(18, "Số lượng sản phẩm", "Dell Gaming G15 5511");
        dataset.setValue(12, "Số lượng sản phẩm", "LDell Inspiron 16 5620");
        dataset.setValue(9, "Số lượng sản phẩm", "Lenovo ThinkPad P14s");
        return dataset;
    }

    private CategoryDataset createDoanhThuDataset() {
        var dataset = new DefaultCategoryDataset();
        dataset.setValue(46, "Doanh thu", "Tháng 1");
        dataset.setValue(37, "Doanh thu", "Tháng 2");
        dataset.setValue(29, "Doanh thu", "Tháng 3");
        dataset.setValue(22, "Doanh thu", "Tháng 4");
        dataset.setValue(13, "Doanh thu", "Tháng 5");
        dataset.setValue(11, "Doanh thu", "Tháng 6");
        dataset.setValue(13, "Doanh thu", "Tháng 7");
        dataset.setValue(17, "Doanh thu", "Tháng 8");
        dataset.setValue(24, "Doanh thu", "Tháng 9");
        dataset.setValue(26, "Doanh thu", "Tháng 10");
        dataset.setValue(30, "Doanh thu", "Tháng 11");
        dataset.setValue(19, "Doanh thu", "Tháng 12");
        return dataset;
    }

    private JFreeChart createBarChart(CategoryDataset dataset, String name) {
        JFreeChart barChart = ChartFactory.createBarChart(
                "",
                "",
                name,
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);
        return barChart;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JComboBox cbxYear;
    private JPanel doanhThuChartPanel;
    private JPanel bestSellerChartPanel;
    private JPanel bestEmployeeChartPanel;
}
