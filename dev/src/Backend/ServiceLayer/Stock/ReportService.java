package Backend.ServiceLayer.Stock;


import java.util.List;

import Backend.BusinessLayer.Controllers.Stock.productscontroller;
import Backend.BusinessLayer.objects.Stock.Category;
import Backend.ServiceLayer.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ReportService {
    public String makeJson(Object toConvert) {
        Gson g = new Gson();
        String pre = g.toJson(toConvert);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(pre);
        return gson.toJson(jsonElement);
    }


    public productscontroller productsController ;

    public ReportService(ProductService productsController ) {
        this.productsController=productsController.getProductsController();
    }




    public String getDefectiveItemsByStore() {
        String json;
        Response res;
        try {
            String msg= productsController.getDefectiveItemsByStore();
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    public String getProductsAndAmount(List<Category> categories) {
        String json;
        Response res;
        try {
            String msg= productsController.getProductsAndAmount(categories);
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }
    public String getDamagedItemReportsByStore() {
        String json;
        Response res;
        try {
            String msg= productsController.getDamagedItemReportsByStore();
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    public String getExpiredItemReportsByStore() {
        String json;
        Response res;
        try {
            String msg= productsController.getExpiredItemReportsByStore();
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }

    public String getMinReport() {
        String json;
        Response res;
        try {
            String msg= productsController.getMinReport( );
            res = new Response<>(msg);

        } catch (Exception e) {
            res = new Response(e);

        }
        json = makeJson(res);
        return json;
    }
}

