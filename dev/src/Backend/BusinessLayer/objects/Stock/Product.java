package Backend.BusinessLayer.objects.Stock;

import Backend.BusinessLayer.objects.Stock.Item;
import Backend.DataAccessLayer.stockData.ProductDTO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private ProductDTO productDTO;

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    private HashMap<LocalDate, Item> Items;
    private int minAmount;
    private boolean inSale;
    private double sellingPrice;
    private double DiscountPrice;
    private double manufacturePrice;
    private int shelfNumber;
    private String manufacturer;
    private Category category;

    public Product(int id, String name, int minAmount, double sellingPrice, double manufacturePrice, int shelfNumber,
            String manufacturer, Category category) {
        this.id = id;
        this.name = name;
        this.minAmount = minAmount;
        this.sellingPrice = sellingPrice;
        this.DiscountPrice = sellingPrice;
        this.manufacturePrice = manufacturePrice;
        this.shelfNumber = shelfNumber;
        this.manufacturer = manufacturer;
        this.category = category;
        this.Items = new HashMap<>();
        this.inSale = false;
        this.productDTO = new ProductDTO(id, name, minAmount, inSale, sellingPrice, sellingPrice, manufacturePrice,
                shelfNumber, manufacturer, manufacturer);

    }

    public Product(int id, String name, int minAmount, double sellingPrice, double manufacturePrice, int shelfNumber,
                   String manufacturer) {
        this.id = id;
        this.name = name;
        this.minAmount = minAmount;
        this.sellingPrice = sellingPrice;
        this.DiscountPrice = sellingPrice;
        this.manufacturePrice = manufacturePrice;
        this.shelfNumber = shelfNumber;
        this.manufacturer = manufacturer;
        this.category = null; // ----------------------------------------------------------- something wrong a string on the dto and a class in the object
        this.Items = new HashMap<>();
        this.inSale = false;
        this.productDTO = new ProductDTO(id, name, minAmount, inSale, sellingPrice, sellingPrice, manufacturePrice,
                shelfNumber, manufacturer, manufacturer);

    }
    public Product(Product prod){
        this.id = prod.getId();
        this.name = prod.getName();
        this.productDTO = prod.getProductDTO(); //why?
        this.Items =  prod.getItems();
        this.minAmount = prod.getMinAmount();
        this.sellingPrice = prod.getSellingPrice();
        this.DiscountPrice = prod.getDiscountPrice();
        this.manufacturePrice = prod.getManufacturePrice();
        this.shelfNumber = prod.getShelfNumber();
        this.manufacturer = prod.getManufacturer();
        this.category = prod.getCategory();
        this.inSale = prod.isInSale();
    }

    public double getSalePrice() {
        return DiscountPrice;
    }

    public String startDiscount(Double percent) {
        DiscountPrice = sellingPrice - (sellingPrice * percent) / 100;
        setInSale(true);
        return "Discount started.";
    }

    public boolean isInSale() {
        return inSale;
    }

    public void setInSale(boolean inSale) {
        this.inSale = inSale;
    }

    public HashMap<LocalDate, Item> getItems() {
        return Items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
    }

    public double getSellingPrice() {
        return inSale ? DiscountPrice : sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getDiscountPrice(){return DiscountPrice;}

    public void setDiscountPrice(double DiscountPrice){this.DiscountPrice = DiscountPrice;}

    public double getManufacturePrice() {
        return manufacturePrice;
    }

    public void setManufacturePrice(double manufacturePrice) {
        this.manufacturePrice = manufacturePrice;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public void add_ITEM(LocalDate date, Item amount){
        this.Items.put(date,amount);
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategorname() {
        String out = "/" + category.getName();
        while (category.getFatherCategory() != null) {
            out = "/" + category.getFatherCategory().getName() + out;
            category = category.getFatherCategory();
        }
        return out.substring(1);
    }

    public Category getCategory(){return category;}

    public void setCategory(Category category) {
        this.category = category;
    }

    public void addStorageItem(LocalDate expDate, int quantity) {
        if (Items.containsKey(expDate)) {
            while(quantity>0){

            Items.get(expDate).addStorageItem(1);
            quantity--;}

        } else {
            Item item = new Item(id, expDate, 0, quantity);
            Items.put(expDate, item);
        }
    }

    public void addStoreItem(LocalDate expDate, int quantity) {
        if (Items.containsKey(expDate)) {
            while(quantity>0){
            Items.get(expDate).addStoreItem(1);
            quantity--;}

        } else {
            Item item = new Item(id, expDate, 0, quantity);
            Items.put(expDate, item);
        }
    }

    public int getStorageAmount() {
        int total = 0;
        for (Item item : Items.values()) {
            total += item.getStorageamount();
        }
        return total;
    }

    public int getStoreAmount() {
        int total = 0;
        for (Item item : Items.values()) {
            total += item.getStoreamount();
        }
        return total;
    }

    public int getFullAmount() {
        return getStorageAmount() + getStoreAmount();
    }

    //
    public List<Item> getExpireditems() {
        return checkExpiryDates();

    }

    public List<Item> checkExpiryDates() {
        List<Item> expiredItems = new LinkedList<>();
        for (LocalDate expirationDate : Items.keySet()) {
            if (expirationDate.isBefore(LocalDate.now())) {
                Item item = Items.get(expirationDate);
                expiredItems.add(item);
                item.isExpired();
            }
        }
        return expiredItems;
    }

    //
    public String removeStoreItem(LocalDate eDate,int amount) {


        if (eDate != null||Items.get(eDate)!=null) {
        Item item = Items.get(eDate);
        if(item.getStoreamount()>=amount){
        item.removeStoreItem(amount);
        if (item.getfullAmount()==0&&item.getDamaged()==0&&item.getExpired()==0) {
        Items.remove(eDate);
        }
        return "Item removed";
        }
    
        else
        throw new IllegalArgumentException("cannot add item");
    }
    else
    throw new IllegalArgumentException("cannot add item");

    }

    //
    public String removestorageItem(LocalDate eDate,int amount) {

        if (eDate != null||Items.get(eDate)!=null) {
            Item item = Items.get(eDate);
            if(item.getStorageamount()>=amount){
            item.removestorageitem(amount);
            if (item.getfullAmount()==0&&item.getDamaged()==0&&item.getExpired()==0) {
            Items.remove(eDate);
            }
            return "Item removed";
            }
        
            else
            throw new IllegalArgumentException("cannot add item");
        }
        else
        throw new IllegalArgumentException("cannot add item");
    
        }

    //
    public String addDamfromstore(LocalDate exp,int amount) {
        if (Items.isEmpty()) {
            throw new IllegalArgumentException("cannot add item");
        }
        Item item = Items.get(exp);
        if (item != null && item.getStoreamount() >=amount) {
            item.addDmgstore(amount);

            return "add damg item";

        } else
        throw new IllegalArgumentException("cannot add item");

    }

    //
    public String addDamfromstorage(LocalDate exp,int amount) {
        if (Items.isEmpty()) {
            throw new IllegalArgumentException("cannot add item");
        }
        Item item = Items.get(exp);
        if (item != null && item.getStorageamount() >=amount) {
            item.addDmgstorage(amount);

            return "add damg item";

        } else
            throw new IllegalArgumentException("cannot add item");


    }

    public List<Item> getDamaged() {
        return checkDamaged();

    }

    public List<Item> checkDamaged() {
        List<Item> Damageditems = new LinkedList<>();
        for (Item item : Items.values()) {
            if (item.getDamaged() != 0)
                Damageditems.add(item);

        }
        return Damageditems;
    }
    
    public int getDamagedamount() {
int out=0;
for (Item item : checkDamaged()) {
    out+=item.getDamaged();
    
}
return out;
    }
}