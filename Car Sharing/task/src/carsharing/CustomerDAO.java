package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerDAO {

    Connection conn;
    String sql;


    public CustomerDAO(Connection connect) {
        this.conn = connect;
    }

    public void createTable() {


        String sql = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                "(ID INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "NAME VARCHAR(30) UNIQUE NOT NULL," +
                "RENTED_CAR_ID INTEGER UNIQUE," +
                "foreign key (RENTED_CAR_ID) REFERENCES CAR(ID))";


        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
    }

    public void createCustomer() {
        Scanner sc = new Scanner(System.in);

        try (Statement stmt = conn.createStatement()) {
            System.out.println("Enter the customer name:");
            String name = sc.nextLine();
            sql = "INSERT INTO CUSTOMER(NAME) VALUES('" + name + "')";
            stmt.executeUpdate(sql);
            System.out.println("The customer was added!");

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public List<String> getCustomerList() {

        List<String> customerList = new ArrayList<>();
        String sql = "SELECT * FROM CUSTOMER;";

        try (Statement stmt = conn.createStatement()) {
            ResultSet customerResults = stmt.executeQuery(sql);
            while (customerResults.next()) {
                customerList.add(customerResults.getString("NAME"));
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }

        return customerList;
    }

    public int getCustomerCarIdByName(String customerName) {

        try (Statement stmt = conn.createStatement()) {

            sql = "SELECT * FROM CUSTOMER WHERE NAME = '" + customerName + "'";

            ResultSet customerIdResultSet = stmt.executeQuery(sql);

            if (customerIdResultSet.next()) {

                return customerIdResultSet.getInt("RENTED_CAR_ID");
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return -1;
    }

    public int getCustomerIdByName(String customerName) {

        try (Statement stmt = conn.createStatement()) {

            sql = "SELECT * FROM CUSTOMER WHERE NAME = '" + customerName + "'";

            ResultSet customerIdResultSet = stmt.executeQuery(sql);

            if (customerIdResultSet.next()) {

                return customerIdResultSet.getInt("ID");
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return -1;
    }

    public boolean rentCar(int customerId, int carId) {

        String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, carId);
            pstmt.setInt(2, customerId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException se) {
            return false;
        }
    }

    public void returnCar(int customerId) {

        String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = null WHERE ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
        }

    }

    public boolean isCarAlreadyRented(int carId) {

        String sql = "SELECT COUNT(*) FROM CUSTOMER WHERE RENTED_CAR_ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, carId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;
    }

}
