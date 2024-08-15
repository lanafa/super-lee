package Backend.BusinessLayer.Controllers.Stock;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.objects.Stock.Category;
import Backend.BusinessLayer.objects.Stock.Discount;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.DataAccessLayer.stockData.*;


public class discountcontroller {
    
    private int counter = 1;
    public void setCounter(int counter) {
        this.counter = counter;
    }
    private static discountcontroller instance = null;
    private HashMap<Integer, Discount> productDiscounts;
    DataBaseController dataBaseController= DataBaseController.getInstance();
    public HashMap<Integer, Discount> getProductDiscounts() {
        return productDiscounts;
    }
    private HashMap<Integer, Discount> categoryDiscounts;

    public discountcontroller() {
        productDiscounts = new HashMap<>();
        categoryDiscounts = new HashMap<>();
     
    }

    public static discountcontroller getInstance() {
        if(instance == null){
            instance = new discountcontroller();
        }
        return instance;
    }
    //
    public String buildProductDiscount( Product product, double percent, LocalDate start, LocalDate end) throws SQLException {
        if(product!=null){
            Discount discount = new Discount(product, start, end, percent, counter);
            DiscountProductDTO discountDTO= new DiscountProductDTO(counter, product.getId(), start.toString(), end.toString(), percent) ;
            try {
                dataBaseController.add_discount(discountDTO);

            } catch (Exception e) {
            }
            if (discount.getStartDate().isBefore(LocalDate.now()) || discount.getStartDate().isEqual(LocalDate.now())) {
                addPDiscount(product, percent);}
            productDiscounts.put(counter, discount);

            counter++;           
            return "Discount added successfully";}
        else throw new IllegalArgumentException("no such product");

    }
    //
    public String buildCategoryDiscount(Category category, double percent, LocalDate start,
                                        LocalDate end) throws SQLException {
        if(category!=null){
            Discount discount = new Discount(counter, category, start, end, percent);
            DiscountCategoryDTO discountDTO= new DiscountCategoryDTO(counter, category.getName(), start.toString(), end.toString(), percent) ;
          try {
            dataBaseController.add_discount(discountDTO);

          } catch (Exception e) {
          }
            for (Product product : category.getProducts()) {
                if (discount.getStartDate().isBefore(LocalDate.now()) || discount.getStartDate().isEqual(LocalDate.now())) {

                addPDiscount(product, percent);                 
            }
             }
            categoryDiscounts.put(counter, discount);
            counter++; 
            System.out.println( "The discount for category: "+ discount.getCategory().getName()+" has started " );



            return "Discount added successfully";}
        else throw new IllegalArgumentException("no such category");
    }

    public List<Product> getExpiredProductsDiscounts() throws SQLException {
        List<Product> products = new LinkedList<>();

        if (!productDiscounts.isEmpty()) {
            for (Discount discount : productDiscounts.values()) {
                if (discount.getEndDate().compareTo(LocalDate.now()) > 0) {
                    products.add(discount.getProduct());
                    dataBaseController.deletediscountproduct(discount.getDiscountID());
                    System.out.println( "The discount for product: "+ discount.getProduct().getId()+" has finished " );
                }
            }
        }
        return products;
    }

