package Backend.BusinessLayer.Controllers.Suppliers;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.objects.Suppliers.Contact;
import Backend.BusinessLayer.objects.Suppliers.Pair;
import Backend.BusinessLayer.objects.Suppliers.Payment;
import Backend.BusinessLayer.objects.Suppliers.SupplierCard;
import Backend.DataAccessLayer.SupplierData.ContactDTO;
import Backend.DataAccessLayer.SupplierData.SupplierCardDTO;

import java.sql.SQLException;
import java.util.*;

public class SupplierController {
    private static SupplierController instance = null;
    public Map<Integer, SupplierCard> suppliers;
    private int suppid_counter = 0;
    private int contactid_counter = 0;


    private SupplierController() {
        suppliers = new HashMap<>();
    }

    DataBaseController dataBaseController=DataBaseController.getInstance();

    /**
     * Returns the Singleton instance of the SupplierController.
     * If the instance doesn't exist, it creates one and returns it.
     * @return The Singleton instance of the SupplierController.
     */
    public static SupplierController getInstance() {
        if (instance == null) {
            instance = new SupplierController();
        }
        return instance;
    }
    public int supp_size(){
        return suppliers.size();
    }

    /**
     * Adds a new supplier to the suppliers map.
     * If the company ID already exists in the map, throws a RuntimeException.
     *
     * @param company_name The name of the company.
     * @param phone_number The phone number of the company.
     * @param company_id   The ID of the company.
     * @param bank_account The bank account of the company.
     * @param payment      The payment method of the company.
     * @param contactList  A linked list containing all the contacts of the company.
     * @throws RuntimeException if the company ID already exists in the map.
     */
    public String addSupplier(String company_name, String phone_number, int company_id, String bank_account, LinkedList<Contact> contactList) throws Exception {
        for (SupplierCard card : suppliers.values()) {
            if (card.getCompany_id() == company_id) {
                    throw new Exception("This supplier already exists in the system!");
            }
        }
        for (Contact contact : contactList) {
            contact.setId(contactid_counter++);
        }
        SupplierCard toAdd = new SupplierCard(suppid_counter,company_name, phone_number, company_id, bank_account, contactList);
        suppliers.put(suppid_counter++, toAdd);
        
        dataBaseController.addSupplier(toAdd);
        return "Supplier added successfully";
    }

    public String addSupplier(SupplierCard supplier) throws Exception{
        for (SupplierCard card : suppliers.values()) {
            if (card.getCompany_id() == supplier.getCompany_id()) {
                    throw new Exception("This company already exists in the system!");
            }
        }

        suppliers.put(supplier.getSupplier_id(),supplier);
        dataBaseController.addSupplier(supplier);
        return "Supplier added successfully";
    }

    /**
     * Removes a supplier from the suppliers map.
     * If the company ID doesn't exist in the map, throws a RuntimeException.
     *
     * @param supplier_id The ID of the supplier to remove.
     * @throws RuntimeException if the supplier ID doesn't exist in the map.
     */
    public String removeSupplier(int supplier_id) throws Exception{
        if (!suppliers.containsKey(supplier_id)) {
                throw new Exception("This supplier id does not appear to be in the system!");
        }
        suppliers.remove(supplier_id);

        dataBaseController.deleteSupplier(supplier_id);
        return "Supplier removed successfully";
    }

    public String editSupplier_name(int supplier_id,String name) throws Exception{
        if (!suppliers.containsKey(supplier_id)) {
            throw new Exception("This supplier id does not appear to be in the system!");
        }
        suppliers.get(supplier_id).setSupplier_name(name);

        dataBaseController.updateSupplier(suppliers.get(supplier_id).object2Dto());
        return "Name changed successfully!";
    }

    /**
     * Changes the bank account of a supplier in the suppliers map.
     * If the company ID doesn't exist in the map, throws a RuntimeException.
     *
     * @param supplier_id  The ID of the company to modify.
     * @param newBanckAcc The new bank account of the company.
     * @throws RuntimeException if the company ID doesn't exist in the map.
     */
    public String changeBankAccount(int supplier_id, String newBanckAcc) throws Exception {
        if (!suppliers.containsKey(supplier_id)) {
                throw new Exception("This in no working Suppliers for this company");
        }
        suppliers.get(supplier_id).setBank_account(newBanckAcc);

        dataBaseController.updateSupplier(suppliers.get(supplier_id).object2Dto());
        return "Bank Account changed successfully";
    }

    /**
     * Changes the payment method for a given supplier company.
     *
     * @param supplier_id    The ID of the supplier company to change the payment method for.
     * @param paymentMethod The new payment method to set for the supplier company.
     * @throws RuntimeException if there is no working supplier for the given company ID.
     */
    // public String changePaymentMethod(int supplier_id, Payment.PaymentMethod paymentMethod) throws Exception{
    //     if (!suppliers.containsKey(supplier_id)) {
    //             throw new Exception("This in no working Suppliers for this company");
    //     }
    //     suppliers.get(supplier_id).setPaymentMethod(paymentMethod);
    //     //make changes to persistent data!!!
    //     //make changes to persistent data!!!
    //     //make changes to persistent data!!!
    //     dataBaseController.updateSupplier(suppliers.get(supplier_id).object2Dto());
    //     return "Payment Method changed successfully";
    // }

