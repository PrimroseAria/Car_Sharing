package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CarDAO {

    /*final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:./src/carsharing/db/carsharing";
    String sql = null;*/
    private Connection conn;
    String sql;


    public CarDAO(Connection conn) {
        this.conn = conn;

    }


    public void createTable() {



            String sql = "CREATE TABLE IF NOT EXISTS CAR " +
                    "(ID INTEGER not NULL PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR(40) UNIQUE NOT NULL," +
                    "COMPANY_ID INTEGER NOT NULL," +
                    "foreign key (COMPANY_ID) REFERENCES COMPANY(ID))";

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

    public List<String> getCarList(int companyId) {

        List<String> carList = new ArrayList<>();
        String sql = "SELECT * FROM CAR WHERE COMPANY_ID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            ResultSet carResults = pstmt.executeQuery();

            while (carResults.next()) {
                carList.add(carResults.getString("NAME"));
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }

        return carList;
    }

    public void addCar(int companyId) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the car name:");
        String carName = sc.nextLine();


        String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, carName);
            pstmt.setInt(2, companyId);
            pstmt.executeUpdate();
            System.out.println("The car was added!");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public int getCarIdByName(String carName) {

        try (Statement stmt = conn.createStatement()) {

            sql = "SELECT * FROM CAR WHERE NAME = '" + carName + "'";

            ResultSet carIdResultSet = stmt.executeQuery(sql);

            if (carIdResultSet.next()) {
                return carIdResultSet.getInt("ID");
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return -1;


    }

    public String getCarNameById(int carId) {

        try (Statement stmt = conn.createStatement()) {

            sql = "SELECT * FROM CAR WHERE ID = '" + carId + "'";

            ResultSet carIdResultSet = stmt.executeQuery(sql);

            if (carIdResultSet.next()) {
                return carIdResultSet.getString("NAME");
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;


    }

    public int getCarCompanyIdById(int carId) {

        try (Statement stmt = conn.createStatement()) {

            sql = "SELECT * FROM CAR WHERE ID = '" + carId + "'";

            ResultSet carIdResultSet = stmt.executeQuery(sql);

            if (carIdResultSet.next()) {
                return carIdResultSet.getInt("COMPANY_ID");
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return -1;


    }



    /*public void close() {

        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }

        } catch(SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();

        }
    }*/

}
