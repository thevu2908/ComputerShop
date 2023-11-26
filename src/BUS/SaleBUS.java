package BUS;

import DAO.SaleDAO;
import DTO.SaleDTO;
import utils.DateTime;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Date;

public class SaleBUS {
    private ArrayList<SaleDTO> salesList;
    private SaleDAO saleDAO;

    public SaleBUS() {
        saleDAO = new SaleDAO();
    }

    public void loadData() {
        salesList = saleDAO.getData();
    }

    public boolean addSale(String saleId, String saleInfo, String startDate, String endDate) {
        if (saleId.equals("") || saleInfo.equals("") || startDate.equals("") || endDate.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedSaleId(saleId)) {
            JOptionPane.showMessageDialog(null, "Mã chương trình khuyến mãi đã tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!saleInfo.endsWith("%")) {
            saleInfo = saleInfo + "%";
        }

        if (!Validate.isValidNumber(saleInfo.substring(0, saleInfo.length() - 1), "Giá khuyến mãi")) {
            return false;
        }

        if (!Validate.isValidDate(startDate)) {
            JOptionPane.showMessageDialog(null, "Ngày bắt đầu không hợp lệ (dd-MM-yyyy)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidDate(endDate)) {
            JOptionPane.showMessageDialog(null, "Ngày kết thúc không hợp lệ (dd-MM-yyyy)", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Date currentDate = DateTime.getCurrentDate();
            Date startDateObj = DateTime.parseDate(DateTime.formatDate(startDate));
            Date endDateObj = DateTime.parseDate(DateTime.formatDate(endDate));

            if (startDateObj.before(currentDate)) {
                JOptionPane.showMessageDialog(null, "Ngày bắt đầu đã trước ngày hiện tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (endDateObj.before(startDateObj)) {
                JOptionPane.showMessageDialog(null, "Ngày kết thúc phải sau ngày bắt đầu", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Thêm chương trình khuyến mãi thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }

        SaleDTO sale = new SaleDTO(saleId, saleInfo, DateTime.formatDate(startDate), DateTime.formatDate(endDate),
                "Chưa áp dụng", 0);

        if (saleDAO.addSale(sale) > 0) {
            JOptionPane.showMessageDialog(null, "Thêm chương trình khuyến mãi thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm chương trình khuyến mãi thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteSale(String saleId) {
        String status = getSaleById(saleId).getSaleStatus();
        if (status.equals("Đang áp dụng")) {
            JOptionPane.showMessageDialog(null, "Không thể xóa chương trình khuyến mãi đang áp dụng", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (status.equals("Ngưng áp dụng")) {
            JOptionPane.showMessageDialog(null, "Không thể xóa chương trình khuyến mãi ngưng áp dụng", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (saleDAO.deleteSale(saleId) > 0) {
            JOptionPane.showMessageDialog(null, "Xóa chương trình khuyến mãi thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Xóa chương trình khuyến mãi thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean applySale(String saleId) {
        SaleDTO sale = getSaleById(saleId);

        if (sale.getSaleStatus().equals("Đang áp dụng")) {
            JOptionPane.showMessageDialog(null, "Chương trình khuyến này đang áp dụng", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedAppliedSale()) {
            JOptionPane.showMessageDialog(null, "Hiện đang có chương trình khuyến mãi khác đang áp dụng", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            Date currentDate = DateTime.getCurrentDate();
            Date startDate = DateTime.parseDate(sale.getStartDate());
            Date endDate = DateTime.parseDate(sale.getEndDate());

            if (startDate.after(currentDate)) {
                JOptionPane.showMessageDialog(null, "Chương trình khuyến mãi này chưa tới ngày áp dụng", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (endDate.before(currentDate)) {
                JOptionPane.showMessageDialog(null, "Chương trình khuyến mãi này đã hết hạn ngày áp dụng", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Áp dụng chương trình khuyến mãi thất bại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }

        if (saleDAO.applySale(saleId) > 0) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Áp dụng chương trình khuyến mãi thất bại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean stopApplySale(String saleId) {
        try {
            SaleDTO sale = getSaleById(saleId);

            if (!sale.getSaleStatus().equals("Đang áp dụng")) {
                JOptionPane.showMessageDialog(null, "Chương trình khuyến mãi này đang không được áp dụng", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Date currentDate = DateTime.getCurrentDate();
            Date endDate = DateTime.parseDate(sale.getEndDate());

            if (currentDate.before(endDate)) {
                String question = "Chương trình khuyến mãi này còn áp dụng tới ngày "
                        + DateTime.formatDate(sale.getEndDate())
                        + "\nBạn có muốn ngưng áp dụng không ?";

                int choice = JOptionPane.showConfirmDialog(null, question, "Hỏi", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    if (saleDAO.stopApplySale(saleId) > 0) {
                        return true;
                    }
                }
            } else {
                if (saleDAO.stopApplySale(saleId) > 0) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ngưng áp dụng chương trình khuyến mãi thất bại", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }

    public String createNewId() {
        loadData();
        int id = salesList.size() + 1;
        return "CTKM" + String.format("%02d", id);
    }

    public SaleDTO findApplyingSale() {
        loadData();

        for (SaleDTO saleDTO : salesList) {
            if (saleDTO.getSaleStatus().equals("Đang áp dụng")) {
                return saleDTO;
            }
        }

        return null;
    }

    public boolean checkExistedAppliedSale() {
        loadData();

        for (SaleDTO saleDTO : salesList) {
            if (saleDTO.getIsDeleted() == 0 && saleDTO.getSaleStatus().equals("Đang áp dụng")) {
                return true;
            }
        }

        return false;
    }

    public boolean checkExistedSaleId(String saleId) {
        loadData();

        for (SaleDTO saleDTO : salesList) {
            if (saleDTO.getIsDeleted() == 0 && saleDTO.getSaleId().equals(saleId)) {
                return true;
            }
        }

        return false;
    }

    public SaleDTO getSaleById(String id) {
        loadData();

        for (SaleDTO saleDTO : salesList) {
            if (saleDTO.getIsDeleted() == 0 && saleDTO.getSaleId().equals(id)) {
                return saleDTO;
            }
        }

        return null;
    }

    public void renderToTable(DefaultTableModel model) {
        loadData();
        model.setRowCount(0);

        for (SaleDTO saleDTO : salesList) {
            if (saleDTO.getIsDeleted() == 0) {
                String status = saleDTO.getSaleStatus();
                if (status.equals("Đang áp dụng")) {
                    status = "<html><font color='#0d6efd' style='font-weight:bold;'>Đang áp dụng</font></html>";
                } else if (status.equals("Ngưng áp dụng")) {
                    status = "<html><font color='#dc3545' style='font-weight: bold;'>Ngưng áp dụng</font></html>";
                } else {
                    status = "<html><font color='#ffc107' style='font-weight: bold;'>Chưa áp dụng</font></html>";
                }

                model.addRow(new Object[]{
                        saleDTO.getSaleId(),
                        saleDTO.getSaleInfo(),
                        DateTime.formatDate(saleDTO.getStartDate()),
                        DateTime.formatDate(saleDTO.getEndDate()),
                        status
                });
            }
        }
    }
}