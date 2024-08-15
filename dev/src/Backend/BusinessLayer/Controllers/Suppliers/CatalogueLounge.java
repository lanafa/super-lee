package Backend.BusinessLayer.Controllers.Suppliers;

import java.util.HashMap;
import java.util.Map;

public class CatalogueLounge {
    private static CatalogueLounge instance = null;
    private Map<Integer, Map<Integer,Integer>> catalouge; // suppid --- > <itemid,cataloguenumber>
    private Map<Integer,Integer> capacity;


    private CatalogueLounge(){
        catalouge = new HashMap<>();
        capacity = new HashMap<>();
    }
    public static CatalogueLounge getInstance(){
        if (instance==null){
            instance = new CatalogueLounge();
        }
        return instance;
    }

    public void add_info(int supp_id,int item_id){
        if(!catalouge.containsKey(supp_id)){
            Map<Integer,Integer> curr = new HashMap<>();
            curr.put(item_id,0);
            catalouge.put(supp_id,curr);
            capacity.put(supp_id,1);
        }else{
            Map<Integer,Integer> curr =new HashMap<>();
            curr.put(item_id,capacity.get(supp_id));
            capacity.put(supp_id,capacity.get(supp_id) + 1);
        }
    }
}
