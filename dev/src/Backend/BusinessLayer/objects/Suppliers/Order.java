package Backend.BusinessLayer.objects.Suppliers;
import Backend.DataAccessLayer.SupplierData.OrderDTO;
import Backend.DataAccessLayer.SupplierData.RecordDAO;
import Backend.DataAccessLayer.SupplierData.RecordDTO;
import Backend.DataAccessLayer.stockData.ProductDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Order {
    private int till_arrival;
    private int order_id;
    private String supplier_name;
    private int supplier_id;
    private String address;
    private String contact_number;
    private List<Record> item_list;
    private double order_price;

    public Order(int order_id, String supplier_name, int supplier_id, List<Record> item_list,String address, String contact_number) {
        this.till_arrival = 0;
        this.order_id = order_id;
        this.supplier_name = supplier_name;
        this.supplier_id = supplier_id;
        this.item_list = item_list;
        this.address = address;
        this.contact_number = contact_number;
        order_price = 0;
    }
    public Order(int order_id, String supplier_name, int supplier_id, String address, String contact_number ) {
        this.till_arrival = 0;
        this.order_id = order_id;
        this.supplier_name = supplier_name;
        this.supplier_id = supplier_id;
        this.address = address;
        this.contact_number = contact_number;
        item_list = new LinkedList<>();
        order_price = 0;
    }

    public Order(OrderDTO odto){
        this.till_arrival = odto.getTill_arrival();
        this.order_id = odto.getOrder_id();
        this.supplier_name = odto.getSupplier_name();
        this.supplier_id = odto.getSupplier_id();
        this.address = odto.getAddress();
        this.contact_number = odto.getContact_number();
        this.order_price = odto.getOrder_price();
        item_list = new LinkedList<Record>();
    }

    public Order(Order otherOrder) {
        this.till_arrival = otherOrder.till_arrival;
        this.order_id = otherOrder.order_id;
        this.supplier_name = otherOrder.supplier_name;
        this.supplier_id = otherOrder.supplier_id;
        this.address = otherOrder.address;
        this.contact_number = otherOrder.contact_number;
        this.order_price = otherOrder.order_price;
        this.item_list = new LinkedList<>();
        for (Record record : otherOrder.getItem_list()) {
            Record rec = new Record(record);
            this.item_list.add(rec);
        }
    }
    


    public void accept_Records(Map<RecordDTO,ProductDTO> recs){
        for(Entry<RecordDTO, ProductDTO> entry: recs.entrySet()){
            Record curr = new Record(entry.getValue(), entry.getKey());
            item_list.add(curr);
        }
    }





    public int getOrder_id() {
        return order_id;
    }
    public int getSupplier_id(){return supplier_id;}

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public int getSupplier_number() {
        return supplier_id;
    }

    public void setSupplier_number(int supplier_number) {
        this.supplier_id = supplier_number;
    }

    public List<Record> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<Record> item_list) {
        this.item_list = item_list;
    }

//    public LocalDate getOrder_date() {
//        return order_date;
//    }
//
//    public void setOrder_date(LocalDate order_date) {
//        this.order_date = order_date;
//    }

    public void addRecord(Record toAdd){
        item_list.add(toAdd);
    }
    public void UpdateRecord(int product_id,int quantity) throws SQLException{
        for (Record record:item_list){
            if(record.getId() == product_id){
                if(quantity == 0) {
                    item_list.remove(record);
                    RecordDAO.getInstance().delete(order_id, product_id);
                    RecordDAO.getInstance().delete2(order_id, product_id);
                }
                else {
                    record.update_quantity(product_id,quantity);}
            }
        }
    }

    public void set_price(double price){ this.order_price = price;}
//    public String formatted_date(){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        String formattedDate = order_date.format(formatter);
//        return formattedDate;
//    }

    public double base_price(){
        double base = 0;
        for (Record rec:item_list) {
            base = base + rec.getFinal_price();
        }
        return base;
    }

    public int quantities(){
        int base = 0;
        for (Record rec:item_list) {
            base = base + rec.getQuantity();
        }
        return base;
    }


    public double getOrder_price(){
        return order_price;
    }

    public boolean isPeriodic(){return false;}

    public int get_till_arrival(){return till_arrival;}

    public void set_till_arrival(int arrival){this.till_arrival = arrival;}
    
    public OrderDTO object2Dto(int contract_id){
        return new OrderDTO(order_id,supplier_name,supplier_id,address,contact_number,contract_id,order_price,till_arrival);
    }

    public String getAddress() {
        return address;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void clearRecords(){
        item_list.clear();
    }
}

