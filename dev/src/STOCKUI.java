import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.gson.Gson;

import com.google.gson.Gson;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.BusinessLayer.objects.Stock.Category;
import Backend.BusinessLayer.objects.Stock.Item;
import Backend.BusinessLayer.objects.Stock.Product;
import Backend.BusinessLayer.objects.Suppliers.Contact;
import Backend.BusinessLayer.objects.Suppliers.Contract;
import Backend.BusinessLayer.objects.Suppliers.Order;
import Backend.BusinessLayer.objects.Suppliers.Pair;
import Backend.BusinessLayer.objects.Suppliers.Record;

import Backend.BusinessLayer.objects.Suppliers.Payment;
import Backend.BusinessLayer.objects.Suppliers.PeriodicOrder;
import Backend.BusinessLayer.objects.Suppliers.SupplierCard;
import Backend.ServiceLayer.Response;
import Backend.ServiceLayer.ServiceFactory;

public class STOCKUI {

    public static boolean handle_response(String json, boolean printObject) {
        Gson gson = new Gson();
        Response res = gson.fromJson(json, Response.class);
        if (res.isError()) {
            System.out.println("\n" + res.getErrorMessage() + "\n");
            return true;
        } else {
            if (printObject) {
                System.out.println("\n" + res.getValue() + "\n");
                return false;
            } else {
                return false;
            }
        }
    }

    static ServiceFactory store = Main.store;

    public void start() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int menu;

