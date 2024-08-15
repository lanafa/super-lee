package Backend.BusinessLayer.Controllers.Stock;

import Backend.BusinessLayer.objects.Stock.Category;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.BusinessLayer.objects.Stock.Item;
import Backend.BusinessLayer.objects.Suppliers.Record;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.DataAccessLayer.stockData.ItemDTO;
import Backend.DataAccessLayer.stockData.ProductDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class productscontroller {

    private static productscontroller instance = null;

    public DataBaseController getDataBaseController() {
        return dataBaseController;
    }

    public void setDataBaseController(DataBaseController dataBaseController) {
        this.dataBaseController = dataBaseController;
    }

    private DataBaseController dataBaseController = DataBaseController.getInstance();

    private HashMap<Integer, Product> products;

    public HashMap<Integer, Product> getProducts() {
        return products;
    }

    public productscontroller() {
        products = new HashMap<Integer, Product>();
    }

    public static productscontroller getInstance() {
        if (instance == null) {
            instance = new productscontroller();
        }
        return instance;
    }

    //
    public String changeProductPrice(int productId, double newPrice) throws SQLException {

        if (!products.containsKey(productId)) {
            throw new IllegalArgumentException("Product not found in store");
        }
        products.get(productId).setSellingPrice(newPrice);
        dataBaseController.changeProductPrice(productId, newPrice);
        return "Product price changed successfully";
    }

    //
    public String changeProductName(int productId, String newName) throws SQLException {

        if (!products.containsKey(productId)) {
            throw new IllegalArgumentException("Product not found in store");
        }
        Product product = returnProduct(productId);
        product.setName(newName);
        dataBaseController.changeProductName(productId, newName);

        return "Product name changed successfully";
    }

    //
    public String buildProduct(int productID, String name, int minAmount, double sellingPrice, double manufacturePrice,
            int shelfNumber, String manufacture, Category category) throws SQLException {
        if (category == null) {
            return "can not add product";
        } else if (products.containsKey(productID)) {
            return "product already in the store";
        } else {
            Product product = new Product(productID, name, minAmount, sellingPrice, manufacturePrice,
                    shelfNumber, manufacture, category);
            category.addProduct(product);

            products.put(productID, product);
            product.setCategory(category);

            while (category.getFatherCategory() != null) {
                category.getFatherCategory().addProduct(product);
                category = category.getFatherCategory();
            }

            dataBaseController.add_product(returnProduct(productID).getProductDTO());

            return "Product: " + productID + " was added successfully";
        }
    }

    public String buildProd2(int productID, String name, int minAmount, double sellingPrice, double manufacturePrice,
            int shelfNumber, String manufacture, Category category) throws SQLException {
        if (category == null) {
            return "can not add product";
        } else if (products.containsKey(productID)) {
            return "product already in the store";
        } else {
            Product product = new Product(productID, name, minAmount, sellingPrice, manufacturePrice,
                    shelfNumber, manufacture, category);
            category.addProduct(product);

            products.put(productID, product);
            product.setCategory(category);

            while (category.getFatherCategory() != null) {
                category.getFatherCategory().addProduct(product);
                category = category.getFatherCategory();
            }

            return "Product: " + productID + " was added successfully";
        }
    }

    //
    public Product returnProduct(int productID) {
        if (products.containsKey(productID)) {
            return products.get(productID);
        } else {
            throw new IllegalArgumentException("Product not found");
        }

    }

    //
    public List<Product> returnProducts() {

        List<Product> productsList = new LinkedList<Product>();

        if (!products.isEmpty()) {
            for (Product product : products.values()) {
                productsList.add(product);
            }
        }

        return productsList;
    }

    //
    public String addStorageItem(int productID, LocalDate expiryDate, int quantity) throws SQLException {

        if (products.containsKey(productID)) {
            if (products.get(productID).getItems().get(expiryDate) == null) {
                try {
                    ItemDTO i = new ItemDTO(expiryDate.toString(), 0, 0, productID, 0, quantity);
                    dataBaseController.add_item(i);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            } else {
                try {
                    dataBaseController.addStorageItem(expiryDate.toString(), productID, quantity);

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            products.get(productID).addStorageItem(expiryDate, quantity);
            checkExpiryDates();
            return "Item added to storage successfully";

        } else {
            throw new IllegalArgumentException("Product not found in store");
        }
    }

    //
    public String addStoreItem(int productID, LocalDate expiryDate, int quantity) throws SQLException {

        if (products.containsKey(productID)) {
            if (products.get(productID).getItems().get(expiryDate) == null) {
                try {
                    ItemDTO i = new ItemDTO(expiryDate.toString(), 0, 0, productID, 0, quantity);
                    dataBaseController.add_item(i);
                } catch (Exception e) {
                    // TODO: handle exception
                }

            } else {
                try {
                    dataBaseController.addStoreItem(expiryDate.toString(), productID, quantity);

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            products.get(productID).addStoreItem(expiryDate, quantity);
            checkExpiryDates();
            return "Item added to store successfully";

        } else {
            throw new IllegalArgumentException("Product not found in store");
        }
    }

    public void checkExpiryDates() {
        for (Product product : products.values()) {
            product.checkExpiryDates();
        }
        Alert();
    }

    //
    public String changeProductMinAmount(int productID, int minAmount) throws SQLException {

        if (products.containsKey(productID)) {
            products.get(productID).setMinAmount(minAmount);
            dataBaseController.changeProductMinAmount(productID, minAmount);

            return "Minimum amount for product changed successfully";
        } else {
            throw new IllegalArgumentException("Product not found in store");
        }
    }

    // //
    public String removeStoreItem(int productID, LocalDate exDate, int amount) throws SQLException {

        Alert();
        if (products.containsKey(productID)) {

            products.get(productID).removeStoreItem(exDate, amount);
            dataBaseController.removeStoreItem(exDate.toString(), productID, amount);

            return "Removed item successfully";
        } else {
            throw new IllegalArgumentException("Product not found in store");
        }

    }

    // //
    public String removeStorageItem(int productID, LocalDate eDate, int amount) throws SQLException {
        Alert();
        if (products.containsKey(productID)) {
            products.get(productID).removestorageItem(eDate, amount);
            dataBaseController.removestorageitem(eDate.toString(), productID, amount);

            return "Removed item successfully";
        } else {
            throw new IllegalArgumentException("Product not found in store");
        }

    }

    public Product getProduct(int prod_id) {
        return products.get(prod_id);
    }

    public void Alert(){
        List<Product> product=returnProducts();
        for (Product product1 : product) {
            if(product1.getFullAmount()<=(product1.getMinAmount())){
                if(product1.getFullAmount()==0){
                    System.out.println("Product ID: "+product1.getId()+" is out of stock");}
            
                    else System.out.println("Product ID: "+product1.getId()+" is almost  out of stock current amount: "+product1.getFullAmount());
                }
            }
    }


    public String returnInformationProducts() {
        String output = "";
        for (Product product : products.values()) {
            output += ("productID= " + product.getId() + " productName= " + product.getName() + " productFullAmount= "
                    + product.getFullAmount()
                    + " productStoreAmount= " + product.getStoreAmount() + " productStorageAmount= "
                    + product.getStorageAmount() + " product Location= " + product.getShelfNumber()
                    + " Product current price: " + product.getSellingPrice() + " Is InSale: " + product.isInSale()
                    + " category name:" + product.getCategorname());
            output += "\n\n";

        }
        return output;
    }

    public String returnInformationProduct(int poductid) {
        if (!products.containsKey(poductid)) {
            throw new IllegalArgumentException("Product not found in store");
        }
        return ("productID= " + products.get(poductid).getId() + " productName= " + products.get(poductid).getName()
                + " productFullAmount= " + products.get(poductid).getFullAmount()
                + " productStoreAmount= " + products.get(poductid).getStoreAmount() + " productStorageAmount= "
                + products.get(poductid).getStorageAmount() + " productLocation= "
                + products.get(poductid).getShelfNumber() + " Current price: "
                + products.get(poductid).getSellingPrice() + " Is InSale: " + products.get(poductid).isInSale()
                + "category name: " + products.get(poductid).getCategorname());

    }

    public String getDefectiveItemsByStore() {
        List<Product> productList1 = new LinkedList<>();
        List<Item> items1 = new LinkedList<>();
        List<Item> items2 = new LinkedList<>();

        List<Product> productList = returnProducts();
        for (Product product : productList) {
            if (product.getDamaged().size() != 0) {
                productList1.add(product);
                items1.addAll(product.getExpireditems());

            }
        }
        String output = "Damaged report \nThe products that have damaged items are  \n";
        for (Product product : productList1) {
            int amount = 0;
            for (Item oItem : product.getDamaged()) {
                amount += oItem.getDamaged();
            }
            output += "product ID: " + product.getId() + " and the amount is: " + amount + "\n";

        }
        for (Product product : productList) {
            if (product.getExpireditems().size() != 0) {
                productList1.add(product);
                items2.addAll(product.getExpireditems());
            }

        }

        output += "\nExpired report \nThe products that have expired items are  \n";
        for (Product product : productList1) {
            int amount = 0;
            for (Item oItem : product.getExpireditems()) {
                amount += oItem.getExpired();
            }
            output += "product ID: " + product.getId() + " and the amount is: " + amount + "\n";

        }
        List<Item> items3 = new LinkedList<>();
        items3.addAll(items1);
        items3.addAll(items2);

        return output;
    }

    public void ADDdtoitem(ItemDTO item) {
        LocalDate date = LocalDate.parse(item.getExpDate());
        products.get(item.getId()).addStorageItem(date, item.getStorageamount());
        products.get(item.getId()).addStoreItem(date, item.getStoreamount());
        products.get(item.getId()).getItems().get(date).setDamaged(item.getDamaged());

        products.get(item.getId()).checkExpiryDates();
    }

    public void ADDdtoproduct(ProductDTO productDTO) throws SQLException {
        Category cat = Categorycontroller.getInstance().returnCategory(productDTO.getCategory());
        buildProd2(productDTO.getId(), productDTO.getName(), productDTO.getMinAmount(), productDTO.getSellingPrice(),
                productDTO.getManufacturePrice(), productDTO.getShelfNumber(), productDTO.getManufacturer(), cat);
    }

    //
    public String getProductsAndAmount(List<Category> categories) {
        List<Product> products = new LinkedList<>();

        for (Category category : categories) {
            products.addAll(category.getProducts());

        }
        HashMap<Product, Integer> productsAndAmount = new HashMap<>();

        List<Product> productss = new LinkedList<>();
        String out = "Products and amount Report\n";
        try {
            for (Product product : products) {
                productss.add(product);
                productsAndAmount.put(product, product.getFullAmount());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        for (Map.Entry<Product, Integer> entry : productsAndAmount.entrySet()) {
            out += ("proudct ID: = " + entry.getKey().getId() + ", Product amount  = " + entry.getValue()) + "\n";
        }
        return out;
    }

    //
    public String getDamagedItemReportsByStore() {
        List<Product> productList1 = new LinkedList<>();
        List<Item> items = new LinkedList<>();
        List<Product> productList = returnProducts();
        for (Product product : productList) {
            if (product.getDamaged().size() != 0) {
                productList1.add(product);
                items.addAll(product.getDamaged());

            }
        }

        String output = "Damaged report \nThe products that have damaged items are  \n";
        for (Product product : productList1) {
            int amount = 0;
            for (Item oItem : product.getDamaged()) {
                amount += oItem.getDamaged();
            }
            output += "product ID: " + product.getId() + " and the amount is: " + amount + "\n";

        }
        return output;
    }

    public String getExpiredItemReportsByStore() {
        List<Product> productList = returnProducts();
        List<Product> productList1 = new LinkedList<>();
        List<Item> items = new LinkedList<>();

        for (Product product : productList) {
            if (product.getExpireditems().size() != 0) {
                productList1.add(product);
                items.addAll(product.getExpireditems());
            }

        }

        String output = "Expired report \nThe products that have expired items are  \n";
        for (Product product : productList1) {
            int amount = 0;
            for (Item oItem : product.getExpireditems()) {
                amount += oItem.getExpired();
            }
            output += "product ID: " + product.getId() + " and the amount is: " + amount + "\n";

        }
        return output;
    }

    public String getMinReport() {
        List<Integer> mList = new LinkedList<>();
        List<Product> mList1 = new LinkedList<>();

        List<Product> productList = returnProducts();
        for (Product product : productList) {
            if (product.getFullAmount() <= product.getMinAmount()) {
                mList.add(product.getId());
                mList1.add(product);

            }
        }

        String output = "Min report \n The products that need to be restocked are \n";
        for (Integer product : mList) {
            output += "product ID: " + product + "\n";

        }
        return output;
    }

    public String receive_Order(List<Record> records) throws Exception {
        for (Record record : records) {
            Product prod = products.get(record.getId());
            LocalDate date = LocalDate.now();
            LocalDate expiary = date.plusYears(1); // assumed
            addStorageItem(prod.getId(), expiary, record.getQuantity());
        }
        return "";
    }

    public List<Product> getMinReport1() {
        List<Integer> mList = new LinkedList<>();
        List<Product> mList1 = new LinkedList<>();

        List<Product> productList = returnProducts();
        for (Product product : productList) {
            if (product.getFullAmount() <= product.getMinAmount()) {
                mList.add(product.getId());
                mList1.add(product);
            }
        }
        return mList1;

    }

    public Map<Product, Integer> runThrough_Day() {
        Map<Product, Integer> min = new HashMap<>();
        List<Product> productList = returnProducts();
        for (Product product : productList) {
            if (product.getFullAmount() <= product.getMinAmount()) {
                min.put(product, product.getMinAmount() * 2);
            }
        }
        return min;
    }

    public String addDamfromstore(LocalDate exp, int productId, int amount) throws SQLException {
        try {
            dataBaseController.addDmgstore(exp.toString(), productId, amount);

        } catch (Exception e) {
            // TODO: handle exception
        }

        return products.get(productId).addDamfromstore(exp, amount);

    }

    //
    public String addDamfromstorage(LocalDate exp, int productId, int amount) throws SQLException {
        try {
            dataBaseController.addDmgstorage(exp.toString(), productId, amount);

        } catch (Exception e) {
            // TODO: handle exception
        }

        return products.get(productId).addDamfromstorage(exp, amount);

        //

    }
}
