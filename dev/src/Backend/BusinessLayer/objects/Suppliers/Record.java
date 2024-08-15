package Backend.BusinessLayer.objects.Suppliers;

import Backend.BusinessLayer.Controllers.Suppliers.ContractController;
import Backend.BusinessLayer.Controllers.Suppliers.OrdersController;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.DataAccessLayer.SupplierData.RecordDTO;
import Backend.DataAccessLayer.stockData.ProductDTO;

public class Record extends Product{
    private int order_id;
    private int supplier_id;
    private int quantity;
    private double ovl_price;
    private double discount;
    private double final_price;

    public Record(Product product,int supplier_id,int order_id, int quantity,double discount, double final_price) {
        super(product);
        this.order_id = order_id;
        this.ovl_price = (ContractController.getInstance().supp_contracts.get(supplier_id).search_price(product.getId())*quantity);
        this.supplier_id = supplier_id;
        this.quantity = quantity;
        this.discount = discount;
        this.final_price = final_price;
   }
   public Record(Record rec){
       super(rec.getId(),rec.getName(),rec.getMinAmount(),rec.getSellingPrice(),rec.getManufacturePrice(),rec.getShelfNumber(),rec.getManufacturer());
       this.order_id = rec.order_id;
       this.supplier_id = rec.supplier_id;
       this.quantity = rec.quantity;
       this.ovl_price = rec.ovl_price;
       this.discount = rec.discount;
       this.final_price = rec.final_price;
   }

   public Record(ProductDTO pdto, RecordDTO rdto){
       super(pdto.dto2Object());
       this.order_id = rdto.getOrder_id();
       this.supplier_id = rdto.getSupplier_id();
       this.quantity = rdto.getQuantity();
       this.ovl_price = rdto.getOvl_price();
       this.discount = rdto.getDiscount();
       this.final_price = rdto.getFinal_price();
   }


    public double getFinal_price(){
        return final_price;
    }
    public int getQuantity(){
        return quantity;
    }
    public double getOvl_price(){
        return this.ovl_price;
    }
    public void update_quantity(int product_id,int quantity){
        this.quantity = quantity;
        Contract cont=OrdersController.getInstance().suppliers_contracts.get(supplier_id);
        int quantity_can=cont.quant(product_id, quantity);
        this.discount=cont.get_margin_price(product_id,quantity_can);
        this.final_price=cont.getItemPrice(product_id, quantity_can);
        this.ovl_price = cont.search_price(product_id) * quantity;
    }
    public int getSupplier_id(){return supplier_id;}
    public int getOrder_id(){return order_id;}

    public RecordDTO object2Dto(){
        return new RecordDTO(supplier_id,order_id,quantity,discount,final_price,getId(),ovl_price);
    }

    public int getProduct_id(){
        return this.getId();
    }

}