    /**
     * Changes the net term for a given supplier company.
     *
     * @param netTerm    The new net term to set for the supplier company.
     * @throws RuntimeException if there is no working supplier for the given company ID.
     */
    // public String changeNetTerm(int supplier_id, Payment.NetTerm netTerm) throws Exception {
    //     if (!suppliers.containsKey(supplier_id)) {
    //             throw new Exception("This in no working Suppliers for this company");
    //     }
    //     suppliers.get(supplier_id).setNetTerm(netTerm);
    //     dataBaseController.updateSupplier(suppliers.get(supplier_id).object2Dto());
    //     return "Net Term changed successfully";
    // }

    /**
     * Sets the payment details for a given supplier company.
     *
     * @param supplier_id    The ID of the supplier company to set the payment details for.
     * @param paymentMethod The new payment method to set for the supplier company.
     * @param netTerm       The new net term to set for the supplier company.
     * @throws RuntimeException if there is no working supplier for the given company ID.
     */
    // public String setPayment(int supplier_id, Payment.PaymentMethod paymentMethod, Payment.NetTerm netTerm) throws Exception {
    //     Payment newPayment = new Payment(paymentMethod, netTerm);
    //     if (!suppliers.containsKey(supplier_id)) {
    //             throw new Exception("This in no working Suppliers for this company");
    //     }
    //     suppliers.get(supplier_id).setPayment(newPayment);
    //     //make changes to persistent data!!!
    //     //make changes to persistent data!!!
    //     //make changes to persistent data!!!
    //     dataBaseController.updateSupplier(suppliers.get(supplier_id).object2Dto());
    //     return "Payment changed successfully";
    // }

    /**
     * Adds a new contact for a given supplier company.
     *
     * @param supplier_id   The ID of the supplier company to add the contact for.
     * @param name         The name of the contact to add.
     * @param phone_number The phone number of the contact to add.
     * @param email        The email address of the contact to add.
     * @param fax          The fax number of the contact to add.
     * @throws RuntimeException if there is no working supplier for the given company ID or if the contact already exists.
     */

    public String addContact(int supplier_id, String name, String phone_number, String email, String fax) throws Exception {
        if (!suppliers.containsKey(supplier_id)) {
                throw new Exception("This supplier does not exist in the system!");
        }
        Contact curr = new Contact(name, phone_number, email, supplier_id, fax);
        curr.setId(contactid_counter);
        suppliers.get(supplier_id).addContact(curr);
        ContactDTO cdto = curr.object2Dto();
        dataBaseController.addContact(cdto);
        dataBaseController.insert_supp_contact(supplier_id, contactid_counter++);
        return "Contact added successfully";
    }

    public String ChangePhoneNumber(int supp_id,String newPhone) throws Exception{
        if (!suppliers.containsKey(supp_id)) {
            throw new Exception("This in no working Suppliers for this company");
        }
        suppliers.get(supp_id).setPhone_number(newPhone);
        dataBaseController.updateSupplier(suppliers.get(supp_id).object2Dto());
        return "PhoneNumber changed successfully!";
    }

    /**
     * Removes an existing contact for a given supplier company.
     *
     * @param supplier_id The ID of the supplier company to remove the contact from.
     * @param contact_id The ID of the contact to remove.
     * @throws RuntimeException if there
     */
    public String removeContact(int supplier_id, int contact_id) throws Exception{
        if (!suppliers.containsKey(supplier_id)) {
                throw new Exception("This in no working Suppliers for this company");
        }
        if (!suppliers.get(supplier_id).ifTheContactList(contact_id)) {
                throw new Exception("contact not found");
        }
        suppliers.get(supplier_id).removeContact(contact_id);
        dataBaseController.delete_supp_contact(supplier_id, contact_id);
        return "Contact removed successfully";
    }

    public SupplierCard get_supplier(int supp_id) throws Exception{
        if(suppliers.containsKey(supp_id))
            return suppliers.get(supp_id);
        throw new Exception("Supplier id is not registered in the system!");
    }

    public void clear(){
        suppliers.clear();
        suppid_counter = 0;
    }


    public Collection<SupplierCard> listSuppliers(){
        return suppliers.values();
    }

    public void setSuppid_counter(int suppid_counter) {
        this.suppid_counter = suppid_counter;
    }


    public void LoadData()throws SQLException{
        Pair<List<SupplierCardDTO>,Integer> pair = dataBaseController.getSupps();

        this.suppid_counter = pair.getValue();
        this.contactid_counter = dataBaseController.get_cont_size();

        for (SupplierCardDTO sdto : pair.getKey()) {
            SupplierCard supp = new SupplierCard(sdto);
            supp.acceptContacts(dataBaseController.getSupplierContactsById(supp.getSupplier_id()));
            this.suppliers.put(supp.getSupplier_id(), supp);
        }
    }
}