    public List<Category> getExpiredCategoriesDiscounts() throws SQLException {
        List<Category> categories = new LinkedList<>();
        
        if (!categoryDiscounts.isEmpty()) {
            for (Discount discount : categoryDiscounts.values()) {
                if (discount.getEndDate().compareTo(LocalDate.now()) < 0) {
                    categories.add(discount.getCategory());
                    dataBaseController.deletediscountcategory(discount.getDiscountID());
                    System.out.println( "The discount for category: "+ discount.getCategory().getName()+" has finished " );
                }
            }
        }
        return categories;
    }
    public String getactiveCategoriesDiscounts() {
        String out="";

        if (!categoryDiscounts.isEmpty()) {
            for (Discount discount : categoryDiscounts.values()) {
                if (discount.getEndDate().compareTo(LocalDate.now()) > 0) {
                    out+= ( "The discount for category: "+ discount.getCategory().getName()+" is still active /n" );
                }
            }
            return out;
        }
        return "no active discounts";
    }
    public String getactiveProductsDiscounts() {
        String out="";
        if (!productDiscounts.isEmpty()) {
            for (Discount discount : productDiscounts.values()) {
                if (discount.getEndDate().compareTo(LocalDate.now()) < 0) {
                    out+=  "The discount for product: "+ discount.getProduct().getId()+" is still active \n " ;
                }
            }
            return out;
        }
        return "no active discounts";
    }
    //
    public String endDiscounts() throws SQLException {

        for (Category cat : getExpiredCategoriesDiscounts()) {
            for (Product product : cat.getProducts()) {

                product.setInSale(false);
                checkforbetter(product);

            }
        }
        for (Product product : getExpiredProductsDiscounts()) {
            
            product.setInSale(false);
            checkforbetter(product);

        }

        return "Discounts ended successfully";
    }
    public String addPDiscount( Product product, Double discount) throws SQLException {


        if (!product.isInSale()) {
            product.startDiscount(discount);
try {
    dataBaseController.startDiscount(discount, product.getId());

} catch (Exception e) {
}
            
            return "Discount added successfully";
        } else {
            product.setInSale(false);
            double salePrice = product.getSellingPrice()
                    - (product.getSellingPrice() * discount) / 100;

            if (salePrice < product.getSalePrice()) {
                product.startDiscount(discount);
                try {
                    dataBaseController.startDiscount(discount, product.getId());
                
                } catch (Exception e) {
                }                return "New discount added successfully";
            } else {
                product.setInSale(true);
                return "Discount not applied as it is not better than the current sale price.";
            }
        }

    }

    public String endDiscount( Product product) {


        product.setInSale(false);
        return "Discount ended successfully";
    }
    public List<Discount> returnDiscounts() {


        List<Discount> discounts = new LinkedList<Discount>();

        if (!productDiscounts.isEmpty()) {
            for (Discount discount : productDiscounts.values()) {
                discounts.add(discount);
            }
        }
        if (!categoryDiscounts.isEmpty()) {
            for (Discount discount : categoryDiscounts.values()) {
                discounts.add(discount);
            }
        }

        return discounts;
    }
    
    public Discount returnDiscountp (int id) {

        

        return productDiscounts.get(id);
    }
    public void checkforbetter(Product productid) throws SQLException{
        double percenct=0;
        for (Discount cDiscount : categoryDiscounts.values()) {
    for (Product product : cDiscount.getCategory().getProducts()) {
        double price=cDiscount.getPercent();
        if(product.getId()==productid.getId() && price>percenct && cDiscount.getStartDate().isBefore(LocalDate.now())){
            percenct= price;
    

        }}}
        for (Discount pDiscount : productDiscounts.values()) {
                double price=pDiscount.getPercent();
                if(pDiscount.getProduct().getId()==productid.getId() && price>percenct && pDiscount.getStartDate().isBefore(LocalDate.now())){
                    percenct= price;
            
                }}
                if(percenct!=0){
                    dataBaseController.startDiscount(percenct, productid.getId());
                    addPDiscount(productid, percenct);
    
    
                }
               
            
        }

    public void ADDdtodiscountC(DiscountCategoryDTO dto) throws SQLException {
        LocalDate startdate= LocalDate.parse(dto.getStartDate());
        LocalDate enddate= LocalDate.parse(dto.getEndDate());


        Discount dis= new Discount(counter, Categorycontroller.getInstance().returnCategory(dto.getCategory()), startdate, enddate, dto.getPercent());
        productDiscounts.put(counter, dis);

        counter++;    }

    public void ADDdtodiscountP(DiscountProductDTO dto) throws SQLException {
        LocalDate startdate= LocalDate.parse(dto.getStartDate());
        LocalDate enddate= LocalDate.parse(dto.getEndDate());
Discount dis= new Discount(productscontroller.getInstance().returnProduct(dto.getProduct()), startdate, enddate, dto.getPercent(), dto.getDiscountID());
    productDiscounts.put(counter, dis);
    counter++;

}


}
