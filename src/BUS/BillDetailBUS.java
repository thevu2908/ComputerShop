package BUS;

import DAO.BillDetailDAO;
import DTO.BillDTO;
import DTO.BillDetailDTO;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BillDetailBUS {
    private ArrayList<BillDetailDTO> billDetailList;
    private ProductBUS productBUS;
    private BillDetailDAO billDetailDAO;
    private BillBUS billBUS;

    public BillDetailBUS() {
        productBUS = new ProductBUS();
        billDetailDAO = new BillDetailDAO();
        billDetailList = new ArrayList<>();
        billBUS = new BillBUS();
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

    public void renderToTable(DefaultTableModel model, ArrayList<BillDetailDTO> list) {
        model.setRowCount(0);

        for (BillDetailDTO billDetailDTO : list) {
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



    public ArrayList<BillDetailDTO> getListSoldProductInMonth() {
        loadData();
        ArrayList<String> ListBillIdInMonth = billBUS.getListBillIdInMonth(); // biến chứa danh sách các hóa đơn trong tháng hiện tại
        ArrayList<BillDetailDTO> ListSoldProductInMonth = new ArrayList<>(); // biến chứa danh sách sản phẩm bán được trong tháng hiện tại
        for(BillDetailDTO item : billDetailList) {
            boolean isExistedProduct = ListSoldProductInMonth.stream().anyMatch( billDetail -> billDetail.getProductId().contains(item.getProductId()));
            boolean flag = false;
            if(ListBillIdInMonth.contains(item.getBillId())) { // kiểm tra mã hóa đơn của chi tiết hóa đơn có chứa trong danh sách các hóa đơn tháng hiện tại

                if(isExistedProduct){ // kiểm tra đã tồn tại sản phẩm của chi tiết hóa đơn trong danh sách sản phẩm bán được trong tháng hiện tại chưa
                    int quantityOfBillDetail = item.getQuantity(); // số lượng của chi tiết hóa đơn
                    int index = ListSoldProductInMonth.indexOf(ListSoldProductInMonth.stream().filter(billDetail -> billDetail.getProductId().contains(item.getProductId())).findFirst().orElse(null));
                    ListSoldProductInMonth.get(index).setQuantity(ListSoldProductInMonth.get(index).getQuantity() + quantityOfBillDetail);
                    flag = true;
                }

                if(!flag){ // nếu flag là false tức là sản phẩm trong chi tiết hóa đơn chưa tồn tại trong danh sách sản phẩm bán được trong tháng hiện tại thì tạo mới và thêm vào danh sách sản phẩm trong tháng hiện tại
                    BillDetailDTO billDetailDTO = new BillDetailDTO(item.getProductId(), item.getQuantity());
                    ListSoldProductInMonth.add(billDetailDTO);
                }

            }
        }
        Comparator<BillDetailDTO> quantityComparator = (e1, e2) -> Integer.compare(e1.getQuantity(), e2.getQuantity());
        Collections.sort(ListSoldProductInMonth, quantityComparator);
        Collections.reverse(ListSoldProductInMonth);
        return ListSoldProductInMonth;
    }

//    public static void main(String[] args) {
//        BillDetailBUS billDetailBUS = new BillDetailBUS();
//        System.out.println(billDetailBUS.getListSoldProductInMonth().size());
//        for(BillDetailDTO billDetailDTO : billDetailBUS.getListSoldProductInMonth()){
//            System.out.println(String.format("Sản phẩm: %s    số lương:  %d",billDetailDTO.getProductId(),billDetailDTO.getQuantity()));
//        }
//
//    }

}