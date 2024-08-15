// package Backend.ServiceLayer;

// import java.time.LocalDate;
// import java.util.HashMap;

// import Backend.BusinessLayer.objects.*;
// import Backend.BusinessLayer.objects.Stock.Category;
// import Backend.BusinessLayer.objects.Stock.Discount;
// import Backend.BusinessLayer.objects.Stock.Product;


// public class StoreService {
//     private static StoreService instance = new StoreService();
//     private HashMap<Integer, ServiceFactory> factories;



//     private StoreService() {
//         factories = new HashMap<Integer, ServiceFactory>();


//         for (int i = 1; i <= 10; i++) {
//             factories.put(i, new ServiceFactory());
//         }
//         for (int storeId = 1; storeId <= 10; storeId++) {
//             Category category0=new Category("Dairy", null);
//             factories.get(storeId).getCategoryService().categoryController.getCategories().put(category0.getName(), category0);
//             Category category1=new Category("Milk", category0);
//             category1.setFatherCategory(category0);
//             category0.addSubCategory(category1);
//             factories.get(storeId).getCategoryService().categoryController.getCategories().put(category1.getName(), category1);
//             Category category2=new Category("1.5L",category1);
//             factories.get(storeId).getCategoryService().categoryController.getCategories().put(category2.getName(), category2);
//             category2.setFatherCategory(category1);
//             category1.addSubCategory(category2);
//             Category category3=new Category("Bathroom products", null);
//             factories.get(storeId).getCategoryService().categoryController.  getCategories().put(category3.getName(), category3);
//             Category category4=new Category("Shampoo", category3);
//             factories.get(storeId).getCategoryService().categoryController. getCategories().put(category4.getName(), category4);
//             category4.setFatherCategory(category3);
//             category3.addSubCategory(category4);
//             Category category5=new Category("250ml",category4);
//             factories.get(storeId).getCategoryService().categoryController.getCategories().put(category5.getName(), category5);
//             category5.setFatherCategory(category4);
//             category4.addSubCategory(category5);
//             Product product0=new Product(1, "Milk3%", 3, 7, 5, 1, "Tnuva",   factories.get(storeId).categoryService.getCategory(category2.getName()));
//             product0.setCategory(category2);
//             // Item item=new Item(1, LocalDate.now().minusDays(1));
//             // Item item1=new Item(1, LocalDate.now().minusDays(1));
//             // Item item2=new Item(1, LocalDate.now().minusDays(1));
//             // product0.addStoreItem(LocalDate.now().minusDays(1), item);
//             // product0.addStoreItem(LocalDate.now().minusDays(1), item1);
//             // product0.addStoreItem(LocalDate.now().minusDays(1), item2);
//             factories.get(storeId).productService.productsController.getProducts().put(1, product0);
//             Product product1=new Product(2, "Shampoo 250ml", 3, 15, 10, 2, "Pinuk",   factories.get(storeId).categoryService.getCategory(category5.getName()));
// product1.setCategory(category5);
// category0.addProduct(product0);
// category1.addProduct(product0);
// category2.addProduct(product0);

// category3.addProduct(product1);
// category4.addProduct(product1);
// category5.addProduct(product1);
// product1.addStorageItem(LocalDate.now().plusDays(1), 4);
// product1.addDamfromstorage(LocalDate.now().plusDays(1));
// product1.addDamfromstorage(LocalDate.now().plusDays(1));
// product1.addStorageItem(LocalDate.now().minusDays(1), 3);

//             factories.get(storeId).productService.productsController.getProducts().put(2, product1);
//             Discount discount0=new Discount(product0,LocalDate.now().minusDays(1),LocalDate.now().minusDays(1),20,1);
//             factories.get(storeId).getDiscountService().discountController.getProductDiscounts().put(1, discount0);
//             Discount discount1=new Discount(product1,LocalDate.now(),LocalDate.now().plusDays(1),20,1);
//             factories.get(storeId).getDiscountService().discountController.getProductDiscounts().put(2, discount1);
//             product1.startDiscount(discount0.getPercent());
//             product0.startDiscount(discount1.getPercent());
//             factories.get(storeId).getDiscountService().discountController.setCounter(3);

           
//         }}

//     public static StoreService getInstance() {
//         return instance;
//     }

//     public ServiceFactory pickStore(int storeId) {

//         factories.get(storeId).getDiscountService().endDiscounts();
//         factories.get(storeId).getProductService().checkDates();
//         HashMap<Product,Integer> productsAndAmount = new HashMap<Product, Integer>();

// for (Product product : factories.get(storeId).productService.productsController.getMinReport1().getProducts()) {
//             int amount=product.getMinAmount()-product.getFullAmount()+10;
//             productsAndAmount.put(product, amount);
    
// }
// if(!productsAndAmount.isEmpty()){
//     factories.get(storeId).Prepare_Order(productsAndAmount);
// }

//         return factories.get(storeId);
//     }


// }
