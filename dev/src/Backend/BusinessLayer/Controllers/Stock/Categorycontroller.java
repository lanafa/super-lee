package Backend.BusinessLayer.Controllers.Stock;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.objects.Stock.Category;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.DataAccessLayer.stockData.CategoryDTO;

public class Categorycontroller {
   
    private DataBaseController dataBaseController=DataBaseController.getInstance();

    public DataBaseController getDataBaseController() {
        return dataBaseController;
    }
    public void setDataBaseController(DataBaseController dataBaseController) {
        this.dataBaseController = dataBaseController;
    }
    private static Categorycontroller instance = null;
    private HashMap<String, Category> categories;

   

    public Categorycontroller() {
        categories = new HashMap<String, Category>();
       
    }

    public static Categorycontroller getInstance() {
        if(instance == null){
            instance = new Categorycontroller();
        }
        return instance;
    }
    public HashMap<String, Category> getCategories() {
        return categories;
    }
    
    public String buildCategory(String name, List<Product> products, List<Category> subCategories,
                                String fatherCategory) throws SQLException {
        if(!fatherCategory.equals("0")&&categories.containsKey(fatherCategory)){
            Category category = new Category(name, returnCategory(fatherCategory));
            categories.put(category.getName(), category);
            try {
                dataBaseController.addCategory(returnCategory(name).getCategoryDTO());
//heeeeeeeeeeeeeeeeeey
                
            } catch (Exception e) {
                // TODO: handle exception
            }

            addSubCategory(fatherCategory,category.getName());
            return "Category: " +name +" was added successfully";

        }

        

else if(!categories.containsKey(fatherCategory)&&!fatherCategory.equals("0")) {
    return "no such father category";

}
else{
    Category category = new Category(name, null);
    categories.put(name, category);
                dataBaseController.addCategory(returnCategory(name).getCategoryDTO());

    return "Category: " +name +" was added successfully";

}
       
    }

    public String buildCategory2(String name, List<Product> products, List<Category> subCategories,
    String fatherCategory) throws SQLException {
if(!fatherCategory.equals("0")&&categories.containsKey(fatherCategory)){
Category category = new Category(name, returnCategory(fatherCategory));
categories.put(category.getName(), category);
try {
//dataBaseController.addCategory(returnCategory(name).getCategoryDTO());
//heeeeeeeeeeeeeeeeeey

} catch (Exception e) {
// TODO: handle exception
}

addSubCategory(fatherCategory,category.getName());
return "Category: " +name +" was added successfully";

}



else if(!categories.containsKey(fatherCategory)&&!fatherCategory.equals("0")) {
return "no such father category";

}
else{
Category category = new Category(name, null);
categories.put(name, category);
//dataBaseController.addCategory(returnCategory(name).getCategoryDTO());

return "Category: " +name +" was added successfully";

}

}
    
    public String removeCategory( String categoryID) throws SQLException {
        Category categoryToRemove = returnCategory( categoryID);
        if (categoryToRemove == null) {
            throw new IllegalArgumentException("There is no category with ID " + categoryID);
        }

        if (!categoryToRemove.getSubCategories().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete a category that has subcategories");
        }

        if (!categoryToRemove.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete a category that has products still assigned to it");
        }

        categoryToRemove.setFatherCategory(null);
        categories.remove(categoryID);
        dataBaseController.deleteCategory(categoryID);

        return "Category removed successfully";
    }
    
    public Category returnCategory(String id) {
        if(id==""){
            return null;}
        if (categories.containsKey(id)) {
            return categories.get(id);
        } else {
            throw new IllegalArgumentException("No such category");
        }

    }
    
    public List<Category> returnCategories() {


        List<Category> categoriesList =  new LinkedList<Category>();


        for (Category category : categories.values()) {
            categoriesList.add(category);
        }
        return categoriesList;
    }
    public String returnCategories1() {


        String out = "";

        for (Category category : categories.values()) {
            List<String> subs=new LinkedList<>();
            List<String> products=new LinkedList<>();

            if (category.getFatherCategory() != null) {
                for (Category cat:category.getSubCategories()
                     ) {subs.add(cat.getName());

                } for (Product pro:category.getProducts()
                ) {products.add(pro.getName());

                }
                out += "Category name: " + category.getName()  + " subCategories: " + subs + " Father category: " + category.getFatherCategory().getName() + " Products: " + products+"\n\n";
            } else {
                for (Category cat:category.getSubCategories()
                ) {subs.add(cat.getName());

                } for (Product pro:category.getProducts()
                ) {products.add(pro.getName());}

                out += "Category name: " + category.getName() + " categoryID: " +  " subCategories: " + subs + " Father category: no father category " + " Products: " + products+" \n\n";
            }
        }
        return out;

    }
    public void addDto(CategoryDTO categoryDTO) throws SQLException{
        String out;
        if(categoryDTO.getFaString()==null){out="0";}
        else{
            out=categoryDTO.getFaString();
        }
        buildCategory2(categoryDTO.getName(), null, null, out);
        }
    
    public String addSubCategory(String categoryID, String  subCategoryID) throws SQLException {

        if (categories.containsKey(subCategoryID)) {

            Category subCategory = returnCategory( subCategoryID);

            if (categories.containsKey(categoryID)) {
                if(categories.get(subCategoryID).getFatherCategory()==null){
                categories.get(categoryID).addSubCategory(subCategory);
                categories.get(subCategoryID).setFatherCategory( categories.get(categoryID));
                dataBaseController.changeCategoryFather(categoryID, subCategoryID);
 
                return "Subcategory added successfully";
                }
                throw new IllegalArgumentException("No such category");

            } else {
                throw new IllegalArgumentException("No such category");
            }
        }
        else    throw new IllegalArgumentException("No such category");

    }
    
    public String setFatherCategory( String categoryID, String fatherCategoryID) throws SQLException {

        Category category = returnCategory( categoryID);
        Category fatherCategory = returnCategory( fatherCategoryID);
        if (category == null || fatherCategory == null) {
            throw new IllegalArgumentException("Invalid category ID");
        }
        if (category.equals(fatherCategory)) {
            throw new IllegalArgumentException("A category cannot be its own father category");
        }
        category.setFatherCategory(fatherCategory);
        fatherCategory.addSubCategory(category);
        dataBaseController.changeCategoryFather(fatherCategoryID, categoryID);

        return "Father category set successfully";

    }

    public String changeCategoryName(String categoryID, String newName) throws SQLException {

        if (categories.isEmpty()) {
            throw new IllegalArgumentException("No categories found: " );
        }
        Category category = returnCategory( categoryID);
        category.setName(newName);
        categories.remove(categoryID);
        categories.put(newName, category);
        dataBaseController.changeCategoryName(newName,categoryID);

        return "Category name changed successfully.";
    }

}
