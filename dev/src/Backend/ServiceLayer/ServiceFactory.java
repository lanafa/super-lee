package Backend.ServiceLayer;


import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.objects.Stock.Category;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.BusinessLayer.objects.Suppliers.Contact;
import Backend.BusinessLayer.objects.Suppliers.Pair;
import Backend.BusinessLayer.objects.Suppliers.Payment;
import Backend.BusinessLayer.objects.Suppliers.Record;
import Backend.ServiceLayer.Stock.CategoryService;
import Backend.ServiceLayer.Stock.DiscountService;
import Backend.ServiceLayer.Stock.ProductService;
import Backend.ServiceLayer.Stock.ReportService;
import Backend.ServiceLayer.Suppliers.ContractService;
import Backend.ServiceLayer.Suppliers.OrdersService;
import Backend.ServiceLayer.Suppliers.SupplierService;

public class ServiceFactory {

    CategoryService categoryService = new CategoryService();
    DiscountService discountService = new DiscountService();
    ProductService productService = new ProductService();
    ReportService reportService = new ReportService(productService);
    ContractService contractService=new ContractService();
    OrdersService ordersService=new OrdersService();
    SupplierService supplierService=new SupplierService();

  
   //loaddata{
     //  each one .load data()
     // Orderscontroller.getinstance().fillmap()
   //}
    public ServiceFactory() {
    }

    public void LoadData(){
        
    }
    public ContractService getContractService() {
        return contractService;
    }
    public OrdersService getOrdersService() {
        return ordersService;
    }
    public CategoryService getCategoryService() {
        return categoryService;
    }
    public SupplierService getSupplierService() {
        return supplierService;
    }

    public DiscountService getDiscountService() {
        return discountService;
    }
    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public String addCategory( String name, String parentCategoryID) {
        return categoryService.addCategory( name, parentCategoryID);
    }

    public String deleteCategory( String catID) {
        return categoryService.deleteCategory( catID);
    }

    public String addSubCategory( String categoryID, String subcategoryID) {
        return categoryService.addSubCategory( categoryID, subcategoryID);
    }

    public String addFatherCategory( String categoryID, String fatherCategoryID) {
        return categoryService.addFatherCategory( categoryID, fatherCategoryID);
    }

    public String changeCategoryName( String categoryID, String newName) {
        return categoryService.changeCategoryName( categoryID, newName);
    }
    public String removeStoreItem( int productID,LocalDate eexDate,int amount){
       return productService.removeStoreItem(productID, eexDate, amount);
    }
    public String removeStorageItem( int productID,LocalDate eexDate,int amount) {
return productService.removeStorageItem(productID, eexDate, amount);
    }



    public String buildProductDiscount( int product, double percent, LocalDate start,
                                        LocalDate end) {
        return discountService.buildProductDiscount(productService.productsController.returnProduct(product), percent, start, end);
    }

    public String buildCategoryDiscount( String category, double percent, LocalDate start,
                                         LocalDate end) {
        return discountService.buildCategoryDiscount( categoryService.getCategory(category), percent, start, end);
    }
    public String returnCategories( ) {
        return categoryService.returnCategories1();

    }
    public String getDefectiveItemsByStore( ) {
        return reportService.getDefectiveItemsByStore();
    }

    public String getProductsAndAmount( List<Category> categories) {
        return reportService.getProductsAndAmount(categories);
    }

    public String getDamagedItemReportsByStore() {
        return reportService.getDamagedItemReportsByStore();
    }

    public String getExpiredItemReportsByStore() {
        return reportService.getExpiredItemReportsByStore();
    }

    public String getMinReport() {
        return reportService.getMinReport();
    }
    public String getactiveCategoriesDiscounts() {
        return discountService.getactiveCategoriesDiscounts();
    } public String getactiveProductsDiscounts() {
        return discountService.getactiveProductsDiscounts();
    }

    public String addProductToStore(int barcode,String name, int minAmount, double sellingPrice, double manufacturePrice,
                                    int shelfNumber, String manufacturer, String category) {
        return productService.addProductToStore(barcode,name, minAmount, sellingPrice, manufacturePrice, shelfNumber,
                manufacturer,categoryService.getCategory(category));
    }

    public String changeProductPrice( int productID, double newPrice) {
        return productService.changeProductPrice( productID, newPrice);
    }

    public String changeProductName( int productID, String newName) {
        return productService.changeProductName( productID, newName);
    }

    public String changeProductMinAmount(int productID, int minAmount) {
        return productService.changeProductMinAmount( productID, minAmount);
    }

    public String addStorageItem( int productID, LocalDate expirationDate,int quantity) {
        return productService.addStorageItem( productID, expirationDate,quantity);
    }

