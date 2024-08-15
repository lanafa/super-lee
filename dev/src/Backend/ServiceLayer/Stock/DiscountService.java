package Backend.ServiceLayer.Stock;

import Backend.BusinessLayer.Controllers.Stock.discountcontroller;
import Backend.BusinessLayer.objects.Stock.Category;
import Backend.BusinessLayer.objects.Stock.Discount;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.ServiceLayer.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.time.LocalDate;
import java.util.List;

public class DiscountService {
    public String makeJson(Object toConvert) {
        Gson g = new Gson();
        String pre = g.toJson(toConvert);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(pre);
        return gson.toJson(jsonElement);
    }

    public discountcontroller discountController = discountcontroller.getInstance();

    public DiscountService() {
    }

    public String buildProductDiscount(Product product, double percent, LocalDate start,
            LocalDate end) {
        String json;
        Response res;
        try {
            String msg = discountController.buildProductDiscount(product, percent, start, end);

            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    //
    public String buildCategoryDiscount(Category category, double percent, LocalDate start,
            LocalDate end) {
        String json;
        Response res;
        try {
            String msg = discountController.buildCategoryDiscount(category, percent, start, end);
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    // //
    public String endDiscounts() {
        String json;
        Response res;
        try {
            String msg = discountController.endDiscounts();
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    public String addPDiscount(Product product, double discount) {
        String json;
        Response res;
        try {
            String msg = discountController.addPDiscount(product, discount);
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    public String endDiscount(Product product) {
        String json;
        Response res;
        try {
            String msg = discountController.endDiscount(product);

            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    public String geDiscounts() {
        String json;
        Response res;
        try {
            List<Discount> msg = discountController.returnDiscounts();
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    public String getactiveCategoriesDiscounts() {
        String json;
        Response res;
        try {
            String msg = discountController.getactiveCategoriesDiscounts();
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    public String getactiveProductsDiscounts() {
        String json;
        Response res;
        try {
            String msg = discountController.getactiveProductsDiscounts();
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }
}
