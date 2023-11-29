package BUS;

import DAO.ProductDAO;
import DTO.ProductDTO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import validation.Validate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ProductBUS {
    private ProductDAO productDAO;
    private ProductTypeBUS productTypeBUS;
    private SaleBUS saleBUS;
    private ArrayList<ProductDTO> productList;
    private ArrayList<ProductDTO> productStorageList;

    public ProductBUS() {
        productDAO = new ProductDAO();
        productTypeBUS = new ProductTypeBUS();
        saleBUS = new SaleBUS();
    }

    public void loadProductData() {
        productList = productDAO.getData();
    }

    public void loadProductStorageData() {
        productStorageList = productDAO.getStorageData();
    }

    public boolean addProduct(String id, String name, String type, String price, String cpu, String ram, String storage,
                          String screen, String screenCard) {
        if (id.equals("") || name.equals("") ||type.equals("") || price.equals("") || cpu.equals("") || ram.equals("")
                || storage.equals("") || screen.equals("") || screenCard.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkExistedProductId(id)){
            JOptionPane.showMessageDialog(null, "Mã sản phẩm đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidNumber(price, "Giá")) {
            return false;
        }

        int intPrice = Integer.parseInt(price);
        String typeId = productTypeBUS.getIdByName(type);
        String saleId = saleBUS.findApplyingSale() == null ? null : saleBUS.findApplyingSale().getSaleId();
        ProductDTO product = new ProductDTO(id, typeId, saleId, name, intPrice, cpu, ram, storage, screen, screenCard, 0, 0);

        if (productDAO.addProduct(product) > 0) {
            JOptionPane.showMessageDialog(null, "Thêm sản phẩm thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Thêm sản phẩm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteProduct(String id) {
        int choice = JOptionPane.showConfirmDialog(null, "Bạn có muốn xoá sản phẩm " + id + " không ?", "Câu hỏi",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            if (productDAO.deleteProduct(id) > 0) {
                JOptionPane.showMessageDialog(null, "Xoá sản phẩm thành công");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Xoá sản phẩm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return false;
    }

    public boolean updateProduct(String id, String name, String type, String price, String cpu, String ram, String storage,
                              String screen, String screenCard) {
        if (id.equals("")){
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm muốn sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (name.equals("") ||type.equals("") || price.equals("") || cpu.equals("") || ram.equals("")
                || storage.equals("") || screen.equals("") || screenCard.equals("")) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập đầy đủ thông tin", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!checkExistedProductId(id)) {
            JOptionPane.showMessageDialog(null, "Không tồn tại mã sản phẩm này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!Validate.isValidNumber(price, "Giá")) {
            return false;
        }

        int intPrice = Integer.parseInt(price);
        String typeId = productTypeBUS.getIdByName(type);
        String saleId = saleBUS.findApplyingSale() == null ? null : saleBUS.findApplyingSale().getSaleId();
        ProductDTO product = new ProductDTO(id, typeId, saleId, name, intPrice, cpu, ram, storage, screen, screenCard, 0, 0);

        if (productDAO.updateProduct(product) > 0) {
            JOptionPane.showMessageDialog(null, "Sửa thông tin sản phẩm thành công");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Sửa thông tin sản phẩm thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateProductQuantity(String productId, int quantity) {
        if (productDAO.updateProductQuantity(productId, quantity) > 0) {
            return true;
        }
        return false;
    }

    public boolean applySale(String saleId) {
        loadProductData();

        if (saleBUS.applySale(saleId)) {
            for (ProductDTO productDTO : productList) {
                if (productDAO.applySale(productDTO.getProductId(), saleId) <= 0) {
                    JOptionPane.showMessageDialog(null, "Áp dụng chương trình khuyến mãi thất bại", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            JOptionPane.showMessageDialog(null, "Áp dụng chương trình khuyến mãi thành công");
            return true;
        }

        return false;
    }

    public boolean stopApplySale(String saleId) {
        loadProductData();

        if (saleBUS.stopApplySale(saleId)) {
            for (ProductDTO productDTO : productList) {
                if (productDAO.stopApplySale(productDTO.getProductId()) <= 0) {
                    JOptionPane.showMessageDialog(null, "Ngưng áp dụng chương trình khuyến mãi thất bại", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            JOptionPane.showMessageDialog(null, "Ngưng áp dụng chương trình khuyến mãi thành công");
            return true;
        }

        return false;
    }

    public void autoStopApplySale() {
        loadProductData();

        for (ProductDTO productDTO : productList) {
            productDAO.stopApplySale(productDTO.getProductId());
        }
    }

    public boolean addProductStorage(String productId, int quantity) {
        ProductDTO product = new ProductDTO(productId, quantity);

        if (productDAO.addPProductStorage(product) > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateProductStorageQuantity(String productId, int quantity) {
        if (productDAO.updateProductStorageQuantity(productId, quantity) > 0) {
            return true;
        }
        return false;
    }

    public boolean checkExistedProductId(String productID) {
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getIsDeleted() == 0 && productDTO.getProductId().equals(productID)) {
                return true;
            }
        }

        return false;
    }

    public void exportExcel(File path) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet("Sản phẩm");
        sheet.trackAllColumnsForAutoSizing();

        int rowIndex = 0;

        writeExcelTitle(sheet, rowIndex);

        loadProductData();
        for (ProductDTO productDTO : productList) {
            if (productDTO.getIsDeleted() == 0) {
                rowIndex++;
                SXSSFRow row = sheet.createRow(rowIndex);
                writeExcelData(productDTO, row);
            }
        }

        autoResizeColumn(sheet, 11);

        if(writeExcel(workbook, path)) {
            JOptionPane.showMessageDialog(null, "Xuất danh sách sản phẩm thành file excel thành công");
        } else {
            JOptionPane.showMessageDialog(null, "Xuất danh sách sản phẩm thành file excel thất bại", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean writeExcel(SXSSFWorkbook workbook, File path) {
        try {
            String fileName = path.getName();
            if (!fileName.endsWith(".xlsx")) {
                path = new File(path.getParentFile(), fileName + ".xlsx");
            }

            FileOutputStream fos = new FileOutputStream(path.toString());
            workbook.write(fos);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void writeExcelData(ProductDTO productDTO, SXSSFRow row) {
        SXSSFCell cell = row.createCell(0);
        cell.setCellValue(productDTO.getProductId());

        cell = row.createCell(1);
        cell.setCellValue(productDTO.getProductName());

        cell = row.createCell(2);
        cell.setCellValue(productDTO.getProductType());

        cell = row.createCell(3);
        cell.setCellValue(productDTO.getProductPrice());

        cell = row.createCell(4);
        cell.setCellValue(productDTO.getProductCPU());

        cell = row.createCell(5);
        cell.setCellValue(productDTO.getProductRAM());

        cell = row.createCell(6);
        cell.setCellValue(productDTO.getProductStorage());

        cell = row.createCell(7);
        cell.setCellValue(productDTO.getProductScreen());

        cell = row.createCell(8);
        cell.setCellValue(productDTO.getProductScreenCard());
    }

    public void writeExcelTitle(SXSSFSheet sheet, int rowIndex) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);

        SXSSFRow row = sheet.createRow(rowIndex);

        SXSSFCell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Mã sản phẩm");

        cell = row.createCell(1);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Tên sản phẩm");

        cell = row.createCell(2);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Hãng");

        cell = row.createCell(3);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Giá");

        cell = row.createCell(4);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("CPU");

        cell = row.createCell(5);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("RAM");

        cell = row.createCell(6);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Ổ cứng");

        cell = row.createCell(7);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Màn hình");

        cell = row.createCell(8);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Card màn hình");
    }

    public void autoResizeColumn(SXSSFSheet sheet, int columns) {
        for (int i = 0; i < columns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public boolean importExcel(String path) {
        int cnt = 0;

        try {
            FileInputStream fis = new FileInputStream(path);

            Workbook workbook = getWorkBook(fis, path);
            if (workbook == null) {
                return false;
            }

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> iterator = sheet.iterator();
            while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                if (nextRow.getRowNum() == 0) {
                    continue;
                }

                ProductDTO product = new ProductDTO();
                String id = createNewProductID();
                product.setProductId(id);

                Iterator<Cell> cellIterator = nextRow.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    Object cellValue = getCellValue(cell);

                    switch (cell.getColumnIndex()) {
                        case 0:
                            product.setProductName(cellValue.toString());
                            break;
                        case 1:
                            product.setProductType(cellValue.toString());
                            break;
                        case 2:
                            product.setProductPrice(Integer.parseInt(cellValue.toString()));
                            break;
                        case 3:
                            product.setProductCPU(cellValue.toString());
                            break;
                        case 4:
                            product.setProductRAM(cellValue.toString());
                            break;
                        case 5:
                            product.setProductStorage(cellValue.toString());
                            break;
                        case 6:
                            product.setProductScreen(cellValue.toString());
                            break;
                        case 7:
                            product.setProductScreenCard(cellValue.toString());
                            break;
                        default:
                            break;
                    }
                }

                if (productDAO.addProduct(product) > 0) {
                    cnt++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cnt > 0) {
            JOptionPane.showMessageDialog(null, "Đã import " + cnt + " sản phẩm");
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Import excel thất bại \nHãy kiểm tra lại thông tin các sản phẩm trong file",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public Object getCellValue(Cell cell) {
        Object cellValue = null;
        CellType cellType = cell.getCellType();

        switch (cellType) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                DataFormatter dataFormatter = new DataFormatter();
                cellValue = dataFormatter.formatCellValue(cell);
                break;
            case _NONE:
            case BLANK:
                cellValue = "";
                break;
            default:
                break;
        }

        return cellValue;
    }

    public Workbook getWorkBook(FileInputStream fis, String path) throws IOException {
        Workbook workbook = null;

        if (path.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(fis);
        } else if (path.endsWith("xls")) {
            workbook = new HSSFWorkbook(fis);
        } else {
            JOptionPane.showMessageDialog(null, "File excel không hợp lệ (.xlsx)", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        return workbook;
    }

    public String createNewProductID() {
        loadProductData();
        int id = productList.size() + 1;
        return "SP" + String.format("%03d", id);
    }

    public ProductDTO getProductStorageById(String productID) {
        loadProductStorageData();

        for (ProductDTO productDTO : productStorageList){
            if (productDTO.getIsDeleted() == 0 && productDTO.getProductId().equals(productID)){
                return productDTO;
            }
        }

        return null;
    }

    public ProductDTO getProductById(String productID) {
        loadProductData();

        for (ProductDTO productDTO : productList){
            if (productDTO.getIsDeleted() == 0 && productDTO.getProductId().equals(productID)){
                return productDTO;
            }
        }
        return null;
    }

    public String getNameById(String id) {
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getIsDeleted() == 0 && productDTO.getProductId().equals(id)) {
                return productDTO.getProductName();
            }
        }

        return "";
    }

    public int getPriceById(String id) {
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getIsDeleted() == 0 && productDTO.getProductId().equals(id)) {
                return productDTO.getProductPrice();
            }
        }

        return 0;
    }

    public int getIsDeletedById(String id) {
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getProductId().equals(id)) {
                return productDTO.getIsDeleted();
            }
        }

        return 0;
    }

    public String getTypeNameById(String id) {
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getIsDeleted() == 0 && productDTO.getProductId().equals(id)) {
                return productDTO.getProductType();
            }
        }

        return "";
    }

    public int getProductStorageQuantityById(String productId) {
        loadProductStorageData();

        for (ProductDTO productDTO : productStorageList) {
            if (productDTO.getIsDeleted() == 0 && productDTO.getProductId().equals(productId)) {
                return productDTO.getProductQuantity();
            }
        }

        return 0;
    }

    public ArrayList<String> initProductStorageIdSuggestion(int col) {
        loadProductStorageData();
        ArrayList<String> list = new ArrayList<>();

        for (ProductDTO productDTO : productStorageList) {
            if (getIsDeletedById(productDTO.getProductId()) == 0 && productDTO.getProductQuantity() > 0) {
                list.add(productDTO.getProductId());
            }
        }

        return list;
    }

    public ArrayList<String> initProductIdSuggestion(int col) {
        loadProductData();;
        ArrayList<String> list = new ArrayList<>();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getIsDeleted() == 0) {
                list.add(productDTO.getProductId());
            }
        }

        return list;
    }

    public void renderToSellTable(DefaultTableModel model, ArrayList<ProductDTO> list) {
        model.setRowCount(0);
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getIsDeleted() == 0) {
                int quantity = productDTO.getProductQuantity();

                if (list != null) {
                    for (ProductDTO product : list) {
                        if (productDTO.getProductId().equals(product.getProductId())) {
                            quantity -= product.getProductQuantity();
                        }
                    }
                }

                model.addRow(new Object[]{
                        productDTO.getProductId(),
                        productDTO.getProductName(),
                        productTypeBUS.getNameById(productDTO.getProductType()),
                        productDTO.getProductPrice(),
                        saleBUS.getSaleById(productDTO.getSaleId()) == null
                                ? ""
                                : "Giảm " + saleBUS.getSaleById(productDTO.getSaleId()).getSaleInfo(),
                        quantity
                });
            }
        }

        model.fireTableDataChanged();
    }

    public void renderToProductTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadProductData();

        for (ProductDTO productDTO : productList) {
            if (productDTO.getIsDeleted() == 0) {
                model.addRow(new Object[]{
                        productDTO.getProductId(),
                        productDTO.getProductName(),
                        productTypeBUS.getNameById(productDTO.getProductType()),
                        productDTO.getProductPrice(),
                });
            }
        }

        model.fireTableDataChanged();
    }

    public void renderToStorageProductTable(DefaultTableModel model) {
        model.setRowCount(0);
        loadProductStorageData();

        for (ProductDTO productDTO : productStorageList) {
            if (getIsDeletedById(productDTO.getProductId()) == 0) {
                model.addRow(new Object[]{
                        productDTO.getProductId(),
                        getNameById(productDTO.getProductId()),
                        productTypeBUS.getNameById(getTypeNameById(productDTO.getProductId())),
                        productDTO.getProductQuantity()
                });
            }
        }

        model.fireTableDataChanged();
    }
}