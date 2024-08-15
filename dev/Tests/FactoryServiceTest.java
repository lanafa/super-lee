package Tests;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import Backend.BusinessLayer.Controllers.Stock.productscontroller;
import Backend.BusinessLayer.objects.Stock.Category;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.ServiceLayer.ServiceFactory;

class FactoryServiceTest {
    private ServiceFactory factoryService= new ServiceFactory();

    @BeforeEach
    void setUp() {
        factoryService = new ServiceFactory();
    }
    @AfterEach
    void cleanUp() {
factoryService.getCategoryService().categoryController.getCategories().clear(); 
for (Product p : productscontroller.getInstance().getProducts().values()) {
    p.getItems().clear();

    
}   
factoryService.getProductService().productsController.getProducts().clear();    
factoryService.getDiscountService().discountController.getProductDiscounts().clear();    
// factoryService.getDiscountService().discountController.get().clear();    

}


    @org.junit.jupiter.api.Test
    void addCategory() {
        factoryService.addCategory("Milk", "0");
        assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");

        Category category= factoryService.getCategoryService().categoryController.returnCategories().get(0);
        assertEquals("Milk", category.getName(), "Adding category failed" );
        assertNull(category.getFatherCategory(), "Adding category failed");

    }

    

    @org.junit.jupiter.api.Test
    void deleteCategory() {
        factoryService.addCategory("Milk", "0");
        assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");
        factoryService.deleteCategory("Milk");
        assertEquals(0, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");


    }

    @org.junit.jupiter.api.Test
    void addSubCategory() {
        factoryService.addCategory("Milk", "0");
        assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");
        assertEquals(0, factoryService.getCategoryService().getCategory("Milk").getSubCategories().size(), "Adding category failed");
        factoryService.addCategory("3% Fat","0");
        factoryService.addSubCategory("Milk", "3% Fat");
        assertEquals(1, factoryService.getCategoryService().getCategory("Milk").getSubCategories().size(), "Adding category failed");
        assertEquals("Milk", factoryService.getCategoryService().getCategory("3% Fat").getFatherCategory().getName(), "Adding category failed");

    


    }

   @org.junit.jupiter.api.Test
    void addFatherCategory() {
        factoryService.addCategory("3% Fat", "0");
        assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");
       assertNull(factoryService.getCategoryService().getCategory("3% Fat").getFatherCategory(), "Adding category failed");
       factoryService.addCategory("Milk", "0");
       factoryService.addFatherCategory("3% Fat", "Milk");
       assertEquals("Milk", factoryService.getCategoryService().getCategory("3% Fat").getFatherCategory().getName(), "Adding category failed");
        assertEquals(1, factoryService.getCategoryService().getCategory("Milk").getSubCategories().size(), "Adding category failed");


    }

    @org.junit.jupiter.api.Test
    void changeCategoryName() {
        factoryService.addCategory("Milk", "0");
        assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");

        Category category= factoryService.getCategoryService().categoryController.returnCategories().get(0);
        assertEquals("Milk", category.getName(), "Adding category failed" );
        factoryService.changeCategoryName("Milk", "3% Milk");
        assertEquals("3% Milk", category.getName(), "Adding category failed" );

    }
     @org.junit.jupiter.api.Test
    void addProductToStore() {

        factoryService.addCategory("Milk", "0");
        assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");
        factoryService.addProductToStore(1,"milk", 10, 10, 7, 1, "Strauss", "Milk");
        assertEquals(1, factoryService.getProductService().productsController.getProducts().size(), "Adding Product failed");
        assertEquals(10, factoryService.getProductService().productsController.returnProduct(1).getSellingPrice(), "Adding Product failed");
        assertEquals("milk", factoryService.getProductService().productsController.returnProduct(1).getName(), "Adding Product failed");
        assertEquals(10, factoryService.getProductService().productsController.returnProduct(1).getMinAmount(), "Adding Product failed");
        assertEquals(7, factoryService.getProductService().productsController.returnProduct(1).getManufacturePrice(), "Adding Product failed");
        assertEquals(1, factoryService.getProductService().productsController.returnProduct(1).getShelfNumber(), "Adding Product failed");
        assertEquals("Strauss", factoryService.getProductService().productsController.returnProduct(1).getManufacturer(), "Adding Product failed");
        assertEquals("Milk", factoryService.getProductService().productsController.returnProduct(1).getCategorname(), "Adding Product failed");

        

        
    }

    @org.junit.jupiter.api.Test
    void buildProductDiscount() {
        
        factoryService.addCategory("Milk", "0");
        assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");

        factoryService.addProductToStore(1,"milk", 10, 10, 7, 1, "Strauss", "Milk");

        assertEquals(1, factoryService.getProductService().getProductsController().getProducts().size(), "Adding Product failed");
        assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getSellingPrice(), "Adding Product failed");
        factoryService.buildProductDiscount(1, 20, LocalDate.now(), LocalDate.now());
        assertEquals(8, factoryService.getProductService().getProductsController().returnProduct(1).getSellingPrice(), "Adding Product failed");



     }

