package GUI;

import BUS.StatisticsBUS;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class StatisticsGUI {
    private StatisticsBUS statisticsBUS;

    public StatisticsGUI() {
        statisticsBUS = new StatisticsBUS();
        initComboBoxYear();
        setCurrentMonth();
        initRevenue();
        initBestSeller();
        initBestEmployee();

        cbxYearOfRevenue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int year = Integer.parseInt(String.valueOf(cbxYearOfRevenue.getSelectedItem()));
                initBarChart("Doanh thu", doanhThuChartPanel, year);
            }
        });

        cbxFilterEmployeeMonth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initBarChart("Doanh thu bán được", bestEmployeeChartPanel, 2023);
            }
        });

        cbxFilterProductMonth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initBarChart("Số lượng bán", bestSellerChartPanel, 2023);
            }
        });

        mainTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (mainTabbedPane.getSelectedComponent() == revenuePanel) {
                    initComboBoxYear();
                    initRevenue();
                } else if (mainTabbedPane.getSelectedComponent() == bestEmployeePanel) {
                    setCurrentMonth();
                    initBestEmployee();
                } else if (mainTabbedPane.getSelectedComponent() == bestSellerPanel) {
                    setCurrentMonth();
                    initBestSeller();
                }
            }
        });
    }

    public void initBestEmployee() {
        initBarChart("Doanh thu bán được", bestEmployeeChartPanel, 2023);
    }

    public void initBestSeller() {
        initBarChart("Số lượng bán", bestSellerChartPanel, 2023);
    }

    public void initRevenue() {
        initBarChart("Doanh thu", doanhThuChartPanel, 2023);
    }

    public void initBarChart(String name, JPanel panel, int year) {
        CategoryDataset dataset = null;
        if (name.equals("Doanh thu")) {
            dataset = statisticsBUS.createRevenueDataset(year);
            name = name + " (triệu đồng)";
        } else if (name.equals("Số lượng bán")) {
            dataset = statisticsBUS.createBestSellerDataset(cbxFilterProductMonth.getSelectedItem().toString(), year);
        } else if (name.equals("Doanh thu bán được")) {
            dataset = statisticsBUS.createBestEmployeeDataset(cbxFilterEmployeeMonth.getSelectedItem().toString(), year);
            name = name + " (triệu đồng)";
        }

        JFreeChart chart = createBarChart(dataset, name);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        panel.setLayout(new java.awt.BorderLayout());
        panel.add(chartPanel,BorderLayout.CENTER);
    }

    public JFreeChart createBarChart(CategoryDataset dataset, String name) {
        JFreeChart barChart = ChartFactory.createBarChart(
                "",
                "",
                name,
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );
        return barChart;
    }

    public void initComboBoxYear() {
        LocalDate currentDate = LocalDate.now();
        int year = Integer.parseInt(String.valueOf(currentDate.getYear()));

        for (int i = 2023; i <= year; i++) {
            cbxYearOfRevenue.addItem(i);
        }

        cbxYearOfRevenue.setSelectedItem(year);
    }

    public void setCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        String month = String.valueOf(currentDate.getMonthValue());
        cbxFilterEmployeeMonth.setSelectedItem(month);
        cbxFilterProductMonth.setSelectedItem(month);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private JPanel mainPanel;
    private JTabbedPane mainTabbedPane;
    private JComboBox cbxYearOfRevenue;
    private JPanel doanhThuChartPanel;
    private JPanel bestSellerChartPanel;
    private JPanel bestEmployeeChartPanel;
    private JComboBox cbxFilterEmployeeMonth;
    private JComboBox cbxFilterProductMonth;
    private JPanel revenuePanel;
    private JPanel bestSellerPanel;
    private JPanel bestEmployeePanel;
}