package Backend.DataAccessLayer.SupplierData;
import Backend.BusinessLayer.objects.Suppliers.*;

import java.util.HashMap;
import java.util.Map;

public class QuantityReportDTO {
    private Map<Integer,Double> products_price;
    private Map<Integer, Map<Integer, Pair<Character, Integer>>> discounts; // itemid--><quantity, <p/v,discount>>
    private Map<Integer, Pair<Character, Integer>> ovl_discount; // quantity --> <p/v,discount>

    private int contract_id;

    public QuantityReportDTO(int contract_id) {
        this.discounts = new HashMap<>();
        this.ovl_discount = new HashMap<>();
        this.contract_id=contract_id;
    }

    public QuantityReportDTO(int contract_id,Map<Integer, Map<Integer, Pair<Character, Integer>>> discounts
    ,Map<Integer, Pair<Character, Integer>> ovl_discount, Map<Integer, Double> products_price) {
        this.discounts = discounts;
        this.ovl_discount = ovl_discount;
        this.contract_id=contract_id;
        this.products_price = products_price;
    }

    public Map<Integer, Map<Integer, Pair<Character, Integer>>> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Map<Integer, Map<Integer, Pair<Character, Integer>>> discounts) {
        this.discounts = discounts;
    }

    public Map<Integer, Pair<Character, Integer>> getOvl_discount() {
        return ovl_discount;
    }
    public Map<Integer,Double> getProducts_prices(){
        return products_price;
    }

    public void setOvl_discount(Map<Integer, Pair<Character, Integer>> ovl_discount) {
        this.ovl_discount = ovl_discount;
    }

    public int getContract_id() {
        return contract_id;
    }

    public void addDiscount(int itemId, int quantity, Pair<Character, Integer> characterIntegerPair) {
        if (discounts.containsKey(itemId)) {
            Map<Integer, Pair<Character, Integer>> itemDiscounts = discounts.get(itemId);
            itemDiscounts.put(quantity, characterIntegerPair);
        } else {
            Map<Integer, Pair<Character, Integer>> itemDiscounts = new HashMap<>();
            itemDiscounts.put(quantity, characterIntegerPair);
            discounts.put(itemId, itemDiscounts);
        }
    }

    public void addOverallDiscount(int quantity, Pair<Character, Integer> characterIntegerPair) {
        ovl_discount.put(quantity, characterIntegerPair);
    }

    public QuantityReport dto2Object(){
        return new QuantityReport(contract_id,discounts,ovl_discount,products_price);
    }
}
