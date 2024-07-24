package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CompanyDAO {

    private Connection conn;

    String sql;

    public CompanyDAO(Connection conn) {
        this.conn = conn;

    }

    public void createTable() {


        String sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
                "(ID INTEGER not NULL PRIMARY KEY AUTO_INCREMENT," +
                "NAME VARCHAR(30) UNIQUE NOT NULL)";


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

    public List<String> getCompanyList() {

        List<String> companyList = new ArrayList<>();
        String sql = "SELECT * FROM COMPANY;";

        try (Statement stmt = conn.createStatement()) {
            ResultSet companyResults = stmt.executeQuery(sql);
            while (companyResults.next()) {
                companyList.add(companyResults.getString("NAME"));
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }

        return companyList;
    }

    public void createCompany() {
        Scanner sc = new Scanner(System.in);

        try (Statement stmt = conn.createStatement()) {
            System.out.println("Enter the company name:");
            String name = sc.nextLine();
            sql = "INSERT INTO COMPANY(NAME) VALUES('" + name + "')";
            stmt.executeUpdate(sql);
            System.out.println("The company was created!");

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public int getCompanyIdByName(String companyName) {

        try (Statement stmt = conn.createStatement()) {

            sql = "SELECT * FROM COMPANY WHERE NAME = '" + companyName + "'";

            ResultSet companyIdResultSet = stmt.executeQuery(sql);

            if (companyIdResultSet.next()) {
                return companyIdResultSet.getInt("ID");
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return -1;


    }

    public String getCompanyNameById(int companyId) {

        try (Statement stmt = conn.createStatement()) {

            sql = "SELECT * FROM COMPANY WHERE ID = '" + companyId + "'";

            ResultSet carIdResultSet = stmt.executeQuery(sql);

            if (carIdResultSet.next()) {
                return carIdResultSet.getString("NAME");
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return null;
    }
    /*public void close() {

        try {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();

        }
    }*/

}