    @org.junit.jupiter.api.Test
    void buildCategoryDiscount() {
         
        factoryService.addCategory("Milk", "0");
        assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");

        factoryService.addProductToStore(1,"milk", 10, 10, 7, 1, "Strauss", "Milk"); assertEquals(1, factoryService.getProductService().productsController.getProducts().size(), "Adding Product failed");

        assertEquals(10, factoryService.getProductService().productsController.returnProduct(1).getSellingPrice(), "Adding Product failed");
        factoryService.buildCategoryDiscount("Milk", 20, LocalDate.now(), LocalDate.now());

        assertEquals(8, factoryService.getProductService().getProductsController().returnProduct(1).getSellingPrice(), "Adding Product failed");
      
        assertEquals(1, factoryService.getDiscountService().discountController.returnDiscounts().size(), "Adding Product failed");
    }

    
 @org.junit.jupiter.api.Test
    void addStorageItem() {
        factoryService.addCategory("Milk", "0");
        assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");

        factoryService.addProductToStore(1,"milk", 10, 10, 7, 1, "Strauss", "Milk");factoryService.addStorageItem(1, LocalDate.now().plusDays(1), 10);
        assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getStorageAmount(), "Adding Items failed");


    }

    @org.junit.jupiter.api.Test
    void addStoreItem() {
        factoryService.addCategory("Milk", "0");
        assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");

        factoryService.addProductToStore(1,"milk", 10, 10, 7, 1, "Strauss", "Milk");factoryService.addStoreItem(LocalDate.now().plusDays(1),1, 10);
        assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getStoreAmount(), "Adding Items failed");
    }

   

   

    @org.junit.jupiter.api.Test
    void changeProductPrice() {
        factoryService.addCategory("Milk", "0");
        assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");

        factoryService.addProductToStore(1,"milk", 10, 10, 7, 1, "Strauss", "Milk");assertEquals(1, factoryService.getProductService().productsController.getProducts().size(), "Adding Product failed");
        assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getSellingPrice(), "Adding Product failed");
        assertEquals("milk", factoryService.getProductService().getProductsController().returnProduct(1).getName(), "Adding Product failed");
        assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getMinAmount(), "Adding Product failed");
        assertEquals(7, factoryService.getProductService().getProductsController().returnProduct(1).getManufacturePrice(), "Adding Product failed");
        assertEquals(1,factoryService.getProductService().getProductsController().returnProduct(1).getShelfNumber(), "Adding Product failed");
        assertEquals("Strauss", factoryService.getProductService().getProductsController().returnProduct(1).getManufacturer(), "Adding Product failed");
        assertEquals("Milk", factoryService.getProductService().getProductsController().returnProduct(1).getCategorname(), "Adding Product failed");
  factoryService.changeProductPrice(1, 12);
  assertEquals(12, factoryService.getProductService().getProductsController().returnProduct(1).getSellingPrice(), "Changing the product price failed");

    }

    @org.junit.jupiter.api.Test
    void changeProductName() {
        factoryService.addCategory("Milk", "0");

        factoryService.addProductToStore(1,"milk", 10, 10, 7, 1, "Strauss", "Milk");
        assertEquals(1, factoryService.getProductService().productsController.getProducts().size(), "Adding Product failed");
        assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getSellingPrice(), "Adding Product failed");
        assertEquals("milk", factoryService.getProductService().getProductsController().returnProduct(1).getName(), "Adding Product failed");
        assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getMinAmount(), "Adding Product failed");
        assertEquals(7, factoryService.getProductService().getProductsController().returnProduct(1).getManufacturePrice(), "Adding Product failed");
        assertEquals(1, factoryService.getProductService().getProductsController().returnProduct(1).getShelfNumber(), "Adding Product failed");
        assertEquals("Strauss", factoryService.getProductService().getProductsController().returnProduct(1).getManufacturer(), "Adding Product failed");
        assertEquals("Milk",factoryService.getProductService().getProductsController().returnProduct(1).getCategorname(), "Adding Product failed");
  factoryService.changeProductName(1, "Milk");
  assertEquals("Milk", factoryService.getProductService().getProductsController().returnProduct(1).getName(), "Changing the product name failed");
    }

    @org.junit.jupiter.api.Test
    void changeProductMinAmount() {
        factoryService.addCategory("Milk", "0");

        factoryService.addProductToStore(1,"milk", 10, 10, 7, 1, "Strauss", "Milk");
        assertEquals(1, factoryService.getProductService().getProductsController().getProducts().size(), "Adding Product failed");
        assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getSellingPrice(), "Adding Product failed");
        assertEquals("milk", factoryService.getProductService().getProductsController().returnProduct(1).getName(), "Adding Product failed");
        assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getMinAmount(), "Adding Product failed");
        assertEquals(7, factoryService.getProductService().getProductsController().returnProduct(1).getManufacturePrice(), "Adding Product failed");
        assertEquals(1, factoryService.getProductService().getProductsController().returnProduct(1).getShelfNumber(), "Adding Product failed");
        assertEquals("Milk",factoryService.getProductService().getProductsController().returnProduct(1).getCategorname(), "Adding Product failed");
  factoryService.changeProductMinAmount(1, 5);
  assertEquals(5, factoryService.getProductService().getProductsController().returnProduct(1).getMinAmount(), "Changing the product MinAmount failed");
    }

   

 

