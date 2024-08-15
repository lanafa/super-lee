package Backend.ServiceLayer.Stock;

import java.time.LocalDate;
import java.util.List;

import Backend.BusinessLayer.Controllers.Stock.productscontroller;
import Backend.BusinessLayer.objects.Stock.Category;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.ServiceLayer.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ProductService {
    public String makeJson(Object toConvert) {
        Gson g = new Gson();
        String pre = g.toJson(toConvert);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(pre);
        return gson.toJson(jsonElement);
    }

    public productscontroller productsController = productscontroller.getInstance();

    public productscontroller getProductsController() {
        return productsController;
    }

    public ProductService() {
    }

    //

    //
    public String addProductToStore(int barcode, String name, int minAmount, double sellingPrice,
            double manufacturingPrice,
            int shelfNumber, String manufacturer, Category category) {
        String json;
        Response res;
        try {
            String msg = productsController.buildProduct(barcode, name, minAmount, sellingPrice, manufacturingPrice,
                    shelfNumber, manufacturer, category);
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    //
    public String changeProductPrice(int productId, double newPrice) {
        String json;
        Response res;
        try {
            String msg = productsController.changeProductPrice(productId, newPrice);

            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    //
    public String changeProductName(int productId, String newName) {
        String json;
        Response res;
        try {
            String msg = productsController.changeProductName(productId, newName);
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    //
    public String returnInformationProducts() {
        String json;
        Response res;
        try {
            String msg = productsController.returnInformationProducts();
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    public String returnInformationProduct(int productid) {
        String json;
        Response res;
        try {
            String msg = productsController.returnInformationProduct(productid);
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    //
    public String changeProductMinAmount(int productId, int min) {
        String json;
        Response res;
        try {
            String msg = productsController.changeProductMinAmount(productId, min);
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    //
    public String addStorageItem(int productId, LocalDate expirationDate, int quantity) {
        String json;
        Response res;
        try {
            String msg = productsController.addStorageItem(productId, expirationDate, quantity);

            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    //
    public String addStoreItem(LocalDate expirationDate, int productId, int quantity) {
        String json;
        Response res;
        try {
            String msg = productsController.addStoreItem(productId, expirationDate, quantity);

            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    // //

    // public String moveProductToCategory( int productId, Category newCategoryId) {
    // String output = "{}";
    // try {
    // output = productsController.moveProductToCategory( productId, newCategoryId);
    // dataBaseController.moveProductToCategory(productId, newCategoryId.getName());
    // System.out.println(output);
    // return output;
    // } catch (Exception e) {
    // System.out.println(e.getMessage());

    // return e.getMessage();
    // }
    // }
    public String removeStoreItem(int productID, LocalDate eexDate, int amount) {
        String json;
        Response res;
        try {
            String msg = productsController.removeStoreItem(productID, eexDate, amount);
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    public String removeStorageItem(int productID, LocalDate eexDate, int amount) {
        String json;
        Response res;
        try {
            String msg = productsController.removeStorageItem(productID, eexDate, amount);
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }
    // public String ADDsale(int ProductID,LocalDate eexDate){
    // String output = "{}";
    // try {
    // output = productsController.ADDsale(ProductID,quantity);
    // System.out.println(output);
    // return output;
    // } catch (Exception e) {
    // System.out.println(e.getMessage());

    // return e.getMessage(); }
    // }
    public String addDamfromstore(int id, LocalDate eexDate, int amount) {
        String json;
        Response res;
        try {
            String msg = productsController.addDamfromstore(eexDate, id, amount);
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    public String addDamfromstorage(int id, LocalDate eexDate, int amount) {
        String json;
        Response res;
        try {
            String msg = productsController.addDamfromstorage(eexDate, id, amount);
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

}
