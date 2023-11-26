package BUS;

import DAO.ExportDetailDAO;
import DTO.ExportDTO;
import DTO.ExportDetailDTO;
import DTO.ProductDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ExportDetailBUS {
    private ArrayList<ExportDetailDTO> exportDetailList;
    private ExportDetailDAO exportDetailDAO;
    private ProductBUS productBUS;
    private ExportBUS exportBUS;
    private EmployeeBUS employeeBUS;

    public ExportDetailBUS() {
        exportDetailDAO = new ExportDetailDAO();
        productBUS = new ProductBUS();
        exportBUS = new ExportBUS();
        employeeBUS = new EmployeeBUS();
    }

    public void loadData() {
        exportDetailList = exportDetailDAO.getData();
    }

    public boolean addExportDetail(String exportId, String productId, String quantity) {
        if (exportBUS.getStatusById(exportId).equals("Đã duyệt")) {
            JOptionPane.showMessageDialog(null, "Không thể thêm chi tiết phiếu xuất đã được duyệt", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (exportId.equals("") || productId.equals("") || quantity.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidNumber(quantity, "Số lượng")) {
            return false;
        }

        if (!productBUS.checkExistedProductId(productId)) {
            JOptionPane.showMessageDialog(null, "Sản phẩm không tồn tại \nVui lòng nhập lại mã sản phẩm", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int productQuantity = productBUS.getProductStorageQuantityById(productId);
        int orderedQuantity = getOrderedProductQuantity(productId);
        int boughtQuantity = Integer.parseInt(quantity);

        if (productQuantity < boughtQuantity) {
            JOptionPane.showMessageDialog(null, "Số lượng còn lại của sản phẩm trong kho không đủ", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (productQuantity - orderedQuantity < boughtQuantity) {
            JOptionPane.showMessageDialog(
                    null,
                    "Đã có phiếu xuất được lập để xuất sản phẩm này nhưng chưa được duyệt " +
                            "\nSố lượng sản phẩm còn lại trong kho sẽ không đủ nếu duyệt phiếu xuất đó " +
                            "\nVui lòng duyệt phiếu xuất đó hoặc nhập thêm sản phẩm",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        boolean flag = false;
        ExportDetailDTO exportDetail = getExportDetailById(exportId, productId);

        if (exportDetail != null) { // case importDetail have already existed in list
            exportDetail.setQuantity(exportDetail.getQuantity() + boughtQuantity);
            if (updateExportDetailQuantity(exportDetail)) {
                flag = true;
            }
        } else { // case importDetail haven't existed in list
            exportDetail = new ExportDetailDTO(exportId, productId, boughtQuantity);
            if (exportDetailDAO.addExportDetail(exportDetail) > 0) {
                flag = true;
            }
        }

        if (flag) {
            JOptionPane.showMessageDialog(null, "Thêm chi tiết phiếu xuất thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm chi tiết phiếu xuất thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateExportDetailQuantity(ExportDetailDTO exportDetail) {
        return exportDetailDAO.updateExportDetailQuantity(exportDetail) > 0;
    }

    public boolean confirmExport(String exportId) {
        ArrayList<ExportDetailDTO> list = getExportDetailsByExportId(exportId);

        if (list.size() == 0) {
            JOptionPane.showMessageDialog(null, "Không thể duyệt phiếu nhập không có chi tiết phiếu nhập", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (exportBUS.confirmExport(exportId) && changeProductQuantity(exportId)) {
            JOptionPane.showMessageDialog(null, "Duyệt phiếu xuất thành công");
            return true;
        } else {
            return false;
        }
    }

    public boolean changeProductQuantity(String exportId) {
        loadData();

        for (ExportDetailDTO exportDetailDTO : exportDetailList) {
            if (exportDetailDTO.getExportId().equals(exportId)) {
                String productId = exportDetailDTO.getProductId();
                int quantity = exportDetailDTO.getQuantity();

                ProductDTO product = productBUS.getProductById(productId);
                ProductDTO storageProduct = productBUS.getProductStorageById(productId);

                if (
                        !(productBUS.updateProductQuantity(productId, product.getProductQuantity() + quantity)
                        &&
                        productBUS.updateProductStorageQuantity(productId, storageProduct.getProductQuantity() - quantity))
                ) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean deleteExportDetail(String exportId, String productId) {
        if (exportBUS.getStatusById(exportId).equals("Đã duyệt")) {
            JOptionPane.showMessageDialog(null, "Không thể xóa chi tiết phiếu xuất đã được duyệt", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (exportDetailDAO.deleteExportDetail(exportId, productId) > 0) {
            JOptionPane.showMessageDialog(null, "Xóa chi tiết phiếu xuất thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Xóa chi tiết phiếu xuất thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void printExport(String exportId, String path) {
        if (exportBUS.getStatusById(exportId).equals("Chưa duyệt")) {
            JOptionPane.showMessageDialog(null, "Không thể in phiếu xuất chưa được duyệt", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Document document = new Document();

            if (!path.endsWith(".pdf")) {
                path = path + ".pdf";
            }

            PdfWriter pdfWriter =  PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            setPDFTitle(document);
            setPDFHeader(document, pdfWriter, exportId);
            setPDFInformation(document, exportId);
            setPDFProductTable(document, exportId);
            setPDFFooter(document, exportId);

            document.close();
            JOptionPane.showMessageDialog(null, "In phiếu xuất thành công");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi in phiếu xuất", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setPDFFooter(Document document, String exportId) {
        try {
            Font fontBold = new Font(
                    BaseFont.createFont("/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                    11,
                    Font.BOLD
            );

            Font font = new Font(
                    BaseFont.createFont("/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                    11,
                    Font.NORMAL
            );

            Paragraph totalBox = new Paragraph();
            totalBox.setAlignment(Element.ALIGN_RIGHT);
            totalBox.setSpacingBefore(10);

            Chunk title = new Chunk("TỔNG SỐ LƯỢNG: ", fontBold);
            Chunk totalPrice = new Chunk(exportBUS.getExportById(exportId).getTotalQuantity() + "", font);

            totalBox.add(title);
            totalBox.add(totalPrice);

            document.add(totalBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPDFProductTable(Document document, String exportId) {
        loadData();
        try {
            Font font = new Font(
                    BaseFont.createFont("/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                    11,
                    Font.NORMAL
            );

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100f);

            float[] columnWidths = {20f, 100f, 30f};
            table.setWidths(columnWidths);

            PdfPCell noHeaderCell = new PdfPCell(new Paragraph("STT", font));
            noHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            noHeaderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            noHeaderCell.setPadding(10);
            table.addCell(noHeaderCell);

            PdfPCell nameHeaderCell = new PdfPCell(new Paragraph("Tên sản phẩm", font));
            nameHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            nameHeaderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            noHeaderCell.setPadding(10);
            table.addCell(nameHeaderCell);

            PdfPCell quantityHeaderCell = new PdfPCell(new Paragraph("Số lượng", font));
            quantityHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            quantityHeaderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            quantityHeaderCell.setPadding(10);
            table.addCell(quantityHeaderCell);

            int i = 0;
            for (ExportDetailDTO exportDetailDTO : exportDetailList) {
                if (exportDetailDTO.getExportId().equals(exportId)) {
                    i++;
                    PdfPCell noCell = new PdfPCell(new Paragraph(i++ + "", font));
                    noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    noCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    noCell.setPadding(10);

                    String name = productBUS.getNameById(exportDetailDTO.getProductId());
                    PdfPCell nameCell = new PdfPCell(new Paragraph(name, font));
                    nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    nameCell.setPadding(10);

                    String quantity = exportDetailDTO.getQuantity() + "";
                    PdfPCell quantityCell = new PdfPCell(new Paragraph(quantity, font));
                    quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    quantityCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    quantityCell.setPadding(10);

                    table.addCell(noCell);
                    table.addCell(nameCell);
                    table.addCell(quantityCell);
                }
            }

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPDFInformation(Document document, String exportId) {
        try {
            Font font = new Font(
                    BaseFont.createFont("/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                    11,
                    Font.NORMAL
            );

            Paragraph employeeName = new Paragraph(
                    "Họ tên nhân viên lập phiếu: "
                            + employeeBUS.getNameById(exportBUS.getExportById(exportId).getEmployeeId()).toUpperCase(),
                    font
            );
            employeeName.setSpacingBefore(15);
            employeeName.setSpacingAfter(20);

            document.add(employeeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPDFHeader(Document document, PdfWriter pdfWriter, String exportId) {
        try {
            Font font = new Font(
                    BaseFont.createFont("/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                    11,
                    Font.NORMAL
            );

            String[] date =exportBUS.getExportById(exportId).getExportDate().split("-");
            Paragraph dateString = new Paragraph("Ngày " + date[2] + " tháng " + date[1] + " năm " + date[0], font);
            dateString.setAlignment(Element.ALIGN_CENTER);
            dateString.setSpacingAfter(10);

            Paragraph importIdString = new Paragraph("Mã PX: " + exportId, font);
            importIdString.setAlignment(Element.ALIGN_RIGHT);

            PdfContentByte content = pdfWriter.getDirectContent();
            content.setLineWidth(1f);
            content.setColorStroke(BaseColor.BLACK);

            content.moveTo(35f, 720f);
            content.lineTo(560f, 720f);
            content.stroke();

            document.add(dateString);
            document.add(importIdString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPDFTitle(Document document) {
        try {
            Font fontTitle = new Font(
                    BaseFont.createFont("/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                    20,
                    Font.BOLD
            );

            Paragraph title = new Paragraph("PHIẾU XUẤT HÀNG", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);

            document.add(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getOrderedProductQuantity(String productId) {
        loadData();

        ArrayList<ExportDTO> exportList = exportBUS.getExportList();
        int quantity = 0;

        for (ExportDTO exportDTO : exportList) {
            if (exportDTO.getStatus().toLowerCase().equals("chưa duyệt")) {
                ArrayList<ExportDetailDTO> exportDetailList = getExportDetailsByExportId(exportDTO.getExportId());

                for (ExportDetailDTO exportDetailDTO : exportDetailList) {
                    if (exportDetailDTO.getProductId().equals(productId)) {
                        quantity += exportDetailDTO.getQuantity();
                    }
                }
            }
        }

        return quantity;
    }

    public ArrayList<ExportDetailDTO> getExportDetailsByExportId(String exportId) {
        loadData();
        ArrayList<ExportDetailDTO> list = new ArrayList<>();

        for (ExportDetailDTO exportDetailDTO : exportDetailList) {
            if (exportDetailDTO.getExportId().equals(exportId)) {
                list.add(exportDetailDTO);
            }
        }

        return list;
    }

    public ExportDetailDTO getExportDetailById(String exportId, String productId) {
        loadData();

        for (ExportDetailDTO exportDetailDTO : exportDetailList) {
            if (exportDetailDTO.getExportId().equals(exportId) && exportDetailDTO.getProductId().equals(productId)) {
                return exportDetailDTO;
            }
        }

        return null;
    }

    public int calculateTotalQuantity(String exportId) {
        int total = 0;
        for (ExportDetailDTO exportDetailDTO : exportDetailList) {
            if (exportDetailDTO.getExportId().equals(exportId)) {
               total += exportDetailDTO.getQuantity();
            }
        }
        exportBUS.setTotalQuantity(exportId, total);
        return total;
    }

    public void renderToTable(DefaultTableModel model, String exportId) {
        model.setRowCount(0);
        loadData();

        for (ExportDetailDTO exportDetailDTO : exportDetailList) {
            if (exportDetailDTO.getExportId().equals(exportId)) {
                model.addRow(new Object[]{
                        exportDetailDTO.getProductId(),
                        productBUS.getNameById(exportDetailDTO.getProductId()),
                        exportDetailDTO.getQuantity()
                });
            }
        }

        model.fireTableDataChanged();
    }
}