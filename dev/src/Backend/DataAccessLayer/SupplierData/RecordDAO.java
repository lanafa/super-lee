package Backend.DataAccessLayer.SupplierData;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.Controllers.Suppliers.SupplierController;

import java.nio.file.Paths;
import java.sql.*;

public class RecordDAO {
    private Connection connection;
    public static RecordDAO getInstance() {
        if(instance == null){
            instance = new RecordDAO();
        }
        return instance;
    }

    private static RecordDAO instance = null;

    private RecordDAO() {

        this.connection = DataBaseController.get_con();
    }

    public void insert(RecordDTO record) throws SQLException {
        String query = "INSERT INTO records (order_id, supplier_id, quantity, ovl_price, discount, final_price, product_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, record.getOrder_id());
            statement.setInt(2, record.getSupplier_id());
            statement.setInt(3, record.getQuantity());
            statement.setDouble(4, record.getOvl_price());
            statement.setDouble(5, record.getDiscount());
            statement.setDouble(6, record.getFinal_price());
            statement.setInt(7, record.getProduct_id());
            statement.executeUpdate();
        }
    }

    public void insert2(int order_id,int prod_id,int q,int p) throws SQLException{
        String query = "INSERT INTO periodic_order_records (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, order_id);
            statement.setInt(2, prod_id);
            statement.setInt(3, q);
            statement.setInt(4, p);
            statement.executeUpdate();
        }

    }

    public void update(RecordDTO record) throws SQLException {
        String query = "UPDATE records SET quantity = ?, ovl_price = ?, discount = ?, final_price = ? WHERE order_id = ? AND supplier_id = ? AND product_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, record.getQuantity());
            statement.setDouble(2, record.getOvl_price());
            statement.setDouble(3, record.getDiscount());
            statement.setDouble(4, record.getFinal_price());
            statement.setInt(5, record.getOrder_id());
            statement.setInt(6, record.getSupplier_id());
            statement.setInt(7, record.getProduct_id());
            statement.executeUpdate();
        }
        
    }

    public void update2(int order_id, int prod_id, int q, int p) throws SQLException {
        String query = "UPDATE periodic_order_records SET quantity = ?, price = ? WHERE order_id = ? AND product_id = ? ";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, q);
            statement.setInt(2, p);
            statement.setInt(3, order_id);
            statement.setInt(4, prod_id);
            statement.executeUpdate();
        }
        

    }

    public void delete(int orderId,int product_id) throws SQLException {
        String query = "DELETE FROM records WHERE order_id = ? AND product_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            statement.setInt(2, product_id);
            statement.executeUpdate();
        }
    }

    public void delete2(int orderId,int product_id) throws SQLException {
        String query = "DELETE FROM periodic_order_records WHERE order_id = ? AND product_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            statement.setInt(2, product_id);
            statement.executeUpdate();
        }
    }

    private RecordDTO mapRowToRecord(ResultSet resultSet) throws SQLException {
        int orderId = resultSet.getInt("order_id");
        int supplierId = resultSet.getInt("supplier_id");
        int quantity = resultSet.getInt("quantity");
        double ovlPrice = resultSet.getDouble("ovl_price");
        double discount = resultSet.getDouble("discount");
        double finalPrice = resultSet.getDouble("final_price");
        int productId = resultSet.getInt("product_id");

        return new RecordDTO(supplierId,orderId ,quantity, discount, finalPrice,productId,ovlPrice);
    }

    public RecordDTO getById(int orderId) throws SQLException {
        String query = "SELECT * FROM records WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToRecord(resultSet);
                }
            }
        }
        return null;
    }

    public void deleteData()throws SQLException{
        try (Statement statement = connection.createStatement()) {
            String deleteRecordsQuery = "DELETE FROM records";
            statement.executeUpdate(deleteRecordsQuery);
        }
    }
}