        try {
            do {
                System.out.println("Please choose an option:");
                System.out.println("1. Products");
                System.out.println("2. Categories");
                System.out.println("3. Reports");
                System.out.println("4. Discounts");
                System.out.println("5. Items");
                System.out.println("0. Exit");

                menu = Integer.parseInt(reader.readLine());

                switch (menu) {
                    case 1:
                        Products();
                        break;

                    case 2:
                        Categories();
                        break;
                    case 3:
                        Reports();
                        break;

                    case 4:
                        discounts();
                        break;
                    case 5:
                        Items();
                        break;

                    case 0:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }
            } while (menu != 0);
        } catch (NumberFormatException | IOException e) {
            System.out.println("Invalid input. Please try again.");
        } //finally {
        //     try {
        //         reader.close();
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // }
    }

    public static void Categories() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int action = -1;
        do {
            System.out.println("Please choose what to do:");
            System.out.println("1.add Category");
            System.out.println("2.delete Category");
            System.out.println("3.add SubCategory");
            System.out.println("4.add FatherCategory");
            System.out.println("5.change Category Name");

            System.out.println("0. Exit");

            try {
                action = Integer.parseInt(reader.readLine());
                switch (action) {
                    case 1:
                        System.out.println("Pick a name for the category:");
                        String name = (reader.readLine());
                        System.out.println("Pick a Father for the category:");
                        System.out.println("type 0 if no father:");

                        String faInteger = (reader.readLine());
                        handle_response(store.addCategory(name, faInteger),true);
                        break;
                    case 2:
                        System.out.println("Pick a category to delete:");
                        String categoryID = (reader.readLine());
                        handle_response(store.deleteCategory(categoryID),true);
                        break;
                    case 3:
                        System.out.println("Pick a category to add a subcategory to:");
                        categoryID = (reader.readLine());
                        System.out.println("Pick a subcategory :");
                        String subCategory = (reader.readLine());

                        handle_response(store.addSubCategory(categoryID, subCategory),true);
                        break;

                    case 4:
                        System.out.println("Pick a category to to add father to:");
                        categoryID = (reader.readLine());
                        System.out.println("Pick a Father for the category:");
                        String fatherCategory = (reader.readLine());
                        handle_response(store.addFatherCategory(categoryID, fatherCategory),true);
                        break;
                    case 5:
                        System.out.println("pick a category :");
                        String category = (reader.readLine());
                        System.out.println("Pick a name for the category:");
                        name = (reader.readLine());
                        handle_response(store.changeCategoryName(category, name),true);
                        break;

                    case 0:
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }

            } catch (NumberFormatException | IOException e) {
                System.out.println("Invalid input. Please try again.");
            }

        } while (action != 0);

    }

    public static void discounts() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int action = -1;
        do {
            System.out.println("Please choose what to do:");
            System.out.println("1.add Product Discount");
            System.out.println("2.add Category Discount");
            System.out.println("0. Exit");

            try {
                action = Integer.parseInt(reader.readLine());
                switch (action) {
                    case 1:
                        LocalDate start = LocalDate.now();
                        LocalDate finish = LocalDate.now();

                        System.out.println("Pick a product for the discount:");
                        int productID = Integer.parseInt(reader.readLine());
                        System.out.println("add dicount amount:");
                        Double discount = Double.parseDouble(reader.readLine());
                        System.out.println("When does the discount start yyyy-MM-DD:");
                        try {
                            start = LocalDate.parse(reader.readLine());
                            System.out.println(start.toString());

                        } catch (Exception e) {
                            System.out.println("invalid date:");
                            break;
                        }
                        System.out.println("When does the discount finish yyyy-MM-DD:");

                        try {
                            finish = LocalDate.parse(reader.readLine());

                        } catch (Exception e) {
                            System.out.println("invalid date:");
                            break;
                        }

                        store.buildProductDiscount(productID, discount, start, finish);
                        break;
                    case 2:
                        System.out.println("Pick a category for the discount:");
                        String categoryID = (reader.readLine());
                        System.out.println("add dicount amount:");
                        discount = Double.parseDouble(reader.readLine());
                        System.out.println("When does the discount start yyyy-MM-DD:");
                        try {
                            start = LocalDate.parse(reader.readLine());

                        } catch (Exception e) {
                            System.out.println("invalid date:");
                            break;
                        }
                        System.out.println("When does the discount finish yyyy-MM-DD:");

                        try {
                            finish = LocalDate.parse(reader.readLine());

                        } catch (Exception e) {
                            System.out.println("invalid date:");
                            break;
                        }
                        store.buildCategoryDiscount(categoryID, discount, start, finish);
                        break;

                    case 0:
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }

            } catch (NumberFormatException | IOException e) {
                System.out.println("Invalid input. Please try again.");
            }

        } while (action != 0);

    }

    public static void Products() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int action = -1;
        do {
            System.out.println("Please choose what to do:");
            System.out.println("1.add Product");
            System.out.println("2.change Product Price");
            System.out.println("3.change Product Name");
            System.out.println("4.change Product Min Amount");
            // System.out.println("5.move Product To Category");

            System.out.println("0. Exit");

            try {
                action = Integer.parseInt(reader.readLine());
                switch (action) {
                    case 1:
                        System.out.print("Enter the product barcode: ");
                        int barcode = Integer.parseInt(reader.readLine());
                        System.out.print("Enter product name: ");
                        String name = (reader.readLine());

                        System.out.print("Enter minimum stock amount: ");
                        int amount = Integer.parseInt(reader.readLine());

                        System.out.print("Enter selling price: ");
                        double sellingPrice = Double.parseDouble(reader.readLine());

                        System.out.print("Enter manufacturing price: ");
                        double manufacturePrice = Double.parseDouble(reader.readLine());

                        System.out.print("Enter shelf number: ");
                        int shelfNumber = Integer.parseInt(reader.readLine());

                        System.out.print("Enter manufacturer name: ");
                        String manufacturer = (reader.readLine());

                        System.out.print("Enter category: ");
                        System.out.println("enter the sub-sub-category");
                        String category = (reader.readLine());
                        store.addProductToStore(barcode, name, amount, sellingPrice, manufacturePrice, shelfNumber,
                                manufacturer,
                                category);
                        break;
                    case 2:
                        System.out.print("Enter a product : ");
                        int ProductID = Integer.parseInt(reader.readLine());

                        System.out.print("Enter new  selling price: ");
                        double newPrice = Double.parseDouble(reader.readLine());
                        store.changeProductPrice(ProductID, newPrice);
                        break;
                    case 3:
                        System.out.print("Enter productID: ");
                        ProductID = Integer.parseInt(reader.readLine());
                        System.out.print("Enter product name: ");
                        String newname = (reader.readLine());
                        store.changeProductName(ProductID, newname);
                        break;

                    case 4:
                        System.out.print("enter ProductID: ");
                        ProductID = Integer.parseInt(reader.readLine());

                        System.out.print("Enter new min: ");
                        int newmin = Integer.parseInt(reader.readLine());
                        store.changeProductMinAmount(ProductID, newmin);
                        break;
                    // case 5:
                    // System.out.print("enter ProductID: ");
                    // ProductID = Integer.parseInt(reader.readLine());

                    // System.out.print("enter a categoryID: ");
                    // String newCategoryID = (reader.readLine());
                    // store.moveProductToCategory(ProductID, newCategoryID);
                    // break;

                    case 0:
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }

            } catch (NumberFormatException | IOException e) {
                System.out.println("Invalid input. Please try again.");
            }

        } while (action != 0);

    }

    public static void Reports() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int action = -1;
        do {
            System.out.println("Please choose what to do:");
            System.out.println("1.DefectiveItemsReport");
            System.out.println("2.Stock");
            System.out.println("3.DamagedItemReport");
            System.out.println("4.ExpiredItemReport");
            System.out.println("5.MinReportt");
            System.out.println("6.return Information for allProducts");
            System.out.println("7.return Information for one Product");
            System.out.println("8.return all category discounts");
            System.out.println("9.return all product discounts");
            System.out.println("10.return all categories");

            System.out.println("0. Exit");

            try {
                action = Integer.parseInt(reader.readLine());
                switch (action) {
                    case 1:
                        store.getDefectiveItemsByStore();
                        break;
                    case 2:
                        String categoryname = " 1";
                        List<Category> categories = new LinkedList<>();
                        System.out.println(" Type exit if you wish to stop");

                        while (!categoryname.equals("exit")) {
                            System.out.println("enter a category name");
                            categoryname = (reader.readLine());
                            try {
                                if (categoryname.equals("exit")) {
                                } else
                                    categories.add(
                                            store.getCategoryService().categoryController.returnCategory(categoryname));
                            } catch (Exception e) {
                                System.out.println(" no such category ");

                            }
                        }
                        store.getProductsAndAmount(categories);
                        break;

                    case 3:
                        store.getDamagedItemReportsByStore();
                        break;

                    case 4:
                        store.getExpiredItemReportsByStore();
                        break;

                    case 5:
                        store.getMinReport();
                        break;
                    case 6:
                        store.returnInformationProducts();
                        break;
                    case 7:
                        System.out.println("Pick a product to get its info:");
                        int productID = Integer.parseInt(reader.readLine());
                        store.returnInformationProduct(productID);
                        break;
                    case 8:
                        store.getactiveCategoriesDiscounts();
                        break;
                    case 9:
                        store.getactiveProductsDiscounts();
                        break;
                    case 10:
                        store.returnCategories();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }

            } catch (NumberFormatException | IOException e) {
                System.out.println("Invalid input. Please try again.");
            }

        } while (action != 0);

    }

    public static void Items() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int action = -1;
        do {
            System.out.println("Please choose what to do:");
            System.out.println("1. Add Item");
            System.out.println("2. Add Defective Item from Store");
            System.out.println("3. Add Defective Item from Storage");
            System.out.println("4. remove item from store ");
            System.out.println("5. remove item from storage ");
            System.out.println("0. Exit");

            try {
                action = Integer.parseInt(reader.readLine());
                switch (action) {
                    case 1:
                        System.out.println("Please choose where to add the item:");
                        System.out.println("1. Storage");
                        System.out.println("2. Store");
                        System.out.println("0. Exit");

                        int location = Integer.parseInt(reader.readLine());
                        switch (location) {
                            case 1:
                                System.out.println("Pick a product to add items to:");
                                int productID = Integer.parseInt(reader.readLine());
                                LocalDate expdate = LocalDate.now();

                                System.out.println("When does the item expire yyyy-MM-DD:");
                                try {
                                    expdate = LocalDate.parse(reader.readLine());
                                } catch (Exception e) {
                                    System.out.println("Not a valid date");
                                    break;
                                }

                                System.out.println("Pick how many items to add :");
                                int amount = Integer.parseInt(reader.readLine());
                                store.addStorageItem(productID, expdate, amount);
                                break;
                            case 2:
                                System.out.println("Pick a product to add items to:");
                                productID = Integer.parseInt(reader.readLine());
                                expdate = LocalDate.now();

                                System.out.println("When does the item expire yyyy-MM-DD:");
                                try {
                                    expdate = LocalDate.parse(reader.readLine());
                                } catch (Exception e) {
                                    System.out.println("Not a valid date");
                                    break;
                                }

                                System.out.println("Pick how many items to add :");
                                amount = Integer.parseInt(reader.readLine());
                                store.addStoreItem(expdate, productID, amount);
                                break;
                            case 0:
                                break;
                            default:
                                System.out.println("Invalid choice. Please try again.");
                                break;
                        }
                        break;
                    case 2:
                        System.out.print("Enter ProductID: ");
                        int storeProductID = Integer.parseInt(reader.readLine());
                        System.out.print("Enter Expiration Date (yyyy-MM-DD): ");
                        try {
                            LocalDate storeExpDate = LocalDate.parse(reader.readLine());
                            System.out.print("Enter amount: ");
                            int amount = Integer.parseInt(reader.readLine());
                            store.addDamfromstore(storeExpDate, storeProductID, amount);
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please try again.");
                        }
                        break;
                    case 3:
                        System.out.print("Enter ProductID: ");
                        int storageProductID = Integer.parseInt(reader.readLine());
                        System.out.print("Enter Expiration Date (yyyy-MM-DD): ");
                        try {
                            LocalDate storageExpDate = LocalDate.parse(reader.readLine());
                            System.out.print("Enter amount: ");
                            int amount = Integer.parseInt(reader.readLine());
                            store.addDamfromstorage(storageExpDate, storageProductID, amount);
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please try again.");
                        }
                        break;
                    case 4:
                        System.out.print("Enter ProductID: ");
                        storeProductID = Integer.parseInt(reader.readLine());
                        System.out.print("Enter Expiration Date (yyyy-MM-DD): ");
                        try {
                            LocalDate storeExpDate = LocalDate.parse(reader.readLine());
                            System.out.print("Enter amount: ");
                            int amount = Integer.parseInt(reader.readLine());
                            store.removeStoreItem(storeProductID, storeExpDate, amount);
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please try again.");
                        }
                        break;
                    case 5:
                        System.out.print("Enter ProductID: ");
                        storageProductID = Integer.parseInt(reader.readLine());
                        System.out.print("Enter Expiration Date (yyyy-MM-DD): ");
                        try {
                            LocalDate storageExpDate = LocalDate.parse(reader.readLine());
                            System.out.print("Enter amount: ");
                            int amount = Integer.parseInt(reader.readLine());
                            store.removeStorageItem(storageProductID, storageExpDate, amount);
                        } catch (Exception e) {
                            System.out.println("Invalid input. Please try again.");
                        }
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }

            } catch (NumberFormatException | IOException e) {
                System.out.println("Invalid input. Please try again.");
            }

        } while (action != 0);

    }
}