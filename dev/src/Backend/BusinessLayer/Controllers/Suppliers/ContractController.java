package Backend.BusinessLayer.Controllers.Suppliers;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.BusinessLayer.objects.Suppliers.Contract;
import Backend.BusinessLayer.objects.Suppliers.Pair;
import Backend.BusinessLayer.objects.Suppliers.Payment;
import Backend.DataAccessLayer.SupplierData.ContractDAO;
import Backend.DataAccessLayer.SupplierData.ContractDTO;
import Backend.DataAccessLayer.SupplierData.QuantityReportDTO;

import java.sql.SQLException;
import java.util.*;

public class ContractController {
    private static ContractController instance =null;
    public Map<Integer, Contract> contracts;//contract_id-->contract
    public Map<Integer, Contract> supp_contracts;//supp_id-->contract
    private int contract_id;

    DataBaseController dataBaseController=DataBaseController.getInstance();

    private ContractController(){
        contracts=new HashMap<>();
        supp_contracts = new HashMap<>();
        contract_id=0;
    }

    public static ContractController getInstance(){
        if (instance==null){
            instance=new ContractController();
        }
        return instance;
    }
    public int contracts_size(){
        return contracts.size();
    }
    public double calculate_itemP(int supp_id,int item_id, int quantity) throws Exception {
        Contract curr = contracts.get(supp_id);
        if(!curr.check_avail(item_id)){
            throw new Exception("This item is not included in the supplier's contract!");
        }
        double discounted_price = curr.getItemPrice(item_id, quantity);
        return discounted_price;
    }

    public String addContract(int supplier_id, Payment payment, Map<Product,Integer> item_list, Pair<Boolean, Boolean> supplier_config, int days,Map<Integer,Double> items_prices) throws Exception{
        if(SupplierController.getInstance().get_supplier(supplier_id) == null){
            throw new Exception("Supplier isn't registered in the System");
        }
        if(supp_contracts.containsKey(supplier_id)){
            throw new Exception("This Supplier already has a valid contract!");
        }
        Contract created = new Contract(contract_id,supplier_id,payment,item_list,supplier_config,days);
        contracts.put(contract_id++,created);
        supp_contracts.put(supplier_id,created);
        OrdersController.getInstance().put_suppCont(supplier_id,created);
        List<Product> items = new ArrayList<>(item_list.keySet());
        OrdersController.getInstance().fill_suppitems(items,SupplierController.getInstance().get_supplier(supplier_id));
        created.addItemPricesToQR(items_prices);
        /// adding to the data base
        dataBaseController.addContract(created.object2Dto());
        for (Map.Entry<Product, Integer> entry : item_list.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            ContractDAO.getInstance().insert2(created.getContract_id(), product.getId(), quantity);
        }
        dataBaseController.updateQreport(created.getQ_report().object2Dto());
        return "Contract added successfully!";
    }

    public String removeContract(int supp_id) throws Exception {
        if(supp_contracts.containsKey(supp_id)) {
            Contract removed = supp_contracts.remove(supp_id);
            contracts.remove(removed.getContract_id());
            OrdersController.getInstance().remove_contract(supp_id);
        }else{
            throw new Exception("There are no contracts made with this supplier!");
        }
        dataBaseController.deleteContract(contract_id);
        return "Contract removed successfully!";
    }

    public Contract get_contract(int supp_id) throws Exception {
        if(!supp_contracts.containsKey(supp_id)) throw new Exception("There are no contracts made with this supplier!");
        return supp_contracts.get(supp_id);
    }


    public String addEdit_item(int supp_id,Product item, int quantity,double price_per_one) throws Exception {
        if(!supp_contracts.containsKey(supp_id)) throw new Exception("There are no contracts made with this supplier!");
        Contract curr = supp_contracts.get(supp_id);
        curr.addEdit_item(item,quantity,price_per_one);
        dataBaseController.updateContract(curr.object2Dto());
        return "Item added to the supplier's contract successfully!";
    }

