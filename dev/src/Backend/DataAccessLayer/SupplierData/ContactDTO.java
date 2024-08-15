package Backend.DataAccessLayer.SupplierData;

import Backend.BusinessLayer.objects.Suppliers.Contact;

public class ContactDTO {
    private int id;
    private String name;
    private String phoneNumber;
    private String email;
    private int companyNumber;
    private String fax;
    // private int supplier_id;

    public ContactDTO(int id, String name, String phoneNumber, String email, int companyNumber, String fax) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.companyNumber = companyNumber;
        this.fax = fax;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(int companyNumber) {
        this.companyNumber = companyNumber;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    // public int getSupplier_id() {
    //     return supplier_id;
    // }

    public Contact dto2Object(){
        return new Contact(id,name,phoneNumber,email,companyNumber,fax);
    }
}
