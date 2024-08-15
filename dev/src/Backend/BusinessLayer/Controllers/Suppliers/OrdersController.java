package Backend.BusinessLayer.Controllers.Suppliers;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.Controllers.Stock.productscontroller;
import Backend.BusinessLayer.objects.Suppliers.*;
import Backend.BusinessLayer.objects.Stock.*;
import Backend.BusinessLayer.objects.Suppliers.Record;
import Backend.DataAccessLayer.SupplierData.OrderDTO;
import Backend.DataAccessLayer.SupplierData.PeriodicOrderDTO;
import Backend.DataAccessLayer.SupplierData.RecordDAO;
import Backend.DataAccessLayer.SupplierData.RecordDTO;
import Backend.DataAccessLayer.stockData.ProductDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class OrdersController {
    private static OrdersController instance = null;
    private int current_day;
    

    public Map<Integer, Order> orders;// Order Id ====> shortage Order
    public Map<Integer,PeriodicOrder> periodic_orders; // Order Id ====> periodic Order
    // these two maps need to be updated in the other controllers when adding new data
    private Map<Integer, List<SupplierCard>> item_suppliers; //item_id --> supps

    public Map<Integer, Contract> suppliers_contracts; // supp_id --> contracts
    int order_counter;

    DataBaseController dataBaseController=DataBaseController.getInstance();
    private OrdersController() {
        orders = new HashMap<>();
        this.item_suppliers = new HashMap<Integer,List<SupplierCard>>();
        this.suppliers_contracts = new HashMap<Integer, Contract>();
        order_counter = 0;
        this.periodic_orders = new HashMap<Integer,PeriodicOrder>();
        LocalDate date = LocalDate.now();
        current_day = date.getDayOfWeek().getValue();

        //ORDER COUNTER DO ID'S SHOULD BE HANDLED WHEN DATA BASE IS ESTABLISHED
    }
    //load data{
        //datacontroller.loaddata()
    //}
    public void increment_day()throws SQLException{
        if(current_day == 6){
            current_day = 0;
        }else {
            current_day++;
        }
        runThrough_Day();
    }

    private void runThrough_Day() throws SQLException {
        Map<Product,Integer> minrep =  productscontroller.getInstance().runThrough_Day();
        if(minrep.size()>0){
            PrepareOrders(minrep);
        }
        for (PeriodicOrder per_order : periodic_orders.values()) {
            if(per_order.get_arrival_day() == this.current_day){
                try{
                productscontroller.getInstance().receive_Order(per_order.getItem_list());
                }catch(Exception ex){
                    System.out.println("\n\n");
                    System.out.println(ex.getMessage());
                }
                dataBaseController.insert_to_order_history(suppliers_contracts.get(per_order.getSupplier_id()).getContract_id(),per_order.getOrder_id(),1);
            }
        }
        for (Order order : orders.values()) {
            order.set_till_arrival(order.get_till_arrival() - 1);
            if(order.get_till_arrival() == 0){
                try{
                    productscontroller.getInstance().receive_Order(order.getItem_list());
                    }catch(Exception ex){
                        System.out.println(ex.getMessage());
                    }
               // dataBaseController.addOrder(order.object2Dto(suppliers_contracts.get(order.getSupplier_id()).getContract_id()));
                //make copy of order and use Dao.insert() ==> (to history and contract's order history...) and delete from database
                orders.remove(order.getOrder_id());
                dataBaseController.insert_to_order_history(suppliers_contracts.get(order.getSupplier_id()).getContract_id(),order.getOrder_id(),0);
            }
        }

    }

    public static OrdersController getInstance() {
        if (instance == null) {
            instance = new OrdersController();
        }
        return instance;
    }
    public int item_suppSize(int item_id){
        return item_suppliers.get(item_id).size();
    }
    public int cont_size(){
        return suppliers_contracts.size();
    }
    public void remove_contract(int supp_id){
        suppliers_contracts.remove(supp_id);
    }

    public void remove_item_supplier(int item_id,int supp_id){
        List<SupplierCard> supps = item_suppliers.get(item_id);
        for (SupplierCard supp: supps){
            if(supp.getSupplier_id() == supp_id){
                item_suppliers.get(item_id).remove(supp);
            }
        }
    }

    public void put_suppCont(int id, Contract contract){
        suppliers_contracts.put(id,contract);
    }


    public String PrepareOrders(Map<Product,Integer> items)throws SQLException {//map could be changed its key to be item_id
        Map<Integer, Order> curr_orders = new HashMap<>();
        for (Product item: items.keySet()) {
            List<SupplierCard> supps = item_suppliers.get(item.getId());
            //here should be added another comp that sorts depending on arrival time of suppliers
            Comparator<SupplierCard> comp_price = new Comparator<SupplierCard>() {
                @Override
                public int compare(SupplierCard o1, SupplierCard o2) {
                    Contract o_1 = suppliers_contracts.get(o1.getSupplier_id());
                    Contract o_2 = suppliers_contracts.get(o2.getSupplier_id());
                    return Double.compare(o_1.getItemPrice(item.getId(),items.get(item)),o_2.getItemPrice(item.getId(),items.get(item)));
                }
            };
            Comparator<SupplierCard> comp_days = new Comparator<SupplierCard>() {
                @Override
                public int compare(SupplierCard o1, SupplierCard o2) {
                    Contract o_1 = suppliers_contracts.get(o1.getSupplier_id());
                    Contract o_2 = suppliers_contracts.get(o2.getSupplier_id());
                    return Integer.compare(o_1.needed_days(), o_2.needed_days());
                }
            };
            //implement so it compares using the days to deliver order for current day (function in suppliersCard or contracts that returns how many days left)

            Collections.sort(supps,comp_days.thenComparing(comp_price));//sorted by item price after discount depending on the regarding contracts
            int idx = whole_idx(supps,item.getId(),items.get(item));
            if(idx != -1){
                SupplierCard curr_supp = supps.get(idx);
                Contract curr_contract = suppliers_contracts.get(curr_supp.getSupplier_id());
                if(!curr_orders.containsKey(curr_supp.getSupplier_id())) {
                    //LocalDate date = LocalDate.now();
                    Order prep = new Order(order_counter++, curr_supp.getSupplier_name(), curr_supp.getSupplier_id(), "Super_lee", curr_supp.spit_contact().getPhone_number());
                    curr_orders.put(curr_supp.getSupplier_id(), prep);
                }
                int item_id = item.getId();
                int quantity = items.get(item);
                int order_id = curr_orders.get(curr_supp.getSupplier_id()).getOrder_id();
                Record toAdd = new Record(item,curr_supp.getSupplier_id(),order_id,quantity,curr_contract.get_margin_price(item_id,quantity),curr_contract.getItemPrice(item_id,quantity));
                curr_orders.get(curr_supp.getSupplier_id()).addRecord(toAdd);
            }
            else{
                int suppIdx = 0;
                int quantity = items.get(item);
                while(quantity > 0){
                    SupplierCard curr_supp = supps.get(suppIdx);
                    Contract cont = suppliers_contracts.get(curr_supp.getSupplier_id());
                    if(!curr_orders.containsKey(curr_supp.getSupplier_id())) {
                        //LocalDate date = LocalDate.now();
                        Order prep = new Order(order_counter++, curr_supp.getSupplier_name(), curr_supp.getSupplier_id(), "Super_lee", curr_supp.spit_contact().getPhone_number());
                        curr_orders.put(curr_supp.getSupplier_id(), prep);
                    }
                    int quan_could = cont.quant(item.getId(),quantity);
                    int order_id = curr_orders.get(curr_supp.getSupplier_id()).getOrder_id();
                    Record toAdd = new Record(item,curr_supp.getSupplier_id(),order_id,quan_could,cont.get_margin_price(item.getId(),quan_could),cont.getItemPrice(item.getId(),quan_could));
                    quantity = quantity - quan_could;
                    curr_orders.get(curr_supp.getSupplier_id()).addRecord(toAdd);
                    suppIdx++;
                }
            }
        }
        handle_orders(curr_orders);
        
        return "Order made for -out of stock- items successfully!";

    }


    private int whole_idx(List<SupplierCard> supps, int item_id ,int quantity){
        int idx = -1;
        for (SupplierCard sup:supps) {
            Contract curr =suppliers_contracts.get(sup.getSupplier_id());
            if(curr.can_Whole(item_id,quantity)){
                idx = idx + 1;
                return idx;
            }
            idx = idx+1;
        }
        idx = -1;
        return idx;
    }

    private void handle_orders(Map<Integer, Order> orders)throws SQLException{
        List<Integer> keys = new ArrayList<>(orders.keySet());
        for (Integer k: keys){
            Contract curr_cont = suppliers_contracts.get(k);
            Order curr_order = orders.get(k);
            curr_order.set_till_arrival(curr_cont.needed_days());
            double price = curr_cont.calculate_overall(curr_order.quantities(),curr_order.base_price());
            curr_order.set_price(price);
            OrderDTO value = curr_order.object2Dto(curr_cont.getContract_id());
            dataBaseController.addOrder(value);
            List<Record> recs = curr_order.getItem_list();
            for (Record rec : recs) {
                dataBaseController.add_record(rec.object2Dto());
            }
            this.orders.put(curr_order.getOrder_id(),curr_order);
            curr_cont.archive_order(curr_order);
            dataBaseController.insert_to_order_history(curr_cont.getContract_id(),curr_order.getOrder_id(),0);
        }

    }


    public void fill_suppitems(List<Product> items, SupplierCard supplier){
        for (Product item:items) {
            if(!item_suppliers.containsKey(item.getId()))
                item_suppliers.put(item.getId(),new LinkedList<SupplierCard>());
            item_suppliers.get(item.getId()).add(supplier);
        }
    }


    public String cancel_order(int order_id) throws Exception{// needs to be changed so it removes also a periodic Order
        //for now it will only be able to cancel periodic Orders...
        if(!periodic_orders.containsKey(order_id)){
            throw new Exception("Order does not appear in the system as a periodic Order!");
        }
        periodic_orders.remove(order_id);
        //int supp_id = curr.getSupplier_id();
        //Contract toR = suppliers_contracts.get(supp_id);
        //toR.remove_order(order_id);
        dataBaseController.delete_Per_Order(order_id);
        return "Order cancelled successfully!";
    }



    public void clear(){
        orders.clear();
        suppliers_contracts.clear();
        item_suppliers.clear();
        order_counter = 0;
    }

    public int orders_size(){
        return orders.size();
    }
    // public void add_preMade_order(Order order){
    //     orders.put(order_counter++,order);
    //     Contract contract = suppliers_contracts.get(order.getSupplier_id());
    //     contract.archive_order(order);

    //     //add to history of contract
    //     /*
    //     .
    //     .
    //     .
    //     .
    //      */
    // }
    public Map<Integer,Order> getOrders(){
        return orders;
    }
    public Map<Integer,PeriodicOrder> get_periodic_orders(){
        return periodic_orders;
    }




    public String add_PeriodicOrder(String supplier_name, int supplier_id, String address, String contact_number,List<Record> items,int day) throws Exception{
        check_day(day);
        if(!suppliers_contracts.containsKey(supplier_id)){
            throw new Exception("There is no contract in the system connected to this supplier");
        }
        Contract currCont = suppliers_contracts.get(supplier_id);
        if (!currCont.getSupplier_config().getKey()) {
            throw new Exception("This Supplier doesn't arrive in fixed times!");
        }
        PeriodicOrder per_order = new PeriodicOrder(order_counter,supplier_name,supplier_id,address,contact_number,items,day);

        double price = currCont.calculate_overall(per_order.quantities(),per_order.base_price());
        per_order.set_price(price);
        this.periodic_orders.put(order_counter++,per_order);
        //this.periodic_days.put(day,per_order);
       //currCont.archive_order(per_order);
        //Make changes to persistent data!!!
        //Make changes to persistent data!!!
        //Make changes to persistent data!!!
        //Make changes to persistent data!!!
        OrderDTO odto = new OrderDTO(per_order.getOrder_id(), supplier_name, supplier_id, address, contact_number, price, 0);
        dataBaseController.addPerOrder(per_order.object2DtoPeriodic(),odto);
        for(Record r:items){
            RecordDAO.getInstance().insert(r.object2Dto());
            RecordDAO.getInstance().insert2(r.getOrder_id(),r.getProduct_id(),r.getQuantity(),(int)r.getFinal_price());
        }
        return "Order registered successfully!";
    }

    public String add_to_Periodic_items(int order_id, Record item) throws Exception{
        if(!periodic_orders.containsKey(order_id)){
            throw new Exception("This Order isn't registered in the system (at least not periodic)");
        }
        PeriodicOrder order = periodic_orders.get(order_id);
        //Make changes to persistent data!!!
        //items
        String res=order.add_Record(item);
        dataBaseController.updateperOrder(order.object2DtoPeriodic());
        RecordDAO.getInstance().insert(item.object2Dto());
        RecordDAO.getInstance().insert2(item.getOrder_id(),item.getProduct_id(),item.getQuantity(),(int)item.getFinal_price());
        return res;
    }


    
    public String updatePeriodic_date(int order_id,int day) throws Exception{
        if(!periodic_orders.containsKey(order_id)){
            throw new Exception("This Order isn't registered in the system (at least not periodic)");
        }
        PeriodicOrder order = periodic_orders.get(order_id);
        String res=order.setArrival_day(day);
        //Make changes to persistent data!!!
        //String res=order.Update_arrival(day);
        dataBaseController.updateperOrder(order.object2DtoPeriodic());
        return res;

    }
    public String add_toPeriodic_product(int product_id, int order_id,int quantity) throws Exception{
        if(!periodic_orders.containsKey(order_id)){
            throw new Exception("This Order id is not classified as periodic in the system!");
        }
        PeriodicOrder order = periodic_orders.get(order_id);
        String res=order.Update_Record(product_id,quantity);
        RecordDTO rec=null;
        for(Record r:order.getItem_list()){
            if(r.getProduct_id()==product_id){
                rec=r.object2Dto();
                RecordDAO.getInstance().update(rec);
                RecordDAO.getInstance().update2(order_id, product_id, quantity,(int) rec.getFinal_price());
            }
        }
        return res;
    }
    private void check_day(int day) throws Exception{
        if(day>6 || day<0){throw new Exception("a day should be in range of [0 - 6]!");}
    }

    public void setOrder_counter(int order_counter) {
        this.order_counter = order_counter;
    }

    public void fill_map(){
        Collection<Contract> conts = ContractController.getInstance().listContracts();
        for (Contract contract : conts) {
            int curr_supp_id = contract.getSupplier_id();
            Collection<Product> prods = contract.list_items();
            for (Product product : prods) {
                if(item_suppliers.containsKey(product.getId())){
                    try {
                        item_suppliers.get(product.getId()).add(SupplierController.getInstance().get_supplier(curr_supp_id));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    item_suppliers.put(product.getId(),new LinkedList<>());
                    try {
                        item_suppliers.get(product.getId()).add(SupplierController.getInstance().get_supplier(curr_supp_id));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }


    public void LoadData()throws SQLException{
        //make sure to insert 0/1 in periodic field when inserting orders
        Pair<List<OrderDTO>,Integer> orders = dataBaseController.get_orders(0);
        int part1 = orders.getValue();
        for (OrderDTO oDto : orders.getKey()) {
            Order curr = new Order(oDto);
            curr.accept_Records(dataBaseController.getRecordsByOrderId(oDto.getOrder_id(), oDto.getSupplier_id()));
            this.orders.put(curr.getOrder_id(), curr);
        }
        orders = dataBaseController.get_orders(1);
        int part2 = orders.getValue();
        for (OrderDTO oDto : orders.getKey()) {
            PeriodicOrderDTO perOrder = dataBaseController.getPerOrder(oDto.getOrder_id());
            Map<RecordDTO,ProductDTO> recs = dataBaseController.getRecordsByOrderId(oDto.getOrder_id(), oDto.getSupplier_id());
            PeriodicOrder percurr = new PeriodicOrder(perOrder, oDto, recs);
            this.periodic_orders.put(percurr.getOrder_id(), percurr);
        }
        order_counter = part1+part2;
    }

    public void Load_afer(){
        suppliers_contracts = ContractController.getInstance().supp_contracts;
        fill_map();
    }
}
