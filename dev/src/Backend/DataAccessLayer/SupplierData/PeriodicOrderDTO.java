package Backend.DataAccessLayer.SupplierData;

import Backend.BusinessLayer.objects.Suppliers.Order;
import Backend.BusinessLayer.objects.Suppliers.PeriodicOrder;
import Backend.BusinessLayer.objects.Suppliers.Record;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PeriodicOrderDTO {
    private Integer arrival_day; // DAY OF THE WEEK [0-6] 0 for Monday .... 6 for Sunday
    private int order_id;

    
    // private String supplier_name;
    // private int supplier_id;
    // private String address;
    // private String contact_number;
    // private double order_price;
    // private int contract_id;

    private OrderDAO orderDAO=OrderDAO.getInstance();

    public PeriodicOrderDTO(int order_id, Integer day) {
        this.order_id=order_id;
        this.arrival_day = day;

    }
    // public PeriodicOrderDTO(int order_id, Integer day,boolean editable,List<RecordDTO> recordDTOS ,Integer next_arrival){
    //     this.order_id=order_id;
    //     this.arrival_day = day;
    // }

    // public PeriodicOrderDTO(int order_id, Integer day,boolean editable,List<RecordDTO> recordDTOS ,Integer next_arrival, String supplier_name , int supplier_id,String address,String contact_number,double order_price,int contract_id) {
    //     this.order_id=order_id;
    //     this.arrival_day = day;
    //     // this.supplier_name = supplier_name;
    //     // this.supplier_id = supplier_id;
    //     // this.address = address;
    //     // this.contact_number = contact_number;
    //     // this.order_price = order_price;
    //     // this.contract_id = contract_id;
        
    // }

    public Integer getArrival_day() {
        return arrival_day;
    }

    public void setArrival_day(Integer arrival_day) {
        this.arrival_day = arrival_day;
    }

    // public boolean isEditable() {
    //     return editable;
    // }

    // public void setEditable(boolean editable) {
    //     this.editable = editable;
    // }

    // public Integer getNext_arrival() {
    //     return next_arrival;
    // }

    // public void setNext_arrival(Integer next_arrival) {
    //     this.next_arrival = next_arrival;
    // }

    // public List<RecordDTO> getNextRecord() {
    //     return nextRecord;
    // }

    // public void setNextRecord(List<RecordDTO> nextRecord) {
    //     this.nextRecord = nextRecord;
    // }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    // public PeriodicOrder dto2ObjectPeriodic() throws SQLException {
    //     // List<Record> nextRec=new ArrayList<>();
    //     // for (RecordDTO rec:nextRecord){
    //     //     nextRec.add(rec.dto2Object());
    //     // }
    //     Order order= orderDAO.getById(order_id).dto2Object();
    //     return new PeriodicOrder(order_id,order.getSupplier_name(), order.getSupplier_id(), order.getAddress(), order.getContact_number(),order.getItem_list(),arrival_day);
    // }

    public PeriodicOrder dto2ObjectPeriodic(){
        return null;
    }


    // public String getSupplier_name() {
    //     return supplier_name;
    // }
    
    // public int getSupplier_id() {
    //     return supplier_id;
    // }
    
    // public String getAddress() {
    //     return address;
    // }
    
    // public String getContact_number() {
    //     return contact_number;
    // }
    
    // public double getOrder_price() {
    //     return order_price;
    // }
    
    // public int getContract_id() {
    //     return contract_id;
    // }
    










}
