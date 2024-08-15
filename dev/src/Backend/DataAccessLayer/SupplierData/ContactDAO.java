package Backend.DataAccessLayer.SupplierData;
import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.objects.Suppliers.Contact;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ContactDAO {
    private Connection connection;

    public static ContactDAO getInstance() {
        if(instance == null){
            instance = new ContactDAO();
        }
        return instance;
    }

    private static ContactDAO instance =null;


    private ContactDAO() {
        this.connection = DataBaseController.get_con();
    }

    public void insert(ContactDTO contact) throws SQLException {
        String query = "INSERT INTO contacts (id, name, phone_number, email, company_number, fax) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, contact.getId());
            statement.setString(2, contact.getName());
            statement.setString(3, contact.getPhoneNumber());
            statement.setString(4, contact.getEmail());
            statement.setInt(5, contact.getCompanyNumber());
            statement.setString(6, contact.getFax());
            statement.executeUpdate();
        }
    }

    public void update(ContactDTO contact) throws SQLException {
        String query = "UPDATE contacts SET name = ?, phone_number = ?, email = ?, company_number = ?, fax = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getPhoneNumber());
            statement.setString(3, contact.getEmail());
            statement.setInt(4, contact.getCompanyNumber());
            statement.setString(5, contact.getFax());
            statement.setInt(6, contact.getId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String query = "DELETE FROM contacts WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public ContactDTO getById(int id) throws SQLException {
        String query = "SELECT * FROM contacts WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToContact(resultSet);
                }
            }
        }
        return null;
    }

    public List<ContactDTO> getAll() throws SQLException {
        String query = "SELECT * FROM contacts";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            List<ContactDTO> contacts = new ArrayList<>();
            while (resultSet.next()) {
                ContactDTO contact = mapRowToContact(resultSet);
                contacts.add(contact);
            }
            return contacts;
        }
    }

    private ContactDTO mapRowToContact(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String phoneNumber = resultSet.getString("phone_number");
        String email = resultSet.getString("email");
        int companyNumber = resultSet.getInt("company_number");
        String fax = resultSet.getString("fax");
        return new ContactDTO(id, name, phoneNumber, email, companyNumber, fax);
    }

    public void deleteData() throws SQLException{
        try (Statement statement = connection.createStatement()) {
            String deleteContactsQuery = "DELETE FROM contacts";
            statement.executeUpdate(deleteContactsQuery);
        }
    }
}
