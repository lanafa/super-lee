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

import Backend.BusinessLayer.Controllers.Stock.productscontroller;

public class ItemDAO {
    public static ItemDAO getInstance() {
        return instance;
    }


    private static ItemDAO instance = new ItemDAO();
    protected Connection connection2(){
        String path = Paths.get("").toAbsolutePath().toString() ;
        String _connectionString = "jdbc:sqlite:" + path+"/superli.db";
        Connection connection=null;
        try {
          connection = DriverManager.getConnection(_connectionString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
        }
    public ItemDAO() {
        this.connection = connection2();
    }
    private Connection connection;

    public void insert(ItemDTO itemDTO) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("INSERT INTO items (expDate,storageAmount,StoreAmount,ExpiredAmount,DamagedAmount,id) VALUES (?,?,?,?,?,?)");
            pstmt.setString(1, itemDTO.getExpDate());
            pstmt.setInt(2, itemDTO.getStorageamount());
            pstmt.setInt(3, itemDTO.getStoreamount());
            pstmt.setInt(4, itemDTO.getExpired());
            pstmt.setInt(5, itemDTO.getDamaged());
            pstmt.setInt(6, itemDTO.getId());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }

    public void addDmgstore(String expDate,int idd,int amount) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE items SET DamagedAmount=DamagedAmount+? ,StoreAmount=StoreAmount-? WHERE expDate=? AND id=?");
            pstmt.setInt(1, amount);
            pstmt.setInt(2, amount);
            pstmt.setString(3, expDate);
            pstmt.setInt(4, idd);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    public void makeExpired(String expDate,int idd) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE items SET ExpiredAmount=StorageAmount+StoreAmount ,StoreAmount=0,StorageAmount=0 WHERE expDate=? AND id=?");
            pstmt.setString(1, expDate);
            pstmt.setInt(2, idd);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    
    
    public void addDmgstorage(String expDate,int idd,int amount) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE items SET DamagedAmount=DamagedAmount+? ,StorageAmount=StorageAmount-? WHERE expDate=? AND id=?");
            pstmt.setInt(1, amount);
            pstmt.setInt(2, amount);
            pstmt.setString(3, expDate);
            pstmt.setInt(4, idd);

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    
    public void addStorageItem(String expDate,int idd,int amount) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE items SET StorageAmount=StorageAmount+? WHERE expDate=? AND id=?");
            pstmt.setInt(1, amount);
            pstmt.setString(2, expDate);
            pstmt.setInt(3, idd);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
        
    public void addStoreItem(String expDate,int idd,int amount) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE items SET StoreAmount=StoreAmount+? WHERE expDate=? AND id=?");
            pstmt.setInt(1, amount);
            pstmt.setString(2, expDate);
            pstmt.setInt(3, idd);

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
    public void removestorageitem(String expDate,int idd,int amount) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE items SET StorageAmount=StorageAmount-? WHERE expDate=? AND id=?");
            pstmt.setInt(1, amount);
            pstmt.setString(2, expDate);
            pstmt.setInt(3, idd);

            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
        
    public void removeStoreItem(String expDate,int idd,int amount) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement("UPDATE items SET StoreAmount=StoreAmount-? WHERE expDate=? AND id=?");
            pstmt.setInt(1, amount);
            pstmt.setString(2, expDate);
            pstmt.setInt(3, idd);
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
    public Connection getConnection() {
        return connection;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    public ItemDTO getDiscountById(int id) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ItemDTO itemDTO = null;

        try {
            pstmt = connection.prepareStatement("SELECT * FROM items WHERE id = ?");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                itemDTO = mapRowToItem(rs);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        return itemDTO;
    }
    public void LoadData() throws SQLException{
        List<ItemDTO> itItemDTOs=getItems();
        for (ItemDTO itemDTO : itItemDTOs) {
            productscontroller.getInstance().ADDdtoitem(itemDTO);
            
        }
        

    }
    public List<ItemDTO> getItems() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ItemDTO> Items = new LinkedList<ItemDTO>() {
            
        };

        try {
            pstmt = connection.prepareStatement("SELECT * FROM items ");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Items.add(mapRowToItem(rs));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        return Items;
    }
    private ItemDTO mapRowToItem(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String  expDate=rs.getString("expDate");
        int damaged = rs.getInt("DamagedAmount");
        int expired = rs.getInt("ExpiredAmount");
        int storeamount = rs.getInt("StoreAmount");
        int storageamount = rs.getInt("StorageAmount");
        return new ItemDTO(expDate,damaged,expired,id,storeamount,storageamount);
    }

    public void deleteData() throws SQLException{
        PreparedStatement pstmt = null;
    try {
        pstmt = connection.prepareStatement("DELETE FROM items ");
        pstmt.executeUpdate();
    } finally {
        if (pstmt != null) {
            pstmt.close();
        }
    }
}

}
