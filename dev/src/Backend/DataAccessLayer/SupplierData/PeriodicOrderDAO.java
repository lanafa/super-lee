package Backend.DataAccessLayer.SupplierData;
import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.Controllers.Suppliers.OrdersController;
import Backend.BusinessLayer.objects.Suppliers.PeriodicOrder;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeriodicOrderDAO {
    private Connection connection;
    private RecordDAO recordDAO=RecordDAO.getInstance();
   //private OrdersController ordersController=OrdersController.getInstance();
    public static PeriodicOrderDAO getInstance() {
        if(instance == null){
            instance = new PeriodicOrderDAO();
        }
        return instance;
    }

    private static PeriodicOrderDAO instance = null;

    private PeriodicOrderDAO() {
        this.connection = DataBaseController.get_con();
    }





    public void insert(PeriodicOrderDTO periodicOrder, OrderDTO odto) throws SQLException {
        String query = "INSERT INTO periodic_orders (arrival_day,order_id) " +
                "VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, periodicOrder.getArrival_day());
            statement.setInt(2, periodicOrder.getOrder_id());
            statement.executeUpdate();
        }
        String last_query = "INSERT INTO orders (order_id, supplier_name, supplier_id, address, contact_number, order_price, contract_id,till_arrival,periodic) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement statement = connection.prepareStatement(last_query)) {
            statement.setInt(1, periodicOrder.getOrder_id());
            statement.setString(2, odto.getSupplier_name());
            statement.setInt(3, odto.getSupplier_id());
            statement.setString(4, odto.getAddress());
            statement.setString(5, odto.getContact_number());
            statement.setDouble(6, odto.getOrder_price());
            statement.setInt(7, odto.getContract_id());
            statement.setInt(8, 0);
            statement.setInt(9, 1);
            statement.executeUpdate();
        }
    }







    public void update(PeriodicOrderDTO periodicOrder) throws SQLException {
        String query = "UPDATE periodic_orders SET arrival_day = ? WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, periodicOrder.getArrival_day());
            statement.setInt(2, periodicOrder.getOrder_id());
            statement.executeUpdate();
        }
    }

    public void delete(int order_id) throws SQLException {
        String query = "DELETE FROM periodic_orders WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, order_id);
            statement.executeUpdate();
        }

        // String deleteNextRecordQuery = "DELETE FROM periodic_order_records WHERE order_id = ?";
        // try (PreparedStatement deleteNextRecordStatement = connection.prepareStatement(deleteNextRecordQuery)) {
        //     deleteNextRecordStatement.setInt(1, order_id);
        //     deleteNextRecordStatement.executeUpdate();
        // }
        String deletefromOrders = "DELETE FROM orders WHERE order_id = ?";
        try (PreparedStatement del = connection.prepareStatement(deletefromOrders)) {
            del.setInt(1, order_id);
            del.executeUpdate();
        }

        String deletefromrec = "DELETE FROM records WHERE order_id = ?";
        try (PreparedStatement del = connection.prepareStatement(deletefromrec)) {
            del.setInt(1, order_id);
            del.executeUpdate();
        }

        String deletefromperRec = "DELETE FROM periodic_order_records WHERE order_id = ?";
        try (PreparedStatement del = connection.prepareStatement(deletefromperRec)) {
            del.setInt(1, order_id);
            del.executeUpdate();
        }


    }

    private PeriodicOrderDTO mapRowToPeriodicOrder(ResultSet resultSet) throws SQLException {
        int order_id = resultSet.getInt("order_id");
        Integer arrival_day = resultSet.getInt("arrival_day");
        return new PeriodicOrderDTO(order_id,arrival_day);
    }

    public PeriodicOrderDTO getById(int order_id) throws SQLException {
        String query = "SELECT * FROM periodic_orders WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, order_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToPeriodicOrder(resultSet);
                }
            }
        }
        return null;
    }

    // private List<RecordDTO> getNextRecordsForOrder(int order_id) throws SQLException {
    //     List<RecordDTO> records = new ArrayList<>();
    //     String query = "SELECT * FROM periodic_order_records WHERE order_id = ?";
    //     try (PreparedStatement statement = connection.prepareStatement(query)) {
    //         statement.setInt(1, order_id);
    //         try (ResultSet resultSet = statement.executeQuery()) {
    //             while (resultSet.next()) {
    //                 RecordDTO record = recordDAO.getById(order_id);
    //                 records.add(record);
    //             }
    //         }
    //     }
    //     return records;
    // }

    // public int loadData()throws SQLException{
    //     List<PeriodicOrder> periodicOrders = new ArrayList<>();
    //     String query = "SELECT * FROM periodic_orders";
    //     try (PreparedStatement statement = connection.prepareStatement(query);
    //          ResultSet resultSet = statement.executeQuery()) {
    //         while (resultSet.next()) {
    //             periodicOrders.add(mapRowToPeriodicOrder(resultSet).dto2ObjectPeriodic());
    //         }
    //     }
    //     int perCounter=periodicOrders.size();
    //     for (PeriodicOrder p:periodicOrders) {
    //        OrdersController.getInstance().periodic_orders.put(p.getOrder_id(),p);
    //     }
    //     return perCounter;
    // }

    public void deleteData()throws SQLException{
        try (Statement statement = connection.createStatement()) {
            String deletePeriodicOrderRecordsQuery = "DELETE FROM periodic_order_records";
            String deletePeriodicOrdersQuery = "DELETE FROM periodic_orders";
            statement.executeUpdate(deletePeriodicOrderRecordsQuery);
            statement.executeUpdate(deletePeriodicOrdersQuery);
        }
        for (PeriodicOrder o:OrdersController.getInstance().periodic_orders.values()){
            o.clearPerRecords();
        }
        OrdersController.getInstance().periodic_orders.clear();
    }
}