    public String remove_item(int supp_id,Product item) throws Exception {
        if(!supp_contracts.containsKey(supp_id)) throw new Exception("There are no contracts made with this supplier!");
        Contract curr = supp_contracts.get(supp_id);
        try {
            curr.remove_item(item);
            OrdersController.getInstance().remove_item_supplier(item.getId(),supp_id);
        }catch (Exception ex){
            throw new Exception(ex.getMessage());
        }
        dataBaseController.updateContract(curr.object2Dto());
        return "Item removed from the supplier's contract successfully!";
    }

    //if discount is ovl we put a random item_id .....
    public String add_discount(int supp_id,int item_id,int quantity,int discount,boolean ovl,Character c) throws Exception{
        if(!supp_contracts.containsKey(supp_id)) throw new Exception("There are no contracts made with this supplier!");
        Contract curr = supp_contracts.get(supp_id);
        curr.add_discount(item_id,quantity,discount,ovl,c);
        //contractDTO = curr.todal
        return "Discount added successfully!";
    }
    //if discount is ovl we put a random item_id .....
    public String remove_discount(int supp_id,int item_id,int quantity,boolean ovl) throws Exception {
        if(!supp_contracts.containsKey(supp_id)) throw new Exception("There are no contracts made with this supplier!");
        Contract curr = supp_contracts.get(supp_id);
        curr.remove_discount(item_id,quantity,ovl);
        return "Discount removed successfully!";
    }

    public void clear(){
        contracts.clear();
        supp_contracts.clear();
        contract_id = 0;
    }

    public String addContract(Contract contract) throws Exception{
        contracts.put(contract_id++,contract);
        if(supp_contracts.containsKey(contract.getSupplier_id())){
            throw new Exception("This Supplier already has a valid contract!");
        }
        supp_contracts.put(contract.getSupplier_id(),contract);
        OrdersController.getInstance().put_suppCont(contract.getSupplier_id(),contract);
        List<Product> items = new ArrayList<>(contract.getItem_list().keySet());
        OrdersController.getInstance().fill_suppitems(items,SupplierController.getInstance().get_supplier(contract.getSupplier_id()));
        dataBaseController.addContract(contract.object2Dto());
        return "Contract added successfully!";
    }


    public String change_contractPayment(int supp_id, Payment payment) throws Exception{
        if(!supp_contracts.containsKey(supp_id)) throw new Exception("There are no contracts made with this supplier!");
        supp_contracts.get(supp_id).setPayment(payment);
        dataBaseController.updateContract(supp_contracts.get(supp_id).object2Dto());
        return "Payment conditions updated successfully for the desired contract!";
    }

    public Collection<Contract> listContracts(){
        return contracts.values();
    }

    public void setContract_id(int contractId){
        this.contract_id=contractId;
    }


    public void LoadData() throws SQLException{
        Pair<List<ContractDTO>,Integer> pair = dataBaseController.get_contracts();
        int counter = pair.getValue();
        contract_id = counter;
        for (ContractDTO cDto : pair.getKey()) {
            Contract curr = new Contract(cDto);
            Map<Integer,Integer> curr_map = dataBaseController.get_orderHistory(curr.getContract_id(), 0);
            curr.accept_orderHistory(curr_map);
            curr_map = dataBaseController.get_orderHistory(curr.getContract_id(), 1);
            curr.accept_order_per_history(curr_map);
            QuantityReportDTO qdto = dataBaseController.get_quantityreport(curr.getContract_id());
            curr.accept_Qreport(qdto);
            Map<Integer,Integer> prods_amount = dataBaseController.get_itemlist(curr.getContract_id());
            curr.accept_itemlist(prods_amount);
            contracts.put(curr.getContract_id(), curr);
            supp_contracts.put(curr.getSupplier_id(), curr);
        }
    }
    }
