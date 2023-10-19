package GUI;

import BUS.StatisticsBUS;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatisticsGUI {
    private StatisticsBUS statisticsBUS;
    public StatisticsGUI() {
        statisticsBUS = new StatisticsBUS();
        initDoanhThu();
        initBestSeller();
        initBestEmployee();


        cbxYearOfRevenue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int year = Integer.parseInt(String.valueOf(cbxYearOfRevenue.getSelectedItem()));
                System.out.println(year);
                initBarChart("Doanh thu",doanhThuChartPanel,year);

            }
        });



    }

    public void initBestEmployee() {
        initBarChart("Doanh thu đem về", bestEmployeeChartPanel,2023);
    }

    public void initBestSeller() {
        initBarChart("Số lượng sản phẩm", bestSellerChartPanel,2023);
    }

    public void initDoanhThu() {
        initBarChart("Doanh thu", doanhThuChartPanel,2023);
    }

    public  void initBarChart(String name, JPanel panel,int year) {
        CategoryDataset dataset = null;
        if (name.equals("Doanh thu")) {
            dataset = statisticsBUS.createDoanhThuDataset(year);
        } else if (name.equals("Số lượng sản phẩm")) {
            dataset = statisticsBUS.createBestSellerDataset();
        } else if (name.equals("Doanh thu đem về")) {
            dataset = statisticsBUS.createBestEmployeeDataset();
        }

        JFreeChart chart = createBarChart(dataset, name);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        panel.setLayout(new java.awt.BorderLayout());
        panel.add(chartPanel,BorderLayout.CENTER);
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
    private JComboBox cbxYearOfRevenue;
    private JPanel doanhThuChartPanel;
    private JPanel bestSellerChartPanel;
    private JPanel bestEmployeeChartPanel;
}
