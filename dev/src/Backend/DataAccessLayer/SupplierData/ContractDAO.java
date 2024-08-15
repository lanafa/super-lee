package Backend.DataAccessLayer.SupplierData;
import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.Controllers.Suppliers.ContractController;
import Backend.BusinessLayer.Controllers.Suppliers.OrdersController;
import Backend.BusinessLayer.objects.Suppliers.Contract;
import Backend.BusinessLayer.objects.Suppliers.Pair;
import Backend.DataAccessLayer.stockData.ProductDAO;
import Backend.DataAccessLayer.stockData.ProductDTO;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContractDAO {
    private Connection connection;
    private ProductDAO productDAO=ProductDAO.getInstance();
    private OrderDAO orderDAO=OrderDAO.getInstance();
    //private ContractController contractController=ContractController.getInstance();
    //private OrdersController ordersController=OrdersController.getInstance();
    private QuantityReportDAO quantityReportDAO=QuantityReportDAO.getInstance();

    public static ContractDAO getInstance() {
        if(instance == null){
            instance = new ContractDAO();
        }
        return instance;
    }

    private static ContractDAO instance = new ContractDAO();

    private ContractDAO() {
        this.connection = DataBaseController.get_con();
    }

    public void insert(ContractDTO contract)throws SQLException{
        String query = "INSERT INTO contract (contractId, supplierId, PaymentMethod, NetTerm,supplierConfig,daysToReady) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, contract.getContractId());
            statement.setInt(2, contract.getSupplierId());
            statement.setString(3, contract.getPaymentMethod());
            statement.setString(4, contract.getNetTerm());
            statement.setString(5, contract.getSupplierConfigString());
            statement.setInt(6,contract.getDays_toReady());
            statement.executeUpdate();
        }
    }

    public void insert2(int contract_id,int product_id,int amount) throws SQLException{
        String query = "INSERT INTO item_list (contract_id, product_id, amount) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, contract_id);
            statement.setInt(2, product_id);
            statement.setInt(3,amount);
            statement.executeUpdate();
        }
    }

    public void update(ContractDTO contract) throws SQLException {
        String query = "UPDATE contract SET supplierId = ?, PaymentMethod = ?, NetTerm = ?, supplierConfig = ? ,daysToReady = ? WHERE contractId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, contract.getSupplierId());
            statement.setString(2, contract.getPaymentMethod());
            statement.setString(3, contract.getNetTerm());
            statement.setString(4, contract.getSupplierConfigString());
            statement.setInt(5, contract.getDays_toReady());
            statement.setInt(6,contract.getContractId());
            statement.executeUpdate();
        }
    }

    public void delete(int contractId) throws SQLException {
        String deleteOrderHistoryQuery = "DELETE FROM order_history WHERE contract_id = ?";
        try (PreparedStatement orderHistoryStatement = connection.prepareStatement(deleteOrderHistoryQuery)) {
            orderHistoryStatement.setInt(1, contractId);
            orderHistoryStatement.executeUpdate();
        }

        String deleteItemListQuery = "DELETE FROM item_list WHERE contract_id = ?";
        try (PreparedStatement itemListStatement = connection.prepareStatement(deleteItemListQuery)) {
            itemListStatement.setInt(1, contractId);
            itemListStatement.executeUpdate();
        }

        String deleteContractQuery = "DELETE FROM contract WHERE contractId = ?";
        try (PreparedStatement contractStatement = connection.prepareStatement(deleteContractQuery)) {
            contractStatement.setInt(1, contractId);
            contractStatement.executeUpdate();
        }
    }
    private List<OrderDTO> getOrderHistory(int contractId) throws SQLException {
        List<OrderDTO> orderHistory = new ArrayList<>();
        String query = "SELECT order_id FROM order_history WHERE contract_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, contractId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("order_id");
                    OrderDTO order = getOrder(orderId);
                    orderHistory.add(order);
                }
            }
        }
        return orderHistory;
    }

    private OrderDTO getOrder(int orderId) throws SQLException {
        return orderDAO.getById(orderId);
    }

    // private Map<ProductDTO, Integer> getItemList(int contractId) throws SQLException {
    //     Map<ProductDTO, Integer> itemList = new HashMap<>();
    //     String query = "SELECT product_id, amount FROM item_list WHERE contract_id = ?";
    //     try (PreparedStatement statement = connection.prepareStatement(query)) {
    //         statement.setInt(1, contractId);
    //         try (ResultSet rs = statement.executeQuery()) {
    //             while (rs.next()) {
    //                 int productId = rs.getInt("product_id");
    //                 int amount = rs.getInt("amount");
    //                 ProductDTO product = productDAO.getProductById(productId);
    //                 itemList.put(product, amount);
    //             }
    //         }
    //     }
    //     return itemList;
    // }
    public Map<Integer, Integer> getItemList(int contractId) throws SQLException {
        Map<Integer, Integer> itemList = new HashMap<>();
        String query = "SELECT product_id, amount FROM item_list WHERE contract_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, contractId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    int amount = rs.getInt("amount");
                    itemList.put(productId, amount);
                }
            }
        }
        return itemList;
    }

    public ContractDTO getContractById(int contractId) throws SQLException {
        String query = "SELECT * FROM contract WHERE contractId = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, contractId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapRowToContract(rs);
                }
            }
        }
        return null;
    }

    public ContractDTO mapRowToContract(ResultSet rs) throws SQLException {
        int contractId = rs.getInt("contractId");
        int supplierId = rs.getInt("supplierId");
        String paymentMethod = rs.getString("PaymentMethod");
        String netTerm = rs.getString("NetTerm");
        String supplierConfigString = rs.getString("supplierConfig");
        Pair toadd=new Pair(false,false);
        int daystoready=rs.getInt("daysToReady");
        PaymentDTO p= new PaymentDTO(PaymentDTO.PaymentMethod.valueOf(paymentMethod),PaymentDTO.NetTerm.valueOf(netTerm));
        if (supplierConfigString=="11"){
            toadd=new Pair(true,true);
        }
        else if (supplierConfigString=="01"){
            toadd=new Pair(false,true);
        }
        else if (supplierConfigString=="10"){
            toadd=new Pair(true,false);
        }
        ContractDTO contract = new ContractDTO(contractId, supplierId,p,toadd,daystoready);
        return contract;
    }

    public Pair<List<ContractDTO>,Integer> get_contracts() throws  SQLException{
        List<ContractDTO> contracts = new ArrayList<>();
        String query = "SELECT * FROM contract";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                contracts.add(mapRowToContract(resultSet));
            }
        }
        int counter=contracts.size();
        Pair<List<ContractDTO>,Integer> pair = new Pair<List<ContractDTO>,Integer>(contracts,counter);
        return pair;
        }




    public void deleteData() throws SQLException{
        try (Statement statement = connection.createStatement()) {
            String deleteOrderQuery = "DELETE FROM order_history";
            String deleteItemQuery = "DELETE FROM item_list";
            String deleteContractQuery = "DELETE FROM contract";
            String del = "DELETE FROM quantity_report_discounts";
            String del1 = "DELETE FROM quantity_report_ovl_discounts";
            String del2 = "DELETE FROM quantity_report_prices";

            statement.executeUpdate(deleteOrderQuery);
            statement.executeUpdate(deleteItemQuery);
            statement.executeUpdate(deleteContractQuery);
            statement.executeUpdate(del);
            statement.executeUpdate(del1);
            statement.executeUpdate(del2);
        }
        for (Contract c:ContractController.getInstance().contracts.values()){
            c.clearData();
        }
        ContractController.getInstance().contracts.clear();
        ContractController.getInstance().supp_contracts.clear();
        ContractController.getInstance().setContract_id(0);
        OrdersController.getInstance().suppliers_contracts.clear();
    }

    public Map<Integer,Integer> get_orderHistory(int cont_id ,int periodic) throws SQLException{
        Map<Integer,Integer> map = new HashMap<>();
        String query = "SELECT * FROM order_history WHERE contract_id = ? AND periodic = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cont_id);
            statement.setInt(2, periodic);
            try(ResultSet res = statement.executeQuery()){
                while(res.next()){
                    int ord_id = res.getInt("order_id");
                    int copies = res.getInt("copies");
                    map.put(ord_id, copies);
                }
            }
            return map;
        }
    }

    public QuantityReportDTO get_quantityreport(int contract_id)throws SQLException{
        Map<Integer,Double> products_price = new HashMap<>();
        Map<Integer, Map<Integer, Pair<Character, Integer>>> discounts = new HashMap<>();
        Map<Integer, Pair<Character, Integer>> ovl_discount = new HashMap<>();
        String query = "SELECT * FROM quantity_report_prices WHERE contract_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, contract_id);
            try(ResultSet res = statement.executeQuery()){
                while(res.next()){
                    int prod_id = res.getInt("product_id");
                    Double price = res.getDouble("price");
                    products_price.put(prod_id, price);
                }
            }
        }
        String query2 = "SELECT * FROM quantity_report_ovl_discounts WHERE contract_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query2)) {
            statement.setInt(1, contract_id);
            try(ResultSet res = statement.executeQuery()){
                while(res.next()){
                    int quantity = res.getInt("quantity");
                    Character p_v = res.getString("p_v").charAt(0);
                    int discount = res.getInt("discount");
                    Pair<Character,Integer> pair = new Pair<Character,Integer>(p_v, discount);
                    ovl_discount.put(quantity, pair);
                }
            }
        }

        String query3 = "SELECT * FROM quantity_report_discounts WHERE contract_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query3)) {
            statement.setInt(1, contract_id);
            try(ResultSet res = statement.executeQuery()){
                while(res.next()){
                    int prod_id = res.getInt("product_id");
                    int quantity = res.getInt("quantity");
                    Character p_v = res.getString("p_v").charAt(0);
                    int discount = res.getInt("discount");
                    Pair<Character,Integer> pair = new Pair<Character,Integer>(p_v, discount);
                    if(!discounts.containsKey(prod_id)){
                        discounts.put(prod_id, new HashMap<>());
                        discounts.get(prod_id).put(quantity, pair);
                    }else{
                        discounts.get(prod_id).put(quantity, pair);
                    }

                }
            }
        }

        QuantityReportDTO qdto = new QuantityReportDTO(contract_id, discounts, ovl_discount, products_price);
        return qdto;
    }

    
}
