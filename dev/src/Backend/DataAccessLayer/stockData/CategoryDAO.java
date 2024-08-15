package Backend.DataAccessLayer.stockData;
import java.io.File;
import java.nio.file.Paths;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import Backend.BusinessLayer.Controllers.Stock.Categorycontroller;

public class CategoryDAO{
    public static CategoryDAO getInstance() {
        return instance;
    }
  
    private static CategoryDAO instance = new CategoryDAO();
    
    protected Connection connection2(){
        String path = Paths.get("").toAbsolutePath().toString();
        String _connectionString = "jdbc:sqlite:" + path+ "/superli.db";
        
        System.out.println(path);
        Connection connection=null;
        try {
          connection = DriverManager.getConnection(_connectionString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
        }
    private Connection connection;

    public CategoryDAO() {
        
        this.connection = connection2();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void insert(CategoryDTO categoryDTO) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("INSERT INTO Categories (name,facategory) VALUES (?,?)");

            pstmt.setString(1,categoryDTO.getName());
            pstmt.setString(2,categoryDTO.getFaString());



            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    public void changeCategoryName(String namec,String id) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE Categories SET name=?  WHERE name=?");
            pstmt.setString(1, namec);
            pstmt.setString(2, id);

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }            

    public void changeCategoryFather(String namec,String id) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE Categories SET facategory=?  WHERE name=?");
            pstmt.setString(1, namec);
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    public void deleteCategory(String categoryId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("DELETE FROM categories WHERE name=?");
            pstmt.setString(1, categoryId);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
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
    public CategoryDTO getCategoryById(int id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        CategoryDTO categoryDTO = null;

        try {
            pstmt = connection.prepareStatement("SELECT * FROM Categories WHERE name = ?");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                categoryDTO = mapRowToCategory(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        return categoryDTO;
    }
    public List<CategoryDTO> getCategorys() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<CategoryDTO> categoryS= new LinkedList<>();
        try {
            pstmt = connection.prepareStatement("SELECT * FROM Categories");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                categoryS.add(mapRowToCategory(rs));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        return categoryS;
    }

    private CategoryDTO mapRowToCategory(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String faString = rs.getString("facategory");
        return new CategoryDTO(name, faString);
    }
    public void LoadData() throws SQLException{
        for (CategoryDTO categoryDTO : getCategorys()) {
           // System.out.println(getCategorys().size());
Categorycontroller.getInstance().addDto(categoryDTO);
            
        }

    }
    public void deleteData() throws SQLException{
        PreparedStatement pstmt = null;
    try {
        pstmt = connection.prepareStatement("DELETE FROM Categories");
        pstmt.executeUpdate();
    } finally {
        if (pstmt != null) {
            pstmt.close();
        }
    }
}
}
