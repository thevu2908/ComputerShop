package BUS;

import DTO.BillDetailDTO;
import DTO.EmployeeDTO;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;

public class StatisticsBUS {
    private BillBUS billBUS;
    private EmployeeBUS employeeBUS;
    private BillDetailBUS billDetailBUS;
    private ProductBUS productBUS;

    public StatisticsBUS() {
        billBUS = new BillBUS();
        employeeBUS = new EmployeeBUS();
        billDetailBUS = new BillDetailBUS();
        productBUS  = new ProductBUS();
    }

    public double[] getRevenueOfMonth(int year) {
        double[] revenueOfMonth = billBUS.getBillRevenueOfMonths(year);
        double[] totalRevenue = new double[12];
        for (int i = 0; i < 12; i++) {
            totalRevenue[i] += revenueOfMonth[i];
        }
        return totalRevenue;
    }

    public CategoryDataset createRevenueDataset(int year) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        double[] totalRevenue = getRevenueOfMonth(year);

        for (int i = 0; i < 12; i++) {
            dataset.setValue(totalRevenue[i], "Doanh thu (triệu đồng)", "Tháng " + (i + 1));
        }

        return dataset;
    }

    public CategoryDataset createBestEmployeeDataset(String month, int year) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ArrayList<EmployeeDTO> listExcellentEmployee = billBUS.getBestEmployee(month, year);

        for (int i = 0; i < listExcellentEmployee.size(); i++) {
            if (i == 10) {
                break;
            }

            dataset.setValue(
                    listExcellentEmployee.get(i).getTotal(),
                    "Doanh thu đem về (triệu đồng)",
                    employeeBUS.getNameById(listExcellentEmployee.get(i).getEmployeeId())
            );

        }

        return dataset;
    }

    public CategoryDataset createBestSellerDataset(String month, int year) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        ArrayList <BillDetailDTO> listBestSeller = billDetailBUS.getSoldProductListInMonth(month, year);

        for (int i = 0; i < listBestSeller.size(); i++) {
            if (i == 10) {
                break;
            }

            dataset.setValue(
                    listBestSeller.get(i).getQuantity(),
                    "Số lượng bán",
                    productBUS.getNameById(listBestSeller.get(i).getProductId())
            );
        }

        return dataset;
    }
}