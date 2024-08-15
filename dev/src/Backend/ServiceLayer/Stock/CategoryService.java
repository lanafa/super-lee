package Backend.ServiceLayer.Stock;
import Backend.BusinessLayer.Controllers.Stock.Categorycontroller;
import Backend.BusinessLayer.objects.Stock.Category;
import Backend.ServiceLayer.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import java.util.List;

public class CategoryService {
    public String makeJson(Object toConvert) {
        Gson g = new Gson();
        String pre = g.toJson(toConvert);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(pre);
        return gson.toJson(jsonElement);
    }

    public Categorycontroller categoryController =Categorycontroller.getInstance();

    public CategoryService() {
    }
    //
    public Category getCategory(String categoryID) {
        try {

            return categoryController.returnCategory( categoryID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public String returnCategories1() {
        String json;
        Response res;
        try {
             String msg= categoryController.returnCategories1();
             res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }
    //
    public String addCategory(String name, String parentCategoryID) {
        String json;
        Response res;
        try {

            String msg= categoryController.buildCategory(name, null, null, parentCategoryID);
            res = new Response<>(msg);


        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }
    //
    public String deleteCategory( String categoryID) {
        String json;
        Response res;
        try {
            String msg= categoryController.removeCategory(categoryID);
            res = new Response<>(msg);



        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }
    //
   
    //
    public String addSubCategory( String categoryID, String subCategoryID) {
        String json;
        Response res;
        try {
            String msg= categoryController.addSubCategory( categoryID, subCategoryID);
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }
    //
    public String addFatherCategory( String categoryID, String fatherCategoryID) {
        String json;
        Response res;
        try {
            String msg= categoryController.setFatherCategory( categoryID, fatherCategoryID);
            res = new Response<>(msg);


        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }
    //
    public String changeCategoryName( String categoryID, String newName) {
        String json;
        Response res;
        try {
            String msg= categoryController.changeCategoryName( categoryID, newName);
            res = new Response<>(msg);

           

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }
}
