package Backend.BusinessLayer.objects.Suppliers;

import Backend.DataAccessLayer.SupplierData.PaymentDTO;

public class Payment {
    public enum PaymentMethod{
        CASH,
        BIT,
        CHECK,
        CRYPTO,
        CREDITCARD,
        BankTransfer,
    }
    public enum NetTerm{
        NET30,
        NET45,
        NET60,
        NET90,
    }
    private PaymentMethod method;
    private NetTerm term;


    public Payment(PaymentMethod method, NetTerm term){
        this.method = method;
        this.term = term;
    }

    public PaymentMethod getPaymentMethod(){
        return this.method;
    }
    public NetTerm getNetTerm(){
        return this.term;
    }
    public void setPaymentMethod(PaymentMethod method){
        this.method = method;
    }
    public void setNetTerm(NetTerm term){
        this.term = term;
    }

    public PaymentDTO object2Dto(){
        return new PaymentDTO(PaymentDTO.PaymentMethod.valueOf(method.toString()),PaymentDTO.NetTerm.valueOf(term.toString()));
    }

}
