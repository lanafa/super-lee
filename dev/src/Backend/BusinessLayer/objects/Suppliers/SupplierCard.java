package Backend.BusinessLayer.objects.Suppliers;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.DataAccessLayer.SupplierData.ContactDTO;
import Backend.DataAccessLayer.SupplierData.ContractDTO;
import Backend.DataAccessLayer.SupplierData.SupplierCardDAO;
import Backend.DataAccessLayer.SupplierData.SupplierCardDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SupplierCard {
    private int supplier_id;
    private int company_id;
    private String supplier_name;
    private String phone_number;
    private String bank_account;
    private List<Contact> contactList;

    DataBaseController dataBaseController=DataBaseController.getInstance();

    public SupplierCard(int supplier_id, String supplier_name, String phone_number, int company_id, String bank_account,List<Contact> contactList) {
        this.supplier_id = supplier_id;
        this.bank_account = bank_account;
        this.company_id = company_id;
        this.contactList = contactList;
        //this.payment = payment;
        this.phone_number = phone_number;
        this.supplier_name = supplier_name;
       // id_counter=0;
    }

    public SupplierCard(SupplierCardDTO sdto){
        this.supplier_id = sdto.getSupplier_id();
        this.company_id = sdto.getCompany_id();
        this.supplier_name = sdto.getSupplier_name();
        this.phone_number = sdto.getPhone_number();
        this.bank_account = sdto.getBank_account();
        contactList = new LinkedList<>();
    }

    public void acceptContacts(List<ContactDTO> contacts){
        for (ContactDTO contactDTO : contacts) {
            Contact curr = new Contact(contactDTO);
            this.contactList.add(curr);
        }
    }

    public int getSupplier_id(){return supplier_id;}

    public String getSupplier_name() {
        return supplier_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public int getCompany_id() {
        return company_id;
    }

    public String getBank_account() {
        return bank_account;
    }

    // public Payment getPayment() {
    //     return payment;
    // }

    public List<Contact> getContactList() {
        return contactList;
    }


    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    // public void setPayment(Payment payment) {
    //     this.payment = payment;
    // }

    // public void setPaymentMethod(Payment.PaymentMethod p) {
    //     payment.setPaymentMethod(p);
    // }

    // public void setNetTerm(Payment.NetTerm n) {
    //     payment.setNetTerm(n);
    // }

    public void setContactList(LinkedList<Contact> contactList) {
        this.contactList = contactList;
    }

    public boolean ifTheContactList(int id) {
        for (Contact curr : contactList) {
            if (curr.getId() == id)
                return true;
        }
        return false;
    }

    public void addContact(Contact contact) throws SQLException {
        this.contactList.add(contact);
    }

    public void removeContact(int id) throws  SQLException{
        for (Contact curr : contactList) {
            if (curr.getId() == id) {
                contactList.remove(curr);
            }
        }
    }

    public Contact spit_contact(){
        Random random = new Random();
        if(contactList.size() != 0) {
            int index = random.nextInt(contactList.size());
            Contact randomElement = contactList.get(index);
            return randomElement;
        }
        return new Contact("","","",0,"");
    }

    public SupplierCardDTO object2Dto(){
        return new SupplierCardDTO(supplier_id,supplier_name,phone_number,company_id,bank_account);
    }

    // public void setId_counter(int id_counter) {
    //     this.id_counter = id_counter;
    // }

    public void clearContactList(){
        contactList.clear();
    }
}