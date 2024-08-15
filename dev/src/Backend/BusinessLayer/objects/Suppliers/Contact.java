package Backend.BusinessLayer.objects.Suppliers;

import Backend.DataAccessLayer.SupplierData.ContactDTO;
import Backend.DataAccessLayer.SupplierData.ContractDTO;

public class Contact {
    int id;
    private String name;
    private String phone_number;
    private String email;
    private int company_number;
    private String fax;

    public Contact(String name, String phone_number, String email, int company_number, String fax) {
        this.id = -1;
        this.name = name;
        this.phone_number = phone_number;
        this.email = email;
        this.company_number = company_number;
        this.fax = fax;
    }
    public Contact(int id,String name, String phone_number, String email, int company_number, String fax) {
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
        this.email = email;
        this.company_number = company_number;
        this.fax = fax;
    }

    public Contact(ContactDTO cdto){
        this.id = cdto.getId();
        this.name = cdto.getName();
        this.phone_number = cdto.getPhoneNumber();
        this.company_number = cdto.getCompanyNumber();
        this.fax = cdto.getFax();

    }

    public void set_id(int id){
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int new_id) {
        this.id = new_id;
    }

    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public int getCompany_number() {
        return company_number;
    }

    public String getFax() {
        return fax;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompany_number(int company_number) {
        this.company_number = company_number;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public ContactDTO object2Dto(){
        return new ContactDTO(id,name,phone_number,email,company_number,fax);
    }

}


