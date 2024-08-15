package Backend.BusinessLayer.Controllers;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import Backend.DataAccessLayer.stockData.*;
import Backend.BusinessLayer.Controllers.Suppliers.ContractController;
import Backend.BusinessLayer.Controllers.Suppliers.OrdersController;
import Backend.BusinessLayer.Controllers.Suppliers.SupplierController;
import Backend.BusinessLayer.objects.Suppliers.Contact;
import Backend.BusinessLayer.objects.Suppliers.Pair;
import Backend.BusinessLayer.objects.Suppliers.SupplierCard;
import Backend.DataAccessLayer.SupplierData.*;


public class DataBaseController{
    //private static DataBaseController instance = null;
    private CategoryDAO categoryDAO;
    private DiscounCategoryDAO discountCATDAO;
    private DiscountProductDAO discountPRODAO;

    private ItemDAO itemDAO;
    private ProductDAO productDAO;
    private ContactDAO contactDAO;
    private ContractDAO contractDAO;
    private OrderDAO orderDAO;
    private PeriodicOrderDAO periodicOrderDAO;
    private QuantityReportDAO quantityReportDAO;
    private RecordDAO recordDAO;
    private SupplierCardDAO supplierCardDAO;
    
    protected static Connection connection2(){
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
    public static Connection c;
    public static Connection get_con(){return c;}
    public static DataBaseController getInstance() {
        if(instance == null){
            instance = new DataBaseController();
        }
        return instance;
    }
   public static void setInstance(DataBaseController instance) {
       DataBaseController.instance = instance;
   }
   private static DataBaseController instance = null;
   public DataBaseController() {
        this.c = connection2();
       categoryDAO = CategoryDAO.getInstance();
       discountCATDAO = DiscounCategoryDAO.getInstance();
       discountPRODAO = DiscountProductDAO.getInstance();
       itemDAO = ItemDAO.getInstance();
       productDAO = ProductDAO.getInstance();
       this.contactDAO = ContactDAO.getInstance();
       this.contractDAO = ContractDAO.getInstance();
       this.orderDAO = OrderDAO.getInstance();
       this.periodicOrderDAO = PeriodicOrderDAO.getInstance();
       this.quantityReportDAO = QuantityReportDAO.getInstance();
       this.recordDAO = RecordDAO.getInstance();
       this.supplierCardDAO =SupplierCardDAO.getInstance();
   }


    public void LoadData() throws SQLException{
        categoryDAO.LoadData();
        productDAO.LoadData();
        itemDAO.LoadData();
        discountCATDAO.LoadData();
        discountPRODAO.LoadData();
        SupplierController.getInstance().LoadData();
        OrdersController.getInstance().LoadData();
        ContractController.getInstance().LoadData();
        OrdersController.getInstance().Load_afer();
    }
    public Pair<List<SupplierCardDTO>,Integer> getSupps() throws SQLException{
        return supplierCardDAO.getSupps();
    }
    public Integer get_cont_size() throws SQLException{
        return supplierCardDAO.get_cont_size();
    }

    public List<ContactDTO> getSupplierContactsById(int supp_id) throws SQLException{
        return supplierCardDAO.getSupplierContactsById(supp_id);
    }

    public void insert_supp_contact(int supp_id,int contact_id)throws SQLException{
        supplierCardDAO.insert_supp_contact(supp_id, contact_id);
    }

    public void delete_supp_contact(int supp_id,int contact_id)throws SQLException{
        supplierCardDAO.delete_supp_contact(supp_id, contact_id);
    }


    public Map<RecordDTO,ProductDTO> getRecordsByOrderId(int orderId, int supp_id) throws SQLException {
        return orderDAO.getRecordsByOrderId(orderId, supp_id);
    }

    public Pair<List<OrderDTO>,Integer> get_orders(int periodic) throws SQLException{
        return orderDAO.get_orders(periodic);
    }

    public List<PeriodicOrderDTO> get_per_ords()throws SQLException{
        return null;
    }

    public PeriodicOrderDTO getPerOrder(int order_id)throws SQLException{
        return periodicOrderDAO.getById(order_id);
    }

    public Pair<List<ContractDTO>,Integer> get_contracts() throws  SQLException{
        return contractDAO.get_contracts();
    }


    public Map<Integer,Integer> get_orderHistory(int cont_id,int periodic)throws SQLException{
        return contractDAO.get_orderHistory(cont_id,periodic);
    }


    public QuantityReportDTO get_quantityreport(int contract_id)throws SQLException{
        return contractDAO.get_quantityreport(contract_id);
    }


    public Map<Integer,Integer> get_itemlist(int cont_id) throws SQLException{ //prod_id ==> quantity can be supplied
        return contractDAO.getItemList(cont_id);
    }

    public void add_record(RecordDTO rec)throws SQLException{
        recordDAO.insert(rec);
    }

    public void insert_to_order_history(int cont_id,int ord_id, int periodic)throws SQLException{
        orderDAO.insert_to_order_history(cont_id,ord_id,periodic);
    }


    public void insert_to_per_order_history(int cont_id,int ord_id, int periodic)throws SQLException{
        orderDAO.insert_to_per_order_history(cont_id,ord_id,periodic);
    }

    // public void update_per_record(int prod_id, int ord_id , int quantity) throws SQLException{
    //     recordDAO.update_per_record( prod_id,  ord_id ,  quantity);
    // }












    public void changeCategoryFather(String namec,String id) throws SQLException {
        categoryDAO.changeCategoryFather(namec, id);
    }

    public void addCategory(CategoryDTO categoryDTO) throws SQLException {
        categoryDAO.insert(categoryDTO);
    }
    public void changeProducdiscountprice( int productID, double price) throws SQLException {
        productDAO.changeProducdiscountprice(productID, price);
    }

    public void changeCategoryName(String namec,String id) throws SQLException {
        categoryDAO.changeCategoryName(namec,id);
    }
    public void deleteCategory(String categoryId) throws SQLException {
        categoryDAO.deleteCategory(categoryId);
    }
    public void add_discount(DiscountCategoryDTO discountDTO) throws SQLException {
        discountCATDAO.insert(discountDTO);
    }
    public void add_discount(DiscountProductDTO discountDTO) throws SQLException {
        discountPRODAO.insert(discountDTO);
    }
    public void add_item(ItemDTO itemDTO) throws SQLException {
        itemDAO.insert(itemDTO);
    }

    public void addDmgstore(String expDate,int id,int amount) throws SQLException {
        itemDAO.addDmgstore(expDate,id,amount);
    }
    
    public void addDmgstorage(String expDate,int id,int amount) throws SQLException {
        itemDAO.addDmgstorage(expDate,id,amount);
    }
    
    public void addStorageItem(String expDate,int id,int amount) throws SQLException {
        itemDAO.addStorageItem(expDate,id,amount);
    }
        
    public void addStoreItem(String expDate,int id,int amount) throws SQLException {
        itemDAO.addStoreItem(expDate,id,amount);
    }
    public void removestorageitem(String expDate,int id,int amount) throws SQLException {
        itemDAO.removestorageitem(expDate,id,amount);
    }
        
    public void removeStoreItem(String expDate,int id,int amount) throws SQLException {
        itemDAO.removeStoreItem(expDate,id,amount);
    }
    public void add_product(ProductDTO productDTO) throws SQLException {
        productDAO.insert(productDTO);
    }
    public void changeProductPrice( int productId, double newPrice) throws SQLException {
        productDAO.changeProductPrice(productId, newPrice);
    }
    public void changeProductName(int productId, String newName) throws SQLException {
        productDAO.changeProductName(productId, newName);
    }
    public void changeProductMinAmount( int productID, int minAmountt) throws SQLException {
        productDAO.changeProductMinAmount(productID, minAmountt);
    }

    public void moveProductToCategory( int productID, String newCategoryID) throws SQLException {
        productDAO.moveProductToCategory(productID, newCategoryID);
    }
    public void deletediscountproduct(int  id) throws SQLException {
        discountPRODAO.deletediscountproduct(id);
    }
    public void deletediscountcategory(int  id) throws SQLException {
        discountCATDAO.deletediscountcategory(id);
    }
    public void startDiscount(Double percent,int productId) throws SQLException {
    productDAO.startDiscount(percent, productId);
    }
    public void deleteData() throws SQLException {
        productDAO.deleteData();
        categoryDAO.deleteData();
        discountCATDAO.deleteData();
        discountPRODAO.deleteData();
        itemDAO.deleteData();
        contactDAO.deleteData();
        contractDAO.deleteData();
        orderDAO.deleteData();
        periodicOrderDAO.deleteData();
        quantityReportDAO.deleteData();
        recordDAO.deleteData();
        supplierCardDAO.deleteData();
    }
    public void add_discount(QuantityReportDTO quantityReportDTO) throws SQLException {
        this.quantityReportDAO.insert(quantityReportDTO);
    }

    public void updateQreport(QuantityReportDTO quantityReportDTO)throws SQLException{
        this.quantityReportDAO.update(quantityReportDTO);
    }

    public void addContract(ContractDTO c)throws SQLException{
        this.contractDAO.insert(c);
    }

    public void deleteContract(int contractid)throws SQLException{
        this.contractDAO.delete(contractid);
    }

    public void updateContract(ContractDTO c)throws SQLException{
        this.contractDAO.update(c);
    }

    public void addSupplier(SupplierCard s) throws SQLException{
        this.supplierCardDAO.insert(s.object2Dto());
        int supp_id = s.getSupplier_id();
        List<Contact> conts = s.getContactList();
        if(!conts.isEmpty()){
        for (Contact contact : conts) {
            contactDAO.insert(contact.object2Dto());
            supplierCardDAO.insert_supp_contact(supp_id, contact.getId());
            }
        }
    }

    public void deleteSupplier(int supplierid) throws SQLException{
        this.supplierCardDAO.delete(supplierid);
    }

    public void updateSupplier(SupplierCardDTO s) throws SQLException{
        this.supplierCardDAO.update(s);
    }

    public void addContact(ContactDTO c) throws SQLException{
        this.contactDAO.insert(c);
    }

    public void deleteContact(int contactid) throws SQLException{
        this.contactDAO.delete(contactid);
    }

    public void addOrder(OrderDTO order) throws SQLException{
        this.orderDAO.insert(order);
    }

    public void deleteOrder(int orderid)throws SQLException{
        this.orderDAO.delete(orderid);
    }

    public void delete_Per_Order(int orderid)throws SQLException{
        this.periodicOrderDAO.delete(orderid);
    }


    public void addPerOrder(PeriodicOrderDTO p, OrderDTO oDto) throws SQLException{
        this.periodicOrderDAO.insert(p,oDto);
    }

    public void updateperOrder(PeriodicOrderDTO p) throws SQLException{
        this.periodicOrderDAO.update(p);
    }

    public void addToOrderHistory(int contract_id,int order_id) throws SQLException{
        this.orderDAO.addToOrderHistory(contract_id, order_id);
    }
    public void insertqr(QuantityReportDTO object2Dto) throws SQLException {
        this.quantityReportDAO.insert(object2Dto);
    }
}
