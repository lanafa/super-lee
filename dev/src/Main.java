import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.module.ModuleDescriptor.Exports;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Backend.BusinessLayer.Controllers.DataBaseController;
import Backend.ServiceLayer.ServiceFactory;

public class Main {
    static  ServiceFactory  store=new ServiceFactory();

         public static void main(String[] args) throws SQLException, IOException {
        STOCKUI a = null;
        CLUI c = null;
        
         BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
         int menu;

        try {
            do {
                System.out.println("Please choose an option:");
                System.out.println("1. Stock");
                System.out.println("2. Supllier");
                System.out.println("3. deleteData");
                System.out.println("4. LoadData");
                System.out.println("0. Exit");
              
            

                menu = Integer.parseInt(reader.readLine());

                switch (menu) {
                    case 1:
                    if(a == null){
                        a=new STOCKUI();
                    } 
                        a.start();
                        break;

                    case 2:
                    if(c == null){
                   c =new CLUI(store);
                    }
                    c.start();
                        
                    break;
                    case 3:

                    DataBaseController.getInstance().deleteData();
                    break;
                case 4:
                    DataBaseController.getInstance().LoadData();
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
        }  finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    }

