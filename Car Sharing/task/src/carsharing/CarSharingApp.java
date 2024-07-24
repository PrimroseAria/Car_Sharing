package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class CarSharingApp {

    public CompanyDAO company;
    public CarDAO cars;
    public CustomerDAO customer;
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";
    String sql;
    Connection conn;
    Statement stmt;

    public CarSharingApp() {

        try {
            final String JDBC_DRIVER = "org.h2.Driver";
            final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            company = new CompanyDAO(conn);     // Creates object for Company that gets the database connection
            cars = new CarDAO(conn);            // Creates object for Cars that gets the database connection
            customer = new CustomerDAO(conn);   // Creates object for Customer that gets the database connection
            company.createTable();  // Ensure the COMPANY table is created
            cars.createTable();     // Ensure the CAR table is created
            customer.createTable(); // Ensure the CUSTOMER table is created
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void manager() {

    }

    public void menu() {

        Scanner sc = new Scanner(System.in);


        loop1:
        while (true) {

            System.out.println("\n1. Log in as a manager \n2. Log in as a customer \n3. Create a customer \n0. Exit");

            switch (sc.next()) {

                case "1", "Log in", "Manager":
                    //company.createConnection();

                    loop2:
                    while (true) {


                        System.out.println("\n1. Company list \n2. Create a company \n0. Back");

                        switch (sc.next()) {

                            case "1", "Company list", "company list":
                                System.out.println("\nChoose the company:");

                                List<String> companyList = company.getCompanyList();

                                if (!companyList.isEmpty()) {
                                    int counter = 1;


                                    for (String company : companyList) {
                                        System.out.println(counter + ". " + company);
                                        counter++;
                                    }

                                    System.out.println("0. Back");
                                    int companyChoice = sc.nextInt();
                                    if (companyChoice == 0) {
                                        break;

                                    } else {
                                        String selectedCompanyName = companyList.get(companyChoice - 1);

                                        System.out.println("'" + selectedCompanyName + "' company");

                                        carsCompanyMenu(selectedCompanyName);
                                    }

                                } else {
                                    System.out.println("The company list is empty!");
                                }
                                break;

                            case "2", "Create", "create":


                                company.createCompany();
                                break;

                            case "0", "Back", "back":

                                break loop2;

                        }
                    }

                    break;

                case "2":

                    List<String> customerList = customer.getCustomerList();


                    if (!customerList.isEmpty()) {
                        int counter = 1;
                        System.out.println("\nChoose a customer:");


                        for (String customer : customerList) {
                            System.out.println(counter + ". " + customer);
                            counter++;
                        }

                        System.out.println("0. Back");
                        int customerChoice = sc.nextInt();
                        if (customerChoice == 0) {
                            break;

                        } else {
                            String selectedCustomerName = customerList.get(customerChoice - 1);

                            //System.out.println("'" + selectedCustomerName + "' company");

                            customerMenu(selectedCustomerName);
                        }

                    } else {
                        System.out.println("The customer list is empty!");
                    }


                    break;

                case "3":
                    customer.createCustomer();

                    break;

                case "0", "Exit", "exit":

                    close();
                    System.exit(0);
            }
        }


    }

    public void carsCompanyMenu(String companyName) {

        Scanner sc = new Scanner(System.in);

        int foreignKeyCompany = company.getCompanyIdByName(companyName);

        loop3:
        while (true) {
            System.out.println("\n1. Car list \n2. Create a car \n0. Back");

            switch (sc.next()) {
                case "1":


                    List<String> carList = cars.getCarList(foreignKeyCompany);

                    if (!carList.isEmpty()) {
                        int counter = 1;


                        for (String company : carList) {
                            System.out.println(counter + ". " + company);
                            counter++;
                        }


                    } else {
                        System.out.println("The car list is empty!");
                    }

                    break;

                case "2":

                    cars.addCar(foreignKeyCompany);
                    break;

                case "0":

                    break loop3;
            }
        }
    }

    public void carsCustomerMenu(String companyName, int customerId) {

        Scanner sc = new Scanner(System.in);

        int foreignKeyCompany = company.getCompanyIdByName(companyName);

        loopCarsCustomer:
        while (true) {

            List<String> carList = cars.getCarList(foreignKeyCompany);

            if (!carList.isEmpty()) {
                int counter = 1;
                System.out.println("\nChoose a car:");


                /*for (String car : carList) {
                    if (customer.isCarAlreadyRented(cars.getCarIdByName(car))) {
                        carList.remove(car);
                    } else {
                        System.out.println(counter + ". " + car);
                        counter++;
                    }
                }*/

                Iterator<String> iterator = carList.iterator();
                while (iterator.hasNext()) {
                    String car = iterator.next();
                    if (customer.isCarAlreadyRented(cars.getCarIdByName(car))) {
                        iterator.remove();
                    } else {
                        System.out.println(counter + ". " + car);
                        counter++;
                    }
                }

                System.out.println("0. Back");
                int carChoice = sc.nextInt();
                if (carChoice == 0) {
                    break;

                } else {

                    String selectedCarName = carList.get(carChoice - 1);
                    int foreignKeyCar = cars.getCarIdByName(selectedCarName);

                    // add id of car inside the customer as the rented_CAR_ID
                    boolean rented = customer.rentCar(customerId, foreignKeyCar);

                    if (rented) {
                        System.out.println("You rented '" + cars.getCarNameById(foreignKeyCar) + "'");
                    } else {
                        System.out.println("You werent able to rent " + cars.getCarNameById(foreignKeyCar));
                    }

                    break;

                }


            } else {
                System.out.println("The car list is empty!");
                break;
            }

        }
    }

    public void customerMenu(String customerName) {
        Scanner sc = new Scanner(System.in);

        customerLoop:
        while (true) {
            System.out.println("\n1. Rent a car \n2. Return a rented car \n3. My rented car \n0. Back");

            switch (sc.next()) {

                case "1":

                    if (customer.getCustomerCarIdByName(customerName) > 0) {
                        System.out.println("You've already rented a car!");
                        //System.out.println(customer.getCustomerCarIdByName(customerName));

                    } else {
                        System.out.println("\nChoose a company:");

                        List<String> companyList = company.getCompanyList();

                        if (!companyList.isEmpty()) {
                            int counter = 1;


                            for (String company : companyList) {
                                System.out.println(counter + ". " + company);
                                counter++;
                            }

                            System.out.println("0. Back");
                            int companyChoice = sc.nextInt();
                            if (companyChoice == 0) {
                                break;

                            } else {
                                String selectedCompanyName = companyList.get(companyChoice - 1);

                                //System.out.println("'" + selectedCompanyName + "' company");
                                int customerId = customer.getCustomerIdByName(customerName);

                                carsCustomerMenu(selectedCompanyName, customerId);
                            }
                        } else {
                            System.out.println("The company list is empty!");
                        }
                    }


                    break;


                case "2":

                    if (customer.getCustomerCarIdByName(customerName) > 0) {
                        customer.returnCar(customer.getCustomerIdByName(customerName));
                        System.out.println("You've returned a rented car!");

                    } else {
                        System.out.println("You didn't rent a car!");
                    }

                    break;


                case "3":

                    if (customer.getCustomerCarIdByName(customerName) > 0) {
                        System.out.println("Your rented car:");
                        System.out.println(cars.getCarNameById(customer.getCustomerCarIdByName(customerName)));
                        System.out.println("Company:");
                        System.out.println(company.getCompanyNameById(cars.getCarCompanyIdById(customer.getCustomerCarIdByName(customerName))));

                    } else {
                        System.out.println("You didn't rent a car!");
                    }

                    break;


                case "0":

                    break customerLoop;

            }

        }

    }


    public void close() {

        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

}


// assign the company names to an index based on its spot in the menu
// use company names to get id of company in ResultSet
// use id from company ResultSet as foreign key indentifier for which cars belong to said company