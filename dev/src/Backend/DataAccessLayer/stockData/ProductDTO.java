package Backend.DataAccessLayer.stockData;

import Backend.BusinessLayer.objects.Stock.Product;

public class ProductDTO {
    private int id;
    private String name;
    private int minAmount;
    private boolean inSale;
     private double sellingPrice;
     private double DiscountPrice;
     private double manufacturePrice;
     private int shelfNumber;
     private String manufacturer;
     private String category;
    public ProductDTO(int id, String name, int minAmount, boolean inSale, double sellingPrice, double discountPrice,
            double manufacturePrice, int shelfNumber, String manufacturer, String category) {
        this.id = id;
        this.name = name;
        this.minAmount = minAmount;
        this.inSale = inSale;
        this.sellingPrice = sellingPrice;
        DiscountPrice = discountPrice;
        this.manufacturePrice = manufacturePrice;
        this.shelfNumber = shelfNumber;
        this.manufacturer = manufacturer;
        this.category = category;
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
    public boolean isInSale() {
        return inSale;
    }
    public void setInSale(boolean inSale) {
        this.inSale = inSale;
    }
    public double getSellingPrice() {
        return sellingPrice;
    }
    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
    public double getDiscountPrice() {
        return DiscountPrice;
    }
    public void setDiscountPrice(double discountPrice) {
        DiscountPrice = discountPrice;
    }
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
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Product dto2Object(){
       return new Product(id,name,minAmount,sellingPrice,manufacturePrice,shelfNumber,manufacturer);
    }
   
}
