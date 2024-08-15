package Backend.DataAccessLayer.stockData;
import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.Controllers.Stock.productscontroller;

public class ProductDAO {
    public static ProductDAO getInstance() {
        if(instance == null){
            instance = new ProductDAO();
        }
        return instance;
    }


    private static ProductDAO instance =null;

protected Connection connection2(){

    String path = Paths.get("").toAbsolutePath().toString();
    String _connectionString = "jdbc:sqlite:" + path+"/superli.db";
    Connection connection=null;
    try {
      connection = DriverManager.getConnection(_connectionString);
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return connection;
    }
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // }
    
    public void open() {
        try {
            String path = new File("superli.db").getAbsolutePath();
            DriverManager.getConnection("jdbc:sqlite:" + path);
            } catch (SQLException e) {
            e.printStackTrace();
        }
    }
private Connection connection;


    public ProductDAO() {
        this.connection = DataBaseController.get_con();
    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public void insert(ProductDTO productDTO) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("INSERT INTO products (manufacture,id,minAmount,shelfId,nameproduct,manufacturePrice,discountPrice,sellingAmount,Category,insale) VALUES (?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1,productDTO.getManufacturer());
            pstmt.setInt(2, productDTO.getId());
            pstmt.setInt(3, productDTO.getMinAmount());
            pstmt.setInt(4, productDTO.getShelfNumber());
            pstmt.setString(5, productDTO.getName());
            pstmt.setDouble(6, productDTO.getManufacturePrice());
            pstmt.setDouble(7, productDTO.getDiscountPrice());
            pstmt.setDouble(8, productDTO.getSellingPrice());
            pstmt.setString(9, productDTO.getCategory());
            pstmt.setBoolean(10, productDTO.isInSale());

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    public void changeProductPrice( int productId, double newPrice) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE products SET sellingAmount=?  WHERE id=?");
            pstmt.setDouble(1, newPrice);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    public void changeProductName(int productId, String newName) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE products SET nameproduct=?  WHERE id=?");
            pstmt.setInt(2, productId);
            pstmt.setString(1, newName);

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    public void changeProductMinAmount( int productID, int minAmountt) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE products SET minAmount=?  WHERE id=?");
            pstmt.setInt(2, productID);
            pstmt.setInt(1, minAmountt);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    public void changeProducdiscountprice( int productID, double price) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE products SET discountPrice=? WHERE insale=FALSE AND  id=?");
            pstmt.setInt(2, productID);
            pstmt.setDouble(1, price);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    public void startDiscount(Double percent,int productId) throws SQLException {
   
    PreparedStatement pstmt = null;
    try {
        pstmt = connection.prepareStatement("UPDATE products SET discountPrice=sellingAmount - (sellingAmount * ?) / 100,insale=TRUE WHERE id=?");
        pstmt.setInt(2, productId);
        pstmt.setDouble(1, percent);

        pstmt.executeUpdate();
    } finally {
        if (pstmt != null) {
            pstmt.close();
        }
    }
}
public void endDiscount(int productId) throws SQLException {
   
    PreparedStatement pstmt = null;
    try {
        pstmt = connection.prepareStatement("UPDATE products SET discountPrice=sellingAmount ,insale=FALSE WHERE id=?");
        pstmt.setInt(2, productId);

        pstmt.executeUpdate();
    } finally {
        if (pstmt != null) {
            pstmt.close();
        }
    }
}

    public void moveProductToCategory( int productID, String newCategoryID) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE products SET Category=?  WHERE id=?");
            pstmt.setInt(2, productID);
            pstmt.setString(1, newCategoryID);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    public List<ProductDTO> getProducts() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ProductDTO> products = new LinkedList<ProductDTO>() {
            
        };

        try {
            pstmt = connection.prepareStatement("SELECT * FROM products");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                products.add(mapRowToProduct(rs));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        return products;
    }
    public void LoadData() throws SQLException{
        for (ProductDTO productDTO : getProducts()) {
            productscontroller.getInstance().ADDdtoproduct(productDTO);
            
        }
    }
    public ProductDTO getProductById(int productId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ProductDTO product = null;

        try {
            pstmt = connection.prepareStatement("SELECT * FROM products WHERE id = ?");
            pstmt.setInt(1, productId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                product = mapRowToProduct(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        return product;
    }

    public ProductDTO mapRowToProduct(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("nameproduct");
        int minAmount = rs.getInt("minAmount");
        boolean inSale = rs.getBoolean("insale");
        double sellingPrice = rs.getDouble("sellingAmount");
        double discountPrice = rs.getDouble("discountPrice");
        double manufacturePrice = rs.getDouble("manufacturePrice");
        int shelfNumber = rs.getInt("shelfId");
        String manufacturer = rs.getString("manufacture");
        String category = rs.getString("Category");

        return new ProductDTO(id, name, minAmount, inSale, sellingPrice, discountPrice, manufacturePrice, shelfNumber, manufacturer, category);
    }

    public void deleteData() throws SQLException{
        PreparedStatement pstmt = null;
    try {
        pstmt = connection.prepareStatement("DELETE FROM products ");
        pstmt.executeUpdate();
    } finally {
        if (pstmt != null) {
            pstmt.close();
        }
    }
}

}