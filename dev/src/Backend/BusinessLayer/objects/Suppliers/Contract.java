package Backend.BusinessLayer.objects.Suppliers;


import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.Controllers.Stock.productscontroller;
import Backend.BusinessLayer.Controllers.Suppliers.OrdersController;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.DataAccessLayer.SupplierData.ContractDTO;
import Backend.DataAccessLayer.SupplierData.OrderDTO;
import Backend.DataAccessLayer.SupplierData.PeriodicOrderDTO;
import Backend.DataAccessLayer.SupplierData.QuantityReportDAO;
import Backend.DataAccessLayer.SupplierData.QuantityReportDTO;
import Backend.DataAccessLayer.stockData.ProductDTO;

import java.security.KeyStore.Entry;
import java.sql.SQLException;
import java.util.*;

public class Contract {
    private int days_toReady;
    private int contract_id;
    private int supplier_id;
    private Payment payment;
    private List<Order> order_history;
    private List<PeriodicOrder> order_per_history;
    private Map<Product,Integer> item_list; // Product--> quantity can be supplied
    private QuantityReport Q_report;
    private Pair<Boolean, Boolean> supplier_config; //fixed or not <--> delivers or not

    DataBaseController dataBaseController=DataBaseController.getInstance();


//    public Contract(int supplier_id, Payment payment, LinkedList<Order> order_history,
//                    Map<Product,Integer> item_list, Pair<Boolean, Boolean> supplier_config) {
//        this.supplier_id = supplier_id;
//        this.payment = payment;
//        this.order_history = order_history;
//        this.item_list = item_list;
//        this.supplier_config = supplier_config;
//        Q_report = new QuantityReport();
//    }

    public Contract(int contract_id, int supplier_id, Payment payment, Map<Product,Integer> item_list, Pair<Boolean, Boolean> supplier_config, int daystoready) {
        this.contract_id = contract_id;
        this.supplier_id = supplier_id;
        this.payment = payment;
        this.item_list = item_list;
        this.supplier_config = supplier_config;
        this.order_history = new LinkedList<>();
        this.order_per_history = new LinkedList<>();
        this.days_toReady = daystoready;
        Q_report = new QuantityReport(contract_id);
    }

    public Contract(ContractDTO cdto){
        this.days_toReady = cdto.getDays_toReady();
        this.contract_id = cdto.getContractId();
        this.supplier_id = cdto.getSupplierId();
        this.payment = cdto.getPayment().dto2Object();
        this.supplier_config = cdto.getSupplierConfig();
        this.order_history = new LinkedList<>();
        this.order_per_history = new LinkedList<>();
        this.item_list = new HashMap<>();
    }

    public void accept_orderHistory(Map<Integer,Integer> order_id_occur){//id ==> copies
        for (Map.Entry<Integer, Integer> entry : order_id_occur.entrySet()) {
            int copies = entry.getKey();
            int order_id = entry.getKey();
            Order order = OrdersController.getInstance().orders.get(order_id);
            for(int i = 0; i < copies; i++){
                Order copy = new Order(order);
                order_history.add(copy);
            }
        }
    }
    public void accept_order_per_history(Map<Integer,Integer> per_ords_id_occur){ //id ==> copies
        for (Map.Entry<Integer, Integer> entry : per_ords_id_occur.entrySet()) {
            int copies = entry.getKey();
            int order_id = entry.getKey();
            PeriodicOrder order = (PeriodicOrder) OrdersController.getInstance().orders.get(order_id);
            for(int i = 0; i < copies; i++){
                PeriodicOrder copy = new PeriodicOrder(order);
                order_per_history.add(copy);
            }
        }
        
    }

    public void accept_Qreport(QuantityReportDTO q_r){
        QuantityReport qr = new QuantityReport(q_r);
        this.Q_report = qr;
    }

