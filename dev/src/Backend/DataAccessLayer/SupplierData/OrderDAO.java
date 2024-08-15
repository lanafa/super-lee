package Backend.DataAccessLayer.SupplierData;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.Controllers.Suppliers.OrdersController;
import Backend.BusinessLayer.objects.Suppliers.Order;
import Backend.BusinessLayer.objects.Suppliers.Pair;
import Backend.DataAccessLayer.stockData.ProductDAO;
import Backend.DataAccessLayer.stockData.ProductDTO;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAO {
    private Connection connection;

    private RecordDAO recordDAO=RecordDAO.getInstance();
    private PeriodicOrderDAO periodicOrderDAO=PeriodicOrderDAO.getInstance();
    //public OrdersController ordersController=OrdersController.getInstance();

    public static OrderDAO getInstance() {
        if(instance == null){
            instance = new OrderDAO();
        }
        return instance;
    }

    private static OrderDAO instance = null;


    private OrderDAO() {
        this.connection = DataBaseController.get_con();
    }
    public void insert(OrderDTO order) throws SQLException {
        String query = "INSERT INTO orders (order_id, supplier_name, supplier_id, address, contact_number, order_price, contract_id,till_arrival) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, order.getOrder_id());
            statement.setString(2, order.getSupplier_name());
            statement.setInt(3, order.getSupplier_id());
            statement.setString(4, order.getAddress());
            statement.setString(5, order.getContact_number());
            statement.setDouble(6, order.getOrder_price());
            statement.setInt(7, order.getContract_id());
            statement.setInt(8, order.getTill_arrival());
            statement.executeUpdate();
        }

        // List<RecordDTO> itemList = order.getItem_list();
        // if (itemList != null && !itemList.isEmpty()) {
        //     String recordQuery = "INSERT INTO order_records (order_id, product_id, quantity) VALUES (?, ?, ?)";
        //     try (PreparedStatement recordStatement = connection.prepareStatement(recordQuery)) {
        //         for (RecordDTO record : itemList) {
        //             recordStatement.setInt(1, order.getOrder_id());
        //             recordStatement.setInt(2, record.getProduct_id());
        //             recordStatement.setInt(3, record.getQuantity());
        //             recordStatement.addBatch();
        //         }
        //         recordStatement.executeBatch();
        //     }
        // }
    }

    public void update(OrderDTO order) throws SQLException {
        String query = "UPDATE orders SET supplier_name = ?, supplier_id = ?, address = ?, contact_number = ?, order_price = ?, contract_id = ? ,till_arrival= ? WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, order.getSupplier_name());
            statement.setInt(2, order.getSupplier_id());
            statement.setString(3, order.getAddress());
            statement.setString(4, order.getContact_number());
            statement.setDouble(5, order.getOrder_price());
            statement.setInt(6, order.getContract_id());
            statement.setInt(7, order.getOrder_id());
            statement.setInt(8, order.getTill_arrival());
            statement.executeUpdate();
        }

        // List<RecordDTO> itemList = order.getItem_list();
        // if (itemList != null && !itemList.isEmpty()) {
        //     deleteRecordsByOrderId(order.getOrder_id());
        //     insertRecords(itemList, order.getOrder_id());
        // }
    }

    public void delete(int orderId) throws SQLException {
        deleteRecordsByOrderId(orderId);
        String query = "DELETE FROM orders WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            statement.executeUpdate();
        }
    }

    private void deleteRecordsByOrderId(int orderId) throws SQLException {
        String query = "DELETE FROM order_records WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            statement.executeUpdate();
        }
    }

    private void insertRecords(List<RecordDTO> itemList, int orderId) throws SQLException {
        String recordQuery = "INSERT INTO order_records (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement recordStatement = connection.prepareStatement(recordQuery)) {
            for (RecordDTO record : itemList) {
                recordStatement.setInt(1, orderId);
                recordStatement.setInt(2, record.getProduct_id());
                recordStatement.setInt(3, record.getQuantity());
                recordStatement.addBatch();
            }
            recordStatement.executeBatch();
        }
    }

    public OrderDTO getById(int orderId) throws SQLException {
        String orderQuery = "SELECT * FROM orders WHERE order_id = ?";
        try (PreparedStatement orderStatement = connection.prepareStatement(orderQuery)) {
            orderStatement.setInt(1, orderId);
            try (ResultSet orderResultSet = orderStatement.executeQuery()) {
                if (orderResultSet.next()) {
                    return mapRowToOrder(orderResultSet);
                }
            }
        }
        return null;
    }

    public Map<RecordDTO,ProductDTO> getRecordsByOrderId(int orderId, int supp_id) throws SQLException {
        Map<RecordDTO,ProductDTO> records = new HashMap<>();
        String recordQuery = "SELECT * FROM records WHERE order_id = ? AND supplier_id = ?";
        try (PreparedStatement recordStatement = connection.prepareStatement(recordQuery)) {
            recordStatement.setInt(1, orderId);
            recordStatement.setInt(2, supp_id);
            try (ResultSet recordResultSet = recordStatement.executeQuery()) {
                while (recordResultSet.next()) {
                    int prod_id = recordResultSet.getInt("product_id");
                    int order_id = recordResultSet.getInt("order_id");
                    int supplier_id = recordResultSet.getInt("supplier_id");
                    int quantity = recordResultSet.getInt("quantity");
                    double ovl_price = recordResultSet.getDouble("ovl_price");
                    double discount = recordResultSet.getDouble("discount");
                    double final_price = recordResultSet.getDouble("final_price");
                    RecordDTO curr = new RecordDTO(supplier_id, order_id, quantity, discount, final_price, prod_id, ovl_price);
                    String inner_query = "SELECT * FROM products WHERE id = ?";
                    try (PreparedStatement innerStatement = connection.prepareStatement(inner_query)) {
                        innerStatement.setInt(1, prod_id);
                        try (ResultSet innerResultSet = innerStatement.executeQuery()) {
                            if (innerResultSet.next()) {
                                ProductDTO pdto = ProductDAO.getInstance().mapRowToProduct(innerResultSet);
                                records.put(curr, pdto);
                            }
                        }
                    }

                }
            }
        }
        return records;
    }

    private OrderDTO mapRowToOrder(ResultSet resultSet) throws SQLException {
        int orderId = resultSet.getInt("order_id");
        String supplierName = resultSet.getString("supplier_name");
        int supplierId = resultSet.getInt("supplier_id");
        String address = resultSet.getString("address");
        String contactNumber = resultSet.getString("contact_number");
        double orderPrice = resultSet.getDouble("order_price");
        int contractId = resultSet.getInt("contract_id");
        int tillArrive=resultSet.getInt("till_arrival");
        return new OrderDTO(orderId, supplierName, supplierId,address,contactNumber,contractId,orderPrice,tillArrive);
    }


    //perioid = 1 , gets periodic orders otherwise gets normal orders(shortage) with the size of each
    public Pair<List<OrderDTO>,Integer> get_orders(int periodic) throws SQLException{
        List<OrderDTO> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE periodic = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, periodic);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(mapRowToOrder(resultSet));
            }
        }
        int counter= orders.size();
        Pair<List<OrderDTO>,Integer> pair = new Pair<List<OrderDTO>,Integer>(orders, counter);
        return pair;
    }

    public void deleteData() throws SQLException{
        try (Statement statement = connection.createStatement()) {
            String deleteOrderRecordsQuery = "DELETE FROM order_records";
            String deleteOrdersQuery = "DELETE FROM orders";

            statement.executeUpdate(deleteOrderRecordsQuery);
            statement.executeUpdate(deleteOrdersQuery);
        }
        for (Order o:OrdersController.getInstance().orders.values()){
            o.clearRecords();
        }
        OrdersController.getInstance().orders.clear();
        OrdersController.getInstance().periodic_orders.clear();
        OrdersController.getInstance().setOrder_counter(0);
    }
    
    public void addToOrderHistory(int contract_id,int order_id) throws SQLException{
        String query = "INSERT INTO order_history (contract_id, order_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, contract_id);
            statement.setInt(2, order_id);
            statement.executeUpdate();
        }
        
    }
    public void insert_to_order_history(int cont_id,int ord_id, int periodic)throws SQLException {
        String query = "INSERT OR IGNORE INTO order_history (contract_id, order_id, copies, periodic)"+
        "VALUES (?, ?, 1, ?) ON CONFLICT(contract_id,order_id) DO UPDATE SET copies = copies + 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cont_id);
            statement.setInt(2, ord_id);
            statement.setInt(3, periodic);
            statement.executeUpdate();
        }

    }
    public void insert_to_per_order_history(int cont_id,int ord_id, int periodic)throws SQLException {
    }
}
