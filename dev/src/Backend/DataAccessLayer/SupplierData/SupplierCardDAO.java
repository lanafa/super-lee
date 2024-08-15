package Backend.DataAccessLayer.SupplierData;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.Controllers.Suppliers.SupplierController;
import Backend.BusinessLayer.objects.Suppliers.Pair;
import Backend.BusinessLayer.objects.Suppliers.Payment;
import Backend.BusinessLayer.objects.Suppliers.SupplierCard;
import Backend.DataAccessLayer.stockData.CategoryDAO;

import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class SupplierCardDAO {
    private Connection connection;

    private ContactDAO contactDAO=ContactDAO.getInstance();

   // private SupplierController supplierController=SupplierController.getInstance();

    public static SupplierCardDAO getInstance() {
        if(instance == null){
            instance = new SupplierCardDAO();
        }

        return instance;
    }
    private static SupplierCardDAO instance = null;

    private SupplierCardDAO() {
        this.connection = DataBaseController.get_con();
    }

    public void insert(SupplierCardDTO supplierCard) throws SQLException {
        String query = "INSERT INTO supplier_cards (supplier_id, company_id, supplier_name, phone_number, bank_account)" +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, supplierCard.getSupplier_id());
            statement.setInt(2, supplierCard.getCompany_id());
            statement.setString(3, supplierCard.getSupplier_name());
            statement.setString(4, supplierCard.getPhone_number());
            statement.setString(5, supplierCard.getBank_account());
            statement.executeUpdate();
        }
    }

    public void insert_supp_contact(int supplier_id, int contact_id) throws SQLException{
        String query = "INSERT INTO supplier_contacts (supplier_id,contact_id)" + "VALUES (?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, supplier_id);
            statement.setInt(2, contact_id);
            statement.executeUpdate();
        }
        
    }

    public void update(SupplierCardDTO supplierCard) throws SQLException {
        String query = "UPDATE supplier_cards SET company_id = ?, supplier_name = ?, phone_number = ?, bank_account = ?" +
                "WHERE supplier_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, supplierCard.getCompany_id());
            statement.setString(2, supplierCard.getSupplier_name());
            statement.setString(3, supplierCard.getPhone_number());
            statement.setString(4, supplierCard.getBank_account());
            statement.setInt(5, supplierCard.getSupplier_id());
            statement.executeUpdate();
        }
    }



    public void delete(int supplierId) throws SQLException {
        String deleteContactsQuery = "DELETE FROM supplier_contacts WHERE supplier_id = ?";
        try (PreparedStatement deleteContactsStatement = connection.prepareStatement(deleteContactsQuery)) {
            deleteContactsStatement.setInt(1, supplierId);
            deleteContactsStatement.executeUpdate();
        }

        String deleteSupplierQuery = "DELETE FROM supplier_cards WHERE supplier_id = ?";
        try (PreparedStatement deleteSupplierStatement = connection.prepareStatement(deleteSupplierQuery)) {
            deleteSupplierStatement.setInt(1, supplierId);
            deleteSupplierStatement.executeUpdate();
        }
    }

    private SupplierCardDTO mapRowToSupplierCard(ResultSet resultSet) throws SQLException {
        int supplierId = resultSet.getInt("supplier_id");
        int companyId = resultSet.getInt("company_id");
        String supplierName = resultSet.getString("supplier_name");
        String phoneNumber = resultSet.getString("phone_number");
        String bankAccount = resultSet.getString("bank_account");
        return new SupplierCardDTO(supplierId, supplierName, phoneNumber, companyId, bankAccount);
    }

    
    public List<ContactDTO> getSupplierContactsById(int supplierId) throws SQLException {
        List<ContactDTO> contacts = new ArrayList<>();
        String query = "SELECT * FROM supplier_contacts WHERE supplier_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, supplierId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int contactId = resultSet.getInt("contact_id");
                    ContactDTO contact = contactDAO.getById(contactId);
                    if (contact != null) {
                        contacts.add(contact);
                    }
                }
            }
        }
        return contacts;
    }

    public SupplierCardDTO getSupplierCardById(int supplierId) throws SQLException {
        String query = "SELECT * FROM supplier_cards WHERE supplier_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, supplierId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToSupplierCard(resultSet);
                }
            }
        }
        return null;
    }

    public Pair<List<SupplierCardDTO>,Integer> getSupps() throws SQLException {
        List<SupplierCardDTO> supps = new LinkedList<>();
        String query = "SELECT * FROM supplier_cards";
        try (PreparedStatement statement = connection.prepareStatement(query);
          ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                supps.add(mapRowToSupplierCard(resultSet));
            }
        }
        Integer counter=supps.size();

        Pair<List<SupplierCardDTO> , Integer> pair = new Pair(supps, counter);
        return pair;
        // SupplierController.getInstance().setSuppid_counter(counter);
        // for (SupplierCard s:supplierCards) {
        //    SupplierController.getInstance().suppliers.put(s.getSupplier_id(),s);
        //     s.setId_counter(getSupplierContactsById(s.getSupplier_id()).size());
        // }
    }

    public Integer get_cont_size()throws SQLException{
        int size = 0;
        String query = "SELECT * FROM contacts";
        try (PreparedStatement statement = connection.prepareStatement(query);
          ResultSet resultSet = statement.executeQuery()) {
              while(resultSet.next())
               size++;
        }
        return size;
    }

    public void deleteData() throws SQLException{
        try (Statement statement = connection.createStatement()) {
            String deleteSupplierContactsQuery = "DELETE FROM supplier_contacts";
            String deleteSupplierCardsQuery = "DELETE FROM supplier_cards";
            statement.executeUpdate(deleteSupplierContactsQuery);
            statement.executeUpdate(deleteSupplierCardsQuery);
        }
        for (SupplierCard s:SupplierController.getInstance().suppliers.values()){
            s.clearContactList();
        }
        SupplierController.getInstance().suppliers.clear();
        SupplierController.getInstance().setSuppid_counter(0);
    }

    public void delete_supp_contact(int supp_id, int contact_id) throws SQLException{
        String query = "DELETE FROM supplier_contacts WHERE supplier_id = ? AND contact_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, supp_id);
            statement.setInt(2, contact_id);
            statement.executeUpdate();
        }
    }
}
