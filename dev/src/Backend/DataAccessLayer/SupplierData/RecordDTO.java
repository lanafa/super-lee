package Backend.DataAccessLayer.SupplierData;

import Backend.BusinessLayer.objects.Stock.Product;
import Backend.BusinessLayer.objects.Suppliers.Record;
import Backend.DataAccessLayer.stockData.ProductDAO;

import java.sql.SQLException;

public class RecordDTO {
    private int order_id;
    private int supplier_id;
    private int quantity;
    private double ovl_price;
    private double discount;
    private double final_price;
    private int product_id;

    private ProductDAO productDAO=ProductDAO.getInstance();

    public RecordDTO(Product product, int supplier_id, int order_id, int quantity, double discount, double final_price) {
        this.order_id = order_id;
        this.ovl_price = (product.getManufacturePrice() * quantity);
        this.supplier_id = supplier_id;
        this.quantity = quantity;
        this.discount = discount;
        this.final_price = final_price;
        this.product_id=product.getId();
    }

    public RecordDTO(int supplier_id, int order_id, int quantity, double discount, double final_price,int product_id,double ovl_price) {
        this.order_id = order_id;
        this.ovl_price = ovl_price;
        this.supplier_id = supplier_id;
        this.quantity = quantity;
        this.discount = discount;
        this.final_price = final_price;
        this.product_id=product_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getOvl_price() {
        return ovl_price;
    }

    public void setOvl_price(double ovl_price) {
        this.ovl_price = ovl_price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getFinal_price() {
        return final_price;
    }
    public int getProduct_id() {
        return product_id;
    }

    // public Record dto2Object() throws SQLException {
    //     Product p=productDAO.getProductById(product_id).dto2Object();
    //     return new Record(p,supplier_id,order_id,quantity,discount,final_price);
    // }

    public Record dto2Object(){
        return null;
    }

}
