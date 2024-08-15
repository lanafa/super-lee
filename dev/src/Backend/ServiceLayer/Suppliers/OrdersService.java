package Backend.ServiceLayer.Suppliers;

import java.util.List;
import Backend.BusinessLayer.Controllers.Suppliers.OrdersController;
import Backend.BusinessLayer.objects.*;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.BusinessLayer.objects.Suppliers.Order;
import Backend.BusinessLayer.objects.Suppliers.PeriodicOrder;
import Backend.BusinessLayer.objects.Suppliers.Record;
import Backend.ServiceLayer.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Map;

public class OrdersService {
    private OrdersController ordersController;

    public OrdersService() {
        ordersController = OrdersController.getInstance();
    }

    public String makeJson(Object toConvert) {
        Gson g = new Gson();
        String pre = g.toJson(toConvert);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(pre);
        return gson.toJson(jsonElement);
    }

    public String Advance_day() {
        String json;
        Response res;
        try {
            ordersController.increment_day();
            res = new Response<String>("");
        } catch (Exception ex) {
            res = new Response(ex);
        }
        json = makeJson(res);
        return json;
    }

    public String Prepare_Order(Map<Product, Integer> items) {
        String json;
        Response res;
        try {
            String msg = ordersController.PrepareOrders(items);
            res = new Response<String>(msg);
        } catch (Exception ex) {
            res = new Response(ex);
        }
        json = makeJson(res);
        return json;
    }

    public String cancel_order(int order_id) {
        String json;
        Response res;
        try {
            String msg = ordersController.cancel_order(order_id);
            res = new Response<String>(msg);
        } catch (Exception ex) {
            res = new Response(ex);
        }

        json = makeJson(res);
        return json;
    }