    public void accept_itemlist(Map<Integer,Integer> items){ //prod_id ==> quantity can be supplied
        for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
            int amount = entry.getValue();
            int prod_id = entry.getKey();
            item_list.put(productscontroller.getInstance().returnProduct(prod_id), amount);
        }
    }














    public Contract(int contract_id , int supplier_id, Payment payment, Map<Product,Integer> item_list, QuantityReport Q_report, Pair<Boolean, Boolean> supplier_config, int daystoready) {
        this(contract_id,supplier_id, payment, item_list, supplier_config,daystoready);
        this.Q_report = Q_report;
        this.order_history = new LinkedList<>();
    }


    public int getSupplier_id() {
        return supplier_id;
    }

    public Payment getPayment() {
        return payment;
    }

    public List<Order> getOrder_history() {
        return order_history;
    }

    public List<PeriodicOrder> getPerOrder_history(){
        return order_per_history;
    }

    public Map<Product,Integer> getItem_list() {
        return item_list;
    }

    public QuantityReport getQ_report() {
        return Q_report;
    }

    public Pair<Boolean, Boolean> getSupplier_config() {
        return supplier_config;
    }


    public void setSupplier_number(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setOrder_history(LinkedList<Order> order_history) {
        this.order_history = order_history;
    }

    public void setItem_list(Map<Product,Integer> item_list) {
        this.item_list = item_list;
    }

    public void setQ_report(QuantityReport Q_report) {
        this.Q_report = Q_report;
    }

    public void setSupplier_config(Pair<Boolean, Boolean> supplier_config) {
        this.supplier_config = supplier_config;
    }

    public double getItemPrice(int prod_id, int quantity){
        double price = Q_report.get_price(prod_id,quantity,search_price(prod_id));
        return price;
    }
    public double get_margin_price(int item_id,int quantity){
        return (search_price(item_id)*quantity) - (getItemPrice(item_id,quantity));
    }

    public double search_price(int prod_id){// to be now changed
        return this.Q_report.getproduct_price(prod_id);
//        List<Product> item_listK = new ArrayList<>(item_list.keySet());
//        for (Product product:item_listK){
//            if(product.getId() == prod_id) return product.getManufacturePrice();
//        }
//      return -1;
    }


    public boolean check_avail(int prod_id){
        List<Product> item_listK = new ArrayList<>(item_list.keySet());
        for (Product item:item_listK) {
            if (item.getId() == prod_id) return true;
        }
        return false;
    }

    public boolean can_Whole(int prod_id, int quantity){
        Integer supped_quantity = -1;
        for (Product item:item_list.keySet()) {
            if(item.getId() == prod_id)
                supped_quantity = item_list.get(item);
        }

        return (supped_quantity.intValue() >= quantity);
    }

    public int quant(int prod_id, int quantity){
        int quant = 0;
        System.out.println(item_list.size());
        for (Product item:item_list.keySet()) {
            if(item.getId() == prod_id){
                quant = (item_list.get(item) >= quantity) ? quantity : item_list.get(item);
            }
        }
        return quant;
    }

    public double calculate_overall(int quantity, double initP){
        return Q_report.calculate_overall(quantity,initP);
    }

    public void archive_order(Order order){
       this.order_history.add(order);
    }
    public void remove_order(int order_id){
        for (Order order: order_history) {
            if(order.getOrder_id() == order_id){
                order_history.remove(order);
            }
        }
    }

    public int getContract_id() {
        return contract_id;
    }

    public void addEdit_item(Product product,int quantity, double price_per_one){
        List<Product> items = new ArrayList<>(item_list.keySet());
        for (Product it:items) {
            if(it.getId() == product.getId()){
                item_list.put(it,quantity);
                Q_report.setproduct_price(product.getId(),price_per_one);
                return;
            }
            item_list.put(product,quantity);
            Q_report.setproduct_price(product.getId(),price_per_one);
        }
    }

    public void remove_item(Product product)  {
        List<Product> item_listK = new ArrayList<>(item_list.keySet());
        for (Product it:item_listK) {
            if (it.getId() == product.getId()) {
                item_list.remove(it);
                return;
            }
            }throw new IllegalArgumentException("product doesn't exist in the contract!");
        }


    public void add_discount(int prod_id,int quantity,int discount,boolean ovl,Character c) throws SQLException {
        if(ovl){
            if(c == 'p'){
                Q_report.addP_discount_ovl(quantity,discount);
            }else{
                Q_report.addV_discount_ovl(quantity,discount);
            }
        }else{
            if(c == 'p'){
                Q_report.addP_discount(prod_id,quantity,discount);
            }else{
                Q_report.addV_discount(prod_id,quantity,discount);
            }
        }
        dataBaseController.updateQreport(Q_report.object2Dto());
      }

    public void remove_discount(int prod_id,int quantity,boolean ovl) throws SQLException {
        if(ovl){
            Q_report.remove_ovl_discount(quantity);
        }else{
            Q_report.remove_discount(prod_id,quantity);
        }
        dataBaseController.updateQreport(Q_report.object2Dto());
    }

    public ContractDTO object2Dto(){
        return new ContractDTO(contract_id,supplier_id,payment.object2Dto(),supplier_config,days_toReady);
    }

    public int needed_days(){
        return this.days_toReady;
    }


    public Collection<Product> list_items(){
        return item_list.keySet();
    }
    public void clearData(){
        item_list.clear();
        order_history.clear();
    }

    public void addItemPricesToQR(Map<Integer,Double> items_prices){
        Q_report.addItemPricesToQR(items_prices);
    }

    public Pair getSupplierConfig() {
        return this.supplier_config;
    }
}


