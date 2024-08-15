package Backend.BusinessLayer.objects.Suppliers;

import Backend.DataAccessLayer.SupplierData.QuantityReportDTO;

import java.util.*;

public class QuantityReport {
    private int contract_id;
    private Map<Integer,Double> products_price; //product id ===> product price by  supplier
    private Map<Integer,Map<Integer, Pair<Character,Integer>>> discounts; //product id--><quantity, <p/v,discount>>
    private Map<Integer,Pair<Character,Integer>> ovl_discount;//quantity --> <p/v,discount>


    public QuantityReport(int contract_id){
        this.contract_id = contract_id;
        this.discounts = new HashMap<>();
        this.ovl_discount = new HashMap<>();
        this.products_price=new HashMap<>();
    }

    public QuantityReport(int contract_id,Map<Integer, Map<Integer, Pair<Character, Integer>>> discounts
            ,Map<Integer, Pair<Character, Integer>> ovl_discount,Map<Integer,Double> products_price) {
        this.discounts = discounts;
        this.ovl_discount = ovl_discount;
        this.contract_id=contract_id;
        this.products_price=products_price;
    }
    public QuantityReport(QuantityReportDTO qdto){
        this.contract_id = qdto.getContract_id();
        this.products_price = qdto.getProducts_prices();
        this.discounts = qdto.getDiscounts();
        this.ovl_discount = qdto.getOvl_discount();
    }

    public void remove_discount(int item_id,int quantity){
        discounts.get(item_id).remove(quantity);
    }
    public void addP_discount(int item_id,int quantity, int discount){
        Pair<Character,Integer> pair = new Pair<>('p',discount);
        Map<Integer,Pair<Character,Integer>> map = new HashMap<>();
        map.put(quantity,pair);
        discounts.put(item_id,map);
    }
    public void addV_discount(int item_id,int quantity, int discount){
        Pair<Character,Integer> pair = new Pair<>('v',discount);
        Map<Integer,Pair<Character,Integer>> map = new HashMap<>();
        map.put(quantity,pair);
        discounts.put(item_id,map);

    }
    public void addP_discount_ovl(int quantity, int discount){
        Pair<Character,Integer> pair = new Pair<>('p',discount);
        ovl_discount.put(quantity,pair);
    }
    public void addV_discount_ovl(int quantity, int discount){
        Pair<Character,Integer> pair = new Pair<>('v',discount);
        ovl_discount.put(quantity,pair);
    }
    public void remove_ovl_discount(int quantity){
        ovl_discount.remove(quantity);
    }

    public double get_price(int item_id, int quantity,double initP){
        if(!discounts.containsKey(item_id)) return (initP*quantity);
        Map<Integer,Pair<Character,Integer>> discs = discounts.get(item_id);
        List<Integer> keys = new ArrayList<>(discs.keySet());
        Collections.sort(keys);
        double price = 0;
         do {
            int idx = search_index(keys, quantity);
            if (idx < 0) {price = price + (initP * quantity); break;}
            Integer q = keys.get(idx);
            Pair<Character, Integer> p = discs.get(q);
            price = (p.getKey() == 'p') ? (price +(((initP * q) * (100 - p.getValue())) / 100)) :(price + ((initP * q) - p.getValue()));
            quantity = (quantity - q);
        }while (quantity>0);
        return price;
    }

    private int search_index(List<Integer> keys, int given_key){
        int holder = -1;
        for (Integer i:keys) {
            if(i<=given_key) holder++;
        }
        return holder;
    }

    public double calculate_overall(int quantity, double initP) {
        double discount=0;
        List<Integer> keys = new ArrayList<>(ovl_discount.keySet());
        Collections.sort(keys);
        int idx = search_index(keys, quantity);
        if (idx >= 0) {
            Integer q = keys.get(idx);
            Pair<Character, Integer> p = ovl_discount.get(q);
            discount = (p.getKey() == 'p') ? (initP*(p.getValue()/100)) :p.getValue();
        }
        return (initP - discount);
    }


    public void addproduct_price(int prod_id,double price){
        products_price.put(prod_id,price);
    }

    public double getproduct_price(int prod_id){
        return products_price.get(prod_id);
    }

    public void setproduct_price(int prod_id, double price){
         products_price.put(prod_id,price);
    }

    public QuantityReportDTO object2Dto(){
        return new QuantityReportDTO(contract_id,discounts,ovl_discount,products_price);
    }

    public void addItemPricesToQR(Map<Integer,Double> items_prices){
        products_price.putAll(items_prices);
    }

    }