    public String list_Orders() {
        String json=null;
        Response res;
        try {
            Map<Integer, Order> orders = ordersController.getOrders();
            res = new Response<Map<Integer, Order>>(orders);
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{\n");
            jsonBuilder.append("  \"orders\": {\n");
            for (Map.Entry<Integer, Order> entry : orders.entrySet()) {
                int orderId = entry.getKey();
                Order currentOrder = entry.getValue();
                jsonBuilder.append("    \"").append(orderId).append("\": {\n");
                jsonBuilder.append("      \"till_arrival\": ").append(currentOrder.get_till_arrival()).append(",\n");
                jsonBuilder.append("      \"order_id\": ").append(currentOrder.getOrder_id()).append(",\n");
                jsonBuilder.append("      \"supplier_name\": \"").append(currentOrder.getSupplier_name())
                        .append("\",\n");
                jsonBuilder.append("      \"supplier_id\": ").append(currentOrder.getSupplier_id()).append(",\n");
                jsonBuilder.append("      \"address\": \"").append(currentOrder.getAddress()).append("\",\n");
                jsonBuilder.append("      \"contact_number\": \"").append(currentOrder.getContact_number())
                        .append("\",\n");
                jsonBuilder.append("      \"item_list\": [\n");
                for (int i = 0; i < currentOrder.getItem_list().size(); i++) {
                    Record currentItem = currentOrder.getItem_list().get(i);
                    jsonBuilder.append("        {\n");
                    jsonBuilder.append("          \"order_id\": ").append(currentItem.getOrder_id()).append(",\n");
                    jsonBuilder.append("          \"supplier_id\": ").append(currentItem.getSupplier_id())
                            .append(",\n");
                    jsonBuilder.append("          \"quantity\": ").append(currentItem.getQuantity()).append(",\n");
                    jsonBuilder.append("          \"ovl_price\": ").append(currentItem.getOvl_price()).append(",\n");
                    jsonBuilder.append("          \"discount\": ").append(currentItem.getDiscountPrice()).append(",\n");
                    jsonBuilder.append("          \"final_price\": ").append(currentItem.getFinal_price()).append("\n");
                    jsonBuilder.append("        }");
                    if (i < currentOrder.getItem_list().size() - 1) {
                        jsonBuilder.append(",");
                    }
                    jsonBuilder.append("\n");
                }
                jsonBuilder.append("      ],\n");
                jsonBuilder.append("      \"order_price\": ").append(currentOrder.getOrder_price()).append("\n");
                jsonBuilder.append("    }");
                if (!entry.equals(orders.entrySet().iterator().next())) {
                    jsonBuilder.append(",");
                }
                jsonBuilder.append("\n");
            }
            jsonBuilder.append("  }\n");
            jsonBuilder.append("}");
            json=jsonBuilder.toString();
        } catch (Exception ex) {
            res = new Response(ex);
        }
        return json;
    }

    public String add_PeriodicOrder(String supplier_name, int supplier_id, String address, String contact_number,
            List<Record> items, int day) {
        String json;
        Response res;
        try {
            String msg = ordersController.add_PeriodicOrder(supplier_name, supplier_id, address, contact_number, items,
                    day);
            res = new Response<String>(msg);
        } catch (Exception ex) {
            res = new Response(ex);
        }
        json = makeJson(res);
        return json;
    }

    public String add_to_Periodic_items(int order_id, Record item) {
        String json;
        Response res;
        try {
            String msg = ordersController.add_to_Periodic_items(order_id, item);
            res = new Response<String>(msg);
        } catch (Exception ex) {
            res = new Response(ex);
        }
        json = makeJson(res);
        return json;
    }

    public String updatePeriodic_date(int order_id, int day) {
        String json;
        Response res;
        try {
            String msg = ordersController.updatePeriodic_date(order_id, day);
            res = new Response<String>(msg);
        } catch (Exception ex) {
            res = new Response(ex);
        }
        json = makeJson(res);
        return json;
    }

    public String add_toPeriodic_product(int product_id, int order_id, int quantity) {
        String json;
        Response res;
        try {
            String msg = ordersController.add_toPeriodic_product(product_id, order_id, quantity);
            res = new Response<String>(msg);
        } catch (Exception ex) {
            res = new Response(ex);
        }
        json = makeJson(res);
        return json;
    }

    public String listPerOrders() {
        String json="";
        Response res;
        try {
            Map<Integer, PeriodicOrder> orders = ordersController.get_periodic_orders();
            res = new Response<Map<Integer, PeriodicOrder>>(orders);
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{\n");
            jsonBuilder.append("  \"orders\": {\n");
            for (Map.Entry<Integer, PeriodicOrder> entry : orders.entrySet()) {
                int orderId = entry.getKey();
                PeriodicOrder currentOrder = entry.getValue();
                jsonBuilder.append("    \"").append(orderId).append("\": {\n");
                jsonBuilder.append("      \"till_arrival\": ").append(currentOrder.get_till_arrival()).append(",\n");
                jsonBuilder.append("      \"order_id\": ").append(currentOrder.getOrder_id()).append(",\n");
                jsonBuilder.append("      \"supplier_name\": \"").append(currentOrder.getSupplier_name())
                        .append("\",\n");
                jsonBuilder.append("      \"supplier_id\": ").append(currentOrder.getSupplier_id()).append(",\n");
                jsonBuilder.append("      \"address\": \"").append(currentOrder.getAddress()).append("\",\n");
                jsonBuilder.append("      \"contact_number\": \"").append(currentOrder.getContact_number())
                        .append("\",\n");
                jsonBuilder.append("      \"arrival_day\": ").append(currentOrder.get_arrival_day()).append(",\n");
                jsonBuilder.append("      \"item_list\": [\n");
                for (int i = 0; i < currentOrder.getItem_list().size(); i++) {
                    Record currentItem = currentOrder.getItem_list().get(i);
                    jsonBuilder.append("        {\n");
                    jsonBuilder.append("          \"order_id\": ").append(currentItem.getId()).append(",\n");
                    jsonBuilder.append("          \"supplier_id\": ").append(currentItem.getSupplier_id()).append(",\n");
                    jsonBuilder.append("          \"quantity\": ").append(currentItem.getQuantity()).append(",\n");
                    jsonBuilder.append("          \"ovl_price\": ").append(currentItem.getOvl_price()).append(",\n");
                    jsonBuilder.append("          \"discount\": ").append(currentItem.getDiscountPrice()).append(",\n");
                    jsonBuilder.append("          \"final_price\": ").append(currentItem.getFinal_price()).append("\n");
                    jsonBuilder.append("        }");
                    if (i < currentOrder.getItem_list().size() - 1) {
                        jsonBuilder.append(",");
                    }
                    jsonBuilder.append("\n");
                }
                jsonBuilder.append("      ],\n");
                jsonBuilder.append("      \"order_price\": ").append(currentOrder.getOrder_price()).append("\n");
                jsonBuilder.append("    }");
                if (!entry.equals(orders.entrySet().iterator().next())) {
                    jsonBuilder.append(",");
                }
                jsonBuilder.append("\n");
            }
            jsonBuilder.append("  }\n");
            jsonBuilder.append("}");
            json=jsonBuilder.toString();
        } catch (Exception ex) {
            res = new Response(ex);
        }
        //json = makeJson(res);
        return json;
    }

}
