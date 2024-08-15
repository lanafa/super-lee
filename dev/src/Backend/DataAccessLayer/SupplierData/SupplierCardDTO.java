package Backend.DataAccessLayer.SupplierData;
import Backend.BusinessLayer.objects.Suppliers.Contact;
import Backend.BusinessLayer.objects.Suppliers.Payment;
import Backend.BusinessLayer.objects.Suppliers.SupplierCard;

import java.util.ArrayList;
import java.util.List;

public class SupplierCardDTO {
    private int supplier_id;
    private int company_id;
    private String supplier_name;
    private String phone_number;
    private String bank_account;
   // private PaymentDTO payment;
    //private List<ContactDTO> contactList;
    //private int id_counter;

    public SupplierCardDTO(int supplier_id, String supplier_name, String phone_number, int company_id, String bank_account,  List<ContactDTO> contactList) {
        this.supplier_id = supplier_id;
        this.bank_account = bank_account;
        this.company_id = company_id;
        //this.contactList = contactList;
       // this.payment = payment;
        this.phone_number = phone_number;
        this.supplier_name = supplier_name;
        //id_counter=0;
    }
    public SupplierCardDTO(int supplier_id, String supplier_name, String phone_number, int company_id, String bank_account) {
        this.supplier_id = supplier_id;
        this.bank_account = bank_account;
        this.company_id = company_id;
        //this.contactList = contactList;
       // this.payment = payment;
        this.phone_number = phone_number;
        this.supplier_name = supplier_name;
        //this.id_counter=id_counter;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    // public PaymentDTO getPayment() {
    //     return payment;
    // }

    // public void setPayment(PaymentDTO payment) {
    //     this.payment = payment;
    // }

    // public List<ContactDTO> getContactList() {
    //     return contactList;
    // }

    // public void setContactList(List<ContactDTO> contactList) {
    //     this.contactList = contactList;
    // }

    // public int getId_counter() {
    //     return id_counter;
    // }

    // public String getPaymentMethod(){
    //     return payment.getPaymentMethod().toString();
    // }

    // public String getNetTerm(){
    //     return payment.getNetTerm().toString();
    // }

    // public SupplierCard dto2Object(){
    //     List<Contact> contacts=new ArrayList<>();
    //     for (ContactDTO c:contactList){
    //         contacts.add(c.dto2Object());
    //     }
    //     return new SupplierCard(supplier_id,supplier_name,phone_number,company_id,bank_account,contacts);
    // }


    public SupplierCard dto2Object(){
        return null;
    }
}
