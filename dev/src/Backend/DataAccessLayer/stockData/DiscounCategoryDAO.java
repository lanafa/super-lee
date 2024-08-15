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


public class DiscounCategoryDAO {
    
    public static DiscounCategoryDAO getInstance() {
        return instance;
    }

    private static DiscounCategoryDAO instance = new DiscounCategoryDAO();
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

    public DiscounCategoryDAO() {
        this.connection = connection2();
    }
    public void LoadData() throws SQLException{
        List<DiscountCategoryDTO> dtos=getdiscountCategory();
        for (DiscountCategoryDTO dto : dtos) {
            discountcontroller.getInstance().ADDdtodiscountC(dto);
            
        }
        

    }

    public void insert(DiscountCategoryDTO discountDTO) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("INSERT INTO discountCategory (discountId,stardate,finishDate,discountAmount,category) VALUES (?,?,?,?,?)");
            pstmt.setInt(1, discountDTO.getDiscountID());
            pstmt.setString(2, discountDTO.getStartDate());
            pstmt.setString(3, discountDTO.getEndDate());
            pstmt.setDouble(4, discountDTO.getPercent());
            pstmt.setString(5, discountDTO.getCategory());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    public void deletediscountcategory(int  id) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("DELETE FROM discountCategory WHERE discountId=?");
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
        pstmt = connection.prepareStatement("DELETE FROM discountCategory ");
        pstmt.executeUpdate();
    } finally {
        if (pstmt != null) {
            pstmt.close();
        }
    }
}
// public List<DiscounCategoryDAO> loadDiscountDAOs() throws SQLException{
//     List<DiscounCategoryDAO> discountDTOs= getdiscountCategory();
//     List<Discount> discountCategory= new LinkedList<>();
//     for(DiscounCategoryDAO d : discountDTOs){
        
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
    public DiscountCategoryDTO getDiscountById(int id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DiscountCategoryDTO DiscountDTO = null;

        try {
            pstmt = connection.prepareStatement("SELECT * FROM discountCategory WHERE discountId = ?");
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
    public List<DiscountCategoryDTO> getdiscountCategory() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<DiscountCategoryDTO> discountCategory= new LinkedList<>();
        try {
            pstmt = connection.prepareStatement("SELECT * FROM discountCategory ");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                discountCategory.add(mapRowToDiscount(rs));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        return discountCategory;
    }

    private DiscountCategoryDTO mapRowToDiscount(ResultSet rs) throws SQLException {
        String endDate = rs.getString("finishDate");
        String startDate = rs.getString("startDate");
        String category = rs.getString("category");
        int discountID = rs.getInt("discountID");
        int percent = rs.getInt("discountAmount");
        return new DiscountCategoryDTO(discountID,category,startDate,endDate,percent);
    }


}