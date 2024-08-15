package Backend.BusinessLayer.objects.Suppliers;

import Backend.DataAccessLayer.SupplierData.OrderDTO;
import Backend.DataAccessLayer.SupplierData.PeriodicOrderDTO;
import Backend.DataAccessLayer.SupplierData.RecordDTO;
import Backend.DataAccessLayer.stockData.ProductDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PeriodicOrder extends Order{
    Integer arrival_day; //DAY OF THE WEEK [0-6] 0 for monday .... 6 for sunday
    //boolean editable;
    //Integer next_arrival;
    //List<Record> nextRecord;

    public PeriodicOrder(int order_id, String supplier_name, int supplier_id, String address, String contact_number,Integer day){
        super(order_id,supplier_name,supplier_id,address,contact_number);
        //this.editable = false;
        //this.nextRecord = new ArrayList<>();
        //this.next_arrival = 0;
        this.arrival_day = day;
    }
    public PeriodicOrder(int order_id, String supplier_name, int supplier_id, String address, String contact_number,List<Record> items,Integer day){
        super(order_id,supplier_name,supplier_id,items,address,contact_number);
       // this.editable = false;
        //this.nextRecord = new ArrayList<>();
        //this.next_arrival = 0;
        this.arrival_day = day;
    }

    public PeriodicOrder(PeriodicOrderDTO pdto,OrderDTO odto,Map<RecordDTO,ProductDTO> recs){
        super(odto);
        super.accept_Records(recs);
        this.arrival_day = pdto.getArrival_day();
    }

    public PeriodicOrder(PeriodicOrder po){
        super(po);
        this.arrival_day = po.get_arrival_day();
    }

    public int get_arrival_day(){return arrival_day;}


    public String Edit_Order(List<Record> next){
        if(isEditable()){
            super.setItem_list((List<Record>) next);
            return "Periodic Order updated successfully";
        }else {
            return "This Order cannot be change/updated at this moment, try again tomorrow!";
        }
    }

    public String add_Record(Record toAdd) throws Exception{
        if(isEditable()){
            super.addRecord(toAdd);
            return "Product added to Order successfully!";
        }else {
            throw new Exception("This Order cannot be change/updated at this moment, try again tomorrow!");
        }
    }
    public String Update_Record(int product_id,int quantity) throws Exception{
        if(isEditable()) {
            super.UpdateRecord(product_id, quantity);
            if (quantity == 0) {
                return "(product) removed from order successfully!";
            }
            return "(product) quantity in order updated successully!";
        }else {
            throw new Exception("This Order cannot be change/updated at this moment, try again tomorrow!");
        }
    }

    public boolean isEditable(){
        //should be changed such that it applies to the concept of manually simulated days counter...
        LocalDate date = LocalDate.now();
        Integer day = date.getDayOfWeek().getValue() + 1;
        if(day.intValue() == arrival_day.intValue()){
            return false;
        }
        return true;
    }

    //Make_Order{
    // updates the items quantities in the inventory and updates the new info about the order if there is any when the delivery time is due...
    // }

    public String Update_arrival(int day){
        if(isEditable()){
            this.arrival_day = day;
            return "Arrival day for the order updated successfully!";
        }else {
            return "This Order cannot be change/updated at this moment, try again tomorrow!";
        }
    }

    @Override
    public boolean isPeriodic(){return true;}

    public PeriodicOrderDTO object2DtoPeriodic(){
        return new PeriodicOrderDTO(getOrder_id(),arrival_day);
    }
    public void clearPerRecords(){
        clearRecords();
       // nextRecord.clear();
    }

    public String setArrival_day(int day){
        this.arrival_day=day;
        return "Arrival day for the order updated successfully!";
    }

}
