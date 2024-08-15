package Backend.ServiceLayer.Suppliers;

import Backend.BusinessLayer.Controllers.Suppliers.ContractController;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.BusinessLayer.objects.Suppliers.Contract;
import Backend.BusinessLayer.objects.Suppliers.Order;
import Backend.BusinessLayer.objects.Suppliers.Pair;
import Backend.BusinessLayer.objects.Suppliers.Payment;
import Backend.BusinessLayer.objects.Suppliers.PeriodicOrder;
import Backend.BusinessLayer.objects.Suppliers.Record;
import Backend.ServiceLayer.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.Collection;
import java.util.Map;

public class ContractService {
    ContractController contractController;

    public ContractService() {
        contractController = ContractController.getInstance();
    }

    public String makeJson(Object toConvert) {
        Gson g = new Gson();
        String pre = g.toJson(toConvert);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(pre);
        return gson.toJson(jsonElement);
    }

    public String addContract(int supplier_id, Payment payment, Map<Product, Integer> item_list,
            Pair<Boolean, Boolean> supplier_config, int days, Map<Integer, Double> items_prices) {
        String json;
        Response res;
        try {
            String msg = contractController.addContract(supplier_id, payment, item_list, supplier_config, days,
                    items_prices);
            res = new Response(msg);
        } catch (Exception ex) {
            res = new Response(ex);
        }

        json = makeJson(res);
        return json;
    }

    public String removeContract(int supplier_id) {
        String json;
        Response res;
        try {
            String msg = contractController.removeContract(supplier_id);
            res = new Response(msg);
        } catch (Exception ex) {
            res = new Response(ex);
        }

        json = makeJson(res);
        return json;
    }

    public String get_contract(int supplier_id) {
        String json;
        Response res;
        try {
            Contract contract = contractController.get_contract(supplier_id);
            res = new Response<Contract>(contract);
        } catch (Exception ex) {
            res = new Response(ex);
        }

        json = makeJson(res);
        return json;
    }

    public String addEdit_item(int supplier_id, Product item, int quantity, double price_per_one) {
        String json;
        Response res;
        try {
            String msg = contractController.addEdit_item(supplier_id, item, quantity, price_per_one);
            res = new Response<String>(msg);
        } catch (Exception ex) {
            res = new Response(ex);
        }

        json = makeJson(res);
        return json;
    }

    public String remove_item(int supplier_id, Product item) {
        String json;
        Response res;
        try {
            String msg = contractController.remove_item(supplier_id, item);
            res = new Response<String>(msg);
        } catch (Exception ex) {
            res = new Response(ex);
        }

        json = makeJson(res);
        return json;
    }

    // if discount is ovl we put a random item_id .....
    public String add_discount(int supplier_id, int item_id, int quantity, int discount, boolean ovl, Character c) {
        String json;
        Response res;
        try {
            String msg = contractController.add_discount(supplier_id, item_id, quantity, discount, ovl, c);
            res = new Response<String>(msg);
        } catch (Exception ex) {
            res = new Response(ex);
        }

        json = makeJson(res);
        return json;
    }

    // if discount is ovl we put a random item_id .....
    public String remove_discount(int supplier_id, int item_id, int quantity, int discount, boolean ovl) {
        String json;
        Response res;
        try {
            String msg = contractController.remove_discount(supplier_id, item_id, quantity, ovl);
            res = new Response<String>(msg);
        } catch (Exception ex) {
            res = new Response(ex);
        }

        json = makeJson(res);
        return json;
    }

    public String change_contractPayment(int supp_id, Payment payment) {
        String json;
        Response res;
        try {
            String msg = contractController.change_contractPayment(supp_id, payment);
            res = new Response<String>(msg);
        } catch (Exception ex) {
            res = new Response(ex);
        }

        json = makeJson(res);
        return json;
    }

