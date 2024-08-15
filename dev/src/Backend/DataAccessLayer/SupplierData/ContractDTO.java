package Backend.DataAccessLayer.SupplierData;
import Backend.BusinessLayer.objects.Suppliers.*;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.DataAccessLayer.stockData.ProductDTO;

import java.util.*;

public class ContractDTO {
    private int contractId;
    private int supplierId;
    private PaymentDTO payment;
    //private List<OrderDTO> orderHistory;
    //private Map<ProductDTO, Integer> itemList; // pro --> amount
    private Pair<Boolean, Boolean> supplierConfig;

    private int days_toReady;

    // public ContractDTO(int contractId, int supplierId, PaymentDTO payment, Pair<Boolean, Boolean> supplierConfig,int days_toReady) {
    //     this.contractId = contractId;
    //     this.supplierId = supplierId;
    //     this.payment = payment;
    //     //this.itemList = itemList;
    //     this.supplierConfig = supplierConfig;
    //     //this.orderHistory = new LinkedList<>();
    //     this.days_toReady=days_toReady;
    // }

    public ContractDTO(int contractId, int supplierId, PaymentDTO payment,Pair<Boolean, Boolean> supplierConfig,int days_toReady) {
        this.contractId = contractId;
        this.supplierId = supplierId;
        this.payment = payment;
       // this.itemList = itemList;
        this.supplierConfig = supplierConfig;
       // this.orderHistory = orderHistory;
        this.days_toReady=days_toReady;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }

    // public List<OrderDTO> getOrderHistory() {
    //     return orderHistory;
    // }

    // public void setOrderHistory(List<OrderDTO> orderHistory) {
    //     this.orderHistory = orderHistory;
    // }

    // public Map<ProductDTO, Integer> getItemList() {
    //     return itemList;
    // }

    // public void setItemList(Map<ProductDTO, Integer> itemList) {
    //     this.itemList = itemList;
    // }


    public Pair<Boolean, Boolean> getSupplierConfig() {
        return supplierConfig;
    }

    public void setSupplierConfig(Pair<Boolean, Boolean> supplierConfig) {
        this.supplierConfig = supplierConfig;
    }

    public String getPaymentMethod(){
        return payment.getPaymentMethod().toString();
    }

    public String getNetTerm(){
        return payment.getNetTerm().toString();
    }

    public int getDays_toReady() {
        return days_toReady;
    }

    public void setDays_toReady(int days_toReady) {
        this.days_toReady = days_toReady;
    }

    public String getSupplierConfigString(){
        if (supplierConfig.getKey() & supplierConfig.getValue())
            return "11";
        if (!supplierConfig.getKey() & supplierConfig.getValue())
            return "01";
        if (supplierConfig.getKey() & !supplierConfig.getValue())
            return "10";
        return "00";
    }

    // public Contract dto2Object(){
    //     Map<Product, Integer> items=new HashMap<>();
    //     for (Map.Entry<ProductDTO, Integer> entry : itemList.entrySet()) {
    //         ProductDTO product = entry.getKey();
    //         items.put(product.dto2Object(),entry.getValue());
    //     }
    //     return new Contract(contractId,supplierId,payment.dto2Object(),items,supplierConfig,days_toReady);
    // }
    
    //implement
    public Contract dto2Object(){
        return null;
    }

}
