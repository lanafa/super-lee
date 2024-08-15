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

import Backend.BusinessLayer.Controllers.Stock.discountcontroller;


public class DiscountProductDAO {
    
    public static DiscountProductDAO getInstance() {
        return instance;
    }

    private static DiscountProductDAO instance = new DiscountProductDAO();
    protected Connection connection2(){
        String path =Paths.get("").toAbsolutePath().toString();
        String _connectionString = "jdbc:sqlite:" + path+"/superli.db";
        Connection connection=null;
        try {
          connection = DriverManager.getConnection(_connectionString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
        }
    private Connection connection;

    public DiscountProductDAO() {
        this.connection = connection2();
    }

    public void insert(DiscountProductDTO discountDTO) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("INSERT INTO discountProduct (discountId,startdate,finishDate,discountAmount,productDiscount) VALUES (?,?,?,?,?)");
            pstmt.setInt(1, discountDTO.getDiscountID());
            pstmt.setString(2, discountDTO.getStartDate());
            pstmt.setString(3, discountDTO.getEndDate());
            pstmt.setDouble(4, discountDTO.getPercent());
            pstmt.setInt(5, discountDTO.getProduct());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    public void LoadData() throws SQLException{
        List<DiscountProductDTO> dtos=getdiscountProduct();
        for (DiscountProductDTO dto : dtos) {
            discountcontroller.getInstance().ADDdtodiscountP(dto);
            
        }
        

    }
    public void deletediscountproduct(int  id) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("DELETE FROM discountProduct WHERE discountId=?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    public void deleteData() throws SQLException{
        PreparedStatement pstmt = null;
    try {
        pstmt = connection.prepareStatement("DELETE FROM discountProduct ");
        pstmt.executeUpdate();
    } finally {
        if (pstmt != null) {
            pstmt.close();
        }
    }
}
// public List<DiscountProductDAO> loadDiscountDAOs() throws SQLException{
//     List<DiscountProductDAO> discountDTOs= getdiscountProduct();
//     List<Discount> discountProduct= new LinkedList<>();
//     for(DiscountProductDAO d : discountDTOs){
        
//     }
    
// }



    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
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
    public DiscountProductDTO getDiscountById(int id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DiscountProductDTO DiscountDTO = null;

        try {
            pstmt = connection.prepareStatement("SELECT * FROM discountProduct WHERE discountId = ?");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                DiscountDTO = mapRowToDiscount(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        return DiscountDTO;
    }
    public List<DiscountProductDTO> getdiscountProduct() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<DiscountProductDTO> discountProduct= new LinkedList<>();
        try {
            pstmt = connection.prepareStatement("SELECT * FROM discountProduct ");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                discountProduct.add(mapRowToDiscount(rs));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        return discountProduct;
    }

    private DiscountProductDTO mapRowToDiscount(ResultSet rs) throws SQLException {
        String endDate = rs.getString("finishDate");
        String startDate = rs.getString("startDate");
        int product = rs.getInt("productDiscount");
        int discountID = rs.getInt("discountID");
        int percent = rs.getInt("discountAmount");
        return new DiscountProductDTO(discountID,product,startDate,endDate,percent);
    }


}