    public String listContracts() {
        String json = "";
        Response res;
        try {
            Collection<Contract> contracts = contractController.listContracts();
            res = new Response(contracts);
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{\n");
            jsonBuilder.append("  \"contracts\": [\n");
            for (Contract currentContract : contracts) {
                jsonBuilder.append("    {\n");
                jsonBuilder.append("      \"days_toReady\": ").append(currentContract.needed_days()).append(",\n");
                jsonBuilder.append("      \"contract_id\": ").append(currentContract.getContract_id()).append(",\n");
                jsonBuilder.append("      \"supplier_id\": ").append(currentContract.getSupplier_id()).append(",\n");
                jsonBuilder.append("      \"payment\": {\n");
                jsonBuilder.append("        \"method\": \"").append(currentContract.getPayment().getPaymentMethod())
                        .append("\",\n");
                jsonBuilder.append("        \"term\": \"").append(currentContract.getPayment().getNetTerm())
                        .append("\"\n");
                jsonBuilder.append("      },\n");
                jsonBuilder.append("      \"order_history\": [\n");
                for (Order currentOrder : currentContract.getOrder_history()) {
                    jsonBuilder.append("        {\n");
                    jsonBuilder.append("          \"till_arrival\": ").append(currentOrder.get_till_arrival())
                            .append(",\n");
                    jsonBuilder.append("          \"order_id\": ").append(currentOrder.getOrder_id()).append(",\n");
                    jsonBuilder.append("          \"supplier_name\": \"").append(currentOrder.getSupplier_name())
                            .append("\",\n");
                    jsonBuilder.append("          \"supplier_id\": ").append(currentOrder.getSupplier_id())
                            .append(",\n");
                    jsonBuilder.append("          \"address\": \"").append(currentOrder.getAddress()).append("\",\n");
                    jsonBuilder.append("          \"contact_number\": \"").append(currentOrder.getContact_number())
                            .append("\",\n");
                    jsonBuilder.append("          \"item_list\": [\n");
                    for (Record currentRecord : currentOrder.getItem_list()) {
                        jsonBuilder.append("            {\n");
                        jsonBuilder.append("              \"order_id\": ").append(currentRecord.getOrder_id())
                                .append(",\n");
                        jsonBuilder.append("              \"supplier_id\": ").append(currentRecord.getSupplier_id())
                                .append(",\n");
                        jsonBuilder.append("              \"quantity\": ").append(currentRecord.getQuantity())
                                .append(",\n");
                        jsonBuilder.append("              \"ovl_price\": ").append(currentRecord.getOvl_price())
                                .append(",\n");
                        jsonBuilder.append("              \"discount\": ").append(currentRecord.getDiscountPrice())
                                .append(",\n");
                        jsonBuilder.append("              \"final_price\": ").append(currentRecord.getFinal_price())
                                .append("\n");
                        jsonBuilder.append("            }");
                        if (!currentRecord
                                .equals(currentOrder.getItem_list().get(currentOrder.getItem_list().size() - 1))) {
                            jsonBuilder.append(",");
                        }
                        jsonBuilder.append("\n");
                    }
                    jsonBuilder.append("          ],\n");
                    jsonBuilder.append("          \"order_price\": ").append(currentOrder.getOrder_price()).append("\n");
                    jsonBuilder.append("        }");
                    if (!currentOrder.equals(
                            currentContract.getOrder_history().get(currentContract.getOrder_history().size() - 1))) {
                        jsonBuilder.append(",");
                    }
                    jsonBuilder.append("\n");
                }
                jsonBuilder.append("      ],\n");
                jsonBuilder.append("      \"order_per_history\": [\n");
                for (PeriodicOrder currentPeriodicOrder : currentContract.getPerOrder_history()) {
                    jsonBuilder.append("        {\n");
                    jsonBuilder.append("          \"till_arrival\": ").append(currentPeriodicOrder.get_till_arrival())
                            .append(",\n");
                    jsonBuilder.append("          \"order_id\": ").append(currentPeriodicOrder.getOrder_id())
                            .append(",\n");
                    jsonBuilder.append("          \"supplier_name\": \"").append(currentPeriodicOrder.getSupplier_name())
                            .append("\",\n");
                    jsonBuilder.append("          \"supplier_id\": ").append(currentPeriodicOrder.getSupplier_id())
                            .append(",\n");
                    jsonBuilder.append("          \"address\": \"").append(currentPeriodicOrder.getAddress())
                            .append("\",\n");
                    jsonBuilder.append("          \"contact_number\": \"")
                            .append(currentPeriodicOrder.getContact_number()).append("\",\n");
                    jsonBuilder.append("          \"item_list\": [\n");
                    for (Record currentRecord : currentPeriodicOrder.getItem_list()) {
                        jsonBuilder.append("            {\n");
                        jsonBuilder.append("              \"order_id\": ").append(currentRecord.getOrder_id())
                                .append(",\n");
                        jsonBuilder.append("              \"supplier_id\": ").append(currentRecord.getSupplier_id())
                                .append(",\n");
                        jsonBuilder.append("              \"quantity\": ").append(currentRecord.getQuantity())
                                .append(",\n");
                        jsonBuilder.append("              \"ovl_price\": ").append(currentRecord.getOvl_price())
                                .append(",\n");
                        jsonBuilder.append("              \"discount\": ").append(currentRecord.getDiscountPrice())
                                .append(",\n");
                        jsonBuilder.append("              \"final_price\": ").append(currentRecord.getFinal_price())
                                .append("\n");
                        jsonBuilder.append("            }");
                        if (!currentRecord.equals(currentPeriodicOrder.getItem_list()
                                .get(currentPeriodicOrder.getItem_list().size() - 1))) {
                            jsonBuilder.append(",");
                        }
                        jsonBuilder.append("\n");
                    }
                    jsonBuilder.append("          ],\n");
                    jsonBuilder.append("          \"order_price\": ").append(currentPeriodicOrder.getOrder_price())
                            .append(",\n");
                    jsonBuilder.append("          \"arrival_day\": ").append(currentPeriodicOrder.get_arrival_day())
                            .append("\n");
                    jsonBuilder.append("        }");
                    if (!currentPeriodicOrder.equals(currentContract.getPerOrder_history()
                            .get(currentContract.getPerOrder_history().size() - 1))) {
                        jsonBuilder.append(",");
                    }
                    jsonBuilder.append("\n");
                }
                jsonBuilder.append("      ],\n");
                jsonBuilder.append("      \"item_list\": {\n");
                for (Map.Entry<Product, Integer> entry : currentContract.getItem_list().entrySet()) {
                    Product product = entry.getKey();
                    Integer quantity = entry.getValue();
                    jsonBuilder.append("        \"product\": {\n");
                    jsonBuilder.append("          \"id\": ").append(product.getId()).append(",\n");
                    jsonBuilder.append("          \"name\": \"").append(product.getName()).append("\"\n");
                    jsonBuilder.append("        },\n");
                    jsonBuilder.append("        \"quantity\": ").append(quantity).append("\n");
                }
                jsonBuilder.append("      },\n");
                jsonBuilder.append("      \"supplier_config\": {\n");
                jsonBuilder.append("        \"first\": ").append(currentContract.getSupplierConfig().getKey())
                        .append(",\n");
                jsonBuilder.append("        \"second\": ").append(currentContract.getSupplierConfig().getValue())
                        .append("\n");
                jsonBuilder.append("      }\n");
                jsonBuilder.append("    }");
                if (!currentContract.equals(contracts.toArray()[contracts.size() - 1])) {
                    jsonBuilder.append(",");
                }
                jsonBuilder.append("\n");
            }
            jsonBuilder.append("  ]\n");
            jsonBuilder.append("}");

            json = jsonBuilder.toString();
        } catch (Exception ex) {
            res = new Response(ex);
        }

        // json = makeJson(res);
        return json;
    }
}
