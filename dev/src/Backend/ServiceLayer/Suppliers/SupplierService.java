package Backend.ServiceLayer.Suppliers;

import Backend.BusinessLayer.Controllers.Suppliers.SupplierController;
import Backend.BusinessLayer.objects.Suppliers.Contact;
import Backend.BusinessLayer.objects.Suppliers.Payment;
import Backend.BusinessLayer.objects.Suppliers.SupplierCard;
import Backend.ServiceLayer.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SupplierService {
    private SupplierController supplierController;

    public SupplierService() {
        supplierController = SupplierController.getInstance();
    }

    public String makeJson(Object toConvert) {
        Gson g = new Gson();
        String pre = g.toJson(toConvert);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(pre);
        return gson.toJson(jsonElement);
    }

    public String addSupplier(String company_name, String phone_number, int company_id, String bank_account,
            LinkedList<Contact> contactList) {
        String json;
        Response res;
        try {
            String msg = supplierController.addSupplier(company_name, phone_number, company_id, bank_account,
                    contactList); // payment removed
            res = new Response<>(msg);
        } catch (Exception ex) {
            res = new Response<>(ex);
        }
        return makeJson(res);
    }

    public String removeSupplier(int company_id) {
        String json;
        Response res;
        try {
            String msg = supplierController.removeSupplier(company_id);
            res = new Response<>(msg);
        } catch (Exception ex) {
            res = new Response<>(ex);
        }
        return makeJson(res);
    }

    public String changeBankAccount(int company_id, String newBanckAcc) {
        String json;
        Response res;
        try {
            String msg = supplierController.changeBankAccount(company_id, newBanckAcc);
            res = new Response<>(msg);
        } catch (Exception ex) {
            res = new Response<>(ex);
        }
        return makeJson(res);
    }

    public String addContact(int company_id, String name, String phone_number, String email, String fax) {
        String json;
        Response res;
        try {
            String msg = supplierController.addContact(company_id, name, phone_number, email, fax);
            res = new Response<>(msg);
        } catch (Exception ex) {
            res = new Response<>(ex);
        }
        return makeJson(res);
    }

    public String removeContact(int company_id, int contact_id) {
        String json;
        Response res;
        try {
            String msg = supplierController.removeContact(company_id, contact_id);
            res = new Response<>(msg);
        } catch (Exception ex) {
            res = new Response<>(ex);
        }
        return makeJson(res);
    }

    public String listSuppliers() {
        String json = "";
        Response res;
        Gson gson = new Gson();
        try {
            Collection<SupplierCard> msg = supplierController.listSuppliers();
            res = new Response<>(msg);
            for (SupplierCard supplierCard : msg) {
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("{\n");
                jsonBuilder.append("  \"supplier_id\": ").append(supplierCard.getSupplier_id()).append(",\n");
                jsonBuilder.append("  \"company_id\": ").append(supplierCard.getCompany_id()).append(",\n");
                jsonBuilder.append("  \"supplier_name\": \"").append(supplierCard.getSupplier_name()).append("\",\n");
                jsonBuilder.append("  \"phone_number\": \"").append(supplierCard.getPhone_number()).append("\",\n");
                jsonBuilder.append("  \"bank_account\": \"").append(supplierCard.getBank_account()).append("\",\n");

                List<Contact> contacts = supplierCard.getContactList();
                jsonBuilder.append("  \"contactList\": [\n");
                for (int i = 0; i < contacts.size(); i++) {
                    Contact contact = contacts.get(i);
                    jsonBuilder.append("    {\n");
                    jsonBuilder.append("      \"contact_id\": ").append(contact.getId()).append(",\n");
                    jsonBuilder.append("      \"name\": \"").append(contact.getName()).append("\",\n");
                    jsonBuilder.append("      \"email\": \"").append(contact.getEmail()).append("\"\n");
                    jsonBuilder.append("    }");
                    if (i < contacts.size() - 1) {
                        jsonBuilder.append(",");
                    }
                    jsonBuilder.append("\n");
                }
                jsonBuilder.append("  ]\n");
                jsonBuilder.append("}");
                String curr = jsonBuilder.toString();
                json = json + curr;
            }
        } catch (Exception ex) {
            res = new Response<>(ex);
        }
        return json;
    }

    public String getSupplier(int supplier_id) {
        String json;
        Response res;
        try {
            SupplierCard supplier = supplierController.get_supplier(supplier_id);
            res = new Response<SupplierCard>(supplier);
        } catch (Exception ex) {
            res = new Response(ex);
        }
        return makeJson(res);
    }

    public String editSupplier_name(int supplier_id, String name) {
        String json;
        Response res;
        try {
            String msg = supplierController.editSupplier_name(supplier_id, name);
            res = new Response(msg);
        } catch (Exception ex) {
            res = new Response<>(ex);
        }
        return makeJson(res);
    }

    public String ChangePhoneNumber(int supp_id, String newPhone) {
        String json;
        Response res;
        try {
            String msg = supplierController.ChangePhoneNumber(supp_id, newPhone);
            res = new Response<>(msg);
        } catch (Exception ex) {
            res = new Response<>(ex);
        }
        return makeJson(res);
    }
}
