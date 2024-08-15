import Backend.BusinessLayer.Controllers.Stock.Categorycontroller;
import Backend.BusinessLayer.Controllers.Stock.productscontroller;
import Backend.BusinessLayer.Controllers.Suppliers.OrdersController;
import Backend.BusinessLayer.Controllers.Suppliers.SupplierController;
import Backend.BusinessLayer.objects.Suppliers.Contact;
import Backend.BusinessLayer.objects.Suppliers.Contract;
import Backend.BusinessLayer.objects.Suppliers.Order;
import Backend.BusinessLayer.objects.Suppliers.Pair;
import Backend.BusinessLayer.objects.Suppliers.Payment;
import Backend.BusinessLayer.objects.Suppliers.Record;
import Backend.BusinessLayer.objects.Suppliers.SupplierCard;
import Backend.ServiceLayer.ServiceFactory;
import Backend.ServiceLayer.Response;
import Backend.BusinessLayer.objects.Stock.Category;
import Backend.BusinessLayer.objects.Stock.Item;
import Backend.BusinessLayer.objects.Stock.Product;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class CLUI {

    private Scanner scanner;
    private ServiceFactory serviceFactory;
    private  Gson gson;
    static productscontroller product = productscontroller.getInstance();

    public Map<Integer,Product> items_map;

    public CLUI(ServiceFactory store) {
        this.scanner = new Scanner(System.in);
        serviceFactory = store;
        gson = new Gson();

        System.out.println("1/2");
        int secret=scanner.nextInt();scanner.nextLine();
        if(secret==1){
        //----------------------------------------------------------------------------------------------------//
        try {
            productscontroller.getInstance().buildProduct(10, "bread", 3, 5, 4, 1, "man",new Category("As", null));
            productscontroller.getInstance().buildProduct(11, "XL", 2, 5, 4, 1, "man",new Category("As", null));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        store.addStoreItem(LocalDate.of(2024, 11, 24), 10, 6);
        store.addStoreItem(LocalDate.of(2024, 11, 28), 11, 4);
        

        Payment payment = new Payment(Backend.BusinessLayer.objects.Suppliers.Payment.PaymentMethod.BIT,
                Backend.BusinessLayer.objects.Suppliers.Payment.NetTerm.NET30);


        Map<Product, Integer> map = new HashMap<>();
        map.put(productscontroller.getInstance().getProduct(10), 5);
        map.put(productscontroller.getInstance().getProduct(11), 2);

        Pair<Boolean, Boolean> supplier_config = new Pair<>(true, true);

        Map<Integer,Double> items_prices = new HashMap<>(); //prod_id ==> price
        Map<Integer,Map<Integer, Pair<Character,Integer>>> discounts = new HashMap<>(); //
        Map<Integer,Pair<Character,Integer>> ovl_discount = new HashMap<>();//
        items_prices.put(10, new Double(5));
        items_prices.put(11, new Double(6));
        Map<Integer,Double> items_prices2 = new HashMap<>();
        items_prices2.put(10, new Double(3));
        items_prices2.put(11, new Double(3));



        store.addSupplier("comp", "123123", 0, "bank account", new LinkedList<>());
        store.addContract(0, payment, map, supplier_config,1,items_prices);
        store.addSupplier("compppppppppp", "123123123", 1, "vbbadg", new LinkedList<>());
        store.addContract(1, payment, map, supplier_config, 3, items_prices2);

        }
        //-------------------------------------------------------------------------------------------------------/
       else{
        items_map = new HashMap<>();
        Product product1 = new Product(1, "milk", -1, 5, 2, 1, "man");
        Product product2 = new Product(2, "Bamba", -1, 5, 4, 1, "man");
        Product product3 = new Product(3, "Fanta", -1, 5, 4, 1, "man");
        Product product4 = new Product(4, "Maltesers", -1, 5, 4, 1, "man");
        Product product5 = new Product(5, "Tuna", -1, 5, 4, 1, "man");
        items_map.put(1, product1);
        items_map.put(2, product2);
        items_map.put(3, product3);
        items_map.put(4, product4);
        items_map.put(5, product5);
        try {
            // store.addCategory("milk", "0");

            // //Category c=new Category("milk", null);
            List<Product> lc=new ArrayList<>();
            lc.add(product1);
            lc.add(product2);
            lc.add(product3);
            lc.add(product4);
            lc.add(product5);
            Categorycontroller.getInstance().buildCategory("milk", lc, null, "0");
            productscontroller.getInstance().buildProduct(1, "milk", -1, 5, 4, 1, "milk", Categorycontroller.getInstance().getCategories().get("milk"));
            productscontroller.getInstance().buildProduct(2, "Bamba", -1, 5, 4, 1, "milk", Categorycontroller.getInstance().getCategories().get("milk"));
            productscontroller.getInstance().buildProduct(3, "Fanta", -1, 5, 4, 1, "milk", Categorycontroller.getInstance().getCategories().get("milk"));
            productscontroller.getInstance().buildProduct(4, "Maltesers", -1, 5, 4, 1, "milk", Categorycontroller.getInstance().getCategories().get("milk"));
            productscontroller.getInstance().buildProduct(5, "Tuna", -1, 5, 4, 1, "milk", Categorycontroller.getInstance().getCategories().get("milk"));
        } catch (SQLException e) {
        }
    }

    }

    public static String makeJson(Object toConvert) {
        Gson g = new Gson();
        String pre = g.toJson(toConvert);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement jsonElement = JsonParser.parseString(pre);
        return gson.toJson(jsonElement);
    }

    public   boolean handle_response(String json, boolean printObject) {
        Response res = gson.fromJson(json, Response.class);
        if (res.isError()) {
            System.out.println("\n" + res.getErrorMessage()+ "\n");
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

    public void start() {
        boolean flag=true;
        while (flag) {
            System.out.println("Please choose an option:");
            System.out.println("1. Manage Suppliers");
            System.out.println("2. Manage Contracts");
            System.out.println("3. Manage Orders");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    manageSuppliers();
                    break;
                case 2:
                    manageContracts();
                    break;
                case 3:
                    manageOrders();
                    break;
                case 4:
                    flag=false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }


    private void manageSuppliers() {
        while (true) {
            System.out.println("Please choose a supplier option:");
            System.out.println("1. Add Supplier");
            System.out.println("2. Edit Supplier");
            System.out.println("3. Remove Supplier");
            System.out.println("4. List Suppliers");
            System.out.println("5. Back");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addSupplier();
                    break;
                case 2:
                    editSupplier();
                    break;
                case 3:
                    removeSupplier();
                    break;
                case 4:
                    listSuppliers();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void manageContracts() {
        while (true) {
            System.out.println("Please choose a contract option:");
            System.out.println("1. Add Contract");
            System.out.println("2. Edit Contract");
            System.out.println("3. Remove Contract");
            System.out.println("4. List Contracts");
            System.out.println("5. Back");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addContract();
                    break;
                case 2:
                    editContract();
                    break;
                case 3:
                    removeContract();
                    break;
                case 4:
                    listContracts();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
            }
        }


    private void manageOrders() {
        while (true) {
            System.out.println("Please choose an option:");
            System.out.println("1. Place Periodic Order");
            System.out.println("2. List Shortage Orders");
            System.out.println("3. List Periodic Orders");
            System.out.println("4. Advance Day");
            System.out.println("5. Cancel Order");
            System.out.println("6. Update Periodic date");
            System.out.println("7. change product in a Periodic order");
            System.out.println("8. add an item to a Periodic order");
            System.out.println("0. Back");

            int choice = scanner.nextInt();scanner.nextLine();

            switch (choice) {
                case 1:
                    Prepare_Order();
                    break;
                case 2:
                    listOrders();
                    break;
                case 3:
                    listPerOrders();
                    break;
                case 4:
                    advanceDay();
                    return;
                case 5:
                    Cancel_Order();
                    return;
                case 6:
                    updatePeriodic_date();
                    return; 
                case 7:
                    add_toPeriodic_product();
                    return;
                case 8:
                    add_to_Periodic_items();
                    return;   
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
            }
        }



    public void addSupplier() {
        scanner.nextLine();
        System.out.print("Enter the supplier company name: ");
        String company_name = scanner.nextLine();


        System.out.print("Enter the supplier phone number: ");
        String phone_number = scanner.nextLine();

        System.out.print("Enter the supplier company ID: ");
        int company_id = scanner.nextInt();
        scanner.nextLine(); // consume the newline character left by nextInt()

        System.out.print("Enter the supplier bank account number: ");
        String bank_account = scanner.nextLine();

        System.out.print("Enter the number of contacts for this supplier: ");
        int num_contacts = scanner.nextInt();
        scanner.nextLine(); // consume the newline character left by nextInt()

        LinkedList<Contact> contactList = new LinkedList<>();
        for (int i = 1; i <= num_contacts; i++) {
            System.out.println("Enter contact " + i + " details:");
            System.out.print("Name: ");
            String name = scanner.nextLine();

            System.out.print("Phone number: ");
            String contact_phone_number = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Company id: ");
            int contact_company_number = scanner.nextInt();
            scanner.nextLine(); // consume the newline character left by nextInt()

            System.out.print("Fax: ");
            String fax = scanner.nextLine();


            Contact contact = new Contact(name, contact_phone_number, email, contact_company_number, fax);
            contactList.add(contact);
        }

        handle_response(serviceFactory.addSupplier(company_name,phone_number,company_id,bank_account,contactList),true);

    }

    public void listSuppliers() {
        String json = serviceFactory.listSuppliers();
        System.out.println(json);
    }

    public void removeSupplier() {
        System.out.println("Please enter supplier id: ");
        int supplier_id = scanner.nextInt();
        String json = serviceFactory.removeSupplier(supplier_id);
        Response res = gson.fromJson(json, Response.class);
        if (res.isError()) {
            System.out.println(res.getErrorMessage());
        } else {
            System.out.println(res.getValue());
        }
    }


    public void editSupplier() {
        System.out.print("Enter the ID of the supplier you want to edit: ");
        int id = scanner.nextInt();

        // String json = serviceFactory.getSupplier(id);
        // if (handle_response(json, false)) {
        //     return;
        // }

        System.out.println("Enter the attribute you want to edit:");
        System.out.println("1. Name");
        System.out.println("2. Phone number");
        System.out.println("3. BankAccount");
        System.out.println("4. Back");
        int choice = scanner.nextInt();

        String newValue = "";
        switch (choice) {
            case 1:
                System.out.print("Enter the new name: ");
                newValue = scanner.next();
                handle_response(serviceFactory.editSupplier_name(id, newValue), true);
                break;
            case 2:
                System.out.print("Enter the new phone number: ");
                newValue = scanner.next();
                handle_response(serviceFactory.ChangePhoneNumber(id, newValue), true);
                break;
            case 3:
                System.out.print("Enter the new BankAccount: ");
                newValue = scanner.next();
                handle_response(serviceFactory.changeBankAccount(id, newValue), true);
                break;
            case 4:
                break;
            default:
                System.out.println("Invalid choice");
                return;
        }

    }


    private void addContract() {
        Map<Product,Integer> items = new HashMap<>();
        Map<Integer,Double> items_prices=new HashMap<>();
        System.out.println("Enter contract details: ");
        System.out.print("Enter supplier ID of the desired supplier: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Select the payment method (CASH/BIT/CHECK/CRYPTO/CREDITCARD/BankTransfer): ");
        Payment.PaymentMethod payment_method = Payment.PaymentMethod.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Select the net term (Net30/Net45/Net60/Net90): ");
        Payment.NetTerm net_term = Payment.NetTerm.valueOf(scanner.nextLine().toUpperCase());
        Payment payment = new Payment(payment_method,net_term);
        //fill items
        int items_quantity = 0 ;
        while (true) {
            System.out.println("Enter how many items are included in the contract (MAX 5)");
             items_quantity= scanner.nextInt();
            if(items_quantity > 5){continue;}else {break;}
        }



        for (int i = 1; i<=items_quantity; i++) {
            System.out.println("Choose the items included in the contract: ");
            System.out.println("1. Milk");
            System.out.println("2. Bamba");
            System.out.println("3. Fanta");
            System.out.println("4. Maltesers");
            System.out.println("5. Tuna");
            int choice = scanner.nextInt();
            System.out.println("Enter how much can be supplied in the contract");
            switch (choice) {
                case 1:
                    int q1=scanner.nextInt();
                    System.out.println("Enter the price for the item :");
                    Double p1=scanner.nextDouble();
                    items_prices.put(items_map.get(choice).getId(), p1);
                    items.put(items_map.get(choice), q1);
                    break;
                case 2:
                    int q2=scanner.nextInt();
                    System.out.println("Enter the price for the item :");
                    Double p2=scanner.nextDouble();
                    items_prices.put(items_map.get(choice).getId(), p2);
                    items.put(items_map.get(choice), q2);
                    break;
                case 3:
                    int q3=scanner.nextInt();
                    System.out.println("Enter the price for the item :");
                    Double p3=scanner.nextDouble();
                    items_prices.put(items_map.get(choice).getId(), p3);
                    items.put(items_map.get(choice), q3);
                    break;
                case 4:
                    int q4=scanner.nextInt();
                    System.out.println("Enter the price for the item :");
                    Double p4=scanner.nextDouble();
                    items_prices.put(items_map.get(choice).getId(), p4);
                    items.put(items_map.get(choice), q4);
                    break;
                case 5:
                    int q5=scanner.nextInt();
                    System.out.println("Enter the price for the item :");
                    Double p5=scanner.nextDouble();
                    items_prices.put(items_map.get(choice).getId(), p5);
                    items.put(items_map.get(choice), q5);
                    break;
                default:
                    System.out.println("Invalid choice");
                    return;
            }

        }
        boolean constant = false;
        boolean brings = false;
        System.out.println("Enter whether the supplier of this contract has fixed days he comes in: ");
        System.out.println("1. true");
        System.out.println("2. false");
        int cons = scanner.nextInt();
        constant = (cons == 1) ? true : false;
        System.out.println("Enter whether the supplier of this contract is responsible of delivering the orders or now: ");
        System.out.println("1. true");
        System.out.println("2. false");
        cons = scanner.nextInt();
        brings = (cons==1)? true : false;
        Pair<Boolean,Boolean> pair = new Pair<>(constant,brings);
        System.out.println("Enter the days it takes the supplier of this contract to prepare an order and order it  once receiving one");
        int days = scanner.nextInt();
        handle_response(serviceFactory.addContract(id,payment,items,pair,days,items_prices),true);
    }

    public void removeContract() {
        while (true) {
            System.out.println("Enter the supplier id to remove contract (enter -1 to exit): ");
            int id = scanner.nextInt();
            if(id == -1 || !handle_response(serviceFactory.removeContract(id), true)) break;
            System.out.println("\n" +"Enter a valid id..");
        }
    }


    public void editContract() {
        System.out.println("Enter supplier ID to edit his corresponding contract: ");
        int suppid = scanner.nextInt();
        scanner.nextLine();
        // if(handle_response(serviceFactory.get_contract(suppid),false)){
        //     return;
        // }
        System.out.println("What do you want to edit?");
        System.out.println("1. Payment conditions");
        System.out.println("2. Items");
        System.out.println("3. Add overall discount");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter new payment method (CASH/BIT/CHECK/CRYPTO/CREDITCARD/BankTransfer): ");
                Payment.PaymentMethod paymentMethod = Payment.PaymentMethod.valueOf(scanner.nextLine().toUpperCase());
                System.out.print("Enter new net term (Net30/Net45/Net60/Net90): ");
                Payment.NetTerm netTerm = Payment.NetTerm.valueOf(scanner.nextLine().toUpperCase());
                Payment payment = new Payment(paymentMethod,netTerm);
                handle_response(serviceFactory.change_contractPayment(suppid,payment),true);
                break;
            case 2:
                System.out.println("Choose the item to add discount to it: ");
                System.out.println("1. Milk");
                System.out.println("2. Bamba");
                System.out.println("3. Fanta");
                System.out.println("4. Maltesers");
                System.out.println("5. Tuna");
                int itemChoice = scanner.nextInt();scanner.nextLine();
                System.out.println("Enter the kind of the discount (Percentage/Value):");
                System.out.println("1. Percentage");
                System.out.println("2. Value");
                int choice3 = scanner.nextInt();scanner.nextLine();
                System.out.println("Enter quantity to add discount to it");
                int choice_quant = scanner.nextInt();scanner.nextLine();
                System.out.println("Enter the value of the discount");
                int dis = scanner.nextInt();scanner.nextLine();
                Character c = (choice3 == 1) ? 'p' : 'v';
                handle_response(serviceFactory.add_discount(suppid,items_map.get(itemChoice).getId(),choice_quant,dis,false,c),true);
                break;
            case 3:
                System.out.println("Enter quantity to add discount to it");
                int quant = scanner.nextInt();scanner.nextLine();
                System.out.println("Enter the value of the discount");
                int discount = scanner.nextInt();scanner.nextLine();
                System.out.println("Enter the kind of the discount (Percentage/Value):");
                System.out.println("1. Percentage");
                System.out.println("2. Value");
                int choice4 = scanner.nextInt();scanner.nextLine();
                Character c2 = (choice4 == 1) ? 'p' : 'v';
                handle_response(serviceFactory.add_discount(suppid,-1,quant,discount,true,c2),true);
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    public void listContracts() {
        String json = serviceFactory.listContracts();
        System.out.println(json);
    }



    public void Cancel_Order() {
        System.out.println("Enter Order id to cancel: ");
        int id = scanner.nextInt();scanner.nextLine();
        handle_response(serviceFactory.cancel_order(id),true);
    }

    public void Prepare_Order() {
        List<Record> items=new LinkedList<>();
        System.out.println("Enter Supplier id:");
        int supplier_id = scanner.nextInt();scanner.nextLine();
        System.out.println("Enter Order id:");
        int order_id = scanner.nextInt();scanner.nextLine();
        System.out.println("Enter address:");
        String address = scanner.nextLine();
        System.out.println("Enter Contact number:");
        String contact_number = scanner.nextLine();
        System.out.println("Enter the day of the arrival:");
        int days = scanner.nextInt();scanner.nextLine();
        Contract cont=OrdersController.getInstance().suppliers_contracts.get(supplier_id);
        System.out.println("Enter how many items are included in the order (Max 5)");
        int quant = scanner.nextInt();scanner.nextLine();
        for (int i = 1; i<=quant; i++) {
            System.out.println("Choose the items included in the Order: ");
            System.out.println("1. Milk");
            System.out.println("2. Bamba");
            System.out.println("3. Fanta");
            System.out.println("4. Maltesers");
            System.out.println("5. Tuna");
            int choice = scanner.nextInt();scanner.nextLine();
            Product curr = items_map.get(choice);
            System.out.println("Enter quantity to order:");
            int q = scanner.nextInt();scanner.nextLine();
            int quantity_can=cont.quant(curr.getId(), q);
            double discount=cont.get_margin_price(curr.getId(),quantity_can);
            double final_price=cont.getItemPrice(curr.getId(), quantity_can);
            Record toAdd=new Record(curr, supplier_id,order_id, quantity_can, discount, final_price);
            items.add(toAdd);
        }
        String supp_name=SupplierController.getInstance().suppliers.get(supplier_id).getSupplier_name();
        handle_response(serviceFactory.add_PeriodicOrder(supp_name, supplier_id, address, contact_number, items, days),true);
    }

    public void listOrders() {
        String json = serviceFactory.listOrders();
        System.out.println(json);
    }

    public void listPerOrders(){
        String json=serviceFactory.listPerOrder();
        System.out.println(json);
    }

    public void advanceDay() {
        handle_response(serviceFactory.incrementDay(), false);
    }

    public void updatePeriodic_date(){
        System.out.println("Enter Order id:");
        int order_id = scanner.nextInt();scanner.nextLine();
        System.out.println("Enter day:");
        int address = scanner.nextInt();
        handle_response(serviceFactory.updatePeriodic_date(order_id, address),true);
    }

    public void add_toPeriodic_product(){
        System.out.println("Enter Order id:");
        int order_id = scanner.nextInt();scanner.nextLine();
        System.out.println("Enter product id:");
        int prod_id = scanner.nextInt();scanner.nextLine();
        System.out.println("Enter Quantity:");
        int q = scanner.nextInt();scanner.nextLine();
        handle_response(serviceFactory.add_toPeriodic_product(prod_id, order_id, q), true);
    }

    public void add_to_Periodic_items(){
        System.out.println("Enter Order id:");
        int order_id = scanner.nextInt();scanner.nextLine();
        System.out.println("Enter Supplier id:");
        int supplier_id = scanner.nextInt();scanner.nextLine();
        Contract cont=OrdersController.getInstance().suppliers_contracts.get(supplier_id);
        System.out.println("Choose the items to add to the order: ");
        System.out.println("1. Milk");
        System.out.println("2. Bamba");
        System.out.println("3. Fanta");
        System.out.println("4. Maltesers");
        System.out.println("5. Tuna");
        int choice = scanner.nextInt();scanner.nextLine();
        Product curr = items_map.get(choice);
        System.out.println("Enter quantity to order:");
        int q = scanner.nextInt();scanner.nextLine();
        int quantity_can=cont.quant(curr.getId(), q);
        double discount=cont.get_margin_price(curr.getId(),quantity_can);
        double final_price=cont.getItemPrice(curr.getId(), quantity_can);
        Record toAdd=new Record(curr, supplier_id,order_id, quantity_can, discount, final_price);
        handle_response(serviceFactory.add_to_Periodic_items(order_id, toAdd), true);
    }

}