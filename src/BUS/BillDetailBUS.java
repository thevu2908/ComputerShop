package BUS;

import DAO.BillDetailDAO;
import DTO.BillDetailDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class BillDetailBUS {
    private ArrayList<BillDetailDTO> billDetailList;
    private BillDetailDAO billDetailDAO;
    private BillBUS billBUS;
    private ProductBUS productBUS;
    private EmployeeBUS employeeBUS;
    private CustomerBUS customerBUS;
    private SaleBUS saleBUS;

    public BillDetailBUS() {
        billDetailDAO = new BillDetailDAO();
        productBUS = new ProductBUS();
        billBUS = new BillBUS();
        employeeBUS = new EmployeeBUS();
        customerBUS = new CustomerBUS();
        saleBUS = new SaleBUS();
    }

    public void loadData() {
        billDetailList = billDetailDAO.getData();
    }

    public boolean addBillDetail(BillDetailDTO billDetailDTO) {
        if (billDetailDAO.addBillDetail(billDetailDTO) > 0) {
           return true;
        } else {
            return false;
        }
    }

    public void printBill(String billId, String path) {
        loadData();
        try {
            Document document = new Document();

            if (!path.endsWith(".pdf")) {
                path = path + ".pdf";
            }

            PdfWriter pdfWriter =  PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            setPDFTitle(document);
            setPDFHeader(document, pdfWriter, billId);
            setPDFInformation(document, billId);
            setPDFProductTable(document, billId);
            setPDFFooter(document, pdfWriter, billId);

            document.close();
            JOptionPane.showMessageDialog(null, "In hóa đơn thành công");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi in hóa đơn", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setPDFFooter(Document document, PdfWriter pdfWriter, String billId) {
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

            int billTmpPrice = billBUS.getBillById(billId).getTotal();
            int billDiscount = billBUS.getBillById(billId).getDiscount();
            int billTotal = billTmpPrice - billDiscount;

            int maxLength = getMaxLength(billTmpPrice, billDiscount, billTotal);
            String format = "%1$" + maxLength + "s";

            Paragraph tmpBox = new Paragraph();
            tmpBox.setAlignment(Element.ALIGN_RIGHT);
            tmpBox.setSpacingBefore(10);

            Chunk tmpPriceTitle = new Chunk("Tạm tính:  ", fontBold);
            Chunk tmpPrice = new Chunk(String.format(format, billTmpPrice) + " VNĐ", font);
            tmpBox.add(tmpPriceTitle);
            tmpBox.add(tmpPrice);

            Paragraph discountBox = new Paragraph();
            discountBox.setAlignment(Element.ALIGN_RIGHT);
            discountBox.setSpacingBefore(5);

            Chunk discountTitle = new Chunk("Giảm giá:   ", fontBold);
            Chunk discount = new Chunk(String.format(format, billDiscount) + " VNĐ", font);
            discountBox.add(discountTitle);
            discountBox.add(discount);

            PdfContentByte content = pdfWriter.getDirectContent();
            content.setLineWidth(1f);
            content.setColorStroke(BaseColor.BLACK);

            content.moveTo(430f, 490f);
            content.lineTo(560f, 490f);
            content.stroke();

            Paragraph totalBox = new Paragraph();
            totalBox.setAlignment(Element.ALIGN_RIGHT);
            totalBox.setSpacingBefore(10);

            Chunk totalTitle = new Chunk("Tổng tiền:  ", fontBold);
            Chunk total = new Chunk(String.format(format, billTotal) + " VNĐ", font);
            totalBox.add(totalTitle);
            totalBox.add(total);

            document.add(tmpBox);
            document.add(discountBox);
            document.add(totalBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPDFProductTable(Document document, String billId) {
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
            for (BillDetailDTO billDetailDTO : billDetailList) {
                if (billDetailDTO.getBillId().equals(billId)) {
                    i++;
                    PdfPCell noCell = new PdfPCell(new Paragraph(i++ + "", font));
                    noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    noCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    noCell.setPadding(10);

                    String name = productBUS.getNameById(billDetailDTO.getProductId());
                    PdfPCell nameCell = new PdfPCell(new Paragraph(name, font));
                    nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    nameCell.setPadding(10);

                    String price = productBUS.getPriceById(billDetailDTO.getProductId()) + "";
                    PdfPCell priceCell = new PdfPCell(new Paragraph(price, font));
                    priceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    priceCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    priceCell.setPadding(10);

                    String quantity = billDetailDTO.getQuantity() + "";
                    PdfPCell quantityCell = new PdfPCell(new Paragraph(quantity, font));
                    quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    quantityCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    quantityCell.setPadding(10);

                    String total = productBUS.getPriceById(billDetailDTO.getProductId()) * billDetailDTO.getQuantity() + "";
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

    public void setPDFInformation(Document document, String billId) {
        try {
            Font font = new Font(
                    BaseFont.createFont("/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                    11,
                    Font.NORMAL
            );

            Paragraph employeeName = new Paragraph(
                    "Họ tên nhân viên lập phiếu: "
                            + employeeBUS.getNameById(billBUS.getBillById(billId).getEmployeeId()).toUpperCase(),
                    font
            );
            employeeName.setSpacingBefore(15);
            employeeName.setSpacingAfter(5);

            Paragraph customerName = new Paragraph(
                    "Họ tên khách hàng: "
                            + customerBUS.getCustomerById(billBUS.getBillById(billId).getCustomerId()).getCustomerName().toUpperCase(),
                    font
            );
            customerName.setSpacingAfter(5);

            Paragraph customerAddress = new Paragraph(
                    "Địa chỉ: "
                            + customerBUS.getCustomerById(billBUS.getBillById(billId).getCustomerId()).getCustomerAddress(),
                    font
            );
            customerAddress.setSpacingAfter(5);
            customerAddress.setFont(font);

            Paragraph customerPhone = new Paragraph(
                    "Số điện thoại: "
                            + customerBUS.getCustomerById(billBUS.getBillById(billId).getCustomerId()).getCustomerPhone(),
                    font
            );
            customerPhone.setSpacingAfter(20);

            document.add(employeeName);
            document.add(customerName);
            document.add(customerAddress);
            document.add(customerPhone);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPDFHeader(Document document, PdfWriter pdfWriter, String billId) {
        try {
            Font font = new Font(
                    BaseFont.createFont("/fonts/ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
                    11,
                    Font.NORMAL
            );

            String[] date = billBUS.getBillById(billId).getBillDate().split("-");
            Paragraph dateString = new Paragraph("Ngày " + date[2] + " tháng " + date[1] + " năm " + date[0], font);
            dateString.setAlignment(Element.ALIGN_CENTER);
            dateString.setSpacingAfter(10);

            Paragraph importIdString = new Paragraph("Mã HD: " + billId, font);
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

            Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(10);

            document.add(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMaxLength(int tmpPrice, int discount, int total) {
        String[] arr = {String.valueOf(tmpPrice), String.valueOf(discount), String.valueOf(total)};
        return Arrays.asList(arr).stream().map(String::length).max(Integer::compareTo).get();
    }

    public ArrayList<BillDetailDTO> getSoldProductListInMonth(String month, int year) {
        loadData();

        ArrayList<String> ListBillIdInMonth = billBUS.getBillListIdInMonth(month, year);
        ArrayList<BillDetailDTO> ListSoldProductInMonth = new ArrayList<>();

        for (BillDetailDTO item : billDetailList) {
            boolean isExistedProduct = ListSoldProductInMonth.stream().anyMatch(
                    billDetail -> billDetail.getProductId().contains(item.getProductId())
            );
            boolean flag = false;

            if (ListBillIdInMonth.contains(item.getBillId())) {
                if (isExistedProduct) {
                    int quantityOfBillDetail = item.getQuantity();
                    int index = ListSoldProductInMonth.indexOf(
                            ListSoldProductInMonth.stream().filter(
                                    billDetail -> billDetail.getProductId().contains(item.getProductId())).findFirst().orElse(null)
                    );
                    ListSoldProductInMonth.get(index).setQuantity(
                            ListSoldProductInMonth.get(index).getQuantity() + quantityOfBillDetail);
                    flag = true;
                }

                if (!flag) {
                    BillDetailDTO billDetailDTO = new BillDetailDTO(item.getProductId(), item.getQuantity());
                    ListSoldProductInMonth.add(billDetailDTO);
                }
            }
        }

        Collections.sort(ListSoldProductInMonth, (x, y) -> (int) (y.getQuantity() - x.getQuantity()));
        return ListSoldProductInMonth;
    }

    public void renderToTable(DefaultTableModel model, ArrayList<BillDetailDTO> list) {
        model.setRowCount(0);

        for (BillDetailDTO billDetailDTO : list) {
            String saleId = productBUS.getProductById(billDetailDTO.getProductId()).getSaleId();
            int discount = saleId == null || saleId.equals("")
                    ? 0
                    : Integer.parseInt(saleBUS.getSaleById(saleId).getSaleInfo().substring(
                            0,
                            saleBUS.getSaleById(saleId).getSaleInfo().length() - 1));

            int price = productBUS.getPriceById(billDetailDTO.getProductId());
            price = price - (price * discount / 100);
            int quantity = billDetailDTO.getQuantity();

            model.addRow(new Object[]{
                    billDetailDTO.getProductId(),
                    productBUS.getNameById(billDetailDTO.getProductId()),
                    price,
                    quantity,
                    price * quantity
            });
        }

        model.fireTableDataChanged();
    }

    public void renderToTable(DefaultTableModel model, String billId) {
        model.setRowCount(0);
        loadData();

        for (BillDetailDTO billDetailDTO : billDetailList) {
            if (billDetailDTO.getBillId().equals(billId)) {
                int price = productBUS.getPriceById(billDetailDTO.getProductId());
                int quantity = billDetailDTO.getQuantity();

                model.addRow(new Object[]{
                        billDetailDTO.getProductId(),
                        productBUS.getNameById(billDetailDTO.getProductId()),
                        price,
                        quantity,
                        price * quantity
                });
            }
        }

        model.fireTableDataChanged();
    }
}