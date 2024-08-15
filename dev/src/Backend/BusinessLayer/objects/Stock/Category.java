package Backend.BusinessLayer.objects.Stock;

import java.util.LinkedList;
import java.util.List;
import Backend.DataAccessLayer.stockData.CategoryDTO;
public class Category {
    private String name;
    private List<Product> products;
    private List<Category> subCategories;
    private Category fatherCategory;
    private CategoryDTO categoryDTO;

    public CategoryDTO getCategoryDTO() {
        return categoryDTO;
    }

    public void setCategoryDTO(CategoryDTO categoryDTO) {
        this.categoryDTO = categoryDTO;
    }

    public Category(String name, Category fatherCategory) {
        this.products = new LinkedList<Product>();
        this.name = name;
        this.subCategories = new LinkedList<Category>();
        this.fatherCategory = fatherCategory;
        if(fatherCategory!=null)
        this.categoryDTO=new CategoryDTO(name,fatherCategory.getName());
        else
        this.categoryDTO=new CategoryDTO(name,null);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public Category getFatherCategory() {
        return fatherCategory;
    }

    public void setFatherCategory(Category fatherCategory) {
        this.fatherCategory = fatherCategory;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void addSubCategory(Category category) {
        this.subCategories.add(category);
    }
}