    public String addStoreItem( LocalDate expirationDate, int productID,int quantity) {
        return productService.addStoreItem(expirationDate, productID,quantity);
    }
    public void returnInformationProducts(){
        productService.returnInformationProducts();
    }
    public void returnInformationProduct(int productid){
        productService.returnInformationProduct(productid);
    }



    // public String moveProductToCategory( int productID, String newCategoryID) {
    //     return productService.moveProductToCategory( productID,categoryService.getCategory(newCategoryID));
    // }

    // public String ADDsale(int ProductID,int quantity){
    //     return productService.ADDsale(ProductID,quantity);
    // }

    public String addDamfromstore(LocalDate exp,int productId,int amount) {
        return productService.addDamfromstore( productId,exp,amount);
    }
    public String addDamfromstorage(LocalDate exp,int productId,int amount) {
        return productService.addDamfromstorage( productId,exp,amount);
    }
    public String addSupplier(String company_name, String phone_number, int company_id, String bank_account , LinkedList<Contact> contactList) {
        return supplierService.addSupplier(company_name, phone_number, company_id, bank_account, contactList);
    }
    public String removeSupplier(int company_id) {
        return supplierService.removeSupplier(company_id);
    }

    public String changeBankAccount(int company_id, String newBanckAcc){
        return supplierService.changeBankAccount(company_id, newBanckAcc);
    }

    // public String changePaymentMethod(int company_id, Payment.PaymentMethod paymentMethod){
    //     return supplierService.changePaymentMethod(company_id, paymentMethod);
    // }

    // public String changeNetTerm(int company_id, Payment.NetTerm netTerm) {
    //     return supplierService.changeNetTerm(company_id, netTerm);
    // }

    // public String setPayment(int company_id, Payment.PaymentMethod paymentMethod, Payment.NetTerm netTerm){
    //     return supplierService.setPayment(company_id, paymentMethod, netTerm);
    // }

    public String addContact(int company_id, String name, String phone_number, String email, String fax){
        return supplierService.addContact(company_id, name, phone_number, email, fax);
    }

    public String removeContact(int company_id, int contact_id){
        return supplierService.removeContact(company_id, contact_id);
    }
    public String getSupplier(int supplier_id){
        return supplierService.getSupplier(supplier_id);
    }

    public String editSupplier_name(int supplier_id,String name) {
        return supplierService.editSupplier_name(supplier_id,name);
    }

    public String ChangePhoneNumber(int supp_id,String newPhone){
        return supplierService.ChangePhoneNumber(supp_id,newPhone);
    }

    public String listSuppliers() {
        return supplierService.listSuppliers();
    }

    public String Prepare_Order(Map<Product,Integer> items){
        return ordersService.Prepare_Order(items);
    }
    public String cancel_order(int order_id){
        return ordersService.cancel_order(order_id);
    }
    public String listOrders(){
        return ordersService.list_Orders();
    }

    public String addContract(int supplier_id, Payment payment, Map<Product,Integer> item_list, Pair<Boolean, Boolean> supplier_config, int days,Map<Integer,Double> items_prices){
        return contractService.addContract(supplier_id,payment,item_list,supplier_config, days,items_prices);
    }
    public String removeContract(int supplier_id){
        return contractService.removeContract(supplier_id);
    }
    public String get_contract(int supplier_id){
        return contractService.get_contract(supplier_id);
    }
    public String addEdit_item(int supplier_id, Product item, int quantity,double price_per_one){
        return contractService.addEdit_item(supplier_id,item,quantity,price_per_one);
    }
    public String remove_item(int supplier_id, Product item){
        return contractService.remove_item(supplier_id,item);
    }
    public String add_discount(int supplier_id, int item_id,int quantity,int discount,boolean ovl,Character c){
        return contractService.add_discount(supplier_id,item_id,quantity,discount,ovl,c);
    }
    public String remove_discount(int supplier_id, int item_id,int quantity,int discount,boolean ovl){
        return contractService.remove_discount(supplier_id,item_id,quantity,discount,ovl);
    }

    public String change_contractPayment(int supp_id, Payment payment) {
        return contractService.change_contractPayment(supp_id,payment);
    }

    public String listContracts(){
        return contractService.listContracts();
    }

    public String listPerOrder(){
        return ordersService.listPerOrders();
    }

    public String incrementDay(){
        return ordersService.Advance_day();
    }

    public String add_PeriodicOrder(String supplier_name, int supplier_id, String address, String contact_number,List<Record> items,int day){
        return ordersService.add_PeriodicOrder(supplier_name, supplier_id, address, contact_number, items, day);
    }

    public String updatePeriodic_date(int order_id,int day){
        return ordersService.updatePeriodic_date(order_id, day);
    }

    public String add_toPeriodic_product(int product_id, int order_id,int quantity){
        return ordersService.add_toPeriodic_product(product_id, order_id, quantity);
    }

    public String add_to_Periodic_items(int order_id, Record item){
        return ordersService.add_to_Periodic_items(order_id, item);
    }
}





