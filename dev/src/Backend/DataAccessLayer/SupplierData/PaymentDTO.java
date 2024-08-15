package Backend.DataAccessLayer.SupplierData;

import Backend.BusinessLayer.objects.Suppliers.Payment;

public class PaymentDTO {
    public enum PaymentMethod {
        CASH,
        BIT,
        CHECK,
        CRYPTO,
        CREDITCARD,
        BankTransfer,
    }

    public enum NetTerm {
        NET30,
        NET45,
        NET60,
        NET90,
    }

    private PaymentMethod method;
    private NetTerm term;

    public PaymentDTO(PaymentMethod method, NetTerm term) {
        this.method = method;
        this.term = term;
    }

    public PaymentMethod getPaymentMethod() {
        return this.method;
    }

    public NetTerm getNetTerm() {
        return this.term;
    }

    public void setPaymentMethod(PaymentMethod method) {
        this.method = method;
    }

    public void setNetTerm(NetTerm term) {
        this.term = term;
    }

    public Payment dto2Object(){
        return new Payment(Payment.PaymentMethod.valueOf(method.toString()),Payment.NetTerm.valueOf(term.toString()));
    }
}
