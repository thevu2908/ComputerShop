package BUS;

import DAO.ImportDetailDAO;
import DTO.ImportDetailDTO;
import DTO.ProductDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ImportDetailBUS {
    private ArrayList<ImportDetailDTO> importDetailList;
    private ImportDetailDAO importDetailDAO;
    private ProductBUS productBUS;
    private ImportBUS importBUS;
    private EmployeeBUS employeeBUS;
    private SupplierBUS supplierBUS;

    public ImportDetailBUS() {
        importDetailDAO = new ImportDetailDAO();
        productBUS = new ProductBUS();
        importBUS = new ImportBUS();
        employeeBUS = new EmployeeBUS();
        supplierBUS = new SupplierBUS();
    }

    public void loadData() {
        importDetailList = importDetailDAO.getData();
    }

    public boolean addImportDetail(String importId, String productId, String quantity) {
        if (importBUS.getStatusById(importId).equals("Đã duyệt")) {
            JOptionPane.showMessageDialog(null, "Không thể thêm chi tiết phiếu nhập đã được duyệt", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (importId.equals("") || productId.equals("") || quantity.equals("")) {
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

        boolean flag = false;
        int numQuantity = Integer.parseInt(quantity);
        ImportDetailDTO importDetail = getImportDetailById(importId, productId);

        if (importDetail != null) { // case importDetail have already existed in list
            importDetail.setQuantity(importDetail.getQuantity() + numQuantity);
            if (updateImportDetailQuantity(importDetail)) {
                flag = true;
            }
        } else { // case importDetail haven't existed in list
            importDetail = new ImportDetailDTO(importId, productId, numQuantity);
            if (importDetailDAO.addImportDetail(importDetail) > 0) {
                flag = true;
            }
        }

        if (flag) {
            JOptionPane.showMessageDialog(null, "Thêm chi tiết phiếu nhập thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm chi tiết phiếu nhập thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateImportDetailQuantity(ImportDetailDTO importDetail) {
        return importDetailDAO.updateImportDetailQuantity(importDetail) > 0;
    }

    public boolean deleteImportDetail(String importId, String productId) {
        if (importBUS.getStatusById(importId).equals("Đã duyệt")) {
            JOptionPane.showMessageDialog(null, "Không thể xóa chi tiết phiếu nhập đã được duyệt", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (importDetailDAO.deleteImportDetail(importId, productId) > 0) {
            JOptionPane.showMessageDialog(null, "Xóa chi tiết phiếu nhập thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Xóa chi tiết phiếu nhập thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean confirmImport(String importId) {
        ArrayList<ImportDetailDTO> list = getImportDetailsByImportId(importId);

        if (list.size() == 0) {
            JOptionPane.showMessageDialog(null, "Không thể duyệt phiếu nhập chưa có sản phẩm nhập", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (importBUS.confirmImport(importId) && increaseProductStorageQuantity(importId)) {
            JOptionPane.showMessageDialog(null, "Duyệt phiếu nhập thành công");
            return true;
        } else {
            return false;
        }
    }

    public boolean increaseProductStorageQuantity(String importId) {
        loadData();

        for (ImportDetailDTO importDetailDTO : importDetailList) {
            if (importDetailDTO.getImportId().equals(importId)) {
                ProductDTO product = productBUS.getProductStorageById(importDetailDTO.getProductId());

                if (product != null) {
                    if (
                            !productBUS.updateProductStorageQuantity(
                                    importDetailDTO.getProductId(),
                                    product.getProductQuantity() + importDetailDTO.getQuantity()
                            )
                    ) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void printImport(String importId, String path) {
        if (importBUS.getStatusById(importId).equals("Chưa duyệt")) {
            JOptionPane.showMessageDialog(null, "Không thể in phiếu nhập chưa được duyệt", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        loadData();
        try {
            Document document = new Document();

            if (!path.endsWith(".pdf")) {
                path = path + ".pdf";
            }

            PdfWriter pdfWriter =  PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            setPDFTitle(document);
            setPDFHeader(document, pdfWriter, importId);
            setPDFInformation(document, importId);
            setPDFProductTable(document, importId);
            setPDFFooter(document, importId);

            document.close();
            JOptionPane.showMessageDialog(null, "In phiếu nhập thành công");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi in phiếu nhập", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setPDFFooter(Document document, String importId) {
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

            Chunk title = new Chunk("TỔNG TIỀN: ", fontBold);
            Chunk totalPrice = new Chunk(importBUS.getImportById(importId).getImportTotalPrice() + " VNĐ", font);

            totalBox.add(title);
            totalBox.add(totalPrice);

            document.add(totalBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPDFProductTable(Document document, String importId) {
        try {
            Font font = new Font(
                    BaseFont.createFont("/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                    11,
                    Font.NORMAL
            );

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100f);

            float[] columnWidths = {20f, 100f, 40f, 30f, 40f};
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

            PdfPCell priceHeaderCell = new PdfPCell(new Paragraph("Đơn giá", font));
            priceHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            priceHeaderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            priceHeaderCell.setPadding(10);
            table.addCell(priceHeaderCell);

            PdfPCell quantityHeaderCell = new PdfPCell(new Paragraph("Số lượng", font));
            quantityHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            quantityHeaderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            quantityHeaderCell.setPadding(10);
            table.addCell(quantityHeaderCell);

            PdfPCell totalHeaderCell = new PdfPCell(new Paragraph("Thành tiền", font));
            totalHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            totalHeaderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            totalHeaderCell.setPadding(10);
            table.addCell(totalHeaderCell);

            int i = 0;
            for (ImportDetailDTO importDetailDTO : importDetailList) {
                if (importDetailDTO.getImportId().equals(importId)) {
                    i++;
                    PdfPCell noCell = new PdfPCell(new Paragraph(i++ + "", font));
                    noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    noCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    noCell.setPadding(10);

                    String name = productBUS.getNameById(importDetailDTO.getProductId());
                    PdfPCell nameCell = new PdfPCell(new Paragraph(name, font));
                    nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    nameCell.setPadding(10);

                    String price = productBUS.getPriceById(importDetailDTO.getProductId()) + "";
                    PdfPCell priceCell = new PdfPCell(new Paragraph(price, font));
                    priceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    priceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    priceCell.setPadding(10);

                    String quantity = importDetailDTO.getQuantity() + "";
                    PdfPCell quantityCell = new PdfPCell(new Paragraph(quantity, font));
                    quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    quantityCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    quantityCell.setPadding(10);

                    String total = productBUS.getPriceById(importDetailDTO.getProductId()) * importDetailDTO.getQuantity() + "";
                    PdfPCell totalCell = new PdfPCell(new Paragraph(total, font));
                    totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    totalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    totalCell.setPadding(10);

                    table.addCell(noCell);
                    table.addCell(nameCell);
                    table.addCell(priceCell);
                    table.addCell(quantityCell);
                    table.addCell(totalCell);
                }
            }

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPDFInformation(Document document, String importId) {
        try {
            Font font = new Font(
                    BaseFont.createFont("/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                    11,
                    Font.NORMAL
            );

            Paragraph employeeName = new Paragraph(
                    "Họ tên nhân viên lập phiếu: "
                            + employeeBUS.getNameById(importBUS.getImportById(importId).getEmployeeId()).toUpperCase(),
                    font
            );
            employeeName.setSpacingBefore(15);
            employeeName.setSpacingAfter(5);

            Paragraph supplierName = new Paragraph(
                    "Họ tên nhà cung cấp: "
                            + supplierBUS.getNameById(importBUS.getImportById(importId).getSupplierId()).toUpperCase(),
                    font
            );
            supplierName.setSpacingAfter(5);

            Paragraph supplierAddress = new Paragraph(
                    "Địa chỉ: "
                            + supplierBUS.getSupplierById(importBUS.getImportById(importId).getSupplierId()).getSupplierAddress(),
                    font
            );
            supplierAddress.setSpacingAfter(5);
            supplierAddress.setFont(font);

            Paragraph supplierPhone = new Paragraph(
                    "Số điện thoại: "
                            + supplierBUS.getSupplierById(importBUS.getImportById(importId).getSupplierId()).getSupplierPhone(),
                    font
            );
            supplierPhone.setSpacingAfter(20);

            document.add(employeeName);
            document.add(supplierName);
            document.add(supplierAddress);
            document.add(supplierPhone);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPDFHeader(Document document, PdfWriter pdfWriter, String importId) {
        try {
            Font font = new Font(
                    BaseFont.createFont("/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                    11,
                    Font.NORMAL
            );

            String[] date = importBUS.getImportById(importId).getImportDate().split("-");
            Paragraph dateString = new Paragraph("Ngày " + date[2] + " tháng " + date[1] + " năm " + date[0], font);
            dateString.setAlignment(Element.ALIGN_CENTER);
            dateString.setSpacingAfter(10);

            Paragraph importIdString = new Paragraph("Mã PN: " + importId, font);
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

            Paragraph title = new Paragraph("PHIẾU NHẬP HÀNG", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);

            document.add(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ImportDetailDTO> getImportDetailsByImportId(String importId) {
        loadData();
        ArrayList<ImportDetailDTO> list = new ArrayList<>();

        for (ImportDetailDTO importDetailDTO : importDetailList) {
            if (importDetailDTO.getImportId().equals(importId)) {
                list.add(importDetailDTO);
            }
        }

        return list;
    }

    public ImportDetailDTO getImportDetailById(String importId, String productId) {
        loadData();

        for (ImportDetailDTO importDetailDTO : importDetailList) {
            if (importDetailDTO.getImportId().equals(importId) && importDetailDTO.getProductId().equals(productId)) {
                return importDetailDTO;
            }
        }

        return null;
    }

    public int calculateTotalPrice(String importId) {
        int total = 0;
        for (ImportDetailDTO importDetailDTO : importDetailList) {
            if (importDetailDTO.getImportId().equals(importId)) {
                int price = productBUS.getPriceById(importDetailDTO.getProductId());
                total += price * importDetailDTO.getQuantity();
            }
        }
        importBUS.setTotalPrice(importId, total);
        return total;
    }

    public void renderToTable(DefaultTableModel model, String importId) {
        model.setRowCount(0);
        loadData();

        for (ImportDetailDTO importDetailDTO : importDetailList) {
            if (importDetailDTO.getImportId().equals(importId)) {
                int price = productBUS.getPriceById(importDetailDTO.getProductId());
                model.addRow(new Object[]{
                        importDetailDTO.getProductId(),
                        productBUS.getNameById(importDetailDTO.getProductId()),
                        price,
                        importDetailDTO.getQuantity(),
                        price * importDetailDTO.getQuantity()
                });
            }
        }

        model.fireTableDataChanged();
    }
}