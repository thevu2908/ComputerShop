package BUS;

import DTO.BillDTO;
import DTO.BillDetailDTO;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;

public class StatisticsBUS {
    BillBUS billBUS;
    ImportBUS importBUS;
    EmployeeBUS employeeBUS;
    BillDetailBUS billDetailBUS;
    ProductBUS productBUS;

    public StatisticsBUS() {
        billBUS = new BillBUS();
        importBUS = new ImportBUS();
        employeeBUS = new EmployeeBUS();
        billDetailBUS = new BillDetailBUS();
        productBUS  = new ProductBUS();
    }

    public int[] getRevenueOfMonth(int year) {
        int [] revenueOfMonth = billBUS.getRevenueBillOfMonths(year);
        int [] expenseOfMonth = importBUS.getExpenseOfMonths(year);
        int [] totalRevenue = new int[12];
        for (int i = 0; i< 12; i++) {
            totalRevenue[i] += revenueOfMonth[i] - expenseOfMonth[i];
        }
        return totalRevenue;
    }

    public CategoryDataset createDoanhThuDataset(int year) {
        var dataset = new DefaultCategoryDataset();
        int [] totalRevenue = getRevenueOfMonth(year);
        int month=1;
        for(int item : totalRevenue){
//            dataset.setValue(item, "Doanh thu", String.format("Tháng %d",month++
            System.out.println("Doanh thu năm:" + year);
            System.out.println(String.format("Tháng %d:  ",month++) + item);
        }
        dataset.setValue(totalRevenue[0], "Doanh thu", "Tháng 1");
        dataset.setValue(totalRevenue[1], "Doanh thu", "Tháng 2");
        dataset.setValue(totalRevenue[2], "Doanh thu", "Tháng 3");
        dataset.setValue(totalRevenue[3], "Doanh thu", "Tháng 4");
        dataset.setValue(totalRevenue[4], "Doanh thu", "Tháng 5");
        dataset.setValue(totalRevenue[5], "Doanh thu", "Tháng 6");
        dataset.setValue(totalRevenue[6], "Doanh thu", "Tháng 7");
        dataset.setValue(totalRevenue[7], "Doanh thu", "Tháng 8");
        dataset.setValue(totalRevenue[8], "Doanh thu", "Tháng 9");
        dataset.setValue(totalRevenue[9], "Doanh thu", "Tháng 10");
        dataset.setValue(totalRevenue[10], "Doanh thu", "Tháng 11");
        dataset.setValue(totalRevenue[11], "Doanh thu", "Tháng 12");
        return dataset;
    }

    public CategoryDataset createBestEmployeeDataset() {
        var dataset = new DefaultCategoryDataset();
        ArrayList<BillDTO> listExcellentEmployee= billBUS.getExcellentEmployee();
        for(BillDTO billDTO : listExcellentEmployee){
            System.out.println(employeeBUS.getNameById(billDTO.getEmployeeId()));
            System.out.println(billDTO.getTotal());
        }
//
//        System.out.println("check:" + listExcellentEmployee.get(0).getTotal());
        dataset.setValue(listExcellentEmployee.get(0).getTotal(), "Doanh thu đem về", employeeBUS.getNameById(listExcellentEmployee.get(0).getEmployeeId()));
        dataset.setValue(listExcellentEmployee.get(1).getTotal(), "Doanh thu đem về", employeeBUS.getNameById(listExcellentEmployee.get(1).getEmployeeId()));
        dataset.setValue(listExcellentEmployee.get(2).getTotal(), "Doanh thu đem về", employeeBUS.getNameById(listExcellentEmployee.get(2).getEmployeeId()));
        dataset.setValue(listExcellentEmployee.get(3).getTotal(), "Doanh thu đem về", employeeBUS.getNameById(listExcellentEmployee.get(3).getEmployeeId()));
//        dataset.setValue(listExcellentEmployee.get(4).getTotal(), "Doanh thu đem về", employeeBUS.getNameById(listExcellentEmployee.get(4).getEmployeeId()));
//        dataset.setValue(listExcellentEmployee.get(5).getTotal(), "Doanh thu đem về", employeeBUS.getNameById(listExcellentEmployee.get(5).getEmployeeId()));
//        dataset.setValue(listExcellentEmployee.get(6).getTotal(), "Doanh thu đem về", employeeBUS.getNameById(listExcellentEmployee.get(6).getEmployeeId()));
//        dataset.setValue(listExcellentEmployee.get(7).getTotal(), "Doanh thu đem về", employeeBUS.getNameById(listExcellentEmployee.get(7).getEmployeeId()));
//        dataset.setValue(listExcellentEmployee.get(8).getTotal(), "Doanh thu đem về", employeeBUS.getNameById(listExcellentEmployee.get(8).getEmployeeId()));
//        dataset.setValue(listExcellentEmployee.get(9).getTotal(), "Doanh thu đem về", employeeBUS.getNameById(listExcellentEmployee.get(9).getEmployeeId()));
//        dataset.setValue(listExcellentEmployee.get(10).getTotal(), "Doanh thu đem về", employeeBUS.getNameById(listExcellentEmployee.get(10).getEmployeeId()));
//        dataset.setValue(listExcellentEmployee.get(11).getTotal(), "Doanh thu đem về", employeeBUS.getNameById(listExcellentEmployee.get(11).getEmployeeId()));

        return dataset;
    }

        public CategoryDataset createBestSellerDataset() {
        var dataset = new DefaultCategoryDataset();
        ArrayList <BillDetailDTO> listBestSeller = billDetailBUS.getListSoldProductInMonth();
        dataset.setValue(listBestSeller.get(0).getQuantity(), "Số lượng sản phẩm", productBUS.getNameById(listBestSeller.get(0).getProductId()));
        dataset.setValue(listBestSeller.get(1).getQuantity(), "Số lượng sản phẩm", productBUS.getNameById(listBestSeller.get(1).getProductId()));
        dataset.setValue(listBestSeller.get(2).getQuantity(), "Số lượng sản phẩm", productBUS.getNameById(listBestSeller.get(2).getProductId()));
        dataset.setValue(listBestSeller.get(3).getQuantity(), "Số lượng sản phẩm", productBUS.getNameById(listBestSeller.get(3).getProductId()));
//        dataset.setValue(20, "Số lượng sản phẩm", "Apple MacBook Air");
//        dataset.setValue(18, "Số lượng sản phẩm", "Dell Gaming G15 5511");
//        dataset.setValue(12, "Số lượng sản phẩm", "LDell Inspiron 16 5620");
//        dataset.setValue(9, "Số lượng sản phẩm", "Lenovo ThinkPad P14s");
        return dataset;
    }

}