@org.junit.jupiter.api.Test
void removeStorageItem() {
    factoryService.addCategory("Milk", "0");
    assertEquals(1, factoryService.getCategoryService().categoryController. returnCategories().size(), "Adding category failed");

    factoryService.addProductToStore(1,"milk", 10, 10, 7, 1, "Strauss", "Milk");
    factoryService.addStorageItem(1, LocalDate.now().plusDays(1), 10);
    assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getStorageAmount(), "Adding Items failed");
    factoryService.removeStorageItem(1, LocalDate.now().plusDays(1), 4);
    assertEquals(6,factoryService.getProductService().getProductsController().returnProduct(1).getStorageAmount(),"Adding Items failed");


}

@org.junit.jupiter.api.Test
void removeStoreItem() {
    factoryService.addCategory("Milk", "0");
    assertEquals(1, factoryService.getCategoryService().categoryController. returnCategories().size(), "Adding category failed");

    factoryService.addProductToStore(1,"milk", 10, 10, 7, 1, "Strauss", "Milk");
    factoryService.addStoreItem(LocalDate.now().plusDays(1),1, 10);
    assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getStoreAmount(), "Adding Items failed");
    factoryService.removeStoreItem(1, LocalDate.now().plusDays(1), 4);
    assertEquals(6,factoryService.getProductService().getProductsController().returnProduct(1).getStoreAmount(),"removing Items failed");
}
@org.junit.jupiter.api.Test
void addDefectiveStorageItem() {
    factoryService.addCategory("Milk", "0");
    assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");

    factoryService.addProductToStore(1,"milk", 10, 10, 7, 1, "Strauss", "Milk");
    factoryService.addStorageItem(1, LocalDate.now().plusDays(1), 10);
    assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getStorageAmount(), "Adding Items failed");
    factoryService.addDamfromstorage(LocalDate.now().plusDays(1), 1, 5);
    assertEquals(5, factoryService.getProductService().getProductsController().returnProduct(1).getStorageAmount(), "Adding Items failed");
}

@org.junit.jupiter.api.Test
void addDefectiveStoreItem() {
    factoryService.addCategory("Milk", "0");
    assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");

    factoryService.addProductToStore(1,"milk", 10, 10, 7, 1, "Strauss", "Milk");factoryService.addStoreItem(LocalDate.now().plusDays(1),1, 10);
    assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getStoreAmount(), "Adding Items failed");
    factoryService.addDamfromstore(LocalDate.now().plusDays(1), 1, 5);
    assertEquals(5, factoryService.getProductService().getProductsController().returnProduct(1).getDamagedamount(), "Adding Items failed");
}
@org.junit.jupiter.api.Test
void getDefectiveStoreItem() {
    factoryService.addCategory("Milk", "0");
    assertEquals(1, factoryService.getCategoryService().categoryController.returnCategories().size(), "Adding category failed");

    factoryService.addProductToStore(1,"milk", 10, 10, 7, 1, "Strauss", "Milk");factoryService.addStoreItem(LocalDate.now().plusDays(1),1, 10);

    assertEquals(10, factoryService.getProductService().getProductsController().returnProduct(1).getStoreAmount(), "Adding Items failed");

    factoryService.addDamfromstore(LocalDate.now().plusDays(1), 1, 5);
    assertEquals(5, factoryService.getProductService().getProductsController().returnProduct(1).getDamagedamount(), "Adding Items failed");
}





}