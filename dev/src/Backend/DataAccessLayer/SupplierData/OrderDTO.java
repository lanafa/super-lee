package Backend.DataAccessLayer.SupplierData;

import Backend.BusinessLayer.objects.Suppliers.Order;
import Backend.BusinessLayer.objects.Suppliers.Record;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDTO {
    private int order_id;
    private String supplier_name;
    private int supplier_id;
    private String address;
    private String contact_number;
   // private List<RecordDTO> item_list;
    private double order_price;
    private int contract_id;
    private int till_arrival;

    public OrderDTO(int order_id, String supplier_name, int supplier_id, String address, String contact_number,int contract_id,int till_arrival) {
        this.order_id = order_id;
        this.supplier_name = supplier_name;
        this.supplier_id = supplier_id;
       // this.item_list = item_list;
        this.address = address;
        this.contact_number = contact_number;
        order_price = 0;
        this.contract_id=contract_id;
        this.till_arrival=till_arrival;
    }

    public OrderDTO(int order_id, String supplier_name, int supplier_id,  String address, String contact_number,int contract_id,double order_price,int till_arrival) {
        this.order_id = order_id;
        this.supplier_name = supplier_name;
        this.supplier_id = supplier_id;
       // this.item_list = item_list;
        this.address = address;
        this.contact_number = contact_number;
        this.order_price =order_price ;
        this.contract_id=contract_id;
        this.till_arrival=till_arrival;
    }

    public OrderDTO(int order_id, String supplier_name, int supplier_id,String address, String contact_number,double order_price,int till_arrival) {
        this.order_id = order_id;
        this.supplier_name = supplier_name;
        this.supplier_id = supplier_id;
       // this.item_list = item_list;
        this.address = address;
        this.contact_number = contact_number;
        this.order_price =order_price ;
        this.till_arrival=till_arrival;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    // public List<RecordDTO> getItem_list() {
    //     return item_list;
    // }

    // public void setItem_list(List<RecordDTO> item_list) {
    //     this.item_list = item_list;
    // }

    public double getOrder_price() {
        return order_price;
    }

    public int getContract_id() {
        return contract_id;
    }

    public int getTill_arrival() {
        return till_arrival;
    }

    public void setTill_arrival(int till_arrival) {
        this.till_arrival = till_arrival;
    }

    // public Order dto2Object() throws SQLException {
    //     List<Record> items =new ArrayList<>();
    //     for (RecordDTO record:item_list){
    //         Record toAdd =record.dto2Object();
    //         items.add(toAdd);
    //     }
    //     return new Order(order_id,supplier_name,supplier_id,items,address,contact_number);
    // }


    //implement
    public Order dto2Object(){
        return null;
    }

}
