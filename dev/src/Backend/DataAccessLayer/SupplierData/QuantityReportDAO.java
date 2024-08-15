package Backend.DataAccessLayer.SupplierData;
import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.objects.Suppliers.Pair;

import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class QuantityReportDAO {
    private Connection connection;


    public static QuantityReportDAO getInstance() {
        if(instance == null){
            instance = new QuantityReportDAO();
        }
        return instance;
    }
    private static QuantityReportDAO instance = null;


    private QuantityReportDAO() {
        this.connection = DataBaseController.get_con();
    }

    public void insert(QuantityReportDTO quantityReport) throws SQLException {
        String query = "INSERT INTO quantity_report_prices (contract_id,product_id, price) VALUES (?, ?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(query)){

            for (Map.Entry<Integer, Double> entry :quantityReport.getProducts_prices().entrySet()) {
                int productId = entry.getKey();
                double price = entry.getValue();
                statement.setInt(1, quantityReport.getContract_id());
                statement.setInt(2, productId);
                statement.setInt(3, (int)price);
                statement.executeUpdate();
            }
        }
        // Insert discounts
        Map<Integer, Map<Integer, Pair<Character, Integer>>> discounts = quantityReport.getDiscounts();
        if (discounts != null && !discounts.isEmpty()) {
            String discountsQuery = "INSERT INTO quantity_report_discounts (contract_id, product_id, quantity, p_v, discount) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement discountsStatement = connection.prepareStatement(discountsQuery)) {
                for (Map.Entry<Integer, Map<Integer, Pair<Character, Integer>>> entry : discounts.entrySet()) {
                    int itemId = entry.getKey();
                    Map<Integer, Pair<Character, Integer>> itemDiscounts = entry.getValue();
                    for (Map.Entry<Integer, Pair<Character, Integer>> itemEntry : itemDiscounts.entrySet()) {
                        int quantity = itemEntry.getKey();
                        Pair<Character, Integer> discount = itemEntry.getValue();
                        discountsStatement.setInt(1, quantityReport.getContract_id());
                        discountsStatement.setInt(2, itemId);
                        discountsStatement.setInt(3, quantity);
                        discountsStatement.setString(4, String.valueOf(discount.getKey()));
                        discountsStatement.setInt(5, discount.getValue());
                        discountsStatement.addBatch();
                    }
                }
                discountsStatement.executeBatch();
            }
        }

        // Insert overall discounts
        Map<Integer, Pair<Character, Integer>> ovlDiscounts = quantityReport.getOvl_discount();
        if (ovlDiscounts != null && !ovlDiscounts.isEmpty()) {
            String ovlDiscountsQuery = "INSERT INTO quantity_report_ovl_discounts (contract_id, quantity, p_v, discount) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ovlDiscountsStatement = connection.prepareStatement(ovlDiscountsQuery)) {
                for (Map.Entry<Integer, Pair<Character, Integer>> entry : ovlDiscounts.entrySet()) {
                    int quantity = entry.getKey();
                    Pair<Character, Integer> discount = entry.getValue();
                    ovlDiscountsStatement.setInt(1, quantityReport.getContract_id());
                    ovlDiscountsStatement.setInt(2, quantity);
                    ovlDiscountsStatement.setString(3, String.valueOf(discount.getKey()));
                    ovlDiscountsStatement.setInt(4, discount.getValue());
                    ovlDiscountsStatement.addBatch();
                }
                ovlDiscountsStatement.executeBatch();
            }
        }
    }

    public void update(QuantityReportDTO quantityReport) throws SQLException {
        String deleteprice = "DELETE FROM quantity_report_prices WHERE contract_id = ?";
        try (PreparedStatement deleteprice1 = connection.prepareStatement(deleteprice)) {
            deleteprice1.setInt(1, quantityReport.getContract_id());
            deleteprice1.executeUpdate();
        }
        // Delete existing discounts for the contract_id
        String deleteDiscountsQuery = "DELETE FROM quantity_report_discounts WHERE contract_id = ?";
        try (PreparedStatement deleteDiscountsStatement = connection.prepareStatement(deleteDiscountsQuery)) {
            deleteDiscountsStatement.setInt(1, quantityReport.getContract_id());
            deleteDiscountsStatement.executeUpdate();
        }

        // Delete existing overall discounts for the contract_id
        String deleteOvlDiscountsQuery = "DELETE FROM quantity_report_ovl_discounts WHERE contract_id = ?";
        try (PreparedStatement deleteOvlDiscountsStatement = connection.prepareStatement(deleteOvlDiscountsQuery)) {
            deleteOvlDiscountsStatement.setInt(1, quantityReport.getContract_id());
            deleteOvlDiscountsStatement.executeUpdate();
        }

        // Insert new discounts
        Map<Integer, Map<Integer, Pair<Character, Integer>>> discounts = quantityReport.getDiscounts();
        if (discounts != null && !discounts.isEmpty()) {
            String discountsQuery = "INSERT INTO quantity_report_discounts (contract_id, product_id, quantity, p_v, discount) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement discountsStatement = connection.prepareStatement(discountsQuery)) {
                for (Map.Entry<Integer, Map<Integer, Pair<Character, Integer>>> entry : discounts.entrySet()) {
                    int itemId = entry.getKey();
                    Map<Integer, Pair<Character, Integer>> itemDiscounts = entry.getValue();
                    for (Map.Entry<Integer, Pair<Character, Integer>> itemEntry : itemDiscounts.entrySet()) {
                        int quantity = itemEntry.getKey();
                        Pair<Character, Integer> discount = itemEntry.getValue();
                        discountsStatement.setInt(1, quantityReport.getContract_id());
                        discountsStatement.setInt(2, itemId);
                        discountsStatement.setInt(3, quantity);
                        discountsStatement.setString(4, String.valueOf(discount.getKey()));
                        discountsStatement.setInt(5, discount.getValue());
                        discountsStatement.addBatch();
                    }
                }
                discountsStatement.executeBatch();
            }
        }

        // Insert new overall discounts
        Map<Integer, Pair<Character, Integer>> ovlDiscounts = quantityReport.getOvl_discount();
        if (ovlDiscounts != null && !ovlDiscounts.isEmpty()) {
            String ovlDiscountsQuery = "INSERT INTO quantity_report_ovl_discounts (contract_id, quantity, p_v, discount) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ovlDiscountsStatement = connection.prepareStatement(ovlDiscountsQuery)) {
                for (Map.Entry<Integer, Pair<Character, Integer>> entry : ovlDiscounts.entrySet()) {
                    int quantity = entry.getKey();
                    Pair<Character, Integer> discount = entry.getValue();
                    ovlDiscountsStatement.setInt(1, quantityReport.getContract_id());
                    ovlDiscountsStatement.setInt(2, quantity);
                    ovlDiscountsStatement.setString(3, String.valueOf(discount.getKey()));
                    ovlDiscountsStatement.setInt(4, discount.getValue());
                    ovlDiscountsStatement.addBatch();
                }
                ovlDiscountsStatement.executeBatch();
            }
        }

        String query = "INSERT INTO quantity_report_prices (contract_id,product_id, price) VALUES (?, ?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(query)){

            for (Map.Entry<Integer, Double> entry :quantityReport.getProducts_prices().entrySet()) {
                int productId = entry.getKey();
                double price = entry.getValue();
                statement.setInt(1, quantityReport.getContract_id());
                statement.setInt(2, productId);
                statement.setInt(3, (int)price);
                statement.executeUpdate();
            }
        }
    }

    public void delete(int contractId) throws SQLException {
        // Delete from quantity_report table
        String deleteQuery = "DELETE FROM quantity_report_prices WHERE contract_id = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
            deleteStatement.setInt(1, contractId);
            deleteStatement.executeUpdate();
        }

        // Delete from quantity_report_discounts table
        String deleteDiscountsQuery = "DELETE FROM quantity_report_discounts WHERE contract_id = ?";
        try (PreparedStatement deleteDiscountsStatement = connection.prepareStatement(deleteDiscountsQuery)) {
            deleteDiscountsStatement.setInt(1, contractId);
            deleteDiscountsStatement.executeUpdate();
        }

        // Delete from quantity_report_ovl_discounts table
        String deleteOvlDiscountsQuery = "DELETE FROM quantity_report_ovl_discounts WHERE contract_id = ?";
        try (PreparedStatement deleteOvlDiscountsStatement = connection.prepareStatement(deleteOvlDiscountsQuery)) {
            deleteOvlDiscountsStatement.setInt(1, contractId);
            deleteOvlDiscountsStatement.executeUpdate();
        }
    }
    private void getAllDiscounts(QuantityReportDTO quantityReport, int contractId) throws SQLException {
        String query = "SELECT * FROM quantity_report_discounts WHERE contract_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, contractId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int itemId = resultSet.getInt("item_id");
                    int quantity = resultSet.getInt("quantity");
                    char pV = resultSet.getString("p_v").charAt(0);
                    int discount = resultSet.getInt("discount");
                    quantityReport.addDiscount(itemId, quantity, new Pair<>(pV, discount));
                }
            }
        }
    }

    private void getAllOverallDiscounts(QuantityReportDTO quantityReport, int contractId) throws SQLException {
        String query = "SELECT * FROM quantity_report_ovl_discounts WHERE contract_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, contractId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int quantity = resultSet.getInt("quantity");
                    char pV = resultSet.getString("p_v").charAt(0);
                    int discount = resultSet.getInt("discount");
                    quantityReport.addOverallDiscount(quantity, new Pair<>(pV, discount));
                }
            }
        }
    }
    private QuantityReportDTO mapRowToQuantityReport(ResultSet resultSet) throws SQLException {
        int contractId = resultSet.getInt("contract_id");
        QuantityReportDTO quantityReport = new QuantityReportDTO(contractId);
        getAllDiscounts(quantityReport, contractId);
        getAllOverallDiscounts(quantityReport, contractId);
        return quantityReport;
    }

    public QuantityReportDTO getQuantityReportById(int contractId) throws SQLException {
        QuantityReportDTO quantityReport = null;
        String query = "SELECT * FROM quantity_report WHERE contract_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, contractId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    quantityReport = mapRowToQuantityReport(resultSet);
                }
            }
        }
        return quantityReport;
    }
    public void deleteData()throws SQLException{
        try (Statement statement = connection.createStatement()) {
            String deleteQuantityReportDiscountsQuery = "DELETE FROM quantity_report_discounts";
            String deleteQuantityReportOvlDiscountsQuery = "DELETE FROM quantity_report_ovl_discounts";
            String deleteprices="DELETE FROM quantity_report_prices";
            statement.executeUpdate(deleteQuantityReportDiscountsQuery);
            statement.executeUpdate(deleteQuantityReportOvlDiscountsQuery);
            statement.executeUpdate(deleteprices);
        }
    }

}
